# ModEvo
## A Bioinformatics Cloud Computing Research Platform

### Getting Started
To run the program, use the run script. The file takes several perameters
``` bash
./run -cjitp 8080
```
The -c flag is to clean the database and all stored files.
For sqlite3, the database file is deleted and then remade.
The -j flag compiles the Java files in the executables folder.
The class files are not tracked by git, so make sure to use this
flag when first running. The -i flag imports all the required packages
for Go. This is also required when first running the program. The -t
flag just compiles the Go code but does not run it. The -p flag is the
port flag, specify which port to run after. This flag must go last and
does require a port number.

### Executables Management
The ModEvo software is located in the executables folder. To add a new
version of the software, drop a new folder in the executables folder. Each
version must have a unique folder name, for obvious reasons. Then the data.sql
file must be updated to include any new program names and types. The database
is expecting a program name as the primary key and the program type, eg java or
python. The Go program will find any software added at the start of the server
and check about every 30 seconds.

Software is run by calling the API. API information can be found inside json.json file.
The APi call is structured as an JSON object with the name of the software and an array
of which programs to be run in order. While this does couple the front and the back end,
it an easy method to control program execution.

### User Management
Users are all stored as temporary users. There was once plans to have permanent users,
but this has not been implemented as of yet, and may never be. A temporary user is a
user who does not have a chosen username and password. The username is a randomly generated
12 character string. Each user has a folder for storing their results. To get access to
a stored result, a user merely has to visit the url "api/results/query?name=id" with id
being a 12 character randomly generated string. Once a user has requested a valid results
page, they will have their session set to the user who generated the result. The user
will then be able to view all of their results associated with that stored user.
Sessions are stored on the front end and the back end. Both contain a stored user name
and session id. If the id's do not match, then both stored values will be deleted and
the user will be logged out.

### The Database
The database is currently a sqlite3 database in a single file. This is convienent
because it is light weight and easy to set up and tear down. The schema is located
inside of the data.sql file. Check that out for more information.

### API
The API is coded inside of api.go. The documentation for what to send and expect to
recieve is located inside json.json. The API heavily relies upon session stored on the
front end to work with the user. If you are having problems with interfacing with the
API, make sure sessions are working on the front end.
