package com.piramide.elwis.utils;

/**
 * Constants for Project module
 *
 * @author Fernando
 */
public class ProjectConstants {
    /*Table names*/
    public static final String TABLE_FREETEXT = "freetext";
    public static final String TABLE_PROJECT = "project";
    public static final String TABLE_PROJECT_TIME = "projecttime";
    public static final String TABLE_PROJECT_ACTIVITY = "projectactivity";
    public static final String TABLE_PROJECT_USER = "projectuser";
    public static final String TABLE_SUB_PROJECT = "subproject";
    public static final String TABLE_PROJECT_ASSIGNEE = "projectassignee";
    public static final String TABLE_PROJECT_TIME_LIMIT = "projecttimelimit";


    /*Jndi names*/
    public static final String JNDI_FREETEXT = "Elwis.ProjectFreeText";
    public static final String JNDI_PROJECT = "Elwis.Project";
    public static final String JNDI_PROJECT_TIME = "Elwis.ProjectTime";
    public static final String JNDI_PROJECT_ACTIVITY = "Elwis.ProjectActivity";
    public static final String JNDI_PROJECT_USER = "Elwis.ProjectUser";
    public static final String JNDI_SUB_PROJECT = "Elwis.SubProject";
    public static final String JNDI_PROJECT_ASSIGNEE = "Elwis.ProjectAssignee";
    public static final String JNDI_PROJECT_TIME_LIMIT = "Elwis.ProjectTimeLimit";

    /**
     * To be invoiced constants for project
     */
    public static enum ToBeInvoicedType {
        ALL_TIMES(1),
        NO_TIMES(2),
        DEPENDS(3);

        private int value;

        ToBeInvoicedType(int constant) {
            this.value = constant;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getAsString() {
            return String.valueOf(value);
        }
    }

    /**
     * To be invoiced constants for project
     */
    public static enum ProjectStatus {
        ENTERED(1),
        OPENED(2),
        CLOSED(3),
        FINISHED(4),
        INVOICED(5);

        private int value;

        ProjectStatus(int constant) {
            this.value = constant;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getAsString() {
            return String.valueOf(value);
        }
    }

    public static enum ProjectTimeStatus {
        ENTERED(1),
        RELEASED(2),
        CONFIRMED(3),
        NOT_CONFIRMED(4),
        INVOICED(5);

        private int value;

        ProjectTimeStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getAsString() {
            return String.valueOf(value);
        }
    }
}