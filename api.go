package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"path"
)

var ()

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

func APITemplate(w http.ResponseWriter, r *http.Request) {
	// get folder name
	u := r.URL.Query()
	if len(u["name"]) <= 0 {
		w.Write([]byte("No Query"))
		return
	}
	q := u["name"][0]
	if q == "" {
		w.Write([]byte(""))
		return
	}
	p := path.Join(progDir, q, q+".tmpl")
	if !CheckFile(p) {
		w.Write([]byte("No File found"))
		return
	}
	file, err := ReadFile(p)
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	w.Write([]byte(file))
	return
}

func APIListResults(w http.ResponseWriter, r *http.Request) {
	//person, err := IsLoggedIn(w, r)
	//if err != nil {
	//w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
	//return
	//}
	//list, err := ListDir(path.Join(UserDir, person.hash))
	list, err := ListDir(path.Join(UserDir, "aaa"))
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
func APIGetResults(w http.ResponseWriter, r *http.Request) {
	//person, err := IsLoggedIn(w, r)
	//if err != nil {
	//w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
	//return
	//}
	u := r.URL.Query()
	_, folder := path.Split(r.URL.Path)
	if folder != "sampleProgs" {
		w.Write([]byte(""))
		return
	}
	if len(u["name"]) <= 0 {
		w.Write([]byte("No Query"))
		return
	}
	q := u["name"][0]
	if q == "" || q != "sampleProg" {
		w.Write([]byte(""))
		return
	}
	//result, err := ReadFile(path.Join(UserDir, person.hash, folder, q, "*.txt"))
	result, err := ReadFile(path.Join(UserDir, "aaa", folder, q, "*.txt"))
	if err != nil {
		w.Write([]byte(fmt.Sprintf("Error: %s\n", err.Error())))
		return
	}
	w.Write([]byte(result))
}
