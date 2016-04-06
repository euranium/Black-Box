package main

import (
	//"bytes"
	"fmt"
	//"os"
	"os/exec"
	//"log"
	//"strings"
)

var (
	currentDir = "~/server"
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
		case args := <-Tasks:
			args = append([]string{"exec.py"}, args...)
			cmd := exec.Command("python", args...)
			out, err := cmd.CombinedOutput()
			if err != nil {
				fmt.Printf("error: %s, msg: %s", err.Error(), out)
			} else {
				fmt.Printf("finished running: %s\n", out)
			}
			/*
				dir := cmd.Args[len(cmd.Args)-1]
				empty := make([]string, 0)
				// remove last elememt
				fmt.Println("splicing till:", cmd.Args[:len(cmd.Args)-2])
				cmd.Args = append(cmd.Args[:len(cmd.Args)-2], empty...)
				fmt.Println("args:", cmd.Args)
				err := os.Chdir(dir)
				if err != nil {
					fmt.Println("error changing dir:", err.Error())
					return
				}
				out, err := cmd.CombinedOutput()
				if err != nil {
					fmt.Printf("error: %s, msg: %s", err.Error(), out)
				} else {
					fmt.Printf("finished running: %s\n", out)
				}
				os.Chdir(currentDir)
			*/
		}
	}
}
