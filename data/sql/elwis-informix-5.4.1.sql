/*
miky:20151208
add product type type
*/
Alter Table producttype add producttypetype Smallint before typeid;
update producttype set producttypetype = 1;

Alter Table product add initdatetime Int8 before langtextid;
Alter Table product add enddatetime Int8 before initdatetime;
Alter Table product add websitelink NVarchar(255);
Alter Table product add eventaddress NVarchar(255) before initdatetime;

/*add wv app fields in user*/
Alter Table elwisuser add visiblemobileapp Smallint;
Alter Table elwisuser add moborganizationid Integer before password;
Alter Table elwisuser add Constraint foreign key (moborganizationid) references address (addressid);

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'WVAPPVIEW', 'Wirtschaftsverein-App fields view', 136, -308758540, 'Admin.WirtschaftsvereinApp.view.functionality');



/*
miky:20151211
app versions
*/
update systemconstant set value='5.5.0.2' where name='APP_VERSION';
update systemconstant set value='5.4.1' where name='DB_VERSION';
