/*Ivan:20080704*/
/*Change size for linkvalue column in categfieldvalue table, to 250 characters*/
Alter Table categfieldvalue Modify linkvalue varchar(250); 

/*Updating application and database versions */
update systemconstant set value='2.14.2' where name='APP_VERSION';
update systemconstant set value='2.12.1' where name='DB_VERSION';