/*
miky:20130927
user access rights on data level
*/
Create Table useraddressaccess (
addressid Int NOT NULL,
companyid Int NOT NULL,
usergroupid Int NOT NULL,
Primary Key (addressid, usergroupid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table useraddressaccess add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table useraddressaccess add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;
Alter Table useraddressaccess add Foreign Key (usergroupid) references usergroup (usergroupid) on delete  restrict on update  restrict;

/*add is public in address */
ALTER TABLE address ADD ispublic Smallint DEFAULT 1 after imageid;
UPDATE address set ispublic = 0 where personal = 1;


/*
miky:20131010
update versions
*/
update systemconstant set value='5.1.RC2' where name='APP_VERSION';
update systemconstant set value='5.0.1' where name='DB_VERSION';
