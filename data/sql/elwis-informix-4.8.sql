/*
miky:20130201
campaign sent log function access rights
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(9, 'CAMPAIGNSENTLOG', 'Campaign sent log', 128, 2, 'CampaignSentLog.functionality');

/*
miky:20130206
add cpersonfield and update salutation criteria value
*/
ALTER TABLE campcriterionvalue ADD cpersonfield nvarchar(30);

update campcriterionvalue set tableid = 5 where campcriterionvalueid = 28;
update campcriterionvalue set cpersonfield = 'cpSalutationId' where campcriterionvalueid = 28;

/*
miky:20130208
communication overview function access rights
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'COMMUNICATIONOVERVIEW', 'Communication overview', 129, 1, 'Communication.overview.functionality');

/*
miky:20130226
modify mail state
*/
ALTER TABLE mail modify state Integer NOT NULL;

UPDATE mail SET state = 9 WHERE state = 5;
UPDATE mail SET state = 5 WHERE state = 4;

/*
miky:20130315
report jrxml functionality
*/
ALTER TABLE report ADD jrxmlfileid Integer before module;
ALTER TABLE report ADD jrxmlfilename NVarchar(200) before module;
ALTER TABLE report ADD sourcetype Smallint DEFAULT 1 NOT NULL before state;
ALTER TABLE report add Constraint Foreign Key (jrxmlfileid) references freetext (freetextid);

Create Table reportqueryparam (
companyid Integer NOT NULL,
parametername NVarchar(100) NOT NULL,
queryparamid Integer NOT NULL,
reportfilterid Integer,
reportid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (queryparamid)) LOCK MODE ROW;

Alter Table reportqueryparam add Constraint Foreign Key (companyid) references company (companyid);
Alter Table reportqueryparam add Constraint Foreign Key (reportfilterid) references reportfilter (reportfilterid);
Alter Table reportqueryparam add Constraint Foreign Key (reportid) references report (reportid);

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(31, 'JRXMLREPORT', 'Report with jrxml source', 130, -308758540, 'Report.jrxml.external.functionality');


update systemconstant set value='4.10' where name='APP_VERSION';
update systemconstant set value='4.8' where name='DB_VERSION';

UPDATE STATISTICS HIGH;