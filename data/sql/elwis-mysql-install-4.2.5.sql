/*
ELWIS database data initialization script.
@author: Fernando Montaño
Installation script based in the elwis database version 4.2.5
This script runs for BM 4.4.1

This is for new installation only.

This creates the database, structure a basic data for the super-company:
User: fer
Pwd: .jatun.
Company:jatun
*/

/** Creating database */
CREATE DATABASE elwis CHARACTER SET utf8 COLLATE utf8_general_ci;

use elwis;
/** Creating structure **/


SET FOREIGN_KEY_CHECKS=0;
SET NAMES utf8;
/*Table structure for table `accessrights` */

CREATE TABLE `accessrights` (
  `functionid` int(11) NOT NULL DEFAULT '0',
  `moduleid` int(11) NOT NULL DEFAULT '0',
  `permission` tinyint(4) DEFAULT NULL,
  `roleid` int(11) NOT NULL DEFAULT '0',
  `active` tinyint(1) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`functionid`,`roleid`),
  KEY `IX_Relationship18` (`roleid`),
  KEY `IX_Relationship19` (`functionid`),
  KEY `companymodule` (`companyid`,`moduleid`),
  CONSTRAINT `accessrights_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`),
  CONSTRAINT `accessrights_ibfk_2` FOREIGN KEY (`functionid`) REFERENCES `systemfunction` (`functionid`),
  CONSTRAINT `accessrights_ibfk_3` FOREIGN KEY (`companyid`, `moduleid`) REFERENCES `companymodule` (`companyid`, `moduleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `account` */

CREATE TABLE `account` (
  `accountid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(150) NOT NULL DEFAULT '',
  `number` varchar(150) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `action` */

CREATE TABLE `action` (
  `actiontypeid` int(11) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `createdatetime` bigint(20) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `currencyid` int(11) DEFAULT NULL,
  `followupdate` int(11) DEFAULT NULL,
  `netgross` smallint(6) DEFAULT NULL,
  `number` varchar(40) DEFAULT NULL,
  `processid` int(11) NOT NULL DEFAULT '0',
  `updatedatetime` bigint(20) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `value` decimal(13,2) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contactid`,`processid`),
  KEY `salesprocess` (`processid`),
  KEY `actiontype` (`actiontypeid`),
  KEY `contact` (`contactid`),
  KEY `company` (`companyid`),
  KEY `currencyid` (`currencyid`),
  KEY `userid` (`userid`),
  CONSTRAINT `action_ibfk_1` FOREIGN KEY (`processid`) REFERENCES `salesprocess` (`processid`),
  CONSTRAINT `action_ibfk_2` FOREIGN KEY (`actiontypeid`) REFERENCES `actiontype` (`actiontypeid`),
  CONSTRAINT `action_ibfk_3` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`),
  CONSTRAINT `action_ibfk_5` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `action_ibfk_6` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`),
  CONSTRAINT `action_ibfk_7` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `actionposition` */

CREATE TABLE `actionposition` (
  `amount` decimal(10,2) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `discount` decimal(13,2) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `positionid` int(11) NOT NULL DEFAULT '0',
  `price` decimal(13,2) DEFAULT NULL,
  `productid` int(11) DEFAULT NULL,
  `processid` int(11) NOT NULL DEFAULT '0',
  `totalprice` decimal(13,2) DEFAULT NULL,
  `totalpricegross` decimal(10,2) DEFAULT NULL,
  `unitid` int(11) DEFAULT NULL,
  `unitpricegross` decimal(10,2) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`positionid`),
  KEY `freetext` (`descriptionid`),
  KEY `actionref` (`contactid`,`processid`),
  KEY `product` (`productid`),
  KEY `company` (`companyid`),
  KEY `unitid` (`unitid`),
  CONSTRAINT `actionposition_ibfk_1` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `actionposition_ibfk_2` FOREIGN KEY (`contactid`, `processid`) REFERENCES `action` (`contactid`, `processid`),
  CONSTRAINT `actionposition_ibfk_3` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `actionposition_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `actionposition_ibfk_5` FOREIGN KEY (`unitid`) REFERENCES `productunit` (`unitid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `actiontype` */

CREATE TABLE `actiontype` (
  `actiontypeid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `probability` smallint(6) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `typename` varchar(80) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`actiontypeid`),
  KEY `company` (`companyid`),
  CONSTRAINT `actiontype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `actiontypeseq` */

CREATE TABLE `actiontypeseq` (
  `actiontypeid` int(11) NOT NULL,
  `companyid` int(11) NOT NULL,
  `numberid` int(11) NOT NULL,
  PRIMARY KEY (`actiontypeid`,`numberid`),
  KEY `numberid` (`numberid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `actiontypeseq_ibfk_1` FOREIGN KEY (`actiontypeid`) REFERENCES `actiontype` (`actiontypeid`),
  CONSTRAINT `actiontypeseq_ibfk_2` FOREIGN KEY (`numberid`) REFERENCES `sequencerule` (`numberid`),
  CONSTRAINT `actiontypeseq_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `address` */

CREATE TABLE `address` (
  `active` tinyint(1) DEFAULT NULL,
  `addressid` int(11) NOT NULL DEFAULT '0',
  `addresstype` char(1) NOT NULL DEFAULT '',
  `bankaccountid` int(11) DEFAULT NULL,
  `birthday` int(11) DEFAULT NULL,
  `cityid` int(11) DEFAULT NULL,
  `code` tinyint(4) DEFAULT NULL,
  `companyid` int(11) DEFAULT NULL,
  `countryid` int(11) DEFAULT NULL,
  `education` varchar(40) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `housenumber` varchar(10) DEFAULT NULL,
  `imageid` int(11) DEFAULT NULL,
  `keywords` varchar(50) DEFAULT NULL,
  `languageid` int(11) DEFAULT NULL,
  `lastmoddate` int(11) DEFAULT NULL,
  `lastmoduser` int(11) DEFAULT NULL,
  `name1` varchar(40) DEFAULT NULL,
  `name2` varchar(40) DEFAULT NULL,
  `name3` varchar(40) DEFAULT NULL,
  `personal` tinyint(1) DEFAULT NULL,
  `pobox` varchar(8) DEFAULT NULL,
  `recorddate` int(11) DEFAULT NULL,
  `recorduser` int(11) DEFAULT NULL,
  `salutationid` int(11) DEFAULT NULL,
  `searchname` varchar(60) DEFAULT NULL,
  `street` varchar(40) DEFAULT NULL,
  `taxnumber` varchar(20) DEFAULT NULL,
  `titleid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `waydescriptionid` int(11) DEFAULT NULL,
  `zipofpobox` varchar(8) DEFAULT NULL,
  `alte_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`addressid`),
  KEY `cityid` (`cityid`),
  KEY `bankaccountid` (`bankaccountid`),
  KEY `companyid` (`companyid`),
  KEY `countryid` (`countryid`),
  KEY `freetextid` (`freetextid`),
  KEY `languageid` (`languageid`),
  KEY `salutationid` (`salutationid`),
  KEY `titleid` (`titleid`),
  KEY `waydescriptionid` (`waydescriptionid`),
  KEY `lastmoduser` (`lastmoduser`),
  KEY `recorduser` (`recorduser`),
  KEY `imageid` (`imageid`),
  CONSTRAINT `0_2677` FOREIGN KEY (`waydescriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `0_2678` FOREIGN KEY (`bankaccountid`) REFERENCES `bankaccount` (`bankaccountid`),
  CONSTRAINT `0_2679` FOREIGN KEY (`cityid`) REFERENCES `city` (`cityid`),
  CONSTRAINT `0_2680` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2681` FOREIGN KEY (`countryid`) REFERENCES `country` (`countryid`),
  CONSTRAINT `0_2682` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `0_2683` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `0_2684` FOREIGN KEY (`lastmoduser`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `0_2685` FOREIGN KEY (`recorduser`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `0_2686` FOREIGN KEY (`salutationid`) REFERENCES `salutation` (`salutationid`),
  CONSTRAINT `0_2687` FOREIGN KEY (`titleid`) REFERENCES `title` (`titleid`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`imageid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `addressgroup` */

CREATE TABLE `addressgroup` (
  `addressgroupid` int(11) NOT NULL DEFAULT '0',
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `groupaddrid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `telecomid` int(11) DEFAULT '0',
  `sendtoall` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`addressgroupid`),
  KEY `groupaddrid` (`groupaddrid`),
  KEY `telecomid` (`telecomid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `addressgroup_ibfk_1` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `addressgroup_ibfk_2` FOREIGN KEY (`groupaddrid`) REFERENCES `mailgroupaddr` (`groupaddrid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `addressgroup_ibfk_3` FOREIGN KEY (`telecomid`) REFERENCES `telecom` (`telecomid`),
  CONSTRAINT `addressgroup_ibfk_4` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `addressgroup_ibfk_5` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `addresssource` */

CREATE TABLE `addresssource` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `sourceid` int(11) NOT NULL DEFAULT '0',
  `sourcename` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`sourceid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2699` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `appmailsignature` */

CREATE TABLE `appmailsignature` (
  `enabled` tinyint(4) DEFAULT NULL,
  `htmlsignature` longblob,
  `languageiso` varchar(10) NOT NULL DEFAULT '',
  `textsignature` longblob,
  PRIMARY KEY (`languageiso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `appointment` */

CREATE TABLE `appointment` (
  `appointmentid` int(11) NOT NULL DEFAULT '0',
  `apptypeid` int(11) NOT NULL DEFAULT '0',
  `addressid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `createdbyid` int(11) NOT NULL DEFAULT '0',
  `enddate` int(11) DEFAULT NULL,
  `endtime` varchar(10) DEFAULT NULL,
  `enddatetime` bigint(20) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `isallday` tinyint(1) NOT NULL DEFAULT '0',
  `isrecurrence` tinyint(4) DEFAULT NULL,
  `location` varchar(30) DEFAULT NULL,
  `priorityid` int(11) DEFAULT NULL,
  `private` tinyint(4) DEFAULT NULL,
  `startdate` int(11) DEFAULT NULL,
  `starttime` varchar(10) DEFAULT NULL,
  `startdatetime` bigint(20) DEFAULT NULL,
  `title` varchar(60) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`appointmentid`),
  KEY `apptypeid` (`apptypeid`),
  KEY `priorityid` (`priorityid`),
  KEY `companyid` (`companyid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `freetextid` (`freetextid`),
  KEY `userid` (`userid`),
  KEY `createdbyid` (`createdbyid`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`apptypeid`) REFERENCES `appointmenttype` (`apptypeid`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`priorityid`) REFERENCES `priority` (`priorityid`),
  CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `appointment_ibfk_4` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `appointment_ibfk_5` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `appointment_ibfk_6` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `appointment_ibfk_7` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `appointment_ibfk_8` FOREIGN KEY (`createdbyid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `appointmenttype` */

CREATE TABLE `appointmenttype` (
  `apptypeid` int(11) NOT NULL DEFAULT '0',
  `color` varchar(10) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`apptypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `appointmenttype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `article` */

CREATE TABLE `article` (
  `articleid` int(11) NOT NULL DEFAULT '0',
  `articletitle` varchar(160) DEFAULT NULL,
  `categoryid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contentid` int(11) NOT NULL DEFAULT '0',
  `createdatetime` bigint(20) NOT NULL DEFAULT '0',
  `createuserid` int(11) NOT NULL DEFAULT '0',
  `keywords` varchar(40) DEFAULT NULL,
  `number` varchar(20) DEFAULT NULL,
  `productid` int(11) DEFAULT NULL,
  `publishedto` smallint(6) DEFAULT NULL,
  `rootquestionid` int(11) DEFAULT NULL,
  `updateuserid` int(11) NOT NULL DEFAULT '0',
  `updatedatetime` bigint(20) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  `viewtimes` int(11) NOT NULL DEFAULT '1',
  `visitdatetime` bigint(20) NOT NULL DEFAULT '0',
  `vote1` int(11) DEFAULT NULL,
  `vote2` int(11) DEFAULT NULL,
  `vote3` int(11) DEFAULT NULL,
  `vote4` int(11) DEFAULT NULL,
  `vote5` int(11) DEFAULT NULL,
  PRIMARY KEY (`articleid`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  KEY `createuserid` (`createuserid`),
  KEY `updateuserid` (`updateuserid`),
  KEY `contentid` (`contentid`),
  KEY `categoryid` (`categoryid`),
  KEY `rootquestionid` (`rootquestionid`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_3` FOREIGN KEY (`createuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_4` FOREIGN KEY (`updateuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_5` FOREIGN KEY (`contentid`) REFERENCES `freetext` (`freetextid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_6` FOREIGN KEY (`categoryid`) REFERENCES `articlecategory` (`categoryid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `article_ibfk_7` FOREIGN KEY (`rootquestionid`) REFERENCES `articlequestion` (`questionid`),
  CONSTRAINT `article_ibfk_8` FOREIGN KEY (`rootquestionid`) REFERENCES `articlequestion` (`questionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlecategory` */

CREATE TABLE `articlecategory` (
  `categoryid` int(11) NOT NULL DEFAULT '0',
  `categoryname` varchar(40) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `parentcategoryid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryid`),
  KEY `companyid` (`companyid`),
  KEY `parentcategoryid` (`parentcategoryid`),
  CONSTRAINT `articlecategory_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlecategory_ibfk_2` FOREIGN KEY (`parentcategoryid`) REFERENCES `articlecategory` (`categoryid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlecomment` */

CREATE TABLE `articlecomment` (
  `articleid` int(11) DEFAULT NULL,
  `commentid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `createdatetime` bigint(20) DEFAULT NULL,
  `createuserid` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`commentid`),
  KEY `companyid` (`companyid`),
  KEY `createuserid` (`createuserid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `articleid` (`articleid`),
  CONSTRAINT `articlecomment_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlecomment_ibfk_2` FOREIGN KEY (`createuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlecomment_ibfk_3` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlecomment_ibfk_4` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlehistory` */

CREATE TABLE `articlehistory` (
  `action` smallint(6) DEFAULT NULL,
  `articleid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `historyid` int(11) NOT NULL DEFAULT '0',
  `logdatetime` bigint(20) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`historyid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  KEY `articleid` (`articleid`),
  CONSTRAINT `articlehistory_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlehistory_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlehistory_ibfk_3` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlelink` */

CREATE TABLE `articlelink` (
  `articleid` int(11) NOT NULL DEFAULT '0',
  `comment` varchar(160) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `createdatetime` bigint(20) DEFAULT NULL,
  `createuserid` int(11) NOT NULL DEFAULT '0',
  `linkid` int(11) NOT NULL DEFAULT '0',
  `url` varchar(200) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`linkid`),
  KEY `companyid` (`companyid`),
  KEY `createuserid` (`createuserid`),
  KEY `articleid` (`articleid`),
  CONSTRAINT `articlelink_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlelink_ibfk_2` FOREIGN KEY (`createuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlelink_ibfk_3` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlequestion` */

CREATE TABLE `articlequestion` (
  `categoryid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `createdatetime` bigint(20) DEFAULT NULL,
  `createuserid` int(11) NOT NULL DEFAULT '0',
  `detailid` int(11) NOT NULL DEFAULT '0',
  `published` tinyint(4) DEFAULT NULL,
  `productid` int(11) DEFAULT NULL,
  `questionid` int(11) NOT NULL DEFAULT '0',
  `summary` varchar(160) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`questionid`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  KEY `createuserid` (`createuserid`),
  KEY `detailid` (`detailid`),
  KEY `categoryid` (`categoryid`),
  CONSTRAINT `articlequestion_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlequestion_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlequestion_ibfk_3` FOREIGN KEY (`createuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlequestion_ibfk_4` FOREIGN KEY (`detailid`) REFERENCES `freetext` (`freetextid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlequestion_ibfk_5` FOREIGN KEY (`categoryid`) REFERENCES `articlecategory` (`categoryid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlerating` */

CREATE TABLE `articlerating` (
  `articleid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`articleid`,`userid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  CONSTRAINT `articlerating_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlerating_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlerating_ibfk_3` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `articlerelated` */

CREATE TABLE `articlerelated` (
  `articleid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `relatedarticleid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`articleid`,`relatedarticleid`),
  KEY `companyid` (`companyid`),
  KEY `relatedarticleid` (`relatedarticleid`),
  CONSTRAINT `articlerelated_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlerelated_ibfk_2` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `articlerelated_ibfk_3` FOREIGN KEY (`relatedarticleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `attach` */

CREATE TABLE `attach` (
  `attachid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `file` longblob NOT NULL,
  `mailid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `visible` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`attachid`),
  KEY `companyid` (`companyid`),
  KEY `mailid` (`mailid`),
  CONSTRAINT `attach_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `attach_ibfk_2` FOREIGN KEY (`mailid`) REFERENCES `mail` (`mailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `attachment` */

CREATE TABLE `attachment` (
  `attachmentid` int(11) NOT NULL DEFAULT '0',
  `attachmenttype` smallint(6) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contenttype` varchar(50) DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `filesize` int(11) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`attachmentid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `attachment_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `attachment_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `bank` */

CREATE TABLE `bank` (
  `addressid` int(11) DEFAULT NULL,
  `bankcode` varchar(8) DEFAULT NULL,
  `bankid` int(11) NOT NULL DEFAULT '0',
  `banklabel` varchar(20) DEFAULT NULL,
  `bankname` varchar(40) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `internationalcode` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`bankid`),
  KEY `companyid` (`companyid`),
  KEY `addressid` (`addressid`),
  CONSTRAINT `0_2701` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2702` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `bankaccount` */

CREATE TABLE `bankaccount` (
  `accountnumber` varchar(12) DEFAULT NULL,
  `accountowner` varchar(40) DEFAULT NULL,
  `addressid` int(11) NOT NULL DEFAULT '0',
  `bankaccountid` int(11) NOT NULL DEFAULT '0',
  `bankid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `iban` varchar(34) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`bankaccountid`),
  KEY `addressid` (`addressid`),
  KEY `bankid` (`bankid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2704` FOREIGN KEY (`bankid`) REFERENCES `bank` (`bankid`),
  CONSTRAINT `0_2705` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2706` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `body` */

CREATE TABLE `body` (
  `body` longblob,
  `bodyid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `type` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bodyid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `body_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `branch` */

CREATE TABLE `branch` (
  `branchgroup` int(11) DEFAULT NULL,
  `branchid` int(11) NOT NULL DEFAULT '0',
  `branchname` varchar(40) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchid`),
  KEY `companyid` (`companyid`),
  KEY `branchgroup` (`branchgroup`),
  CONSTRAINT `0_2708` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `branch_ibfk_1` FOREIGN KEY (`branchgroup`) REFERENCES `branch` (`branchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campactivcontact` */

CREATE TABLE `campactivcontact` (
  `activityid` int(11) DEFAULT NULL,
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `fromemail` varchar(250) DEFAULT NULL,
  `generationid` int(11) DEFAULT NULL,
  `taskid` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignid`,`contactid`),
  KEY `activityid` (`activityid`),
  KEY `taskid` (`taskid`),
  KEY `contactid` (`contactid`),
  KEY `companyid` (`companyid`),
  KEY `generationid` (`generationid`),
  CONSTRAINT `campactivcontact_ibfk_1` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `campactivcontact_ibfk_2` FOREIGN KEY (`activityid`) REFERENCES `campactivity` (`activityid`),
  CONSTRAINT `campactivcontact_ibfk_3` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`),
  CONSTRAINT `campactivcontact_ibfk_4` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`),
  CONSTRAINT `campactivcontact_ibfk_5` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `campactivcontact_ibfk_6` FOREIGN KEY (`generationid`) REFERENCES `campgeneration` (`generationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campactivity` */

CREATE TABLE `campactivity` (
  `activityid` int(11) NOT NULL DEFAULT '0',
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `closedate` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `cost` decimal(13,2) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `numbercontact` int(11) DEFAULT NULL,
  `percent` int(11) DEFAULT NULL,
  `startdate` int(11) DEFAULT NULL,
  `state` smallint(6) DEFAULT NULL,
  `title` varchar(80) NOT NULL DEFAULT '',
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activityid`),
  KEY `freetextid` (`freetextid`),
  KEY `userid` (`userid`),
  KEY `campaignid` (`campaignid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campactivity_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campactivity_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `campactivity_ibfk_3` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `campactivity_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campactivityuser` */

CREATE TABLE `campactivityuser` (
  `activityid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `percent` int(11) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activityid`,`userid`),
  KEY `userid` (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campactivityuser_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `campactivityuser_ibfk_2` FOREIGN KEY (`activityid`) REFERENCES `campactivity` (`activityid`),
  CONSTRAINT `campactivityuser_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campaign` */

CREATE TABLE `campaign` (
  `addresstype` tinyint(1) DEFAULT NULL,
  `awaitedutility` decimal(13,2) DEFAULT NULL,
  `budgetcost` decimal(13,2) DEFAULT NULL,
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `campaignname` varchar(40) NOT NULL DEFAULT '',
  `changedate` int(11) DEFAULT NULL,
  `closedate` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contacttype` tinyint(1) DEFAULT NULL,
  `isdouble` tinyint(1) DEFAULT NULL,
  `employeeid` int(11) DEFAULT NULL,
  `includepartner` tinyint(1) DEFAULT NULL,
  `numbercontacts` int(6) DEFAULT NULL,
  `realcost` decimal(13,2) DEFAULT NULL,
  `recorddate` int(11) DEFAULT NULL,
  `remark` int(11) DEFAULT NULL,
  `startdate` int(11) DEFAULT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0',
  `text` int(11) DEFAULT NULL,
  `totalhits` int(11) DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignid`),
  KEY `companyid` (`companyid`),
  KEY `employeeid` (`employeeid`),
  KEY `remark` (`remark`),
  KEY `typeid` (`typeid`),
  CONSTRAINT `0_2710` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2711` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `0_3004` FOREIGN KEY (`remark`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campaign_ibfk_1` FOREIGN KEY (`typeid`) REFERENCES `camptype` (`camptypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campaigntext` */

CREATE TABLE `campaigntext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `bydefault` int(1) DEFAULT NULL,
  `freetextid` int(11) DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`languageid`,`templateid`),
  KEY `templateid` (`templateid`),
  KEY `freetextid` (`freetextid`),
  KEY `campaignid` (`campaignid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campaigntext_ibfk_1` FOREIGN KEY (`templateid`) REFERENCES `camptemplate` (`templateid`),
  CONSTRAINT `campaigntext_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campaigntext_ibfk_3` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `campaigntext_ibfk_4` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `campaigntext_ibfk_5` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campaigntextimg` */

CREATE TABLE `campaigntextimg` (
  `companyid` int(11) NOT NULL,
  `imagestoreid` int(11) NOT NULL,
  `languageid` int(11) NOT NULL,
  `templateid` int(11) NOT NULL,
  PRIMARY KEY (`imagestoreid`,`languageid`,`templateid`),
  KEY `companyid` (`companyid`),
  KEY `languageid` (`languageid`,`templateid`),
  CONSTRAINT `campaigntextimg_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `campaigntextimg_ibfk_2` FOREIGN KEY (`languageid`, `templateid`) REFERENCES `campaigntext` (`languageid`, `templateid`),
  CONSTRAINT `campaigntextimg_ibfk_3` FOREIGN KEY (`imagestoreid`) REFERENCES `imagestore` (`imagestoreid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campattach` */

CREATE TABLE `campattach` (
  `attachid` int(11) NOT NULL DEFAULT '0',
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `comment` varchar(100) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filename` varchar(80) NOT NULL DEFAULT '',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `size` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`attachid`),
  KEY `freetextid` (`freetextid`),
  KEY `campaignid` (`campaignid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campattach_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campattach_ibfk_2` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `campattach_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campcontact` */

CREATE TABLE `campcontact` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `active` smallint(6) DEFAULT NULL,
  `activityid` int(11) DEFAULT NULL,
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `campcontactid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(4) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`campaignid`,`campcontactid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `campaignid` (`campaignid`),
  KEY `contactperson` (`addressid`,`contactpersonid`),
  KEY `userid` (`userid`),
  KEY `activityid` (`activityid`),
  CONSTRAINT `0_2926` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `0_2927` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2928` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `0_2929` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `campcontact_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `campcontact_ibfk_2` FOREIGN KEY (`activityid`) REFERENCES `campactivity` (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campcriterion` */

CREATE TABLE `campcriterion` (
  `campaignid` int(4) NOT NULL DEFAULT '0',
  `categoryid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `criterionid` int(11) NOT NULL DEFAULT '0',
  `numberhits` int(11) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  `valueid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `campcriterionvalueid` int(11) DEFAULT NULL,
  PRIMARY KEY (`criterionid`),
  KEY `companyid` (`companyid`),
  KEY `campaignid` (`campaignid`),
  KEY `valueid` (`valueid`),
  KEY `campcriterionvalueid` (`campcriterionvalueid`),
  KEY `categoryid` (`categoryid`),
  CONSTRAINT `0_2718` FOREIGN KEY (`campcriterionvalueid`) REFERENCES `campcriterionvalue` (`campcriterionvalueid`),
  CONSTRAINT `0_2719` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `0_2720` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2721` FOREIGN KEY (`valueid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campcriterion_ibfk_1` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Search criteria for campaings';

/*Table structure for table `campcriterionvalue` */

CREATE TABLE `campcriterionvalue` (
  `campcriterionvalueid` int(11) NOT NULL DEFAULT '0',
  `tableid` int(11) NOT NULL DEFAULT '0',
  `descriptionkey` varchar(50) DEFAULT NULL,
  `field` varchar(20) NOT NULL DEFAULT '',
  `fieldtype` tinyint(2) NOT NULL DEFAULT '0',
  `fieldname` varchar(30) DEFAULT NULL,
  `tablename` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`campcriterionvalueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `campgenattach` */

CREATE TABLE `campgenattach` (
  `attachmentid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `generationid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`attachmentid`,`generationid`),
  KEY `generationid` (`generationid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campgenattach_ibfk_1` FOREIGN KEY (`generationid`) REFERENCES `campgeneration` (`generationid`),
  CONSTRAINT `campgenattach_ibfk_2` FOREIGN KEY (`attachmentid`) REFERENCES `attachment` (`attachmentid`),
  CONSTRAINT `campgenattach_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `campgeneration` */

CREATE TABLE `campgeneration` (
  `activityid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `generationid` int(11) NOT NULL DEFAULT '0',
  `generationtime` bigint(20) NOT NULL DEFAULT '0',
  `templateid` int(11) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`generationid`),
  KEY `activityid` (`activityid`),
  KEY `userid` (`userid`),
  KEY `templateid` (`templateid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `campgeneration_ibfk_1` FOREIGN KEY (`activityid`) REFERENCES `campactivity` (`activityid`),
  CONSTRAINT `campgeneration_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `campgeneration_ibfk_3` FOREIGN KEY (`templateid`) REFERENCES `camptemplate` (`templateid`),
  CONSTRAINT `campgeneration_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `campgentext` */

CREATE TABLE `campgentext` (
  `campgentextid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) DEFAULT NULL,
  `generationid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`campgentextid`),
  KEY `generationid` (`generationid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `languageid` (`languageid`),
  CONSTRAINT `campgentext_ibfk_1` FOREIGN KEY (`generationid`) REFERENCES `campgeneration` (`generationid`),
  CONSTRAINT `campgentext_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `campgentext_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `campgentext_ibfk_4` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `campgentextimg` */

CREATE TABLE `campgentextimg` (
  `campgentextid` int(11) NOT NULL,
  `companyid` int(11) NOT NULL,
  `imagestoreid` int(11) NOT NULL,
  PRIMARY KEY (`campgentextid`,`imagestoreid`),
  KEY `companyid` (`companyid`),
  KEY `imagestoreid` (`imagestoreid`),
  CONSTRAINT `campgentextimg_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `campgentextimg_ibfk_2` FOREIGN KEY (`imagestoreid`) REFERENCES `imagestore` (`imagestoreid`),
  CONSTRAINT `campgentextimg_ibfk_3` FOREIGN KEY (`campgentextid`) REFERENCES `campgentext` (`campgentextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `camptemplate` */

CREATE TABLE `camptemplate` (
  `campaignid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `description` varchar(30) DEFAULT NULL,
  `documenttype` int(11) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`templateid`),
  KEY `campaignid` (`campaignid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `camptemplate_ibfk_1` FOREIGN KEY (`campaignid`) REFERENCES `campaign` (`campaignid`),
  CONSTRAINT `camptemplate_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `camptype` */

CREATE TABLE `camptype` (
  `camptypeid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `title` varchar(80) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`camptypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `camptype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `caseactivity` */

CREATE TABLE `caseactivity` (
  `activityid` int(11) NOT NULL DEFAULT '0',
  `caseid` int(11) NOT NULL DEFAULT '0',
  `closedate` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `fromuserid` int(11) NOT NULL DEFAULT '0',
  `isopen` tinyint(4) DEFAULT NULL,
  `opendate` int(11) DEFAULT NULL,
  `parentactivityid` int(11) DEFAULT NULL,
  `stateid` int(11) NOT NULL DEFAULT '0',
  `touserid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  `worklevelid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activityid`),
  KEY `companyid` (`companyid`),
  KEY `fromuserid` (`fromuserid`),
  KEY `touserid` (`touserid`),
  KEY `freetextid` (`freetextid`),
  KEY `stateid` (`stateid`),
  KEY `caseid` (`caseid`),
  KEY `worklevelid` (`worklevelid`),
  KEY `parentactivityid` (`parentactivityid`),
  CONSTRAINT `caseactivity_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `caseactivity_ibfk_2` FOREIGN KEY (`fromuserid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `caseactivity_ibfk_3` FOREIGN KEY (`touserid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `caseactivity_ibfk_4` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `caseactivity_ibfk_5` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`),
  CONSTRAINT `caseactivity_ibfk_6` FOREIGN KEY (`caseid`) REFERENCES `supportcase` (`caseid`),
  CONSTRAINT `caseactivity_ibfk_7` FOREIGN KEY (`worklevelid`) REFERENCES `caseworklevel` (`worklevelid`),
  CONSTRAINT `caseactivity_ibfk_8` FOREIGN KEY (`parentactivityid`) REFERENCES `caseactivity` (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `caseseverity` */

CREATE TABLE `caseseverity` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `severityid` int(11) NOT NULL DEFAULT '0',
  `severityname` varchar(40) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`severityid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `langtextid_2` (`langtextid`),
  CONSTRAINT `caseseverity_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `caseseverity_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `caseseverity_ibfk_3` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `casetype` */

CREATE TABLE `casetype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `casetypeid` int(11) NOT NULL DEFAULT '0',
  `casetypename` varchar(40) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`casetypeid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `langtextid_2` (`langtextid`),
  CONSTRAINT `casetype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `casetype_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `casetype_ibfk_3` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `caseworklevel` */

CREATE TABLE `caseworklevel` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `worklevelid` int(11) NOT NULL DEFAULT '0',
  `sequence` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `worklevelname` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`worklevelid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `langtextid_2` (`langtextid`),
  CONSTRAINT `caseworklevel_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `caseworklevel_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `caseworklevel_ibfk_3` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `categfieldvalue` */

CREATE TABLE `categfieldvalue` (
  `addressid` int(11) DEFAULT NULL,
  `attachid` int(11) DEFAULT NULL,
  `categoryid` int(11) NOT NULL DEFAULT '0',
  `categoryvalueid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `datevalue` int(11) DEFAULT NULL,
  `decimalvalue` decimal(13,2) DEFAULT NULL,
  `fieldvalueid` int(11) NOT NULL DEFAULT '0',
  `filename` varchar(250) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `integervalue` int(11) DEFAULT NULL,
  `linkvalue` varchar(250) DEFAULT NULL,
  `processid` int(11) DEFAULT NULL,
  `productid` int(11) DEFAULT NULL,
  `stringvalue` varchar(80) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`fieldvalueid`),
  KEY `categoryid` (`categoryid`),
  KEY `categoryvalueid` (`categoryvalueid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `customerid` (`customerid`),
  KEY `productid` (`productid`),
  KEY `companyid` (`companyid`),
  KEY `freetextid` (`freetextid`),
  KEY `attachid` (`attachid`),
  KEY `processid` (`processid`),
  CONSTRAINT `categfieldvalue_ibfk_1` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`),
  CONSTRAINT `categfieldvalue_ibfk_10` FOREIGN KEY (`attachid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `categfieldvalue_ibfk_11` FOREIGN KEY (`processid`) REFERENCES `salesprocess` (`processid`),
  CONSTRAINT `categfieldvalue_ibfk_2` FOREIGN KEY (`categoryvalueid`) REFERENCES `categoryvalue` (`categoryvalueid`),
  CONSTRAINT `categfieldvalue_ibfk_4` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `categfieldvalue_ibfk_5` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerid`),
  CONSTRAINT `categfieldvalue_ibfk_6` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `categfieldvalue_ibfk_7` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `categfieldvalue_ibfk_8` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `categfieldvalue_ibfk_9` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `category` */

CREATE TABLE `category` (
  `categoryid` int(11) NOT NULL DEFAULT '0',
  `categorygroupid` int(11) DEFAULT NULL,
  `categoryname` varchar(20) DEFAULT NULL,
  `categorytype` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `hassubcategories` tinyint(4) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `parentcategory` int(11) DEFAULT NULL,
  `secondgroupid` int(11) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `tableid` varchar(8) NOT NULL DEFAULT '',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `categorygroupid` (`categorygroupid`),
  KEY `secondgroupid` (`secondgroupid`),
  CONSTRAINT `0_2724` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `category_ibfk_2` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `category_ibfk_3` FOREIGN KEY (`categorygroupid`) REFERENCES `categorygroup` (`categorygroupid`),
  CONSTRAINT `category_ibfk_4` FOREIGN KEY (`secondgroupid`) REFERENCES `categorygroup` (`categorygroupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `categorygroup` */

CREATE TABLE `categorygroup` (
  `categorygroupid` int(11) NOT NULL DEFAULT '0',
  `categorytabid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `label` varchar(50) NOT NULL DEFAULT '',
  `sequence` int(11) NOT NULL DEFAULT '0',
  `tableid` varchar(8) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`categorygroupid`),
  KEY `categorytabid` (`categorytabid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `categorygroup_ibfk_1` FOREIGN KEY (`categorytabid`) REFERENCES `categorytab` (`categorytabid`),
  CONSTRAINT `categorygroup_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `categoryrelation` */

CREATE TABLE `categoryrelation` (
  `categoryid` int(11) NOT NULL DEFAULT '0',
  `categoryvalueid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`categoryid`,`categoryvalueid`),
  KEY `companyid` (`companyid`),
  KEY `categoryvalueid` (`categoryvalueid`),
  CONSTRAINT `categoryrelation_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `categoryrelation_ibfk_2` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`),
  CONSTRAINT `categoryrelation_ibfk_3` FOREIGN KEY (`categoryvalueid`) REFERENCES `categoryvalue` (`categoryvalueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `categorytab` */

CREATE TABLE `categorytab` (
  `categorytabid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `label` varchar(50) NOT NULL DEFAULT '',
  `sequence` int(11) NOT NULL DEFAULT '0',
  `tableid` varchar(8) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`categorytabid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `categorytab_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `categoryvalue` */

CREATE TABLE `categoryvalue` (
  `categoryid` int(11) NOT NULL DEFAULT '0',
  `categoryvalueid` int(11) NOT NULL DEFAULT '0',
  `categoryvaluename` varchar(40) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `tableid` varchar(8) NOT NULL DEFAULT '',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryvalueid`),
  KEY `categoryid` (`categoryid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2726` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`),
  CONSTRAINT `0_2727` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `city` */

CREATE TABLE `city` (
  `cityid` int(11) NOT NULL DEFAULT '0',
  `cityname` varchar(40) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `countryid` int(11) DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  `zip` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`cityid`),
  KEY `companyid` (`companyid`),
  KEY `countryid` (`countryid`),
  CONSTRAINT `0_2729` FOREIGN KEY (`countryid`) REFERENCES `country` (`countryid`),
  CONSTRAINT `0_2730` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `columngroup` */

CREATE TABLE `columngroup` (
  `axis` tinyint(4) DEFAULT NULL COMMENT 'store the axis (X or Y) at the that belong this column group (in report of type ressumen or matrix) . defined how constant: ie. (1=X, 2=Y)',
  `columngroupid` int(11) NOT NULL DEFAULT '0',
  `columnorder` tinyint(4) DEFAULT NULL COMMENT 'how be oder this column. defined how constant: ie. (1=ASCENDENT, 2=DESCENDANT)',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `groupbydate` tinyint(4) DEFAULT NULL COMMENT 'only if the column is of date type, denote how grouping the column. defined how constant: ie. (1=DAY, 2=MONTH, 3=YEAR)',
  `reportcolumnid` int(11) NOT NULL DEFAULT '0',
  `sequence` smallint(6) DEFAULT NULL COMMENT 'store the position of the grouper column in the report view',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`columngroupid`),
  KEY `reportcolumnid` (`reportcolumnid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `columngroup_ibfk_1` FOREIGN KEY (`reportcolumnid`) REFERENCES `reportcolumn` (`reportcolumnid`),
  CONSTRAINT `columngroup_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `columntotalize` */

CREATE TABLE `columntotalize` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `reportcolumnid` int(11) NOT NULL DEFAULT '0',
  `reporttotalizeid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`reportcolumnid`,`reporttotalizeid`),
  KEY `reporttotalizeid` (`reporttotalizeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `columntotalize_ibfk_1` FOREIGN KEY (`reportcolumnid`) REFERENCES `reportcolumn` (`reportcolumnid`),
  CONSTRAINT `columntotalize_ibfk_2` FOREIGN KEY (`reporttotalizeid`) REFERENCES `reporttotalize` (`reporttotalizeid`),
  CONSTRAINT `columntotalize_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `company` */

CREATE TABLE `company` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `copytemplate` smallint(6) DEFAULT NULL,
  `creditnoteruleid` int(11) DEFAULT NULL,
  `emailcontract` varchar(200) DEFAULT NULL,
  `finishlicense` int(11) DEFAULT NULL,
  `invoicedayssend` int(11) NOT NULL DEFAULT '0',
  `invoiceruleid` int(11) DEFAULT NULL,
  `language` varchar(5) DEFAULT NULL,
  `login` varchar(20) DEFAULT NULL,
  `logoid` int(11) DEFAULT NULL,
  `maildomain` varchar(40) DEFAULT NULL,
  `maxattachsize` int(11) DEFAULT NULL,
  `maxmaxattachsize` int(11) DEFAULT NULL,
  `mediatype` char(1) DEFAULT NULL,
  `netgross` tinyint(4) DEFAULT NULL,
  `routepageid` int(11) DEFAULT NULL,
  `rowsperpage` int(11) DEFAULT NULL,
  `salutationid` int(11) DEFAULT NULL,
  `startlicense` int(11) DEFAULT NULL,
  `style` varchar(20) DEFAULT NULL,
  `telecomtypestatus` varchar(40) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `timezone` varchar(25) DEFAULT NULL,
  `trial` tinyint(1) DEFAULT NULL,
  `usersallowed` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `isdefault` tinyint(4) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`companyid`),
  UNIQUE KEY `login` (`login`),
  KEY `routepageid` (`routepageid`),
  KEY `logoid` (`logoid`),
  KEY `invoiceruleid` (`invoiceruleid`),
  KEY `creditnoteruleid` (`creditnoteruleid`),
  KEY `salutationid` (`salutationid`),
  CONSTRAINT `0_2732` FOREIGN KEY (`routepageid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `0_2733` FOREIGN KEY (`companyid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `company_ibfk_1` FOREIGN KEY (`logoid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `company_ibfk_2` FOREIGN KEY (`invoiceruleid`) REFERENCES `sequencerule` (`numberid`),
  CONSTRAINT `company_ibfk_3` FOREIGN KEY (`creditnoteruleid`) REFERENCES `sequencerule` (`numberid`),
  CONSTRAINT `company_ibfk_4` FOREIGN KEY (`salutationid`) REFERENCES `salutation` (`salutationid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `companymodule` */

CREATE TABLE `companymodule` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `moduleid` int(11) NOT NULL DEFAULT '0',
  `maintablelimit` int(11) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`companyid`,`moduleid`),
  KEY `IX_Relationship23` (`companyid`),
  KEY `IX_Relationship24` (`moduleid`),
  CONSTRAINT `companymodule_ibfk_2` FOREIGN KEY (`moduleid`) REFERENCES `systemmodule` (`moduleid`),
  CONSTRAINT `companymodule_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `competitorproduct` */

CREATE TABLE `competitorproduct` (
  `changedate` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `competitorid` int(11) NOT NULL DEFAULT '0',
  `compproductid` int(11) NOT NULL DEFAULT '0',
  `entrydate` int(11) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `productname` varchar(80) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`compproductid`),
  KEY `companyid` (`companyid`),
  KEY `competitorid` (`competitorid`),
  KEY `productid` (`productid`),
  CONSTRAINT `0_2991` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2993` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `competitorproduct_ibfk_1` FOREIGN KEY (`competitorid`) REFERENCES `address` (`addressid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contact` */

CREATE TABLE `contact` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `datefinished` int(11) DEFAULT NULL,
  `datestart` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `contactnumber` varchar(40) DEFAULT NULL,
  `contactpersonid` int(11) DEFAULT NULL,
  `docfilename` varchar(250) DEFAULT NULL,
  `employeeid` int(11) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `isaction` tinyint(1) DEFAULT NULL,
  `isinout` char(1) DEFAULT NULL,
  `note` varchar(250) DEFAULT NULL,
  `probability` smallint(3) DEFAULT NULL,
  `processid` int(11) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `templateid` int(11) DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `tmpfreetextid` int(11) DEFAULT NULL,
  PRIMARY KEY (`contactid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `employeeid` (`employeeid`),
  KEY `freetextid` (`freetextid`),
  KEY `templateid` (`templateid`),
  KEY `contactperson` (`addressid`,`contactpersonid`),
  KEY `salesprocess` (`processid`),
  CONSTRAINT `0_2735` FOREIGN KEY (`templateid`) REFERENCES `template` (`templateid`),
  CONSTRAINT `0_2736` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2738` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `0_2739` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `0_2740` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2741` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`processid`) REFERENCES `salesprocess` (`processid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contactattach` */

CREATE TABLE `contactattach` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `contenttype` varchar(20) NOT NULL DEFAULT '',
  `filename` varchar(50) NOT NULL DEFAULT '',
  `filesize` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contactid`,`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `freetextid` (`freetextid`),
  CONSTRAINT `contactattach_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `contactattach_ibfk_2` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`),
  CONSTRAINT `contactattach_ibfk_3` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contactperson` */

CREATE TABLE `contactperson` (
  `active` tinyint(1) DEFAULT NULL,
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) NOT NULL DEFAULT '0',
  `departmentid` int(11) DEFAULT NULL,
  `function` varchar(40) DEFAULT NULL,
  `persontypeid` int(11) DEFAULT NULL,
  `recorddate` int(11) DEFAULT NULL,
  `recorduserid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`addressid`,`contactpersonid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `departmentid` (`departmentid`),
  KEY `persontypeid` (`persontypeid`),
  KEY `contactpersonid` (`contactpersonid`),
  KEY `recorduserid` (`recorduserid`),
  CONSTRAINT `0_2748` FOREIGN KEY (`contactpersonid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2749` FOREIGN KEY (`persontypeid`) REFERENCES `persontype` (`persontypeid`),
  CONSTRAINT `0_2750` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2751` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2752` FOREIGN KEY (`departmentid`) REFERENCES `department` (`departmentid`),
  CONSTRAINT `contactperson_ibfk_1` FOREIGN KEY (`recorduserid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `contracttype` */

CREATE TABLE `contracttype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contracttypeid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL DEFAULT '',
  `tobeinvoiced` tinyint(4) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contracttypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `contracttype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `costcenter` */

CREATE TABLE `costcenter` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `costcenterid` int(11) NOT NULL DEFAULT '0',
  `costcentername` varchar(40) DEFAULT NULL,
  `parentcostcenterid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`costcenterid`),
  KEY `parentcostcenterid` (`parentcostcenterid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2754` FOREIGN KEY (`parentcostcenterid`) REFERENCES `costcenter` (`costcenterid`),
  CONSTRAINT `0_2755` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `country` */

CREATE TABLE `country` (
  `areacode` varchar(4) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `countryid` int(11) NOT NULL DEFAULT '0',
  `countryname` varchar(30) DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `prefix` int(11) DEFAULT NULL,
  PRIMARY KEY (`countryid`),
  KEY `companyid` (`companyid`),
  KEY `currencyid` (`currencyid`),
  CONSTRAINT `0_2757` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`),
  CONSTRAINT `country_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `currency` */

CREATE TABLE `currency` (
  `companyid` int(11) DEFAULT NULL,
  `currencyid` int(11) NOT NULL DEFAULT '0',
  `isbasiccurrency` tinyint(1) DEFAULT NULL,
  `unit` decimal(13,2) DEFAULT NULL,
  `symbol` varchar(4) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `currencyname` varchar(30) DEFAULT NULL,
  `label` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`currencyid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2760` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `customer` */

CREATE TABLE `customer` (
  `branchid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `customerid` int(11) NOT NULL DEFAULT '0',
  `customernumber` varchar(20) DEFAULT NULL,
  `customertypeid` int(11) DEFAULT NULL,
  `employeeid` int(11) DEFAULT NULL,
  `expectedturnover` decimal(13,2) DEFAULT NULL,
  `numberofemployees` int(5) DEFAULT NULL,
  `partnerid` int(11) DEFAULT NULL,
  `payconditionid` int(11) DEFAULT NULL,
  `paymoralityid` int(11) DEFAULT NULL,
  `priorityid` int(11) DEFAULT NULL,
  `sourceid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`customerid`),
  KEY `branchid` (`branchid`),
  KEY `companyid` (`companyid`),
  KEY `customertypeid` (`customertypeid`),
  KEY `partnerid` (`partnerid`),
  KEY `payconditionid` (`payconditionid`),
  KEY `paymoralityid` (`paymoralityid`),
  KEY `priorityid` (`priorityid`),
  KEY `sourceid` (`sourceid`),
  KEY `employeeid` (`employeeid`),
  CONSTRAINT `0_2762` FOREIGN KEY (`customerid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2763` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2764` FOREIGN KEY (`branchid`) REFERENCES `branch` (`branchid`),
  CONSTRAINT `0_2765` FOREIGN KEY (`customertypeid`) REFERENCES `customertype` (`customertypeid`),
  CONSTRAINT `0_2766` FOREIGN KEY (`sourceid`) REFERENCES `addresssource` (`sourceid`),
  CONSTRAINT `0_2767` FOREIGN KEY (`partnerid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2768` FOREIGN KEY (`payconditionid`) REFERENCES `paycondition` (`payconditionid`),
  CONSTRAINT `0_2769` FOREIGN KEY (`paymoralityid`) REFERENCES `paymorality` (`paymoralityid`),
  CONSTRAINT `0_2770` FOREIGN KEY (`priorityid`) REFERENCES `priority` (`priorityid`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `customertype` */

CREATE TABLE `customertype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `customertypeid` int(11) NOT NULL DEFAULT '0',
  `customertypename` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`customertypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2777` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `dashbadmcomp` */

CREATE TABLE `dashbadmcomp` (
  `componentid` int(11) NOT NULL DEFAULT '0',
  `columny` int(11) DEFAULT NULL,
  `companyid` int(11) DEFAULT NULL,
  `dashbcontainerid` int(11) NOT NULL DEFAULT '0',
  `rowx` int(11) DEFAULT NULL,
  `xmlcomponentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`dashbcontainerid`,`componentid`),
  KEY `componentid` (`componentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `dashbadmcomp_ibfk_1` FOREIGN KEY (`dashbcontainerid`) REFERENCES `dashbcontainer` (`dashbcontainerid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dashbadmcomp_ibfk_2` FOREIGN KEY (`componentid`) REFERENCES `dashbcomponent` (`componentid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dashbadmcomp_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `dashbcompcolumn` */

CREATE TABLE `dashbcompcolumn` (
  `companyid` int(11) DEFAULT NULL,
  `compcolumnid` int(11) NOT NULL DEFAULT '0',
  `componentid` int(11) DEFAULT NULL,
  `ord` varchar(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `xmlcolumnid` int(11) DEFAULT NULL,
  PRIMARY KEY (`compcolumnid`),
  KEY `componentid` (`componentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `dashbcompcolumn_ibfk_1` FOREIGN KEY (`componentid`) REFERENCES `dashbcomponent` (`componentid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dashbcompcolumn_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `dashbcomponent` */

CREATE TABLE `dashbcomponent` (
  `companyid` int(11) DEFAULT NULL,
  `componentid` int(11) NOT NULL DEFAULT '0',
  `xmlcomponentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`componentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `dashbcomponent_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `dashbcontainer` */

CREATE TABLE `dashbcontainer` (
  `companyid` int(11) DEFAULT NULL,
  `dashbcontainerid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`dashbcontainerid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  CONSTRAINT `dashbcontainer_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dashbcontainer_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `dashbfilter` */

CREATE TABLE `dashbfilter` (
  `companyid` int(11) DEFAULT NULL,
  `componentid` int(11) DEFAULT NULL,
  `filterid` int(11) NOT NULL DEFAULT '0',
  `isrange` smallint(6) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `value` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`filterid`),
  KEY `componentid` (`componentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `dashbfilter_ibfk_1` FOREIGN KEY (`componentid`) REFERENCES `dashbcomponent` (`componentid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dashbfilter_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `department` */

CREATE TABLE `department` (
  `addressid` int(11) DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `departmentid` int(11) NOT NULL DEFAULT '0',
  `departmentname` varchar(30) DEFAULT NULL,
  `parentdepartmentid` int(11) DEFAULT NULL,
  `managerid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`departmentid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `parentdepartmentid` (`parentdepartmentid`),
  KEY `manager` (`addressid`,`managerid`),
  CONSTRAINT `0_2779` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2780` FOREIGN KEY (`parentdepartmentid`) REFERENCES `department` (`departmentid`),
  CONSTRAINT `0_2781` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2933` FOREIGN KEY (`addressid`, `managerid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `elwisuser` */

CREATE TABLE `elwisuser` (
  `addressid` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `accessip` varchar(200) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `dayfragmentation` int(11) DEFAULT NULL,
  `defaultview` int(11) DEFAULT NULL,
  `emailappointment` varchar(200) DEFAULT NULL,
  `emailquestion` varchar(200) DEFAULT NULL,
  `emailschetask` varchar(200) DEFAULT NULL,
  `emailsupportcase` varchar(200) DEFAULT NULL,
  `finaldaywork` int(11) DEFAULT NULL,
  `favoritelanguage` varchar(5) DEFAULT NULL,
  `hasmailaccount` tinyint(1) DEFAULT NULL,
  `holidaycountryid` int(11) DEFAULT NULL,
  `initialdaywork` int(11) DEFAULT NULL,
  `isdefault` tinyint(1) DEFAULT NULL,
  `login` varchar(20) DEFAULT NULL,
  `maxrecentlist` smallint(6) DEFAULT NULL,
  `password` varchar(80) DEFAULT NULL,
  `rowsperpage` int(11) DEFAULT NULL,
  `seeprivatedata` tinyint(1) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `timezone` varchar(35) DEFAULT NULL,
  `type` tinyint(3) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  KEY `companyid` (`companyid`),
  KEY `favoritelanguageid` (`favoritelanguage`),
  KEY `userid` (`userid`),
  KEY `holidaycountryid` (`holidaycountryid`),
  KEY `addressid` (`addressid`),
  CONSTRAINT `0_2785` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `elwisuser_ibfk_1` FOREIGN KEY (`holidaycountryid`) REFERENCES `country` (`countryid`),
  CONSTRAINT `elwisuser_ibfk_2` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `emailsource` */

CREATE TABLE `emailsource` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filesize` int(11) NOT NULL DEFAULT '0',
  `mailid` int(11) NOT NULL DEFAULT '0',
  `source` longblob NOT NULL,
  PRIMARY KEY (`mailid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `emailsource_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `emailsource_ibfk_2` FOREIGN KEY (`mailid`) REFERENCES `mail` (`mailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `employee` */

CREATE TABLE `employee` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `costcenterid` int(11) DEFAULT NULL,
  `costhour` decimal(13,2) DEFAULT NULL,
  `costposition` decimal(13,2) DEFAULT NULL,
  `dateend` int(11) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `employeeid` int(11) NOT NULL DEFAULT '0',
  `function` varchar(40) DEFAULT NULL,
  `healthfund` int(11) DEFAULT NULL,
  `hiredate` int(11) DEFAULT NULL,
  `hourlyrate` decimal(13,2) DEFAULT NULL,
  `initials` varchar(10) DEFAULT NULL,
  `officeid` int(11) DEFAULT NULL,
  `socialsecnumber` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`employeeid`),
  KEY `companyid` (`companyid`),
  KEY `costcenterid` (`costcenterid`),
  KEY `departmentid` (`departmentid`),
  KEY `employeeid` (`employeeid`),
  KEY `officeid` (`officeid`),
  KEY `healthfund` (`healthfund`),
  CONSTRAINT `0_2788` FOREIGN KEY (`healthfund`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2789` FOREIGN KEY (`officeid`) REFERENCES `office` (`officeid`),
  CONSTRAINT `0_2790` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2791` FOREIGN KEY (`costcenterid`) REFERENCES `costcenter` (`costcenterid`),
  CONSTRAINT `0_2792` FOREIGN KEY (`departmentid`) REFERENCES `department` (`departmentid`),
  CONSTRAINT `0_2793` FOREIGN KEY (`employeeid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `favorite` */

CREATE TABLE `favorite` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`addressid`,`userid`),
  KEY `companyid` (`companyid`),
  KEY `addressid` (`addressid`),
  KEY `userid` (`userid`),
  CONSTRAINT `0_2795` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2796` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `0_2797` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `filter` */

CREATE TABLE `filter` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filterid` int(11) NOT NULL DEFAULT '0',
  `folderid` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`filterid`),
  KEY `companyid` (`companyid`),
  KEY `folderid` (`folderid`),
  CONSTRAINT `filter_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `filter_ibfk_2` FOREIGN KEY (`folderid`) REFERENCES `folder` (`folderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `filtercondition` */

CREATE TABLE `filtercondition` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `conditionid` int(11) NOT NULL DEFAULT '0',
  `conditionkey` int(11) NOT NULL DEFAULT '0',
  `filterid` int(11) DEFAULT NULL,
  `namekey` int(11) NOT NULL DEFAULT '0',
  `text` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`conditionid`),
  KEY `companyid` (`companyid`),
  KEY `filterid` (`filterid`),
  CONSTRAINT `filtercondition_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `filtercondition_ibfk_2` FOREIGN KEY (`filterid`) REFERENCES `filter` (`filterid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `filtervalue` */

CREATE TABLE `filtervalue` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filtervalueid` int(11) NOT NULL DEFAULT '0',
  `pksequence` smallint(6) DEFAULT NULL COMMENT 'group sequence of primary key',
  `reportfilterid` int(11) NOT NULL DEFAULT '0',
  `sequence` smallint(6) NOT NULL DEFAULT '0' COMMENT 'denote the position in values of filter',
  `value` varchar(250) DEFAULT NULL COMMENT 'fiter value',
  PRIMARY KEY (`filtervalueid`),
  KEY `reportfilterid` (`reportfilterid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `filtervalue_ibfk_1` FOREIGN KEY (`reportfilterid`) REFERENCES `reportfilter` (`reportfilterid`),
  CONSTRAINT `filtervalue_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `folder` */

CREATE TABLE `folder` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `columntoshow` smallint(6) DEFAULT NULL,
  `date` int(11) NOT NULL DEFAULT '0',
  `folderid` int(11) NOT NULL DEFAULT '0',
  `isopen` tinyint(4) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `parentid` int(11) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `usermailid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`folderid`),
  KEY `companyid` (`companyid`),
  KEY `usermailid` (`usermailid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `folder_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `folder_ibfk_2` FOREIGN KEY (`usermailid`) REFERENCES `usermail` (`usermailid`),
  CONSTRAINT `folder_ibfk_3` FOREIGN KEY (`parentid`) REFERENCES `folder` (`folderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `freetext` */

CREATE TABLE `freetext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `freetexttype` smallint(6) DEFAULT NULL,
  `freetextvalue` longblob,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`freetextid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2799` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `functiondependency` */

CREATE TABLE `functiondependency` (
  `dependencyid` int(11) NOT NULL DEFAULT '0',
  `functionid` int(11) NOT NULL DEFAULT '0',
  `operationdependency` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`dependencyid`,`functionid`),
  KEY `IX_Relationship7` (`functionid`),
  CONSTRAINT `functiondependency_ibfk_1` FOREIGN KEY (`functionid`) REFERENCES `systemfunction` (`functionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `holiday` */

CREATE TABLE `holiday` (
  `countryid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `day` int(11) DEFAULT '0',
  `holidayid` int(11) NOT NULL DEFAULT '0',
  `month` int(11) DEFAULT '0',
  `movetomonday` tinyint(4) DEFAULT '0',
  `occurrence` int(11) DEFAULT '0' COMMENT 'The year or week of month value',
  `title` varchar(40) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`holidayid`),
  KEY `companyid` (`companyid`),
  KEY `countryid` (`countryid`),
  CONSTRAINT `holiday_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `holiday_ibfk_2` FOREIGN KEY (`countryid`) REFERENCES `country` (`countryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `imagestore` */

CREATE TABLE `imagestore` (
  `companyid` int(11) NOT NULL,
  `filedata` longblob NOT NULL,
  `filename` varchar(255) NOT NULL,
  `imagestoreid` int(11) NOT NULL,
  `imagetype` smallint(6) NOT NULL,
  `sessionid` varchar(60) NOT NULL,
  PRIMARY KEY (`imagestoreid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `imagestore_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `importcolumn` */

CREATE TABLE `importcolumn` (
  `columnid` int(11) NOT NULL,
  `columnname` varchar(100) DEFAULT NULL,
  `columnvalue` int(11) NOT NULL,
  `companyid` int(11) NOT NULL,
  `groupid` int(11) NOT NULL,
  `importcolumnid` int(11) NOT NULL,
  `profileid` int(11) NOT NULL,
  `uiposition` int(11) NOT NULL,
  PRIMARY KEY (`importcolumnid`),
  KEY `companyid` (`companyid`),
  KEY `profileid` (`profileid`),
  CONSTRAINT `importcolumn_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `importcolumn_ibfk_2` FOREIGN KEY (`profileid`) REFERENCES `importprofile` (`profileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `importprofile` */

CREATE TABLE `importprofile` (
  `companyid` int(11) NOT NULL,
  `label` varchar(100) NOT NULL,
  `profileid` int(11) NOT NULL,
  `profiletype` int(11) NOT NULL,
  `skipfirstrow` tinyint(4) NOT NULL,
  `userid` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`profileid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  CONSTRAINT `importprofile_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `importprofile_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `incominginvoice` */

CREATE TABLE `incominginvoice` (
  `amountgross` decimal(10,2) DEFAULT NULL,
  `amountnet` decimal(10,2) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `currencyid` int(11) NOT NULL DEFAULT '0',
  `ininvoiceid` int(11) NOT NULL DEFAULT '0',
  `invoicedate` int(11) DEFAULT NULL,
  `invoicenumber` varchar(30) NOT NULL DEFAULT '',
  `notesid` int(11) DEFAULT NULL,
  `openamount` decimal(8,2) DEFAULT NULL,
  `paiduntil` int(11) DEFAULT NULL,
  `receiptdate` int(11) DEFAULT NULL,
  `supplierid` int(11) NOT NULL DEFAULT '0',
  `tobepaiduntil` int(11) DEFAULT NULL,
  `type` smallint(6) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ininvoiceid`),
  KEY `companyid` (`companyid`),
  KEY `currencyid` (`currencyid`),
  KEY `supplierid` (`supplierid`),
  KEY `notesid` (`notesid`),
  CONSTRAINT `incominginvoice_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `incominginvoice_ibfk_2` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`),
  CONSTRAINT `incominginvoice_ibfk_3` FOREIGN KEY (`supplierid`) REFERENCES `supplier` (`supplierid`),
  CONSTRAINT `incominginvoice_ibfk_4` FOREIGN KEY (`notesid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `incomingpayment` */

CREATE TABLE `incomingpayment` (
  `amount` decimal(10,2) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `ininvoiceid` int(11) NOT NULL DEFAULT '0',
  `notesid` int(11) DEFAULT NULL,
  `paydate` int(11) DEFAULT NULL,
  `paymentid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`paymentid`),
  KEY `ininvoiceid` (`ininvoiceid`),
  KEY `companyid` (`companyid`),
  KEY `notesid` (`notesid`),
  CONSTRAINT `incomingpayment_ibfk_1` FOREIGN KEY (`ininvoiceid`) REFERENCES `incominginvoice` (`ininvoiceid`),
  CONSTRAINT `incomingpayment_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `incomingpayment_ibfk_3` FOREIGN KEY (`notesid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoice` */

CREATE TABLE `invoice` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `creditnoteofid` int(11) DEFAULT NULL,
  `currencyid` int(11) NOT NULL DEFAULT '0',
  `documentid` int(11) DEFAULT NULL,
  `invoiceid` int(11) NOT NULL DEFAULT '0',
  `invoicedate` int(11) NOT NULL DEFAULT '0',
  `netgross` tinyint(4) DEFAULT NULL,
  `notesid` int(11) DEFAULT NULL,
  `number` varchar(200) NOT NULL DEFAULT '',
  `openamount` decimal(10,2) DEFAULT NULL,
  `payconditionid` int(11) NOT NULL DEFAULT '0',
  `paymentdate` int(11) NOT NULL DEFAULT '0',
  `reminderlevel` int(11) DEFAULT NULL,
  `reminderdate` int(11) DEFAULT NULL,
  `ruleformat` varchar(200) DEFAULT NULL,
  `rulenumber` int(11) DEFAULT NULL,
  `type` smallint(6) NOT NULL DEFAULT '0',
  `totalamountnet` decimal(10,2) DEFAULT NULL,
  `totalamountgross` decimal(10,2) DEFAULT NULL,
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`invoiceid`),
  UNIQUE KEY `number` (`number`,`companyid`),
  KEY `payconditionid` (`payconditionid`),
  KEY `currencyid` (`currencyid`),
  KEY `notesid` (`notesid`),
  KEY `documentid` (`documentid`),
  KEY `templateid` (`templateid`),
  KEY `companyid` (`companyid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `creditnoteofid` (`creditnoteofid`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`payconditionid`) REFERENCES `paycondition` (`payconditionid`),
  CONSTRAINT `invoice_ibfk_10` FOREIGN KEY (`creditnoteofid`) REFERENCES `invoice` (`invoiceid`),
  CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`),
  CONSTRAINT `invoice_ibfk_4` FOREIGN KEY (`notesid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoice_ibfk_5` FOREIGN KEY (`documentid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoice_ibfk_6` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `invoice_ibfk_7` FOREIGN KEY (`templateid`) REFERENCES `invoicetemplate` (`templateid`),
  CONSTRAINT `invoice_ibfk_8` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `invoice_ibfk_9` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicefreenum` */

CREATE TABLE `invoicefreenum` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freenumberid` int(11) NOT NULL DEFAULT '0',
  `invoicedate` int(11) DEFAULT NULL,
  `number` int(11) NOT NULL DEFAULT '0',
  `ruleformat` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`freenumberid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `invoicefreenum_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicepayment` */

CREATE TABLE `invoicepayment` (
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `creditnoteid` int(11) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `invoiceid` int(11) NOT NULL DEFAULT '0',
  `paydate` int(11) NOT NULL DEFAULT '0',
  `paymentid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`paymentid`),
  KEY `invoiceid` (`invoiceid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `creditnoteid` (`creditnoteid`),
  CONSTRAINT `invoicepayment_ibfk_1` FOREIGN KEY (`invoiceid`) REFERENCES `invoice` (`invoiceid`),
  CONSTRAINT `invoicepayment_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoicepayment_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `invoicepayment_ibfk_4` FOREIGN KEY (`creditnoteid`) REFERENCES `invoice` (`invoiceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoiceposition` */

CREATE TABLE `invoiceposition` (
  `accountid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contractid` int(11) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `discountvalue` decimal(10,2) DEFAULT NULL,
  `invoiceid` int(11) NOT NULL DEFAULT '0',
  `paystepid` int(11) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `number` int(11) NOT NULL DEFAULT '0',
  `positionid` int(11) NOT NULL DEFAULT '0',
  `quantity` decimal(10,2) NOT NULL DEFAULT '0.00',
  `salepositionid` int(11) DEFAULT NULL,
  `totalprice` decimal(10,2) DEFAULT NULL,
  `totalpricegross` decimal(10,2) DEFAULT NULL,
  `unit` varchar(40) DEFAULT NULL,
  `unitprice` decimal(10,2) DEFAULT NULL,
  `unitpricegross` decimal(10,2) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `vatid` int(11) NOT NULL DEFAULT '0',
  `vatrate` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`positionid`),
  KEY `vatid` (`vatid`),
  KEY `productid` (`productid`),
  KEY `invoiceid` (`invoiceid`),
  KEY `freetextid` (`freetextid`),
  KEY `accountid` (`accountid`),
  KEY `companyid` (`companyid`),
  KEY `contractid` (`contractid`),
  KEY `paystepid` (`paystepid`),
  KEY `salepositionid` (`salepositionid`),
  CONSTRAINT `invoiceposition_ibfk_1` FOREIGN KEY (`vatid`) REFERENCES `vat` (`vatid`),
  CONSTRAINT `invoiceposition_ibfk_10` FOREIGN KEY (`salepositionid`) REFERENCES `saleposition` (`salepositionid`),
  CONSTRAINT `invoiceposition_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `invoiceposition_ibfk_3` FOREIGN KEY (`invoiceid`) REFERENCES `invoice` (`invoiceid`),
  CONSTRAINT `invoiceposition_ibfk_4` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoiceposition_ibfk_5` FOREIGN KEY (`accountid`) REFERENCES `account` (`accountid`),
  CONSTRAINT `invoiceposition_ibfk_6` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `invoiceposition_ibfk_7` FOREIGN KEY (`contractid`) REFERENCES `productcontract` (`contractid`),
  CONSTRAINT `invoiceposition_ibfk_8` FOREIGN KEY (`paystepid`) REFERENCES `paymentstep` (`paystepid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicereminder` */

CREATE TABLE `invoicereminder` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `date` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `documentid` int(11) DEFAULT NULL,
  `invoiceid` int(11) NOT NULL DEFAULT '0',
  `reminderid` int(11) NOT NULL DEFAULT '0',
  `reminderlevelid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`reminderid`),
  KEY `invoiceid` (`invoiceid`),
  KEY `documentid` (`documentid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `companyid` (`companyid`),
  KEY `reminderlevelid` (`reminderlevelid`),
  CONSTRAINT `invoicereminder_ibfk_1` FOREIGN KEY (`invoiceid`) REFERENCES `invoice` (`invoiceid`),
  CONSTRAINT `invoicereminder_ibfk_2` FOREIGN KEY (`documentid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoicereminder_ibfk_3` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoicereminder_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `invoicereminder_ibfk_5` FOREIGN KEY (`reminderlevelid`) REFERENCES `reminderlevel` (`reminderlevelid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicetemplate` */

CREATE TABLE `invoicetemplate` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `title` varchar(150) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`templateid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `invoicetemplate_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicetext` */

CREATE TABLE `invoicetext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`languageid`,`templateid`),
  KEY `freetextid` (`freetextid`),
  KEY `templateid` (`templateid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `invoicetext_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `invoicetext_ibfk_2` FOREIGN KEY (`templateid`) REFERENCES `invoicetemplate` (`templateid`),
  CONSTRAINT `invoicetext_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `invoicetext_ibfk_4` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `invoicevat` */

CREATE TABLE `invoicevat` (
  `amount` decimal(13,2) NOT NULL DEFAULT '0.00',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `invoiceid` int(11) NOT NULL DEFAULT '0',
  `vatid` int(11) NOT NULL DEFAULT '0',
  `vatrate` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`invoiceid`,`vatid`),
  KEY `vatid` (`vatid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `invoicevat_ibfk_1` FOREIGN KEY (`vatid`) REFERENCES `vat` (`vatid`),
  CONSTRAINT `invoicevat_ibfk_2` FOREIGN KEY (`invoiceid`) REFERENCES `invoice` (`invoiceid`),
  CONSTRAINT `invoicevat_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `langtext` */

CREATE TABLE `langtext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(1) DEFAULT NULL,
  `langtextid` int(11) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `type` varchar(4) DEFAULT NULL,
  `text` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`langtextid`,`languageid`),
  KEY `companyid` (`companyid`),
  KEY `languageid` (`languageid`),
  CONSTRAINT `0_2801` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `0_2802` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `language` */

CREATE TABLE `language` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isdefault` smallint(6) DEFAULT NULL,
  `iso` varchar(5) DEFAULT NULL,
  `languageid` int(11) NOT NULL DEFAULT '0',
  `languagename` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`languageid`),
  KEY `companyid` (`companyid`),
  KEY `languagename` (`languagename`),
  CONSTRAINT `0_2804` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mail` */

CREATE TABLE `mail` (
  `attachment` tinyint(4) DEFAULT NULL,
  `bodyid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `completeemail` tinyint(4) DEFAULT NULL,
  `createcontact` tinyint(4) DEFAULT NULL,
  `folderid` int(11) DEFAULT NULL,
  `hidden` smallint(6) DEFAULT NULL,
  `isinout` int(11) DEFAULT NULL,
  `mailaccount` varchar(250) DEFAULT NULL,
  `mailid` int(11) NOT NULL DEFAULT '0',
  `mailfrom` varchar(250) NOT NULL DEFAULT '',
  `mailpersonalfrom` varchar(250) DEFAULT NULL,
  `messageidheader` varchar(250) DEFAULT NULL,
  `newemail` tinyint(4) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  `processtosent` tinyint(4) DEFAULT NULL,
  `saveemail` tinyint(4) DEFAULT NULL,
  `sentdatetime` bigint(20) DEFAULT NULL,
  `size` int(11) NOT NULL DEFAULT '0',
  `signatureid` int(11) DEFAULT NULL,
  `state` char(1) NOT NULL DEFAULT '',
  `subject` varchar(250) DEFAULT NULL,
  `uidl` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`mailid`),
  KEY `bodyid` (`bodyid`),
  KEY `companyid` (`companyid`),
  KEY `folderid` (`folderid`),
  KEY `signatureid` (`signatureid`),
  CONSTRAINT `mail_ibfk_1` FOREIGN KEY (`bodyid`) REFERENCES `body` (`bodyid`),
  CONSTRAINT `mail_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mail_ibfk_3` FOREIGN KEY (`folderid`) REFERENCES `folder` (`folderid`),
  CONSTRAINT `mail_ibfk_4` FOREIGN KEY (`signatureid`) REFERENCES `signature` (`signatureid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `mailaccerror` */

CREATE TABLE `mailaccerror` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `errortype` smallint(6) NOT NULL DEFAULT '0',
  `mailaccerrorid` int(11) NOT NULL DEFAULT '0',
  `mailaccountid` int(11) DEFAULT NULL,
  `timeerror` bigint(20) NOT NULL DEFAULT '0',
  `usermailid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`mailaccerrorid`),
  KEY `companyid` (`companyid`),
  KEY `mailaccountid` (`mailaccountid`),
  KEY `usermailid` (`usermailid`),
  CONSTRAINT `mailaccerror_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailaccerror_ibfk_2` FOREIGN KEY (`mailaccountid`) REFERENCES `mailaccount` (`mailaccountid`),
  CONSTRAINT `mailaccerror_ibfk_3` FOREIGN KEY (`usermailid`) REFERENCES `usermail` (`usermailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mailaccount` */

CREATE TABLE `mailaccount` (
  `accounttype` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `defaultaccount` smallint(6) DEFAULT NULL,
  `email` varchar(250) DEFAULT NULL,
  `lastdownloadtime` bigint(20) DEFAULT NULL,
  `login` varchar(50) DEFAULT NULL,
  `mailaccountid` int(11) NOT NULL DEFAULT '0',
  `password` varchar(50) DEFAULT NULL,
  `prefix` varchar(250) DEFAULT NULL,
  `servername` varchar(50) DEFAULT NULL,
  `serverport` varchar(50) DEFAULT NULL,
  `smtplogin` varchar(50) DEFAULT NULL,
  `smtpauth` tinyint(4) DEFAULT NULL,
  `smtppassword` varchar(50) DEFAULT NULL,
  `smtpport` varchar(50) DEFAULT NULL,
  `smtpserver` varchar(50) DEFAULT NULL,
  `smtpssl` tinyint(4) DEFAULT NULL,
  `usermailid` int(11) NOT NULL DEFAULT '0',
  `usepopconf` tinyint(4) DEFAULT NULL,
  `usesslconection` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`mailaccountid`),
  KEY `usermailid` (`usermailid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `mailaccount_ibfk_1` FOREIGN KEY (`usermailid`) REFERENCES `usermail` (`usermailid`),
  CONSTRAINT `mailaccount_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mailcontact` */

CREATE TABLE `mailcontact` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `email` varchar(250) NOT NULL DEFAULT '',
  `mailcontactid` int(11) NOT NULL DEFAULT '0',
  `mailid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`mailcontactid`),
  KEY `companyid` (`companyid`),
  KEY `mailid` (`mailid`),
  KEY `contactid` (`contactid`),
  CONSTRAINT `mailcontact_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailcontact_ibfk_2` FOREIGN KEY (`mailid`) REFERENCES `mail` (`mailid`),
  CONSTRAINT `mailcontact_ibfk_3` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`),
  CONSTRAINT `mailcontact_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailcontact_ibfk_5` FOREIGN KEY (`mailid`) REFERENCES `mail` (`mailid`),
  CONSTRAINT `mailcontact_ibfk_6` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `mailgroupaddr` */

CREATE TABLE `mailgroupaddr` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `groupaddrid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `usermailid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`groupaddrid`),
  KEY `companyid` (`companyid`),
  KEY `usermailid` (`usermailid`),
  CONSTRAINT `mailgroupaddr_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailgroupaddr_ibfk_2` FOREIGN KEY (`usermailid`) REFERENCES `usermail` (`usermailid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `mailrecipient` */

CREATE TABLE `mailrecipient` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `email` varchar(250) NOT NULL DEFAULT '',
  `mailid` int(11) NOT NULL DEFAULT '0',
  `personal` varchar(250) DEFAULT NULL,
  `recipientid` int(11) NOT NULL DEFAULT '0',
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`recipientid`),
  KEY `companyid` (`companyid`),
  KEY `mailid` (`mailid`),
  CONSTRAINT `mailrecipient_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailrecipient_ibfk_3` FOREIGN KEY (`mailid`) REFERENCES `mail` (`mailid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `mailuidltrack` */

CREATE TABLE `mailuidltrack` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `mailaccountid` int(11) NOT NULL DEFAULT '0',
  `uidl` varchar(250) DEFAULT NULL,
  `uidltrackid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uidltrackid`),
  KEY `companyid` (`companyid`),
  KEY `mailaccountid` (`mailaccountid`),
  CONSTRAINT `mailuidltrack_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `mailuidltrack_ibfk_2` FOREIGN KEY (`mailaccountid`) REFERENCES `mailaccount` (`mailaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `office` */

CREATE TABLE `office` (
  `addressid` int(11) DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `employeeid` int(11) DEFAULT NULL,
  `officeid` int(11) NOT NULL DEFAULT '0',
  `officename` varchar(15) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`officeid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `employeeid` (`employeeid`),
  CONSTRAINT `0_2806` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `0_2807` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2808` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `paycondition` */

CREATE TABLE `paycondition` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `conditionname` varchar(40) DEFAULT NULL,
  `conditiontextid1` int(11) DEFAULT NULL,
  `conditiontextid2` int(11) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `paydays` smallint(6) DEFAULT NULL,
  `paydaysdisc` smallint(6) DEFAULT NULL,
  `payconditionid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`payconditionid`),
  KEY `companyid` (`companyid`),
  KEY `conditiontextid1` (`conditiontextid1`),
  KEY `conditiontextid2` (`conditiontextid2`),
  CONSTRAINT `0_2810` FOREIGN KEY (`conditiontextid2`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `0_2811` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2812` FOREIGN KEY (`conditiontextid1`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `payconditiontext` */

CREATE TABLE `payconditiontext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `payconditionid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`languageid`,`payconditionid`),
  KEY `payconditionid` (`payconditionid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `payconditiontext_ibfk_1` FOREIGN KEY (`payconditionid`) REFERENCES `paycondition` (`payconditionid`),
  CONSTRAINT `payconditiontext_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `payconditiontext_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `payconditiontext_ibfk_4` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `paymentstep` */

CREATE TABLE `paymentstep` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contractid` int(11) NOT NULL DEFAULT '0',
  `payamount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `paydate` int(11) DEFAULT NULL,
  `paystepid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`paystepid`),
  KEY `companyid` (`companyid`),
  KEY `contractid` (`contractid`),
  CONSTRAINT `paymentstep_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `paymentstep_ibfk_2` FOREIGN KEY (`contractid`) REFERENCES `productcontract` (`contractid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `paymorality` */

CREATE TABLE `paymorality` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `paymoralityid` int(11) NOT NULL DEFAULT '0',
  `paymoralityname` varchar(40) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`paymoralityid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2814` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `persontype` */

CREATE TABLE `persontype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `persontypeid` int(11) NOT NULL DEFAULT '0',
  `persontypename` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`persontypeid`),
  KEY `persontypeid` (`companyid`),
  CONSTRAINT `0_2816` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pricing` */

CREATE TABLE `pricing` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `productid` int(11) NOT NULL DEFAULT '0',
  `price` decimal(10,3) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`productid`,`quantity`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  CONSTRAINT `0_2885` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2886` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `priority` */

CREATE TABLE `priority` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `priorityid` int(11) NOT NULL DEFAULT '0',
  `priorityname` varchar(20) DEFAULT NULL,
  `sequence` smallint(6) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`priorityid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `langtextid_2` (`langtextid`),
  CONSTRAINT `0_2818` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `priority_ibfk_1` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `priority_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `processpriority` */

CREATE TABLE `processpriority` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `priorityid` int(11) NOT NULL DEFAULT '0',
  `priorityname` varchar(40) DEFAULT NULL,
  `sequence` smallint(6) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`priorityid`),
  KEY `company` (`companyid`),
  CONSTRAINT `processpriority_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `processstatus` */

CREATE TABLE `processstatus` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isfinal` tinyint(4) DEFAULT NULL,
  `statusid` int(11) NOT NULL DEFAULT '0',
  `statusname` varchar(80) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`statusid`),
  KEY `company` (`companyid`),
  CONSTRAINT `processstatus_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `prodsupplier` */

CREATE TABLE `prodsupplier` (
  `active` tinyint(1) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `discount` decimal(10,3) DEFAULT NULL,
  `partnumber` varchar(20) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `supplierid` int(11) NOT NULL DEFAULT '0',
  `unitid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`productid`,`supplierid`),
  KEY `companyid` (`companyid`),
  KEY `supplierid` (`supplierid`),
  KEY `productid` (`productid`),
  KEY `unitid` (`unitid`),
  KEY `supplierid_2` (`supplierid`,`contactpersonid`),
  CONSTRAINT `0_22454` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_22455` FOREIGN KEY (`supplierid`) REFERENCES `supplier` (`supplierid`),
  CONSTRAINT `0_22456` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `0_22457` FOREIGN KEY (`unitid`) REFERENCES `productunit` (`unitid`),
  CONSTRAINT `prodsupplier_ibfk_1` FOREIGN KEY (`supplierid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `product` */

CREATE TABLE `product` (
  `accountid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `currentversion` varchar(40) DEFAULT NULL,
  `descriptionid` int(11) DEFAULT NULL,
  `langtextid` int(11) DEFAULT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `pricegross` decimal(10,2) DEFAULT NULL,
  `productgroupid` int(11) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `productname` varchar(80) DEFAULT NULL,
  `productnumber` varchar(40) DEFAULT NULL,
  `producttypeid` int(11) DEFAULT NULL,
  `unitid` int(11) DEFAULT NULL,
  `vatid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`productid`),
  KEY `companyid` (`companyid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `productgroupid` (`productgroupid`),
  KEY `producttypeid` (`producttypeid`),
  KEY `unitid` (`unitid`),
  KEY `accountid` (`accountid`),
  KEY `langtextid` (`langtextid`),
  KEY `vatid` (`vatid`),
  CONSTRAINT `0_2959` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2960` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `0_2961` FOREIGN KEY (`productgroupid`) REFERENCES `productgroup` (`groupid`),
  CONSTRAINT `0_2962` FOREIGN KEY (`producttypeid`) REFERENCES `producttype` (`typeid`),
  CONSTRAINT `0_2963` FOREIGN KEY (`unitid`) REFERENCES `productunit` (`unitid`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`accountid`) REFERENCES `account` (`accountid`),
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `product_ibfk_3` FOREIGN KEY (`vatid`) REFERENCES `vat` (`vatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `productcontract` */

CREATE TABLE `productcontract` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `amountype` tinyint(4) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `contractenddate` int(11) DEFAULT NULL,
  `contractid` int(11) NOT NULL DEFAULT '0',
  `contractnumber` varchar(40) DEFAULT NULL,
  `contracttypeid` int(11) NOT NULL DEFAULT '0',
  `currencyid` int(11) NOT NULL DEFAULT '0',
  `daystoremind` int(11) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `grouping` varchar(50) DEFAULT NULL,
  `installment` int(11) DEFAULT NULL,
  `invoiceduntil` int(11) DEFAULT NULL,
  `matchcalperiod` smallint(6) DEFAULT NULL,
  `netgross` tinyint(4) DEFAULT NULL,
  `noteid` int(11) DEFAULT NULL,
  `openamount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `orderdate` int(11) DEFAULT NULL,
  `payconditionid` int(11) DEFAULT NULL,
  `paymethod` smallint(6) NOT NULL DEFAULT '0',
  `payperiod` tinyint(4) DEFAULT NULL,
  `paystartdate` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `remindertime` bigint(20) DEFAULT NULL,
  `salepositionid` int(11) DEFAULT NULL,
  `sellerid` int(11) DEFAULT NULL,
  `vatid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contractid`),
  KEY `currencyid` (`currencyid`),
  KEY `vatid` (`vatid`),
  KEY `contracttypeid` (`contracttypeid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `salepositionid` (`salepositionid`),
  KEY `sellerid` (`sellerid`),
  KEY `payconditionid` (`payconditionid`),
  KEY `addressid_2` (`addressid`,`contactpersonid`),
  KEY `noteid` (`noteid`),
  CONSTRAINT `productcontract_ibfk_1` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`),
  CONSTRAINT `productcontract_ibfk_10` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `productcontract_ibfk_11` FOREIGN KEY (`noteid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `productcontract_ibfk_2` FOREIGN KEY (`vatid`) REFERENCES `vat` (`vatid`),
  CONSTRAINT `productcontract_ibfk_4` FOREIGN KEY (`contracttypeid`) REFERENCES `contracttype` (`contracttypeid`),
  CONSTRAINT `productcontract_ibfk_5` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `productcontract_ibfk_6` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `productcontract_ibfk_7` FOREIGN KEY (`salepositionid`) REFERENCES `saleposition` (`salepositionid`),
  CONSTRAINT `productcontract_ibfk_8` FOREIGN KEY (`sellerid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `productcontract_ibfk_9` FOREIGN KEY (`payconditionid`) REFERENCES `paycondition` (`payconditionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `productgroup` */

CREATE TABLE `productgroup` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `groupid` int(11) NOT NULL DEFAULT '0',
  `groupname` varchar(80) DEFAULT NULL,
  `parentgroupid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`groupid`),
  KEY `companyid` (`companyid`),
  KEY `parentgroupid` (`parentgroupid`),
  CONSTRAINT `0_2965` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2966` FOREIGN KEY (`parentgroupid`) REFERENCES `productgroup` (`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `productpicture` */

CREATE TABLE `productpicture` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(80) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `size` int(11) DEFAULT NULL,
  `uploaddate` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`productid`,`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  KEY `freetextid` (`freetextid`),
  CONSTRAINT `0_2972` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2973` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `0_2974` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `producttext` */

CREATE TABLE `producttext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `productid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`languageid`,`productid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  CONSTRAINT `producttext_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `producttext_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `producttext_ibfk_3` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `producttext_ibfk_4` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `producttype` */

CREATE TABLE `producttype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `typeid` int(11) NOT NULL DEFAULT '0',
  `typename` varchar(80) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`typeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2968` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `productunit` */

CREATE TABLE `productunit` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `unitid` int(11) NOT NULL DEFAULT '0',
  `unitname` varchar(80) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`unitid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2970` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `project` */

CREATE TABLE `project` (
  `accountid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `descriptionid` int(11) DEFAULT NULL,
  `enddate` int(11) DEFAULT NULL,
  `hastimelimit` tinyint(4) NOT NULL DEFAULT '0',
  `plannedinvoice` decimal(6,1) DEFAULT NULL,
  `plannednoinvoice` decimal(6,1) DEFAULT NULL,
  `projectid` int(11) NOT NULL DEFAULT '0',
  `projectname` varchar(80) NOT NULL DEFAULT '',
  `responsibleid` int(11) NOT NULL DEFAULT '0',
  `startdate` int(11) NOT NULL DEFAULT '0',
  `status` smallint(6) NOT NULL DEFAULT '0',
  `tobeinvoiced` smallint(6) NOT NULL DEFAULT '0',
  `totalinvoice` decimal(6,1) DEFAULT NULL,
  `totalnoinvoice` decimal(6,1) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`projectid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `companyid` (`companyid`),
  KEY `customerid` (`customerid`,`contactpersonid`),
  KEY `responsibleid` (`responsibleid`),
  KEY `accountid` (`accountid`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `project_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `project_ibfk_3` FOREIGN KEY (`customerid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `project_ibfk_4` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerid`),
  CONSTRAINT `project_ibfk_5` FOREIGN KEY (`responsibleid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `project_ibfk_6` FOREIGN KEY (`accountid`) REFERENCES `account` (`accountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `projectactivity` */

CREATE TABLE `projectactivity` (
  `activityname` varchar(80) NOT NULL DEFAULT '',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `projectactivityid` int(11) NOT NULL DEFAULT '0',
  `projectid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`projectactivityid`),
  KEY `companyid` (`companyid`),
  KEY `projectid` (`projectid`),
  CONSTRAINT `projectactivity_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `projectactivity_ibfk_2` FOREIGN KEY (`projectid`) REFERENCES `project` (`projectid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `projectassignee` */

CREATE TABLE `projectassignee` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `permission` tinyint(4) NOT NULL DEFAULT '0',
  `projectid` int(11) NOT NULL DEFAULT '0',
  `addressid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`projectid`,`addressid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `projectassignee_ibfk_1` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `projectassignee_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `projectassignee_ibfk_3` FOREIGN KEY (`projectid`) REFERENCES `project` (`projectid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `projecttime` */

CREATE TABLE `projecttime` (
  `assigneeid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `confirmedbyid` int(11) DEFAULT NULL,
  `date` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `projectactivityid` int(11) DEFAULT NULL,
  `projectid` int(11) NOT NULL DEFAULT '0',
  `status` smallint(6) NOT NULL DEFAULT '0',
  `subprojectid` int(11) DEFAULT NULL,
  `timeid` int(11) NOT NULL DEFAULT '0',
  `time` decimal(3,1) NOT NULL DEFAULT '0.0',
  `tobeinvoiced` tinyint(4) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`timeid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  KEY `confirmedbyid` (`confirmedbyid`),
  KEY `projectid` (`projectid`),
  KEY `projectactivityid` (`projectactivityid`),
  KEY `subprojectid` (`subprojectid`),
  KEY `assigneeid` (`assigneeid`),
  CONSTRAINT `projecttime_ibfk_1` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `projecttime_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `projecttime_ibfk_3` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `projecttime_ibfk_4` FOREIGN KEY (`confirmedbyid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `projecttime_ibfk_5` FOREIGN KEY (`projectid`) REFERENCES `project` (`projectid`),
  CONSTRAINT `projecttime_ibfk_6` FOREIGN KEY (`projectactivityid`) REFERENCES `projectactivity` (`projectactivityid`),
  CONSTRAINT `projecttime_ibfk_7` FOREIGN KEY (`subprojectid`) REFERENCES `subproject` (`subprojectid`),
  CONSTRAINT `projecttime_ibfk_8` FOREIGN KEY (`assigneeid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `recent` */

CREATE TABLE `recent` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) DEFAULT NULL,
  `recentid` int(11) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`,`addressid`),
  KEY `companyid` (`companyid`),
  KEY `addressid` (`addressid`),
  KEY `userid` (`userid`),
  CONSTRAINT `0_2820` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `0_2821` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2822` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `recipaddress` */

CREATE TABLE `recipaddress` (
  `addressid` int(11) DEFAULT NULL,
  `contactpersonid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `recipaddressid` int(11) NOT NULL DEFAULT '0',
  `recipientid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`recipaddressid`),
  KEY `companyid` (`companyid`),
  KEY `recipientid` (`recipientid`),
  CONSTRAINT `recipaddress_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `recipaddress_ibfk_2` FOREIGN KEY (`recipientid`) REFERENCES `mailrecipient` (`recipientid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `recurexception` */

CREATE TABLE `recurexception` (
  `appointmentid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `datevalue` int(11) DEFAULT NULL,
  `recurexceptionid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`recurexceptionid`),
  KEY `appointmentid` (`appointmentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `recurexception_ibfk_1` FOREIGN KEY (`appointmentid`) REFERENCES `recurrence` (`appointmentid`),
  CONSTRAINT `recurexception_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `recurrence` */

CREATE TABLE `recurrence` (
  `appointmentid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `recurevery` int(11) DEFAULT NULL,
  `rangetype` int(11) DEFAULT NULL,
  `rangevalue` int(11) DEFAULT NULL,
  `ruletype` int(11) DEFAULT NULL,
  `rulevalue` varchar(200) DEFAULT NULL,
  `rulevaluetype` int(11) DEFAULT NULL,
  PRIMARY KEY (`appointmentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `recurrence_ibfk_1` FOREIGN KEY (`appointmentid`) REFERENCES `appointment` (`appointmentid`),
  CONSTRAINT `recurrence_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `relationtype` */

CREATE TABLE `relationtype` (
  `companyid` int(10) NOT NULL DEFAULT '0',
  `relationtypeid` int(11) NOT NULL DEFAULT '0',
  `relationname` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`relationtypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2824` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2825` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `reminder` */

CREATE TABLE `reminder` (
  `appointmentid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `data` varchar(20) DEFAULT NULL,
  `nexttime` bigint(20) DEFAULT NULL,
  `remindertype` int(11) DEFAULT NULL,
  `timebefore` int(11) DEFAULT NULL,
  PRIMARY KEY (`appointmentid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `reminder_ibfk_2` FOREIGN KEY (`appointmentid`) REFERENCES `appointment` (`appointmentid`),
  CONSTRAINT `reminder_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `reminderlevel` */

CREATE TABLE `reminderlevel` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `fee` decimal(5,2) DEFAULT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL DEFAULT '',
  `numberofdays` int(11) NOT NULL DEFAULT '0',
  `reminderlevelid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`reminderlevelid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `reminderlevel_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `remindertext` */

CREATE TABLE `remindertext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `reminderlevelid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`languageid`,`reminderlevelid`),
  KEY `freetextid` (`freetextid`),
  KEY `companyid` (`companyid`),
  KEY `reminderlevelid` (`reminderlevelid`),
  CONSTRAINT `remindertext_ibfk_1` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `remindertext_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `remindertext_ibfk_3` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `remindertext_ibfk_4` FOREIGN KEY (`reminderlevelid`) REFERENCES `reminderlevel` (`reminderlevelid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `report` */

CREATE TABLE `report` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `employeeid` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `filterrule` varchar(100) DEFAULT NULL COMMENT 'store the filter rule to advanced filter',
  `initialtableref` varchar(50) NOT NULL DEFAULT '' COMMENT 'store the table id of the structure of the xml file, is the initial functionality of the report ',
  `module` varchar(50) NOT NULL DEFAULT '' COMMENT 'store the module associated the report',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT 'name report',
  `pageorientation` smallint(6) NOT NULL DEFAULT '1' COMMENT 'report page orientation (1=Portrait,2=Landscape)',
  `pagesize` smallint(6) NOT NULL DEFAULT '1' COMMENT 'report page size (1=A4, 2=letter, 3=legal,.....)',
  `reportformat` varchar(15) NOT NULL DEFAULT 'pdf' COMMENT 'report document extencion format (pdf,rtf,csv,xls,...)',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'store the status of the report: public or private',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'type of the report: simple, resumen or matrix report',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`reportid`),
  KEY `companyid` (`companyid`),
  KEY `employeeid` (`employeeid`),
  KEY `descriptionid` (`descriptionid`),
  CONSTRAINT `report_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `report_ibfk_2` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `report_ibfk_3` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `reportchart` */

CREATE TABLE `reportchart` (
  `categoryid` int(11) DEFAULT NULL COMMENT 'relation with column group, represent an category',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isenable` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'enable or disable this chart in the report (0=false, 1= true)',
  `orientation` smallint(6) DEFAULT NULL COMMENT 'axis orientation to charts. (x orientation or y orientation)',
  `position` smallint(6) NOT NULL DEFAULT '0' COMMENT 'position of the chart in the report (header, footer,....)',
  `reportchartid` int(11) NOT NULL DEFAULT '0',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `serieid` int(11) DEFAULT NULL COMMENT 'relation with column group, represent an serie in the chart ',
  `showonlychart` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'represen an bolean value to show only chart in the report(0=false, 1=true)',
  `title` varchar(150) DEFAULT NULL COMMENT 'title of the chart',
  `type` smallint(6) NOT NULL DEFAULT '0' COMMENT 'type of chart (bar, bubble, pie,.....)',
  `version` int(11) DEFAULT NULL,
  `xaxislabel` varchar(150) DEFAULT NULL COMMENT 'x axis label to chart type xy',
  `xvalueid` int(11) DEFAULT NULL COMMENT 'relation with reporttotalize, represent an totalizer value in the axis x',
  `yaxislabel` varchar(150) DEFAULT NULL COMMENT 'y axis label to chart type xy',
  `yvalueid` int(11) DEFAULT NULL COMMENT 'relation with reporttotalize, represent an totalizer value in the axis y',
  `zvalueid` int(11) DEFAULT NULL COMMENT 'relation with reporttotalize, represent an totalizer value in the axis z',
  PRIMARY KEY (`reportchartid`),
  KEY `reportid` (`reportid`),
  KEY `serieid` (`serieid`),
  KEY `categoryid` (`categoryid`),
  KEY `xvalueid` (`xvalueid`),
  KEY `yvalueid` (`yvalueid`),
  KEY `zvalueid` (`zvalueid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `reportchart_ibfk_1` FOREIGN KEY (`reportid`) REFERENCES `report` (`reportid`),
  CONSTRAINT `reportchart_ibfk_2` FOREIGN KEY (`serieid`) REFERENCES `columngroup` (`columngroupid`),
  CONSTRAINT `reportchart_ibfk_3` FOREIGN KEY (`categoryid`) REFERENCES `columngroup` (`columngroupid`),
  CONSTRAINT `reportchart_ibfk_4` FOREIGN KEY (`xvalueid`) REFERENCES `reporttotalize` (`reporttotalizeid`),
  CONSTRAINT `reportchart_ibfk_5` FOREIGN KEY (`yvalueid`) REFERENCES `reporttotalize` (`reporttotalizeid`),
  CONSTRAINT `reportchart_ibfk_6` FOREIGN KEY (`zvalueid`) REFERENCES `reporttotalize` (`reporttotalizeid`),
  CONSTRAINT `reportchart_ibfk_7` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `reportcolumn` */

CREATE TABLE `reportcolumn` (
  `categoryid` int(11) DEFAULT NULL,
  `columnorder` tinyint(1) DEFAULT NULL COMMENT 'how be oder this column. defined how constant: ie. (1=ASCENDENT, 0=DESCENDENT)',
  `columnref` varchar(70) NOT NULL DEFAULT '' COMMENT 'store column name of the data base',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `istotalizer` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'if this column is toltalizer. defined how constan value (0=false 1=true)',
  `label` varchar(100) DEFAULT NULL COMMENT 'store column name that show in the report',
  `path` longblob NOT NULL COMMENT 'path to get at the data base column ',
  `reportcolumnid` int(11) NOT NULL DEFAULT '0',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `sequence` smallint(6) NOT NULL DEFAULT '0' COMMENT 'denote the position of the column in the report view',
  `tableref` varchar(50) NOT NULL DEFAULT '' COMMENT 'store table name, that contain column report',
  `type` smallint(6) NOT NULL DEFAULT '0' COMMENT 'store the type of the column report, defined how constant: ie. (1=int, 2= string, .......)',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`reportcolumnid`),
  KEY `reportid` (`reportid`),
  KEY `companyid` (`companyid`),
  KEY `categoryid` (`categoryid`),
  CONSTRAINT `reportcolumn_ibfk_1` FOREIGN KEY (`reportid`) REFERENCES `report` (`reportid`),
  CONSTRAINT `reportcolumn_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `reportcolumn_ibfk_3` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `reportfilter` */

CREATE TABLE `reportfilter` (
  `aliascondition` varchar(5) DEFAULT NULL COMMENT 'store the alias of an filter: ie F1, help to create the rule of conditions in advanced filters ( F1 AND ( F2 OR F3) )',
  `categoryid` int(11) DEFAULT NULL,
  `columnref` varchar(70) NOT NULL DEFAULT '' COMMENT 'column name at the that be apply the filter',
  `columntype` smallint(6) NOT NULL DEFAULT '0' COMMENT 'type of the filter column, defined how constant: ie (1=date, 2=int, 3=string)',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filtertype` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'filter type: db filter or normal filter. i.e. (0 = filter with constant value, 1 = filter with db value)',
  `isparameter` tinyint(4) NOT NULL DEFAULT '0',
  `label` varchar(150) DEFAULT NULL,
  `operator` smallint(6) NOT NULL DEFAULT '0' COMMENT 'operator to create the condition of the filter, defined how constant : ie. (1=EQUAL, 2=BETTWEN, 3=GREATER OR EQUAL)',
  `path` longblob NOT NULL COMMENT 'path to get at the data base column',
  `reportfilterid` int(11) NOT NULL DEFAULT '0',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `sequence` smallint(6) NOT NULL DEFAULT '0' COMMENT 'sequence of create filters, 1,2,3,4,.......',
  `tableref` varchar(50) NOT NULL DEFAULT '' COMMENT 'store table name, table that contain the filter column  ',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`reportfilterid`),
  KEY `reportid` (`reportid`),
  KEY `companyid` (`companyid`),
  KEY `categoryid` (`categoryid`),
  CONSTRAINT `reportfilter_ibfk_1` FOREIGN KEY (`reportid`) REFERENCES `report` (`reportid`),
  CONSTRAINT `reportfilter_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `reportfilter_ibfk_3` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `reportrole` */

CREATE TABLE `reportrole` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `roleid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`reportid`,`roleid`),
  KEY `companyid` (`companyid`),
  KEY `roleid` (`roleid`),
  CONSTRAINT `reportrole_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `reportrole_ibfk_2` FOREIGN KEY (`reportid`) REFERENCES `report` (`reportid`),
  CONSTRAINT `reportrole_ibfk_3` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `reporttotalize` */

CREATE TABLE `reporttotalize` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `formula` varchar(250) DEFAULT NULL COMMENT 'store the custom formula for totalize defined for the user',
  `name` varchar(70) DEFAULT NULL COMMENT 'name for totalize',
  `reportid` int(11) NOT NULL DEFAULT '0',
  `reporttotalizeid` int(11) NOT NULL DEFAULT '0',
  `type` smallint(6) NOT NULL DEFAULT '0' COMMENT 'store the totalize type. defined how constant: ie. (1=avg, 2= sum, 3=largest value, 4=smallest value, 5=custom)',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`reporttotalizeid`),
  KEY `reportid` (`reportid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `reporttotalize_ibfk_1` FOREIGN KEY (`reportid`) REFERENCES `report` (`reportid`),
  CONSTRAINT `reporttotalize_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `role` */

CREATE TABLE `role` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(4) DEFAULT NULL,
  `descriptionid` int(11) DEFAULT NULL,
  `roleid` int(11) NOT NULL DEFAULT '0',
  `rolename` varchar(80) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  KEY `IX_Relationship25` (`companyid`),
  KEY `description` (`descriptionid`),
  CONSTRAINT `role_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `role_ibfk_2` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sale` */

CREATE TABLE `sale` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) DEFAULT NULL,
  `contactpersonid` int(11) DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `customerid` int(11) NOT NULL DEFAULT '0',
  `freetextid` int(11) DEFAULT NULL,
  `netgross` smallint(6) DEFAULT NULL,
  `processid` int(11) DEFAULT NULL,
  `saledate` int(11) NOT NULL DEFAULT '0',
  `saleid` int(11) NOT NULL DEFAULT '0',
  `sellerid` int(11) NOT NULL DEFAULT '0',
  `title` varchar(200) NOT NULL DEFAULT '',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`saleid`),
  KEY `customerid` (`customerid`,`contactpersonid`),
  KEY `companyid` (`companyid`),
  KEY `processid` (`processid`),
  KEY `freetextid` (`freetextid`),
  KEY `sellerid` (`sellerid`),
  KEY `contactid` (`contactid`,`processid`),
  KEY `currencyid` (`currencyid`),
  CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerid`),
  CONSTRAINT `sale_ibfk_2` FOREIGN KEY (`customerid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `sale_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `sale_ibfk_4` FOREIGN KEY (`processid`) REFERENCES `salesprocess` (`processid`),
  CONSTRAINT `sale_ibfk_5` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `sale_ibfk_6` FOREIGN KEY (`sellerid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `sale_ibfk_7` FOREIGN KEY (`contactid`, `processid`) REFERENCES `action` (`contactid`, `processid`),
  CONSTRAINT `sale_ibfk_8` FOREIGN KEY (`currencyid`) REFERENCES `currency` (`currencyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `saleposition` */

CREATE TABLE `saleposition` (
  `active` tinyint(4) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `customerid` int(11) NOT NULL DEFAULT '0',
  `deliverydate` int(11) DEFAULT NULL,
  `discount` decimal(13,2) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `paymethod` smallint(6) DEFAULT NULL,
  `productid` int(11) NOT NULL DEFAULT '0',
  `quantity` decimal(10,2) DEFAULT NULL,
  `salepositionid` int(11) NOT NULL DEFAULT '0',
  `saleid` int(11) DEFAULT NULL,
  `serial` varchar(100) DEFAULT NULL,
  `totalprice` decimal(10,2) DEFAULT NULL,
  `totalpricegross` decimal(10,2) DEFAULT NULL,
  `unitid` int(11) DEFAULT NULL,
  `unitprice` decimal(10,2) DEFAULT NULL,
  `unitpricegross` decimal(10,2) DEFAULT NULL,
  `vatid` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `versionnumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`salepositionid`),
  KEY `freetextid` (`freetextid`),
  KEY `productid` (`productid`),
  KEY `saleid` (`saleid`),
  KEY `companyid` (`companyid`),
  KEY `customerid` (`customerid`,`contactpersonid`),
  KEY `unitid` (`unitid`),
  CONSTRAINT `saleposition_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerid`),
  CONSTRAINT `saleposition_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `saleposition_ibfk_3` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `saleposition_ibfk_4` FOREIGN KEY (`saleid`) REFERENCES `sale` (`saleid`),
  CONSTRAINT `saleposition_ibfk_6` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `saleposition_ibfk_7` FOREIGN KEY (`customerid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `saleposition_ibfk_8` FOREIGN KEY (`unitid`) REFERENCES `productunit` (`unitid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `salesprocess` */

CREATE TABLE `salesprocess` (
  `addressid` int(11) DEFAULT NULL,
  `campactivityid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `descriptionid` int(11) DEFAULT NULL,
  `employeeid` int(11) DEFAULT NULL,
  `enddate` int(11) DEFAULT NULL,
  `priorityid` int(11) DEFAULT NULL,
  `processid` int(11) NOT NULL DEFAULT '0',
  `processname` varchar(80) DEFAULT NULL,
  `probability` smallint(6) DEFAULT NULL,
  `statusid` int(11) DEFAULT NULL,
  `startdate` int(11) DEFAULT NULL,
  `value` decimal(13,2) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`processid`),
  KEY `address` (`addressid`),
  KEY `employee` (`employeeid`),
  KEY `freetext` (`descriptionid`),
  KEY `processpriority` (`priorityid`),
  KEY `status` (`statusid`),
  KEY `company` (`companyid`),
  KEY `campactivityid` (`campactivityid`),
  CONSTRAINT `salesprocess_ibfk_1` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `salesprocess_ibfk_2` FOREIGN KEY (`employeeid`) REFERENCES `employee` (`employeeid`),
  CONSTRAINT `salesprocess_ibfk_3` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `salesprocess_ibfk_4` FOREIGN KEY (`priorityid`) REFERENCES `processpriority` (`priorityid`),
  CONSTRAINT `salesprocess_ibfk_5` FOREIGN KEY (`statusid`) REFERENCES `processstatus` (`statusid`),
  CONSTRAINT `salesprocess_ibfk_6` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `salesprocess_ibfk_7` FOREIGN KEY (`campactivityid`) REFERENCES `campactivity` (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `salutation` */

CREATE TABLE `salutation` (
  `addresstextid` int(11) DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `lettertextid` int(11) DEFAULT NULL,
  `salutationid` int(11) NOT NULL DEFAULT '0',
  `salutationlabel` varchar(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`salutationid`),
  KEY `addresstextid` (`addresstextid`),
  KEY `companyid` (`companyid`),
  KEY `lettertextid` (`lettertextid`),
  KEY `salutationid` (`salutationid`),
  CONSTRAINT `0_2827` FOREIGN KEY (`lettertextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `0_2828` FOREIGN KEY (`addresstextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `0_2829` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scheduleduser` */

CREATE TABLE `scheduleduser` (
  `appointmentid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `scheduleduserid` int(11) NOT NULL DEFAULT '0',
  `taskid` int(11) DEFAULT NULL,
  `userid` int(11) NOT NULL,
  `usergroupid` int(11) DEFAULT NULL,
  PRIMARY KEY (`scheduleduserid`),
  KEY `taskid` (`taskid`),
  KEY `usergroupid` (`usergroupid`),
  KEY `appointmentid` (`appointmentid`),
  KEY `userid` (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `scheduleduser_ibfk_1` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`),
  CONSTRAINT `scheduleduser_ibfk_2` FOREIGN KEY (`usergroupid`) REFERENCES `usergroup` (`usergroupid`),
  CONSTRAINT `scheduleduser_ibfk_3` FOREIGN KEY (`appointmentid`) REFERENCES `appointment` (`appointmentid`),
  CONSTRAINT `scheduleduser_ibfk_4` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `scheduleduser_ibfk_5` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `scheduleraccess` */

CREATE TABLE `scheduleraccess` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `owneruserid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `permission` int(11) DEFAULT NULL,
  `privpermission` int(11) DEFAULT NULL,
  PRIMARY KEY (`owneruserid`,`userid`),
  KEY `userid` (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `scheduleraccess_ibfk_1` FOREIGN KEY (`owneruserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `scheduleraccess_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `scheduleraccess_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sequence` */

CREATE TABLE `sequence` (
  `name` varchar(250) NOT NULL DEFAULT '',
  `sequencenumber` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sequencerule` */

CREATE TABLE `sequencerule` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `format` varchar(150) NOT NULL DEFAULT '',
  `label` varchar(150) DEFAULT NULL,
  `lastnumber` int(11) DEFAULT NULL,
  `lastdate` int(11) DEFAULT NULL,
  `numberid` int(11) NOT NULL DEFAULT '0',
  `resettype` smallint(6) DEFAULT NULL,
  `startnumber` int(11) DEFAULT NULL,
  `type` smallint(6) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`numberid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `sequencerule_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `signature` */

CREATE TABLE `signature` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `isdefault` tinyint(1) NOT NULL DEFAULT '0',
  `mailaccountid` int(11) DEFAULT NULL,
  `htmlsignatureid` int(11) DEFAULT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `signatureid` int(11) NOT NULL DEFAULT '0',
  `usermailid` int(11) NOT NULL DEFAULT '0',
  `textsignatureid` int(11) DEFAULT NULL,
  PRIMARY KEY (`signatureid`),
  KEY `companyid` (`companyid`),
  KEY `usermailid` (`usermailid`),
  KEY `textsignatureid` (`textsignatureid`),
  KEY `htmlsignatureid` (`htmlsignatureid`),
  KEY `mailaccountid` (`mailaccountid`),
  CONSTRAINT `signature_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `signature_ibfk_2` FOREIGN KEY (`usermailid`) REFERENCES `usermail` (`usermailid`),
  CONSTRAINT `signature_ibfk_3` FOREIGN KEY (`textsignatureid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `signature_ibfk_4` FOREIGN KEY (`htmlsignatureid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `signature_ibfk_5` FOREIGN KEY (`mailaccountid`) REFERENCES `mailaccount` (`mailaccountid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `signatureimg` */

CREATE TABLE `signatureimg` (
  `companyid` int(11) NOT NULL,
  `imagestoreid` int(11) NOT NULL,
  `signatureid` int(11) NOT NULL,
  `signatureimgid` int(11) NOT NULL,
  PRIMARY KEY (`signatureimgid`),
  KEY `signatureid` (`signatureid`),
  KEY `imagestoreid` (`imagestoreid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `signatureimg_ibfk_1` FOREIGN KEY (`signatureid`) REFERENCES `signature` (`signatureid`),
  CONSTRAINT `signatureimg_ibfk_2` FOREIGN KEY (`imagestoreid`) REFERENCES `imagestore` (`imagestoreid`),
  CONSTRAINT `signatureimg_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `state` */

CREATE TABLE `state` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `stagetype` tinyint(4) NOT NULL DEFAULT '0',
  `sequence` int(11) DEFAULT NULL,
  `stateid` int(11) NOT NULL DEFAULT '0',
  `statename` varchar(40) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`stateid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  KEY `langtextid_2` (`langtextid`),
  CONSTRAINT `state_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `state_ibfk_2` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`),
  CONSTRAINT `state_ibfk_3` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `style` */

CREATE TABLE `style` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '',
  `styleid` int(11) NOT NULL DEFAULT '0',
  `stylesheetid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`styleid`),
  KEY `stylesheetid` (`stylesheetid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `style_ibfk_1` FOREIGN KEY (`stylesheetid`) REFERENCES `stylesheet` (`stylesheetid`),
  CONSTRAINT `style_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `styleattribute` */

CREATE TABLE `styleattribute` (
  `attributeid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `styleid` int(11) NOT NULL DEFAULT '0',
  `value` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`attributeid`),
  KEY `styleid` (`styleid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `styleattribute_ibfk_1` FOREIGN KEY (`styleid`) REFERENCES `style` (`styleid`),
  CONSTRAINT `styleattribute_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `stylesheet` */

CREATE TABLE `stylesheet` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `stylesheetid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`stylesheetid`),
  KEY `userid` (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `stylesheet_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `stylesheet_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `subproject` */

CREATE TABLE `subproject` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `projectid` int(11) NOT NULL DEFAULT '0',
  `subprojectname` varchar(80) NOT NULL DEFAULT '',
  `subprojectid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`subprojectid`),
  KEY `companyid` (`companyid`),
  KEY `projectid` (`projectid`),
  CONSTRAINT `subproject_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `subproject_ibfk_2` FOREIGN KEY (`projectid`) REFERENCES `project` (`projectid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `supplier` */

CREATE TABLE `supplier` (
  `branchid` int(11) DEFAULT NULL,
  `categoryid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `customernumber` varchar(20) DEFAULT NULL,
  `priorityid` int(11) DEFAULT NULL,
  `supplierid` int(11) NOT NULL DEFAULT '0',
  `suppliertypeid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`supplierid`),
  KEY `companyid` (`companyid`),
  KEY `branchid` (`branchid`),
  KEY `categoryid` (`categoryid`),
  KEY `priorityid` (`priorityid`),
  KEY `suppliertypeid` (`suppliertypeid`),
  CONSTRAINT `0_2832` FOREIGN KEY (`branchid`) REFERENCES `branch` (`branchid`),
  CONSTRAINT `0_2833` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryid`),
  CONSTRAINT `0_2834` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2835` FOREIGN KEY (`priorityid`) REFERENCES `priority` (`priorityid`),
  CONSTRAINT `0_2836` FOREIGN KEY (`suppliertypeid`) REFERENCES `suppliertype` (`suppliertypeid`),
  CONSTRAINT `supplier_ibfk_1` FOREIGN KEY (`supplierid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `suppliertype` */

CREATE TABLE `suppliertype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `suppliertypeid` int(11) NOT NULL DEFAULT '0',
  `suppliertypename` varchar(60) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`suppliertypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2838` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `supportattach` */

CREATE TABLE `supportattach` (
  `activityid` int(11) DEFAULT NULL,
  `articleid` int(11) DEFAULT NULL,
  `attachid` int(11) NOT NULL DEFAULT '0',
  `caseid` int(11) DEFAULT NULL,
  `comment` varchar(80) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `filename` varchar(80) DEFAULT NULL,
  `freetextid` int(11) NOT NULL DEFAULT '0',
  `size` int(11) DEFAULT NULL,
  `uploaddatetime` bigint(20) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`attachid`),
  KEY `companyid` (`companyid`),
  KEY `articleid` (`articleid`),
  KEY `caseid` (`caseid`),
  KEY `activityid` (`activityid`),
  KEY `freetextid` (`freetextid`),
  KEY `userid` (`userid`),
  CONSTRAINT `supportattach_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportattach_ibfk_2` FOREIGN KEY (`articleid`) REFERENCES `article` (`articleid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportattach_ibfk_3` FOREIGN KEY (`caseid`) REFERENCES `supportcase` (`caseid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportattach_ibfk_4` FOREIGN KEY (`activityid`) REFERENCES `caseactivity` (`activityid`),
  CONSTRAINT `supportattach_ibfk_5` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `supportattach_ibfk_6` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `supportcase` */

CREATE TABLE `supportcase` (
  `addressid` int(11) DEFAULT NULL,
  `caseid` int(11) NOT NULL DEFAULT '0',
  `casetitle` varchar(160) DEFAULT NULL,
  `casetypeid` int(11) NOT NULL DEFAULT '0',
  `closedate` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `expiredate` int(11) DEFAULT NULL,
  `descriptionid` int(11) NOT NULL DEFAULT '0',
  `keywords` varchar(40) DEFAULT NULL,
  `fromuserid` int(11) NOT NULL DEFAULT '0',
  `number` varchar(20) DEFAULT NULL,
  `openbyuserid` int(11) NOT NULL DEFAULT '0',
  `opendate` int(11) DEFAULT NULL,
  `priorityid` int(11) NOT NULL DEFAULT '0',
  `productid` int(11) DEFAULT NULL,
  `severityid` int(11) DEFAULT NULL,
  `stateid` int(11) NOT NULL DEFAULT '0',
  `totalhours` int(11) DEFAULT NULL,
  `touserid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  `worklevelid` int(11) DEFAULT NULL,
  PRIMARY KEY (`caseid`),
  KEY `companyid` (`companyid`),
  KEY `productid` (`productid`),
  KEY `touserid` (`touserid`),
  KEY `openbyuserid` (`openbyuserid`),
  KEY `fromuserid` (`fromuserid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `descriptionid` (`descriptionid`),
  KEY `priorityid` (`priorityid`),
  KEY `casetypeid` (`casetypeid`),
  KEY `severityid` (`severityid`),
  KEY `stateid` (`stateid`),
  KEY `worklevelid` (`worklevelid`),
  CONSTRAINT `supportcase_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_10` FOREIGN KEY (`casetypeid`) REFERENCES `casetype` (`casetypeid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_11` FOREIGN KEY (`severityid`) REFERENCES `caseseverity` (`severityid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_12` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_13` FOREIGN KEY (`worklevelid`) REFERENCES `caseworklevel` (`worklevelid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_3` FOREIGN KEY (`touserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_4` FOREIGN KEY (`openbyuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_5` FOREIGN KEY (`fromuserid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_6` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_7` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_8` FOREIGN KEY (`descriptionid`) REFERENCES `freetext` (`freetextid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `supportcase_ibfk_9` FOREIGN KEY (`priorityid`) REFERENCES `priority` (`priorityid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `supportcontact` */

CREATE TABLE `supportcontact` (
  `activityid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactid` int(11) NOT NULL DEFAULT '0',
  `caseid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`contactid`,`caseid`),
  KEY `companyid` (`companyid`),
  KEY `caseid` (`caseid`),
  KEY `activityid` (`activityid`),
  CONSTRAINT `supportcontact_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `supportcontact_ibfk_2` FOREIGN KEY (`caseid`) REFERENCES `supportcase` (`caseid`),
  CONSTRAINT `supportcontact_ibfk_3` FOREIGN KEY (`contactid`) REFERENCES `contact` (`contactid`),
  CONSTRAINT `supportcontact_ibfk_4` FOREIGN KEY (`activityid`) REFERENCES `caseactivity` (`activityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `supportuser` */

CREATE TABLE `supportuser` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `productid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`productid`,`userid`),
  KEY `companyid` (`companyid`),
  KEY `userid` (`userid`),
  CONSTRAINT `supportuser_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `supportuser_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`),
  CONSTRAINT `supportuser_ibfk_3` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `systemconstant` */

CREATE TABLE `systemconstant` (
  `description` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `resourcekey` varchar(40) DEFAULT NULL,
  `type` varchar(30) NOT NULL DEFAULT '',
  `value` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`name`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `systemfunction` */

CREATE TABLE `systemfunction` (
  `allowpermission` tinyint(4) DEFAULT NULL,
  `code` varchar(40) DEFAULT NULL,
  `description` varchar(80) DEFAULT NULL,
  `functionid` int(11) NOT NULL DEFAULT '0',
  `moduleid` int(11) NOT NULL DEFAULT '0',
  `namekey` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`functionid`),
  UNIQUE KEY `code` (`code`),
  KEY `IX_Relationship8` (`moduleid`),
  CONSTRAINT `systemfunction_ibfk_2` FOREIGN KEY (`moduleid`) REFERENCES `systemmodule` (`moduleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `systemmodule` */

CREATE TABLE `systemmodule` (
  `description` varchar(80) DEFAULT NULL,
  `moduleid` int(11) NOT NULL DEFAULT '0',
  `modulenamekey` varchar(80) DEFAULT NULL,
  `modulepath` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`moduleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `task` */

CREATE TABLE `task` (
  `addressid` int(11) DEFAULT NULL,
  `activityid` int(11) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `contactpersonid` int(11) DEFAULT NULL,
  `createdatetime` bigint(20) DEFAULT NULL,
  `expiredate` int(11) DEFAULT NULL,
  `expiretime` varchar(10) DEFAULT NULL,
  `expiredatetime` bigint(20) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  `percent` smallint(6) DEFAULT NULL,
  `priorityid` int(11) DEFAULT NULL,
  `processid` int(11) DEFAULT NULL,
  `startdate` int(11) DEFAULT NULL,
  `startdatetime` varchar(20) DEFAULT NULL,
  `starttime` varchar(10) DEFAULT NULL,
  `taskid` int(11) NOT NULL DEFAULT '0',
  `tasktypeid` int(11) DEFAULT NULL,
  `title` varchar(60) DEFAULT NULL,
  `updatedatetime` bigint(20) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  `status` int(11) DEFAULT NULL,
  `notification` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  KEY `priorityid` (`priorityid`),
  KEY `companyid` (`companyid`),
  KEY `freetextid` (`freetextid`),
  KEY `userid` (`userid`),
  KEY `tasktypeid` (`tasktypeid`),
  KEY `processid` (`processid`),
  KEY `addressid` (`addressid`,`contactpersonid`),
  KEY `addressid_2` (`addressid`,`processid`),
  KEY `activityid` (`activityid`),
  CONSTRAINT `task_ibfk_1` FOREIGN KEY (`priorityid`) REFERENCES `priority` (`priorityid`),
  CONSTRAINT `task_ibfk_10` FOREIGN KEY (`addressid`, `processid`) REFERENCES `salesprocess` (`addressid`, `processid`),
  CONSTRAINT `task_ibfk_11` FOREIGN KEY (`activityid`) REFERENCES `campactivity` (`activityid`),
  CONSTRAINT `task_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `task_ibfk_3` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `task_ibfk_4` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `task_ibfk_6` FOREIGN KEY (`tasktypeid`) REFERENCES `tasktype` (`tasktypeid`),
  CONSTRAINT `task_ibfk_7` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`),
  CONSTRAINT `task_ibfk_8` FOREIGN KEY (`processid`) REFERENCES `salesprocess` (`processid`),
  CONSTRAINT `task_ibfk_9` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tasktype` */

CREATE TABLE `tasktype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(20) NOT NULL DEFAULT '',
  `tasktypeid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`tasktypeid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `tasktype_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `telecom` */

CREATE TABLE `telecom` (
  `addressid` int(11) NOT NULL DEFAULT '0',
  `companyid` int(11) NOT NULL DEFAULT '0',
  `description` varchar(200) DEFAULT NULL,
  `contactpersonid` int(11) DEFAULT NULL,
  `predetermined` tinyint(1) DEFAULT '0',
  `telecomid` int(11) NOT NULL DEFAULT '0',
  `telecomnumber` varchar(100) DEFAULT NULL,
  `telecomtype` char(1) DEFAULT NULL,
  `telecomtypeid` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`telecomid`),
  KEY `addressid` (`addressid`),
  KEY `companyid` (`companyid`),
  KEY `telecomtypeid` (`telecomtypeid`),
  KEY `contactperson` (`addressid`,`contactpersonid`),
  CONSTRAINT `0_2840` FOREIGN KEY (`addressid`, `contactpersonid`) REFERENCES `contactperson` (`addressid`, `contactpersonid`),
  CONSTRAINT `0_2841` FOREIGN KEY (`telecomtypeid`) REFERENCES `telecomtype` (`telecomtypeid`),
  CONSTRAINT `0_2842` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2843` FOREIGN KEY (`addressid`) REFERENCES `address` (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `telecomtype` */

CREATE TABLE `telecomtype` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `langtextid` int(11) DEFAULT NULL,
  `telecomtypeid` int(11) NOT NULL DEFAULT '0',
  `telecomtypename` varchar(20) DEFAULT NULL,
  `telecomtypepos` smallint(6) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`telecomtypeid`),
  KEY `companyid` (`companyid`),
  KEY `langtextid` (`langtextid`),
  CONSTRAINT `0_2845` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `telecomtype_ibfk_1` FOREIGN KEY (`langtextid`) REFERENCES `langtext` (`langtextid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `template` */

CREATE TABLE `template` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `description` varchar(30) DEFAULT NULL,
  `mediatype` smallint(6) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`templateid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2847` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `templatetext` */

CREATE TABLE `templatetext` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `bydefault` tinyint(1) DEFAULT NULL,
  `freetextid` int(11) DEFAULT '0',
  `languageid` int(11) NOT NULL DEFAULT '0',
  `templateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`languageid`,`templateid`),
  KEY `companyid` (`companyid`),
  KEY `freetextid` (`freetextid`),
  KEY `templateid` (`templateid`),
  CONSTRAINT `templatetext_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `templatetext_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `templatetext_ibfk_3` FOREIGN KEY (`languageid`) REFERENCES `language` (`languageid`),
  CONSTRAINT `templatetext_ibfk_4` FOREIGN KEY (`templateid`) REFERENCES `template` (`templateid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `templatetextimg` */

CREATE TABLE `templatetextimg` (
  `companyid` int(11) NOT NULL,
  `imagestoreid` int(11) NOT NULL,
  `languageid` int(11) NOT NULL,
  `templateid` int(11) NOT NULL,
  PRIMARY KEY (`imagestoreid`,`languageid`,`templateid`),
  KEY `companyid` (`companyid`),
  KEY `languageid` (`languageid`,`templateid`),
  CONSTRAINT `templatetextimg_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `templatetextimg_ibfk_2` FOREIGN KEY (`languageid`, `templateid`) REFERENCES `templatetext` (`languageid`, `templateid`),
  CONSTRAINT `templatetextimg_ibfk_3` FOREIGN KEY (`imagestoreid`) REFERENCES `imagestore` (`imagestoreid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `title` */

CREATE TABLE `title` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `titleid` int(11) NOT NULL DEFAULT '0',
  `titletext` varchar(30) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `old_short` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`titleid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2851` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usergroup` */

CREATE TABLE `usergroup` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `groupname` varchar(60) DEFAULT NULL,
  `usergroupid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`usergroupid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `usergroup_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usermail` */

CREATE TABLE `usermail` (
  `automaticforward` tinyint(4) DEFAULT NULL,
  `automaticreply` tinyint(4) DEFAULT NULL,
  `bgdownload` tinyint(4) DEFAULT NULL,
  `companyid` int(11) NOT NULL DEFAULT '0',
  `createincontact` tinyint(1) NOT NULL DEFAULT '0',
  `createoutcontact` tinyint(1) NOT NULL DEFAULT '0',
  `date` int(11) NOT NULL DEFAULT '0',
  `editorfont` varchar(100) DEFAULT NULL,
  `editorfontsize` varchar(30) DEFAULT NULL,
  `emptytrashlogout` tinyint(1) NOT NULL DEFAULT '0',
  `forwardemail` varchar(250) DEFAULT NULL,
  `keepemailserver` tinyint(4) DEFAULT NULL,
  `replymessage` varchar(250) DEFAULT NULL,
  `replymode` tinyint(1) NOT NULL DEFAULT '0',
  `replysubject` varchar(250) DEFAULT NULL,
  `savesenditem` tinyint(1) NOT NULL DEFAULT '0',
  `showpopmsgs` tinyint(4) DEFAULT NULL,
  `usermailid` int(11) NOT NULL DEFAULT '0',
  `editmode` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`usermailid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `usermail_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `usermail_ibfk_2` FOREIGN KEY (`usermailid`) REFERENCES `elwisuser` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `userofgroup` */

CREATE TABLE `userofgroup` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `usergroupid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`usergroupid`,`userid`),
  KEY `userid` (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `userofgroup_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userofgroup_ibfk_2` FOREIGN KEY (`usergroupid`) REFERENCES `usergroup` (`usergroupid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userofgroup_ibfk_3` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `userrole` */

CREATE TABLE `userrole` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `roleid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`,`roleid`),
  KEY `IX_Relationship1` (`userid`),
  KEY `IX_Relationship2` (`roleid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`),
  CONSTRAINT `userrole_ibfk_3` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `userrole_ibfk_4` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usersession` */

CREATE TABLE `usersession` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `statusname` varchar(40) NOT NULL DEFAULT '',
  `module` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`userid`,`statusname`,`module`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2998` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `usersession_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usersessionlog` */

CREATE TABLE `usersessionlog` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `endconnection` bigint(20) DEFAULT NULL,
  `ip` varchar(20) NOT NULL DEFAULT '',
  `lastaction` bigint(20) DEFAULT NULL,
  `sessionid` varchar(60) NOT NULL DEFAULT '',
  `startconnection` bigint(20) NOT NULL DEFAULT '0',
  `isconnected` tinyint(4) DEFAULT NULL,
  `userid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `usersessionlog_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `elwisuser` (`userid`),
  CONSTRAINT `usersessionlog_ibfk_2` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usersessionparam` */

CREATE TABLE `usersessionparam` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `userid` int(11) NOT NULL DEFAULT '0',
  `statusname` varchar(40) NOT NULL DEFAULT '',
  `module` varchar(40) NOT NULL DEFAULT '',
  `paramname` varchar(100) NOT NULL DEFAULT '',
  `type` int(2) DEFAULT NULL,
  `value` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`userid`,`statusname`,`module`,`paramname`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_3000` FOREIGN KEY (`userid`, `statusname`, `module`) REFERENCES `usersession` (`userid`, `statusname`, `module`),
  CONSTRAINT `usersessionparam_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `usertask` */

CREATE TABLE `usertask` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `scheduleduserid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  `statusid` int(11) DEFAULT NULL,
  `freetextid` int(11) DEFAULT NULL,
  PRIMARY KEY (`scheduleduserid`),
  KEY `companyid` (`companyid`),
  KEY `freetextid` (`freetextid`),
  CONSTRAINT `usertask_ibfk_1` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `usertask_ibfk_2` FOREIGN KEY (`freetextid`) REFERENCES `freetext` (`freetextid`),
  CONSTRAINT `usertask_ibfk_3` FOREIGN KEY (`scheduleduserid`) REFERENCES `scheduleduser` (`scheduleduserid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vat` */

CREATE TABLE `vat` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `description` varchar(80) DEFAULT NULL,
  `label` varchar(40) DEFAULT NULL,
  `vatid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`vatid`),
  KEY `companyid` (`companyid`),
  CONSTRAINT `0_2936` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vatrate` */

CREATE TABLE `vatrate` (
  `companyid` int(11) NOT NULL DEFAULT '0',
  `validfrom` int(11) NOT NULL DEFAULT '0',
  `vatid` int(11) NOT NULL DEFAULT '0',
  `vatrate` decimal(6,2) DEFAULT NULL,
  `vatrateid` int(11) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`vatrateid`),
  KEY `companyid` (`companyid`),
  KEY `vatid` (`vatid`),
  CONSTRAINT `0_2995` FOREIGN KEY (`companyid`) REFERENCES `company` (`companyid`),
  CONSTRAINT `0_2996` FOREIGN KEY (`vatid`) REFERENCES `vat` (`vatid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/* Inserting default data and super-company*/



/* Address Jatun (addressid=1) */
insert into address
(active, addressid, addresstype, bankaccountid, birthday, cityid, code, companyid,
countryid, education, freetextid, housenumber, imageid, keywords, languageid,
lastmoddate, lastmoduser, name1, name2, name3, personal, pobox, recorddate,
recorduser, salutationid, searchname, street, taxnumber, titleid, version,
waydescriptionid, zipofpobox) values
(1, 1, '0', NULL, NULL, NULL, 9, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
20071107, 1, 'Jatun S.R.L.', 'Innovación & Desarrollo', NULL, 0, NULL, 20071107,
1, NULL, 'Jatun', NULL, NULL, NULL, 1, NULL, NULL);

/* Address Fernando (addressid=2) */
insert into address
(active, addressid, addresstype, bankaccountid, birthday, cityid, code, companyid,
countryid, education, freetextid, housenumber, imageid, keywords, languageid,
lastmoddate, lastmoduser, name1, name2, name3, personal, pobox, recorddate,
recorduser, salutationid, searchname, street, taxnumber, titleid, version,
waydescriptionid, zipofpobox) values
(1, 2, '1', NULL, NULL, NULL, 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
20071107, 1, 'Montaño', 'Fernando', NULL, 0, NULL, 20071107,
1, NULL, 'fer', NULL, NULL, NULL, 1, NULL, NULL);

/* Contact person */

insert into contactperson
(active, addressid, companyid, contactpersonid, departmentid, function,
persontypeid, recorddate, recorduserid, version)
values (1, 1, 1, 2, NULL, NULL, NULL, 20071107, 1, 1);

/* Employee */

insert into employee    (companyid, costcenterid, costhour, costposition, dateend, departmentid,
employeeid, function, healthfund, hiredate, hourlyrate, initials, officeid,
socialsecnumber, version)
values    (1, NULL, NULL, NULL, NULL, NULL, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

/* User: fernando:.jatun.*/
insert into elwisuser
(addressid, active, accessip, companyid, dayfragmentation, defaultview,
emailappointment, emailquestion, emailschetask, emailsupportcase,
finaldaywork, favoritelanguage, hasmailaccount, holidaycountryid,
initialdaywork, isdefault, login, maxrecentlist, password,
rowsperpage, seeprivatedata, timeout, timezone,
type, userid, version)
values
(2, 1, NULL, 1, NULL, NULL,
NULL, NULL, NULL, NULL,
NULL, 'en', NULL, NULL,
NULL, 1, 'fer', 10, 'BKuXdAxVcIyJFSYvBVEIoABeyWo=',
15, NULL, 90, 'America/La_Paz',
1, 1, 1);

/* Super Company: jatun */
insert into company
(companyid, copytemplate, finishlicense, language, login, logoid, maildomain,
maxattachsize, routepageid, rowsperpage, startlicense, style, telecomtypestatus,
timeout, trial, usersallowed, version, active, isdefault)
values    (1, NULL, NULL, 'en', 'jatun', NULL, NULL, NULL, NULL, 10, NULL, NULL, NULL,
90, 0, NULL, 1, 1, 1);

/* System constants */

insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with address table','ADDRESS','Category.address','CATEGORYTYPES','4');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('ELWIS source code version','APP_VERSION',NULL,'VERSIONS','4.4');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with contactPerson table','CONTACTPERSON','Category.contactPerson','CATEGORYTYPES','2');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Criterion with customer table','CUSTOMER','Category.customer','CATEGORYTYPES','1');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('ELWIS database version','DB_VERSION',NULL,'VERSIONS','4.2.5');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('English resource language','ENGLISH','Common.english','SYSTEMLANGUAGE','en');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('German resource language','GERMAN','Common.german','SYSTEMLANGUAGE','de');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Spanish resource language','SPAIN','Common.spanish','SYSTEMLANGUAGE','es');
insert  into `systemconstant`(`description`,`name`,`resourcekey`,`type`,`value`) values ('Enable or disable trial company creation','TRIAL',NULL,'COMPANYCREATION','1');

/* System modules */

insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Administration module',-308758540,'package.Administration','/admin');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Modulo de contacts',1,'module.contactManager','/contacts');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Modulo de campaigns',2,'module.campaign','/campaign');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of products',3,'module.products','/products');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of sales',4,'module.sales','/sales');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of webmail',5,'module.webmail','/webmail');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module Scheduler',6,'module.scheduler','/scheduler');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module support',8,'module.support','/support');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of reports',9,'module.reports','/reports');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Home module',10,'Common.home','/');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Module of finances',11,'module.finance','/finance');
insert  into `systemmodule`(`description`,`moduleid`,`modulenamekey`,`modulepath`) values ('Project module',12,'module.projects','/projects');


/* System functions */
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (7,'FAVORITE','Contact favorites',-555212806,1,'Contact.favorites');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CUSTOMER','Customer  base functionality',-552432681,1,'Product.Tab.Customers');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPLIER','Suplier base functionality',-552347618,1,'Product.Tab.Supplier');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCT','Product',-536716150,3,'Product.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRICING','Pricing',-536452275,3,'Product.Tab.Pricing');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'COMPETITOR','Product Competitor',-536362806,3,'Product.Tab.Competitors');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PICTURE','Product Picture',-536206447,3,'Product.Tab.Pictures');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COMPETITORPRODUCT','Competitor Product',-532549822,3,'Competitor.competitorProduct');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTCATEGORY','Product Category',-532414603,3,'ProductCategory.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESS','Sales process main',-314718634,4,'SalesProcess');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'SALESPROCESSACTION','sales process actions',-313932025,4,'SalesProcessAction.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESSPOSITION','Sales process action positions',-312494525,4,'SalesProcess.actionPositions');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'DEPARTMENT','organization departments',-311051853,1,'Contacts.Tab.departments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'OFFICE','Company offices',-309982931,1,'Contact.Tab.offices');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTSUPPLIER','Product supplier',-302568837,3,'Product.Tab.Supplier');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYINFO','Company info',-301159743,1,'Contact.Tab.companyInfo');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'USER','user',-279728775,-308758540,'Admin.User.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COMPANY','company',-279568150,-308758540,'Admin.Company.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ROLE','role',-279295572,-308758540,'Admin.Role.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'ACCESSRIGHT','access right',-273493572,-308758540,'Admin.AccessRights.Title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTACT','Contacts: Person & Organization',1,1,'subModule.contacts');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BANKACCOUNT','bank accounts',2,1,'Contacts.Tab.bankAccounts');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTACTPERSON','Contact persons',3,1,'Contacts.Tab.contactPersons');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'COMMUNICATION','Communications',4,1,'Contacts.Tab.communications');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'EMPLOYEE','Employees',5,1,'Contact.Tab.employees');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'LANGUAGE','Language Catalog',6,1,'Language.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BANK','Bank catalog',7,1,'Bank.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CITY','City catalog',8,1,'City.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COSTCENTERS','Cost center catalog',9,1,'CostCenter.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'COUNTRY','Country catalog',10,1,'Country.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CURRENCY','Currency catalog',11,1,'Currency.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PERSONTYPE','Person type catalog',12,1,'PersonType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALUTATION','Salutation catalog',13,1,'Salutation.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPLIERTYPE','Supplier type catalog',14,1,'SupplierType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TITLE','Title catalog',15,1,'Title.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ADDRESSSOURCE','Addess source catalog',16,1,'AddressSource.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'BRANCH','Branch catalog',17,1,'Branch.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORY','Category catalog',18,1,'Category.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYVALUE','Category values catalog',19,1,'CategoryValue.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CUSTOMERTYPE','Customer types catalog',20,1,'CustomerType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PAYCONDITION','Payment condition catalog',21,11,'PayCondition.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PAYMORALITY','Payment morality catalog',22,1,'PayMorality.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRIORITY','Priority catalog',23,1,'Priority.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TELECOMTYPE','Telecom type catalog',26,1,'TelecomType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TEMPLATE','Template catalog',27,1,'Template.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACHIEVEMENT','Achievements catalog',28,3,'Achievement.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTTYPE','Product type catalog',29,3,'ProductType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTGROUP','Product group catalog',30,3,'ProductGroup.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTUNIT','Product unit',31,3,'ProductUnit.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'VAT','Vat catalog',32,11,'Vat.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'VATRATE','Vat rate catalog',33,11,'VatRate.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'STATUS','Status catalog',34,4,'SalesProcessStatus.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACTIONTYPE','Action type catalog',35,4,'ActionType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALESPROCESSPRIORITY','Sales process priority catalog',36,4,'Priority.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGN','Campaign',40,2,'Campaign');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNCRITERION','Campaign Criterion',41,2,'Campaign.Tab.selection');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNCONTACTS','Campaign Contacts',42,2,'Campaign.Tab.recipients');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNTEMPLATE','Campaign Templates',43,2,'Campaign.Tab.templates');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (16,'SALESPROCESSEVALUATION','sales process evaluation graphics',44,4,'SalesProcess.Title.evaluation');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (25,'MAIL','Webmail basic functions',45,5,'Webmail.common.mail');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILGROUP','Webmail contact group',46,5,'Webmail.contactGroup.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILFILTER','Webmail filter',47,5,'Webmail.filter.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILFOLDER','Webmail folder',48,5,'Webmail.folder.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WEBMAILSIGNATURE','Webmail signatures',49,5,'Webmail.signature.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASK','Scheduler task',50,6,'Scheduler.Task');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASKUSER','Scheduler user appointment',51,6,'Scheduler.Task.users');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'USERGROUP','Admin usergroup',52,-308758540,'Admin.User.UserGroup');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENT','Scheduler appointment',53,6,'Scheduler.Appointment');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENTPARTICIPANT','Scheduler user appointment',54,6,'Appointment.participants');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'APPOINTMENTTYPE','Scheduler appointment type',55,6,'Task.appointmentType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SCHEDULERPRIORITY','Scheduler priority',56,6,'Task.priority');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'TASKTYPE','Scheduler task type',57,6,'Task.taskType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'HOLIDAY','Scheduler holiday',58,6,'holiday.title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'GRANTACCESS','Scheduler grant access',59,6,'Scheduler.grantAccess');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'PREFERENCE','Scheduler preferences',60,6,'Scheduler.configuration');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'USERINTERFACE','User interface manager',61,10,'UIManager.userConfigurable');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYINTERFACE','Company interface manager',62,10,'UIManager.companyConfigurable');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYLOGO','Company logo',63,10,'UIManager.LogoCompany');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTPRIORITY','Support priority',64,8,'Article.priority');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTCATEGORY','Support category',65,8,'Article.category');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASETYPE','Support case type',66,8,'CaseType.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASESEVERITY','Support case severity',67,8,'CaseSeverity.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'WORKLEVEL','Support work level',68,8,'SupportCase.workLevel');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'STATE','Support state',69,8,'SupportCase.states');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTUSER','Support user',70,8,'SupportUser.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ARTICLE','Support article',71,8,'Article.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'ARTICLECOMMENT','Support article comment',72,8,'Article.comments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'ARTICLERELATED','Support article related',73,8,'Article.Tab.ArticleRelateds');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SUPPORTATTACH','Support support attach',74,8,'Article.Tab.Attachments');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ARTICLELINK','Support article link',75,8,'Article.links');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (1,'ARTICLEHISTORY','Support article history',76,8,'Article.histories');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'QUESTION','Support question',77,8,'Question.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CASE','Support case',78,8,'SupportCase.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'CASEACTIVITY','Support case activity',79,8,'SupportCaseActivity.title.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (4,'ARTICLERATING','Support article rating',80,8,'Article.rating.plural');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (1,'USERSESSION','User session',81,-308758540,'User.userSession');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'REPORT','Report view create update delete',83,9,'Report.common.reports');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COLUMN','Report Column tab view, update',84,9,'Report.tab.columns');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'FILTER','Report filter tab view, create,update, delete',85,9,'Report.tab.filters');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'GROUP','Report group tab view,update',86,9,'Report.Tab.grouping');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'TOTALIZE','Report totalize tab view,update',87,9,'Report.Tab.totalize');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'CHART','Report chart tab view,update',88,9,'Report.ChartType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNTYPE','Campaign type catalog',89,2,'Catalogs.CampaignType');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (31,'CAMPAIGNACTIVITY','Campaign Activity',90,2,'Campaign.Tab.Activity');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CAMPAIGNATTACH','Campaign Attach',92,2,'Campaign.Tab.Attach');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACTIVITYUSER','Campaign Activity User',93,2,'Campaign.ActivityUser');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'USERSETTINGS','User settings view, update accessright',94,10,'Home.userSettings');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'DASHBOARD','Dashboard view, update accessright',95,10,'Home.dashborad');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (11,'REPORTROLE','Reports roles',96,9,'Report.Roles');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (16,'CONTACTIMPORT','Contact data import',97,1,'contact.dataImport');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYTAB','Category tab accessright',98,1,'Catalogs.CategoryTab');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CATEGORYGROUP','Category group accessright',99,1,'Catalogs.CategoryGroup');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'CONTRACTTYPE','Contract type catalog',100,4,'ContractType.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'ACCOUNT','Account catalog',101,11,'Account.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICETEMPLATE','Invoice template catalog',102,11,'InvoiceTemplate.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'REMINDERLEVEL','Reminder level catalog',103,11,'ReminderLevel.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SEQUENCERULE','Sequence rule catalog',104,11,'SequenceRule.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICE','Invoice access right',105,11,'Invoice.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEPOSITION','Invoice position access right',106,11,'InvoicePosition.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEREMINDER','Invoice reminder access right',107,11,'InvoiceReminder.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INVOICEPAYMENT','Invoice payment access right',108,11,'InvoicePayment.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALE','Sale Access right',109,4,'Sale.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'SALEPOSITION','Sale Position Access right',110,4,'SalePosition.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PRODUCTCONTRACT','ProductContract Access right',111,4,'ProductContract.accessRight');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECT','Project',112,12,'Project.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTTIME','Project time',113,12,'ProjectTime.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTACTIVITY','Project activity',114,12,'ProjectActivity.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTUSER','Project assignee',115,12,'ProjectAssignee.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'PROJECTSUBPROJECT','Project sub project',116,12,'ProjectSubProject.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INCOMINGINVOICE','Incoming invoice access rights',117,11,'Finance.incomingInvoice.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (15,'INCOMINGPAYMENT','Incoming invoice payments access rights',118,11,'Finance.incomingPayment.functionality');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'APPLICATIONSIGNATURE','Application email signatures view and update accessright',119,-308758540,'ApplicationSignature.accessRight.title');
insert  into `systemfunction`(`allowpermission`,`code`,`description`,`functionid`,`moduleid`,`namekey`) values (5,'COMPANYPREFERENCES','The company settings view and update accessrights',120,10,'Home.companyPreferences');

/* Company module (Only administration)*/
insert into companymodule (companyid, moduleid, maintablelimit, active) VALUES (1,-308758540,NULL,1);

/* Role */
insert into role (companyid, isdefault, descriptionid, roleid, rolename, version) VALUES (1,1,NULL,1,'System root',1);

/* User role */

insert into userrole (userid, roleid) VALUES (1,1);

/* Access rights (Only admin access rights)*/

/* User */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279728775, -308758540, 15, 1, 1, 1);
/* Company */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279568150, -308758540, 15, 1, 1, 1);
/* Role */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-279295572, -308758540, 15, 1, 1, 1);
/* Access rights */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (-273493572, -308758540, 4, 1, 1, 1);
/* Application signatures */
insert into accessrights (functionid, moduleid, permission, roleid, active, companyid) values    (119, -308758540, 5, 1, 1, 1);

/* Campaign criterion constants */


insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (1,1,'campaign.customerType','customertypeid',6,'customertypename','customertype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (2,1,'campaign.expectedTurn','expectedturnover',2,'0','customer');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (3,1,'campaign.numberEmployees','numberofemployees',1,'0','customer');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (4,1,'campaign.priority','priorityid',6,'priorityname','priority');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (5,1,'campaign.addressSource','sourceid',6,'sourcename','addresssource');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (6,2,'campaign.personType','persontypeid',6,'persontypename','persontype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (7,4,'campaign.city','zip',1,'0','city');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (8,4,'campaign.country','countryid',6,'countryname','country');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (10,4,'campaign.language','languageid',6,'languagename','language');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (11,4,'campaign.recorddate','recorddate',3,'0','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (12,2,'ContactPerson.department','departmentname',4,'departmentname','department');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (13,4,'Contact.city','cityname',4,'cityname','city');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (14,1,'Customer.branch','branchid',6,'branchname','branch');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (15,4,'campaign.addressKeywords','keywords',4,'0','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (16,2,'campaign.contactPersonFunction','function',4,'function','contactperson');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (17,1,'campaign.salesPerson','employeeid',6,'name1_name2_name3','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (18,1,'campaign.salesPartner','partnerid',6,'name1_name2_name3','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (19,3,'campaign.productType','typeid',6,'typename','producttype');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (20,3,'campaign.productGroup','groupid',6,'groupname','productgroup');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (21,3,'campaign.productVersion','currentversion',4,'0','product');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (22,4,'Contact.type','code',1,'code','address');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (23,3,'product.inUse','inuse',1,'inuse','saleposition');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (24,2,'contactPerson.recorddate','recorddate_cp',3,'0','contactperson');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (25,2,'campaign.createdBy','recorduserid',6,'name1_name2_name3','elwisuser');
insert  into `campcriterionvalue`(`campcriterionvalueid`,`tableid`,`descriptionkey`,`field`,`fieldtype`,`fieldname`,`tablename`) values (26,3,'Campaign.product','productid',6,'productname','product');

/*  Already used sequence update */

insert into sequence (`name`, sequencenumber) values ('address',3);
insert into sequence (`name`, sequencenumber) values ('elwisuser',2);
insert into sequence (`name`, sequencenumber) values ('role',2);

SET FOREIGN_KEY_CHECKS=1;