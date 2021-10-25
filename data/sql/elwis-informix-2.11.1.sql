/*Ivan:20080313*/
/*add hiden column in database*/
Alter Table mail add hidden integer before inout;
update mail set hidden=0;

/*miky:20080312*/
/*add column in scheduler access*/
Alter Table scheduleraccess add privpermission integer;
Alter Table scheduleraccess add tempbatch nvarchar(20,0) default 'FALSE';

/*Fer:20080401*/
/*removing unused usermail columns*/
alter table usermail drop email;
alter table usermail drop password;
alter table usermail drop popport;
alter table usermail drop popserver;
alter table usermail drop sslconnection;
alter table usermail drop useraccount;

/*Ivan:20080403*/
/*dataimport accessright*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(16,'CONTACTIMPORT','Contact data import',97,1,'contact.dataImport');


update systemconstant set value='2.13.1' where name='APP_VERSION';
update systemconstant set value='2.11.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;