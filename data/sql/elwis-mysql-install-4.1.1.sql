/*
ELWIS database data initialization script.
@author: Fernando Montaño
Installation script based in the elwis database version 4.1

This is for new installation only.

This creates a basic data for the supercompany:
User: fer
Pwd: .jatun.
Company:jatun
*/

SET FOREIGN_KEY_CHECKS=0;

/* Address Jatun (addressid=1) */
insert into address
(active, addressid, addresstype, bankaccountid, birthday, cityid, code, companyid,
countryid, education, freetextid, housenumber, imageid, keywords, languageid,
lastmoddate, lastmoduser, name1, name2, name3, personal, pobox, recorddate,
recorduser, salutationid, searchname, street, taxnumber, titleid, version,
waydescriptionid, zipofpobox) values
(1, 1, '0', NULL, NULL, NULL, 9, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
20071107, 1, 'Jatun S.R.L.', 'Innovación & Desarrollo', NULL, 0, NULL, 20071107,
1, NULL, 'Jatun', NULL, NULL, NULL, 1, NULL, NULL);

/* Address Fernando (addressid=2) */
insert into address
(active, addressid, addresstype, bankaccountid, birthday, cityid, code, companyid,
countryid, education, freetextid, housenumber, imageid, keywords, languageid,
lastmoddate, lastmoduser, name1, name2, name3, personal, pobox, recorddate,
recorduser, salutationid, searchname, street, taxnumber, titleid, version,
waydescriptionid, zipofpobox) values
(1, 2, '1', NULL, NULL, NULL, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
20071107, 1, 'Montaño', 'Fernando', NULL, 0, NULL, 20071107,
1, NULL, 'fer', NULL, NULL, NULL, 1, NULL, NULL);

/* Contact person */

insert into contactperson
(active, addressid, companyid, contactpersonid, departmentid, function,
persontypeid, recorddate, recorduserid, version)
values (1, 1, 1, 2, NULL, NULL, NULL, 20071107, 1, 1);

/* Employee */

insert into employee    (companyid, costcenterid, costhour, costposition, dateend, departmentid,
employeeid, function, healthfund, hiredate, hourlyrate, initials, officeid,
socialsecnumber, version)
values    (1, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

/* User: fernando:.jatun.*/
insert into elwisuser
(addressid, active, accessip, companyid, dayfragmentation, defaultview,
emailappointment, emailquestion, emailschetask, emailsupportcase,
finaldaywork, favoritelanguage, hasmailaccount, holidaycountryid,
initialdaywork, isdefault, login, maxrecentlist, password,
rowsperpage, seeprivatedata, timeout, timezone,
type, userid, version)
values
(2, 1, NULL, 1, NULL, NULL,
NULL, NULL, NULL, NULL,
NULL, 'en', NULL, NULL,
NULL, 1, 'fer', 10, 'BKuXdAxVcIyJFSYvBVEIoABeyWo=',
15, NULL, 90, NULL,
1, 1, 1);

/* Super Company: jatun */
insert into company
(companyid, copytemplate, finishlicense, language, login, logoid, maildomain,
maxattachsize, routepageid, rowsperpage, startlicense, style, telecomtypestatus,
timeout, trial, usersallowed, version, active, isdefault)
values    (1, NULL, NULL, 'en', 'jatun', NULL, NULL, NULL, NULL, 10, NULL, NULL, NULL,
90, 0, NULL, 1, 1, 1);

/* System constants */

insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with address table','ADDRESS','Category.address','CATEGORYTYPES','4');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('ELWIS source code version','APP_VERSION',NULL,'VERSIONS','4.1');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with contactPerson table','CONTACTPERSON','Category.contactPerson','CATEGORYTYPES','2');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with customer table','CUSTOMER','Category.customer','CATEGORYTYPES','1');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('ELWIS database version','DB_VERSION',NULL,'VERSIONS','4.1');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('English resource language','ENGLISH','Common.english','SYSTEMLANGUAGE','en');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('German resource language','GERMAN','Common.german','SYSTEMLANGUAGE','de');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Spanish resource language','SPAIN','Common.spanish','SYSTEMLANGUAGE','es');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Enable or disable trial company creation','TRIAL',NULL,'COMPANYCREATION','1');

/* System modules */

insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Administration module',-308758540,'package.Administration','/admin');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Modulo de contacts',1,'module.contactManager','/contacts');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Modulo de campaigns',2,'module.campaign','/campaign');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of products',3,'module.products','/products');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of sales',4,'module.sales','/sales');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of webmail',5,'module.webmail','/webmail');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module Scheduler',6,'module.scheduler','/scheduler');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module UIMAnager',7,'module.UIManager','/');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module support',8,'module.support','/support');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of reports',9,'module.reports','/reports');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Home module',10,'Common.home','/');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of finances',11,'module.finance','/finance');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Project module',12,'module.projects','/projects');

/* System functions */
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (7,'FAVORITE','Contact favorites',-555212806,1,'Contact.favorites');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CUSTOMER','Customer  base functionality',-552432681,1,'Product.Tab.Customers');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPLIER','Suplier base functionality',-552347618,1,'Product.Tab.Supplier');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCT','Product',-536716150,3,'Product.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRICING','Pricing',-536452275,3,'Product.Tab.Pricing');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'COMPETITOR','Product Competitor',-536362806,3,'Product.Tab.Competitors');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PICTURE','Product Picture',-536206447,3,'Product.Tab.Pictures');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COMPETITORPRODUCT','Competitor Product',-532549822,3,'Competitor.competitorProduct');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTCATEGORY','Product Category',-532414603,3,'ProductCategory.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESS','Sales process main',-314718634,4,'SalesProcess');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'SALESPROCESSACTION','sales process actions',-313932025,4,'SalesProcessAction.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESSPOSITION','Sales process action positions',-312494525,4,'SalesProcess.actionPositions');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'DEPARTMENT','organization departments',-311051853,1,'Contacts.Tab.departments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'OFFICE','Company offices',-309982931,1,'Contact.Tab.offices');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTSUPPLIER','Product supplier',-302568837,3,'Product.Tab.Supplier');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYINFO','Company info',-301159743,1,'Contact.Tab.companyInfo');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTACT','Contacts: Person & Organization',1,1,'subModule.contacts');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BANKACCOUNT','bank accounts',2,1,'Contacts.Tab.bankAccounts');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTACTPERSON','Contact persons',3,1,'Contacts.Tab.contactPersons');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'COMMUNICATION','Communications',4,1,'Contacts.Tab.communications');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'EMPLOYEE','Employees',5,1,'Contact.Tab.employees');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'LANGUAGE','Language Catalog',6,1,'Language.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BANK','Bank catalog',7,1,'Bank.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CITY','City catalog',8,1,'City.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COSTCENTERS','Cost center catalog',9,1,'CostCenter.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COUNTRY','Country catalog',10,1,'Country.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CURRENCY','Currency catalog',11,1,'Currency.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PERSONTYPE','Person type catalog',12,1,'PersonType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALUTATION','Salutation catalog',13,1,'Salutation.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPLIERTYPE','Supplier type catalog',14,1,'SupplierType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TITLE','Title catalog',15,1,'Title.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ADDRESSSOURCE','Addess source catalog',16,1,'AddressSource.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BRANCH','Branch catalog',17,1,'Branch.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORY','Category catalog',18,1,'Category.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYVALUE','Category values catalog',19,1,'CategoryValue.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CUSTOMERTYPE','Customer types catalog',20,1,'CustomerType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PAYCONDITION','Payment condition catalog',21,11,'PayCondition.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PAYMORALITY','Payment morality catalog',22,1,'PayMorality.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRIORITY','Priority catalog',23,1,'Priority.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTACTMEDIA','Contact media catalog',24,1,'ContactMedia.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'EDITOR','Editor catalog',25,1,'Editor.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TELECOMTYPE','Telecom type catalog',26,1,'TelecomType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TEMPLATE','Template catalog',27,1,'Template.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACHIEVEMENT','Achievements catalog',28,3,'Achievement.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTTYPE','Product type catalog',29,3,'ProductType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTGROUP','Product group catalog',30,3,'ProductGroup.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTUNIT','Product unit',31,3,'ProductUnit.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'VAT','Vat catalog',32,11,'Vat.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'VATRATE','Vat rate catalog',33,11,'VatRate.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'STATUS','Status catalog',34,4,'SalesProcessStatus.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACTIONTYPE','Action type catalog',35,4,'ActionType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESSPRIORITY','Sales process priority catalog',36,4,'Priority.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGN','Campaign',40,2,'Campaign');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNCRITERION','Campaign Criterion',41,2,'Campaign.Tab.selection');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNCONTACTS','Campaign Contacts',42,2,'Campaign.Tab.recipients');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNTEMPLATE','Campaign Templates',43,2,'Campaign.Tab.templates');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (16,'SALESPROCESSEVALUATION','sales process evaluation graphics',44,4,'SalesProcess.Title.evaluation');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (25,'MAIL','Webmail basic functions',45,5,'Webmail.common.mail');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILGROUP','Webmail contact group',46,5,'Webmail.contactGroup.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILFILTER','Webmail filter',47,5,'Webmail.filter.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILFOLDER','Webmail folder',48,5,'Webmail.folder.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILSIGNATURE','Webmail signatures',49,5,'Webmail.signature.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASK','Scheduler task',50,6,'Scheduler.Task');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASKUSER','Scheduler user appointment',51,6,'Scheduler.Task.users');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'USERGROUP','Admin usergroup',52,-308758540,'Admin.User.UserGroup');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENT','Scheduler appointment',53,6,'Scheduler.Appointment');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENTPARTICIPANT','Scheduler user appointment',54,6,'Appointment.participants');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENTTYPE','Scheduler appointment type',55,6,'Task.appointmentType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SCHEDULERPRIORITY','Scheduler priority',56,6,'Task.priority');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASKTYPE','Scheduler task type',57,6,'Task.taskType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'HOLIDAY','Scheduler holiday',58,6,'holiday.title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'GRANTACCESS','Scheduler grant access',59,6,'Scheduler.grantAccess');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'PREFERENCE','Scheduler preferences',60,6,'Scheduler.configuration');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'USERINTERFACE','User interface manager',61,7,'UIManager.userConfigurable');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYINTERFACE','Company interface manager',62,7,'UIManager.companyConfigurable');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYLOGO','Company logo',63,7,'UIManager.LogoCompany');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTPRIORITY','Support priority',64,8,'Article.priority');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTCATEGORY','Support category',65,8,'Article.category');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASETYPE','Support case type',66,8,'CaseType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASESEVERITY','Support case severity',67,8,'CaseSeverity.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WORKLEVEL','Support work level',68,8,'SupportCase.workLevel');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'STATE','Support state',69,8,'SupportCase.states');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTUSER','Support user',70,8,'SupportUser.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ARTICLE','Support article',71,8,'Article.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'ARTICLECOMMENT','Support article comment',72,8,'Article.comments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'ARTICLERELATED','Support article related',73,8,'Article.Tab.ArticleRelateds');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTATTACH','Support support attach',74,8,'Article.Tab.Attachments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ARTICLELINK','Support article link',75,8,'Article.links');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (1,'ARTICLEHISTORY','Support article history',76,8,'Article.histories');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'QUESTION','Support question',77,8,'Question.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASE','Support case',78,8,'SupportCase.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'CASEACTIVITY','Support case activity',79,8,'SupportCaseActivity.title.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'ARTICLERATING','Support article rating',80,8,'Article.rating.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (1,'USERSESSION','User session',81,-308758540,'User.userSession');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'REPORT','Report view create update delete',83,9,'Report.common.reports');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COLUMN','Report Column tab view, update',84,9,'Report.tab.columns');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'FILTER','Report filter tab view, create,update, delete',85,9,'Report.tab.filters');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'GROUP','Report group tab view,update',86,9,'Report.Tab.grouping');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'TOTALIZE','Report totalize tab view,update',87,9,'Report.Tab.totalize');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'CHART','Report chart tab view,update',88,9,'Report.ChartType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNTYPE','Campaign type catalog',89,2,'Catalogs.CampaignType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'CAMPAIGNACTIVITY','Campaign Activity',90,2,'Campaign.Tab.Activity');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNATTACH','Campaign Attach',92,2,'Campaign.Tab.Attach');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACTIVITYUSER','Campaign Activity User',93,2,'Campaign.ActivityUser');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'USERSETTINGS','User settings view, update accessright',94,10,'Home.userSettings');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'DASHBOARD','Dashboard view, update accessright',95,10,'Home.dashborad');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'REPORTROLE','Reports roles',96,9,'Report.Roles');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (16,'CONTACTIMPORT','Contact data import',97,1,'contact.dataImport');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYTAB','Category tab accessright',98,1,'Catalogs.CategoryTab');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYGROUP','Category group accessright',99,1,'Catalogs.CategoryGroup');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTRACTTYPE','Contract type catalog',100,4,'ContractType.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACCOUNT','Account catalog',101,11,'Account.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICETEMPLATE','Invoice template catalog',102,11,'InvoiceTemplate.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'REMINDERLEVEL','Reminder level catalog',103,11,'ReminderLevel.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SEQUENCERULE','Sequence rule catalog',104,11,'SequenceRule.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICE','Invoice access right',105,11,'Invoice.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEPOSITION','Invoice position access right',106,11,'InvoicePosition.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEREMINDER','Invoice reminder access right',107,11,'InvoiceReminder.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEPAYMENT','Invoice payment access right',108,11,'InvoicePayment.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALE','Sale Access right',109,4,'Sale.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALEPOSITION','Sale Position Access right',110,4,'SalePosition.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTCONTRACT','ProductContract Access right',111,4,'ProductContract.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECT','Project',112,12,'Project.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTTIME','Project time',113,12,'ProjectTime.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTACTIVITY','Project activity',114,12,'ProjectActivity.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTUSER','Project assignee',115,12,'ProjectAssignee.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTSUBPROJECT','Project sub project',116,12,'ProjectSubProject.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INCOMINGINVOICE','Incoming invoice access rights',117,11,'Finance.incomingInvoice.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INCOMINGPAYMENT','Incoming invoice payments access rights',118,11,'Finance.incomingPayment.functionality');

insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'USER','user',-279728775,-308758540,'Admin.User.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COMPANY','company',-279568150,-308758540,'Admin.Company.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ROLE','role',-279295572,-308758540,'Admin.Role.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'ACCESSRIGHT','access right',-273493572,-308758540,'Admin.AccessRights.Title');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values(5,'APPLICATIONSIGNATURE','Application email signatures view and update accessright',119,-308758540,'ApplicationSignature.accessRight.title');

/* Company module (Only administration)*/
insert into companymodule (companyid, moduleid, maintablelimit, active) VALUES (1,-308758540,NULL,1);

/* Role */
insert into role (companyid, isdefault, descriptionid, roleid, rolename, version) VALUES (1,1,NULL,1,'System root',1);

/* User role */

insert into userrole (userid, roleid) VALUES (1,1);

/* Access rights (Only admin access rights)*/

/* User */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279728775, -308758540, 15, 1, 1, 1);
/* Company */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279568150, -308758540, 15, 1, 1, 1);
/* Role */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279295572, -308758540, 15, 1, 1, 1);
/* Access rights */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-273493572, -308758540, 4, 1, 1, 1);
/* Application signatures */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (119, -308758540, 5, 1, 1, 1);

/* Campaign criterion constants */


insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (1,1,'campaign.customerType','customertypeid',6,'customertypename','customertype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (2,1,'campaign.expectedTurn','expectedturnover',2,'0','customer');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (3,1,'campaign.numberEmployees','numberofemployees',1,'0','customer');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (4,1,'campaign.priority','priorityid',6,'priorityname','priority');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (5,1,'campaign.addressSource','sourceid',6,'sourcename','addresssource');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (6,2,'campaign.personType','persontypeid',6,'persontypename','persontype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (7,4,'campaign.city','zip',1,'0','city');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (8,4,'campaign.country','countryid',6,'countryname','country');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (10,4,'campaign.language','languageid',6,'languagename','language');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (11,4,'campaign.recorddate','recorddate',3,'0','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (12,2,'ContactPerson.department','departmentname',4,'departmentname','department');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (13,4,'Contact.city','cityname',4,'cityname','city');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (14,1,'Customer.branch','branchid',6,'branchname','branch');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (15,4,'campaign.addressKeywords','keywords',4,'0','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (16,2,'campaign.contactPersonFunction','function',4,'function','contactperson');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (17,1,'campaign.salesPerson','employeeid',6,'name1_name2_name3','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (18,1,'campaign.salesPartner','partnerid',6,'name1_name2_name3','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (19,3,'campaign.productType','typeid',6,'typename','producttype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (20,3,'campaign.productGroup','groupid',6,'groupname','productgroup');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (21,3,'campaign.productVersion','currentversion',4,'0','product');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (22,4,'Contact.type','code',1,'code','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (23,3,'product.inUse','inuse',1,'inuse','saleposition');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (24,2,'contactPerson.recorddate','recorddate_cp',3,'0','contactperson');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (25,2,'campaign.createdBy','recorduserid',6,'name1_name2_name3','elwisuser');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (26,3,'Campaign.product','productid',6,'productname','product');

/*  Already used sequence update */

insert into sequence (`name`, sequencenumber) values ('address',3);
insert into sequence (`name`, sequencenumber) values ('elwisuser',2);
insert into sequence (`name`, sequencenumber) values ('role',2);

SET FOREIGN_KEY_CHECKS=1;