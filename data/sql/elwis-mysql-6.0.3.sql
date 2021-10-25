
/*
miky:20180906
add service date in invoice
*/
ALTER TABLE invoice ADD servicedate Int after sequenceruleid;


/*
miky:20180917
app versions
*/
update systemconstant set value='6.5.11' where name='APP_VERSION';
update systemconstant set value='6.0.3' where name='DB_VERSION';
