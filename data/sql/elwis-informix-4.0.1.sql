/*Ivan:20090310
Add assignee column in project time*/
alter table projecttime add assigneeid integer before companyid;
alter table projecttime add constraint foreign key (assigneeid) references address (addressid);

/*Ivan:20090311
add table projectassignee
*/
Create table projectassignee (
companyid Integer NOT NULL,
permission Smallint NOT NULL,
projectid Integer NOT NULL,
addressid Integer NOT NULL,
version Integer NOT NULL,
Primary Key (projectid,addressid)) LOCK MODE ROW;

alter table projectassignee add constraint foreign key (addressid) references address (addressid);
alter table projectassignee add constraint foreign Key (companyid) references company (companyid);
alter table projectassignee add constraint foreign Key (projectid) references project (projectid);

/*Ivan:20090312
Remove column invoiceable from projecttime table
*/
alter table projecttime drop invoiceable;
alter table projecttime modify projectactivityid integer default null;
drop table projectuser;

/*Ivan:20090313
Change accessright resource for projectAssignee
*/
update systemfunction set namekey='ProjectAssignee.functionality', description='Project assignee' where functionid=115;

/* Fernando: Update version constants */
update systemconstant set value='4.0' where name='APP_VERSION';
update systemconstant set value='4.0.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;