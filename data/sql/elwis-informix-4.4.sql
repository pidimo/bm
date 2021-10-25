/*
miky:20120502
add has email and telecom type in campaign
*/
alter table campaign add hasemail Smallint before includepartner;
alter table campaign add emailtelecomtype integer before employeeid;

/*
miky:20120510
add period price and price per month in productcontract
*/
alter table productcontract add priceperiod Smallint before remindertime;
alter table productcontract add pricepermonth Decimal(10,2) before remindertime;

update productcontract set priceperiod = 1 where paymethod = 2;
update productcontract set pricepermonth = price where paymethod = 2;

/*
miky:20120518
add invoice mail template id field in company and flag to define as temporal draft in mail
*/
alter table company add invmailtemplatid integer before invoicedayssend;
alter table company add constraint foreign key (invmailtemplatid) references template (templateid);

alter table mail add isdrafttemp Smallint before isinout;

/*
miky:20120529
add emlattachuuid in attach and set null to file
*/
alter table attach add emlattachuuid nvarchar(50,0) before file;
alter table attach modify file blob;

/* Fernando: SQL to improve performance */
create index ap_userdate on appointment (userid, startdatetime, enddatetime);


/* Fernando: Update version constants */
update systemconstant set value='4.7' where name='APP_VERSION';
update systemconstant set value='4.4' where name='DB_VERSION';

UPDATE STATISTICS HIGH;



