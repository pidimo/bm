/*
miky:20130327
report artifacts functionality
*/
Create Table reportartifact (
artifactid Int NOT NULL,
companyid Int NOT NULL,
fileid Int,
filename Varchar(150) NOT NULL,
reportid Int NOT NULL,
version Int NOT NULL,
Primary Key (artifactid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table reportartifact add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table reportartifact add Foreign Key (reportid) references report (reportid) on delete  restrict on update  restrict;
Alter Table reportartifact add Foreign Key (fileid) references freetext (freetextid) on delete  restrict on update  restrict;

/*
miky:20130401 versions
*/
update systemconstant set value='4.10.1' where name='APP_VERSION';
update systemconstant set value='4.8.1' where name='DB_VERSION';
