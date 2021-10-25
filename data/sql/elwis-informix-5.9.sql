/*
miky:20160907
add field to invoice shipping
*/
Alter Table customer add invoiceshipping Smallint before numberofemployees;

/*
miky:20160919
add invoice title field
*/
Alter Table invoice add title NVarchar(100) before type;


/*
miky:20160926
app versions
*/
update systemconstant set value='6.3' where name='APP_VERSION';
update systemconstant set value='5.9' where name='DB_VERSION';