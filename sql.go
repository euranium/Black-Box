package main

import (
	//"database/sql"
	"fmt"
	"github.com/jmoiron/sqlx"
	_ "github.com/mattn/go-sqlite3"
	"log"
)

/*
file referance to the db being access
global reference to sql prepared statements to user
*/
var (
	db *sqlx.DB
	// add user
	InsertUser = `Insert Into Users (Name,Folder,Hash,Time,Temp) Values
	(:Name,:Folder,:Hash,:Time,:Temp)`
	// get one user
	QueryUser = "Select * from Users where name=$1"
	// get all programs
	QueryPrograms = "Select Folder, Name, ProgType, Files from Programs"
	// get info on one program
	QueryProgram = `Select Folder, Name, ProgType, Files from Programs
	where Name=$1`
	// get results given a folder and username
	QueryRun = `Select Folder, UserName, ProgName, Files, Viewed, Time, Temp from Stored
	Where Folder=$1`
	// get all results associated w/ a user
	QueryRuns = `Select Folder, ProgName, Files, Viewed, Time, Temp from Stored
	Where UserName=$1`
	// get all completed results
	QueryCompleted = `Select Folder, UserName, ProgName, Files, Time from Stored
	Where UserName=$1 and Files != " "`
	// add a program
	InsertProgram = `Insert Into Programs (Folder,Name,ProgType,Files)
	Values (:Folder,:Name,:ProgType,:Files)`
	// add a program run
	InsertRun = `Insert Into Stored (UserName,Folder,ProgName,Files,Time,Temp)
	Values (:UserName,:Folder,:ProgName,:Files,:Time,:Temp)`
	// update a program run w/ generated files, error message
	UpdateRun = `Update Stored Set Files=$1, Message=$2 Where Folder=$3`
	// set viewed of program to true
	UpdateViewed = `Update Stored Set Viewed=1 Where Folder=$1`
	// update last time user has checked in
	UpdateUserTime = `Update Users Set Time=$1 Where Name=$2`
	// update what user session value is, normally for logging out
	UpdateUserSession = `Update users Set SessionKey=$1, Time=$2 Where Name=$3`
)

/*
initialize and test sql database connection
*/
func DBInit() {
	var err error
	db, err = sqlx.Connect("sqlite3", "./data.db")
	if err != nil {
		fmt.Println("error w/ db open:", err.Error())
		log.Fatalln(err)
		return
	}
	err = db.Ping()
	if err != nil {
		fmt.Println("error w/ db ping:", err.Error())
		log.Fatalln(err)
		return
	}
	return
}

/*
get information out of the db, parse the data into a structure
read returns an array of restus, readrow returns a single entry
*/
func DBRead(prep string, args []interface{}, container interface{}) (err error) {
	err = db.Select(container, prep, args...)
	return
}
func DBReadRow(prep string, args []interface{}, container interface{}) (err error) {
	err = db.QueryRowx(prep, args...).StructScan(container)
	return
}

/*
insert values into a the db given a prepared statement and arguments
will return the error thown
first is given array of arguments in an interface, second is in a map format
*/
func DBWrite(pref string, args []interface{}) (err error) {
	_, err = db.Exec(pref, args...)
	return
}

func DBWriteMap(pref string, values map[string]interface{}) (err error) {
	_, err = db.NamedExec(pref, values)
	return
}

/*
func parseValues(rows *sql.Rows) (vals map[string]string, err error) {
	vals = make(map[string]string)
	cols, err := rows.Columns()
	if err != nil {
		return
	}
	l := len(cols)
	v := make([]string, l)
	var values []interface{}
	for i := 0; i < len(cols); i++ {
		values = append(values, &v[i])
	}
	err = rows.Scan(values...)
	if err != nil {
		return
	}
	for i := 0; i < l; i++ {
		key := cols[i]
		val := v[i]
		vals[key] = val
	}
	return
}

perform a read (get data) from the db,
@args: prep is a prepared statement, args is the arguments for said stmt,
sample is an empty struct of the data expected
@returns: an array of the data retrieved
func DBquery(prep string, args []interface{}, container Container) (results Container, err error) {
	fmt.Println("prep:", prep, "args:", args)
	stmt, err := db.Prepare(prep)
	if err != nil {
		return
	}
	rows, err := stmt.Query(args...)
	if err != nil {
		return
	}
	defer rows.Close()

	//results := container[0]
	empty := container[0]
	// make struct which will be filled w/ values
	toFill := empty
	var vals map[string]string
	for rows.Next() {
		vals, err = parseValues(rows)
		if err != nil {
			return
		}
		var c Container
		container = append(container, &toFill)
		Fill(vals, c)
		//results = append(results, vals)
		results = append(results, toFill)
	}
	return
}
*/
