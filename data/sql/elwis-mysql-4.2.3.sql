/*
Fer: 20100413 increase telecomnumber length
*/
alter table telecom modify telecomnumber varchar(100);
alter table telecom modify description varchar(200);

/*
Fer: 20100505 Remove the invoice's vatid column
*/
alter table invoice drop foreign key invoice_ibfk_3;
alter table invoice drop column vatid;

/* Fer:20100413 Update version constants */
update systemconstant set value='4.3.5' where name='APP_VERSION';
update systemconstant set value='4.2.3' where name='DB_VERSION';