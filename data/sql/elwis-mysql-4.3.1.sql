/* Fernando: 20110809 adding new French locale */
insert into systemconstant (description, name, resourcekey, type, value) values ('French resource language', 'FRENCH', 'Common.french', 'SYSTEMLANGUAGE', 'fr');

/*
miky:20110907
structure for user change password functionality
*/
Create Table passwordchange (
changetime bigint NOT NULL,
companyid Int NOT NULL,
description Varchar(255) NOT NULL,
passwordchangeid Int NOT NULL,
totaluser Int NOT NULL,
updatedatetime bigint,
userid Int NOT NULL,
version Int NOT NULL,
Primary Key (passwordchangeid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table passwordchange add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table passwordchange add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

Create Table rolepasschange (
companyid Int NOT NULL,
passwordchangeid Int NOT NULL,
roleid Int NOT NULL,
Primary Key (passwordchangeid,roleid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table rolepasschange add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table rolepasschange add Foreign Key (passwordchangeid) references passwordchange (passwordchangeid) on delete  restrict on update  restrict;
Alter Table rolepasschange add Foreign Key (roleid) references role (roleid) on delete  restrict on update  restrict;

Create Table userpasschange (
companyid Int NOT NULL,
passwordchangeid Int NOT NULL,
userid Int NOT NULL,
Primary Key (passwordchangeid,userid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table userpasschange add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table userpasschange add Foreign Key (passwordchangeid) references passwordchange (passwordchangeid) on delete  restrict on update  restrict;
Alter Table userpasschange add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

/*
miky:20110921
password change function access rights
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PASSWORDCHANGE', 'Password change', 127, -308758540, 'PasswordChange.functionality');

/* Fernando: SQL to improve performance */
alter table mailuidltrack add index (companyid, mailaccountid);
alter table mailcontact add index(companyid,mailid,contactid);
alter table mailcontact add index(companyid,mailid);
alter table mailrecipient add index(companyid,mailid);
alter table supportcontact add index(caseid, contactid, activityid);
CREATE INDEX part_of_header ON mail (messageidheader(10));
CREATE INDEX part_of_uidl ON mailuidltrack(uidl(15));



/* Fernando: Update version constants */
update systemconstant set value='4.6' where name='APP_VERSION';
update systemconstant set value='4.3.1' where name='DB_VERSION';


