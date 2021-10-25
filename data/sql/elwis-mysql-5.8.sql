/*
miky:20160826
access right for campaign cascade delete
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(8, 'CAMPAIGNCASCADEDELETE', 'Enable campaign cascade delete', 138, 2, 'Campaign.cascadeDelete.functionality');

/*
miky:20160906
app versions
*/
update systemconstant set value='6.2' where name='APP_VERSION';
update systemconstant set value='5.8' where name='DB_VERSION';