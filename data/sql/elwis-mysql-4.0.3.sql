/*Ivan:20090317
Add smtp configuration in  mailaccount
*/
alter table mailaccount add column smtpauth tinyint default null after serverport;
alter table mailaccount add column smtpport varchar(50) default null after smtpauth;
alter table mailaccount add column smtpserver varchar(50) default null after smtpport;
alter table mailaccount add column smtpssl tinyint default null after smtpserver;

/* Ivan: Update version constants */
update systemconstant set value='4.0.2' where name='APP_VERSION';
update systemconstant set value='4.0.3' where name='DB_VERSION';


