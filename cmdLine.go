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
the program and then executes the program. When a signal is sent to this program, the
exec'd program should also recieve it and shut down if needed. Probably no need to handle
the case, unless wanting to wait on program then exit.

to have program run,
Tasks <- {"Name": "Model Name", "Dir" "path/to", "Commands":
	[{ "Program": "program name", "Input": [params]}]
}
example:
Tasks <- {"ModEvo_Model_One", "aaa/adf", [{"ModEvo", ["input", "here"]}, {"chart.py", []}]}

Only runs one exec at a time right now

Runs by execting a python program to change directory to the program.
The program is then run with the provided arguments and the given path

@TODO: add a wait group (sync.WaitGroup) to allow for n number of programs
*/
func RunCmd() {
	for {
		select {
		case input := <-Tasks:
			var msg []byte
			var err error
			// iterate over all commands needing to be run
			for _, command := range input.Commands {
				input := []string{"exec.py", input.Dir, command.Program}
				cmd := exec.Command("python", append(input, command.Input...)...)
				out, err := cmd.CombinedOutput()
				if out != nil {
					msg = append(msg, out...)
				}
				if err != nil {
					fmt.Printf("error: %s, msg: %s\n", err.Error(), msg)
					break
				}
			}
			LogRun(input.Dir, input.Name, err.Error(), msg)
		}
	}
}

/*
log a program run into the db
*/
func LogRun(pathTo, name, errMsg string, msg []byte) {
	// query what files where there before
	var p Programs
	var args []interface{}
	args = append(args, name)
	err := DBReadRow(QueryProgram, args, &p)
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	if p.Files == "" {
		fmt.Println("no results finding", name)
		return
	}

	// check what new files appeared
	files := strings.Split(p.Files, ",")
	files = DifFiles(pathTo, files)
	// update row w/ new files
	args[0] = strings.Join(files, ",")
	args = append(args, string(msg))
	args = append(args, errMsg)
	args = append(args, filepath.Base(pathTo))
	err = DBWrite(UpdateRun, args)
	if err != nil {
		fmt.Println(err.Error())
	}
	return
}
