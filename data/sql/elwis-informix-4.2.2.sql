/*
Ivan Alban: 20091026
Add smtplogin, smtppassword and usepopconf columns in mailaccount table.
*/
alter table mailaccount add smtplogin nvarchar(50,0) before smtpauth;
alter table mailaccount add smtppassword nvarchar(50,0) before smtpport;
alter table mailaccount add  usepopconf Smallint before usesslconection;
update mailaccount set usepopconf=1;

/*
miky:20091111
Add columns to remind end contracts
*/
alter table productcontract add daystoremind Integer before discount;
alter table productcontract add remindertime  Int8 before salepositionid;
alter table company add emailcontract nvarchar(200) before finishlicense;

/*
Ivan Alban: 20091112
modify the data type of amount field in actionposition table
modify the data type of quantity field in saleposition table.
*/
alter table actionposition modify amount decimal(10,2);
alter table saleposition modify quantity decimal(10,2);

/*miky:20091123*/
/*add column to discount value*/
ALTER TABLE invoiceposition ADD discountvalue Decimal(10,2) before freetextid;

/* fer:20091130 Update version constants */
update systemconstant set value='4.3' where name='APP_VERSION';
update systemconstant set value='4.2.2' where name='DB_VERSION';

UPDATE STATISTICS HIGH;