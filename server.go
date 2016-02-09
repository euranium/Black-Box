package main

import (
	"fmt"
	"github.com/gorilla/mux"
	"html/template"
	"net/http"
	"os/exec"
	"path"
)

var (
	Tasks       = make(chan *exec.Cmd, 64)
	progDir     = "executables/"
	userDir     = "users/"
	templateDir = "templates/"
	empty       Empty
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
to edit or for more information, go to routes.go
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

/*
generic template handler
*/
func sendTemplate(w http.ResponseWriter, file, name string, data interface{}) {
	temp, err := ReadFile(file)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	tmpl, err := template.New(name).Parse(string(temp[:]))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	err = tmpl.Execute(w, data)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
}

func testInput(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	frm := r.Form["xml"]
}

/*
handle post data
TODO: make generic:
	- handle any input from pages
	- parse input and parse xml imput requirements
	- handle errors, pass back to correct url
*/
func progInput(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	fmt.Println("%v\n", r.Form)
	w.Write([]byte("submited form\n"))
}

/*
handle web interface to getting input for programs
TODO: better URL grabbing management:
	- parse to make sure no injection
	- integrate db for checking programs, faster and more secure
*/
func program(w http.ResponseWriter, r *http.Request) {
	pth := r.URL.Path[10:]
	sendTemplate(w, path.Join(progDir, pth, "index.tmpl"), "exec", empty)
}

/*
list programs that can be run
TODO: pretty up template
*/
func prog(w http.ResponseWriter, r *http.Request) {
	// get a list of all programs in a dir
	list, err := ListDir(progDir)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	progs := Programs{list}
	sendTemplate(w, path.Join(templateDir, "programs.tmpl"), "programs", progs)
}

/* home site
TODO: pretty up template
*/
func home(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "home.tmpl"), "home", empty)
	return
}

/*
login page
TODO: pretty up template, integrate db, actually do
*/
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

/*
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
*/
