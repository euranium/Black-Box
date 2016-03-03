package main

import (
	"github.com/gorilla/mux"
)

type Routes []Route

/*
generate a new router from RouteList
to edit or for more information, go to routes.go
*/
func NewRouter() *mux.Router {
	router := mux.NewRouter().StrictSlash(true)
	for _, route := range RouteList {
		router.
			Methods(route.Method).
			Path(route.Pattern).
			Name(route.Name).
			Handler(route.HandleFunc)
	}
	return router
}

/*
list of routes, their methods, paths and hanlder funcs
*/
var RouteList = Routes{
	Route{
		"Index",
		"GET",
		"/",
		home,
	},
	Route{
		"login",
		"GET",
		"/login",
		login,
	},
	Route{
		"login",
		"POST",
		"/login",
		checkLogin,
	},
	Route{
		"programs",
		"GET",
		"/programs",
		prog,
	},
	Route{
		"program",
		"GET",
		"/programs/{prog}",
		program,
	},
	Route{
		"testInput",
		"POST",
		"/programs/sampleProgs",
		testInput,
	},
	Route{
		"progInput",
		"POST",
		"/programs/{prog}",
		progInput,
	},
	Route{
		"userFiles",
		"GET",
		"/user/{user}/files",
		files,
	},
	Route{
		"news",
		"GET",
		"/news",
		news,
	},
	Route{
		"publications",
		"GET",
		"/",
		publications,
	},
	Route{
		"people",
		"GET",
		"/people",
		people,
	},
	Route{
		"userHome",
		"GET",
		"/userHome",
		userHome,
	},
}
