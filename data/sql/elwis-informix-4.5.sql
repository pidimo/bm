/*
miky:20120719
insert new criterion value 'sale position contact person'
add new field 'relationfield' in  campcriterionvalue
*/
ALTER TABLE campcriterionvalue modify field nvarchar(30) NOT NULL;
ALTER TABLE campcriterionvalue ADD relationfield nvarchar(30);

INSERT INTO campcriterionvalue(campcriterionvalueid, tableid, descriptionkey, field, fieldtype, fieldname, tablename, relationfield)
VALUES(27, 3 , "CampaignCriteria.salePosition.contactPerson", "positionContactPersonId", 7, "0", "saleposition", "contactPersonId");

/* miky: Update version constants */
update systemconstant set value='4.8' where name='APP_VERSION';
update systemconstant set value='4.5' where name='DB_VERSION';

UPDATE STATISTICS HIGH;
