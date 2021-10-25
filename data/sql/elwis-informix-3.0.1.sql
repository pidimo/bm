/*Ivan:20081013*/
/*remove isdefault column from sequencerule table*/
alter table sequencerule drop isdefault;

/*Add company options for sequencerule invoice and credit note*/
alter table company add invoiceruleid integer before language;
alter table company add creditnoteruleid integer before finishlicense;

alter table company add constraint foreign key (invoiceruleid) references sequencerule (numberid);
alter table company add constraint foreign key (creditnoteruleid) references sequencerule (numberid);


/* Update version constants, PUT other sqls above */
update systemconstant set value='3.0' where name='APP_VERSION';
update systemconstant set value='3.0.1' where name='DB_VERSION';

UPDATE STATISTICS HIGH;