-- user name, password hash, folder name, datetime updated in unix time
Create Table If Not Exists Users (
	name 	Text,
	hash 	Text,
	folder 	Text Unique,
	time 	Real
);

-- store of each program which can be run:
-- folder name, main program name, type of program (eg java, executable)
Create Table If Not Exists Programs (
	folder 		Text,
	name 		String Unique,
	progType 	Text
);

-- store of each program run: user associated w/, folder name,
-- program associated w/, still running or not,
-- expected output in struct string, last access datetime
Create Table If Not Exists Stored (
	userName 	Text,
	folder 		Text Unique,
	progName 	Text,
	pending 	Numeric,
	output 		Text,
	time 		Real
);
