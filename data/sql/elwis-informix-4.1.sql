/*miky:20090327*/
alter table appointment add createdbyid Integer default 0 NOT NULL  before enddate;
update appointment set createdbyid=userid;
Alter Table appointment add Constraint Foreign Key (createdbyid) references elwisuser (userid);

/*miky:20090403*/
Alter Table folder add columntoshow smallint  before date;
UPDATE folder SET columntoshow = 1 WHERE type = 0;
UPDATE folder SET columntoshow = 1 WHERE type = 1;
UPDATE folder SET columntoshow = 2 WHERE type = 2;
UPDATE folder SET columntoshow = 2 WHERE type = 3;
UPDATE folder SET columntoshow = 1 WHERE type = 4;
UPDATE folder SET columntoshow = 2 WHERE type = 5;

/*miky:20090409*/
alter table category add secondgroupid Integer  before sequence;
Alter Table category add Constraint Foreign Key (secondgroupid) references categorygroup (categorygroupid);

/*Ivan:20090421
Add saveemail column in mail table
*/
alter table mail add saveemail Smallint before sentdatetime;
/* add recipaddress this table allow know mailrecipients address or contactpersons selected from ui*/
create table recipaddress(
addressid Integer,
contactpersonid Integer,
companyid Integer NOT NULL,
recipaddressid Integer NOT NULL,
recipientid Integer NOT NULL,
Primary Key (recipaddressid)) LOCK MODE ROW;

Alter table recipaddress add Constraint Foreign Key (companyid) references company (companyid);
Alter table recipaddress add Constraint Foreign Key (recipientid) references mailrecipient (recipientid);

/*
Ivan:20090422
Add createcontact column in mail table
*/
alter table mail add createcontact Smallint before folderid;

/*
Ivan:20090424
add table for account errors when upload or download emails
*/
create table mailaccerror(
companyid Integer NOT NULL,
errortype Smallint NOT NULL,
mailaccerrorid Integer NOT NULL,
mailaccountid Integer,
timeerror Int8 NOT NULL,
usermailid Integer NOT NULL,
Primary Key (mailaccerrorid)) LOCK MODE ROW;

Alter table mailaccerror add Constraint Foreign Key (companyid) references company (companyid);
Alter table mailaccerror add Constraint Foreign Key (mailaccountid) references mailaccount (mailaccountid);
Alter table mailaccerror add Constraint Foreign Key (usermailid) references usermail (usermailid);

/*
Ivan:20090501
Modify project table columns plannedinvoice and plannednoinvoice to support null values
*/
alter table project modify plannedinvoice Decimal(6,1) DEFAULT NULL;
alter table project modify plannednoinvoice Decimal(6,1) DEFAULT NULL;

/*
Ivan:20090504
 add column lastdownloadtime in mailaccount table
 add column automaticforward in usermail table
 add column automaticreply in usermail table
 add column replymessage in usermail table
 add column showpopmsgs in usermail table
*/
alter table mailaccount add lastdownloadtime Int8 before login;
alter table usermail add automaticforward Smallint default 0 before companyid;
alter table usermail add automaticreply Smallint default 0 before companyid;
alter table usermail add replymessage nvarchar(250,0) before replymode;
alter table usermail add replysubject nvarchar(100,0) before savesenditem;
alter table usermail add showpopmsgs Smallint default 0 before usermailid;
/*
Ivan:20090505
add column keepemailserver in usermail
add column forwardemail in usermail
add column messageidheader in mail table
add column attachment in mail table
*/
alter table usermail add forwardemail nvarchar(250,0) before replymessage;
alter table usermail add keepemailserver Smallint default 0 before replymessage;
alter table mail add messageidheader nvarchar(250,0) before priority;
alter table mail add attachment Smallint before bodyid;

/*
Ivan:20090507
add column newemail in mail table
add column completeemail in mail table
*/
alter table mail add newemail Smallint default 0 before priority;
alter table mail add completeemail Smallint default 1 before createcontact;

/*
Ivan:20090508
add table emailsource to store emails downloated
*/
create table emailsource(
companyid Integer NOT NULL,
filesize Integer NOT NULL,
mailid Integer NOT NULL,
source blob NOT NULL,
Primary Key (mailid)) LOCK MODE ROW;

Alter table emailsource add Constraint Foreign Key (companyid) references company (companyid);
Alter table emailsource add Constraint Foreign Key (mailid) references mail (mailid);

/*miky:20090508*/
Alter Table template add mediatype smallint DEFAULT 0  NOT NULL before templateid;

/*Ivan:20090512*/
insert into sequence (name, sequencenumber) values ('mailaccerror', 0);
insert into sequence (name, sequencenumber) values ('recipaddress', 0);

/*Alvaro:20090513*/
Alter table folder add isopen smallint default 0 not null before name;
Alter table folder add parentid integer before type;
Alter table folder add Constraint Foreign Key (parentid) references folder (folderid);
/*Alvaro:20090520*/
Alter table productcontract add noteid integer before openamount;
Alter table productcontract add Constraint Foreign Key (noteid) references freetext (freetextid);

/*Alvaro: 20090522*/
update sequence set name="incomingpayment" where name="incomingPayment";

/*Ivan:20090523 modify default values*/
alter table usermail modify automaticforward Smallint default NULL;
alter table usermail modify automaticreply Smallint default NULL;
alter table usermail modify showpopmsgs Smallint default NULL;
alter table usermail modify keepemailserver Smallint default NULL;
alter table mail modify newemail Smallint default NULL;
alter table mail modify completeemail Smallint default NULL;

update usermail set showpopmsgs = 1;

/*Alvaro: 20090525*/
Alter table folder add totalsize int8 before type;
Alter table folder add mailsnumber integer before name;

/*miky:20090526*/
create table temporalimage(
companyid Integer NOT NULL,
filedata blob NOT NULL,
filename nvarchar(255,0) NOT NULL,
sessionid nvarchar(60,0) NOT NULL,
temporalimageid Integer NOT NULL,
Primary Key (temporalimageid)) LOCK MODE ROW;

Alter table temporalimage add Constraint Foreign Key (companyid) references company (companyid);

/*miky:20090529*/
alter table company add salutationid Integer  before startlicense;
Alter Table company add Constraint Foreign Key (salutationid) references salutation (salutationid);

/*miky:20090529, delete filters related to body email*/
delete from condition where namekey = 4;
delete from filter where filterid NOT IN (select filterid from condition);

/* Fer:20090529 Update version constants */
update systemconstant set value='4.1' where name='APP_VERSION';
update systemconstant set value='4.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;