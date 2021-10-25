/*
Ivan:20090624
add uidl column in mail table, and add uidl table for backtracking of download emails
*/
alter table mail add uidl nvarchar(250,0);
alter table mail add processtosent smallint before saveemail;

create table mailuidltrack(
companyid integer not null,
mailaccountid integer not null,
uidl nvarchar(250,0),
uidltrackid integer not null,
Primary Key (uidltrackid)) LOCK MODE ROW;

Alter table mailuidltrack add constraint foreign key (companyid) references company (companyid);
Alter table mailuidltrack add constraint foreign key (mailaccountid) references mailaccount (mailaccountid);

/* ivan:20090625 Update version constants */
update systemconstant set value='4.1.3' where name='APP_VERSION';
update systemconstant set value='4.1.2' where name='DB_VERSION';

UPDATE STATISTICS HIGH;