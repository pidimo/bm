/*20090114:Ivan*/
/*Add new column in attach table, for store attachment size*/
alter table attach add column size Int(11) after name;

/*Ivan:20090123*/
/*Add contractnumber column in productcontract table*/
alter table productcontract add column contractnumber varchar(40) default null after contractid;

/*Ivan:20090129*/
/*Add finance module into systemmodule table*/
insert into systemmodule (description,moduleid,modulenamekey,modulepath)
values('Module of finances', '11', 'module.finance','/finance');

update systemfunction set moduleid = 11  where code='INVOICE';
update systemfunction set moduleid = 11  where code='INVOICEPOSITION';
update systemfunction set moduleid = 11  where code='INVOICEREMINDER';
update systemfunction set moduleid = 11  where code='INVOICEPAYMENT';
update systemfunction set moduleid = 11  where code='REMINDERLEVEL';
update systemfunction set moduleid = 11  where code='INVOICETEMPLATE';
update systemfunction set moduleid = 11  where code='SEQUENCERULE';

insert into companymodule (companyid, moduleid, maintablelimit, active) select companyid, 11, maintablelimit, active from companymodule where moduleid=4;

update accessrights set moduleid = 11  where functionid=105;
update accessrights set moduleid = 11  where functionid=106;
update accessrights set moduleid = 11  where functionid=107;
update accessrights set moduleid = 11  where functionid=108;
update accessrights set moduleid = 11  where functionid=103;
update accessrights set moduleid = 11  where functionid=102;
update accessrights set moduleid = 11  where functionid=104;

/*Ivan:20090130*/
/*update finance freetext types*/
/*invoice*/
update freetext set freetexttype = 20 where freetextid in (select notesid from invoice);
update freetext set freetexttype = 20 where freetextid in (select documentid from invoice);
/*invoicereminder*/
update freetext set freetexttype = 23 where freetextid in (select descriptionid from invoicereminder);
update freetext set freetexttype = 23 where freetextid in (select documentid from invoicereminder);
/*invoiceposition*/
update freetext set freetexttype = 22 where freetextid in (select freetextid from invoiceposition);
/*invoicepayment*/
update freetext set freetexttype = 21 where freetextid in (select freetextid from invoicepayment);

/*Configuration catalogs accessrights*/
update systemfunction set moduleid = 11  where code='VAT';
update systemfunction set moduleid = 11  where code='VATRATE';
update systemfunction set moduleid = 11  where code='PAYCONDITION';
update systemfunction set moduleid = 11  where code='ACCOUNT';

insert into companymodule (companyid, moduleid, maintablelimit, active)
select companyid, 11, maintablelimit, active
from companymodule
where moduleid=3 and companyid not in (select companyid from companymodule where moduleid=11);

insert into companymodule (companyid, moduleid, maintablelimit, active)
select companyid, 11, maintablelimit, active
from companymodule
where moduleid=1 and companyid not in (select companyid from companymodule where moduleid=11);

update accessrights set moduleid = 11  where functionid=32;
update accessrights set moduleid = 11  where functionid=33;
update accessrights set moduleid = 11  where functionid=21;
update accessrights set moduleid = 11  where functionid=101;

/*Miky:20090203*/
/*update pay period, now the constant is the number of months of the period*/
UPDATE productcontract SET payperiod = 12 WHERE payperiod = 3;
UPDATE productcontract SET payperiod = 6 WHERE payperiod = 2;
UPDATE productcontract SET payperiod = 3 WHERE payperiod = 1;
UPDATE productcontract SET payperiod = 1 WHERE payperiod = 0;

/*Ivan:20090209*/
/*Add contactpersonId in productcontract table*/
alter table productcontract add column contactpersonid int(11) after companyid;
alter table productcontract add foreign key (addressid,contactpersonid) references contactperson (addressid,contactpersonid) on delete  restrict on update  restrict;

/*Ivan:20090212*/
/*adding column  creditnoteofid in the table invoice and column creditnoteid in the table invoicepayment*/
alter table invoice add column creditnoteofid int(11) after contactpersonid;
alter table invoicepayment add column creditnoteid int(11) after companyid;

alter table invoice add foreign key (creditnoteofid) references invoice (invoiceid) on delete  restrict on update  restrict;
alter table invoicepayment add foreign key (creditnoteid) references invoice (invoiceid) on delete  restrict on update  restrict;

/*Ivan:20090218*/
/*change campcriterionvalue for saleposition table*/
update campcriterionvalue set tablename='saleposition' where campcriterionvalueid=23;

/*drop tables contract, prodcustomer and achievement*/
drop table contract;
drop table prodcustomer;
drop table achievement;

/*
Fernando:20090220
Project module schema
*/
Create table project (
accountid Int,
companyid Int NOT NULL,
contactpersonid Int,
customerid Int,
descriptionid Int,
enddate Int NOT NULL,
hastimelimit Tinyint NOT NULL,
plannedinvoice Decimal(6,1) NOT NULL,
plannednoinvoice Decimal(6,1) NOT NULL,
projectid Int NOT NULL,
projectname Varchar(80) NOT NULL,
responsibleid Int NOT NULL,
startdate Int NOT NULL,
status Smallint NOT NULL,
tobeinvoiced Smallint NOT NULL,
totalinvoice Decimal(6,1),
totalnoinvoice Decimal(6,1),
version Int NOT NULL,
Primary Key (projectid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

Create table projectuser (
companyid Int NOT NULL,
permission Tinyint NOT NULL,
projectid Int NOT NULL,
userid Int NOT NULL,
version Int NOT NULL,
Primary Key (projectid,userid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

Create table projectactivity (
activityname Varchar(80) NOT NULL,
companyid Int NOT NULL,
projectactivityid Int NOT NULL,
projectid Int NOT NULL,
version Int NOT NULL,
Primary Key (projectactivityid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

Create table subproject (
companyid Int NOT NULL,
projectid Int NOT NULL,
subprojectname Varchar(80) NOT NULL,
subprojectid Int NOT NULL,
version Int NOT NULL,
Primary Key (subprojectid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

Create table projecttime (
companyid Int NOT NULL,
confirmedbyid Int,
date Int NOT NULL,
descriptionid Int,
invoiceable Tinyint NOT NULL,
projectactivityid Int NOT NULL,
projectid Int NOT NULL,
status Smallint NOT NULL,
subprojectid Int,
timeid Int NOT NULL,
time Decimal(3,1) NOT NULL,
tobeinvoiced Tinyint NOT NULL,
userid Int NOT NULL,
version Int NOT NULL,
Primary Key (timeid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

Alter table projecttime add Foreign Key (descriptionid) references freetext (freetextid);
Alter table project add Foreign Key (descriptionid) references freetext (freetextid);
Alter table project add Foreign Key (companyid) references company (companyid);
Alter table projectuser add Foreign Key (companyid) references company (companyid);
Alter table projectactivity add Foreign Key (companyid) references company (companyid);
Alter table subproject add Foreign Key (companyid) references company (companyid);
Alter table projecttime add Foreign Key (companyid) references company (companyid);
Alter table project add Foreign Key (customerid, contactpersonid) references contactperson (addressid, contactpersonid);
Alter table project add Foreign Key (customerid) references customer (customerid);
Alter table project add Foreign Key (responsibleid) references elwisuser (userid);
Alter table projectuser add Foreign Key (userid) references elwisuser (userid);
Alter table projecttime add Foreign Key (userid) references elwisuser (userid);
Alter table projecttime add Foreign Key (confirmedbyid) references elwisuser (userid);
Alter table project add Foreign Key (accountid) references account (accountid);
Alter table projectuser add Foreign Key (projectid) references project (projectid);
Alter table projectactivity add Foreign Key (projectid) references project (projectid);
Alter table subproject add Foreign Key (projectid) references project (projectid);
Alter table projecttime add Foreign Key (projectid) references project (projectid);
Alter table projecttime add Foreign Key (projectactivityid) references projectactivity (projectactivityid);
Alter table projecttime add Foreign Key (subprojectid) references subproject (subprojectid);

/* Fernando: 20090223
Project module access rights */
insert into systemmodule (description,moduleid,modulenamekey,modulepath) values('Project module', 12, 'module.projects','/projects' );
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECT', 'Project', 112, 12, 'Project.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTTIME', 'Project time', 113, 12, 'ProjectTime.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTACTIVITY', 'Project activity', 114, 12, 'ProjectActivity.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTUSER', 'Project users', 115, 12, 'ProjectUser.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(15, 'PROJECTSUBPROJECT', 'Project sub project', 116, 12, 'ProjectSubProject.functionality');

/*Ivan:20090224*/
/*add columns for Gross values functionality*/
alter table product add column pricegross decimal(10,2) after price;
alter table invoice add column netgross tinyint after invoicedate;
alter table company add column netgross tinyint after maxattachsize;
alter table invoiceposition add column unitpricegross decimal(10,2) after unitprice;
alter table invoiceposition add column totalpricegross decimal(10,2) after totalprice;
/*setting netgross column to use net price in table invoice*/
update invoice set netgross=1;

/* Fernando: 20090224
Campaign total hits column */
alter table campaign add column totalhits int  after text;

/*Ivan:20090225*/
/*add netgross column in productcontract table*/
alter table productcontract add column netgross tinyint after matchcalperiod;
/*setting netgross column to use net price*/
update productcontract set netgross=1;

/*Ivan:20090226
Update campcriterion operator from BETWEEN1 to BETWEEN*/
update campcriterion set operator='BETWEEN' where operator='BETWEEN1';

/*Ivan:20090227
Update invoiceposition unitprice and totalprice columns*/
alter table invoiceposition modify unitprice decimal(10,2) default null;
alter table invoiceposition modify totalprice decimal(10,2) default null;

/* Alvaro, incoming invoices 20090227 */

Create table incominginvoice (
amountgross Decimal(10,2),
amountnet Decimal(10,2),
companyid Int NOT NULL,
currencyid Int NOT NULL,
ininvoiceid Int NOT NULL,
invoicedate Int,
invoicenumber Varchar(30) NOT NULL,
notesid Int,
openamount Decimal(10,2),
paiduntil Int,
receiptdate Int NOT NULL,
supplierid Int NOT NULL,
tobepaiduntil Int,
type Smallint NOT NULL,
version Int NOT NULL,
Primary Key (ininvoiceid))  ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Default;

Create table incomingpayment (
amount Decimal(10,2),
companyid Int NOT NULL,
ininvoiceid Int NOT NULL,
notesid Int,
paydate Int,
paymentid Int NOT NULL,
version Int NOT NULL,
Primary Key (paymentid))  ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci
ROW_FORMAT = Default;

Alter table incomingpayment add Foreign Key (ininvoiceid) references incominginvoice (ininvoiceid) on delete  restrict on update  restrict;
Alter table incominginvoice add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table incomingpayment add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table incominginvoice add Foreign Key (currencyid) references currency (currencyid) on delete  restrict on update  restrict;
Alter table incominginvoice add Foreign Key (supplierid) references supplier (supplierid) on delete  restrict on update  restrict;
Alter table incominginvoice add Foreign Key (notesid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter table incomingpayment add Foreign Key (notesid) references freetext (freetextid) on delete  restrict on update  restrict;

insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values(15,'INCOMINGINVOICE','Incoming invoice access rights',117,11,'Finance.incomingInvoice.functionality');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values(15,'INCOMINGPAYMENT','Incoming invoice payments access rights',118,11,'Finance.incomingPayment.functionality');

/* Update version constants */
update systemconstant set value='4.0.RC1' where name='APP_VERSION';
update systemconstant set value='4.0' where name='DB_VERSION';