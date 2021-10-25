/*
miky:20140728
add total record field
*/
ALTER TABLE importprofile ADD totalrecord Integer before userid;
ALTER TABLE importprofile ADD importstarttime Int8 before label;


/*
miky:20140728
app versions
*/
update systemconstant set value='5.2.5' where name='APP_VERSION';
update systemconstant set value='5.1.3' where name='DB_VERSION';
