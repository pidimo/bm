/*
miky:20150123
add size field
*/
Alter Table campaigntext add size Int after campaignid;

/*
miky:20150225
add fields
*/
Alter Table campgeneration add requestlocale Varchar(30) after generationtime;
Alter Table campgeneration add notificationmail Varchar(250) after generationtime;


/*
miky:20150225
app versions
*/
update systemconstant set value='5.3.5' where name='APP_VERSION';
update systemconstant set value='5.2.2' where name='DB_VERSION';
