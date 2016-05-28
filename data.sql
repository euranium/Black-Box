/*
user name, folder name, session id, password hash, temp user or logged,
time user last access in unix seconds
*/
Create Table If Not Exists Users (
	Name 		TEXT UNIQUE,
	Folder 		TEXT UNIQUE,
	SessionKey 	TEXT,
	Hash 		TEXT,
	Time 		NUMERIC,
	Temp 		NUMERIC
);

/*
store of each program which can be run:
folder name, main program name, type of program (eg java),
files in folder in
*/
CREATE TABLE IF NOT EXISTS Programs (
	Folder 		TEXT UNIQUE,
	Files 		TEXT
);

CREATE TABLE IF NOT EXISTS Command (
	Name 		TEXT UNIQUE,
	ProgType 	TEXT
);

/*
store of each program run: user associated w/, folder name,
program associated w/, files outputed in struct string,
weather the program has been viewed, last access in unix seconds
output will be added when program is done, otherwise empty string
*/
CREATE TABLE IF NOT EXISTS Stored (
	Folder 		TEXT UNIQUE,
	UserName 	TEXT,
	ProgName 	TEXT,
	Message 	TEXT,
	ErrMessage  TEXT,
	Files 		TEXT,
	Viewed 		NUMERIC DEFAULT 0,
	Time 		NUMERIC,
	Temp 		NUMERIC
);

/*
for initilization purposes
*/
INSERT INTO USERS (Name, Folder, SessionKey, Hash, Time, Temp)
VALUES ("aaa", "aaa", " ", "aaa", %s, 0);

INSERT INTO Command (Name, ProgType) VALUES ("ModEvo", "java");
INSERT INTO Command (Name, ProgType) VALUES ("plot.py", "python");
