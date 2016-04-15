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

/*
sql data table structs, edit w/ data.sql
Table is the parent w/ every different type of field
*/

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

type Container []interface{}

/*
dynamically take a map and map it to a structure using reflect
This should be inherited by all sql data structs, but because of
explicit data typing does not inherit correctly. So not being used.
Instead, using sqlx.
*/
func Fill(vals map[string]string, container Container) error {
	// get type of structure
	this := container[0]
	stVal := reflect.ValueOf(this).Elem()
	fmt.Println("value of:", reflect.ValueOf(this))
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
	fmt.Println("finished:", this)
	return nil
}

type User struct {
	Name   string  `db:"Name"  `
	Folder string  `db:"Folder"`
	Hash   string  `db:"Hash"  `
	Time   float64 `db:"Time"   `
}

type Programs struct {
	Folder   string `db:"Folder"  `
	Name     string `db:"Name"    `
	ProgType string `db:"ProgType"`
	Files    string `db:"Files"   `
}

type Stored struct {
	UserName string `db:"UserName"`
	Folder   string `db:"Folder"  `
	ProgName string `db:"ProgName"`
	Files    string `db:"Files"   `
	Time     int64  `db:"Time"    `
}

/*
structs for data hand offs
to be converted to and from JSON
*/
type Result struct {
	Name    string
	Results []File
}

type File struct {
	Name string
	Data string
}

type Program struct {
	Name    string
	Results []File
}

type Submit struct {
	Name  string
	Input []string
}
