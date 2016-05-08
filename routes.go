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
		"login",
		"POST",
		"/login",
		Login,
	},
	Route{
		"login",
		"GET",
		"/login",
		loginPage,
	},
	Route{
		"login",
		"GET",
		"/logout",
		Logout,
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
		"/publications",
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
		"/dashboard",
		dashboard,
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
}
