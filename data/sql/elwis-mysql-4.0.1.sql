/*Ivan:20090310
Add assignee column in project time*/
alter table projecttime add column assigneeid int(11) FIRST;
alter table projecttime add foreign key (assigneeid) references address (addressid) on delete  restrict on update restrict;

/*Ivan:20090311
add table projectassignee 
*/
Create table projectassignee (
companyid Int NOT NULL,
permission Tinyint NOT NULL,
projectid Int NOT NULL,
addressid Int NOT NULL,
version Int NOT NULL,
Primary Key (projectid,addressid)) ENGINE = InnoDB
CHARACTER SET latin1 COLLATE latin1_general_ci;

alter table projectassignee add foreign key (addressid) references address (addressid) on delete  restrict on update restrict;
alter table projectassignee add foreign Key (companyid) references company (companyid) on delete  restrict on update restrict;
alter table projectassignee add foreign Key (projectid) references project (projectid) on delete  restrict on update restrict;

/*Ivan:20090312
Remove column invoiceable from projecttime table
*/
alter table projecttime drop column invoiceable;
alter table projecttime modify projectactivityid int(11) default null;
drop table projectuser;

/*Ivan:20090313
Change accessright resource for projectAssignee
*/
update systemfunction set namekey='ProjectAssignee.functionality', description='Project assignee' where functionid=115;

/* Fernando: Update version constants */
update systemconstant set value='4.0' where name='APP_VERSION';
update systemconstant set value='4.0.1' where name='DB_VERSION';