/*
miky:20130703
add name in additional address and relations
*/
ALTER TABLE addaddress ADD name Varchar(100) after isdefault;

ALTER TABLE contactperson ADD addaddressid Int after active;
Alter Table contactperson add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

ALTER TABLE customer ADD addaddressid Int first;
Alter Table customer add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

ALTER TABLE sale ADD addaddressid Int first;
Alter Table sale add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

ALTER TABLE productcontract ADD addaddressid Int first;
Alter Table productcontract add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

ALTER TABLE invoice ADD addaddressid Int first;
Alter Table invoice add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

/*
miky:20130704
*/
update systemconstant set value='5.0.1' where name='APP_VERSION';
update systemconstant set value='4.9.1' where name='DB_VERSION';
