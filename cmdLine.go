package main

import (
	//"bytes"
	"fmt"
	//"os"
	"os/exec"
	//"log"
	//"strings"
)

var ()

/*
routine to execute and run programs. Execs a python program to change directory to
the program and then executes the program. When a ^C is sent to this program, the
exec'd program should also recieve and shut down. Probably no need to handle the
case, unless wanting to wait on program then exit.

to have program run,
Tasks <- []string{"program", "programName", "args0", ... argsN, "path/to/program"}
Tasks <- []string{"abs/path/2/prog", "args0", ... argsN, "path/to/program"}
example:
Tasks <- []string{"java", path.Join(progDir, "javaProg30Sec"), "rand", "users/aaa/as4B-12da"}
Only runs one exec at a time right now

Runs by execting a python program to change directory to the program.
The program is then run with the provided arguments minus the last path

@TODO: add a wait group (sync.WaitGroup) to allow for n number of programs
*/
func RunCmd() {
	for {
		select {
		case args := <-Tasks:
			args = append([]string{"exec.py"}, args...)
			cmd := exec.Command("python", args...)
			out, err := cmd.CombinedOutput()
			if err != nil {
				fmt.Printf("error: %s, msg: %s", err.Error(), out)
			} else {
				fmt.Printf("finished running: %s\n", out)
			}
		}
	}
}
