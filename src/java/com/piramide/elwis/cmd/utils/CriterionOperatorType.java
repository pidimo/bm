package com.piramide.elwis.cmd.utils;


/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CriterionOperatorType.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CriterionOperatorType {
    public static String CriteriasOperator(String operator) {
        String ope = "";

        if ("EQUAL".equals(operator)) {
            ope = "=";
        } else if ("LESS".equals(operator)) {
            ope = "<";
        } else if ("GREAT".equals(operator)) {
            ope = ">";
        } else if ("EQUAL_LESS".equals(operator)) {
            ope = "<=";
        } else if ("EQUAL_GREAT".equals(operator)) {
            ope = ">=";
        } else if ("DISTINCT".equals(operator)) {
            ope = "<>";
        }
        return ope;
    }

    public static String CriteriaSimpleOperator(String operator) {
        String ope = "";
        if ("EQUAL".equals(operator)) {
            ope = "=";
        } else if ("LESS".equals(operator)) {
            ope = "<";
        } else if ("GREATER".equals(operator)) {
            ope = ">";
        } else if ("LESS_EQUAL".equals(operator)) {
            ope = "<=";
        } else if ("GREATER_EQUAL".equals(operator)) {
            ope = ">=";
        } else if ("DISTINCT".equals(operator)) {
            ope = "<>";
        }
        return ope;
    }
}
