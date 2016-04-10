/*
user name, folder name, password hash,
time user last access in unix seconds
*/
Create Table If Not Exists Users (
	Name 	Text Unique,
	Folder 	Text Unique,
	Hash 	Text,
	Time 	Real
);

/*
store of each program which can be run:
folder name, main program name, type of program (eg java),
files in folder in
*/
Create Table If Not Exists Programs (
	Folder 		Text,
	ProgName 	String Unique,
	ProgType 	Text,
	Files 		Text
);

/*
store of each program run: user associated w/, folder name,
program associated w/, files outputed in struct string,
last access in unix seconds
output will be added when program is done, otherwise empty string
*/
Create Table If Not Exists Stored (
	Name 		Text,
	Folder 		Text Unique,
	ProgName 	Text,
	Files 		Text,
	Time 		Real
);
