package main

/*
structure definitions
drop any structures that will be used here
*/

import (
	"net/http"
)

// empty struct when no data is needing to be passed
type Empty struct{}

var EmptyInter []interface{}

// routing information for each page
type Route struct {
	Name       string
	Method     string
	Pattern    string
	HandleFunc http.HandlerFunc
}

type ErrorMessage struct {
	Error string
}

type Container []interface{}

/*
Structures for querying the db, each struct has a coresponding tag of the db
row name, should be filled automatically by sqlx
*/
type User struct {
	Name       string `db:"Name"      `
	Folder     string `db:"Folder"    json:"-"`
	SessionKey string `db:"SessionKey"json:"-"`
	Hash       string `db:"Hash"      json:"-"`
	Time       int64  `db:"Time"      json:"-"`
	Temp       bool   `db:"Temp"      `
}

type Programs struct {
	Folder string `db:"Folder"  `
	Files  string `db:"Files"   `
}

type Command struct {
	Name     string `db:"Name"    `
	ProgType string `db:"ProgType"`
}

type Stored struct {
	UserName string `db:"UserName"`
	Folder   string `db:"Folder"  `
	ProgName string `db:"ProgName"`
	Files    string `db:"Files"   `
	Viewed   bool   `db:"Viewed"  `
	Time     int64  `db:"Time"    `
	Temp     bool   `db:"Temp"    `
}

/*
structs for data hand offs
to be converted to and from JSON
*/
type Result struct {
	Name    string
	Results []File
}

type Results struct {
	Name   string
	Status string
	Type   string
	Viewed bool
	Time   int64
}

type File struct {
	Name string
	Data string
}

type Program struct {
	Name    string
	Results []File
}

type Submit struct {
	Dir      string
	Name     string
	Commands []Cmd
}

type Cmd struct {
	Program  string
	ProgType string
	Input    []string
}

type ErrorVal struct {
	Error string
}
