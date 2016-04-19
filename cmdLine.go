package main

import (
	"fmt"
	"os/exec"
	"path/filepath"
	"strings"
	//"log"
)

var ()

/*
routine to execute and run programs. Execs a python program to change directory to
the program and then executes the program. When a ^C is sent to this program, the
exec'd program should also recieve and shut down. Probably no need to handle the
case, unless wanting to wait on program then exit.

to have program run,
Tasks <- []string{"programType", "programName", "args0", ... argsN, "path/to/program"}
example:
Tasks <- []string{"java", "javaProg", "rand", "users/aaa/as4B-12da"}
Tasks <- []string{"python", "pyProg.py", "rand", "users/aaa/as4B-12da"}
Only runs one exec at a time right now

Runs by execting a python program to change directory to the program.
The program is then run with the provided arguments minus the last path

@TODO: add a wait group (sync.WaitGroup) to allow for n number of programs
*/
func RunCmd() {
	for {
		select {
		case args := <-Tasks:
			if len(args) <= 2 {
				fmt.Println("error: not enough arguments")
				break
			}
			args = append([]string{"exec.py"}, args...)
			cmd := exec.Command("python", args...)
			out, err := cmd.CombinedOutput()
			if err != nil {
				fmt.Printf("error: %s, msg: %s", err.Error(), out)
				LogRun(args[len(args)-1], args[2], out)
			} else {
				fmt.Printf("finished running: %s\n", out)
				LogRun(args[len(args)-1], args[2], []byte(""))
			}
		}
	}
}

/*
log a program run into the db
*/
func LogRun(pathTo, name string, errMsg []byte) {
	// query what files where there before
	var p Programs
	var args []interface{}
	args = append(args, name)
	err := DBReadRow(QueryProgram, args, &p)
	if err != nil {
		fmt.Println(err.Error())
	}
	if p.Files == "" {
		fmt.Println("no results finding", name)
		return
	}
	fmt.Println("got:", p)

	// check what new files appeared
	files := strings.Split(p.Files, ",")
	files = DifFiles(pathTo, files)
	// update row w/ new files
	args[0] = strings.Join(files, ",")
	args = append(args, string(errMsg))
	args = append(args, filepath.Base(pathTo))
	err = DBWrite(UpdateRun, args)
	if err != nil {
		fmt.Println(err.Error())
	}
	fmt.Println("update success")
	return
}
