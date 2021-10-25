/*
miky:20140408
import deduplication functionality
*/
Create Table importrecord (
cityname Varchar(60),
companyid Int NOT NULL,
countryname Varchar(60),
housenumber Varchar(30),
identitykey Varchar(250) NOT NULL,
importrecordid Int NOT NULL,
isduplicate Smallint NOT NULL,
name1 Varchar(100),
name2 Varchar(100),
name3 Varchar(100),
profileid Int NOT NULL,
recordindex Int,
recordtype Smallint NOT NULL,
street Varchar(100),
version Int NOT NULL,
zip Varchar(30),
Primary Key (importrecordid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table importrecord add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table importrecord add Foreign Key (profileid) references importprofile (profileid) on delete  restrict on update  restrict;


Create Table recordcolumn (
columnindex Int,
columnvalue Varchar(100),
companyid Int NOT NULL,
importcolumnid Int NOT NULL,
importrecordid Int NOT NULL,
Primary Key (importrecordid, importcolumnid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table recordcolumn add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table recordcolumn add Foreign Key (importrecordid) references importrecord (importrecordid) on delete  restrict on update  restrict;
Alter Table recordcolumn add Foreign Key (importcolumnid) references importcolumn (importcolumnid) on delete  restrict on update  restrict;

Create Table recordduplicate (
addressid Int NOT NULL,
companyid Int NOT NULL,
importrecordid Int NOT NULL,
Primary Key (importrecordid, addressid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table recordduplicate add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table recordduplicate add Foreign Key (importrecordid) references importrecord (importrecordid) on delete  restrict on update  restrict;
Alter Table recordduplicate add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;

/*Check duplicates functionality*/
Create Table deduplicontact (
companyid Int NOT NULL,
deduplicontactid Int NOT NULL,
starttime bigint NOT NULL,
status Smallint NOT NULL,
userid Int NOT NULL,
version Int NOT NULL,
Primary Key (deduplicontactid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table deduplicontact add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table deduplicontact add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

Create Table duplicategroup (
companyid Int NOT NULL,
deduplicontactid Int NOT NULL,
duplicategroupid Int NOT NULL,
version Int NOT NULL,
Primary Key (duplicategroupid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table duplicategroup add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table duplicategroup add Foreign Key (deduplicontactid) references deduplicontact (deduplicontactid) on delete  restrict on update  restrict;

Create Table duplicateaddress (
addressid Int NOT NULL,
companyid Int NOT NULL,
duplicategroupid Int NOT NULL,
ismain Smallint NOT NULL,
positionindex Int NOT NULL,
Primary Key (duplicategroupid, addressid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table duplicateaddress add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table duplicateaddress add Foreign Key (duplicategroupid) references duplicategroup (duplicategroupid) on delete  restrict on update  restrict;
Alter Table duplicateaddress add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;

/*deduplication access right*/
INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(16, 'DEDUPLICATIONCONTACT', 'Deduplication contacts functionality', 133, 1, 'DedupliContact.functionality');

/*
miky:20140415
mail account error detail
*/
Create Table mailaccerrdetail (
accerrdetailid Int NOT NULL,
companyid Int NOT NULL,
emails Varchar(250),
mailaccerrorid Int NOT NULL,
subject Varchar(250),
version Int NOT NULL,
Primary Key (accerrdetailid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table mailaccerrdetail add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table mailaccerrdetail add Foreign Key (mailaccerrorid) references mailaccerror (mailaccerrorid) on delete  restrict on update  restrict;


/*
miky:20140417
add import record fields
*/
ALTER TABLE importrecord ADD parentrecordid Int after name3;
ALTER TABLE importrecord ADD function Varchar(80) after countryname;

Alter Table importrecord add Foreign Key (parentrecordid) references importrecord (importrecordid) on delete  restrict on update  restrict;

/*
miky:20140422
remove task composed constraint to salesprocess
*/
alter table task drop foreign key task_ibfk_10;

/*
miky:20140423
add mailidentifier field
*/
ALTER TABLE mailaccerrdetail ADD mailidentifier Int after mailaccerrorid;

/*
miky:20140425
app versions
*/
update systemconstant set value='5.2' where name='APP_VERSION';
update systemconstant set value='5.1' where name='DB_VERSION';
