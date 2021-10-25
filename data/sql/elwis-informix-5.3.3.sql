/*
miky:20150928
add mobile access in company
*/
Alter Table company add mobileactive Smallint before netgross;
Alter Table company add mobileendlicense Integer before netgross;
Alter Table company add mobilestartlicen Integer before netgross;
Alter Table company add mobileuserallowed Integer before netgross;

ALTER TABLE elwisuser DROP mobilefromdate;
ALTER TABLE elwisuser DROP mobiletodate;


/*
miky:20150928
app versions
*/
update systemconstant set value='5.4.1.8' where name='APP_VERSION';
update systemconstant set value='5.3.3' where name='DB_VERSION';
