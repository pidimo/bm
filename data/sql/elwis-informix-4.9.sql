/*
miky:20130606
additional address functionality
*/
Create Table addaddress (
addaddressid Integer NOT NULL,
addressid Integer NOT NULL,
addaddressline NVarchar(200),
cityid Integer,
commentid Integer,
companyid Integer NOT NULL,
countryid Integer,
housenumber NVarchar(10),
isdefault Smallint NOT NULL,
street NVarchar(50),
version Integer NOT NULL,
Primary Key (addaddressid)) LOCK MODE ROW;

Alter Table addaddress add Constraint Foreign Key (addressid) references address (addressid);
Alter Table addaddress add Constraint Foreign Key (cityid) references city (cityid);
Alter Table addaddress add Constraint Foreign Key (commentid) references freetext (freetextid);
Alter Table addaddress add Constraint Foreign Key (companyid) references company (companyid);
Alter Table addaddress add Constraint Foreign Key (countryid) references country (countryid);


Create Table addressreltype (
companyid Integer NOT NULL,
relationtypeid Integer NOT NULL,
relationtype Smallint NOT NULL,
title NVarchar(150) NOT NULL,
version Integer NOT NULL,
Primary Key (relationtypeid)) LOCK MODE ROW;

Alter Table addressreltype add Constraint Foreign Key (companyid) references company (companyid);


Create Table addressrelation (
addressid Integer NOT NULL,
relatedaddressid Integer NOT NULL,
commentid Integer,
companyid Integer NOT NULL,
relationid Integer NOT NULL,
relationtypeid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (relationid)) LOCK MODE ROW;

Alter Table addressrelation add Constraint Foreign Key (addressid) references address (addressid);
Alter Table addressrelation add Constraint Foreign Key (relatedaddressid) references address (addressid);
Alter Table addressrelation add Constraint Foreign Key (commentid) references freetext (freetextid);
Alter Table addressrelation add Constraint Foreign Key (companyid) references company (companyid);
Alter Table addressrelation add Constraint Foreign Key (relationtypeid) references addressreltype (relationtypeid);

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'ADDRESSRELATIONTYPE', 'Address relation type catalog', 131, 1, 'AddressRelationType.functionality');

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'ADDITIONALADDRESS', 'Additional address functionality', 132, 1, 'AdditionalAddress.functionality');

/*add additional address line */
ALTER TABLE address ADD addaddressline NVarchar(200) before addressid;
ALTER TABLE contactperson ADD addaddressline NVarchar(200) before addressid;

/*drop po box in address*/
ALTER TABLE address DROP pobox;
ALTER TABLE address DROP zipofpobox;

/*modify address names length*/
ALTER TABLE address modify name1 NVarchar(60);
ALTER TABLE address modify name2 NVarchar(60);
ALTER TABLE address modify name3 NVarchar(60);

/*
miky:20130614
sent invoice address
*/
ALTER TABLE customer ADD invoiceaddressid Integer before numberofemployees;
ALTER TABLE customer ADD invoicecontactpersonid Integer before numberofemployees;
Alter Table customer add Constraint Foreign Key (invoiceaddressid) references address (addressid);
Alter Table customer add Constraint Foreign Key (invoiceaddressid, invoicecontactpersonid) references contactperson (addressid, contactpersonid);

ALTER TABLE sale ADD sentaddressid Integer before title;
ALTER TABLE sale ADD sentcontactpersonid Integer before title;
Alter Table sale add Constraint Foreign Key (sentaddressid) references address (addressid);
Alter Table sale add Constraint Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid);

ALTER TABLE productcontract ADD sentaddressid Integer before vatid;
ALTER TABLE productcontract ADD sentcontactpersonid Integer before vatid;
Alter Table productcontract add Constraint Foreign Key (sentaddressid) references address (addressid);
Alter Table productcontract add Constraint Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid);

ALTER TABLE invoice ADD sentaddressid Integer before sequenceruleid;
ALTER TABLE invoice ADD sentcontactpersonid Integer before sequenceruleid;
Alter Table invoice add Constraint Foreign Key (sentaddressid) references address (addressid);
Alter Table invoice add Constraint Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid);

ALTER TABLE contact ADD addaddressid Integer before datefinished;
Alter Table contact add Constraint Foreign Key (addaddressid) references addaddress (addaddressid);

/*
miky:20130617
sent invoice address
*/
update systemconstant set value='5.0' where name='APP_VERSION';
update systemconstant set value='4.9' where name='DB_VERSION';

UPDATE STATISTICS HIGH;