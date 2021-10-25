/*miky:20090327*/
Alter Table appointment add createdbyid Int default 0 NOT NULL after contactpersonid;
update appointment set createdbyid=userid;
Alter table appointment add Foreign Key (createdbyid) references elwisuser (userid) on delete  restrict on update  restrict;

/*miky:20090403*/
Alter table folder add column columntoshow smallint after companyid;
UPDATE folder SET columntoshow = 1 WHERE type = 0;
UPDATE folder SET columntoshow = 1 WHERE type = 1;
UPDATE folder SET columntoshow = 2 WHERE type = 2;
UPDATE folder SET columntoshow = 2 WHERE type = 3;
UPDATE folder SET columntoshow = 1 WHERE type = 4;
UPDATE folder SET columntoshow = 2 WHERE type = 5;

/*miky:20090409*/
Alter Table category add secondgroupid Int after parentcategory;
Alter table category add Foreign Key (secondgroupid) references categorygroup (categorygroupid) on delete  restrict on update  restrict;

/*
Ivan:20090421
Add saveemail column in mail table
*/
alter table mail add saveemail Tinyint after priority;
/* add recipaddress this table allow know mailrecipients address or contactpersons selected from ui*/
create table recipaddress(
addressid Integer,
contactpersonid Integer,
companyid Integer NOT NULL,
recipaddressid Integer NOT NULL,
recipientid Integer NOT NULL,
Primary Key (recipaddressid))ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table recipaddress add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table recipaddress add Foreign Key (recipientid) references mailrecipient (recipientid) on delete  restrict on update  restrict;

/*
Ivan:20090422
Add createcontact column in mail table
*/
alter table mail add createcontact Tinyint after companyid;

/*
Ivan:20090424
add table for account errors when upload or download emails
*/
create table mailaccerror(
companyid Integer NOT NULL,
errortype smallint NOT NULL,
mailaccerrorid Integer NOT NULL,
mailaccountid Integer,
timeerror bigint NOT NULL,
usermailid Integer NOT NULL,
Primary Key (mailaccerrorid))ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table mailaccerror add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table mailaccerror add Foreign Key (mailaccountid) references mailaccount (mailaccountid) on delete  restrict on update  restrict;
Alter table mailaccerror add Foreign Key (usermailid) references usermail (usermailid) on delete  restrict on update  restrict;

/*
Ivan:20090501
Modify project table columns plannedinvoice and plannednoinvoice to support null values
*/
alter table project modify plannedinvoice Decimal(6,1) NULL;
alter table project modify plannednoinvoice Decimal(6,1) NULL;

/*
Ivan:20090504
 add column lastdownloadtime in mailaccount table
 add column automaticforward in usermail table
 add column automaticreply in usermail table
 add column replymessage in usermail table
 add column showpopmsgs in usermail table
*/
alter table mailaccount add lastdownloadtime bigint after email;
alter table usermail add automaticforward tinyint default 0 first;
alter table usermail add automaticreply tinyint default 0 after automaticforward;
alter table usermail add replymessage varchar(250) after emptytrashlogout;
alter table usermail add replysubject varchar(100) after replymode;
alter table usermail add showpopmsgs tinyint default 0 after savesenditem;
/*
Ivan:20090505
add column keepemailserver in usermail
add column forwardemail in usermail
add column messageidheader in mail table
add column attachment in mail table
*/
alter table usermail add forwardemail varchar(250) after emptytrashlogout;
alter table usermail add keepemailserver tinyint default 0 after forwardemail;
alter table mail add messageidheader varchar(250) after mailpersonalfrom;
alter table mail add attachment tinyint first;

/*
Ivan:20090507
add column newemail in mail table
add column completeemail in mail table
*/
alter table mail add newemail tinyint default 0 after messageidheader;
alter table mail add completeemail tinyint default 1 after companyid;

/*
Ivan:20090508
add table emailsource to store emails downloated
*/
create table emailsource(
companyid Integer NOT NULL,
filesize Integer NOT NULL,
mailid Integer NOT NULL,
source longblob NOT NULL,
Primary Key (mailid))ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table emailsource add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table emailsource add Foreign Key (mailid) references mail (mailid) on delete  restrict on update  restrict;

/*miky:20090508*/
Alter table template add column mediatype smallint DEFAULT 0 NOT NULL after editorid;

/*Ivan:20090512*/
insert into sequence (name, sequencenumber) values ('mailaccerror', 0);
insert into sequence (name, sequencenumber) values ('recipaddress', 0);

/*Alvaro:20090513*/
Alter table folder add isopen Tinyint not null default 0  after folderid;
Alter table folder add parentid Integer after name;
Alter table folder add Foreign Key (parentid) references folder (folderid) on delete  restrict on update  restrict;

/* Fernando: 20090518 removing non existent tables from sequence */
delete from sequence where name='achievement';
delete from sequence where name='campbudget';
delete from sequence where name='componentview';
delete from sequence where name='contract';
delete from sequence where name='prodcustomer';

/* Fernando: 20090518 Updating the sequence values cause the new sequence generator implamentation */
UPDATE sequence set sequencenumber=(select max(accountid) + 10 from account) where name='account';
UPDATE sequence set sequencenumber=(select max(positionid) + 10 from actionposition) where name='actionposition';
UPDATE sequence set sequencenumber=(select max(actiontypeid) + 10 from actiontype) where name='actiontype';
UPDATE sequence set sequencenumber=(select max(addressid) + 10 from address) where name='address';
UPDATE sequence set sequencenumber=(select max(addressgroupid) + 10 from addressgroup) where name='addressgroup';
UPDATE sequence set sequencenumber=(select max(sourceid) + 10 from addresssource) where name='addresssource';
UPDATE sequence set sequencenumber=(select max(appointmentid) + 10 from appointment) where name='appointment';
UPDATE sequence set sequencenumber=(select max(apptypeid) + 10 from appointmenttype) where name='appointmenttype';
UPDATE sequence set sequencenumber=(select max(articleid) + 10 from article) where name='article';
UPDATE sequence set sequencenumber=(select max(categoryid) + 10 from articlecategory) where name='articlecategory';
UPDATE sequence set sequencenumber=(select max(commentid) + 10 from articlecomment) where name='articlecomment';
UPDATE sequence set sequencenumber=(select max(historyid) + 10 from articlehistory) where name='articlehistory';
UPDATE sequence set sequencenumber=(select max(linkid) + 10 from articlelink) where name='articlelink';
UPDATE sequence set sequencenumber=(select max(questionid) + 10 from articlequestion) where name='articlequestion';
UPDATE sequence set sequencenumber=(select max(attachid) + 10 from attach) where name='attach';
UPDATE sequence set sequencenumber=(select max(attachmentid) + 10 from attachment) where name='attachment';
UPDATE sequence set sequencenumber=(select max(bankid) + 10 from bank) where name='bank';
UPDATE sequence set sequencenumber=(select max(bankaccountid) + 10 from bankaccount) where name='bankaccount';
UPDATE sequence set sequencenumber=(select max(bodyid) + 10 from body) where name='body';
UPDATE sequence set sequencenumber=(select max(branchid) + 10 from branch) where name='branch';
UPDATE sequence set sequencenumber=(select max(activityid) + 10 from campactivity) where name='campactivity';
UPDATE sequence set sequencenumber=(select max(campaignid) + 10 from campaign) where name='campaign';
UPDATE sequence set sequencenumber=(select max(attachid) + 10 from campattach) where name='campattach';
UPDATE sequence set sequencenumber=(select max(campaignid) + 10 from campcontact) where name='campcontact';
UPDATE sequence set sequencenumber=(select max(criterionid) + 10 from campcriterion) where name='campcriterion';
UPDATE sequence set sequencenumber=(select max(generationid) + 10 from campgeneration) where name='campgeneration';
UPDATE sequence set sequencenumber=(select max(campgentextid) + 10 from campgentext) where name='campgentext';
UPDATE sequence set sequencenumber=(select max(templateid) + 10 from camptemplate) where name='camptemplate';
UPDATE sequence set sequencenumber=(select max(camptypeid) + 10 from camptype) where name='camptype';
UPDATE sequence set sequencenumber=(select max(activityid) + 10 from caseactivity) where name='caseactivity';
UPDATE sequence set sequencenumber=(select max(severityid) + 10 from caseseverity) where name='caseseverity';
UPDATE sequence set sequencenumber=(select max(casetypeid) + 10 from casetype) where name='casetype';
UPDATE sequence set sequencenumber=(select max(worklevelid) + 10 from caseworklevel) where name='caseworklevel';
UPDATE sequence set sequencenumber=(select max(fieldvalueid) + 10 from categfieldvalue) where name='categfieldvalue';
UPDATE sequence set sequencenumber=(select max(categoryid) + 10 from category) where name='category';
UPDATE sequence set sequencenumber=(select max(categorygroupid) + 10 from categorygroup) where name='categorygroup';
UPDATE sequence set sequencenumber=(select max(categorytabid) + 10 from categorytab) where name='categorytab';
UPDATE sequence set sequencenumber=(select max(categoryvalueid) + 10 from categoryvalue) where name='categoryvalue';
UPDATE sequence set sequencenumber=(select max(cityid) + 10 from city) where name='city';
UPDATE sequence set sequencenumber=(select max(columngroupid) + 10 from columngroup) where name='columngroup';
UPDATE sequence set sequencenumber=(select max(compproductid) + 10 from competitorproduct) where name='competitorproduct';
UPDATE sequence set sequencenumber=(select max(conditionid) + 10 from condition) where name='condition';
UPDATE sequence set sequencenumber=(select max(contactid) + 10 from contact) where name='contact';
UPDATE sequence set sequencenumber=(select max(contactmediaid) + 10 from contactmedia) where name='contactmedia';
UPDATE sequence set sequencenumber=(select max(contracttypeid) + 10 from contracttype) where name='contracttype';
UPDATE sequence set sequencenumber=(select max(costcenterid) + 10 from costcenter) where name='costcenter';
UPDATE sequence set sequencenumber=(select max(countryid) + 10 from country) where name='country';
UPDATE sequence set sequencenumber=(select max(currencyid) + 10 from currency) where name='currency';
UPDATE sequence set sequencenumber=(select max(customertypeid) + 10 from customertype) where name='customertype';
UPDATE sequence set sequencenumber=(select max(compcolumnid) + 10 from dashbcompcolumn) where name='dashbcompcolumn';
UPDATE sequence set sequencenumber=(select max(componentid) + 10 from dashbcomponent) where name='dashbcomponent';
UPDATE sequence set sequencenumber=(select max(dashbcontainerid) + 10 from dashbcontainer) where name='dashbcontainer';
UPDATE sequence set sequencenumber=(select max(filterid) + 10 from dashbfilter) where name='dashbfilter';
UPDATE sequence set sequencenumber=(select max(departmentid) + 10 from department) where name='department';
UPDATE sequence set sequencenumber=(select max(editorid) + 10 from editor) where name='editor';
UPDATE sequence set sequencenumber=(select max(userid) + 10 from elwisuser) where name='elwisuser';
UPDATE sequence set sequencenumber=(select max(filterid) + 10 from filter) where name='filter';
UPDATE sequence set sequencenumber=(select max(filtervalueid) + 10 from filtervalue) where name='filtervalue';
UPDATE sequence set sequencenumber=(select max(folderid) + 10 from folder) where name='folder';
UPDATE sequence set sequencenumber=(select max(freetextid) + 10 from freetext) where name='freetext';
UPDATE sequence set sequencenumber=(select max(holidayid) + 10 from holiday) where name='holiday';
UPDATE sequence set sequencenumber=(select max(ininvoiceid) + 10 from incominginvoice) where name='incominginvoice';
UPDATE sequence set sequencenumber=(select max(paymentid) + 10 from incomingpayment) where name='incomingPayment';
UPDATE sequence set sequencenumber=(select max(invoiceid) + 10 from invoice) where name='invoice';
UPDATE sequence set sequencenumber=(select max(freenumberid) + 10 from invoicefreenum) where name='invoicefreenum';
UPDATE sequence set sequencenumber=(select max(paymentid) + 10 from invoicepayment) where name='invoicepayment';
UPDATE sequence set sequencenumber=(select max(positionid) + 10 from invoiceposition) where name='invoiceposition';
UPDATE sequence set sequencenumber=(select max(reminderid) + 10 from invoicereminder) where name='invoicereminder';
UPDATE sequence set sequencenumber=(select max(templateid) + 10 from invoicetemplate) where name='invoicetemplate';
UPDATE sequence set sequencenumber=(select max(langtextid) + 10 from langtext) where name='langtext';
UPDATE sequence set sequencenumber=(select max(languageid) + 10 from language) where name='language';
UPDATE sequence set sequencenumber=(select max(mailid) + 10 from mail) where name='mail';
UPDATE sequence set sequencenumber=(select max(mailaccerrorid) + 10 from mailaccerror) where name='mailaccerror';
UPDATE sequence set sequencenumber=(select max(mailaccountid) + 10 from mailaccount) where name='mailaccount';
UPDATE sequence set sequencenumber=(select max(mailcontactid) + 10 from mailcontact) where name='mailcontact';
UPDATE sequence set sequencenumber=(select max(groupaddrid) + 10 from mailgroupaddr) where name='mailgroupaddr';
UPDATE sequence set sequencenumber=(select max(recipientid) + 10 from mailrecipient) where name='mailrecipient';
UPDATE sequence set sequencenumber=(select max(officeid) + 10 from office) where name='office';
UPDATE sequence set sequencenumber=(select max(payconditionid) + 10 from paycondition) where name='paycondition';
UPDATE sequence set sequencenumber=(select max(paystepid) + 10 from paymentstep) where name='paymentstep';
UPDATE sequence set sequencenumber=(select max(paymoralityid) + 10 from paymorality) where name='paymorality';
UPDATE sequence set sequencenumber=(select max(persontypeid) + 10 from persontype) where name='persontype';
UPDATE sequence set sequencenumber=(select max(priorityid) + 10 from priority) where name='priority';
UPDATE sequence set sequencenumber=(select max(priorityid) + 10 from processpriority) where name='processpriority';
UPDATE sequence set sequencenumber=(select max(statusid) + 10 from processstatus) where name='processstatus';
UPDATE sequence set sequencenumber=(select max(productid) + 10 from prodsupplier) where name='prodsupplier';
UPDATE sequence set sequencenumber=(select max(productid) + 10 from product) where name='product';
UPDATE sequence set sequencenumber=(select max(contractid) + 10 from productcontract) where name='productcontract';
UPDATE sequence set sequencenumber=(select max(groupid) + 10 from productgroup) where name='productgroup';
UPDATE sequence set sequencenumber=(select max(typeid) + 10 from producttype) where name='producttype';
UPDATE sequence set sequencenumber=(select max(unitid) + 10 from productunit) where name='productunit';
UPDATE sequence set sequencenumber=(select max(projectid) + 10 from project) where name='project';
UPDATE sequence set sequencenumber=(select max(projectactivityid) + 10 from projectactivity) where name='projectactivity';
UPDATE sequence set sequencenumber=(select max(timeid) + 10 from projecttime) where name='projecttime';
UPDATE sequence set sequencenumber=(select max(recipaddressid) + 10 from recipaddress) where name='recipaddress';
UPDATE sequence set sequencenumber=(select max(recurexceptionid) + 10 from recurexception) where name='recurexception';
UPDATE sequence set sequencenumber=(select max(appointmentid) + 10 from recurrence) where name='recurrence';
UPDATE sequence set sequencenumber=(select max(appointmentid) + 10 from reminder) where name='reminder';
UPDATE sequence set sequencenumber=(select max(reminderlevelid) + 10 from reminderlevel) where name='reminderlevel';
UPDATE sequence set sequencenumber=(select max(reportid) + 10 from report) where name='report';
UPDATE sequence set sequencenumber=(select max(reportchartid) + 10 from reportchart) where name='reportchart';
UPDATE sequence set sequencenumber=(select max(reportcolumnid) + 10 from reportcolumn) where name='reportcolumn';
UPDATE sequence set sequencenumber=(select max(reportfilterid) + 10 from reportfilter) where name='reportfilter';
UPDATE sequence set sequencenumber=(select max(reporttotalizeid) + 10 from reporttotalize) where name='reporttotalize';
UPDATE sequence set sequencenumber=(select max(roleid) + 10 from role) where name='role';
UPDATE sequence set sequencenumber=(select max(saleid) + 10 from sale) where name='sale';
UPDATE sequence set sequencenumber=(select max(salepositionid) + 10 from saleposition) where name='saleposition';
UPDATE sequence set sequencenumber=(select max(processid) + 10 from salesprocess) where name='salesprocess';
UPDATE sequence set sequencenumber=(select max(salutationid) + 10 from salutation) where name='salutation';
UPDATE sequence set sequencenumber=(select max(scheduleduserid) + 10 from scheduleduser) where name='scheduleduser';
UPDATE sequence set sequencenumber=(select max(numberid) + 10 from sequencerule) where name='sequencerule';
UPDATE sequence set sequencenumber=(select max(signatureid) + 10 from signature) where name='signature';
UPDATE sequence set sequencenumber=(select max(stateid) + 10 from state) where name='state';
UPDATE sequence set sequencenumber=(select max(styleid) + 10 from style) where name='style';
UPDATE sequence set sequencenumber=(select max(attributeid) + 10 from styleattribute) where name='styleattribute';
UPDATE sequence set sequencenumber=(select max(stylesheetid) + 10 from stylesheet) where name='stylesheet';
UPDATE sequence set sequencenumber=(select max(subprojectid) + 10 from subproject) where name='subproject';
UPDATE sequence set sequencenumber=(select max(suppliertypeid) + 10 from suppliertype) where name='suppliertype';
UPDATE sequence set sequencenumber=(select max(attachid) + 10 from supportattach) where name='supportattach';
UPDATE sequence set sequencenumber=(select max(caseid) + 10 from supportcase) where name='supportcase';
UPDATE sequence set sequencenumber=(select max(taskid) + 10 from task) where name='task';
UPDATE sequence set sequencenumber=(select max(tasktypeid) + 10 from tasktype) where name='tasktype';
UPDATE sequence set sequencenumber=(select max(telecomid) + 10 from telecom) where name='telecom';
UPDATE sequence set sequencenumber=(select max(telecomtypeid) + 10 from telecomtype) where name='telecomtype';
UPDATE sequence set sequencenumber=(select max(templateid) + 10 from template) where name='template';
UPDATE sequence set sequencenumber=(select max(titleid) + 10 from title) where name='title';
UPDATE sequence set sequencenumber=(select max(usergroupid) + 10 from usergroup) where name='usergroup';
UPDATE sequence set sequencenumber=(select max(vatid) + 10 from vat) where name='vat';
UPDATE sequence set sequencenumber=(select max(vatrateid) + 10 from vatrate) where name='vatrate';

/*Alvaro 20090521*/
Alter table productcontract add noteid Integer after netgross;
Alter table productcontract add Foreign Key (noteid) references freetext (freetextid) on delete  restrict on update  restrict;

/*Alvaro 20090522*/
update sequence set name="incomingpayment" where name="incomingPayment";

/*Ivan:20090523 modify default values*/
alter table usermail modify automaticforward tinyint default NULL;
alter table usermail modify automaticreply tinyint default NULL;
alter table usermail modify showpopmsgs tinyint default NULL;
alter table usermail modify keepemailserver tinyint default NULL;
alter table mail modify newemail tinyint default NULL;
alter table mail modify completeemail tinyint default NULL;

update usermail set showpopmsgs = 1;

/*Alvaro: 20090525*/
Alter table folder add column totalsize bigint after parentid;
Alter table folder add column mailsnumber Integer after isopen;

/*miky:20090526*/
create table temporalimage(
companyid Integer NOT NULL,
filedata longblob NOT NULL,
filename varchar(255) NOT NULL,
sessionid varchar(60) NOT NULL,
temporalimageid Integer NOT NULL,
Primary Key (temporalimageid))ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter table temporalimage add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;

/*miky:20090529*/
Alter Table company add salutationid Int after rowsperpage;
Alter table company add Foreign Key (salutationid) references salutation (salutationid) on delete  restrict on update  restrict;

/*miky:20090529, delete filters related to body email*/
delete from condition where namekey = 4;
delete from filter where filterid NOT IN (select filterid from condition);

/* Fer:20090529 Update version constants */
update systemconstant set value='4.1' where name='APP_VERSION';
update systemconstant set value='4.1' where name='DB_VERSION';