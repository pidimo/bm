
/*
miky:20150429
define priority as not mandatory
*/
alter table task modify priorityid Int default null;


/*
miky:20150430
app versions
*/
update systemconstant set value='5.4.1' where name='APP_VERSION';
update systemconstant set value='5.3.1' where name='DB_VERSION';
