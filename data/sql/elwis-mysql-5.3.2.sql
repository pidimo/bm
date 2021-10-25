/*
miky:20150810
add mobile account access
*/
Alter Table elwisuser add mobileactive Smallint after maxrecentlist;
Alter Table elwisuser add mobilefromdate Int after mobileactive;
Alter Table elwisuser add mobiletodate Int after mobilefromdate;


/*
miky:20150810
app versions
*/
update systemconstant set value='5.4.1.7' where name='APP_VERSION';
update systemconstant set value='5.3.2' where name='DB_VERSION';
