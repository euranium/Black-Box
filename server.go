package main

import (
	"bytes"
	"fmt"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"os/exec"
	"strings"
)

type User struct {
	user_name  string
	first_name string
	last_name  string
	hash       string
}

func main() {
	fmt.Println("main")
	r := mux.NewRouter()
	r.HandleFunc("/", home)
	r.HandleFunc("/login", login)
	r.HandleFunc("/{user}", users)
	r.HandleFunc("/{user}/files", files)
	r.HandleFunc("/{user}/files/{id}", file)
	http.Handle("/", r)
	http.ListenAndServe(":8080", r)
}

// home site
func home(w http.ResponseWriter, r *http.Request) {
	cmd := exec.Command("python3", "api.py", "2", "2")
	cmd.Stdin = strings.NewReader("input")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
		log.Fatal(err)
	}
	result := fmt.Sprintf("api return %s", out.String())
	w.Write([]byte(result))
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
