/*
Ivan Alban: 20091026
Add smtplogin, smtppassword and usepopconf columns in mailaccount table.
*/
alter table mailaccount add column smtplogin varchar(50) after serverport;
alter table mailaccount add column smtppassword varchar(50) after smtpauth;
alter table mailaccount add column usepopconf tinyint after usermailid;
update mailaccount set usepopconf=1;

/*
miky:20091111
Add columns to remind end contracts
*/
alter table productcontract add daystoremind int after currencyid;
alter table productcontract add remindertime Bigint after price;
alter table company add emailcontract varchar(200) after creditnoteruleid;

/*
Ivan Alban: 20091112
modify the data type of amount field in actionposition table
modify the data type of quantity field in saleposition table.
*/
alter table actionposition modify amount decimal(10,2);
alter table saleposition modify quantity decimal(10,2);

/*miky:20091123*/
/*add column to discount value*/
ALTER TABLE invoiceposition ADD discountvalue Decimal(10,2) after discount;

/* Fer:20091130 Update version constants */
update systemconstant set value='4.3' where name='APP_VERSION';
update systemconstant set value='4.2.2' where name='DB_VERSION';