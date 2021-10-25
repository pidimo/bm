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
alter table mailaccount add keepemailserver tinyint after email;
alter table mailaccount add createincontact tinyint DEFAULT 0 NOT NULL after companyid;
alter table mailaccount add createoutcontact tinyint DEFAULT 0 NOT NULL after createincontact;
alter table mailaccount add automaticforward tinyint after accounttype;
alter table mailaccount add forwardemail varchar(250) after email;
alter table mailaccount add automaticreply tinyint after automaticforward;
alter table mailaccount add replysubject varchar(250) after prefix;
alter table mailaccount add replymessage varchar(250) after prefix;

UPDATE mailaccount SET keepemailserver = (SELECT keepemailserver FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET createincontact = (SELECT createincontact FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET createoutcontact = (SELECT createoutcontact FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET automaticforward = (SELECT automaticforward FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET forwardemail = (SELECT forwardemail FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET automaticreply = (SELECT automaticreply FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET replysubject = (SELECT replysubject FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);
UPDATE mailaccount SET replymessage = (SELECT replymessage FROM usermail WHERE usermail.usermailid = mailaccount.usermailid);

ALTER TABLE usermail DROP COLUMN keepemailserver;
ALTER TABLE usermail DROP COLUMN createincontact;
ALTER TABLE usermail DROP COLUMN createoutcontact;
ALTER TABLE usermail DROP COLUMN automaticforward;
ALTER TABLE usermail DROP COLUMN forwardemail;
ALTER TABLE usermail DROP COLUMN automaticreply;
ALTER TABLE usermail DROP COLUMN replysubject;
ALTER TABLE usermail DROP COLUMN replymessage;

/*add new fields for delete pop email in x days*/
alter table mailaccount add removeafterof Int after prefix;
alter table mailuidltrack add deletefrompopat bigint after companyid;

/*
miky:20140825
add sale position categories
*/
Alter Table categfieldvalue add salepositionid Int after productid;
Alter table categfieldvalue add Foreign Key (salepositionid) references saleposition (salepositionid) on delete  restrict on update  restrict;


/*
miky:20140905
app versions
*/
update systemconstant set value='5.3' where name='APP_VERSION';
update systemconstant set value='5.2' where name='DB_VERSION';
