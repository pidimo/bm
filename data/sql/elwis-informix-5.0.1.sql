/*
miky:20130927
user access rights on data level
*/
Create Table useraddressaccess (
addressid Integer NOT NULL,
companyid Integer NOT NULL,
usergroupid Integer NOT NULL,
Primary Key (addressid, usergroupid)) LOCK MODE ROW;

Alter Table useraddressaccess add Constraint Foreign Key (companyid) references company (companyid);
Alter Table useraddressaccess add Constraint Foreign Key (addressid) references address (addressid);
Alter Table useraddressaccess add Constraint Foreign Key (usergroupid) references usergroup (usergroupid);

/*add is public in address */
ALTER TABLE address ADD ispublic Smallint DEFAULT 1 before keywords;
UPDATE address set ispublic = 0 where personal = 1;

/*
miky:20131010
update versions
*/
update systemconstant set value='5.1.RC2' where name='APP_VERSION';
update systemconstant set value='5.0.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;