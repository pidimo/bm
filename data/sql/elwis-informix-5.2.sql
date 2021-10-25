/*
miky:20140808
Update main additional address
*/
UPDATE addaddress SET countryid = (SELECT countryid FROM address WHERE address.addressid = addaddress.addressid)
WHERE addaddresstype = 1;

UPDATE addaddress SET cityid = (SELECT cityid FROM address WHERE address.addressid = addaddress.addressid)
WHERE addaddresstype = 1;

UPDATE addaddress SET street = (SELECT street FROM address WHERE address.addressid = addaddress.addressid)
WHERE addaddresstype = 1;

UPDATE addaddress SET housenumber = (SELECT housenumber FROM address WHERE address.addressid = addaddress.addressid)
WHERE addaddresstype = 1;

UPDATE addaddress SET addaddressline = (SELECT addaddressline FROM address WHERE address.addressid = addaddress.addressid)
WHERE addaddresstype = 1;


/*
miky:20140814
move usermail fields in mailaccount
*/
alter table mailaccount add keepemailserver Smallint before lastdownloadtime;
alter table mailaccount add createincontact Smallint DEFAULT 0 NOT NULL before defaultaccount;
alter table mailaccount add createoutcontact Smallint DEFAULT 0 NOT NULL before defaultaccount;
alter table mailaccount add automaticforward Smallint before companyid;
alter table mailaccount add forwardemail NVarchar(250) before keepemailserver;
alter table mailaccount add automaticreply Smallint before companyid;
alter table mailaccount add replysubject NVarchar(250) before servername;
alter table mailaccount add replymessage NVarchar(250) before replysubject;

UPDATE mailaccount SET keepemailserver = (SELECT keepemailserver FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET createincontact = (SELECT createincontact FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET createoutcontact = (SELECT createoutcontact FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET automaticforward = (SELECT automaticforward FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET forwardemail = (SELECT forwardemail FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET automaticreply = (SELECT automaticreply FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET replysubject = (SELECT replysubject FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET replymessage = (SELECT replymessage FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);

ALTER TABLE usermail DROP keepemailserver;
ALTER TABLE usermail DROP createincontact;
ALTER TABLE usermail DROP createoutcontact;
ALTER TABLE usermail DROP automaticforward;
ALTER TABLE usermail DROP forwardemail;
ALTER TABLE usermail DROP automaticreply;
ALTER TABLE usermail DROP replysubject;
ALTER TABLE usermail DROP replymessage;

/*add new fields for delete pop email in x days*/
alter table mailaccount add removeafterof Integer before replymessage;
alter table mailuidltrack add deletefrompopat Int8 before mailaccountid;

/*
miky:20140825
add sale position categories
*/
Alter Table categfieldvalue add salepositionid Integer  before stringvalue;
Alter Table categfieldvalue add Constraint Foreign Key (salepositionid) references saleposition (salepositionid);


/*
miky:20140905
app versions
*/
update systemconstant set value='5.3' where name='APP_VERSION';
update systemconstant set value='5.2' where name='DB_VERSION';

