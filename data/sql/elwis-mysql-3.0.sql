/*Ivan:20080805*/
/*add prefix column for email accounts*/
alter table mailaccount add prefix varchar(250) after password;

/*Ivan:20080818*/
/*Update salesprocess systemModule value*/
update systemmodule
set modulepath='/sales', modulenamekey='module.sales', description='Module of sales'
where moduleid=4;

/*Update report table replace salesprocess module by sales module*/
update report
set module='sales'
where module='salesprocess';

/*Ivan:20080820*/
/*Sales module, Invoice related tables*/
  Create table invoice (
	`addressid` Int NOT NULL,
	`companyid` Int NOT NULL,
	`contactpersonid` Int,
	`currencyid` Int NOT NULL,
	`documentid` Int,
	`invoiceid` Int NOT NULL,
	`invoicedate` Int NOT NULL,
	`notesid` Int,
	`number` Varchar(200) NOT NULL,
	`openamount` Decimal(10,2),
	`payconditionid` Int NOT NULL,
	`paymentdate` Int NOT NULL,
	`reminderlevel` Int,
	`reminderdate` Int,
	`type` Smallint NOT NULL,
	`totalamountnet` Decimal(10,2),
	`totalamountgross` Decimal(10,2),
	`templateid` Int NOT NULL,
	`vatid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`invoiceid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoiceposition (
	`accountid` Int NOT NULL,
	`companyid` Int NOT NULL,
	`contractid` Int,
	`freetextid` Int,
	`invoiceid` Int NOT NULL,
	`paystepid` Int,
	`productid` Int NOT NULL,
	`number` Int NOT NULL,
	`positionid` Int NOT NULL,
	`quantity` Int NOT NULL,
	`totalprice` Decimal(10,2) NOT NULL,
	`unitprice` Decimal(10,2) NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
	`vatid` Int NOT NULL,
	`vatrate` Decimal(10,2) NOT NULL,
 Primary Key (`positionid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoicereminder (
	`companyid` Int NOT NULL,
	`date` Int NOT NULL,
	`descriptionid` Int,
	`documentid` Int,
	`invoiceid` Int NOT NULL,
	`reminderid` Int NOT NULL,
	`reminderlevelid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`reminderid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoicepayment (
	`amount` Decimal(10,2) NOT NULL,
	`companyid` Int NOT NULL,
	`freetextid` Int,
	`invoiceid` Int NOT NULL,
	`paydate` Int NOT NULL,
	`paymentid` Int NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`paymentid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table reminderlevel (
	`companyid` Int NOT NULL,
	`fee` Decimal(5,2),
	`level` Int NOT NULL,
	`name` Varchar(100) NOT NULL,
	`numberofdays` Int NOT NULL,
	`reminderlevelid` Int NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`reminderlevelid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table remindertext (
	`companyid` Int NOT NULL,
	`freetextid` Int NOT NULL,
	`isdefault` Tinyint NOT NULL,
	`languageid` Int NOT NULL,
	`reminderlevelid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`languageid`,`reminderlevelid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table sequencerule (
	`companyid` Int NOT NULL,
	`format` Varchar(150) NOT NULL,
	`isdefault` Tinyint NOT NULL,
	`lastnumber` Int,
	`lastdate` Int,
	`numberid` Int NOT NULL,
	`resettype` Smallint,
	`startnumber` Int,
	`type` Smallint NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`numberid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoicetemplate (
	`companyid` Int NOT NULL,
	`templateid` Int NOT NULL,
	`title` Varchar(150) NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`templateid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoicetext (
	`companyid` Int NOT NULL,
	`freetextid` Int NOT NULL,
	`isdefault` Tinyint NOT NULL,
	`languageid` Int NOT NULL,
	`templateid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`languageid`,`templateid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table account (
	`accountid` Int NOT NULL,
	`companyid` Int NOT NULL,
	`name` Varchar(150) NOT NULL,
	`number` Varchar(150) NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`accountid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table invoicevat (
	`amount` Decimal(13,2) NOT NULL,
	`companyid` Int NOT NULL,
	`invoiceid` Int NOT NULL,
	`vatid` Int NOT NULL,
	`vatrate` Decimal(5,2) NOT NULL,
 Primary Key (`invoiceid`,`vatid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table producttext (
	`companyid` Int NOT NULL,
	`isdefault` Tinyint NOT NULL,
	`freetextid` Int NOT NULL,
	`languageid` Int NOT NULL,
	`productid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`languageid`,`productid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table payconditiontext (
	`companyid` Int NOT NULL,
	`freetextid` Int NOT NULL,
	`languageid` Int NOT NULL,
	`payconditionid` Int NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`languageid`,`payconditionid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Create table contracttype (
	`companyid` Int NOT NULL,
	`contracttypeid` Int NOT NULL,
	`name` Varchar(100) NOT NULL,
	`tobeinvoiced` Tinyint NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`contracttypeid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table company add column `invoicedayssend` Int not null default 0 after finishlicense;

Alter table product add column `accountid` Int null first;
Alter table product add column `langtextid` Int null after descriptionid;
Alter table product add Foreign Key (`accountid`) references account (`accountid`) on delete  restrict on update  restrict;
Alter table product add Foreign Key (`langtextid`) references langtext (`langtextid`) on delete  restrict on update  restrict;

Alter table invoice add Foreign Key (`payconditionid`) references paycondition (`payconditionid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`currencyid`) references currency (`currencyid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`vatid`) references vat (`vatid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`notesid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`documentid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`addressid`) references address (`addressid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`templateid`) references invoicetemplate (`templateid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table invoice add Foreign Key (`addressid`,`contactpersonid`) references contactperson (`addressid`,`contactpersonid`) on delete  restrict on update  restrict;

Alter table invoiceposition add Foreign Key (`vatid`) references vat (`vatid`) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (`productid`) references product (`productid`) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (`invoiceid`) references invoice (`invoiceid`) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (`accountid`) references account (`accountid`) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table invoicereminder add Foreign Key (`invoiceid`) references invoice (`invoiceid`) on delete  restrict on update  restrict;
Alter table invoicereminder add Foreign Key (`documentid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoicereminder add Foreign Key (`descriptionid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoicereminder add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table invoicereminder add Foreign Key (`reminderlevelid`) references reminderlevel (`reminderlevelid`) on delete  restrict on update  restrict;

Alter table invoicepayment add Foreign Key (`invoiceid`) references invoice (`invoiceid`) on delete  restrict on update  restrict;
Alter table invoicepayment add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoicepayment add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table reminderlevel add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table remindertext add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table remindertext add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table remindertext add Foreign Key (`languageid`) references language (`languageid`) on delete  restrict on update  restrict;
Alter table remindertext add Foreign Key (`reminderlevelid`) references reminderlevel (`reminderlevelid`) on delete  restrict on update  restrict;

Alter table sequencerule add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table invoicetemplate add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table invoicetext add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table invoicetext add Foreign Key (`templateid`) references invoicetemplate (`templateid`) on delete  restrict on update  restrict;
Alter table invoicetext add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table invoicetext add Foreign Key (`languageid`) references language (`languageid`) on delete  restrict on update  restrict;

Alter table account add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table invoicevat add Foreign Key (`vatid`) references vat (`vatid`) on delete  restrict on update  restrict;
Alter table invoicevat add Foreign Key (`invoiceid`) references invoice (`invoiceid`) on delete  restrict on update  restrict;
Alter table invoicevat add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;

Alter table producttext add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table producttext add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table producttext add Foreign Key (`languageid`) references language (`languageid`) on delete  restrict on update  restrict;
Alter table producttext add Foreign Key (`productid`) references product (`productid`) on delete  restrict on update  restrict;

Alter table payconditiontext add Foreign Key (`payconditionid`) references paycondition (`payconditionid`) on delete  restrict on update  restrict;
Alter table payconditiontext add Foreign Key (`freetextid`) references freetext (`freetextid`) on delete  restrict on update  restrict;
Alter table payconditiontext add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;
Alter table payconditiontext add Foreign Key (`languageid`) references language (`languageid`) on delete  restrict on update  restrict;

Alter table contracttype add Foreign Key (`companyid`) references company (`companyid`) on delete  restrict on update  restrict;


/*miky:20080908*/
Alter Table reportcolumn add categoryid Int first;
Alter Table reportfilter add categoryid Int after aliascondition;
/*foreign relations*/
Alter table reportcolumn add Foreign Key (categoryid) references category (categoryid) on delete  restrict on update  restrict;
Alter table reportfilter add Foreign Key (categoryid) references category (categoryid) on delete  restrict on update  restrict;


/*Ivan:20080917*/
/*Accessright for invoices*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'CONTRACTTYPE', 'Contract type catalog', 100, 4, 'ContractType.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'ACCOUNT', 'Account catalog', 101, 4, 'Account.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'INVOICETEMPLATE', 'Invoice template catalog', 102, 4, 'InvoiceTemplate.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'REMINDERLEVEL', 'Reminder level catalog', 103, 4, 'ReminderLevel.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'SEQUENCERULE', 'Sequence rule catalog', 104, 4, 'SequenceRule.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'INVOICE', 'Invoice access right', 105, 4, 'Invoice.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'INVOICEPOSITION', 'Invoice position access right', 106, 4, 'InvoicePosition.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'INVOICEREMINDER', 'Invoice reminder access right', 107, 4, 'InvoiceReminder.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'INVOICEPAYMENT', 'Invoice payment access right', 108, 4, 'InvoicePayment.functionality');

/* Update version constants, PUT other sqls above */
update systemconstant set value='3.0' where name='APP_VERSION';
update systemconstant set value='3.0' where name='DB_VERSION';