/*
miky:20160120
add stylesheet type
*/
Alter Table stylesheet add stylesheettype Smallint after stylesheetid;
update stylesheet set stylesheettype = 1;

/*
miky:20160125
modify length of style name
*/
ALTER TABLE style modify name Varchar(255) NOT NULL;


/*
miky:20160129
app versions
*/
update systemconstant set value='5.5.0.4' where name='APP_VERSION';
update systemconstant set value='5.4.2' where name='DB_VERSION';
