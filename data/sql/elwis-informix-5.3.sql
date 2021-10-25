/*
miky:20150316
user article access
*/
Create Table userarticleaccess (
articleid Integer NOT NULL,
companyid Integer NOT NULL,
usergroupid Integer NOT NULL,
Primary Key (articleid, usergroupid)) LOCK MODE ROW;

Alter Table userarticleaccess add Constraint Foreign Key (companyid) references company (companyid);
Alter Table userarticleaccess add Constraint Foreign Key (articleid) references article (articleid);
Alter Table userarticleaccess add Constraint Foreign Key (usergroupid) references usergroup (usergroupid);

/*update published status defined as internal to access for all users*/
update article set publishedto = 2 where publishedto = 1;


/*
miky:20150319
add fields in project time
*/
Alter Table projecttime add createddate Integer before date;
Alter Table projecttime add releaseddate Integer before status;
Alter Table projecttime add releaseduserid Integer before status;
Alter Table projecttime add fromdatetime Int8 before projectactivityid;
Alter Table projecttime add todatetime Int8 before userid;

Alter table projecttime add Constraint Foreign Key (releaseduserid) references elwisuser (userid);

/*
miky:20150326
new table
*/
Create Table projecttimelimit (
assigneeid Integer,
companyid Integer NOT NULL,
hastimelimit Smallint NOT NULL,
invoicelimit Decimal(6,1),
noinvoicelimit Decimal(6,1),
projectid Integer NOT NULL,
subprojectid Integer,
timelimitid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (timelimitid)) LOCK MODE ROW;

Alter Table projecttimelimit add Constraint Foreign Key (companyid) references company (companyid);
Alter Table projecttimelimit add Constraint Foreign Key (projectid) references project (projectid);
Alter Table projecttimelimit add Constraint Foreign Key (subprojectid) references subproject (subprojectid);
Alter Table projecttimelimit add Constraint Foreign key (assigneeid) references address (addressid);

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTTIMELIMIT', 'Project time limit', 134, 12, 'ProjectTimeLimit.functionality');


/*
miky:20150327
app versions
*/
update systemconstant set value='5.4' where name='APP_VERSION';
update systemconstant set value='5.3' where name='DB_VERSION';
