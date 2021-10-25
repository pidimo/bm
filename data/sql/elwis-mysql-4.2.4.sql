/*
Ivan Alban:20100603
Add relation table between images and signatures.
*/

Create table signatureimg(
companyid int not null,
imagestoreid int not null,
signatureid int not null,
signatureimgid int not null,
Primary Key (signatureimgid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table signatureimg add Foreign Key (signatureid) references signature (signatureid) on delete  restrict on update  restrict;
Alter table signatureimg add Foreign Key (imagestoreid) references imagestore (imagestoreid) on delete  restrict on update  restrict;
Alter table signatureimg add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;

/*
Ivan Alban:20100823
Add time zone column in company table
*/
alter table company add timezone varchar(25) after timeout;

/* Fer:20100824 Update version constants */
update systemconstant set value='4.3.6' where name='APP_VERSION';
update systemconstant set value='4.2.4' where name='DB_VERSION';