package main

import ()

type Routes []Route

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
		"user",
		"GET",
		"/user/{user}",
		users,
	},
	Route{
		"userFiles",
		"GET",
		"/user/{user}/files",
		files,
	},
	Route{
		"userFile",
		"GET",
		"/user/{user}/files/{file}",
		file,
	},
}
