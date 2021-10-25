/*
miky:20151208
add product type type
*/
Alter Table producttype add producttypetype Smallint after companyid;
update producttype set producttypetype = 1;

Alter Table product add initdatetime bigint after descriptionid;
Alter Table product add enddatetime bigint after descriptionid;
Alter Table product add websitelink varchar(255) after version;
Alter Table product add eventaddress varchar(255) after enddatetime;

/*add wv app fields in user*/
Alter Table elwisuser add visiblemobileapp Smallint after version;
Alter Table elwisuser add moborganizationid Int after mobileactive;
Alter Table elwisuser add foreign key (moborganizationid) references address (addressid) on delete restrict on update restrict;

/*access right*/
insert into systemfunction(allowpermission, code, description, functionid, moduleid, namekey) values(1, 'WVAPPVIEW', 'Wirtschaftsverein-App fields view', 136, -308758540, 'Admin.WirtschaftsvereinApp.view.functionality');



/*
miky:20151211
app versions
*/
update systemconstant set value='5.5.0.2' where name='APP_VERSION';
update systemconstant set value='5.4.1' where name='DB_VERSION';
