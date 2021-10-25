/*
miky:20150928
add mobile access in company
*/
Alter Table company add mobileactive Smallint after mediatype;
Alter Table company add mobileendlicense Int after mobileactive;
Alter Table company add mobilestartlicen Int after mobileendlicense;
Alter Table company add mobileuserallowed Int after mobilestartlicen;

ALTER TABLE elwisuser DROP COLUMN mobilefromdate;
ALTER TABLE elwisuser DROP COLUMN mobiletodate;

/*
miky:20150928
app versions
*/
update systemconstant set value='5.4.1.8' where name='APP_VERSION';
update systemconstant set value='5.3.3' where name='DB_VERSION';
