/*
miky:20150810
add mobile account access
*/
Alter Table elwisuser add mobileactive Smallint before password;
Alter Table elwisuser add mobilefromdate Integer before password;
Alter Table elwisuser add mobiletodate Integer before password;


/*
miky:20150810
app versions
*/
update systemconstant set value='5.4.1.7' where name='APP_VERSION';
update systemconstant set value='5.3.2' where name='DB_VERSION';
