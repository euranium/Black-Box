package main

import (
	"errors"
	"flag"
	"fmt"
	"github.com/gorilla/sessions"
	"html/template"
	"net/http"
	"os/exec"
	"path"
)

var (
	empty       Empty
	userDir     = "users/"
	templateDir = "templates/"
	progDir     = "executables/"
	Tasks       = make(chan *exec.Cmd, 64)
	store       = sessions.NewCookieStore([]byte("something-secret-or-not"))
)

var routes = Routes{}

func main() {
	r := NewRouter()
	store.Options = &sessions.Options{
		Path: "/",
	}

	go RunCmd()

	// serve static files for stuff like css, js , imgs
	r.PathPrefix("/").Handler(http.FileServer(http.Dir("./public/")))
	http.Handle("/", r)

	// pass opt flag -port=# to specify an operating port
	flgs := flag.String("port", "8080", "a string")
	flag.Parse()
	fmt.Println("running on port:", *flgs)
	port := ":" + *flgs

	http.ListenAndServe(port, r)
}

/*
check is a user is logged in/valid
TODO: add actuall auth, link with db
*/
func isLoggedIn(w http.ResponseWriter, r *http.Request) (person *User, err error) {
	ses, err := store.Get(r, "user")
	if err != nil {
		fmt.Println("nil")
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
	if !CheckDir(path.Join(userDir, (*person).hash)) {
		fmt.Println("not a valid user")
		http.Redirect(w, r, "/login", 302)
		return nil, errors.New("Not logged In")
	}
	return
}

/*
generic template handler
*/
func sendTemplate(w http.ResponseWriter, file, name string, data interface{}) {
	temp, err := template.ParseFiles("templates/header.tmpl", file)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	err = temp.ExecuteTemplate(w, name, data)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error executing: %s\n", err.Error())))
		return
	}
}

/*
sample handler
TODO: remove when test page no longer needed
*/
func testInput(w http.ResponseWriter, r *http.Request) {
	// user auth checking
	person, err := isLoggedIn(w, r)
	fmt.Println("checked login")
	if err != nil || person.user_name == "" {
		return
	}
	dir := path.Join(userDir, person.hash, RandomString(12))
	fmt.Println("copying to:", dir)
	err = CopyDir("executables/sampleProgs/", dir)
	if err != nil {
		fmt.Println("error:", err.Error())
		w.Write([]byte("Error processing\n"))
	}
	r.ParseForm()
	frm := r.Form["xml"]
	args := []string{"-classpath", dir, "sampleProgV1", path.Join(dir, frm[0])}
	fmt.Println(args)
	Tasks <- exec.Command("java", args...)
	w.Write([]byte("submited form\n"))
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
	if pth == "" {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	folder := path.Join(progDir, pth)
	if !CheckDir(folder) {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	folder = path.Join(folder, "index.tmpl")
	if !CheckFile(folder) {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	sendTemplate(w, folder, "exec", empty)
}

/*
post from login
*/
func checkLogin(w http.ResponseWriter, r *http.Request) {
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
	exists := CheckDir(path.Join(userDir, id))
	if !exists {
		http.Redirect(w, r, "/login", 302)
		return
	}
	ses.Values["id"] = id
	//TODO: update if user is
	ses.Values["user_name"] = id
	err = ses.Save(r, w)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	http.Redirect(w, r, "/", 302)
	return
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
	progs := List{list}
	sendTemplate(w, path.Join(templateDir, "programs.tmpl"), "content", progs)
}

/* home site
TODO: pretty up template
*/
func home(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "home.tmpl"), "content", empty)
	return
}

func dashboard(w http.ResponseWriter, r *http.Request) {
	person, err := isLoggedIn(w, r)
	if err != nil || (*person).hash == "" {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	folder := path.Join(userDir, person.hash)
	list, err := ListDir(folder)
	if err != nil {
		http.Redirect(w, r, "/programs", 302)
		return
	}
	folders := List{list}
	sendTemplate(w, path.Join(templateDir, "dashboard.tmpl"), "content", folders)
}

/*
login page
TODO: pretty up template, integrate db, actually do
*/
func login(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "login.tmpl"), "content", empty)
	return
}

// all the user files
func files(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("files\n"))
	return
}

func news(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "login.tmpl"), "content", empty)
	return
}

func publications(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "publics.tmpl"), "content", empty)
	return
}

func people(w http.ResponseWriter, r *http.Request) {
	sendTemplate(w, path.Join(templateDir, "people.tmpl"), "content", empty)
	return
}
