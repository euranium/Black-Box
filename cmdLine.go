package main

import (
	//"bytes"
	"fmt"
	//"log"
	//"strings"
	//"io"
)

/*
to have program run,
Tasks <- exec.Command("args0", "args1", ...)
example:
Tasks <- exec.Command("java", path.Join(progDir, "javaProg30Sec"), "rand")
Only runs one exec at a time right now
*/
func RunCmd() {
	for {
		select {
		case cmd := <-Tasks:
			out, err := cmd.CombinedOutput()
			if err != nil {
				fmt.Printf("error: %s, msg: %s", err.Error(), out)
			} else {
				fmt.Printf("finished running: %s\n", out)
			}
		}
	}
}
