/* Fernando: 20110809 adding new French locale */
insert into systemconstant (description, name, resourcekey, type, value) values ('French resource language', 'FRENCH', 'Common.french', 'SYSTEMLANGUAGE', 'fr');

/*
miky:20110907
structure for user change password functionality
*/
Create Table passwordchange (
changetime Int8 NOT NULL,
companyid Integer NOT NULL,
description NVarchar(255,0) NOT NULL,
passwordchangeid Integer NOT NULL,
totaluser Integer NOT NULL,
updatedatetime Int8,
userid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (passwordchangeid)) LOCK MODE ROW;

Alter Table passwordchange add Constraint Foreign Key (companyid) references company (companyid);
Alter Table passwordchange add Constraint Foreign Key (userid) references elwisuser (userid);

Create Table rolepasschange (
companyid Integer NOT NULL,
passwordchangeid Integer NOT NULL,
roleid Integer NOT NULL,
Primary Key (passwordchangeid,roleid)) LOCK MODE ROW;

Alter Table rolepasschange add Constraint Foreign Key (companyid) references company (companyid);
Alter Table rolepasschange add Constraint Foreign Key (passwordchangeid) references passwordchange (passwordchangeid);
Alter Table rolepasschange add Constraint Foreign Key (roleid) references role (roleid);

Create Table userpasschange (
companyid Integer NOT NULL,
passwordchangeid Integer NOT NULL,
userid Integer NOT NULL,
Primary Key (passwordchangeid,userid)) LOCK MODE ROW;

Alter Table userpasschange add Constraint Foreign Key (companyid) references company (companyid);
Alter Table userpasschange add Constraint Foreign Key (passwordchangeid) references passwordchange (passwordchangeid);
Alter Table userpasschange add Constraint Foreign Key (userid) references elwisuser (userid);

/*
miky:20110921
password change function access rights
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PASSWORDCHANGE', 'Password change', 127, -308758540, 'PasswordChange.functionality');



/* Fernando: SQL to improve performance */
create index mailuidltrack_mailaccount on mailuidltrack(companyid, mailaccountid);
create index mailcontact_idx1 on  mailcontact(companyid,mailid,contactid);
create index mailcontact_idx2 on mailcontact(companyid,mailid);
create index mailrecipient_ix1 on mailrecipient(companyid,mailid);
create index supportcontact_idx1 on supportcontact(caseid, contactid, activityid);
/*create index mail_idx1 on mail(messageidheader(10));*/
/*CREATE INDEX part_of_uidl ON mailuidltrack(uidl(15));*/

/* Fernando: Update version constants */
update systemconstant set value='4.6' where name='APP_VERSION';
update systemconstant set value='4.3.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;
