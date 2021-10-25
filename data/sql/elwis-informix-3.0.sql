/*Ivan:20080805*/
/*add prefix column for email accounts*/
alter table mailaccount add prefix nvarchar(250,0) before servername;

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
Create Table invoice (
	addressid Integer NOT NULL,
	companyid Integer NOT NULL,
	contactpersonid Integer,
	currencyid Integer NOT NULL,
	documentid Integer,
	invoiceid Integer NOT NULL,
	invoicedate Integer NOT NULL,
	notesid Integer,
	number nVarchar(200,0) NOT NULL,
	openamount Decimal(10,2),
	payconditionid Integer NOT NULL,
	paymentdate Integer NOT NULL,
	reminderlevel Integer,
	reminderdate Integer,
	type Smallint NOT NULL,
	totalamountnet Decimal(10,2),
	totalamountgross Decimal(10,2),
	templateid Integer NOT NULL,
	vatid Integer NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (invoiceid)
) LOCK MODE ROW;

Create Table invoiceposition (
	accountid Integer NOT NULL,
	companyid Integer NOT NULL,
	contractid Integer,
	freetextid Integer,
	invoiceid Integer NOT NULL,
	paystepid Integer,
	productid Integer NOT NULL,
	number Integer NOT NULL,
	positionid Integer NOT NULL,
	quantity Integer NOT NULL,
	totalprice Decimal(10,2) NOT NULL,
	unitprice Decimal(10,2) NOT NULL,
	version Integer Default 0 NOT NULL,
	vatid Integer NOT NULL,
	vatrate Decimal(10,2) NOT NULL,
Primary Key (positionid)
) LOCK MODE ROW;

Create Table invoicereminder (
	companyid Integer NOT NULL,
	date Integer NOT NULL,
	descriptionid Integer,
	documentid Integer,
	invoiceid Integer NOT NULL,
	reminderid Integer NOT NULL,
	reminderlevelid Integer NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (reminderid)
) LOCK MODE ROW;

Create Table invoicepayment (
	amount Decimal(10,2) NOT NULL,
	companyid Integer NOT NULL,
	freetextid Integer,
	invoiceid Integer NOT NULL,
	paydate Integer NOT NULL,
	paymentid Integer NOT NULL,
	version Integer NOT NULL,
Primary Key (paymentid)
) LOCK MODE ROW;

Create Table reminderlevel (
	companyid Integer NOT NULL,
	fee Decimal(5,2),
	level Integer NOT NULL,
	name nVarchar(100,0) NOT NULL,
	numberofdays Integer NOT NULL,
	reminderlevelid Integer NOT NULL,
	version Integer NOT NULL,
Primary Key (reminderlevelid)
) LOCK MODE ROW;

Create Table remindertext (
	companyid Integer NOT NULL,
	freetextid Integer NOT NULL,
	isdefault Smallint NOT NULL,
	languageid Integer NOT NULL,
	reminderlevelid Integer NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (languageid,reminderlevelid)
) LOCK MODE ROW;

Create Table sequencerule (
	companyid Integer NOT NULL,
	format nVarchar(150,0) NOT NULL,
	isdefault Smallint NOT NULL,
	lastnumber Integer,
	lastdate Integer,
	numberid Integer NOT NULL,
	resettype Smallint,
	startnumber Integer,
	type Smallint NOT NULL,
	version Integer NOT NULL,
Primary Key (numberid)
) LOCK MODE ROW;

Create Table invoicetemplate (
	companyid Integer NOT NULL,
	templateid Integer NOT NULL,
	title nVarchar(150,0) NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (templateid)
) LOCK MODE ROW;

Create Table invoicetext (
	companyid Integer NOT NULL,
	freetextid Integer NOT NULL,
	isdefault Smallint NOT NULL,
	languageid Integer NOT NULL,
	templateid Integer NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (languageid,templateid)
) LOCK MODE ROW;

Create Table account (
	accountid Integer NOT NULL,
	companyid Integer NOT NULL,
	name nVarchar(150,0) NOT NULL,
	number nVarchar(150,0) NOT NULL,
	version Integer NOT NULL,
Primary Key (accountid)
) LOCK MODE ROW;

Create Table invoicevat (
	amount Decimal(13,2) NOT NULL,
	companyid Integer NOT NULL,
	invoiceid Integer NOT NULL,
	vatid Integer NOT NULL,
	vatrate Decimal(5,2) NOT NULL,
Primary Key (invoiceid,vatid)
) LOCK MODE ROW;

Create Table producttext (
	companyid Integer NOT NULL,
	isdefault Smallint NOT NULL,
	freetextid Integer NOT NULL,
	languageid Integer NOT NULL,
	productid Integer NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (languageid,productid)
) LOCK MODE ROW;

Create Table payconditiontext (
	companyid Integer NOT NULL,
	freetextid Integer NOT NULL,
	languageid Integer NOT NULL,
	payconditionid Integer NOT NULL,
	version Integer NOT NULL,
Primary Key (languageid,payconditionid)
) LOCK MODE ROW;

Create Table contracttype (
	companyid Integer NOT NULL,
	contracttypeid Integer NOT NULL,
	name nVarchar(100,0) NOT NULL,
	tobeinvoiced Smallint NOT NULL,
	version Integer Default 0 NOT NULL,
Primary Key (contracttypeid)
) LOCK MODE ROW;

Alter table company add invoicedayssend Integer  default 0 not null before language;

Alter table product add accountid Integer before companyid;
Alter table product add langtextid Integer  before price;
Alter table product add Constraint Foreign Key (accountid) references account (accountid);

Alter table invoice add Constraint Foreign Key (payconditionid) references paycondition (payconditionid);
Alter table invoice add Constraint Foreign Key (currencyid) references currency (currencyid);
Alter table invoice add Constraint Foreign Key (vatid) references vat (vatid);
Alter table invoice add Constraint Foreign Key (notesid) references freetext (freetextid);
Alter table invoice add Constraint Foreign Key (documentid) references freetext (freetextid);
Alter table invoice add Constraint Foreign Key (addressid) references address (addressid);
Alter table invoice add Constraint Foreign Key (templateid) references invoicetemplate (templateid);
Alter table invoice add Constraint Foreign Key (companyid) references company (companyid);
Alter table invoice add Constraint Foreign Key (addressid,contactpersonid) references contactperson (addressid,contactpersonid);

Alter table invoiceposition add Constraint Foreign Key (vatid) references vat (vatid);
Alter table invoiceposition add Constraint Foreign Key (productid) references product (productid);
Alter table invoiceposition add Constraint Foreign Key (invoiceid) references invoice (invoiceid);
Alter table invoiceposition add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table invoiceposition add Constraint Foreign Key (accountid) references account (accountid);
Alter table invoiceposition add Constraint Foreign Key (companyid) references company (companyid);

Alter table invoicereminder add Constraint Foreign Key (invoiceid) references invoice (invoiceid);
Alter table invoicereminder add Constraint Foreign Key (documentid) references freetext (freetextid);
Alter table invoicereminder add Constraint Foreign Key (descriptionid) references freetext (freetextid);
Alter table invoicereminder add Constraint Foreign Key (companyid) references company (companyid);
Alter table invoicereminder add Constraint Foreign Key (reminderlevelid) references reminderlevel (reminderlevelid);

Alter table invoicepayment add Constraint Foreign Key (invoiceid) references invoice (invoiceid);
Alter table invoicepayment add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table invoicepayment add Constraint Foreign Key (companyid) references company (companyid);

Alter table reminderlevel add Constraint Foreign Key (companyid) references company (companyid);

Alter table remindertext add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table remindertext add Constraint Foreign Key (companyid) references company (companyid);
Alter table remindertext add Constraint Foreign Key (languageid) references language (languageid);
Alter table remindertext add Constraint Foreign Key (reminderlevelid) references reminderlevel (reminderlevelid);

Alter table sequencerule add Constraint Foreign Key (companyid) references company (companyid);

Alter table invoicetemplate add Constraint Foreign Key (companyid) references company (companyid);

Alter table invoicetext add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table invoicetext add Constraint Foreign Key (templateid) references invoicetemplate (templateid);
Alter table invoicetext add Constraint Foreign Key (companyid) references company (companyid);
Alter table invoicetext add Constraint Foreign Key (languageid) references language (languageid);

Alter table account add Constraint Foreign Key (companyid) references company (companyid);

Alter table invoicevat add Constraint Foreign Key (vatid) references vat (vatid);
Alter table invoicevat add Constraint Foreign Key (invoiceid) references invoice (invoiceid);
Alter table invoicevat add Constraint Foreign Key (companyid) references company (companyid);

Alter table producttext add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table producttext add Constraint Foreign Key (companyid) references company (companyid);
Alter table producttext add Constraint Foreign Key (languageid) references language (languageid);
Alter table producttext add Constraint Foreign Key (productid) references product (productid);

Alter table payconditiontext add Constraint Foreign Key (payconditionid) references paycondition (payconditionid);
Alter table payconditiontext add Constraint Foreign Key (freetextid) references freetext (freetextid);
Alter table payconditiontext add Constraint Foreign Key (companyid) references company (companyid);
Alter table payconditiontext add Constraint Foreign Key (languageid) references language (languageid);

Alter table contracttype add Constraint Foreign Key(companyid) references company (companyid);


/*miky:20080908*/
Alter Table reportcolumn add categoryid Integer before columnorder;
Alter Table reportfilter add categoryid Integer before columnref;
/*foreign relations*/
Alter Table reportcolumn add Constraint Foreign Key (categoryid) references category (categoryid);
Alter Table reportfilter add Constraint Foreign Key (categoryid) references category (categoryid);


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

UPDATE STATISTICS HIGH;