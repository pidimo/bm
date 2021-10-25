/*Miky:20071213*/
/*modify column*/
ALTER TABLE usersessionparam MODIFY paramname varchar(100) NOT NULL default '';

/*Miky:20071214*/
/*Fix cityname column of city table defined how report filter with IS operator, replace for EQUALS operator*/
/*Execute update firts to filtervalue table after to reportfilter table*/

UPDATE filtervalue
SET value = (SELECT c.cityname FROM city AS c, reportfilter AS rf
WHERE rf.reportfilterid = filtervalue.reportfilterid
AND rf.tableref = 'city' AND rf.columnref = 'cityname' AND rf.operator = 300
AND c.cityid = filtervalue.value)
WHERE EXISTS
  (SELECT c.cityname FROM city AS c, reportfilter AS rf
WHERE rf.reportfilterid = filtervalue.reportfilterid
AND rf.tableref = 'city' AND rf.columnref = 'cityname' AND rf.operator = 300
AND c.cityid = filtervalue.value);

UPDATE reportfilter
SET filtertype = 0, operator = 1
WHERE tableref = 'city' AND columnref = 'cityname' AND operator = 300;




/*Updating application and database versions */
update systemconstant set value='2.12.14' where name='APP_VERSION';
update systemconstant set value='2.10.10' where name='DB_VERSION';
