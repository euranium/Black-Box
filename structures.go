package main

import (
	"net/http"
)

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

type example struct {
	menu []food
}

type food struct {
	name        string
	price       string
	description string
	calories    int
}
