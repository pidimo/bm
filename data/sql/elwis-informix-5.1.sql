/*
miky:20140408
import deduplication functionality
*/
Create Table importrecord (
cityname NVarchar(60),
companyid Integer NOT NULL,
countryname NVarchar(60),
housenumber NVarchar(30),
identitykey NVarchar(250) NOT NULL,
importrecordid Integer NOT NULL,
isduplicate Smallint NOT NULL,
name1 NVarchar(100),
name2 NVarchar(100),
name3 NVarchar(100),
profileid Integer NOT NULL,
recordindex Integer,
recordtype Smallint NOT NULL,
street NVarchar(100),
version Integer NOT NULL,
zip NVarchar(30),
Primary Key (importrecordid)) LOCK MODE ROW;

Alter Table importrecord add Constraint Foreign Key (companyid) references company (companyid);
Alter Table importrecord add Constraint Foreign Key (profileid) references importprofile (profileid);


Create Table recordcolumn (
columnindex Integer,
columnvalue NVarchar(100),
companyid Integer NOT NULL,
importcolumnid Integer NOT NULL,
importrecordid Integer NOT NULL,
Primary Key (importrecordid, importcolumnid)) LOCK MODE ROW;

Alter Table recordcolumn add Constraint Foreign Key (companyid) references company (companyid);
Alter Table recordcolumn add Constraint Foreign Key (importrecordid) references importrecord (importrecordid);
Alter Table recordcolumn add Constraint Foreign Key (importcolumnid) references importcolumn (importcolumnid);

Create Table recordduplicate (
addressid Integer NOT NULL,
companyid Integer NOT NULL,
importrecordid Integer NOT NULL,
Primary Key (importrecordid, addressid)) LOCK MODE ROW;

Alter Table recordduplicate add Constraint Foreign Key (companyid) references company (companyid);
Alter Table recordduplicate add Constraint Foreign Key (importrecordid) references importrecord (importrecordid);
Alter Table recordduplicate add Constraint Foreign Key (addressid) references address (addressid);

/*Check duplicates functionality*/
Create Table deduplicontact (
companyid Integer NOT NULL,
deduplicontactid Integer NOT NULL,
starttime Int8 NOT NULL,
status Smallint NOT NULL,
userid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (deduplicontactid)) LOCK MODE ROW;

Alter Table deduplicontact add Constraint Foreign Key (companyid) references company (companyid);
Alter Table deduplicontact add Constraint Foreign Key (userid) references elwisuser (userid);

Create Table duplicategroup (
companyid Integer NOT NULL,
deduplicontactid Integer NOT NULL,
duplicategroupid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (duplicategroupid)) LOCK MODE ROW;

Alter Table duplicategroup add Constraint Foreign Key (companyid) references company (companyid);
Alter Table duplicategroup add Constraint Foreign Key (deduplicontactid) references deduplicontact (deduplicontactid);

Create Table duplicateaddress (
addressid Integer NOT NULL,
companyid Integer NOT NULL,
duplicategroupid Integer NOT NULL,
ismain Smallint NOT NULL,
positionindex Integer NOT NULL,
Primary Key (duplicategroupid, addressid)) LOCK MODE ROW;

Alter Table duplicateaddress add Constraint Foreign Key (companyid) references company (companyid);
Alter Table duplicateaddress add Constraint Foreign Key (duplicategroupid) references duplicategroup (duplicategroupid);
Alter Table duplicateaddress add Constraint Foreign Key (addressid) references address (addressid);

/*deduplication access right*/
INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(16, 'DEDUPLICATIONCONTACT', 'Deduplication contacts functionality', 133, 1, 'DedupliContact.functionality');

/*
miky:20140415
mail account error detail
*/
Create Table mailaccerrdetail (
accerrdetailid Integer NOT NULL,
companyid Integer NOT NULL,
emails NVarchar(250),
mailaccerrorid Integer NOT NULL,
subject NVarchar(250),
version Integer NOT NULL,
Primary Key (accerrdetailid)) LOCK MODE ROW;

Alter Table mailaccerrdetail add Constraint Foreign Key (companyid) references company (companyid);
Alter Table mailaccerrdetail add Constraint Foreign Key (mailaccerrorid) references mailaccerror (mailaccerrorid);


/*
miky:20140417
add import record fields
*/
ALTER TABLE importrecord ADD parentrecordid Integer before profileid;
ALTER TABLE importrecord ADD function NVarchar(80) before housenumber;

Alter Table importrecord add Constraint Foreign Key (parentrecordid) references importrecord (importrecordid);

/*
miky:20140422
remove task composed constraint to salesprocess
*/
alter table task drop Constraint r202_1165;

/*
miky:20140423
add mailidentifier field
*/
ALTER TABLE mailaccerrdetail ADD mailidentifier Integer before subject;

/*
miky:20140425
app versions
*/
update systemconstant set value='5.2' where name='APP_VERSION';
update systemconstant set value='5.1' where name='DB_VERSION';


UPDATE STATISTICS HIGH;