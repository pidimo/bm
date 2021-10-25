/*
miky:20130830
dynamic search functionality
*/
Create Table dynamicsearch (
companyid Integer NOT NULL,
dynamicsearchid Integer NOT NULL,
module NVarchar(50) NOT NULL,
name NVarchar(50) NOT NULL,
userid Integer NOT NULL,
Primary Key (dynamicsearchid)) LOCK MODE ROW;

Alter Table dynamicsearch add Constraint Foreign Key (companyid) references company (companyid);
Alter Table dynamicsearch add Constraint Foreign Key (userid) references elwisuser (userid);

Create Table dysearchfield (
alias NVarchar(50) NOT NULL,
companyid Integer NOT NULL,
dynamicsearchid Integer NOT NULL,
dysearchfieldid Integer NOT NULL,
position Integer NOT NULL,
Primary Key (dysearchfieldid)) LOCK MODE ROW;

Alter Table dysearchfield add Constraint Foreign Key (companyid) references company (companyid);
Alter Table dysearchfield add Constraint Foreign Key (dynamicsearchid) references dynamicsearch (dynamicsearchid);

