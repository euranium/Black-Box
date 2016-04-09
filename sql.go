package main

import (
	"database/sql"
	"fmt"
	_ "github.com/mattn/go-sqlite3"
)

var (
	db *sql.DB
)

func Init() {
	db, err := sql.Open("sqlite3", "./data.db")
	if err != nil {
		fmt.Println("error w/ db open:", err.Error())
	}
	smt := `
	Create Table If Not Exists Users (
		name 	Text,
		hash 	Text,
		folder 	Text Unique,
		time 	Real
	);
	`
	_, err = db.Exec(smt)
	if err != nil {
		fmt.Println("exec stmt err:", err.Error())
	}
}

func Close() {
	defer db.Close()
}
