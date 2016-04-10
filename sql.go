package main

import (
	"database/sql"
	"fmt"
	"github.com/kisielk/sqlstruct"
	"github.com/mattn/go-sqlite3"
)

/*
file referance to the db being access
global reference to sql prepared statements to user
*/
var (
	db      *sql.DB
	GetUser = "select * from Users where name=?"
)

/*
initialize sql database connection
*/
func DBInit() {
	var dbDriver string
	var err error
	sql.Register(dbDriver, &sqlite3.SQLiteDriver{})
	db, err = sql.Open(dbDriver, "./data.db")
	if err != nil {
		fmt.Println("error w/ db open:", err.Error())
	}

	// ping bc I need to do something w/ db var to compile
	err = db.Ping()
	if err != nil {
		fmt.Println("error w/ db ping:", err.Error())
	}
	go Handler()
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
*/
func DBread(prep string, args []interface{}) (results []Table, err error) {
	fmt.Println("prep:", prep, "args:", args)
	stmt, err := db.Prepare(prep)
	if err != nil {
		fmt.Println("error: prepare", err.Error())
		return
	}
	rows, err := stmt.Query(args...)
	if err != nil {
		fmt.Println("error: stmt", err.Error())
		return
	}
	defer rows.Close()
	table := new(Table)
	for rows.Next() {
		fmt.Println("sample before:", table)
		err = sqlstruct.Scan(table, rows)
		if err != nil {
			fmt.Println("error: struct", err.Error())
			return
		}
		fmt.Println("sample after:", table)
		results = append(results, *table)
	}
	return
}

/*
insert values into a the db given a prepared statement and arguments
will return the error thown
*/
func DBwrite(pref string, args []string) (err error) {
	stmt, err := db.Prepare(pref)
	if err != nil {
		fmt.Println("error: prepare", err.Error())
		return
	}
	// dont need to capture result right now
	_, err = stmt.Exec(args)
	return
}
