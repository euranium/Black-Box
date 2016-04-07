-- user name, password hash, folder name, date updated
Create Table If Not Exists Users (
	name Text,
	hash Text,
	folder Text Unique,
	timer Numeric
);

-- folder name, main program name, type of program (eg java, executable)
Create Table If Not Exists Programs (
	folder String,
	name String Unique,
	progType String
);
