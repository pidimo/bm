package com.piramide.elwis.utils;

import com.piramide.elwis.utils.configuration.EARJndiProperty;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:55:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportConstants {

    public static final String TABLE_ARTICLE = "article";
    public static final String TABLE_ARTICLECATEGORY = "articlecategory";
    public static final String TABLE_ARTICLEQUESTION = "articlequestion";
    public static final String TABLE_ARTICLECOMMENT = "articlecomment";
    public static final String TABLE_ARTICLERELATED = "articlerelated";
    public static final String TABLE_ARTICLELINK = "articlelink";
    public static final String TABLE_ARTICLEHISTORY = "articlehistory";
    public static final String TABLE_ARTICLERATING = "articlerating";
    public static final String TABLE_STATE = "state";
    public static final String TABLE_SUPPORTFREETEXT = "freetext";
    public static final String TABLE_SUPPORT_CASE = "supportcase";
    public static final String TABLE_SUPPORT_CASE_ACTIVITY = "caseactivity";
    public static final String TABLE_SUPPORT_SEVERITY = "caseseverity";
    public static final String TABLE_SUPPORT_CASETYPE = "casetype";
    public static final String TABLE_SUPPORTCASE_WORKLEVEL = "caseworklevel";
    public static final String TABLE_CASE_ACTIVITY = "caseactivity";
    public static final String TABLE_SUPPORT_ATTACH = "supportattach";
    public static final String TABLE_SUPPORT_CONTACT = "supportcontact";
    public static final String TABLE_SUPPORT_USER = "supportuser";
    public static final String TABLE_USERARTICLEACCESS = "userarticleaccess";

    public static final String JNDI_ARTICLE = "Elwis.Article";
    public static final String JNDI_ARTICLECATEGORY = "Elwis.ArticleCategory";
    public static final String JNDI_ARTICLEQUESTION = "Elwis.ArticleQuestion";
    public static final String JNDI_ARTICLECOMMENT = "Elwis.ArticleComment";
    public static final String JNDI_ARTICLERELATED = "Elwis.ArticleRelated";
    public static final String JNDI_ARTICLELINK = "Elwis.ArticleLink";
    public static final String JNDI_ARTICLEHISTORY = "Elwis.ArticleHistory";
    public static final String JNDI_ARTICLERATING = "Elwis.ArticleRating";
    public static final String JNDI_SUPPORT_FREETEXT = "Elwis.SupportFreeText";
    public static final String JNDI_SUPPORT_CASE = "Elwis.SupportCase";
    public static final String JNDI_SUPPORT_USER = "Elwis.SupportUser";
    public static final String JNDI_SUPPORT_CASE_TYPE = "Elwis.SupportCaseType";
    public static final String JNDI_SUPPORT_CASE_SEVERTITY = "Elwis.SupportCaseSeverity";
    public static final String JNDI_STATE = "Elwis.State";
    public static final String JNDI_SUPPORT_CASE_WORKLEVEL = "Elwis.SupportCaseWorkLevel";
    public static final String JNDI_SUPPORT_CASE_ACTIVITY = "Elwis.SupportCaseActivity";
    public static final String JNDI_SUPPORT_ATTACH = "Elwis.SupportAttach";
    public static final String JNDI_SUPPORT_CONTACT = "Elwis.SupportContact";
    public static final String JNDI_USERARTICLEACCESS = "Elwis.UserArticleAccess";

    public static final String JNDI_SUPPORTCASEINTEGRATIONSERVICE = EARJndiProperty.getEarName() + "SupportCaseIntegrationServiceBean/local";

    //published values - state catalog
    public static final int SUPPORT_TYPE_STATE = 1;
    public static final int OPEN_STATE = 0;
    public static final int PROGRESS_STATE = 1;
    public static final int CLOSE_STATE = 2;

    //published values - question
    public static final String YES = "0";
    public static final String NO = "1";


    //history --action values
    public static final String CREATE_ARTICLE = "0";
    public static final String UPDATE_ARTICLE = "1";
    public static final String DELETE_ARTICLE = "2";
    public static final String CREATE_LINK = "3";
    public static final String UPDATE_LINK = "4";
    public static final String DELETE_LINK = "5";
    public static final String CREATE_ATTACH = "6";
    public static final String UPDATE_ATTACH = "7";
    public static final String DELETE_ATTACH = "8";
    public static final String CREATE_RELATION = "9";
    public static final String UPDATE_RELATION = "10";
    public static final String DELETE_RELATION = "11";
    public static final String CREATE_COMMENT = "12";
    public static final String UPDATE_COMMENT = "13";
    public static final String DELETE_COMMENT = "14";

    public static final int OPENTYPE_OPEN = 0;
    public static final int OPENTYPE_CLOSE = 1;
    public static final int OPENTYPE_CASECLOSE = 2;

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    /*for  report values */
    public static final String ARTICLE_REPORT_LIST = "1";
    public static final String QUESTION_REPORT_LIST = "2";
    public static final String CASE_REPORT_LIST = "3";

    public static final Integer ARTICLE_ACCESS_CREATOR_USER_KEY = -1000;

    public static enum ArticlePublishedToStatus {
        CREATOR_USER(0),
        ALL_USERS(2);

        Integer constant;
        private ArticlePublishedToStatus(Integer value) {
            this.constant = value;
        }

        public Integer getConstant() {
            return constant;
        }

        public String getConstantAsString() {
            return constant.toString();
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }
    }

}
