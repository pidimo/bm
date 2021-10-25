/*
miky:20110627
update decimal numbers to 4 digits after decimal separator
*/
alter table invoiceposition modify unitprice decimal(14,4);
alter table invoiceposition modify unitpricegross decimal(14,4);

alter table saleposition modify unitprice decimal(14,4);
alter table saleposition modify unitpricegross decimal(14,4);

alter table actionposition modify price decimal(14,4);
alter table actionposition modify unitpricegross decimal(14,4);

/*****************************/

/*****************************/

/*
Fernando:20110628 Extending zip code length from 8 to 10 characters
 */
alter table city modify zip nvarchar(10);
alter table address modify zipofpobox nvarchar(10);

/*
Fernando:20110628 Extending branch's label length from 40 to 100 characters
*/
alter table branch modify branchname nvarchar(100);

/*
miky:20110630
add sequence rule relation in invoice and invoicefreenum
*/
alter table invoicefreenum add sequenceruleid integer;
alter table invoicefreenum add constraint foreign key (sequenceruleid) references sequencerule (numberid);

UPDATE invoicefreenum SET sequenceruleid = (SELECT s.numberid FROM sequencerule s
WHERE invoicefreenum.companyid = s.companyid AND invoicefreenum.ruleformat = s.format AND s.type = 1);

alter table invoice add sequenceruleid integer before type;
alter table invoice add constraint foreign key (sequenceruleid) references sequencerule (numberid);

UPDATE invoice SET sequenceruleid = (SELECT s.numberid FROM sequencerule s
WHERE invoice.companyid = s.companyid AND invoice.ruleformat = s.format AND s.type = 1);

/*
miky:20110704
add debitor in sequence rule
*/
alter table sequencerule add debitorid integer before format;
alter table sequencerule add constraint foreign key (debitorid) references address (addressid);
alter table sequencerule add debitornumber nvarchar(20,0) before format;

/*
miky:20110704
add taxkey
*/
alter table vat add taxkey integer before vatid;

/*
miky:20110704
add account relation in bank account
*/
alter table bankaccount add accountid integer before accountnumber;
alter table bankaccount add constraint foreign key (accountid) references account (accountid);

/*
miky:20110705
add description in bank account
*/
alter table bankaccount add description nvarchar(250,0) before iban;

/*
miky:20110705
add bank account relation
*/
alter table invoicepayment add bankaccountid integer before companyid;
alter table invoicepayment add constraint foreign key (bankaccountid) references bankaccount (bankaccountid);


/*
Fernando:20110705 Clearing lists search persistence status of old parameters
 */
delete from usersessionparam where paramname like 'name1@_name2@_name3@_searchName';
delete from usersessionparam where paramname like 'name1A1@_name2A1@_name3A1@_searchNameA1';
delete from usersessionparam where paramname like 'name1A1@_name2A1@_searchNameA1@_contactPersonNameCP1A2@_contactPersonNameCP2A2@_searchNameCPA2';

/*
miky:20110709
Lexware Interface module access rights
*/
insert into systemmodule (description,moduleid,modulenamekey,modulepath) values('Lexware module', 13, 'module.lexware','/lexware' );
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICECUSTOMERDATAEXPORT', 'Invoice customer data report', 121, 13, 'InvoiceCustomerDataExport.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICEPARTNERDATAEXPORT', 'Invoice partner data report', 122, 13, 'InvoicePartnerDataExport.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICECUSTOMEREXPORT', 'Invoice customer report', 123, 13, 'InvoiceCustomerExport.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICEPARTNEREXPORT', 'Invoice partner report', 124, 13, 'InvoicePartnerExport.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICECUSTOMERPAYMENTEXPORT', 'Invoice customer payment report', 125, 13, 'InvoiceCustomerPaymentExport.functionality');
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'INVOICEPARTNERPAYMENTEXPORT', 'Invoice partner payment report', 126, 13, 'InvoicePartnerPaymentExport.functionality');

/*
miky:20110710
add exported field
*/
alter table invoiceposition add exported Smallint before freetextid;
alter table invoicepayment add exported Smallint before freetextid;

/*
miky:20110710
add default discount in customer
*/
alter table customer add defaultdiscount decimal(13,2) before employeeid;

/* Fernando: Update version constants */
update systemconstant set value='4.5' where name='APP_VERSION';
update systemconstant set value='4.3' where name='DB_VERSION';

UPDATE STATISTICS HIGH;
