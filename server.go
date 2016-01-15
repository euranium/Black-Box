package main

import (
	"fmt"
	"github.com/gorilla/mux"
	"html/template"
	"io/ioutil"
	"net/http"
	"os/exec"
	"path"
)

var (
	Tasks       = make(chan *exec.Cmd, 64)
	progDir     = "executables/"
	userDir     = "users/"
	templateDir = "templates/"
)

var routes = Routes{}

func main() {
	r := NewRouter()

	go RunCmd()

	http.Handle("/", r)
	http.ListenAndServe(":8080", r)
}

/*
generate a new router from RouteList
*/
func NewRouter() *mux.Router {
	router := mux.NewRouter().StrictSlash(true)
	for _, route := range RouteList {
		router.
			Methods(route.Method).
			Path(route.Pattern).
			Name(route.Name).
			Handler(route.HandleFunc)
	}
	return router
}

func progInput(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("submited form\n"))
}

/*
handle web interface to getting input for programs
*/
func program(w http.ResponseWriter, r *http.Request) {
	// get html template for program
	pth := r.URL.Path[10:]
	temp, err := ioutil.ReadFile(path.Join(progDir, pth, "index.html"))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	tmpl, err := template.New("exec").Parse(string(temp[:]))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	err = tmpl.Execute(w, nil)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
}

/*
list programs that can be run
*/
func prog(w http.ResponseWriter, r *http.Request) {
	// get a list of all programs in a dir
	list, err := ListDir(progDir)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	progs := Programs{list}

	// get the html template and fill it with data
	temp, err := ioutil.ReadFile(path.Join(templateDir, "programs.html"))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	tmpl, err := template.New("programs").Parse(string(temp[:]))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	err = tmpl.Execute(w, progs)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
}

// home site
func home(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("home page\n"))
	return
}

// login page if needed, can just ust user hash instead of auth
func login(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("login\n"))
	return
}

// use home page
func users(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("home\n"))
	return
}

// all the user files
func files(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("files\n"))
	return
}

// specific user file
func file(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("user file\n"))
	return
}

func java30Run(w http.ResponseWriter, r *http.Request) {
	Tasks <- exec.Command("java", path.Join(progDir, "javaProg30Sec"), "rand")
	w.Write([]byte("running 30 sec java\n"))
	return
}

func java30Read(w http.ResponseWriter, r *http.Request) {
	if CheckFile("javaProg30SecOutput.txt") {
		data, err := ioutil.ReadFile("javaProg30SecOutput.txt")
		if err != nil {
			w.Write([]byte("Error retrieving file\n"))
			return
		}
		w.Write(data)
	} else {
		w.Write([]byte("File not found\n"))
	}
	return
}
