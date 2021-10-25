/*
miky:20150316
user article access
*/
Create Table userarticleaccess (
articleid Int NOT NULL,
companyid Int NOT NULL,
usergroupid Int NOT NULL,
Primary Key (articleid, usergroupid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table userarticleaccess add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table userarticleaccess add Foreign Key (articleid) references article (articleid) on delete  restrict on update  restrict;
Alter Table userarticleaccess add Foreign Key (usergroupid) references usergroup (usergroupid) on delete  restrict on update  restrict;

/*update published status defined as internal to access for all users*/
update article set publishedto = 2 where publishedto = 1;


/*
miky:20150319
add fields in project time
*/
Alter Table projecttime add createddate Int after confirmedbyid;
Alter Table projecttime add releaseddate Int after projectid;
Alter Table projecttime add releaseduserid Int after releaseddate;
Alter Table projecttime add fromdatetime bigint after descriptionid;
Alter Table projecttime add todatetime bigint after tobeinvoiced;

Alter table projecttime add Foreign Key (releaseduserid) references elwisuser (userid) on delete  restrict on update  restrict;

/*
miky:20150326
new table
*/
Create Table projecttimelimit (
assigneeid Int,
companyid Int NOT NULL,
hastimelimit Tinyint NOT NULL,
invoicelimit Decimal(6,1),
noinvoicelimit Decimal(6,1),
projectid Int NOT NULL,
subprojectid Int,
timelimitid Int NOT NULL,
version Int NOT NULL,
Primary Key (timelimitid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table projecttimelimit add Foreign Key (companyid) references company (companyid) on delete restrict on update restrict;
Alter Table projecttimelimit add Foreign Key (projectid) references project (projectid) on delete restrict on update restrict;
Alter Table projecttimelimit add Foreign Key (subprojectid) references subproject (subprojectid) on delete restrict on update restrict;
Alter Table projecttimelimit add foreign key (assigneeid) references address (addressid) on delete restrict on update restrict;

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTTIMELIMIT', 'Project time limit', 134, 12, 'ProjectTimeLimit.functionality');



/*
miky:20150327
app versions
*/
update systemconstant set value='5.4' where name='APP_VERSION';
update systemconstant set value='5.3' where name='DB_VERSION';

