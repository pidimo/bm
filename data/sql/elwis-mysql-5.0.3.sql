/*
fernando:20140402
update ssl use values for pop and smpt
*/
update mailaccount set smtpssl=null  where smtpssl=0;
update mailaccount set usesslconection=null  where usesslconection=0;
ALTER TABLE mailaccount MODIFY smtpssl Smallint;


/*
miky:20131023
*/
update systemconstant set value='5.1.3' where name='APP_VERSION';
update systemconstant set value='5.0.3' where name='DB_VERSION';
