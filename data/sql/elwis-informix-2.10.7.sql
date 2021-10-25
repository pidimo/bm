/* Ivan:20070112 */
/* home access rights security */
insert into systemmodule (description,moduleid,modulenamekey,modulepath)
values('Home module', '10', 'Common.home','/' );
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(5,'USERSETTINGS','User settings view, update accessright',94,10,'Home.userSettings');
insert into systemfunction(allowpermission,code,description,functionid,moduleid,namekey)
values(5,'DASHBOARD','Dashboard view, update accessright',95,10,'Home.dashborad');

/* Mauren:20070116 */
/* Campaign criterion updating */

INSERT INTO campcriterionvalue VALUES(23, 3 , "product.inUse", "inuse", 1, "inuse", "prodcustomer");

update systemfunction set allowpermission =15  where functionid=70;

UPDATE STATISTICS HIGH;

/* Fernando Montaño:20070410 */
/* Sequence name correction for campaign attach */
update sequence set name ='campattach'  where name='Elwis.CampaignAttach';

