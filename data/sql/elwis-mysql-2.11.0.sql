/*Ivan:20080213*/
/*add mail account table*/
Create table mailaccount (
	accounttype Int,
	companyid Int NOT NULL,
	defaultaccount Smallint,
	email Varchar(250),
	login Varchar(50),
	mailaccountid Int NOT NULL,
	password Varchar(50),
	servername Varchar(50),
	serverport Varchar(50),
	usermailid Int NOT NULL,
	usesslconection Smallint,
 Primary Key (mailaccountid)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table signature add column mailaccountid Int null after isdefault;

Alter table mailaccount add Foreign Key (usermailid) references usermail (usermailid);
Alter table mailaccount add Foreign Key (companyid) references company (companyid);
Alter table signature add Foreign Key (mailaccountid) references mailaccount (mailaccountid);

Alter Table mail add column signatureid Int null after size;
Alter Table mail add Foreign Key (signatureid) references signature (signatureid);

Alter Table mail add column mailaccount Varchar(250) null after inout;
UPDATE mail as m
SET mailaccount = (SELECT um.email FROM usermail AS um join folder
AS f on m.folderid = f.folderid WHERE um.usermailid=f.usermailid);


Alter Table signature drop column message;
insert into sequence (name, sequencenumber) values ('mailaccount', 0);

/** Fer: 20080226 Adding reports-role relation table and permission for role **/
Create table reportrole (
    companyid int NOT NULL,
    reportid int NOT NULL,
    roleid int NOT NULL,
Primary Key (reportid, roleid)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table reportrole add Foreign Key (companyid) references company (companyid);
Alter table reportrole add Foreign Key (reportid) references report (reportid);
Alter table reportrole add Foreign Key (roleid) references role (roleid);

insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
        values(11,'REPORTROLE','Reports roles',96,9,'Report.Roles');


/*Mauren: 20080226 add product filter in campaignCriterion*/
INSERT INTO campcriterionvalue VALUES(26, 3 , "Campaign.product", "productid", 6, "productname", "product");


/*Updating application and database versions */
update systemconstant set value='2.13.0' where name='APP_VERSION';
update systemconstant set value='2.11.0' where name='DB_VERSION';
