package com.piramide.elwis.web.common.validator;

import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class check if one foreign key id values exists in their referenced table.
 * If no existence, then another user was delete the item, show an error.
 *
 * @author Fernando Montaño
 * @version $Id: ForeignkeyValidator.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ForeignkeyValidator {

    private Log log = LogFactory.getLog(ForeignkeyValidator.class);
    /**
     * Singleton instance.
     */
    public static final ForeignkeyValidator i = new ForeignkeyValidator();

    private ForeignkeyValidator() {
    }

    /**
     * Check if foreign key from other relationated table exists or was deleted by other user.
     *
     * @param referencedTableName  foreign table
     * @param referencedPrimaryKey foreign key to check existence
     * @param keyValue             the value of primary key
     * @param errors               AcctionErrors to put the error if it exists
     * @param errorMessageKey      the error message key
     * @return the AcctionErrors with the error added, if exists.
     */
    public ActionErrors validate(String referencedTableName, String referencedPrimaryKey, Object keyValue,
                                 ActionErrors errors, String errorMessageKey) {
        log.debug("Executing DBExistence checker...");
        try {
            referencedPrimaryKey = referencedPrimaryKey.toLowerCase();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ")
                    .append(referencedPrimaryKey)
                    .append(" FROM ")
                    .append(referencedTableName)
                    .append(" WHERE ")
                    .append(referencedPrimaryKey)
                    .append("=")
                    .append(keyValue);
            List result = QueryUtil.i.executeQuery(sql.toString());
            /** If no found the id in the foreign table, then was deleted by other user. **/
            if (result.size() == 0) {
                errors.add(referencedPrimaryKey, new ActionError(errorMessageKey));
            }

        } catch (Exception e) {
            log.error("Errors validating existences of the foreign key", e);
        }
        return errors;
    }

    /**
     * Check if foreign key from other relationated table exists or was deleted by other user.
     *
     * @param referencedTableName foreign table
     * @param idColumn            foreign key to check existence
     * @param idValue             the value of primary key
     * @return true if exits, false otherwise
     */
    public boolean exists(String referencedTableName, String idColumn, Object idValue) {
        log.debug("Executing DBExistence checker...");
        try {

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ")
                    .append(idColumn)
                    .append(" FROM ")
                    .append(referencedTableName)
                    .append(" WHERE ")
                    .append(idColumn)
                    .append("=")
                    .append(idValue);
            List result = QueryUtil.i.executeQuery(sql.toString());
            /** If no found the id in the foreign table, then was deleted by other user. **/
            if (result.size() > 0) {
                return true;
            }

        } catch (Exception e) {
            log.error("Errors validating existences of the foreign key", e);
        }
        return false;
    }

    /**
     * Check if foreign key from other relationated table exists or was deleted by other user.
     *
     * @param referencedTableName  foreign table
     * @param referencedPrimaryKey foreign key to check existence
     * @param keyValue             the value of primary key
     * @param errors               AcctionErrors to put the error if it exists
     * @param error                The action error
     * @return the AcctionErrors with the error added, if exists.
     */
    public ActionErrors validate(String referencedTableName, String referencedPrimaryKey, Object keyValue,
                                 ActionErrors errors, ActionError error) {
        log.debug("Executing DBExistence checker...");
        try {
            referencedPrimaryKey = referencedPrimaryKey.toLowerCase();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ")
                    .append(referencedPrimaryKey)
                    .append(" FROM ")
                    .append(referencedTableName)
                    .append(" WHERE ")
                    .append(referencedPrimaryKey)
                    .append("=")
                    .append(keyValue);

            List result = QueryUtil.i.executeQuery(sql.toString());
            /** If no found the id in the foreign table, then was deleted by other user. **/
            if (result.size() == 0) {
                errors.add(referencedPrimaryKey, error);
            }

        } catch (Exception e) {
            log.error("Errors validating existences of the foreign key", e);
        }
        return errors;
    }

    /**
     * Check if compound foreign key from other relationated table exists or was deleted by other user.
     *
     * @param foreignTable  foreign table
     * @param foreignKey0   column key 0
     * @param foreignKey1   column key 1
     * @param foreignValue0 key value 0
     * @param foreignValue1 key value 1
     * @param errors        action errors
     * @param error         error to put to action errors if validate found the error.
     * @return the AcctionErrors with the error added, if exists.
     */
    public ActionErrors validate(String foreignTable, String foreignKey0, String foreignKey1, Object foreignValue0,
                                 Object foreignValue1, ActionErrors errors, ActionError error) {
        log.debug("Executing DBExistence checker...");
        try {

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ")
                    .append(foreignKey0)
                    .append(", ")
                    .append(foreignKey1)
                    .append(" FROM ")
                    .append(foreignTable)
                    .append(" WHERE ")
                    .append(foreignKey0)
                    .append("=")
                    .append(foreignValue0)
                    .append(" AND ")
                    .append(foreignKey1)
                    .append("=")
                    .append(foreignValue1);

            List result = QueryUtil.i.executeQuery(sql.toString());
            //If no found the id in the foreign table, then was deleted by other user.
            if (result.size() == 0) {
                errors.add(foreignTable, error);
            }

        } catch (Exception e) {
            log.error("Errors validating existences of the foreign key", e);
        }
        return errors;
    }

    /**
     * Check if compound foreign key from other relationated table exists or was deleted by other user.
     *
     * @param foreignTable  foreign table
     * @param columnsValues Map which contains as key the column name and as value the value for that column.
     * @param errors        action errors
     * @param error         error to put to action errors if validate found the error.
     * @return the AcctionErrors with the error added, if exists.
     */
    public ActionErrors validate(String foreignTable, Map columnsValues, ActionErrors errors, ActionError error) {
        log.debug("Executing validation of compound foreign key...");

        try {

            StringBuffer sql = new StringBuffer();
            sql.append("SELECT ");


            int i = 0;
            for (Iterator iterator = columnsValues.keySet().iterator(); iterator.hasNext();) {
                String column = (String) iterator.next();
                sql.append(column);
                if (i < columnsValues.size() - 1) {
                    sql.append(", ");
                }
                i++;
            }

            sql.append(" FROM ")
                    .append(foreignTable)
                    .append(" WHERE ");

            List params = new LinkedList();
            i = 1;
            for (Iterator iterator = columnsValues.keySet().iterator(); iterator.hasNext();) {
                String column = (String) iterator.next();
                sql.append(column)
                        .append("=")
                        .append("?");

                if (i < columnsValues.size()) {
                    sql.append(" AND ");
                }

                params.add(columnsValues.get(column));
                i++;
            }

            List result = QueryUtil.i.executeQuery(new String(sql), params);
            //If no found the id in the foreign table, then was deleted by other user.
            if (result.isEmpty()) {
                errors.add(foreignTable, error);
            }

        } catch (Exception e) {
            log.error("Errors validating existences of the foreign key", e);
        }
        return errors;
    }
}
