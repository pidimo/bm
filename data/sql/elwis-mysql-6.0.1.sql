/*
miky:20170522
add template id in action type
*/
alter table actiontype add templateid Int after sequence;
alter table actiontype add foreign key (templateid) references template (templateid) on delete  restrict on update  restrict;


/*
miky:20170616
app versions
*/
update systemconstant set value='6.5.3' where name='APP_VERSION';
update systemconstant set value='6.0.1' where name='DB_VERSION';
