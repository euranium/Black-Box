package main

import (
	"encoding/hex"
	"errors"
	"fmt"
	"github.com/fatih/structs"
	"gopkg.in/fsnotify.v1"
	"io"
	"io/ioutil"
	"log"
	"math/rand"
	"os"
	"path"
	"path/filepath"
	"strings"
	"time"
)

/*
Make sure all programs in the fs are in the db
*/
func FilesInit() (err error) {
	folders, err := ListDir(progDir)
	if err != nil {
		return
	}
	var p []Programs
	err = DBRead(QueryPrograms, EmptyInter, &p)
	if err != nil {
		fmt.Println("err query:", err.Error())
		return
	}
	for _, f := range folders {
		found := false
		for _, prog := range p {
			if prog.Folder == f {
				found = true
				break
			}
		}
		if !found {
			AddProgram(f)
			fmt.Println("Added Program:", f)
		}
	}
	return
}

func AddProgram(folder string) (err error) {
	var prog Programs
	prog.Folder = folder
	files, err := ListDir(filepath.Join(progDir, folder))
	if err != nil {
		return
	}
	prog.Files = strings.Join(files, ",")
	err = DBWriteMap(InsertProgram, structs.Map(prog))
	if err != nil {
		fmt.Println("add program err:", err)
	}
	return
}

/*
func AddCommand(cmd *Cmd) (name string, err error) {
	name = ""
	if cmd.Exec != nil && cmd.Exec.Name != "" {
		name, err = AddCommand(cmd.Exec)
	}
	if err != nil {
		return
	}
	var c Command
	c.Name = cmd.Name
	c.ProgType = cmd.ProgType
	c.CommandName = name
	if name == "" {
		var args []interface{}
		args = append(args, c.Name)
		args = append(args, c.ProgType)
		err = DBWrite(InsertCmd, args)
	} else {
		err = DBWriteMap(InsertCommand, structs.Map(c))
	}
	return cmd.Name, err
}

/*
func AddProgram(folder string) (err error) {
	file := filepath.Join(progDir, folder, "config.json")
	config, err := ReadFile(file)
	if err != nil {
		return
	}
	//fmt.Println("config:", string(config))
	var prog Programs
	prog.Folder = folder
	files, err := ListDir(filepath.Join(progDir, folder))
	if err != nil {
		return
	}
	prog.Files = strings.Join(files, ",")
	//fmt.Println("programs:", prog)

	dec := json.NewDecoder(bytes.NewReader(config))
	err = dec.Decode(&prog)
	if err != nil {
		fmt.Println("error decode:", err.Error())
		return
	}
	if prog.ProgType == "" {
		return errors.New("Incorrect config formation")
	}
	err = DBWriteMap(InsertProgram, structs.Map(prog))
	if err != nil {
		fmt.Println("insert err:", err.Error())
	}
	return
}
*/

/*
copy all files from a directory to a new dir
creates a hard link if possible, else just copy
file contents
*/
func CopyDir(src, dst string) (err error) {
	// make sure old exists and new does not
	if !CheckDir(src) || CheckDir(dst) {
		return errors.New("Directory Location Error")
	}
	sfi, err := os.Stat(src)
	if err != nil {
		return
	}
	dir, _ := os.Open(src)
	if err != nil {
		return
	}
	err = os.Mkdir(dst, sfi.Mode())
	objs, err := dir.Readdir(-1)

	for _, obj := range objs {
		srcptr := path.Join(src, obj.Name())
		dstptr := path.Join(dst, obj.Name())

		if obj.IsDir() {
			err = CopyDir(srcptr, dstptr)
			if err != nil {
				return
			}
		} else {
			err = CopyFile(srcptr, dstptr)
			if err != nil {
				return
			}
		}
	}
	return
}

func CopyFile(src, dst string) (err error) {
	sfi, err := os.Stat(src)
	if err != nil {
		return
	}
	if !sfi.Mode().IsRegular() {
		return fmt.Errorf("CopyFile: non-regular source file %s (%q)", sfi.Name(), sfi.Mode().String())
	}
	// if destination does exist
	if err != nil && !os.IsNotExist(err) {
		return
	}
	if err = os.Link(src, dst); err == nil {
		fmt.Errorf("Link file: %s, %s", src, dst)
		return
	}
	err = copyFileContents(src, dst)
	return
}

func copyFileContents(src, dst string) (err error) {
	fmt.Println("hard copying")
	in, err := os.Open(src)
	if err != nil {
		return
	}
	defer in.Close()
	out, err := os.Create(dst)
	if err != nil {
		return
	}
	defer func() {
		cerr := out.Close()
		if err == nil {
			err = cerr
		}
	}()
	if _, err = io.Copy(out, in); err != nil {
		return
	}
	err = out.Sync()
	return
}

func RandomString(strlen int) string {
	rand.Seed(time.Now().UTC().UnixNano())
	const chars = "abcdefghipqrstuvwxyzABCDEFGHIPQRSTUVWXYZ0123456789"
	result := make([]byte, strlen)
	for i := 0; i < strlen; i++ {
		result[i] = chars[rand.Intn(len(chars))]
	}
	// place a '-' every 4 chars
	var str string
	for i := 0; i < strlen; i++ {
		if (i+1)%4 == 0 && i != strlen-1 {
			str += "-"
		}
		str += string(result[i])
	}
	return str

}

/*
check if name is an executable program
*/
func IsExec(name string) bool {
	return CheckDir(path.Join("executables", name))
}

/*
get the contents of a file,
*/
func ReadFile(path string) (file []byte, err error) {
	file, err = ioutil.ReadFile(path)
	if err != nil {
		return
	}
	return
}

/* check if dir exists */
func CheckDir(path string) (exists bool) {
	if path == "" {
		return false
	}
	_, err := os.Stat(path)
	if err == nil {
		return true
	}
	return false
}

/* check if file exists */
func CheckFile(file string) bool {
	if _, err := os.Stat(file); err == nil {
		return true
	}
	return false
}

/* list all files in directory */
func ListDir(directory string) (list []string, err error) {
	dir, err := ioutil.ReadDir(directory)
	if err != nil {
		return nil, err
	}
	for _, n := range dir {
		list = append(list, n.Name())
	}
	return
}

func WatchForFileCreation(fileName string) (err error) {
	watch, err := fsnotify.NewWatcher()
	if err != nil {
		return err
	}
	defer watch.Close()

	done := make(chan bool)
	go func() {
		for {
			select {
			case event := <-watch.Events:
				if event.Op&fsnotify.Write == fsnotify.Write {
					log.Println("modified file: ", event.Name)
				}
				if event.Op&fsnotify.Write == fsnotify.Create {
					log.Println("created file: ", event.Name)
				}
			case err := <-watch.Errors:
				log.Println("error: ", err)
			}
		}
	}()

	err = watch.Add(fileName)
	if err != nil {
		return
	}
	<-done

	return
}

func IsResult(user, folder string) bool {
	return CheckDir(path.Join("users", user, folder))
}

func ReadFileType(folder, tp string) []File {
	var files []File
	file, err := ioutil.ReadDir(folder)
	if err != nil {
		fmt.Println(err.Error())
		return nil
	}
	for _, f := range file {
		if filepath.Ext(f.Name()) == tp {
			byts, err := ReadFile(path.Join(folder, f.Name()))
			if err != nil {
				fmt.Println(err.Error())
			} else {
				//files = append(files, File{f.Name(), hex.EncodeToString(byts)})
				files = append(files, File{f.Name(), string(byts)})
			}
		}
	}
	return files
}

/*
read all files specified
*/
func ReadFiles(folder string, fls []string) []File {
	var files []File
	members := make(map[string]bool)
	for _, v := range fls {
		members[v] = true
	}
	file, err := ioutil.ReadDir(folder)
	if err != nil {
		fmt.Println(err.Error())
		return nil
	}
	for _, f := range file {
		if members[f.Name()] {
			byts, err := ReadFile(path.Join(folder, f.Name()))
			if err != nil {
				fmt.Println(err.Error())
			} else {
				//files = append(files, File{f.Name(), hex.EncodeToString(byts)})
				files = append(files, File{f.Name(), string(byts)})
			}
		}
	}
	return files
}

/*
given a folder location and a list of files, return what files are
new in the folder
*/
func DifFiles(folder string, oldFiles []string) (newFiles []string) {
	members := make(map[string]bool)
	for _, v := range oldFiles {
		members[v] = true
	}
	file, err := ioutil.ReadDir(folder)
	if err != nil {
		fmt.Println(err.Error())
		return nil
	}
	for _, f := range file {
		if !members[f.Name()] {
			newFiles = append(newFiles, f.Name())
		}
	}
	return
}

/*
create user folder
*/
func CreateUserFolder(person *User) {
	// if person has no name or folder, means a temp user so create a random unique name
	if person.Name == "" || person.Folder == "" {
		person.Folder = RandomString(12)
		for CheckDir(path.Join(UserDir, person.Folder)) {
			person.Folder = RandomString(12)
		}
		person.Name = person.Folder
	}
	os.Mkdir(path.Join(UserDir, person.Folder), 0755)
	return
}

func AddFile(user, fileName string) (err error) {
	return
}

func DeleteFile(user, fileName string) (err error) {
	return
}
