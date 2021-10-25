/*
Ivan Alban: 20090903
Add importprofile table to store import configuration.
*/
create table importprofile(
companyid int not null,
label varchar(100) not null,
profileid int not null,
profiletype int not null,
skipfirstrow tinyint not null,
userid int not null,
version int not null,
Primary Key (profileid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table importprofile add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table importprofile add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

/*
Ivan Alban: 20090903
Add profilecolumn table to store selected columns from import ui
*/
create table importcolumn(
columnid int not null,
columnvalue int not null,
companyid int not null,
groupid int not null,
importcolumnid int not null,
profileid int not null,
uiposition int not null,
Primary Key (importcolumnid))ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table importcolumn add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table importcolumn add Foreign Key (profileid) references importprofile (profileid) on delete  restrict on update  restrict;

/*
Ivan Alban: 20090911
remove productid from productcontract table.
*/
alter table productcontract drop foreign key productcontract_ibfk_3;
alter table productcontract drop column productid;

/*
Ivan Alban: 200090922
add grouping field in productcontract table
*/
alter table productcontract add column grouping varchar(50) after discount;

/*
Ivan Alban: 20090925
add columnName field in importcolumn table
*/
alter table importcolumn add columnname varchar(100) after columnid;

/* Fer:20091006 Update version constants */
update systemconstant set value='4.2.2' where name='APP_VERSION';
update systemconstant set value='4.2.1' where name='DB_VERSION';