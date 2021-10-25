/*
miky:20130201
campaign sent log function access rights
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(9, 'CAMPAIGNSENTLOG', 'Campaign sent log', 128, 2, 'CampaignSentLog.functionality');

/*
miky:20130206
add cpersonfield and update salutation criteria value
*/
ALTER TABLE campcriterionvalue ADD cpersonfield Varchar(30);

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
ALTER TABLE mail modify state Int NOT NULL;

UPDATE mail SET state = 9 WHERE state = 5;
UPDATE mail SET state = 5 WHERE state = 4;

/*
miky:20130315
report jrxml functionality
*/
ALTER TABLE report ADD jrxmlfileid Int after initialtableref;
ALTER TABLE report ADD jrxmlfilename Varchar(200) after jrxmlfileid;
ALTER TABLE report ADD sourcetype Tinyint DEFAULT 1 NOT NULL after reportid;
ALTER TABLE report add Foreign Key (jrxmlfileid) references freetext (freetextid) on delete  restrict on update  restrict;

Create Table reportqueryparam (
companyid Int NOT NULL,
parametername Varchar(100) NOT NULL,
queryparamid Int NOT NULL,
reportfilterid Int,
reportid Int NOT NULL,
version Int NOT NULL,
Primary Key (queryparamid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table reportqueryparam add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table reportqueryparam add Foreign Key (reportfilterid) references reportfilter (reportfilterid) on delete  restrict on update  restrict;
Alter Table reportqueryparam add Foreign Key (reportid) references report (reportid) on delete  restrict on update  restrict;

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(31, 'JRXMLREPORT', 'Report with jrxml source', 130, -308758540, 'Report.jrxml.external.functionality');

update systemconstant set value='4.10' where name='APP_VERSION';
update systemconstant set value='4.8' where name='DB_VERSION';