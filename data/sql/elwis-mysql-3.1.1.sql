/*miky:20081215*/
/*modify accountnumber in bank account*/
ALTER TABLE bankaccount MODIFY accountnumber Varchar(12);

/*Ivan:20081217*/
/*modify invoiceremainon in productcontract table */
alter table productcontract change invoiceremainon matchcalperiod smallint(6);
/*update column matchcalperiod for all productcontracs*/
update productcontract set matchcalperiod=0 where paymethod=2;


/* Update version constants, PUT other sqls above */
update systemconstant set value='3.1.1' where name='APP_VERSION';
update systemconstant set value='3.1.1' where name='DB_VERSION';