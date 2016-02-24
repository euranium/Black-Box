package main

import (
	"errors"
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

	go RunCmd()

	http.Handle("/", r)
	http.ListenAndServe(":8080", r)
}

/*
check is a user is logged in/valid
TODO: add actuall auth, link with db
*/
func isLoggedIn(w http.ResponseWriter, r *http.Request, person *User) error {
	session, err := store.Get(r, "user")
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return err
	}
	val := session.Values["id"]
	if val == nil {
		http.Redirect(w, r, "/login", 302)
		return errors.New("Not logged In")
	}
	ok := false
	if person, ok = val.(*User); !ok {
		http.Redirect(w, r, "/login", 302)
		return errors.New("Session Error")
	}
	if val, err := CheckDir(path.Join(userDir, person.user_name)); err != nil && val {
		http.Redirect(w, r, "/login", 302)
		return errors.New("Not logged In")
	}
	return nil
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

func checkLogin(w http.ResponseWriter, r *http.Request) {
	session, err := store.Get(r, "user")
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	r.ParseForm()
	fmt.Println(r.Form)
	id := r.Form["id"]
	fmt.Println(id)
	exists, err := CheckDir(path.Join(userDir, id[0]))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	if !exists {
		http.Redirect(w, r, "/login", 302)
		return
	}
	user := &User{id[0], id[0]}
	session.Values["id"] = user
	session.Save(r, w)
	http.Redirect(w, r, "/", 302)
	return
}

/*
sample handler
TODO: remove when test page no longer needed
*/
func testInput(w http.ResponseWriter, r *http.Request) {
	// primative user auth checking
	r.ParseForm()
	fmt.Println(r.Form)
	var person = &User{}
	if err := isLoggedIn(w, r, person); err != nil || person.user_name == "" {
		return
	}
	r.ParseForm()
	frm := r.Form["xml"]
	args := []string{"-classpath", "executables/sampleProgs/", "sampleProgV1", path.Join("executables/sampleProgs/", frm[0])}
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
	sendTemplate(w, path.Join(templateDir, "login.tmpl"), "home", empty)
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
