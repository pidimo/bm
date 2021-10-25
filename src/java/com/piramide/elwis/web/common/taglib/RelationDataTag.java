package com.piramide.elwis.web.common.taglib;

import net.java.dev.strutsejb.dto.DTO;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 * This class implements a custom tag what retrieves data from a column of a relationship
 * and upload it to the Request variable with a name equals to the paramater id
 *
 * @author Alvaro
 * @version $Id: RelationDataTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class RelationDataTag extends TagSupport {
    private String id = "";   //Name of the result upload to the request
    private String table1 = "";   //Name of the table1 in the realtionship
    private String table2 = "";   //Name of the table2 in the relationship
    private String column = "";   //Name of the column to select in the table2
    private String table1FkName = ""; //Name of the foreign key in the table1
    private String table2PkName = ""; //Name of the primary key in the table2
    private String table1SearchFieldName = "";    //Name of the filter field in the table1
    private String table1SearchFieldValue = "";   //Value of the field to search in the table1
    private String truncateTo = "";   //if the result will be truntate to the value specified here
    private String table2SearchFieldName = "";    //Name of the filter field in the table2
    private String table2SearchFieldValue = "";   //Value of the field to search in the table2
    private String column2 = "";  //Name of the second column to select in the table2
    private String isNull = "";   //Name of the field in the second table that need to be null

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return (this.id);
    }

    public void setTable1(String table) {
        this.table1 = table;
    }

    public String getTable1() {
        return (this.table1);
    }

    public void setTable2(String table) {
        this.table2 = table;
    }

    public String getTable2() {
        return (this.table2);
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumn() {
        return (this.column);
    }

    public void setTable1FkName(String fkName) {
        this.table1FkName = fkName;
    }

    public String getTable1FkName() {
        return (this.table1FkName);
    }

    public void setTable2PkName(String pkName) {
        this.table2PkName = pkName;
    }

    public String getTable2PkName() {
        return (this.table2PkName);
    }

    public void setTable1SearchFieldName(String fieldName) {
        this.table1SearchFieldName = fieldName;
    }

    public String getTable1SearchFieldName() {
        return (this.table1SearchFieldName);
    }

    public void setTable1SearchFieldValue(String fieldName) {
        this.table1SearchFieldValue = fieldName;
    }

    public String getTable1SearchFieldValue() {
        return (this.table1SearchFieldValue);
    }

    public void setTable2SearchFieldName(String fieldName) {
        this.table2SearchFieldName = fieldName;
    }

    public String getTable2SearchFieldName() {
        return (this.table2SearchFieldName);
    }

    public void setTable2SearchFieldValue(String fieldName) {
        this.table2SearchFieldValue = fieldName;
    }

    public String getTable2SearchFieldValue() {
        return (this.table2SearchFieldValue);
    }

    public void setTruncateTo(String truncateTo) {
        this.truncateTo = truncateTo;
    }

    public String getTruncateTo() {
        return (this.truncateTo);
    }

    public void setColumn2(String column) {
        this.column2 = column;
    }

    public String getColumn2() {
        return (this.column2);
    }

    public void setIsNull(String column) {
        this.isNull = column;
    }

    public String getIsNull() {
        return (this.isNull);
    }

    private Log log = LogFactory.getLog(this.getClass());

    public int doStartTag() {
        log.debug("Executing RelationDataTag.... ..... .....");
        boolean validation = true;
        List result = new ArrayList();
        int truncate = -1;
        boolean activateColumn2 = (column2 != null && !column2.equals(""));
        try {
            if (id.equals("") || table1.equals("") || table2.equals("") || column.equals("") || table1FkName.equals("") ||
                    table2PkName.equals("") || table1SearchFieldName.equals("") || table1SearchFieldValue.equals("") ||
                    ((table2SearchFieldName != null && !table2SearchFieldName.equals("")) && (table2SearchFieldValue == null || table2SearchFieldValue.equals("")))) {
                validation = false;
            }
            if (truncateTo != null && (truncateTo != null && truncateTo.length() > 0)) {
                truncate = Integer.parseInt(truncateTo);
            }
        }
        catch (NullPointerException npe) {
            validation = false;
            //npe.printStackTrace();
        }
        catch (NumberFormatException nfe) {
            validation = false;
            //nfe.printStackTrace();
        }

        if (validation) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT DISTINCT table2.").append(column);
            if (activateColumn2) {
                sql.append(", table2.").append(column2);
            }

            sql.append(" FROM ").append(table1).append(" AS table1, ")
                    .append(table2).append(" as table2 ").append(" WHERE table1.").append(table1SearchFieldName)
                    .append("=").append(table1SearchFieldValue).append(" AND table1.").append(table1FkName)
                    .append("=table2.").append(table2PkName);

            if (table2SearchFieldName != null && table2SearchFieldValue != null && !table2SearchFieldName.equals("") && !table2SearchFieldValue.equals("")) {
                sql.append(" AND table2.").append(table2SearchFieldName).append("=").append(table2SearchFieldValue);
            }
            if (isNull != null && isNull.length() > 0) {
                sql.append(" AND table2." + isNull + " IS NULL");
            }

            sql.append(" ORDER BY table2." + column);
            result = QueryUtil.i.executeQuery(sql.toString());

            if (truncate > 0) {
                List tList = new ArrayList();
                boolean limitReached = false;
                int size = 0;
                Iterator i = result.iterator();
                while (i.hasNext() && !limitReached) {
                    Map result_i = (Map) i.next();
                    String cad = result_i.get(column).toString();
                    DTO dto = new DTO();
                    if (cad.length() < truncate) {
                        dto.put(column, cad + (i.hasNext() ? "," : ""));
                        truncate = (truncate - cad.length()) - 2;
                    } else {
                        dto.put(column, cad.substring(0, (truncate - 2 >= 0 ? truncate : 0)) + "..");
                        limitReached = true;
                    }

                    if (activateColumn2) {
                        dto.put(column2, result_i.get(column2).toString());
                    }
                    tList.add(dto);
                }
                result = tList;
            }
            pageContext.getRequest().setAttribute(id, result);
            pageContext.getRequest().setAttribute(id + "_size", new Integer(result.size()).toString());
        }
        return (SKIP_BODY);
    }
}
