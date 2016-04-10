/*
user name, folder name, password hash,
time user last access in unix seconds
*/
Create Table If Not Exists Users (
	name 	Text Unique,
	folder 	Text Unique,
	hash 	Text,
	time 	Real
);

/*
store of each program which can be run:
folder name, main program name, type of program (eg java),
files in folder in
*/
Create Table If Not Exists Programs (
	folder 		Text,
	progName 	String Unique,
	progType 	Text,
	files 		Text
);

/*
store of each program run: user associated w/, folder name,
program associated w/, files outputed in struct string,
last access in unix seconds
output will be added when program is done, otherwise empty string
*/
Create Table If Not Exists Stored (
	name 		Text,
	folder 		Text Unique,
	progName 	Text,
	files 		Text,
	time 		Real
);
