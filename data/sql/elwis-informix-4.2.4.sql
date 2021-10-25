/*
Ivan Alban:20100603
Add relation table between images and signatures.
*/

Create table signatureimg(
companyid integer not null,
imagestoreid integer not null,
signatureid integer not null,
signatureimgid integer not null,
Primary Key (signatureimgid)) LOCK MODE ROW;

Alter table signatureimg add Constraint Foreign Key (signatureid) references signature (signatureid);
Alter table signatureimg add Constraint Foreign Key (imagestoreid) references imagestore (imagestoreid);
Alter table signatureimg add Constraint Foreign Key (companyid) references company (companyid);

/*
Ivan Alban:20100823
Add time zone column in company table
*/
alter table company add timezone nvarchar(25) before trial;

/* Fer:201008024 Update version constants */
update systemconstant set value='4.3.6' where name='APP_VERSION';
update systemconstant set value='4.2.4' where name='DB_VERSION';

UPDATE STATISTICS HIGH;