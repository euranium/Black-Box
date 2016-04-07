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
