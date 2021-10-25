/*
miky:20120829
modify customertypename
*/
ALTER TABLE customertype modify customertypename varchar(40);

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
ALTER TABLE elwisuser ADD csvdelimiter Varchar(5) after companyid;

/*
miky:20121004
add new field csv report charset
*/
ALTER TABLE elwisuser ADD reportcharset Varchar(30) after password;

/*
miky:20121031
modify time in project time
*/
ALTER TABLE projecttime modify time Decimal(4,2) NOT NULL;


/*
miky:20121128
structure for manage campaign sent email log
*/
Alter Table campgeneration ADD campaignid Int after activityid;
Alter Table campgeneration ADD createcomm Tinyint after companyid;
Alter Table campgeneration ADD documenttype Tinyint after createcomm;
Alter Table campgeneration ADD employeemail Varchar(100) after documenttype;
Alter Table campgeneration ADD senderemployeeid Int after generationtime;
Alter Table campgeneration ADD senderprefix Varchar(120) after senderemployeeid;
Alter Table campgeneration ADD senderprefixtype Tinyint after senderprefix;
Alter Table campgeneration ADD subject Varchar(120) after senderprefixtype;
Alter Table campgeneration ADD telecomtypeid Int after subject;
Alter Table campgeneration add Foreign Key (campaignid) references campaign (campaignid) on delete  restrict on update  restrict;
Alter Table campgeneration add Foreign Key (telecomtypeid) references telecomtype (telecomtypeid) on delete  restrict on update  restrict;

Create Table campsentlog (
campsentlogid Int NOT NULL,
companyid Int NOT NULL,
generationid Int NOT NULL,
totalsent Int NOT NULL,
totalsuccess Int NOT NULL,
version Int NOT NULL,
Primary Key (campsentlogid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table campsentlog add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table campsentlog add Foreign Key (generationid) references campgeneration (generationid) on delete  restrict on update  restrict;

Create Table sentlogcontact (
addressid Int NOT NULL,
campsentlogid Int NOT NULL,
companyid Int NOT NULL,
contactpersonid Int,
errormessage Varchar(255),
sentlogcontactid Int NOT NULL,
status Tinyint NOT NULL,
userid Int,
version Int NOT NULL,
Primary Key (sentlogcontactid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table sentlogcontact add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table sentlogcontact add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table sentlogcontact add Foreign Key (campsentlogid) references campsentlog (campsentlogid) on delete  restrict on update  restrict;
Alter Table sentlogcontact add Foreign Key (addressid, contactpersonid) references contactperson (addressid, contactpersonid) on delete  restrict on update  restrict;
Alter Table sentlogcontact add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

/*
miky:20121128
Update version constants
*/
update systemconstant set value='4.9' where name='APP_VERSION';
update systemconstant set value='4.6' where name='DB_VERSION';

