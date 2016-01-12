package main

import (
	"github.com/gorilla/mux"
	"io/ioutil"
	"net/http"
	"os/exec"
    "path"
)

type User struct {
	user_name  string
	first_name string
	last_name  string
	hash       string
}

var (
    Tasks = make(chan *exec.Cmd, 64)
    progDir = "executables"
)

func main() {
	r := mux.NewRouter()

	go RunCmd()

	r.HandleFunc("/", home)
	r.HandleFunc("/login", login)
	r.HandleFunc("/{user}", users)
	r.HandleFunc("/{user}/files", files)
	r.HandleFunc("/{user}/files/{id}", file)

	r.HandleFunc("/java30/read", java30Read)
	r.HandleFunc("/java500/read", java500Read)
	r.HandleFunc("/java30/run", java30Run)
	r.HandleFunc("/java500/run", java500Run)
    r.HandleFunc("/xml", xmlToString);

	http.Handle("/", r)
	http.ListenAndServe(":8080", r)
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

func java500Run(w http.ResponseWriter, r *http.Request) {
	Tasks <- exec.Command("java", "javaProg500Sec", "rand")
	w.Write([]byte("running 500 sec java\n"))
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

func java500Read(w http.ResponseWriter, r *http.Request) {
	if CheckFile("javaProg500SecOutput.txt") {
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

func structToxml(w http.ResponseWriter, r *http.Request) {
}

func xmlToString(w http.ResponseWriter, r *http.Request) {
    if CheckFile("xml/test.xml") {
        data, err := ioutil.ReadFile("xml/test.xml")
        if (err != nil) {
			w.Write([]byte("Error retrieving file\n"))
			return
        }
        w.Write(data)
    } else {
        w.Write([]byte("Error"));
        return
    }
}
