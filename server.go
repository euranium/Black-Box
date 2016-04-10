package main

import (
	"errors"
	"flag"
	"fmt"
	"github.com/gorilla/sessions"
	"net/http"
	"os"
	"os/signal"
	"path"
)

/*
global variables for directory information,
Tasks channel for program execution (more info in cmdLine.go),
Signal channel for ^C signal interupt handling
store for cookie and session handling
*/
var (
	empty       Empty
	waitOn      = 1
	UserDir     = "users/"
	templateDir = "templates/"
	progDir     = "executables/"
	Tasks       = make(chan []string, 64)
	Signal      = make(chan os.Signal, 1)
	store       = sessions.NewCookieStore([]byte("something-secret-or-not"))
)

func main() {
	r := NewRouter()
	store.Options = &sessions.Options{
		Path: "/",
	}

	// start routine to handle program execution
	go RunCmd()

	// serve static files for stuff like css, js , imgs from public folder
	r.PathPrefix("/").Handler(http.FileServer(http.Dir("./public/")))
	http.Handle("/", r)

	// pass opt flag -port=# to specify an operating port
	flgs := flag.String("port", "8080", "a string")
	flag.Parse()
	fmt.Println("running on port:", *flgs)
	port := ":" + *flgs

	// signal handling
	signal.Notify(Signal, os.Interrupt)
	// db initilization
	DBInit()

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
check is a user is logged in/valid
TODO: add actuall auth, link with db
*/
func IsLoggedIn(w http.ResponseWriter, r *http.Request) (person *User, err error) {
	ses, err := store.Get(r, "user")
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	if ses.Values["user_name"] == nil || ses.Values["id"] == nil {
		http.Redirect(w, r, "/login", 302)
		return nil, errors.New("Session Error")
	}
	name := ses.Values["user_name"].(string)
	id := ses.Values["id"].(string)
	if id == "" {
		http.Redirect(w, r, "/login", 302)
		return nil, errors.New("Session Error")
	}
	if name == "" {
		name = id
	}
	person = &User{}
	(*person).user_name = name
	(*person).hash = id
	if !CheckDir(path.Join(UserDir, (*person).hash)) {
		fmt.Println("not a valid user")
		http.Redirect(w, r, "/login", 302)
		return nil, errors.New("Not logged In")
	}
	return
}

/*
post from login
*/
func Login(w http.ResponseWriter, r *http.Request) {
	ses, err := store.Get(r, "user")
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	r.ParseForm()
	id := r.FormValue("id")
	name := r.FormValue("name")
	if name == "" {
		name = id
	}

	// query db for user
	var args []interface{}
	args = append(args, name)
	results, err := DBread(GetUser, args)
	// if err or no matching results
	if err != nil || len(results) <= 0 {
		fmt.Println("err:", err.Error())
		http.Redirect(w, r, "/dashboard", 302)
		return
	}
	fmt.Println("name:", results[0].name)
	/*
		exists := CheckDir(path.Join(UserDir, id))
		if !exists {
			http.Redirect(w, r, "/login", 302)
			return
		}
	*/
	ses.Values["id"] = id
	ses.Values["user_name"] = id
	err = ses.Save(r, w)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	http.Redirect(w, r, "/dashboard", 302)
	return
}

/*
home page
*/
func home(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "home.html"))
	if err != nil {
		fmt.Println("error:", err.Error())
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
	return
}

/*
dashboard page
*/
func dashboard(w http.ResponseWriter, r *http.Request) {
	person, err := IsLoggedIn(w, r)
	if err != nil || (*person).hash == "" {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	file, err := ReadFile(path.Join(templateDir, "dashboard.html"))
	if err != nil {
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
}

/*
login page
*/
func loginPage(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "login.html"))
	if err != nil {
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
	return
}

func news(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "login.html"))
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

func people(w http.ResponseWriter, r *http.Request) {
	file, err := ReadFile(path.Join(templateDir, "people.html"))
	if err != nil {
		w.Write([]byte("error"))
		return
	}
	w.Write(file)
	return
}
