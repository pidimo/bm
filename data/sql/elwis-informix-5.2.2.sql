/*
miky:20150123
add size field
*/
Alter Table campaigntext add size Integer before templateid;

/*
miky:20150225
add fields
*/
Alter Table campgeneration add requestlocale NVarchar(30) before senderemployeeid;
Alter Table campgeneration add notificationmail NVarchar(250) before requestlocale;


/*
miky:20150225
app versions
*/
update systemconstant set value='5.3.5' where name='APP_VERSION';
update systemconstant set value='5.2.2' where name='DB_VERSION';
