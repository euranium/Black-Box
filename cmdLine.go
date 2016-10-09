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
Tasks <- {"Name": "Model Name", "Dir": "path/to", "Commands":
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
			msg := []byte(" ")
			var err error
			// iterate over all commands needing to be run
			for _, command := range input.Commands {
				input := []string{"exec.py", input.Dir, command.ProgType, command.Program}
				cmd := exec.Command("python", append(input, command.Input...)...)
				out, err := cmd.CombinedOutput()
				if out != nil {
					msg = append(msg, out...)
				}
				if err != nil {
					fmt.Printf("error: %s, msg: %s\n", err.Error(), msg)
					DBLogErrorLocal(err.Error(),input.Dir)
					break
				}
			}
			errMsg := ""
			msgMsg := ""
			if err != nil {
				errMsg = err.Error()
				DBLogErrorLocal(err.Error(),input.dir)
			}
			if msg != nil {
				msgMsg = string(msg[:])
				fmt.Println("output:", msgMsg)
			}
			go LogRun(input.Dir, input.Name, errMsg, msgMsg)
		}
	}
}

/*
log a program run into the db
*/
func LogRun(pathTo, name, errMsg, msg string) {
	// query what files where there before
	var p Programs
	var args []interface{}
	args = append(args, name)
	err := DBReadRow(QueryProgram, args, &p)
	if err != nil {
		fmt.Println(err.Error())
		DBLogErrorLocal(err.Error(),pathTo)
		return
	}
	if p.Files == "" {
		fmt.Println("no results finding", name)
		DBLogErrorLocal("no results finding", pathTo)
		return
	}

	// check what new files appeared
	files := strings.Split(p.Files, ",")
	files = DifFiles(pathTo, files)
	// update row w/ new files
	args[0] = strings.Join(files, ",")
	args = append(args, msg)
	args = append(args, errMsg)
	args = append(args, filepath.Base(pathTo))
	err = DBWrite(UpdateRun, args)
	if err != nil {
		fmt.Println(err.Error())
		DBLogErrorLocal(err.Error(),pathTo)
	}
	return
}

/*
log an error from a local command into the error database
*/
func DBLogErrorLocal(Message, Folder string) {
	var args[]interface{}
	args[0]=Message
	args=append(args,time.Now().Unix())
	args=append(args,"local")
	DBWrite(LogError, args)	
}
