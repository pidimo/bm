/*
miky:20131018
add type in additional address
*/
ALTER TABLE addaddress ADD addaddresstype Smallint DEFAULT 2 NOT NULL after addaddressline;

/*
miky:20131021
add group type in user group
*/
ALTER TABLE usergroup ADD grouptype Smallint after groupname;
update usergroup set grouptype=1;
ALTER TABLE usergroup MODIFY grouptype Smallint NOT NULL;


/*
miky:20131023
*/
update systemconstant set value='5.1' where name='APP_VERSION';
update systemconstant set value='5.0.2' where name='DB_VERSION';
