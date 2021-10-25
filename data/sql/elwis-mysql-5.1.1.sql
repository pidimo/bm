/*
miky:20140515
add import record field
*/
ALTER TABLE importrecord ADD organizationid Int after name3;
Alter Table importrecord add Foreign Key (organizationid) references address (addressid) on delete  restrict on update  restrict;

/*
miky:20140603
app versions
*/
update systemconstant set value='5.2.3' where name='APP_VERSION';
update systemconstant set value='5.1.1' where name='DB_VERSION';
