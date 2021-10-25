
/*
miky:20150429
define priority as not mandatory
*/
alter table task modify priorityid Integer default null;
Alter Table task add Constraint Foreign Key (priorityid) references priority (priorityid);


/*
miky:20150430
app versions
*/
update systemconstant set value='5.4.1' where name='APP_VERSION';
update systemconstant set value='5.3.1' where name='DB_VERSION';
