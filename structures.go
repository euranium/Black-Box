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
	user_name  string
	first_name string
	last_name  string
	hash       string
}

type Programs struct {
	Files []string
}
