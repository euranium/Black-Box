package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"path"
	"path/filepath"
)

var ()

/*
list all the software on the server available to execute
*/
func APIListSoftware(w http.ResponseWriter, r *http.Request) {
	list, err := ListDir(progDir)
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
	if err != nil || person.user_name == "" {
		return
	}
	fmt.Println("form:", r.Form)

	// get and varify program name
	if len(r.Form["name"]) <= 0 {
		w.Write([]byte("No Query"))
		return
	}
	name := r.Form["name"][0]
	if name == "" || !IsExec(name) {
		w.Write([]byte("not exec or no name" + name))
		return
	}

	// get run time type
	if len(r.Form["type"]) <= 0 {
		w.Write([]byte("No type"))
	}
	typ := r.Form["type"][0]
	if typ == "" {
		w.Write([]byte("No Type"))
		return
	}
	dir := path.Join(UserDir, person.hash, RandomString(12))
	fmt.Println("copying to:", dir)
	err = CopyDir(path.Join("executables", name), dir)
	if err != nil {
		fmt.Println("error:", err.Error())
		w.Write([]byte("Error processing\n"))
	}
	var args []string
	input := Sort(r.Form)
	if typ != "exec" {
		// set program name
		args = append(args, typ)
		args = append(args, name)
	} else {
		// set program exec path using absolute path to program
		abs, err := filepath.Abs(filepath.Dir(os.Args[0]))
		if err != nil {
			fmt.Println("error:", err.Error())
			w.Write([]byte("Error processing\n"))
		}
		args = append(args, filepath.Join(abs, dir, name))
	}
	// append program name, input and directory location
	args = append(args, input...)
	args = append(args, dir)
	fmt.Println(args)
	// send it off to be executed
	Tasks <- (args)
	http.Redirect(w, r, "/dashboard", 302)
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
	list, err := ListDir(path.Join(UserDir, person.hash))
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
	person, err := IsLoggedIn(w, r)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	r.ParseForm()
	q := r.Form["name"][0]
	if q == "" || !IsResult(person.hash, q) {
		w.Write([]byte(""))
		return
	}
	result := ReadFileType(path.Join(UserDir, "aaa", q), ".txt")
	w.Write([]byte(result))
}
