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
	// try and add new user, should fail if username already taken
	InsertUser = `INSERT INTO Users (Name,Folder,SessionKey,Hash,Time,Temp) VALUES
	(:Name,:Folder,:SessionKey,:Hash,:Time,:Temp)`
	// get one user
	QueryUser = "SELECT * FROM Users where name=$1"
	// get every user info
	QueryUsers = `SELECT * FROM Users`
	// get all programs
	QueryPrograms = "SELECT Folder, Files FROM Programs"
	// get times associated with stored results
	QueryProgram = `SELECT Folder, Files FROM Programs WHERE Folder=$1`
	// get program associated with a command
	QueryCommand = `SELECT Name, ProgType FROM Command WHERE Name=$1`
	// get results given a folder and username
	QueryRun = `SELECT Folder, UserName, ProgName, Files, Viewed, Time, Temp FROM Stored
	WHERE Folder=$1`
	// get all results associated w/ a user
	QueryRuns = `SELECT Folder, ProgName, Files, Viewed, Time, Temp FROM Stored
	WHERE UserName=$1`
	// get info on all stored results
	QueryStored = `SELECT Folder, ProgName, Files, Viewed, Time, Temp FROM Stored`
	// get all completed results
	QueryCompleted = `SELECT Folder, UserName, ProgName, Files, Time FROM Stored
	WHERE UserName=$1 and Files != " "`
	// add a program
	InsertProgram = `INSERT INTO Programs (Folder,Files) VALUES (:Folder,:Files)`
	// add command which references another command
	InsertCommand = `INSERT INTO Command (Name,ProgType) VALUES (:Name,:ProgType)`
	// add a program run
	InsertRun = `INSERT INTO Stored (UserName,Folder,ProgName,Files,Time,Temp)
	VALUES (:UserName,:Folder,:ProgName,:Files,:Time,:Temp)`
	// update a program run w/ generated files, error message
	UpdateRun = `UPDATE Stored SET Files=$1, Message=$2, ErrMessage=$3 WHERE Folder=$4`
	// set viewed of program to true
	UpdateViewed = `UPDATE Stored SET Viewed=1 WHERE Folder=$1`
	// update last time user has checked in
	UpdateUserTime = `UPDATE Users SET Time=$1 WHERE Name=$2`
	// update what user session value is, normally for logging out
	UpdateUserSession = `UPDATE users SET SessionKey=$1, Time=$2 WHERE Name=$3`
	// delete stored data
	DeleteRun    = `DELETE FROM Stored WHERE Folder=$1 AND (UserName=$2 OR Temp=1)`
	DeleteStored = `DELETE FROM Stored WHERE Folder=$1`
	// delete user
	DeleteUser = `DELETE FROM Users where Name=$1`
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
func DBWrite(prep string, args []interface{}) (err error) {
	_, err = db.Exec(prep, args...)
	return
}

func DBWriteMap(prep string, values map[string]interface{}) (err error) {
	_, err = db.NamedExec(prep, values)
	return
}
