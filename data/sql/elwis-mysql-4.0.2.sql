/*Ivan:200903116
Alter enddate column in project table*/
alter table project modify enddate int(11) default null;

/* Fernando: Update version constants */
update systemconstant set value='4.0.1' where name='APP_VERSION';
update systemconstant set value='4.0.2' where name='DB_VERSION';