/*
miky:20170522
add template id in action type
*/
alter table actiontype add templateid integer before typename;
alter table actiontype add constraint foreign key (templateid) references template (templateid);


/*
miky:20170616
app versions
*/
update systemconstant set value='6.5.3' where name='APP_VERSION';
update systemconstant set value='6.0.1' where name='DB_VERSION';
