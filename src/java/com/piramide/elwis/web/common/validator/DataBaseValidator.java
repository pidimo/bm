package com.piramide.elwis.web.common.validator;

import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class check if one foreign key id values exists in their referenced table.
 * If no existence, then another user was delete the item, show an error.
 *
 * @author Fernando Montaño
 * @version $Id: DataBaseValidator.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class DataBaseValidator {

    private Log log = LogFactory.getLog(getClass());
    private static final String SQL_CHECKDUPLICATE = "SELECT <field> FROM <table> WHERE <field> = ";
    private static final String SQL_CHECK_ONLYONE = "SELECT <field> FROM <table> WHERE <field> = ";
    private static final String SQL_CHECK_COUNT = "SELECT count(companyid) as numberofrecords FROM <table> WHERE companyid = ";
    /**
     * Singleton instance.
     */
    public static final DataBaseValidator i = new DataBaseValidator();

    private DataBaseValidator() {
    }

    /**
     * Check if database field is duplicate
     *
     * @param table      Name in database
     * @param field      Name in table var
     * @param fieldValue Value of field var
     * @param conditions Possible conditios for sql
     * @return ;
     */
    public boolean isDuplicate(String table, String field, String fieldValue, String fieldId, String fieldValuedId, Map conditions, boolean isUpdate) {
        try {
            if (fieldValue.indexOf("'") >= 0) {
                //this is necessary if the value of fieldValue will be between simple qoutes (')
                fieldValue = fieldValue.replaceAll("'", "''");
            }

            StringBuffer sql = new StringBuffer(SQL_CHECKDUPLICATE.replaceAll("<table>", table).replaceAll("<field>", field));
            sql.append("'")
                    .append(fieldValue)
                    .append("'");
            if (isUpdate) {
                sql.append(" AND ").append(fieldId).append(" <> '").append(fieldValuedId).append("'");
            }


            for (Iterator iterator = conditions.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                sql.append(" AND ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }
            List result = QueryUtil.i.executeQuery(sql.toString());

            if (result.size() > 0) {
                return true;
            }

        } catch (Exception e) {
            log.error("Errors validating elment in duplicate", e);
        }
        return false;
    }

    public boolean isOnlyOne(String table, String onlyOneField, String onlyOneValue, Map conditions, Map constantConditions) {
        try {
            if (onlyOneValue.indexOf("'") >= 0) {
                //this is necessary if the value of onlyOneValue will be between simple qoutes (')
                onlyOneValue = onlyOneValue.replaceAll("'", "''");
            }

            StringBuffer sql = new StringBuffer(SQL_CHECK_ONLYONE.replaceAll("<table>", table).replaceAll("<field>", onlyOneField));
            sql.append("'")
                    .append(onlyOneValue)
                    .append("'");


            for (Iterator iterator = conditions.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                sql.append(" AND ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }

            for (Iterator iterator = constantConditions.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                sql.append(" AND ").append(entry.getKey()).append(" = ").append(entry.getValue());
            }

            List result = QueryUtil.i.executeQuery(sql.toString());

            if (result.size() > 0) {
                return false;
            }

        } catch (Exception e) {
            log.error("Errors validating elment in duplicate", e);
        }
        return true;
    }

    @SuppressWarnings({"unchecked"})
    public boolean checkEntries(String table, String moduleId, String companyId) {
        boolean check = false;
        log.debug("... checkEntries function execute ..");
        StringBuilder sql = new StringBuilder(SQL_CHECK_COUNT.replaceAll("<table>", table));
        sql.append(companyId);
        List<Map> count = QueryUtil.i.executeQuery(sql.toString());
        Integer numberOfRecords = new Integer(count.get(0).get("numberofrecords").toString());

        sql = new StringBuilder("select maintablelimit from companymodule where companyid =" + companyId);
        sql.append(" and moduleid=").append(moduleId);

        List limit = QueryUtil.i.executeQuery(sql.toString());

        if (limit.get(0) != null) {
            Map limitNumber = (Map) limit.get(0);
            if (limitNumber.get("maintablelimit") != null) {
                if (numberOfRecords < new Integer(limitNumber.get("maintablelimit").toString())) {
                    check = true;
                }
            } else {
                check = true;
            }
        }
        return check;
    }
}
