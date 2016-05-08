/*
user name, folder name, session id, password hash, temp user or logged,
time user last access in unix seconds
*/
Create Table If Not Exists Users (
	Name 		Text Unique,
	Folder 		Text Unique,
	SessionKey 	Text Unique,
	Hash 		Text,
	Time 		Numeric,
	Temp 		Numeric
);

/*
store of each program which can be run:
folder name, main program name, type of program (eg java),
files in folder in
*/
Create Table If Not Exists Programs (
	Folder 		Text Unique,
	Name 		Text,
	ProgType 	Text,
	Files 		Text
);

/*
store of each program run: user associated w/, folder name,
program associated w/, files outputed in struct string,
weather the program has been viewed, last access in unix seconds
output will be added when program is done, otherwise empty string
*/
Create Table If Not Exists Stored (
	Folder 		Text Unique,
	UserName 	Text,
	ProgName 	Text,
	Message 	Text,
	Files 		Text,
	Viewed 		Numeric Default 0,
	Time 		Numeric,
	Temp 		Numeric
);

/*
for initilization purposes
*/
Insert into Users (Name, Folder, SessionKey, Hash, Time, Temp) 
Values ("aaa", "aaa", " ", "aaa", 1460501217, 0);
