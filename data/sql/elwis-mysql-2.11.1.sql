/*Ivan:20080313*/
/*add hiden column in database*/
Alter table mail add column hidden smallint null after folderid;
update mail set hidden = 0;

/*miky:20080312*/
/*add column in scheduler access*/
Alter Table scheduleraccess add privpermission Int default NULL after permission;
Alter Table scheduleraccess add tempbatch Varchar(10) default 'FALSE' after privpermission;

/*Fer:20080401*/
/*removing unused usermail columns*/
alter table usermail drop column email;
alter table usermail drop column password;
alter table usermail drop column popport;
alter table usermail drop column popserver;
alter table usermail drop column sslconnection;
alter table usermail drop column useraccount;

/*Ivan:20080403*/
/*dataimport accessright*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(16,'CONTACTIMPORT','Contact data import',97,1,'contact.dataImport');

/*Updating application and database versions */
update systemconstant set value='2.13.1' where name='APP_VERSION';
update systemconstant set value='2.11.1' where name='DB_VERSION';
