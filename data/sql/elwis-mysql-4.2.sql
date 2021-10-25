/*miky:20090610, fix wrong task*/
/*create temporal table to save freetext ids*/
create table temporaltable(
tempid Integer NOT NULL,
Primary Key (tempid));

/*insert user task freetext ids*/
INSERT INTO temporaltable (tempid) SELECT freetextid FROM usertask WHERE freetextid IS NOT NULL AND scheduleduserid
IN (SELECT su.scheduleduserid FROM scheduleduser AS su WHERE su.userid IS NULL AND su.taskid IS NOT NUll);

/*delete user task*/
DELETE FROM usertask WHERE scheduleduserid IN (SELECT su.scheduleduserid FROM scheduleduser AS su WHERE su.userid IS NULL AND su.taskid IS NOT NUll);

/*delete scheduled user task*/
DELETE FROM scheduleduser WHERE userid IS NULL AND taskid IS NOT NUll;

/*insert task freetext ids*/
INSERT INTO temporaltable (tempid) SELECT freetextid FROM task WHERE freetextid IS NOT NULL AND taskid NOT IN (SELECT taskid FROM scheduleduser where taskid IS NOT NULL);

/*delete task*/
DELETE FROM task WHERE taskid NOT IN (SELECT taskid FROM scheduleduser where taskid IS NOT NULL);

/*delete freetexts*/
DELETE FROM freetext WHERE freetextid IN (SELECT tempid FROM temporaltable);

/*drop temporaltable*/
DROP TABLE temporaltable;

/*fix field userid in scheduleduser*/
ALTER TABLE scheduleduser MODIFY userid int(11) NOT NULL;

/*
Ivan:20090610
Modify structure for tables:
action,  actionposition, sale and saleposition.
*/
alter table action drop column discount;
alter table action drop column discounttype;
alter table action add currencyid int after contactid;
alter table action add netgross smallint after followupdate;
alter table action add Foreign Key (currencyid) references currency (currencyid) on delete  restrict on update restrict;

update action set netgross = 1;

alter table actionposition drop column productversion;
alter table actionposition add discount decimal(13,2) after descriptionid;
alter table actionposition add number varchar(40) after discount;

/*I need known invoiceid fk key name for mysql*/
alter table saleposition drop Foreign Key saleposition_ibfk_5;
alter table saleposition drop column invoicetoid;

alter table saleposition add vatid int after unitprice;
alter table saleposition add paymethod smallint after freetextid;

alter table sale add column netgross smallint after freetextid;

/*
miky:20090615
required changes in columns and table names in mysql5. 
*/
ALTER TABLE mail CHANGE `inout` isinout int(11) default NULL;
ALTER TABLE contact CHANGE `inout` isinout char(1) default NULL AFTER isaction;

ALTER TABLE `condition` RENAME TO filtercondition;

/*
Ivan:20090615
add contactid column in sale table to relate action and sale
*/
alter table sale add column contactid int after companyid;
alter table sale add Foreign Key (contactid, processid) references action (contactid, processid) on delete  restrict on update restrict;

/*
Alvaro:20090618
remove folder fields (not required anymore)
*/
alter table folder drop column mailsnumber;
alter table folder drop column totalsize;

/*
miky:20090630
template image structure
*/
Create table imagestore (
companyid Int NOT NULL,
filedata Longblob NOT NULL,
filename Varchar(255) NOT NULL,
imagestoreid Int NOT NULL,
imagetype Smallint NOT NULL,
sessionid Varchar(60) NOT NULL,
Primary Key (imagestoreid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Create table campaigntextimg (
companyid Int NOT NULL,
imagestoreid Int NOT NULL,
languageid Int NOT NULL,
templateid Int NOT NULL,
Primary Key (imagestoreid,languageid,templateid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Create table templatetextimg (
companyid Int NOT NULL,
imagestoreid Int NOT NULL,
languageid Int NOT NULL,
templateid Int NOT NULL,
Primary Key (imagestoreid,languageid,templateid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Create table campgentextimg (
campgentextid Int NOT NULL,
companyid Int NOT NULL,
imagestoreid Int NOT NULL,
Primary Key (campgentextid,imagestoreid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table imagestore add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table campaigntextimg add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table templatetextimg add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table campaigntextimg add Foreign Key (languageid,templateid) references campaigntext (languageid,templateid) on delete  restrict on update  restrict;
Alter table templatetextimg add Foreign Key (languageid,templateid) references templatetext (languageid,templateid) on delete  restrict on update  restrict;
Alter table campaigntextimg add Foreign Key (imagestoreid) references imagestore (imagestoreid) on delete  restrict on update  restrict;
Alter table templatetextimg add Foreign Key (imagestoreid) references imagestore (imagestoreid) on delete  restrict on update  restrict;
Alter table campgentextimg add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table campgentextimg add Foreign Key (imagestoreid) references imagestore (imagestoreid) on delete  restrict on update  restrict;
Alter table campgentextimg add Foreign Key (campgentextid) references campgentext (campgentextid) on delete  restrict on update  restrict;

DROP TABLE temporalimage;

/*
Ivan:20090630
Add vatid on product table
*/
alter table product add column vatid int after unitid;
alter table product add Foreign Key (vatid) references vat (vatid) on delete  restrict on update restrict;

/*
Ivan:20090709
Add currencyId in sale table
*/
alter table sale add currencyid int  after contactpersonid;
alter table sale add Foreign Key (currencyid) references currency (currencyid) on delete  restrict on update restrict;

/*miky:20090715*/
Alter Table invoiceposition add salepositionid Int after quantity;
Alter table invoiceposition add Foreign Key (salepositionid) references saleposition (salepositionid) on delete  restrict on update  restrict;

/*
Ivan:20090806
Add discount column in saleposition table
*/
alter table saleposition add discount decimal(13,2) after deliverydate;

/*
Ivan:20090810
Alter number column for actionposition
*/
alter table actionposition drop column number;
alter table actionposition add number int(11) after discount;

/*
Ivan:20090811
Add relation table for action type and sequence.
*/
Create table actiontypeseq (
actiontypeid Int NOT NULL,
companyid Int NOT NULL,
numberid Int NOT NULL,
Primary Key (actiontypeid, numberid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;
Alter table actiontypeseq add Foreign Key (actiontypeid) references actiontype (actiontypeid) on delete  restrict on update  restrict;
Alter table actiontypeseq add Foreign Key (numberid) references sequencerule (numberid) on delete  restrict on update  restrict;
Alter table actiontypeseq add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;

/*
miky:20090813
Remove contactmedia table*/
UPDATE contact SET templateid = (SELECT contactmedia.templateid
FROM contactmedia WHERE contactmedia.contactmediaid = contact.contactmediaid)
WHERE EXISTS (SELECT contactmedia.templateid
FROM contactmedia WHERE contactmedia.contactmediaid = contact.contactmediaid);

ALTER TABLE contact DROP FOREIGN KEY 0_2737;
alter table contact drop column contactmediaid;
DROP TABLE contactmedia;

/*
Ivan:20090813
Sql alters for netgros price and  netgross total price for actionspositions and salepositions.
*/
alter table actionposition add column unitpricegross decimal(10,2) after unitid;
alter table actionposition add column totalpricegross decimal(10,2) after totalprice;

update sale set netgross = 1;
alter table saleposition add column unitpricegross decimal(10,2) after unitprice;
alter table saleposition add column totalpricegross decimal(10,2) after totalprice;

/*
miky:20090814
Remove editor table and access right
*/
ALTER TABLE template DROP FOREIGN KEY 0_2848;
alter table template drop column editorid;
DROP TABLE editor;

/*CONTACTMEDIA*/
DELETE FROM accessrights WHERE functionid = 24;
DELETE FROM systemfunction WHERE functionid = 24;
/*EDITOR*/
DELETE FROM accessrights WHERE functionid = 25;
DELETE FROM systemfunction WHERE functionid = 25;

/*
Ivan:20090814
Alter unitprice and totalprice columns in saleposition table
*/
alter table saleposition modify unitprice decimal(10,2);
alter table saleposition modify totalprice decimal(10,2);

/*
miky:20090821
remove sellprice column
*/
alter table product drop column sellprice;

/*
ivan:20090825
modify replysubject column in usermail table
*/
alter table usermail modify replysubject varchar(250);

/* fer:20090828 Update version constants */
update systemconstant set value='4.2' where name='APP_VERSION';
update systemconstant set value='4.2' where name='DB_VERSION';