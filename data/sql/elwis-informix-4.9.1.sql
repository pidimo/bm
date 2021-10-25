/*
miky:20130703
add name in additional address and relations
*/
ALTER TABLE addaddress ADD name NVarchar(100) before street;

ALTER TABLE contactperson ADD addaddressid Integer before addaddressline;
Alter Table contactperson add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

ALTER TABLE customer ADD addaddressid Integer before branchid;
Alter Table customer add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

ALTER TABLE sale ADD addaddressid Integer before companyid;
Alter Table sale add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

ALTER TABLE productcontract ADD addaddressid Integer before addressid;
Alter Table productcontract add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

ALTER TABLE invoice ADD addaddressid Integer before addressid;
Alter Table invoice add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

/*
miky:20130704
*/
update systemconstant set value='5.0.1' where name='APP_VERSION';
update systemconstant set value='4.9.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;