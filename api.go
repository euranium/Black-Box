package main

import (
	"encoding/json"
	"fmt"
	"github.com/fatih/structs"
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
	//fmt.Println(string(b))
	w.Write([]byte(b))
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
	// TODO: format data
	w.Write([]byte(file))
	return
}

/*
parse data submited to run a project and execute said project, no restrictions
*/
func APISubmitForm(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	person, err := IsLoggedIn(w, r)
	if err != nil {
		SendError(w, "Error Processing User")
		return
	}
	fmt.Println("person is:", person)
	// form is a map["form json"] => "", for some reason, need key to parse
	str := ""
	for k, _ := range r.Form {
		if str == "" {
			str = k
		}
	}

	// parse json bytes to struct
	var input Submit
	dec := json.NewDecoder(strings.NewReader(str))
	err = dec.Decode(&input)
	if err != nil {
		SendError(w, "Error Processing Information")
		return
	}

	//fmt.Println("decoded:", input)
	// check if the user has a directory, if not create one
	if person.Folder == "" && person.Temp {
		fmt.Println("creating Temp user")
		err = SaveTemp(w, r, person)
		if err != nil {
			fmt.Println("err:", err.Error())
			SendError(w, "Error Setting Up Program")
			return
		}
	}

	// create a new folder
	//t := time.Now().Format("2006-Jan-02_15:04:05")
	base := RandomString(12)
	dir := path.Join(UserDir, person.Folder, base)
	err = CopyDir(filepath.Join(progDir, input.Name), dir)
	if err != nil {
		SendError(w, "Error Setting Up Program")
		fmt.Println("error copy:", err.Error())
		return
	}
	var command []string
	command = append(command, "java", input.Name)
	command = append(command, input.Input...)
	command = append(command, dir)
	// save run to db
	err = DBWriteMap(InsertRun, structs.Map(Stored{
		person.Name, base, input.Name, "", false, time.Now().Unix(), person.Temp,
	}))
	if err != nil {
		SendError(w, "Error Running Program")
		fmt.Println("error insert:", err.Error())
		return
	}
	Tasks <- command
	w.Write([]byte(fmt.Sprintf(`{}`)))
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
given a request with query?name=name, query the db for the folder,
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
	//fmt.Println(string(b))
	w.Write(b)
	return
}

/*
upload zip of folder
*/
func APIDownload(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		SendError(w, err.Error())
		return
	}
	r.ParseForm()
	name := r.Form["name"][0]
	var result Stored
	var args []interface{}
	args = append(args, name)
	args = append(args, user.Name)
	fmt.Println(args)
	err = DBReadRow(QueryRun, args, &result)
	//fmt.Println("name:", name)
	if err != nil {
		fmt.Println("error:", err.Error())
		SendError(w, err.Error())
		return
	}
}
