/*
miky:20130327
report artifacts functionality
*/
Create Table reportartifact (
artifactid Integer NOT NULL,
companyid Integer NOT NULL,
fileid Integer,
filename NVarchar(150) NOT NULL,
reportid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (artifactid)) LOCK MODE ROW;

Alter Table reportartifact add Constraint Foreign Key (companyid) references company (companyid);
Alter Table reportartifact add Constraint Foreign Key (reportid) references report (reportid);
Alter Table reportartifact add Constraint Foreign Key (fileid) references freetext (freetextid);

/*
miky:20130401 versions
*/
update systemconstant set value='4.10.1' where name='APP_VERSION';
update systemconstant set value='4.8.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;
