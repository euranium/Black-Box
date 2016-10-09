package main

import (
	"encoding/json"
	"fmt"
	"github.com/fatih/structs"
	"io/ioutil"
	"net/http"
	"path"
	"path/filepath"
	"strings"
	"time"
)

var ()

/*
list all the software on the server available to execute, no restrictions
*/
func APIListSoftware(w http.ResponseWriter, r *http.Request) {
	var p []Programs
	err := DBRead(QueryPrograms, EmptyInter, &p)
	if err != nil {
		fmt.Println("err query:", err.Error())
		SendError(w, "Error Processing Request")
		//DBLogError(err.Error(),w,r)
		return
	}
	progs := make([]string, 0)
	for _, pr := range p {
		progs = append(progs, pr.Folder)
	}
	b, err := json.Marshal(progs)
	if err != nil {
		fmt.Println("err marshal:", err.Error())
		SendError(w, "Error Formating Data")
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(b)
}

/*
send a template for a form associated with a project, no restrictions
*/
func APITemplate(w http.ResponseWriter, r *http.Request) {
	// get folder name
	r.ParseForm()
	if len(r.Form["name"]) <= 0 {
		w.Write([]byte("No Query"))
		return
	}
	name := r.Form["name"][0]
	var p Programs
	var args []interface{}
	args = append(args, name)
	err := DBReadRow(QueryPrograms, args, &p)
	if err != nil {
		fmt.Println("err query:", err.Error())
		SendError(w, "Error Processing Data")
      //DBLogError(err.Error(),w,r)
		return
	}
	if p.Files == "" {
		SendError(w, "Error No Files Found")
		//DBLogError("Error No Files Found",w,r)
	}
	name = path.Join(progDir, name, name+".html")
	file, err := ReadFile(name)
	if err != nil {
		SendError(w, "Error Getting Files")
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write([]byte(file))
	return
}

/*
parse data submited to run a project and execute said project, no restrictions
*/
func APISubmitForm(w http.ResponseWriter, r *http.Request) {
	// parse data into struct
	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		fmt.Println("ready body error", err)
		//DBLogError("err.Error(),w,r)
	}
	var input Submit
	err = json.Unmarshal(body, &input)
	if err != nil {
		fmt.Println("Error parsing json:", err)
		SendError(w, "Error Processing Information")
		//DBLogError(err.Error(),w,r)
		return
	}
	r.ParseForm()
	person, err := IsLoggedIn(w, r)
	if err != nil {
		SendError(w, "Error Processing User")
		//DBLogError("Error Processing User",w,r)
		return
	}

	// verify program is a valid one to run
	for k, v := range input.Commands {
		var args []interface{}
		args = append(args, v.Program)
		var command Command
		err = DBReadRow(QueryCommand, args, &command)
		if err != nil || command.Name == "" {
			fmt.Println("err in prog finding")
			SendError(w, "Error Setting Up Program")
			//DBLogError(err.Error(),w,r)
			return
		}
		input.Commands[k].ProgType = command.ProgType
	}

	// check if the user has a directory, if not create one
	if person.Folder == "" && person.Temp {
		err = SaveTemp(w, r, person)
		if err != nil {
			fmt.Println("err in temp creation:", err.Error())
			SendError(w, "Error Setting Up Program")
			//DBLogError(err.Error(),w,r)
			return
		}
	}

	// create a new folder for program to run in
	base := RandomString(12)
	dir := path.Join(UserDir, person.Folder, base)
	err = CopyDir(filepath.Join(progDir, input.Name), dir)
	if err != nil {
		SendError(w, "Error Setting Up Program")
		fmt.Println("error copy:", err.Error())
		//DBLogErrror(err.Error(),w,r)
		return
	}
	input.Dir = dir
	// save run to db
	run := Stored{person.Name, base, input.Name, "", 0, time.Now().Unix(), person.Temp}
	err = DBWriteMap(InsertRun, structs.Map(run))
	if err != nil {
		SendError(w, "Error Running Program")
		fmt.Println("error insert:", err.Error())
		//DBLogError(err.Error(),w,r)
		return
	}
	Tasks <- input
	b, err := json.Marshal(run)
	if err != nil {
		fmt.Println("err", err.Error())
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(b)
	return
}

/*
list all of the previous program results associated with the user
no params necessary
*/
func APIListResults(w http.ResponseWriter, r *http.Request) {
	person, err := IsLoggedIn(w, r)
	if err != nil {
		SendError(w, "Error Processing User")
		fmt.Printf("Error: %s\n", err.Error())
		//DBLogError(err.Error(),w,r)
		return
	}
	var s []Stored
	var args []interface{}
	results := make([]Results, 0)
	args = append(args, person.Name)
	err = DBRead(QueryRuns, args, &s)
	if err != nil {
		fmt.Printf("querying results: %s\n", err.Error())
		//DBLogError(err.Error(),w,r)
		b, err := json.Marshal(results)
		if err != nil {
			fmt.Println("marshal error:", err.Error())
			SendError(w, "Error Formating Data")
			//DBLogError(err.Error(),w,r)
			return
		}
		w.Write(b)
		return
	}

	for _, v := range s {
		var r Results
		r.Name = v.Folder
		if v.Files == "" {
			r.Status = "pending"
		} else {
			r.Status = "complete"
		}
		if v.Viewed == 0 {
			r.Viewed = false
		} else {
			r.Viewed = true
		}
		r.Type = v.ProgName
		results = append(results, r)
	}

	b, err := json.Marshal(results)
	if err != nil {
		fmt.Println("marshal error:", err.Error())
		SendError(w, "Error Formating Data")
		//go DBLogError(err.Error(), w, r)
		return
	}
	w.Write(b)
	return
}

/*
given a request with api/results/query?name=name, query the db for the folder,
copy the file contents into a struct, and send back in json object
*/
func APIGetResults(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Error Processing User")
		//go DBLogError(err.Error(), w, r)
		return
	}
	// parse url header for program name
	r.ParseForm()
	name := r.Form["name"][0]
	var result Stored
	var args []interface{}
	args = append(args, name)
	//fmt.Println(args)
	err = DBReadRow(QueryRun, args, &result)
	//fmt.Println("name:", name)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Program Not Found")
      //go DBLogError("Program Not Found", w, r)
		return
	}

	// check if file can be accessed by temporary user
	if result.Temp == false && user.Name != result.UserName {
		SendError(w, "Program Not Found")
		//go DBLogError("Program Not Found",w,r)
		return
	}
	if strings.Trim(result.Files, " ") == "" {
		SendError(w, "Program Not Complete")
		//go DBLogError("Program Not Complete",w,r)
		return
	}
	files := ReadFiles(filepath.Join(UserDir, user.Folder, name), strings.Split(result.Files, ","))
	b, err := json.Marshal(Result{result.ProgName, files})
	if err != nil {
		SendError(w, "File Not Found")
		fmt.Printf("Error: %s\n", err.Error())
		//go DBLogError(err.Error(),w,r)
		return
	}

	// record that the user has viewed the program
	if result.Viewed == 0 {
		err = DBWrite(UpdateViewed, args)
		if err != nil {
			fmt.Println("Error:", err.Error())
			SendError(w, "Accessing File")
		//go DBLogError(err.Error(),w,r)
			return
		}
	}
	w.Write(b)
	return
}

/*
get logged in status of user, will respond with only username and temp status
*/
func APILoggedIn(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Error Processing User")
		//DBLogError(err.Error(),w,r)
		return
	}
	b, err := json.Marshal(user)
	if err != nil {
		fmt.Println("erro mashaling:", err.Error())
		SendError(w, "Error Processing User")
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(b)
}

/*
delete a stored result given the name
if storing a logged in user, user must be logged in to delete
else, just delete temp users folder
url: /api/delete/query?name=name
*/
func APIDelete(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Error Processing User")
		//DBLogError(err.Error(),w,r)
		return
	}
	r.ParseForm()
	name := r.Form["name"][0]
	var args []interface{}
	args = append(args, name)
	args = append(args, user.Name)
	err = DBWrite(DeleteRun, args)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "File Not Found")
		//DBLogError(err.Error(),w,r)
		return
	}
}

/*
verify and register a new user, also log them in
*/
func APIRegister(w http.ResponseWriter, r *http.Request) {
	type Message struct {
		Success string
	}
	r.ParseForm()
	// parse input, make sure not empty
	name := r.Form["name"][0]
	pass := r.Form["password"][0]
	person := &User{name, name, RandomString(64), pass, time.Now().Unix(), false}
	// attempt to add to db, should fail if username already taken
	err := DBWriteMap(InsertUser, structs.Map(person))
	if err != nil {
		SendError(w, "Username Taken")
//		DBLogError("Username Taken",w,r)
		return
	}
	CreateUserFolder(person)

	// log in user
	ses, err := store.Get(r, "user")
	if err != nil {
		m=err.Error()
		fmt.Println("error getting session:", m)
		SendError(w, m)
//		DBLogError(m,w,r)
		return
	}
	ses.Values["id"] = person.Name
	ses.Values["session"] = person.SessionKey
	ses.Save(r, w)
	if err != nil {
		m=err.Error()
		fmt.Println("error:", m)
		SendError(w, m)
		//DBLogError(m,w,r)
		return
	}
	b, err := json.Marshal(&Message{"Success"})
	if err != nil {
		m=err.Error()
		fmt.Println("erro mashaling:", m)
		//DBLogError(m,w,r)
		return
	}
	w.Write(b)
}

func SendError(w http.ResponseWriter, msg string) {
	b, err := json.Marshal(&ErrorMessage{msg})
	if err != nil {
		fmt.Println("erro mashaling:", err.Error())
		return
	}
	w.Write(b)
}

/*
logs errors into sql database
queries the client for user information, and the system for time information, inserts into the log schema
*/
func DBLogError(Message string, w http.ResponseWriter, r *http.request) (err error) {
	if r != nil{
	   person, err:=IsLoggedIn(w,r)
	}
      if err != nil{
		   w.write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		   Folder="NULL"
		}
		else{
		   Folder:=&person.folder
		}
	else{
	      Folder="NULL"
	t:=Time.Now().Unix()
	log := ErrorLog{
		Message,
		t,
		Folder
	}
	DBWriteMap(LogError,structs.map(log))
}
	return
}
