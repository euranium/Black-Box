package main

import (
	"encoding/xml"
	"fmt"
	"reflect"
	"strconv"
)

/*
parse a string to a provided xml structure
a struct pointer must be passed which corresponds to the provided xml structure
*/
func StringToXml(data string, out interface{}) (interface{}, error) {
	err := xml.Unmarshal([]byte(data), &out)
	return out, err
}

func ToMap(in interface{}, tag string) (map[string]interface{}, error) {
	out := make(map[string]interface{})

	v := reflect.ValueOf(in)
	if v.Kind() == reflect.Ptr {
		v = v.Elem()
	}

	// we only accept structs
	if v.Kind() != reflect.Struct {
		return nil, fmt.Errorf("ToMap only accepts structs; got %T", v)
	}

	typ := v.Type()
	for i := 0; i < v.NumField(); i++ {
		// gets us a StructField
		fi := typ.Field(i)
		if tagv := fi.Tag.Get(tag); tagv != "" {
			// set key of map to value in struct field
			out[tagv] = v.Field(i).Interface()
		}
	}
	return out, nil
}

/*
parse xml structure and return a string represenation
*/
func XmlToString(data interface{}) (string, error) {
	str, err := xml.MarshalIndent(data, " ", " ")
	return string(str[:]), err
}

func Sort(data map[string][]string) []string {
	sorted := make([]string, len(data))
	for k, v := range data {
		fmt.Println("k:", k, "v:", v)
		i, err := strconv.Atoi(k)
		if err != nil {
			fmt.Println("error:", err.Error())
		} else if v[0] != "" {
			sorted[i] = v[0]
		}
	}
	var srt []string
	for _, v := range sorted {
		if v != "" {
			srt = append(srt, v)
		}
	}
	return srt
}
