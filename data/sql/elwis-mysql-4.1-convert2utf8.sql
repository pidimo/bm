/*
Convert data tables into UTF-8
*/
/* Fernando:20090421
Converting datatables into UTF8 character set
*/

ALTER TABLE accessrights CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE account CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE action CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE actionposition CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE actiontype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE address CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE addressgroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE addresssource CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE appointment CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE appointmenttype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE article CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlecategory CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlecomment CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlehistory CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlelink CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlequestion CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlerating CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE articlerelated CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE attach CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE attachment CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bank CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bankaccount CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE body CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE branch CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campactivcontact CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campactivity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campactivityuser CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campaign CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campaigntext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campattach CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campcontact CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campcriterion CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campcriterionvalue CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campgenattach CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campgeneration CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE campgentext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE camptemplate CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE camptype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE caseactivity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE caseseverity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE casetype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE caseworklevel CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE categfieldvalue CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE category CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE categorygroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE categoryrelation CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE categorytab CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE categoryvalue CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE city CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE columngroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE columntotalize CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE company CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE companymodule CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE competitorproduct CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE condition CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE contact CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE contactattach CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE contactmedia CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE contactperson CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE contracttype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE costcenter CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE country CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE currency CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE customer CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE customertype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE dashbadmcomp CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE dashbcompcolumn CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE dashbcomponent CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE dashbcontainer CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE dashbfilter CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE department CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE editor CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE elwisuser CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE employee CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE favorite CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE filter CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE filtervalue CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE folder CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE freetext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE functiondependency CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE holiday CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE incominginvoice CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE incomingpayment CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoice CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicefreenum CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicepayment CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoiceposition CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicereminder CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicetemplate CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicetext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE invoicevat CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE langtext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE language CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE mail CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE mailaccount CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE mailcontact CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE mailgroupaddr CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE mailrecipient CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE office CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE paycondition CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE payconditiontext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE paymentstep CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE paymorality CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE persontype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE pricing CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE priority CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE processpriority CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE processstatus CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE prodsupplier CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE product CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE productcontract CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE productgroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE productpicture CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE producttext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE producttype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE productunit CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE project CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE projectactivity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE projectassignee CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE projecttime CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE recent CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE recurexception CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE recurrence CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE relationtype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reminder CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reminderlevel CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE remindertext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE report CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reportchart CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reportcolumn CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reportfilter CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reportrole CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reporttotalize CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE role CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE sale CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE saleposition CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE salesprocess CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE salutation CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE scheduleduser CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE scheduleraccess CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE sequence CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE sequencerule CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE signature CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE state CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE style CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE styleattribute CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE stylesheet CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE subproject CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE supplier CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE suppliertype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE supportattach CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE supportcase CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE supportcontact CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE supportuser CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE systemconstant CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE systemfunction CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE systemmodule CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE task CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE tasktype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE telecom CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE telecomtype CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE template CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE templatetext CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE title CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE usergroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE usermail CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE userofgroup CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE userrole CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE usersession CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE usersessionparam CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
SET FOREIGN_KEY_CHECKS=1;
ALTER TABLE usersessionlog CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE usertask CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE vat CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE vatrate CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
Convert reportfilter.body binary text from latin1 to utf8
*/
alter table body add column bodylatin1 longtext CHARACTER SET latin1 COLLATE latin1_general_ci;
update body set bodylatin1=body;
alter table body add column bodyutf8 longtext CHARACTER SET utf8 COLLATE utf8_general_ci;
update body set bodyutf8=bodylatin1;
update body set body=bodyutf8;
alter table body drop column bodylatin1;
alter table body drop column bodyutf8;

/*
Convert reportfilter.path binary text from latin1 to utf8
*/
alter table reportcolumn add column textlatin1 longtext CHARACTER SET latin1 COLLATE latin1_general_ci;
update reportcolumn set textlatin1=path;
alter table reportcolumn add column textutf8 longtext CHARACTER SET utf8 COLLATE utf8_general_ci;
update reportcolumn set textutf8=textlatin1;
update reportcolumn set path=textutf8;
alter table reportcolumn drop column textlatin1;
alter table reportcolumn drop column textutf8;

/*
Convert reportfiler.path binary text from latin1 to utf8
*/

alter table reportfilter add column textlatin1 longtext CHARACTER SET latin1 COLLATE latin1_general_ci;
update reportfilter set textlatin1=path;
alter table reportfilter add column textutf8 longtext CHARACTER SET utf8 COLLATE utf8_general_ci;
update reportfilter set textutf8=textlatin1;
update reportfilter set path=textutf8;
alter table reportfilter drop column textlatin1;
alter table reportfilter drop column textutf8;

/*
Updating Freetext
*/

alter table freetext add column textlatin1 longtext CHARACTER SET latin1 COLLATE latin1_general_ci;
update freetext set textlatin1=freetextvalue;
alter table freetext add column textutf8 longtext CHARACTER SET utf8 COLLATE utf8_general_ci;
update freetext set textutf8=textlatin1;

/*
Just update the TEXT data in freetext, not the binary
*/

update freetext set freetextvalue=textutf8 where freetextid not in (select imageid from address where imageid is not null) and freetexttype=1;
update freetext set freetextvalue=textutf8 where freetextid in (select freetextid from contact where type in (0,1,4)) and freetexttype=2;
update freetext set freetextvalue=textutf8 where freetextid in (select routepageid from company) and freetexttype=3;
update freetext set freetextvalue=textutf8 where freetextid in (select freetextid from campaigntext where templateid in (select templateid from camptemplate where documenttype=1)) and freetexttype=4;
update freetext set freetextvalue=textutf8 where freetexttype=6;
update freetext set freetextvalue=textutf8 where freetextid in (select freetextid from producttext) and freetexttype=7;
update freetext set freetextvalue=textutf8 where freetextid in (select descriptionid from product) and freetexttype=7;
update freetext set freetextvalue=textutf8 where freetextid not in (select freetextid from campattach) and freetexttype=8;
update freetext set freetextvalue=textutf8 where freetexttype=9;
update freetext set freetextvalue=textutf8 where freetexttype=10;
update freetext set freetextvalue=textutf8 where freetexttype=11;
update freetext set freetextvalue=textutf8 where freetextid not in (select freetextid from supportattach) and freetexttype=12;
update freetext set freetextvalue=textutf8 where freetexttype=13;
update freetext set freetextvalue=textutf8 where freetexttype=14;
update freetext set freetextvalue=textutf8 where freetexttype=15;
update freetext set freetextvalue=textutf8 where freetextid  in (select categfieldvalue.freetextid from categfieldvalue) and freetexttype=16;
update freetext set freetextvalue=textutf8 where freetextid in (select freetextid from campgentext where generationid in (select generationid from campgeneration where templateid in (select templateid from camptemplate where documenttype=1))) and freetexttype=17;
update freetext set freetextvalue=textutf8 where freetexttype=19;
update freetext set freetextvalue=textutf8 where freetextid in (select notesid from invoice) and freetexttype=20;
update freetext set freetextvalue=textutf8 where freetexttype=21;
update freetext set freetextvalue=textutf8 where freetexttype=22;
update freetext set freetextvalue=textutf8 where freetextid in (select descriptionid from invoicereminder) and freetexttype=23;
update freetext set freetextvalue=textutf8 where freetexttype=24;
update freetext set freetextvalue=textutf8 where freetexttype=25;
update freetext set freetextvalue=textutf8 where freetexttype=26;
update freetext set freetextvalue=textutf8 where freetexttype=27;
alter table freetext drop column textlatin1;
alter table freetext drop column textutf8;


