/*
miky:20161223
add reply message fields
*/
Alter Table mailaccount add replymessagehtmlid Int after removeafterof;
Alter Table mailaccount add replymessagetextid Int after replymessagehtmlid;

Alter Table mailaccount add Foreign Key (replymessagehtmlid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter Table mailaccount add Foreign Key (replymessagetextid) references freetext (freetextid) on delete  restrict on update  restrict;


/*
miky:20170117
app versions
*/
update systemconstant set value='6.4.1' where name='APP_VERSION';
update systemconstant set value='5.9.3' where name='DB_VERSION';
