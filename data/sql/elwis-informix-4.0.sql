/*20090114:Ivan*/
/*Add new column in attach table, for store attachment size*/
alter table attach add size Integer before visible;

/*Ivan:20090123*/
/*Add contractnumber column in productcontract table*/
alter table productcontract add  contractnumber nvarchar(40) default null before contracttypeid;

/*Ivan:20090129*/
/*Add finance module into systemmodule table*/
insert into systemmodule (description,moduleid,modulenamekey,modulepath)
values('Module of finances', '11', 'module.finance','/finance' );

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
alter table productcontract add  contactpersonid Integer default null  before contractenddate;
alter table productcontract add constraint foreign key(addressid, contactpersonid) references contactperson (addressid,contactpersonid);

/*Ivan:20090212*/
/*adding column  creditnoteofid in the table invoice and column creditnoteid in the table invoicepayment*/
alter table invoice add creditnoteofid Integer before currencyid;
alter table invoicepayment add creditnoteid Integer before freetextid;

alter table invoice add constraint foreign key (creditnoteofid) references invoice (invoiceid);
alter table invoicepayment add constraint foreign key (creditnoteid) references invoice (invoiceid);

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
accountid Integer,
companyid Integer NOT NULL,
contactpersonid Integer,
customerid Integer,
descriptionid Integer,
enddate Integer NOT NULL,
hastimelimit Smallint NOT NULL,
plannedinvoice Decimal(6,1) NOT NULL,
plannednoinvoice Decimal(6,1) NOT NULL,
projectid Integer NOT NULL,
projectname nVarchar(80) NOT NULL,
responsibleid Integer NOT NULL,
startdate Integer NOT NULL,
status Smallint NOT NULL,
tobeinvoiced Smallint NOT NULL,
totalinvoice Decimal(6,1),
totalnoinvoice Decimal(6,1),
version Integer NOT NULL,
Primary Key (projectid)) LOCK MODE ROW;

Create table projectuser (
companyid Integer NOT NULL,
permission Smallint NOT NULL,
projectid Integer NOT NULL,
userid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (projectid,userid)) LOCK MODE ROW;

Create table projectactivity (
activityname nVarchar(80) NOT NULL,
companyid Integer NOT NULL,
projectactivityid Integer NOT NULL,
projectid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (projectactivityid)) LOCK MODE ROW;

Create table subproject (
companyid Integer NOT NULL,
projectid Integer NOT NULL,
subprojectname nVarchar(80) NOT NULL,
subprojectid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (subprojectid)) LOCK MODE ROW;

Create table projecttime (
companyid Integer NOT NULL,
confirmedbyid Integer,
date Integer NOT NULL,
descriptionid Integer,
invoiceable Smallint NOT NULL,
projectactivityid Integer NOT NULL,
projectid Integer NOT NULL,
status Smallint NOT NULL,
subprojectid Integer,
timeid Integer NOT NULL,
time Decimal(3,1) NOT NULL,
tobeinvoiced Smallint NOT NULL,
userid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (timeid)) LOCK MODE ROW;

Alter table projecttime add Constraint Foreign Key (descriptionid) references freetext (freetextid);
Alter table project add Constraint Foreign Key (descriptionid) references freetext (freetextid);
Alter table project add Constraint Foreign Key (companyid) references company (companyid);
Alter table projectuser add Constraint Foreign Key (companyid) references company (companyid);
Alter table projectactivity add Constraint Foreign Key (companyid) references company (companyid);
Alter table subproject add Constraint Foreign Key (companyid) references company (companyid);
Alter table projecttime add Constraint Foreign Key (companyid) references company (companyid);
Alter table project add Constraint Foreign Key (customerid, contactpersonid) references contactperson (addressid, contactpersonid);
Alter table project add Constraint Foreign Key (customerid) references customer (customerid);
Alter table project add Constraint Foreign Key (responsibleid) references elwisuser (userid);
Alter table projectuser add Constraint Foreign Key (userid) references elwisuser (userid);
Alter table projecttime add Constraint Foreign Key (userid) references elwisuser (userid);
Alter table projecttime add Constraint Foreign Key (confirmedbyid) references elwisuser (userid);
Alter table project add Constraint Foreign Key (accountid) references account (accountid);
Alter table projectuser add Constraint Foreign Key (projectid) references project (projectid);
Alter table projectactivity add Constraint Foreign Key (projectid) references project (projectid);
Alter table subproject add Constraint Foreign Key (projectid) references project (projectid);
Alter table projecttime add Constraint Foreign Key (projectid) references project (projectid);
Alter table projecttime add Constraint Foreign Key (projectactivityid) references projectactivity (projectactivityid);
Alter table projecttime add Constraint Foreign Key (subprojectid) references subproject (subprojectid);

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
alter table product add pricegross decimal(10,2) default null before productgroupid;
alter table invoice add netgross smallint default null before notesid;
alter table company add netgross smallint default null before routepageid;
alter table invoiceposition add unitpricegross decimal(10,2) default null before version;
alter table invoiceposition add totalpricegross decimal(10,2) default null before unit;
/*setting netgross column to use net price in table invoice*/
update invoice set netgross=1;

/* Fernando: 20090224
Campaign total hits column */
alter table campaign add totalhits integer  before typeid;

/*Ivan:20090225*/
/*add netgross column in productcontract table*/
alter table productcontract add netgross smallint default null before openamount;
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

Create Table incominginvoice (
amountgross Decimal(10,2),
amountnet Decimal(10,2),
companyid Integer NOT NULL,
currencyid Integer NOT NULL,
ininvoiceid Integer NOT NULL,
invoicedate Integer,
invoicenumber Nvarchar(30) NOT NULL,
notesid Integer,
openamount Decimal(10,2),
paiduntil Integer,
receiptdate Integer NOT NULL,
supplierid Integer NOT NULL,
tobepaiduntil Integer,
type Smallint NOT NULL,
version Integer NOT NULL,
Primary Key (ininvoiceid)
) LOCK MODE ROW;

Create Table incomingpayment (
amount Decimal(10,2),
companyid Integer NOT NULL,
ininvoiceid Integer NOT NULL,
notesid Integer,
paydate Integer,
paymentid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (paymentid)
) LOCK MODE ROW;

Alter Table incomingpayment add Constraint Foreign Key (ininvoiceid) references incominginvoice (ininvoiceid)  ;
Alter Table incominginvoice add Constraint Foreign Key (companyid) references company (companyid)  ;
Alter Table incomingpayment add Constraint Foreign Key (companyid) references company (companyid)  ;
Alter Table incominginvoice add Constraint Foreign Key (currencyid) references currency (currencyid)  ;
Alter Table incominginvoice add Constraint Foreign Key (supplierid) references supplier (supplierid)  ;
Alter Table incominginvoice add Constraint Foreign Key (notesid) references freetext (freetextid)  ;
Alter Table incomingpayment add Constraint Foreign Key (notesid) references freetext (freetextid)  ;

insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values(15,'INCOMINGINVOICE','Incoming invoice access rights',117,11,'Finance.incomingInvoice.functionality');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values(15,'INCOMINGPAYMENT','Incoming invoice payments access rights',118,11,'Finance.incomingPayment.functionality');

/* Update version constants */
update systemconstant set value='4.0.RC1' where name='APP_VERSION';
update systemconstant set value='4.0' where name='DB_VERSION';

UPDATE STATISTICS HIGH;