/*Ivan:20090317
Add smtp configuration in  mailaccount
*/
alter table mailaccount add smtpauth smallint default null before usermailid;
alter table mailaccount add smtpport varchar(50) default null before usermailid;
alter table mailaccount add smtpserver varchar(50) default null before usermailid;
alter table mailaccount add smtpssl smallint default null before usermailid;

/* ivan: Update version constants */
update systemconstant set value='4.0.2' where name='APP_VERSION';
update systemconstant set value='4.0.3' where name='DB_VERSION';

UPDATE STATISTICS HIGH;