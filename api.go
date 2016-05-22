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
		return
	}
	var progs []string
	for _, pr := range p {
		progs = append(progs, pr.Folder)
	}
	b, err := json.Marshal(progs)
	if err != nil {
		fmt.Println("err marshal:", err.Error())
		SendError(w, "Error Formating Data")
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
		return
	}
	if p.Files == "" {
		SendError(w, "Error No Files Found")
	}
	name = path.Join(progDir, name, name+".html")
	file, err := ReadFile(name)
	if err != nil {
		SendError(w, "Error Getting Files")
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
	}
	var input Submit
	err = json.Unmarshal(body, &input)
	if err != nil {
		fmt.Println("Error parsing json:", err)
		SendError(w, "Error Processing Information")
		return
	}
	r.ParseForm()
	person, err := IsLoggedIn(w, r)
	if err != nil {
		SendError(w, "Error Processing User")
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
			return
		}
	}

	// create a new folder for program to run in
	//t := time.Now().Format("2006-Jan-02_15:04:05")
	base := RandomString(12)
	dir := path.Join(UserDir, person.Folder, base)
	err = CopyDir(filepath.Join(progDir, input.Name), dir)
	if err != nil {
		SendError(w, "Error Setting Up Program")
		fmt.Println("error copy:", err.Error())
		return
	}
	input.Dir = dir
	// save run to db
	run := Stored{person.Name, base, input.Name, "", false, time.Now().Unix(), person.Temp}
	err = DBWriteMap(InsertRun, structs.Map(run))
	if err != nil {
		SendError(w, "Error Running Program")
		fmt.Println("error insert:", err.Error())
		return
	}
	Tasks <- input
	b, err := json.Marshal(run)
	if err != nil {
		fmt.Println("err", err.Error())
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
		return
	}
	var s []Stored
	var args []interface{}
	args = append(args, person.Name)
	err = DBRead(QueryRuns, args, &s)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Error Processing Request")
		return
	}
	//fmt.Println("results:", s)
	var results []Results
	for _, v := range s {
		var r Results
		r.Name = v.Folder
		if v.Files == "" {
			r.Status = "pending"
		} else {
			r.Status = "complete"
		}
		r.Type = v.ProgName
		results = append(results, r)
		//list = append(list, v.Folder)
	}
	//fmt.Println(list)
	b, err := json.Marshal(results)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, "Error Formating Data")
		return
	}
	//fmt.Println(string(b[:]))
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
		SendError(w, "File Not Found")
		return
	}

	// check if file can be accessed by temporary user
	if result.Temp == false && user.Name != result.UserName {
		SendError(w, "File Not Found")
		return
	}
	if strings.Trim(result.Files, " ") == "" {
		SendError(w, "Program Not Complete")
		return
	}
	files := ReadFiles(filepath.Join(UserDir, user.Folder, name), strings.Split(result.Files, ","))
	b, err := json.Marshal(Result{name, files})
	if err != nil {
		SendError(w, "File Not Found")
		fmt.Printf("Error: %s\n", err.Error())
		return
	}

	// record that the user has viewed the program
	if !result.Viewed {
		err = DBWrite(UpdateViewed, args)
		if err != nil {
			fmt.Println("Error:", err.Error())
			SendError(w, "Accessing File")
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
		return
	}
	//b, err := json.Marshal(&Logged{user.Name, user.Temp})
	b, err := json.Marshal(user)
	if err != nil {
		fmt.Println("erro mashaling:", err.Error())
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
		return
	}
	CreateUserFolder(person)

	// log in user
	ses, err := store.Get(r, "user")
	if err != nil {
		fmt.Println("error getting session:", err.Error())
		SendError(w, err.Error())
		return
	}
	ses.Values["id"] = person.Name
	ses.Values["session"] = person.SessionKey
	ses.Save(r, w)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, err.Error())
		return
	}
	b, err := json.Marshal(&Message{"Success"})
	if err != nil {
		fmt.Println("erro mashaling:", err.Error())
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
