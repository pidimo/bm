/*
miky:20160412
add last action time for app
*/
Alter Table usersessionlog add lastactionapp Int8 before sessionid;


/*
miky:20160414
add field to enable wvapp
*/
Alter Table elwisuser add enablemobilewvapp Smallint before finaldaywork;

/*initialize enabled mobile wvapp users */
UPDATE elwisuser set enablemobilewvapp = 1 WHERE mobileactive = 1 AND moborganizationid IS NOT NULL;


/*
miky:20160422
app versions
*/
update systemconstant set value='6.0.1' where name='APP_VERSION';
update systemconstant set value='5.5' where name='DB_VERSION';
