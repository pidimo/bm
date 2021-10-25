/*
miky:20161223
add reply message fields
*/
Alter Table mailaccount add replymessagehtmlid Integer before replymessage;
Alter Table mailaccount add replymessagetextid Integer before replymessage;

Alter Table mailaccount add Constraint Foreign Key (replymessagehtmlid) references freetext (freetextid);
Alter Table mailaccount add Constraint Foreign Key (replymessagetextid) references freetext (freetextid);


/*
miky:20170117
app versions
*/
update systemconstant set value='6.4.1' where name='APP_VERSION';
update systemconstant set value='5.9.3' where name='DB_VERSION';
