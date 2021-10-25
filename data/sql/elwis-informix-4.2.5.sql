/*
Ivan Alban:20101108
Add a company configuration column for select the default media type when create a sale action.
*/

alter table company add mediatype char(1) before netgross;

/*
Adding the createdatetime column in task table
*/

alter table task add createdatetime int8 before expiredate;

/*
Adding the updatedatetime column in task table
*/

alter table task add updatedatetime int8 before userid;


/*
Ivan Alban :20101112
Add the createdatetime and updatedatetime in action table to store the create and update date times.
*/

alter table action add createdatetime int8 before companyid;

alter table action add updatedatetime int8 before value;

/*
Ivan Alban:  20101115
Add userid in action table to store the creator user identifier
*/

alter table action add userid integer before value;
alter table action add constraint foreign key (userid) references elwisuser (userid);

/*
Ivan Alban: 20101201
Add processid column in categfieldvalue table
*/

alter table categfieldvalue add processid integer before productid;
alter table categfieldvalue add constraint foreign key (processid) references salesprocess (processid);

 /*
 Ivan Alban: 20101215
 Add docfilename column in contact table
 */
 alter table contact add docfilename nvarchar(250) before employeeid;

/*
Ivan Alban: 20101217
Update USERINTERFACE, COMPANYINTERFACE, COMPANYLOGO from UI manager to Home manager.
*/

update systemfunction set moduleid = 10 where code = 'USERINTERFACE';
update systemfunction set moduleid = 10 where code = 'COMPANYINTERFACE';
update systemfunction set moduleid = 10 where code = 'COMPANYLOGO';
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(5, 'COMPANYPREFERENCES', 'The company settings view and update accessrights', 120, 10, 'Home.companyPreferences');
insert into companymodule(companyid, moduleid, maintablelimit, active)
select companyid, 10 as moduleid, '' as maintablelimit, 1 as active
   from companymodule
   where companymodule.moduleid = 7
         and companymodule.companyid not in (
   select companyid
   from companymodule
   where (companymodule.moduleid = 10));
update accessrights set moduleid = 10 where moduleid = 7;
delete companymodule where moduleid = 7;
delete systemmodule where moduleid = 7; 

/*
Miky:20101220
add maxmaxattachsize in company table
*/
alter table company add maxmaxattachsize integer before mediatype;
update company set maxmaxattachsize = 9 where maxmaxattachsize is null;

/* Ivan: 20101221 Update version constants */
update systemconstant set value='4.4' where name='APP_VERSION';
update systemconstant set value='4.2.5' where name='DB_VERSION';

UPDATE STATISTICS HIGH;
