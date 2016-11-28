package main

import (
	"github.com/gorilla/mux"
)

type Routes []Route

/*
generate a new router from RouteList bellow
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
		"people",
		"GET",
		"/people",
		people,
	},
	Route{
		"publications",
		"GET",
		"/publications",
		publications,
	},
	Route{
		"userHome",
		"GET",
		"/dashboard",
		dashboard,
	},
	Route{
		"contact",
		"GET",
		"/contact",
		contact,
	},
	Route{
		"science",
		"GET",
		"/science",
		science,
	},
	Route{
		"quickStart",
		"GET",
		"/quickstart",
		quickstart,
	},
	Route{
		"poster",
		"GET",
		"/poster",
		poster,
	},
	Route{
		"register",
		"GET",
		"/register",
		register,
	},
	Route{
		"register",
		"POST",
		"/api/register",
		APIRegister,
	},
	Route{
		"API",
		"GET",
		"/api/listsoftware",
		APIListSoftware,
	},
	Route{
		"API",
		"GET",
		"/api/template/{key}",
		APITemplate,
	},
	Route{
		"API",
		"GET",
		"/api/results",
		APIListResults,
	},
	Route{
		"API",
		"GET",
		"/api/results/{name}",
		APIGetResults,
	},
	Route{
		"API",
		"POST",
		"/api/submit",
		APISubmitForm,
	},
	Route{
		"API",
		"GET",
		"/api/loggedIn",
		APILoggedIn,
	},
	Route{
		"API",
		"GET",
		"/api/delete/{name}",
		APIDelete,
	},
}
