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

// routing information for each page
type Route struct {
	Name       string
	Method     string
	Pattern    string
	HandleFunc http.HandlerFunc
}

type User struct {
	user_name string
	hash      string
}

type List struct {
	files []string
}

/*
sql data table structs, edit w/ data.sql
*/

type Get interface {
	GetStruct() Table
}
type Table struct {
	name     string  `sql:"name"`
	folder   string  `sql:"folder"`
	hash     string  `sql:"hash"`
	time     float64 `sql:"time"`
	progType string  `sql:"progType"`
	progType string  `sql:"progName"`
	files    string  `sql:"files"`
}

func (t Table) GetStruct() *Table {
	return new(Table)
}

type UserTable struct {
	Table
	name   string  `sql:"name"`
	folder string  `sql:"folder"`
	hash   string  `sql:"hash"`
	time   float64 `sql:"time"`
}

type ProgramsTable struct {
	folder   string
	name     string
	progType string
	files    string
}

type StoredTable struct {
	userName string
	folder   string
	progName string
	output   string
	time     float64
}

/*
structs for data hand offs
to be converted to and from JSON
*/
type Result struct {
	name   string
	status string
	prog   string
}

type File struct {
	name string
	data string
}

type Program struct {
	name    string
	results []File
}

type Submit struct {
	name  string
	input []string
}
