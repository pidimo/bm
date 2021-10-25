/* Ivan:20070604*/
/*delete standard column from campaign table*/
ALTER TABLE campaign DROP  standard;

/*Ivan:20070910*/
/*delete old dashboard tables*/
DROP TABLE attribute;
DROP TABLE componentview;

/*Change length of email fields*/
ALTER TABLE usermail MODIFY email nvarchar(250) not null;
ALTER TABLE mailcontact MODIFY email nvarchar(250) not null;
ALTER TABLE mail MODIFY mailfrom nvarchar(250) not null;
ALTER TABLE mail MODIFY mailpersonalfrom nvarchar(250);
ALTER TABLE mailrecipient MODIFY email nvarchar(250) not null;
ALTER TABLE mailrecipient MODIFY personal nvarchar(250);

/*Ivan:20070912*/
/*delete productcategory and customercategory tables*/
DROP TABLE productcategory;
DROP TABLE customercategory;

/* Fernando Montaño: 20070918 contactperson table alter*/
alter table contactperson add  recorddate integer  before version;
alter table contactperson add recorduserid integer before version;
alter table contactperson add Constraint Foreign Key (recorduserid) references elwisuser (userid) ;

/* Mauren: 20070920 Criteria adding */
INSERT INTO campcriterionvalue VALUES(24, 2, "contactPerson.recorddate", "recorddate_cp", 3, "0", "contactperson");

/*Ivan:20070926*/
/*email signature, add new columns to relate freetext and signature*/
alter table signature add textsignatureid integer before usermailid;
alter table signature add htmlsignatureid integer before companyid;
alter table signature add Constraint Foreign Key (textsignatureid) references freetext (freetextid) ;
alter table signature add Constraint Foreign Key (htmlsignatureid) references freetext (freetextid);
alter table signature modify message nvarchar(250) default null;

/* Mauren: 20071001 Criteria adding */
INSERT INTO campcriterionvalue VALUES(25, 2, "campaign.createdBy", "recorduserid", 6, "name1_name2_name3", "elwisuser");

/*Ivan: 20071001*/
/*alter company table to define template company*/
alter table company add copytemplate smallint before finishlicense;
alter table company add language nvarchar(5) before login;

/*Ivan: 20071011*/
/*Insert new system constant to enable or disable trial campaing creation*/
insert into systemconstant values("Enable or disable trial company creation", "TRIAL", null, "COMPANYCREATION", 1);

/*Fer:20071017 */
/*Adding DB version and APP version to systemconstant table.*/
insert into systemconstant (name, type, description, value) values ('APP_VERSION', 'VERSIONS', 'ELWIS source code version', '2.12.12');
insert into systemconstant (name, type, description, value) values ('DB_VERSION', 'VERSIONS', 'ELWIS database version', '2.10.8');

UPDATE STATISTICS HIGH;
