/*
miky:20160810
add field to category, this is to identifier the category as field in UI
*/
Alter Table category add fieldidentifier Varchar(60) after descriptionid;

/*
NOTE: The field identifier should be unique for a category by company
*/
update category set fieldidentifier = 'CUSTOMER_BUSINESS_AREA' where categoryid = 2228;


/*
miky:20160815
app versions
*/
update systemconstant set value='6.1.1' where name='APP_VERSION';
update systemconstant set value='5.7' where name='DB_VERSION';