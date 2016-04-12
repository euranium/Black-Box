package main

/*
structure definitions
drop any structures that will be used here
*/

import (
	"errors"
	"fmt"
	"net/http"
	"reflect"
	"strconv"
	"strings"
)

// empty struct when no data is needing to be passed
type Empty struct{}

// routing information for each page
type Route struct {
	Name       string
	Method     string
	Pattern    string
	HandleFunc http.HandlerFunc
}

type User struct {
	user_name string
	hash      string
}

type List struct {
	files []string
}

/*
sql data table structs, edit w/ data.sql
Table is the parent w/ every different type of field
*/
func Fill(t interface{}, vals map[string]string) error {
	return nil
}

type Table struct {
	/*
		Name     string   `sql:"Name"    `
		Folder   string   `sql:"Folder"  `
		Hash     string   `sql:"Hash"    `
		Time     float64  `sql:"Time"    `
		ProgType string   `sql:"ProgType"`
		ProgName string   `sql:"ProgName"`
		Files    []string `sql:"Files"   `
	*/
}

/*
dynamically take a map and map it to a structure using reflect
This should be inherited by all sql data structs
*/
func (t *Table) Fill(vals map[string]string) error {
	// get type of structure
	stVal := reflect.ValueOf(t).Elem()
	fmt.Println("value of:", reflect.ValueOf(t))
	fmt.Println("stVal:", stVal)
	// iterate over all values in map
	for key, val := range vals {
		field := stVal.FieldByName(key)
		// select which datatype case matches for each suppored type
		// and convert from string
		switch field.Type().String() {
		case "int":
			v, err := strconv.ParseInt(val, 10, 64)
			if err != nil {
				return err
			}
			field.SetInt(v)
		case "float32":
			fallthrough
		case "float64":
			v, err := strconv.ParseFloat(val, 64)
			if err != nil {
				return err
			}
			field.SetFloat(v)
		case "string":
			field.SetString(val)
		case "array":
			fallthrough
		case "slice":
			fmt.Println("slice")
			// seperate values into array
			f := strings.Split(val, ",")
			len := len(f)
			// make a field slice
			val := reflect.MakeSlice(field.Type(), len, len*2)
			// append all values into it
			for k := 0; k < len; k++ {
				var v reflect.Value
				v.SetString(f[k])
				val = reflect.Append(val, v)
			}
			// set slice as field value
			field.Set(val)
		default:
			return errors.New(fmt.Sprintf("type not handled: %v", field.Type().String()))
		}
	}
	fmt.Println("finished:", t)
	return nil
}

type UserTable struct {
	Name   string  `db:"name"  `
	Folder string  `db:"folder"`
	Hash   string  `db:"hash"  `
	Time   float64 `db:time"   `
}

type ProgramsTable struct {
	Table
	Folder   string
	Name     string
	ProgType string
	Files    []string
}

type StoredTable struct {
	Table
	Name     string
	Folder   string
	ProgName string
	Files    []string
	Time     float64
}

/*
structs for data hand offs
to be converted to and from JSON
*/
type Result struct {
	name   string
	status string
	prog   string
}

type File struct {
	name string
	data string
}

type Program struct {
	name    string
	results []File
}

type Submit struct {
	name  string
	input []string
}
