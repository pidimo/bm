/*
Ivan:20090602
Added option to enable or disable automatic download of emails
*/
alter table usermail add bgdownload Smallint default NULL before companyid;
update usermail set bgdownload=0;
update usermail set showpopmsgs=0;

/*
Ivan:20090604
Add table appsignature
*/
create table appmailsignature(
enabled smallint,
htmlsignature blob,
languageiso nvarchar(10,0) NOT NULL,
textsignature blob,
Primary Key (languageiso)) LOCK MODE ROW;

insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(5,'APPLICATIONSIGNATURE','Application email signatures view and update accessright',119,-308758540,'ApplicationSignature.accessRight.title');

/* Fer:20090605 Update version constants */
update systemconstant set value='4.1.1' where name='APP_VERSION';
update systemconstant set value='4.1.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;