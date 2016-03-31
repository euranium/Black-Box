Create Table If Not Exists Users (
	name Text Unique,
	folder Text Unique,
	timer Integer
);

Create Table If Not Exists Programs (
	folder String,
	username String,
	datecreated Integer
);
