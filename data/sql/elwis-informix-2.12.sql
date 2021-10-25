/*Ivan:20080418*/
/*Remove Start range birthday and Finish range birthday from elwisuser*/
alter table elwisuser drop rangebirthdayst;
alter table elwisuser drop rangebirthdayfi;

/*Ivan:20080424*/
/*add companyid in tables*/
/*Table:addressgroup*/
alter table addressgroup add companyid int before groupaddrid;
update addressgroup
set companyid = (
    select addr.companyid
    from mailgroupaddr as addr
    where addr.groupaddrid=addressgroup.groupaddrid);
alter table addressgroup modify companyid int not null;
alter table addressgroup add constraint foreign key (companyid) references company (companyid);

/*Table:recurexception*/
alter table recurexception add companyid int before datevalue;
update recurexception 
set companyid = (
    select ap.companyid
    from appointment as ap
    where ap.appointmentid=recurexception.appointmentid);
alter table recurexception modify companyid int not null;
alter table recurexception add constraint foreign key (companyid) references company (companyid);

/*Table:scheduleduser*/
alter table scheduleduser add companyid int before scheduleduserid;
update scheduleduser
set companyid = (
    select usr.companyid
    from elwisuser as usr
    where usr.userid=scheduleduser.userid);
alter table scheduleduser modify companyid int not null;
alter table scheduleduser add constraint foreign key (companyid) references company (companyid);

/*Table:scheduleraccess*/
alter table scheduleraccess add companyid int before owneruserid;
update scheduleraccess
set companyid = (
    select usr.companyid
    from elwisuser as usr
    where usr.userid=scheduleraccess.userid);
alter table scheduleraccess modify companyid int not null;
alter table scheduleraccess add constraint foreign key (companyid) references company (companyid);

/*Table:usertask*/
alter table usertask add companyid int before freetextid;
update usertask
set companyid = (
    select usr.companyid
    from scheduleduser as usr
    where usr.scheduleduserid=usertask.scheduleduserid);
alter table usertask modify companyid int not null;
alter table usertask add constraint foreign key (companyid) references company (companyid);

/*Table:userrole*/
alter table userrole add companyid int before userid;
update userrole
set companyid = (
    select usr.companyid
    from elwisuser as usr
    where usr.userid=userrole.userid);
alter table userrole modify companyid int not null;
alter table userrole add constraint foreign key (companyid) references company (companyid);

/*Table:userofgroup*/
alter table userofgroup add companyid int before usergroupid;
update userofgroup
set companyid = (
    select usr.companyid
    from elwisuser as usr
    where usr.userid=userofgroup.userid);
alter table userofgroup modify companyid int not null;
alter table userofgroup add constraint foreign key (companyid) references company (companyid);

/*Table:usersession*/
alter table usersession add companyid int before module;
update usersession
set companyid = (
    select usr.companyid
    from elwisuser as usr
    where usr.userid=usersession.userid);
alter table usersession modify companyid int not null;
alter table usersession add constraint foreign key (companyid) references company (companyid);

/*Table:usersessionparam*/
alter table usersessionparam add companyid int before statusname;
update usersessionparam
set companyid = (
    select usr.companyid
    from usersession as usr
    where usr.userid=usersessionparam.userid and usr.statusname=usersessionparam.statusname and usr.module=usersessionparam.module);
alter table usersessionparam modify companyid int not null;
alter table usersessionparam add constraint foreign key (companyid) references company (companyid);

/*miky:20080430*/
/*add columns in reportfilter*/
Alter Table reportfilter add isparameter smallint  default 0 NOT NULL before operator;
Alter Table reportfilter add label nvarchar(150,0) before operator;


/*Ivan:20080505*/
/*add missing relations*/
alter table camptype add constraint foreign key (companyid) references company (companyid);
alter table categfieldvalue add constraint foreign key (companyid) references company (companyid);

/*miky:20080519*/
/*add columns in usermail*/
Alter Table usermail add editorfont nvarchar(100,0) before emptytrashlogout;
Alter Table usermail add editorfontsize nvarchar(30,0) before emptytrashlogout;

/*Ivan:20080527*/
/*New requeriments to categories*/
/*add new fields to category table*/
Alter table category add sequence integer before tableid;
Alter table category add categorygroupid integer before categorytype;
Alter table category add parentcategory integer before sequence;
Alter table category add hassubcategories smallint  default 0 not null before langtextid;

/*add categorygroup table*/
create table categorygroup (
    categorygroupid integer not null,
    categorytabid integer,
    companyid integer not null,
    label nvarchar(50,0) not null,
    sequence integer not null,
    tableid nvarchar(8,0) not null,
    version integer not null,
    Primary Key (categorygroupid)
) LOCK MODE ROW;

/*add categorytab table*/
create table categorytab(
    categorytabid integer not null,
    companyid integer not null,
    label nvarchar(50,0) not null,
    sequence integer not null,
    tableid nvarchar(8,0) not null,
    version integer not null,
    Primary Key(categorytabid)
) LOCK MODE ROW;

Alter table category add Constraint Foreign Key (categorygroupid) references categorygroup (categorygroupid);
Alter table categorygroup add Constraint Foreign Key (categorytabid) references categorytab (categorytabid);
Alter table categorygroup add Constraint Foreign Key (companyid) references company (companyid);
Alter table categorytab add Constraint Foreign Key (companyid) references company (companyid);

/*add new categoryrelation table*/
create table categoryrelation (
    categoryid integer not null,
    categoryvalueid integer not null,
    companyid integer not null,
    Primary Key (categoryid,categoryvalueid)
) LOCK MODE ROW;
Alter table categoryrelation add Constraint Foreign Key (companyid) references company (companyid);
Alter table categoryrelation add Constraint Foreign Key (categoryid) references category (categoryid);
Alter table categoryrelation add Constraint Foreign Key (categoryvalueid) references categoryvalue (categoryvalueid);
/*add new data types*/
Alter table categfieldvalue add freetextid integer  default null before integervalue;
Alter table categfieldvalue add linkvalue nvarchar(100,0) default null  before productid;
Alter table categfieldvalue add attachid integer  default null before categoryid;

Alter table categfieldvalue add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table categfieldvalue add Constraint Foreign Key (attachid) references freetext (freetextid);

/*add accessright for categorytab and category group*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
    values(15,'CATEGORYTAB','Category tab accessright',98,1,'Catalogs.CategoryTab');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
    values(15,'CATEGORYGROUP','Category group accessright',99,1,'Catalogs.CategoryGroup');


/*miky:20080610*/
/*create massive communication data base structure*/

Create Table campgeneration (
	activityid Integer,
	companyid Integer NOT NULL,
	generationid Integer NOT NULL,
	generationtime Int8 NOT NULL,
	templateid Integer,
	userid Integer NOT NULL,
Primary Key (generationid)
)
LOCK MODE ROW;

Create Table campgentext (
	campgentextid Integer NOT NULL,
	companyid Integer NOT NULL,
	freetextid Integer,
	generationid Integer NOT NULL,
	isdefault Smallint NOT NULL,
	languageid Integer NOT NULL,
Primary Key (campgentextid)
)
LOCK MODE ROW;

Create Table attachment (
	attachmentid Integer NOT NULL,
	attachmenttype Smallint NOT NULL,
	companyid Integer NOT NULL,
	contenttype NVarchar(50,0),
	filename NVarchar(100,0),
	filesize Integer,
	freetextid Integer,
	version Integer NOT NULL,
Primary Key (attachmentid)
)
LOCK MODE ROW;

Create Table campgenattach (
	attachmentid Integer NOT NULL,
	companyid Integer NOT NULL,
	generationid Integer NOT NULL,
Primary Key (attachmentid,generationid)
)
LOCK MODE ROW;


/*add columns in campactivcontact*/
Alter Table campactivcontact add fromemail nvarchar(250,0) before taskid;
Alter Table campactivcontact add generationid Integer before taskid;

/*foreign relations*/
Alter Table campactivcontact add Constraint Foreign Key (generationid) references campgeneration (generationid);
Alter Table campgentext add Constraint Foreign Key (generationid) references campgeneration (generationid);
Alter Table campgentext add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter Table campgentext add Constraint Foreign Key (languageid) references language (languageid);
Alter Table campgentext add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campgenattach add Constraint Foreign Key (generationid) references campgeneration (generationid);
Alter Table campgenattach add Constraint Foreign Key (attachmentid) references attachment (attachmentid);
Alter Table campgenattach add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campgeneration add Constraint Foreign Key (activityid) references campactivity (activityid);
Alter Table campgeneration add Constraint Foreign Key (userid) references elwisuser (userid);
Alter Table campgeneration add Constraint Foreign Key (templateid) references camptemplate (templateid);
Alter Table campgeneration add Constraint Foreign Key (companyid) references company (companyid);
Alter Table attachment add Constraint Foreign Key (companyid) references company (companyid);
Alter Table attachment add Constraint Foreign Key (freetextid) references freetext (freetextid);

/*initialize pk sequence*/
insert into sequence (name, sequencenumber) values ('attachment', 0);
insert into sequence (name, sequencenumber) values ('campgeneration', 0);
insert into sequence (name, sequencenumber) values ('campgentext', 0);

/*miky:20080612*/
/*drop column used in batch process*/
alter table scheduleraccess drop tempbatch;


update systemconstant set value='2.14' where name='APP_VERSION';
update systemconstant set value='2.12' where name='DB_VERSION';

/*ivan:20080614*/
/*add column to file name in attach file*/
alter table categfieldvalue add  filename nvarchar(200,0)  default null before freetextid;

UPDATE STATISTICS HIGH;
