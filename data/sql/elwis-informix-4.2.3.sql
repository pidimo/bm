/*
Fer: 20100413 increase telecomnumber length
*/
alter table telecom modify telecomnumber nvarchar(100,0);
alter table telecom modify description nvarchar(200,0);

/*
Fer: 20100505 Remove the invoice's vatid column
*/
alter table invoice drop vatid;

/* Fer:20100413 Update version constants */
update systemconstant set value='4.3.5' where name='APP_VERSION';
update systemconstant set value='4.2.3' where name='DB_VERSION';

UPDATE STATISTICS HIGH;