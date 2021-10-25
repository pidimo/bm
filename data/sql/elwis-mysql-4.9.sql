/*
miky:20130606
additional address functionality
*/
Create Table addaddress (
addaddressid Int NOT NULL,
addressid Int NOT NULL,
addaddressline Varchar(200),
cityid Int,
commentid Int,
companyid Int NOT NULL,
countryid Int,
housenumber Varchar(10),
isdefault Smallint NOT NULL,
street Varchar(50),
version Int NOT NULL,
Primary Key (addaddressid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table addaddress add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table addaddress add Foreign Key (cityid) references city (cityid) on delete  restrict on update  restrict;
Alter Table addaddress add Foreign Key (commentid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter Table addaddress add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table addaddress add Foreign Key (countryid) references country (countryid) on delete  restrict on update  restrict;


Create Table addressreltype (
companyid Int NOT NULL,
relationtypeid Int NOT NULL,
relationtype Smallint NOT NULL,
title Varchar(150) NOT NULL,
version Int NOT NULL,
Primary Key (relationtypeid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table addressreltype add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;


Create Table addressrelation (
addressid Int NOT NULL,
relatedaddressid Int NOT NULL,
commentid Int,
companyid Int NOT NULL,
relationid Int NOT NULL,
relationtypeid Int NOT NULL,
version Int NOT NULL,
Primary Key (relationid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table addressrelation add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table addressrelation add Foreign Key (relatedaddressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table addressrelation add Foreign Key (commentid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter Table addressrelation add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table addressrelation add Foreign Key (relationtypeid) references addressreltype (relationtypeid) on delete  restrict on update  restrict;

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'ADDRESSRELATIONTYPE', 'Address relation type catalog', 131, 1, 'AddressRelationType.functionality');

INSERT INTO systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'ADDITIONALADDRESS', 'Additional address functionality', 132, 1, 'AdditionalAddress.functionality');

/*add additional address line */
ALTER TABLE address ADD addaddressline Varchar(200) after active;
ALTER TABLE contactperson ADD addaddressline Varchar(200) after active;

/*drop po box in address*/
ALTER TABLE address DROP COLUMN pobox;
ALTER TABLE address DROP COLUMN zipofpobox;

/*modify address names length*/
ALTER TABLE address modify name1 Varchar(60);
ALTER TABLE address modify name2 Varchar(60);
ALTER TABLE address modify name3 Varchar(60);

/*
miky:20130614
sent invoice address
*/
ALTER TABLE customer ADD invoiceaddressid Int after employeeid;
ALTER TABLE customer ADD invoicecontactpersonid Int after invoiceaddressid;
Alter Table customer add Foreign Key (invoiceaddressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table customer add Foreign Key (invoiceaddressid, invoicecontactpersonid) references contactperson (addressid, contactpersonid) on delete  restrict on update  restrict;

ALTER TABLE sale ADD sentaddressid Int after sellerid;
ALTER TABLE sale ADD sentcontactpersonid Int after sentaddressid;
Alter Table sale add Foreign Key (sentaddressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table sale add Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid) on delete  restrict on update  restrict;

ALTER TABLE productcontract ADD sentaddressid Int after sellerid;
ALTER TABLE productcontract ADD sentcontactpersonid Int after sentaddressid;
Alter Table productcontract add Foreign Key (sentaddressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table productcontract add Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid) on delete  restrict on update  restrict;

ALTER TABLE invoice ADD sentaddressid Int after rulenumber;
ALTER TABLE invoice ADD sentcontactpersonid Int after sentaddressid;
Alter Table invoice add Foreign Key (sentaddressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table invoice add Foreign Key (sentaddressid, sentcontactpersonid) references contactperson (addressid, contactpersonid) on delete  restrict on update  restrict;

ALTER TABLE contact ADD addaddressid Int after addressid;
Alter Table contact add Foreign Key (addaddressid) references addaddress (addaddressid) on delete  restrict on update  restrict;

/*
miky:20130617
sent invoice address
*/
update systemconstant set value='5.0' where name='APP_VERSION';
update systemconstant set value='4.9' where name='DB_VERSION';


