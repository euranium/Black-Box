package main

import (
	"gopkg.in/fsnotify.v1"
	"io/ioutil"
	"log"
	"math/rand"
	"os"
	"time"
)

func RandomString(strlen int) string {
	rand.Seed(time.Now().UTC().UnixNano())
	const chars = "abcdefghipqrstuvwxyzABCDEFGHIPQRSTUVWXYZ0123456789"
	result := make([]byte, strlen)
	for i := 0; i < strlen; i++ {
		result[i] = chars[rand.Intn(len(chars))]
	}
	return string(result)

}

/*
get the contents of a file,
*/
func ReadFile(filePath string) (file []byte, err error) {
	file, err = ioutil.ReadFile(filePath)
	if err != nil {
		return
	}
	return
}

/* check if dir exists */
func CheckDir(path string) (exists bool, err error) {
	if path == "" {
		return false, nil
	}
	_, err = os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return false, err
}

/* check if file exists */
func CheckFile(file string) (exists bool) {
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

func CreateUser(user string) (err error) {
	return
}

func AddFile(user, fileName string) (err error) {
	return
}

func DeleteFile(user, fileName string) (err error) {
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
