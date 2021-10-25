/*
miky:20121205
add generation key
*/
Alter Table campsentlog ADD generationkey Bigint after generationid;
Alter Table sentlogcontact ADD generationkey Bigint after errormessage;

/*
miky:20121221
Update version constants
*/
update systemconstant set value='4.9.1' where name='APP_VERSION';
update systemconstant set value='4.7' where name='DB_VERSION';
