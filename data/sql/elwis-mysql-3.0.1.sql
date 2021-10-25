/*Ivan:20081013*/
/*remove isdefault column from sequencerule table*/
alter table sequencerule drop column isdefault;

/*Add company options for sequencerule invoice and credit note*/
alter table company add column invoiceruleid int(11) after invoicedayssend;
alter table company add column creditnoteruleid int(11) after copytemplate;

alter table company add Foreign Key (`invoiceruleid`) references sequencerule (`numberid`) on delete  restrict on update  restrict;
alter table company add Foreign Key (`creditnoteruleid`) references sequencerule (`numberid`) on delete  restrict on update  restrict;

/* Update version constants, PUT other sqls above */
update systemconstant set value='3.0' where name='APP_VERSION';
update systemconstant set value='3.0.1' where name='DB_VERSION';