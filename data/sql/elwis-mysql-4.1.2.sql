/*
Ivan:20090624
add uidl column in mail table, and add uidl table for backtracking of download emails
*/
alter table mail add column uidl varchar(250) after subject;
alter table mail add column processtosent tinyint after priority;

create table mailuidltrack(
companyid int not null,
mailaccountid int not null,
uidl varchar(250),
uidltrackid int not null,
Primary Key (uidltrackid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table mailuidltrack add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table mailuidltrack add Foreign Key (mailaccountid) references mailaccount (mailaccountid) on delete  restrict on update  restrict;

/* ivan:20090625 Update version constants */
update systemconstant set value='4.1.3' where name='APP_VERSION';
update systemconstant set value='4.1.2' where name='DB_VERSION';