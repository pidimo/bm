/*
miky:20160215
add product close date time
*/
Alter Table product add closingdatetime bigint after accountid;


/*
miky:20160216
app versions
*/
update systemconstant set value='5.5.0.6' where name='APP_VERSION';
update systemconstant set value='5.4.4' where name='DB_VERSION';
