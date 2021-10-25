/*Ivan:20081021*/
/*Add sales module related tables; sale, contract, paymentstep, saleposition tables */
Create table productcontract (
	`addressid` Int NOT NULL,
	`amountype` Tinyint,
	`companyid` Int NOT NULL,
	`contractenddate` Int,
	`contractid` Int NOT NULL,
	`contracttypeid` Int NOT NULL,
	`currencyid` Int NOT NULL,
	`discount` Decimal(10,2),
	`installment` Int,
	`invoiceduntil` Int,
	`invoiceremainon` Smallint,
	`openamount` Decimal(10,2) NOT NULL,
	`orderdate` Int,
	`payconditionid` Int,
	`paymethod` Smallint NOT NULL,
	`payperiod` Tinyint,
	`paystartdate` Int,
	`price` Decimal(10,2) NOT NULL,
	`productid` Int NOT NULL,
	`salepositionid` Int,
	`sellerid` Int,
	`vatid` Int NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
 Primary Key (`contractid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table sale (
	`companyid` Int NOT NULL,
	`contactpersonid` Int,
	`customerid` Int NOT NULL,
	`freetextid` Int,
	`processid` Int,
	`saledate` Int NOT NULL,
	`saleid` Int NOT NULL,
	`sellerid` Int NOT NULL,
	`title` Varchar(200) NOT NULL,
	`version` Int NOT NULL,
 Primary Key (`saleid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table paymentstep (
	`companyid` Int NOT NULL,
	`contractid` Int NOT NULL,
	`payamount` Decimal(10,2) NOT NULL,
	`paydate` Int NOT NULL,
	`paystepid` Int NOT NULL,
 Primary Key (`paystepid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

 Create table saleposition (
	`active` Tinyint NOT NULL,
	`companyid` Int NOT NULL,
	`contactpersonid` Int,
	`customerid` Int NOT NULL,
	`deliverydate` Int,
	`freetextid` Int,
	`invoicetoid` Int NOT NULL,
	`productid` Int NOT NULL,
	`quantity` Int NOT NULL,
	`salepositionid` Int NOT NULL,
	`saleid` Int,
	`serial` Varchar(100),
	`totalprice` Decimal(10,2) NOT NULL,
	`unitid` Int,
	`unitprice` Decimal(10,2) NOT NULL,
	`version` Int NOT NULL DEFAULT 0,
	`versionnumber` Varchar(20),
 Primary Key (`salepositionid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;


Alter table invoiceposition add Foreign Key (contractid) references productcontract (contractid) on delete  restrict on update  restrict;
Alter table invoiceposition add Foreign Key (paystepid) references paymentstep (paystepid) on delete  restrict on update  restrict;

Alter table productcontract add Foreign Key (currencyid) references currency (currencyid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (vatid) references vat (vatid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (productid) references product (productid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (contracttypeid) references contracttype (contracttypeid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (addressid) references address (addressid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (salepositionid) references saleposition (salepositionid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (sellerid) references employee (employeeid) on delete  restrict on update  restrict;
Alter table productcontract add Foreign Key (payconditionid) references paycondition (payconditionid) on delete  restrict on update  restrict;

Alter table sale add Foreign Key (customerid) references customer (customerid) on delete  restrict on update  restrict;
Alter table sale add Foreign Key (customerid,contactpersonid) references contactperson (addressid,contactpersonid) on delete  restrict on update  restrict;
Alter table sale add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table sale add Foreign Key (processid) references salesprocess (processid) on delete  restrict on update  restrict;
Alter table sale add Foreign Key (freetextid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter table sale add Foreign Key (sellerid) references employee (employeeid) on delete  restrict on update  restrict;

Alter table saleposition add Foreign Key (customerid) references customer (customerid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (freetextid) references freetext (freetextid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (productid) references product (productid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (saleid) references sale (saleid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (invoicetoid) references address (addressid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (customerid,contactpersonid) references contactperson (addressid,contactpersonid) on delete  restrict on update  restrict;
Alter table saleposition add Foreign Key (unitid) references productunit (unitid) on delete  restrict on update  restrict;

Alter table paymentstep add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter table paymentstep add Foreign Key (contractid) references productcontract (contractid) on delete  restrict on update  restrict;

/*Add saleposition and productcontract in sequence table*/
insert into sequence (name, sequencenumber) values('saleposition',0);
insert into sequence (name, sequencenumber) values('productcontract',0);

/*Correction sequencerule name in sequence table*/
update sequence set name='sequencerule' where name='Elwis.SequenceRule';

/*add discount column in saleposition table*/
alter table invoiceposition add discount Decimal(10,2) after freetextid;

/*add label in sequencerule table*/
alter table sequencerule add label varchar(150) after format;
update sequencerule set label=format; 

/*miky:20081031*/
/*remove rtf format in created reports*/
UPDATE report SET reportformat = 'pdf'  WHERE reportformat = 'rtf';

Alter table invoice add column ruleformat varchar(200) after reminderdate;
Alter table invoice add column rulenumber Int after ruleformat;
insert into sequence (name, sequencenumber) values('invoicefreenum',0);

/*Ivan:20081210*/
/*Change not null columns in paymentstep table*/
alter table paymentstep modify paydate Int default null;

/*miky:20081210*/
/*modify invoice vatid column to allow null*/
ALTER TABLE invoice MODIFY vatid Int;

/*Ivan:20081210*/
/*Access Right for sale, saleposition and productcontract*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'SALE', 'Sale Access right', 109, 4, 'Sale.accessRight');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'SALEPOSITION', 'Sale Position Access right', 110, 4, 'SalePosition.accessRight');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'PRODUCTCONTRACT', 'ProductContract Access right', 111, 4, 'ProductContract.accessRight');

/*miky:20081211*/
/*add unit field in invoice position and update from productunit*/
Alter Table invoiceposition add unit Varchar(40) after totalprice;

UPDATE invoiceposition SET unit = (SELECT pu.unitname FROM product as p, productunit as pu
WHERE invoiceposition.productid =p.productid AND p.unitid = pu.unitid)
WHERE EXISTS (SELECT pu.unitname  FROM product as p, productunit as pu
WHERE invoiceposition.productid =p.productid AND p.unitid = pu.unitid);

/*Ivan:20081211*/
/*change invoicefreenum and productcontract structures */
Create table invoicefreenum(
    `companyid` Int NOT NULL,
    `freenumberid` Int NOT NULL,
    `invoicedate` Int,
    `number` Int NOT NULL,
    `ruleformat` varchar(200) NOT NULL,
 Primary Key (`freenumberid`)) ENGINE = InnoDB
 CHARACTER SET latin1 COLLATE latin1_general_ci
 ROW_FORMAT = Default;

Alter table invoicefreenum add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
alter table invoice add unique(number, companyid);

/*miky:20081212*/
/*modify quantity in invoice position*/
ALTER TABLE invoiceposition MODIFY quantity Decimal(10,2) NOT NULL;

/*Ivan:20081211*/
/*delete contract accessright*/
delete from accessrights where moduleid=3 and functionid=-532285243;
delete from systemfunction where moduleid=3 and functionid=-532285243;
delete from accessrights where moduleid=3 and functionid=-302605603;
delete from systemfunction where moduleid=3 and functionid=-302605603;

/* Update version constants, PUT other sqls above */
update systemconstant set value='3.1' where name='APP_VERSION';
update systemconstant set value='3.1' where name='DB_VERSION';