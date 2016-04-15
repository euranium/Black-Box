package main

import (
	"database/sql"
	"fmt"
	"github.com/jmoiron/sqlx"
	_ "github.com/kisielk/sqlstruct"
	_ "github.com/mattn/go-sqlite3"
	"log"
)

/*
file referance to the db being access
global reference to sql prepared statements to user
*/
var (
	db            *sqlx.DB
	QueryUser     = "Select * from Users where name=$1"
	QueryPrograms = "Select Folder, Name, ProgType, Files from Programs"
	QueryProgram  = `Select Folder, Name, ProgType, Files from Programs
	where Name=$1`
	InsertProgram = `Insert Into Programs (Folder,Name,ProgType,Files)
	Values (:Folder,:Name,:ProgType,:Files)`
	InsertRun = `Insert Into Stored (UserName,Folder,ProgName,Files,Time)
	Values (:UserName,:Folder,:ProgName,:Files,:Time)`
)

/*
initialize sql database connection
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
	//go Handler()
	return
}

func DBread(prep string, args []interface{}, container Container) (err error) {
	result := container[0]
	err = db.Select(result, prep, args...)
	return
}

/*
insert values into a the db given a prepared statement and arguments
will return the error thown
*/
func DBwrite(pref string, values map[string]interface{}) (err error) {
	_, err = db.NamedExec(pref, values)
	return
}

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
func Handler() {
	select {
	case sig := <-Signal:
		db.Close()
		Close(sig)
		return
	}
}

/*
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
