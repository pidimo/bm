/*Ivan:20080213*/
/*add mail account table*/
Create Table mailaccount (
    accounttype Integer,
    companyid Integer NOT NULL,
    defaultaccount Smallint,
	email nvarchar(250,0),
	login nvarchar(50,0),
	mailaccountid Integer NOT NULL,
	password nvarchar(50,0),
	servername nvarchar(50,0),
	serverport nvarchar(50,0),
	usermailid Integer NOT NULL,
	usesslconection Smallint,
Primary Key (mailaccountid)
);


Alter Table signature add mailaccountid integer before message;

Alter Table mailaccount add Constraint Foreign Key (usermailid) references usermail (usermailid);
Alter Table mailaccount add Constraint Foreign Key (companyid) references company (companyid);
Alter Table signature add Constraint Foreign Key (mailaccountid) references mailaccount (mailaccountid);

Alter Table mail add signatureid integer before sentdatetime;
Alter Table mail add Constraint Foreign Key (signatureid) references signature (signatureid);

Alter Table mail add mailaccount nvarchar(250,0) before mailid;
UPDATE mail SET mailaccount  = (SELECT um.email FROM usermail AS um join folder AS f on mail.folderid = f.folderid WHERE um.usermailid=f.usermailid);

Alter Table signature drop message;
insert into sequence (name, sequencenumber) values ('mailaccount', 0);
/** Fer: 20080226 Adding reports-role relation table and permission for role **/
Create Table reportrole (
    companyid Integer NOT NULL,
    reportid Integer NOT NULL,
    roleid Integer NOT NULL,
Primary Key (reportid, roleid)
);
Alter Table reportrole add Constraint Foreign Key (companyid) references company (companyid);
Alter Table reportrole add Constraint Foreign Key (reportid) references report (reportid);
Alter Table reportrole add Constraint Foreign Key (roleid) references role (roleid);

insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
        values(11,'REPORTROLE','Reports roles',96,9,'Report.Roles');


/*Mauren: 20080226 add product filter in campaignCriterion*/
INSERT INTO campcriterionvalue VALUES(26, 3 , "Campaign.product", "productid", 6, "productname", "product");

update systemconstant set value='2.13.0' where name='APP_VERSION';
update systemconstant set value='2.11.0' where name='DB_VERSION';

UPDATE STATISTICS HIGH;