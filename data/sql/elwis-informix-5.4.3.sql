/*
miky:20160202
add product event max participants
*/
Alter Table product add maxparticipant Integer before price;


/*
miky:20160202
app versions
*/
update systemconstant set value='5.5.0.5' where name='APP_VERSION';
update systemconstant set value='5.4.3' where name='DB_VERSION';
