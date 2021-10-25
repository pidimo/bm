/*
Ivan Alban: 20090903
Add importprofile table to store import configuration.
*/
create table importprofile(
companyid integer not null,
label nvarchar(100,0) not null,
profileid integer not null,
profiletype integer not null,
skipfirstrow smallint not null,
userid integer not null,
version integer not null,
Primary Key (profileid)) LOCK MODE ROW;

Alter Table importprofile add Constraint Foreign Key (companyid) references company (companyid);
Alter Table importprofile add Constraint Foreign Key (userid) references elwisuser (userid);

/*
Ivan Alban: 20090903
Add profilecolumn table to store selected columns from import ui
*/
create table importcolumn(
columnid integer not null,
columnvalue integer not null,
companyid integer not null,
groupid integer not null,
importcolumnid integer not null,
profileid integer not null,
uiposition integer not null,
Primary Key (importcolumnid))LOCK MODE ROW;

Alter Table importcolumn add Constraint Foreign Key (companyid) references company (companyid);
Alter Table importcolumn add Constraint Foreign Key (profileid) references importprofile (profileid);

/*
Ivan Alban : 20090911
remove productid from productcontract table.
*/
alter table productcontract drop productid;

/*
Ivan Alban: 200090922
add grouping field in productcontract table
*/
alter table productcontract add  grouping nvarchar(50) before installment;

/*
Ivan Alban: 20090925
add columnName field in importcolumn table
*/
alter table importcolumn add columnname nvarchar(100,0) before columnvalue;

/* fer:20091006 Update version constants */
update systemconstant set value='4.2.2' where name='APP_VERSION';
update systemconstant set value='4.2.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;