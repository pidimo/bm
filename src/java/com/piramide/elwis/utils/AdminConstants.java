package com.piramide.elwis.utils;

/**
 * Admin manager constants
 */
public abstract class AdminConstants {
    /**
     * TABLE NAMES SECTIOn
     */
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_ROLE = "role";
    public static final String TABLE_USERROLE = "userrole";
    public static final String TABLE_USER = "elwisuser";
    public static final String TABLE_USERGROUP = "usergroup";
    public static final String TABLE_USEROFGROUP = "userofgroup";
    public static final String TABLE_USERSESSIONLOG = "usersessionlog";
    public static final String TABLE_APPLICATIONMAILSIGNATURE = "appmailsignature";
    public static final String TABLE_PASSWORDCHANGE = "passwordchange";
    public static final String TABLE_ROLEPASSWORDCHANGE = "rolepasschange";
    public static final String TABLE_USERPASSWORDCHANGE = "userpasschange";
    public static final String TABLE_DYNAMICSEARCH = "dynamicsearch";
    public static final String TABLE_DYNAMICSEARCHFIELD = "dysearchfield";
    public static final String TABLE_DEMOACCOUNT = "demoaccount";

    /**
     * JNDI SECTION *
     */
    public static final String JNDI_USER = "Elwis.User";
    public static final String JNDI_ADMIN_FREETEXT = "Elwis.AdminFreeText";
    public static final String JNDI_ROLE = "Elwis.Role";
    public static final String JNDI_ACCESSRIGHTS = "Elwis.AccessRights";
    public static final String JNDI_COMPANYMODULE = "Elwis.CompanyModule";
    public static final String JNDI_FUNCTIONDEPENDENCY = "Elwis.FunctionDependency";
    public static final String JNDI_SYSTEMFUNCTION = "Elwis.SystemFunction";
    public static final String JNDI_SYSTEMMODULE = "Elwis.SystemModule";
    public static final String JNDI_COMPANY = "Elwis.Company";
    public static final String JNDI_USEROFGROUP = "Elwis.UserOfGroup";
    public static final String JNDI_USERGROUP = "Elwis.UserGroup";
    public static final String JNDI_USERSESSIONLOG = "Elwis.UserSessionLog";
    public static final String JNDI_USERROLE = "ELWIS.UserRole";
    public static final String JNDI_DELETECOMPANYSERVICE = "ELWIS.DeleteCompanyService";
    public static final String JNDI_APPLICATIONMAILSIGNATURE = "ELWIS.ApplicationMailSignature";
    public static final String JNDI_PASSWORDCHANGE = "Elwis.PasswordChange";
    public static final String JNDI_ROLEPASSWORDCHANGE = "Elwis.RolePasswordChange";
    public static final String JNDI_USERPASSWORDCHANGE = "Elwis.UserPasswordChange";
    public static final String JNDI_DYNAMICSEARCH = "Elwis.DynamicSearch";
    public static final String JNDI_DYNAMICSEARCHFIELD = "Elwis.DynamicSearchField";
    public static final String JNDI_DEMOACCOUNT = "Elwis.DemoAccount";

    public static final String DEMO_COMPANYLOGIN_INDEX = "democompanyloginindex";

    public static String INTERNAL_USER = "1";
    public static String EXTERNAL_USER = "0";

    public static Boolean STATUS_ACTIVE = new Boolean(true);
    public static Boolean STATUS_INACTIVE = new Boolean(false);

    public static final Integer COMPANY_REPORT = Integer.valueOf("1");
    public static final Integer USER_REPORT = Integer.valueOf("2");
    public static final Integer USERGROUP_REPORT = Integer.valueOf("3");
    public static final Integer ROLES_REPORT = Integer.valueOf("4");
    public static final Integer USERSESSION_REPORT = Integer.valueOf("5");

    public static enum CompanyTemplateTypes {
        defaultTemplate(1),
        trialTemplate(2);
        private int constant;

        CompanyTemplateTypes(int cte) {
            this.constant = cte;
        }

        public String getConstantAsString() {
            return String.valueOf(constant);
        }

        public int getConstant() {
            return this.constant;
        }
    }

    /**
     * Used to recover comany creation system constants
     */
    public static final String COMPANY_CREATION_TYPE = "COMPANYCREATION";
    public static final String SYSTEMCONSTANT_TRIAL = "TRIAL";

    public static enum UserGroupType {
        SCHEDULER(1, "UserGroup.type.scheduler"),
        DATA_ACCESS_SECURITY(2, "UserGroup.type.dataAccessSecurity"),
        ARTICLE(3, "UserGroup.type.article");

        private Integer constant;
        private String resource;

        private UserGroupType(Integer value, String resource) {
            this.constant = value;
            this.resource = resource;
        }

        public Integer getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }

        public static UserGroupType findUserGroupType(String constant) {
            for (int i = 0; i < UserGroupType.values().length; i++) {
                UserGroupType type = UserGroupType.values()[i];
                if (type.equal(constant)) {
                    return type;
                }
            }
            return null;
        }
    }

    public static enum CompanyType {
        REGULAR(0, "Company.type.regular"),
        TRIAL(1, "Company.type.trial"),
        DEMO(2, "Company.type.demo");

        private Integer constant;
        private String resource;

        private CompanyType(Integer value, String resource) {
            this.constant = value;
            this.resource = resource;
        }

        public Integer getConstant() {
            return constant;
        }

        public String getResource() {
            return resource;
        }

        public boolean equal(Integer value) {
            return this.constant.equals(value);
        }

        public boolean equal(String value) {
            return this.constant.toString().equals(value);
        }

        public static CompanyType findCompanyType(String constant) {
            for (int i = 0; i < CompanyType.values().length; i++) {
                CompanyType type = CompanyType.values()[i];
                if (type.equal(constant)) {
                    return type;
                }
            }
            return null;
        }
    }

}
