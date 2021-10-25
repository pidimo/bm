/*
miky:20161125
add incoming invoice document field
*/
Alter Table incominginvoice add documentid Integer before ininvoiceid;
Alter Table incominginvoice add Constraint Foreign Key (documentid) references freetext (freetextid);


/*
miky:20161206
app versions
*/
update systemconstant set value='6.4' where name='APP_VERSION';
update systemconstant set value='5.9.2' where name='DB_VERSION';
