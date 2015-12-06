package main

import (
	"bytes"
	"fmt"
	"log"
	"strings"
)

func RunCmd() (err error) {
	for {
		select {
		case cmd := <-Tasks:
			fmt.Println("execing prog")
			cmd.Stdin = strings.NewReader("input")
			var out bytes.Buffer
			cmd.Stdout = &out
			err = cmd.Run()
			if err != nil {
				log.Print(err)
			}
			fmt.Println("finished running")
		}
	}
}
