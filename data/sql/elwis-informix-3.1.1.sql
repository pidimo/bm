/*miky:20081215*/
/*modify accountnumber in bank account*/
ALTER TABLE bankaccount MODIFY accountnumber nVarchar(12,0);

/*Ivan:20081217*/
/*modify invoiceremainon in productcontract table */
rename column productcontract.invoiceremainon to matchcalperiod;
/*update column matchcalperiod for all productcontracs*/
update productcontract set matchcalperiod=0 where paymethod=2;


/* Update version constants, PUT other sqls above */
update systemconstant set value='3.1.1' where name='APP_VERSION';
update systemconstant set value='3.1.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;