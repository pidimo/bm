/*
miky:20151027
web documents
*/
Create Table webdocument (
companyid Int NOT NULL,
name Varchar(100) NOT NULL,
url Varchar(250) NOT NULL,
version Int NOT NULL,
webdocumentid Int NOT NULL,
Primary Key (webdocumentid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table webdocument add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;

Create Table webparameter (
companyid Int NOT NULL,
parametername Varchar(100) NOT NULL,
variablename Varchar(100) NOT NULL,
variabletype Smallint NOT NULL,
version Int NOT NULL,
webdocumentid Int NOT NULL,
webparameterid Int NOT NULL,
Primary Key (webparameterid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table webparameter add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table webparameter add Foreign Key (webdocumentid) references webdocument (webdocumentid) on delete  restrict on update  restrict;

/*add in communications*/
Alter Table contact add webdocumentid Int after version;
Alter Table contact add webgenerateuuid Varchar(100) after webdocumentid;
Alter table contact add Foreign Key (webdocumentid) references webdocument (webdocumentid) on delete  restrict on update  restrict;

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'WEBDOCUMENT', 'Web document', 135, 1, 'WebDocument.functionality');


/*
miky:20151030
app versions
*/
update systemconstant set value='5.5' where name='APP_VERSION';
update systemconstant set value='5.4' where name='DB_VERSION';
