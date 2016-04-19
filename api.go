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
list all the software on the server available to execute
*/
func APIListSoftware(w http.ResponseWriter, r *http.Request) {
	var p []Programs
	err := DBRead(QueryPrograms, EmptyInter, &p)
	if err != nil {
		fmt.Println("err query:", err.Error())
		return
	}
	var progs []string
	for _, pr := range p {
		progs = append(progs, pr.Folder)
	}
	b, err := json.Marshal(progs)
	//fmt.Println(string(b))
	w.Write([]byte(b))
}

/*
send a template for a form associated with a project
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
		return
	}
	if p.Files == "" {
		w.Write([]byte("Error: no file found"))
	}
	name = path.Join(progDir, name, name+".html")
	file, err := ReadFile(name)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	// TODO: format data
	w.Write([]byte(file))
	return
}

/*
parse data submited to run a project and execute said project
*/
func APISubmitForm(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	person, err := IsLoggedIn(w, r)
	if err != nil || person == nil {
		return
	}
	// form is a map["form json"] => "", need key to parse
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
		fmt.Println("error decode:", err.Error())
		return
	}
	//fmt.Println("decoded:", input)

	// create a new folder, w/ format date-time_random
	t := time.Now().Format("2006-Jan-02_15:04:05")
	base := t + "_" + RandomString(12)
	dir := path.Join(UserDir, person.Folder, base)
	//fmt.Println("copying to:", dir)
	err = CopyDir(filepath.Join(progDir, input.Name), dir)
	if err != nil {
		fmt.Println("error copy:", err.Error())
		return
	}
	var command []string
	command = append(command, "java", input.Name)
	command = append(command, input.Input...)
	command = append(command, dir)
	// save run to db
	err = DBWriteMap(InsertRun, structs.Map(Stored{
		person.Name, base, input.Name, "", float64(time.Now().Unix()),
	}))
	if err != nil {
		fmt.Println("error insert:", err.Error())
		return
	}
	Tasks <- command
	return
}

/*
list all of the previous program results associated with the user
no params necessary
*/
func APIListResults(w http.ResponseWriter, r *http.Request) {
	person, err := IsLoggedIn(w, r)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	var s []Stored
	var args []interface{}
	args = append(args, person.Name)
	err = DBRead(QueryRuns, args, &s)
	if err != nil {
		fmt.Println("error:", err.Error())
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	fmt.Println("results:", s)
	var list []string
	for _, v := range s {
		list = append(list, v.Folder)
	}
	fmt.Println(list)
	b, err := json.Marshal(list)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
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
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
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
		w.Write([]byte(err.Error()))
		return
	}
	files := ReadFiles(filepath.Join(UserDir, user.Folder, name), strings.Split(result.Files, ","))
	b, err := json.Marshal(Result{name, files})
	if err != nil {
		w.Write([]byte(err.Error()))
	}
	fmt.Println(string(b))
	w.Write(b)
	return
}

/*
upload zip of folder
*/
func APIDownload(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
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
		w.Write([]byte(err.Error()))
		return
	}
}
