package main

import (
	"encoding/xml"
	"fmt"
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

/*
parse xml structure and return a string represenation
*/
func XmlToString(data interface{}) (string, error) {
	str, err := xml.MarshalIndent(data, " ", " ")
	return string(str[:]), err
}

func Sort(data map[string][]string) (sorted []string) {
	for k, v := range data {
		fmt.Println("k:", k, "v:", v)
		i, err := strconv.Atoi(k)
		if err != nil {
			fmt.Println("error:", err.Error())
			return nil
		}
		sorted[i] = v[0]
	}
	return sorted
}
