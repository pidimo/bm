/*Mauren:20071030*/
/* add customernumber field in supplier table*/
alter table supplier add customernumber varchar(20) after companyid;

/*Mauren:20071031*/
/*Delete report relations for budget report*/
delete from reportchart where reportid
IN (select reportid from report where initialtableref ='campbudget');

delete from reporttotalize where reportid
IN (select reportid from report where initialtableref ='campbudget')  ;

delete from columngroup where reportcolumnid
IN (select reportcolumnid from reportcolumn where reportid
    IN (select reportid from report where initialtableref ='campbudget'));

delete from columntotalize where reportcolumnid
IN (select reportcolumnid from reportcolumn where reportid
    IN (select reportid from report where initialtableref ='campbudget'));

delete from filtervalue where reportfilterid
IN (select reportfilterid from reportfilter where reportid
    IN (select reportid from report where initialtableref ='campbudget'));

delete from reportcolumn where reportid
IN (select reportid from report where initialtableref ='campbudget');

delete from reportfilter where reportid
IN (select reportid from report where initialtableref ='campbudget');

delete from report where initialtableref ='campbudget';

/*Delete permissions*/  
delete from accessrights where functionid =91;
delete from systemfunction where functionid =91;

/*Delete budget table of the campaign module*/
drop table campbudget;

/*Updating application and database versions */
update systemconstant set value='2.12.13' where name='APP_VERSION';
update systemconstant set value='2.10.9' where name='DB_VERSION';
