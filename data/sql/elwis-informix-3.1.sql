/*Ivan:20081021*/
/*Add sales module related tables; sale, contract, paymentstep, saleposition tables */
Create Table productcontract (
addressid Integer NOT NULL,
amountype Smallint,
companyid Integer NOT NULL,
contractenddate Integer,
contractid Integer NOT NULL,
contracttypeid Integer NOT NULL,
currencyid Integer NOT NULL,
discount Decimal(10,2),
installment Integer,
invoiceduntil Integer,
invoiceremainon Smallint,
openamount Decimal(10,2) NOT NULL,
orderdate Integer,
payconditionid Integer,
paymethod Smallint NOT NULL,
payperiod Smallint,
paystartdate Integer,
price Decimal(10,2) NOT NULL,
productid Integer NOT NULL,
salepositionid Integer,
sellerid Integer,
vatid Integer NOT NULL,
version Integer Default 0 NOT NULL,
Primary Key (contractid)
) LOCK MODE ROW;

Create Table sale (
companyid Integer NOT NULL,
contactpersonid Integer,
customerid Integer NOT NULL,
freetextid Integer,
processid Integer,
saledate Integer NOT NULL,
saleid Integer NOT NULL,
sellerid Integer NOT NULL,
title nVarchar(200,0) NOT NULL,
version Integer NOT NULL,
Primary Key (saleid)
) LOCK MODE ROW;

Create Table paymentstep (
companyid Integer NOT NULL,
contractid Integer NOT NULL,
payamount Decimal(10,2) NOT NULL,
paydate Integer NOT NULL,
paystepid Integer NOT NULL,
Primary Key (paystepid)
) LOCK MODE ROW;

Create Table saleposition (
active Smallint NOT NULL,
companyid Integer NOT NULL,
contactpersonid Integer,
customerid Integer NOT NULL,
deliverydate Integer,
freetextid Integer,
invoicetoid Integer NOT NULL,
productid Integer NOT NULL,
quantity Integer NOT NULL,
salepositionid Integer NOT NULL,
saleid Integer,
serial nVarchar(100,0),
totalprice Decimal(10,2) NOT NULL,
unitid Integer,
unitprice Decimal(10,2) NOT NULL,
version Integer Default 0 NOT NULL,
versionnumber nVarchar(20,0),
Primary Key (salepositionid)
) LOCK MODE ROW;

Alter table invoiceposition add Constraint Foreign Key(contractid) references productcontract (contractid);
Alter table invoiceposition add Constraint Foreign Key(paystepid) references paymentstep (paystepid);

Alter table productcontract add Constraint Foreign Key(currencyid) references currency (currencyid);
Alter table productcontract add Constraint Foreign Key(vatid) references vat (vatid);
Alter table productcontract add Constraint Foreign Key(productid) references product (productid);
Alter table productcontract add Constraint Foreign Key(contracttypeid) references contracttype (contracttypeid);
Alter table productcontract add Constraint Foreign Key(addressid) references address (addressid);
Alter table productcontract add Constraint Foreign Key(companyid) references company (companyid);
Alter table productcontract add Constraint Foreign Key(salepositionid) references saleposition (salepositionid);
Alter table productcontract add Constraint Foreign Key(sellerid) references employee (employeeid);
Alter table productcontract add Constraint Foreign Key(payconditionid) references paycondition (payconditionid);

Alter table sale add Constraint Foreign Key(customerid) references customer (customerid);
Alter table sale add Constraint Foreign Key(customerid,contactpersonid) references contactperson (addressid,contactpersonid);
Alter table sale add Constraint Foreign Key(companyid) references company (companyid);
Alter table sale add Constraint Foreign Key(processid) references salesprocess (processid);
Alter table sale add Constraint Foreign Key(freetextid) references freetext (freetextid);
Alter table sale add Constraint Foreign Key(sellerid) references employee (employeeid);

Alter table saleposition add Constraint Foreign Key(customerid) references customer (customerid);
Alter table saleposition add Constraint Foreign Key(freetextid) references freetext (freetextid);
Alter table saleposition add Constraint Foreign Key(productid) references product (productid);
Alter table saleposition add Constraint Foreign Key(saleid) references sale (saleid);
Alter table saleposition add Constraint Foreign Key(invoicetoid) references address (addressid);
Alter table saleposition add Constraint Foreign Key(companyid) references company (companyid);
Alter table saleposition add Constraint Foreign Key(customerid,contactpersonid) references contactperson (addressid,contactpersonid);
Alter table saleposition add Constraint Foreign Key(unitid) references productunit (unitid);

Alter table paymentstep add Constraint Foreign Key(companyid) references company (companyid);
Alter table paymentstep add Constraint Foreign Key(contractid) references productcontract (contractid);

/*Add saleposition and productcontract in sequence table*/
insert into sequence (name, sequencenumber) values('saleposition',0);
insert into sequence (name, sequencenumber) values('productcontract',0);

/*Correction sequencerule name in sequence table*/
insert into sequence (name, sequencenumber) values('sequencerule',0);
update sequence set sequencenumber=(select max(sequencerule.numberid)  from sequencerule)  where name='sequencerule';
delete from sequence where name='Elwis.SequenceRule';

/*add discount column in invoiceposition table*/
alter table invoiceposition add discount Decimal(10,2) before freetextid;

/*add label in sequencerule table*/
alter table sequencerule add label nVarchar(150,0) before lastnumber;
update sequencerule set label=format;

/*miky:20081031*/
/*remove rtf format in created reports*/
UPDATE report SET reportformat = 'pdf'  WHERE reportformat = 'rtf';

Alter table invoice add  ruleformat nVarchar(200,0) before type;
Alter table invoice add  rulenumber Integer before type;
insert into sequence (name, sequencenumber) values('invoicefreenum',0);

/*Ivan:20081210*/
/*Change not null columns in paymentstep table*/
alter table paymentstep modify paydate Integer default null;

/*miky:20081210*/
/*modify invoice vatid column to allow null*/
ALTER TABLE invoice MODIFY vatid Integer;
ALTER TABLE invoice add Constraint Foreign Key (vatid) references vat (vatid);

/*Ivan:20081210*/
/*Access Right for sale, saleposition and productcontract*/
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'SALE', 'Sale Access right', 109, 4, 'Sale.accessRight');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'SALEPOSITION', 'Sale Position Access right', 110, 4, 'SalePosition.accessRight');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey) values (15, 'PRODUCTCONTRACT', 'ProductContract Access right', 111, 4, 'ProductContract.accessRight');

/*miky:20081211*/
/*add unit field in invoice position and update from productunit*/
Alter Table invoiceposition add unit nvarchar(40,0) before unitprice;

UPDATE invoiceposition SET unit = (SELECT pu.unitname FROM product as p, productunit as pu
WHERE invoiceposition.productid =p.productid AND p.unitid = pu.unitid)
WHERE EXISTS (SELECT pu.unitname  FROM product as p, productunit as pu
WHERE invoiceposition.productid =p.productid AND p.unitid = pu.unitid);

/*Ivan:20081211*/
/*change invoicefreenum and productcontract structures */
Create table invoicefreenum(
companyid Integer NOT NULL,
freenumberid Integer NOT NULL,
invoicedate Integer,
number Integer NOT NULL,
ruleformat nVarchar(200,0) NOT NULL,
Primary Key (freenumberid)
) LOCK MODE ROW;

Alter table invoicefreenum add Constraint Foreign Key (companyid) references company (companyid);
alter table invoice add constraint  unique  (number, companyid);

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

UPDATE STATISTICS HIGH;