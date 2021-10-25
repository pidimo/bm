/*
miky:20141003
add caused by field
*/
Alter Table mailaccerror add causedby varchar(250) first;

/*
miky:20141017
app versions
*/
update systemconstant set value='5.3.1' where name='APP_VERSION';
update systemconstant set value='5.2.1' where name='DB_VERSION';

