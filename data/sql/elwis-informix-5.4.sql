/*
miky:20151027
web documents
*/
Create Table webdocument (
companyid Integer NOT NULL,
name NVarchar(100) NOT NULL,
url NVarchar(250) NOT NULL,
version Integer NOT NULL,
webdocumentid Integer NOT NULL,
Primary Key (webdocumentid)) LOCK MODE ROW;

Alter Table webdocument add Constraint Foreign Key (companyid) references company (companyid);

Create Table webparameter (
companyid Integer NOT NULL,
parametername NVarchar(100) NOT NULL,
variablename NVarchar(100) NOT NULL,
variabletype Smallint NOT NULL,
version Integer NOT NULL,
webdocumentid Integer NOT NULL,
webparameterid Integer NOT NULL,
Primary Key (webparameterid)) LOCK MODE ROW;

Alter Table webparameter add Constraint Foreign Key (companyid) references company (companyid);
Alter Table webparameter add Constraint Foreign Key (webdocumentid) references webdocument (webdocumentid);

/*add in communications*/
Alter Table contact add webdocumentid Integer;
Alter Table contact add webgenerateuuid NVarchar(100);
Alter table contact add Constraint Foreign Key (webdocumentid) references webdocument (webdocumentid);

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'WEBDOCUMENT', 'Web document', 135, 1, 'WebDocument.functionality');


/*
miky:20151030
app versions
*/
update systemconstant set value='5.5' where name='APP_VERSION';
update systemconstant set value='5.4' where name='DB_VERSION';

