/*
miky:20170324
user demo account
*/
Create Table demoaccount (
companylogin NVarchar(20) NOT NULL,
companyname NVarchar(60),
creationdate Integer,
demoaccountid Integer NOT NULL,
email NVarchar(100) NOT NULL,
firstname NVarchar(60) NOT NULL,
isalreadycreated Smallint NOT NULL,
lastname NVarchar(60) NOT NULL,
password NVarchar(80),
phonenumber NVarchar(60),
registrationdate Integer NOT NULL,
registrationkey NVarchar(100) NOT NULL,
userlogin NVarchar(20) NOT NULL,
version Integer NOT NULL,
Primary Key (demoaccountid)) LOCK MODE ROW;


/*
miky:20170328
app versions
*/
update systemconstant set value='6.5' where name='APP_VERSION';
update systemconstant set value='6.0' where name='DB_VERSION';

