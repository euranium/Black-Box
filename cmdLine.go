package main

import (
	"bytes"
	"fmt"
	"log"
	"strings"
)

/*
to have program run,
Tasks <- exec.Command("args0", "args1", ...)
example:
Tasks <- exec.Command("java", path.Join(progDir, "javaProg30Sec"), "rand")
Only runs one exec at a time right now
*/
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
