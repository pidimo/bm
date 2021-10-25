/*
miky:20140709
add check duplicate field
*/
ALTER TABLE importprofile ADD checkduplicate Smallint DEFAULT 1 NOT NULL first;


/*
miky:20140710
app versions
*/
update systemconstant set value='5.2.4' where name='APP_VERSION';
update systemconstant set value='5.1.2' where name='DB_VERSION';
