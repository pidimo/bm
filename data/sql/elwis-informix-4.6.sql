/*
miky:20120829
modify customertypename
*/
ALTER TABLE customertype modify customertypename nvarchar(40);

/*
miky:20120831
insert new criterion value 'salutation'
*/
INSERT INTO campcriterionvalue(campcriterionvalueid, tableid, descriptionkey, field, fieldtype, fieldname, tablename, relationfield)
VALUES(28, 4 , "Contact.Person.salutation", "salutationid", 6, "salutationlabel", "salutation", null);

/*
miky:20120925
add new field csv delimiter field
*/
ALTER TABLE elwisuser ADD csvdelimiter nvarchar(5) before dayfragmentation;

/*
miky:20121004
add new field report charset
*/
ALTER TABLE elwisuser ADD reportcharset nvarchar(30) before rowsperpage;

/*
miky:20121031
modify time in project time
*/
ALTER TABLE projecttime modify time Decimal(4,2) NOT NULL;

/*
miky:20121128
structure for manage campaign sent email log
*/
Alter Table campgeneration ADD campaignid Integer before companyid;
Alter Table campgeneration ADD createcomm Smallint before generationid;
Alter Table campgeneration ADD documenttype Smallint before generationid;
Alter Table campgeneration ADD employeemail NVarchar(100,0) before generationid;
Alter Table campgeneration ADD senderemployeeid Integer before templateid;
Alter Table campgeneration ADD senderprefix NVarchar(120,0) before templateid;
Alter Table campgeneration ADD senderprefixtype Smallint before templateid;
Alter Table campgeneration ADD subject NVarchar(120,0) before templateid;
Alter Table campgeneration ADD telecomtypeid Integer before templateid;
Alter Table campgeneration add Constraint Foreign Key (campaignid) references campaign (campaignid);
Alter Table campgeneration add Constraint Foreign Key (telecomtypeid) references telecomtype (telecomtypeid);

Create Table campsentlog (
campsentlogid Integer NOT NULL,
companyid Integer NOT NULL,
generationid Integer NOT NULL,
totalsent Integer NOT NULL,
totalsuccess Integer NOT NULL,
version Integer NOT NULL,
Primary Key (campsentlogid)) LOCK MODE ROW;

Alter Table campsentlog add Constraint Foreign Key (companyid) references company (companyid);
Alter Table campsentlog add Constraint Foreign Key (generationid) references campgeneration (generationid);

Create Table sentlogcontact (
addressid Integer NOT NULL,
campsentlogid Integer NOT NULL,
companyid Integer NOT NULL,
contactpersonid Integer,
errormessage NVarchar(255,0),
sentlogcontactid Integer NOT NULL,
status Smallint NOT NULL,
userid Integer,
version Integer NOT NULL,
Primary Key (sentlogcontactid)) LOCK MODE ROW;

Alter Table sentlogcontact add Constraint Foreign Key (companyid) references company (companyid);
Alter Table sentlogcontact add Constraint Foreign Key (addressid) references address (addressid);
Alter Table sentlogcontact add Constraint Foreign Key (campsentlogid) references campsentlog (campsentlogid);
Alter Table sentlogcontact add Constraint Foreign Key (addressid, contactpersonid) references contactperson (addressid, contactpersonid);
Alter Table sentlogcontact add Constraint Foreign Key (userid) references elwisuser (userid);

/*
miky:20121128
Update version constants
*/
update systemconstant set value='4.9' where name='APP_VERSION';
update systemconstant set value='4.6' where name='DB_VERSION';


UPDATE STATISTICS HIGH;