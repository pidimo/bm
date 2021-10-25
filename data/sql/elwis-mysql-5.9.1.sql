/*
miky:20161012
add fields in contract
*/
Alter Table productcontract add invoicedelay Int after installment;
Alter Table productcontract add nextinvoicedate Int after netgross;

/*define the nextinvoicedate for current periodic contracts*/
update productcontract set nextinvoicedate = paystartdate where paymethod = 2 AND invoiceduntil IS NULL;
update productcontract set nextinvoicedate = invoiceduntil where paymethod = 2 AND invoiceduntil IS NOT NULL;


/*
miky:20161017
add field to cancel contracts
*/
Alter Table productcontract add cancelled Smallint after amountype;

/*define the cancelled field to default value 0*/
update productcontract set cancelled = 0;


/*
miky:20161018
app versions
*/
update systemconstant set value='6.3.2' where name='APP_VERSION';
update systemconstant set value='5.9.1' where name='DB_VERSION';
