/*
miky:20170324
user demo account
*/
Create Table demoaccount (
companylogin Varchar(20) NOT NULL,
companyname Varchar(60),
creationdate Int,
demoaccountid Int NOT NULL,
email Varchar(100) NOT NULL,
firstname Varchar(60) NOT NULL,
isalreadycreated Smallint NOT NULL,
lastname Varchar(60) NOT NULL,
password Varchar(80),
phonenumber Varchar(60),
registrationdate Int NOT NULL,
registrationkey Varchar(100) NOT NULL,
userlogin Varchar(20) NOT NULL,
version Int NOT NULL,
Primary Key (demoaccountid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;


/*
miky:20170328
app versions
*/
update systemconstant set value='6.5' where name='APP_VERSION';
update systemconstant set value='6.0' where name='DB_VERSION';
