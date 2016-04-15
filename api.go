package main

import (
	"encoding/json"
	"fmt"
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
	var container Container
	container = append(container, &p)
	var args []interface{}
	err := DBread(QueryPrograms, args, container)
	if err != nil {
		fmt.Println("err query:", err.Error())
		return
	}
	var progs []string
	for _, pr := range p {
		progs = append(progs, pr.Folder)
	}
	b, err := json.Marshal(progs)
	fmt.Println(string(b))
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
	if name == "" || !IsExec(name) {
		w.Write([]byte("not exec or no name" + name))
		return
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
	fmt.Println("form:", r.Form)
	var str string
	for k, _ := range r.Form {
		str = k
	}

	var input Submit
	dec := json.NewDecoder(strings.NewReader(str))
	err = dec.Decode(&input)
	if err != nil {
		fmt.Println("error decode:", err.Error())
		return
	}
	fmt.Println("decoded:", input)

	t := time.Now().Format("2006-Jan-02_15:04:05")
	dir := path.Join(UserDir, person.Folder, t+"_"+RandomString(12))
	fmt.Println("copying to:", dir)
	err = CopyDir(filepath.Join(progDir, input.Name), dir)
	if err != nil {
		fmt.Println("error copy:", err.Error())
		return
	}
	var command []string
	command = append(command, "java", input.Name)
	command = append(command, input.Input...)
	command = append(command, dir)
	//DBwrite(InsertRun, ToMap(Stored{person.Name, dir, input.Name, "", time.Now().Unix()}))
	Tasks <- command
	return
}

/*
list all of the previous programs associated with the user
*/
func APIListResults(w http.ResponseWriter, r *http.Request) {
	person, err := IsLoggedIn(w, r)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	list, err := ListDir(path.Join(UserDir, person.Folder))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	b, err := json.Marshal(list)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	fmt.Println(string(b[:]))
	w.Write(b)
	return
}

// hard coded results page right now
// expecting /query?name=folder
func APIGetResults(w http.ResponseWriter, r *http.Request) {
	user, err := IsLoggedIn(w, r)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	r.ParseForm()
	name := r.Form["name"][0]
	fmt.Println("name:", name)
	results := ReadFileType(filepath.Join(UserDir, user.Folder, name), ".txt")
	if results == nil {
		w.Write([]byte("error"))
	}
	b, err := json.Marshal(Result{name, results})
	if err != nil {
		w.Write([]byte(err.Error()))
	}
	fmt.Println(string(b))
	w.Write(b)
}
