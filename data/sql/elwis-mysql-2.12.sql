/*Ivan:20080418*/
/*Remove Start range birthday and Finish range birthday from elwisuser*/
alter table elwisuser DROP COLUMN rangebirthdayfi;
alter table elwisuser DROP COLUMN rangebirthdayst;

/*Ivan:20080424*/
/*add companyid in tables*/
/*Table:addressgroup*/
alter table addressgroup add column companyid int(11) null after addressid;
update addressgroup as element
set companyid=(
	select addr.companyid
	from mailgroupaddr as addr
	where addr.groupaddrid = element.groupaddrid);
alter table addressgroup, change companyid companyid int (11) not null;
alter table addressgroup add Foreign Key (companyid) references company (companyid);

/*Table:recurexception*/
alter table recurexception add column companyid int(11) null after appointmentid;
update recurexception as element
set companyid=(
    select ap.companyid
    from appointment as ap
    where ap.appointmentid=element.appointmentid);
alter table recurexception, change companyid companyid int(11) not null;
alter table recurexception add Foreign Key (companyid) references company (companyid);

/*Table:scheduleduser*/
alter table scheduleduser add column companyid int(11) null after appointmentid;
update scheduleduser as element
set companyid=(
    select usr.companyid
    from elwisuser as usr
    where usr.userid=element.userid);
alter table scheduleduser, change companyid companyid int(11) not null;
alter table scheduleduser add Foreign Key (companyid) references company (companyid);

/*Table:scheduleraccess*/
alter table scheduleraccess add column companyid int(11) null first;
update scheduleraccess as element
set companyid=(
    select usr.companyid
    from elwisuser as usr
    where usr.userid=element.userid);
alter table scheduleraccess, change companyid companyid int(11) not null;
alter table scheduleraccess add Foreign Key (companyid) references company (companyid);

/*Table:usertask*/
alter table usertask add column companyid int(11) null first;
update usertask as element
set companyid=(
    select usr.companyid
    from scheduleduser as usr
    where usr.scheduleduserid=element.scheduleduserid);
alter table usertask, change companyid companyid int(11) not null;
alter table usertask add Foreign Key (companyid) references company (companyid);

/*Table:userrole*/
alter table userrole add column companyid int(11) null first;
update userrole as element
set companyid=(
    select usr.companyid
    from elwisuser as usr
    where usr.userid=element.userid);
alter table userrole, change companyid companyid int(11) not null;
alter table userrole add Foreign Key (companyid) references company (companyid);

/*Table:userofgroup*/
alter table userofgroup add column companyid int(11) null first;
update userofgroup as element
set companyid=(
    select usr.companyid
    from elwisuser as usr
    where usr.userid=element.userid);
alter table userofgroup, change companyid companyid int(11) not null;
alter table userofgroup add Foreign Key (companyid) references company (companyid);

/*Table:usersession*/
alter table usersession add column companyid int(11) null first;
update usersession as element
set companyid=(
    select usr.companyid
    from elwisuser as usr
    where usr.userid=element.userid);
alter table usersession, change companyid companyid int(11) not null;
alter table usersession add Foreign Key (companyid) references company (companyid);

/*Table:usersessionparam*/
alter table usersessionparam add column companyid int(11) null first;
update usersessionparam as element
set companyid=(
    select usr.companyid
    from usersession as usr
    where usr.userid=element.userid and usr.statusname=element.statusname and usr.module=element.module);
alter table usersessionparam, change companyid companyid int(11) not null;
alter table usersessionparam add Foreign Key (companyid) references company (companyid);

/*miky:20080430*/
/*add columns in reportfilter*/
Alter Table reportfilter add isparameter tinyint NOT NULL default 0  after filtertype;
Alter Table reportfilter add label Varchar(150) after isparameter;

/*Ivan:20080505*/
/*add missing relations*/
Alter table mailcontact add Foreign Key (companyid) references company (companyid);
Alter table mailcontact add Foreign Key (mailid) references mail (mailid);
Alter table mailcontact add Foreign Key (contactid) references contact (contactid);
Alter table camptype add Foreign Key (companyid) references company (companyid);
Alter table categfieldvalue add Foreign Key (companyid) references company (companyid);

/*miky:20080519*/
/*add columns in usermail*/
Alter Table usermail add editorfont Varchar(100) after date;
Alter Table usermail add editorfontsize Varchar(30) after editorfont;

/*Ivan:20080527*/
/*New requeriments to categories*/
/*add new fields to category table*/
Alter table category add sequence int(11) after langtextid;
Alter table category add categorygroupid int after categoryid;
Alter table category add parentcategory int after langtextid;
Alter table category add hassubcategories tinyint not null default 0 after descriptionid;

/*add categorygroup table*/
create table categorygroup (
    categorygroupid int(11) not null,
    categorytabid int(11),
    companyid int(11) not null,
    label varchar(50) not null,
    sequence int(11) not null,
    tableid varchar(8) not null,
    version int(11) not null,
    Primary Key (categorygroupid))ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 /*add categorytab table*/
create table categorytab(
    categorytabid int(11) not null,
    companyid int(11) not null,
    label varchar(50) not null,
    sequence int(11) not null,
    tableid varchar(8) not null,
    version int(11) not null,
    Primary Key(categorytabid))ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table category add Foreign Key (categorygroupid) references categorygroup (categorygroupid);
Alter table categorygroup add Foreign Key (categorytabid) references categorytab (categorytabid);
Alter table categorygroup add Foreign Key (companyid) references company (companyid);
Alter table categorytab add Foreign Key (companyid) references company (companyid);

/*add new categoryrelation table*/
create table categoryrelation (
    categoryid int(11) not null,
    categoryvalueid int(11) not null,
    companyid int(11) not null,
    Primary Key (categoryid, categoryvalueid))ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table categoryrelation add Foreign Key (companyid) references company (companyid);
Alter table categoryrelation add Foreign Key (categoryid) references category (categoryid);
Alter table categoryrelation add Foreign Key (categoryvalueid) references categoryvalue (categoryvalueid);
/*add new data types*/
Alter table categfieldvalue add freetextid int default null after fieldvalueid;
Alter table categfieldvalue add attachid int default null after addressid;
Alter table categfieldvalue add linkvalue varchar(100) default null after integervalue;

Alter table categfieldvalue add Foreign Key (freetextid) references freetext (freetextid);
Alter table categfieldvalue add Foreign Key (attachid) references freetext (freetextid);

/*add accessright for categorytab and category group*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
        values(15,'CATEGORYTAB','Category tab accessright',98,1,'Catalogs.CategoryTab');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
        values(15,'CATEGORYGROUP','Category group accessright',99,1,'Catalogs.CategoryGroup');


/*miky:20080610*/
/*create massive communication data base structure*/

Create table campgeneration (
	activityid Int,
	companyid Int NOT NULL,
	generationid Int NOT NULL,
	generationtime Bigint NOT NULL,
	templateid Int,
	userid Int NOT NULL,
 Primary Key (generationid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Dynamic;

Create table campgentext (
	campgentextid Int NOT NULL,
	companyid Int NOT NULL,
	freetextid Int,
	generationid Int NOT NULL,
	isdefault Tinyint NOT NULL,
	languageid Int NOT NULL,
 Primary Key (campgentextid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Dynamic;

Create table attachment (
	attachmentid Int NOT NULL,
	attachmenttype Smallint NOT NULL,
	companyid Int NOT NULL,
	contenttype Varchar(50),
	filename Varchar(100),
	filesize Int,
	freetextid Int,
	version Int NOT NULL,
 Primary Key (attachmentid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Dynamic;

Create table campgenattach (
	attachmentid Int NOT NULL,
	companyid Int NOT NULL,
	generationid Int NOT NULL,
 Primary Key (attachmentid,generationid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Dynamic;

/*add columns in campactivcontact*/
Alter Table campactivcontact add fromemail Varchar(250) after contactid;
Alter Table campactivcontact add generationid Int after fromemail;

/*foreign relations*/
Alter table campactivcontact add Foreign Key (generationid) references campgeneration (generationid) on delete  restrict on update  restrict;
Alter table campgentext add Foreign Key (generationid) references campgeneration (generationid) on delete  restrict on update  restrict;
Alter table campgentext add Foreign Key (freetextid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter table campgentext add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table campgentext add Foreign Key (languageid) references language (languageid) on delete  restrict on update  restrict;
Alter table campgenattach add Foreign Key (generationid) references campgeneration (generationid) on delete  restrict on update  restrict;
Alter table campgenattach add Foreign Key (attachmentid) references attachment (attachmentid) on delete  restrict on update  restrict;
Alter table campgenattach add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table campgeneration add Foreign Key (activityid) references campactivity (activityid) on delete  restrict on update  restrict;
Alter table campgeneration add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;
Alter table campgeneration add Foreign Key (templateid) references camptemplate (templateid) on delete  restrict on update  restrict;
Alter table campgeneration add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table attachment add Foreign Key (freetextid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter table attachment add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;

/*initialize pk sequence*/
insert into sequence (name, sequencenumber) values ('attachment', 0);
insert into sequence (name, sequencenumber) values ('campgeneration', 0);
insert into sequence (name, sequencenumber) values ('campgentext', 0);


/*miky:20080612*/
/*drop column used in batch process*/
alter table scheduleraccess drop column tempbatch;

/*Updating application and database versions */
update systemconstant set value='2.14' where name='APP_VERSION';
update systemconstant set value='2.12' where name='DB_VERSION';

/*ivan:20080614*/
/*add column to file name in attach file*/
alter table categfieldvalue add column filename varchar(250) default null after fieldvalueid;