/*
miky:20130830
dynamic search functionality
*/
Create Table dynamicsearch (
companyid Int NOT NULL,
dynamicsearchid Int NOT NULL,
module Varchar(50) NOT NULL,
name Varchar(50) NOT NULL,
userid Int NOT NULL,
Primary Key (dynamicsearchid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table dynamicsearch add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table dynamicsearch add Foreign Key (userid) references elwisuser (userid) on delete  restrict on update  restrict;

Create Table dysearchfield (
alias Varchar(50) NOT NULL,
companyid Int NOT NULL,
dynamicsearchid Int NOT NULL,
dysearchfieldid Int NOT NULL,
position Int NOT NULL,
Primary Key (dysearchfieldid)) ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

Alter Table dysearchfield add Foreign Key (companyid) references company (companyid) on delete  restrict on update  restrict;
Alter Table dysearchfield add Foreign Key (dynamicsearchid) references dynamicsearch (dynamicsearchid) on delete  restrict on update  restrict;


