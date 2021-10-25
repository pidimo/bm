/*
miky:20160504
add field to mark wvapp member as favorite
*/
Alter Table elwisuser add isfavoritewvapp Smallint after isdefault;


/*
miky:20160531
access right for view way description field
*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'WAYDESCRIPTIONVIEW', 'Enable view of field way description in person or organization', 137, 1, 'Contact.wayDescription.view.functionality');

/*update versions*/
update systemconstant set value='6.0.2' where name='APP_VERSION';
update systemconstant set value='5.6' where name='DB_VERSION';

