package main

import (
	"gopkg.in/fsnotify.v1"
	"log"
	"os"
)

func CheckDir(user string) (exists bool) {
	return false
}

func CheckFile(file string) (exists bool) {
	if _, err := os.Stat(file); err == nil {
		return true
	}
	return false
}

func ListDir(user string) (list []string) {
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
