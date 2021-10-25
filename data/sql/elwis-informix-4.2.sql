/*miky:20090610, fix wrong task*/
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

/*
Ivan:20090610
Modify structure for tables:
action,  actionposition, sale and saleposition.
*/
alter table action drop discount;
alter table action drop discounttype;
alter table action add currencyid integer before followupdate;
alter table action add netgross smallint before number;
alter table action add Constraint Foreign Key (currencyid) references currency (currencyid);

update action set netgross = 1;

alter table actionposition drop productversion;
alter table actionposition add discount decimal(13,2) before positionid;
alter table actionposition add number nvarchar(40,0) before positionid;

alter table saleposition drop invoicetoid;
alter table saleposition add vatid integer before version;
alter table saleposition add paymethod smallint before productid;

alter table sale add netgross smallint before processid;

/*
miky:20090615
required changes in columns and table names in mysql5.
*/
RENAME COLUMN mail.inout to isinout;
RENAME COLUMN contact.inout to isinout;

RENAME TABLE condition to filtercondition;

/*
Ivan:20090615
add contactid column in sale table to relate action and sale
*/
alter table sale add contactid integer before contactpersonid;
alter table sale add Constraint Foreign Key (contactid, processid) references action (contactid, processid);

/*
Alvaro:20090618
remove folder fields (not required anymore)
*/
alter table folder drop mailsnumber;
alter table folder drop totalsize;

/*
miky:20090630
template image structure
*/
Create Table imagestore (
companyid Integer NOT NULL,
filedata Blob NOT NULL,
filename NVarchar(255,0) NOT NULL,
imagestoreid Integer NOT NULL,
imagetype Smallint NOT NULL,
sessionid NVarchar(60,0) NOT NULL,
Primary Key (imagestoreid)) LOCK MODE ROW;

Create Table campaigntextimg (
companyid Integer NOT NULL,
imagestoreid Integer NOT NULL,
languageid Integer NOT NULL,
templateid Integer NOT NULL,
Primary Key (imagestoreid,languageid,templateid)) LOCK MODE ROW;

Create Table templatetextimg (
companyid Integer NOT NULL,
imagestoreid Integer NOT NULL,
languageid Integer NOT NULL,
templateid Integer NOT NULL,
Primary Key (imagestoreid,languageid,templateid)) LOCK MODE ROW;

Create Table campgentextimg (
campgentextid Integer NOT NULL,
companyid Integer NOT NULL,
imagestoreid Integer NOT NULL,
Primary Key (campgentextid,imagestoreid)) LOCK MODE ROW;

Alter Table imagestore add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campaigntextimg add Constraint Foreign Key (companyid) references company (companyid);
Alter Table templatetextimg add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campaigntextimg add Constraint Foreign Key (languageid,templateid) references campaigntext (languageid,templateid);
Alter Table templatetextimg add Constraint Foreign Key (languageid,templateid) references templatetext (languageid,templateid);
Alter Table campaigntextimg add Constraint Foreign Key (imagestoreid) references imagestore (imagestoreid);
Alter Table templatetextimg add Constraint Foreign Key (imagestoreid) references imagestore (imagestoreid);
Alter Table campgentextimg add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campgentextimg add Constraint Foreign Key (imagestoreid) references imagestore (imagestoreid);
Alter Table campgentextimg add Constraint Foreign Key (campgentextid) references campgentext (campgentextid);

DROP TABLE temporalimage;

/*
Ivan:20090630
Add vatid on product table
*/
alter table product add vatid integer before version;
alter table product add Constraint Foreign Key (vatid) references vat (vatid);

/*
Ivan:20090709
Add currencyId in sale table
*/
alter table sale add currencyid integer before customerid;
alter table sale add Constraint Foreign Key (currencyid) references currency (currencyid);

/*miky:20090715*/
Alter Table invoiceposition add salepositionid Integer  before totalprice;
Alter Table invoiceposition add Constraint Foreign Key (salepositionid) references saleposition (salepositionid);

/*
Ivan:20090806
Add discount column in saleposition table
*/
alter table saleposition add discount decimal(13,2) before freetextid;

/*
Ivan:20090810
Alter number column for actionposition
*/
alter table actionposition drop number;
alter table actionposition add number integer before positionid;

/*
Ivan:20090811
Add relation table for action type and sequence.
*/
Create table actiontypeseq (
actiontypeid Integer NOT NULL,
companyid Integer NOT NULL,
numberid Integer NOT NULL,
Primary Key (actiontypeid, numberid)) LOCK MODE ROW;
Alter table actiontypeseq add Constraint Foreign Key (actiontypeid) references actiontype (actiontypeid);
Alter table actiontypeseq add Constraint Foreign Key (numberid) references sequencerule (numberid);
Alter table actiontypeseq add Constraint Foreign Key (companyid) references company (companyid);

/*
miky:20090813
Remove contactmedia table*/
UPDATE contact SET templateid = (SELECT contactmedia.templateid
FROM contactmedia WHERE contactmedia.contactmediaid = contact.contactmediaid)
WHERE EXISTS (SELECT contactmedia.templateid
FROM contactmedia WHERE contactmedia.contactmediaid = contact.contactmediaid);

ALTER TABLE contact drop contactmediaid;
DROP TABLE contactmedia;

/*
Ivan:20090813
Sql alters for netgros price and  netgross total price for actionspositions and salepositions.
*/
alter table actionposition add unitpricegross decimal(10,2) before version;
alter table actionposition add totalpricegross decimal(10,2) before unitid;

update sale set netgross = 1;
alter table saleposition add unitpricegross decimal(10,2) before vatid;
alter table saleposition add totalpricegross decimal(10,2) before unitid;

/*
miky:20090814
Remove editor table and access right
*/
ALTER TABLE template drop editorid;
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
ALTER TABLE product drop sellprice;

/*
ivan:20090825
modify replysubject column in usermail table
*/
alter table usermail modify replysubject nvarchar(250);

/* fer:20090828 Update version constants */
update systemconstant set value='4.2' where name='APP_VERSION';
update systemconstant set value='4.2' where name='DB_VERSION';

UPDATE STATISTICS HIGH;