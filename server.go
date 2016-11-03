package main

import (
	"flag"
	"fmt"
	"github.com/gorilla/sessions"
	"net/http"
	"os"
	"path"
	"time"
)

/*
global variables for directory information,
Tasks channel for program execution (more info in cmdLine.go),
store for cookie and session handling
TODO: setup some form of secret cookie handling
*/
var (
	empty       Empty
	waitOn      = 1
	UserDir     = "users/"
	templateDir = "templates/"
	progDir     = "executables/"
	Tasks       = make(chan Submit, 64)
	Signal      = make(chan os.Signal, 1)
	store       = sessions.NewCookieStore([]byte("something-secret-or-not"))
	ticker      = time.NewTicker(30 * time.Second)
)

func main() {
	r := NewRouter()
	//store = sessions.NewCookieStore([]byte(RandomString(64)))
	store.Options = &sessions.Options{
		Path: "/",
	}

	// start routine to handle program execution
	go RunCmd()
	go ClearFiles()
	// db initilization
	DBInit()
	AddSoftware()

	// serve static files for stuff like css, js, imgs from public folder
	r.PathPrefix("/").Handler(http.FileServer(http.Dir("./public/")))
	http.Handle("/", r)

	// pass opt flag -port=# to specify an operating port
	flgs := flag.String("port", "8080", "a string")
	flag.Parse()
	fmt.Println("running on port:", *flgs)
	port := ":" + *flgs

	// signal handling
	//signal.Notify(Signal, os.Interrupt)
	// start server
	http.ListenAndServe(port, r)
}

/*
Program to handle waiting on any number of processes, feels kind of janky
currently waiting on program execution
@TODO: maybe later, create a way to handle multiple exiting w/ a channel
*/
func Close(sig os.Signal) {
	waitOn--
	// if nothing else to wait on, exit
	if waitOn == 0 {
		os.Exit(0)
	}
	fmt.Println("not closing, waiting for process")
	// put signal back for next process to close
	Signal <- sig
	return
}

/*
home page
*/
func home(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "/dashboard", 301)
	//file, err := ReadFile(path.Join(templateDir, "home.html"))
	//if err != nil {
	//fmt.Println("error:", err.Error())
	//w.Write([]byte("error"))
	//return
	//}
	//w.Write(file)
	return
}

/*
home page
*/
func registration(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "home.html"))
	if err != nil {
		fmt.Println("error:", err.Error())
		w.Write([]byte("error"))
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(file)
	return
}

/*
dashboard page
*/
func dashboard(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "dashboard.html"))
	if err != nil {
		//DBLogError(err.Error(),w,r)
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
}

func people(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "people.html"))
	if err != nil {
		w.Write([]byte("error"))
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(file)
	return
}

func contact(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "contact.html"))
	if err != nil {
		w.Write([]byte("error"))
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(file)
	return
}

func science(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "science.html"))
	if err != nil {
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
	return
}

func publications(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "publications.html"))
	if err != nil {
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
	return
}

func quickstart(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "quickstart.html"))
	if err != nil {
		w.Write([]byte("error"))
		//DBLogError(err.Error(),w,r)
		return
	}
	w.Write(file)
	return
}
