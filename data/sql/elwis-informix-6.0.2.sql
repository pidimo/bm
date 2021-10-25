/*
miky:20170720
insert new criterion value 'keywords' for contact & contact person
*/
INSERT INTO campcriterionvalue(campcriterionvalueid, tableid, descriptionkey, field, fieldtype, fieldname, tablename, relationfield, cpersonfield)
VALUES(29, 5, "campaign.addressKeywords", "keywords", 4, "0", "address", null, "cpKeywords");


/*
miky:20170724
app versions
*/
update systemconstant set value='6.5.4' where name='APP_VERSION';
update systemconstant set value='6.0.2' where name='DB_VERSION';
