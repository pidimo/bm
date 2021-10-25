package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.RequestUtils;
import org.apache.strutsel.taglib.utils.EvalHelper;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: ExtendedTableSelectTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ExtendedTableSelectTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());
    String name;
    String property;
    String tableName;       // Name of table
    String primKey;         // Primary Key
    String relationId;
    String labelColumn;     // name of column that was shown

    String styleClass;      // leaf of styles that was applied
    String readOnlyStyle;   // leaf of styles that was applied when read only is true
    String tabindex;
    boolean firstEmpty;     // if the first item is empty
    boolean readonly;       // wen the select field is shown only to read
    String var;             // value selected is returned
    String totalItems;           // total of items
    String varname;         // value of label selected is returned
    String butNot;          // id was is not showed on the select
    String onChange;        // js funtion was applicated on the select

    String tableRelation;   // table with which is related
    String relationField;   // campo por el cual se filtra la relacion
    String withValue;       // valor por el cual se filtra la relacion


    String readOnly;

    String type;


/*
    String catalogTable;    // name of table
    String labelColumn;     // name of column that was shown
    String withAddress = null;
    String aliasAddess = null;
*/

    public ExtendedTableSelectTag() {
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        tableName = null;
        relationId = null;

        primKey = null;
        styleClass = null;
        readOnlyStyle = null;
        tabindex = null;
        firstEmpty = true;
        readonly = false;

        butNot = null;
        onChange = null;

        tableRelation = null;
        relationField = null;
        withValue = null;

    }

    private Object evalAttr(String attrName, String attrValue, Class attrType) throws JspException, NullAttributeException {
        return EvalHelper.eval("extendedTableSelect", attrName, attrValue, attrType, this, pageContext);
    }

    private void evaluateExpressions() throws JspException {
        try {
            setTableRelation((String) evalAttr("tableRelation", getTableRelation(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setRelationId((String) evalAttr("relationId", getRelationId(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setWithValue((String) evalAttr("withValue", getWithValue(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setButNot(((String) evalAttr("butNot", getButNot(), java.lang.String.class)));
        } catch (NullAttributeException ex) {
        }
        try {
            log.debug("ReadOnly:" + readOnly);
            readonly = ((Boolean) evalAttr("readOnly", getReadOnly(), java.lang.Boolean.class)).booleanValue();
        } catch (NullAttributeException ex) {
        }

    }


    public int doEndTag() throws JspException {
        doTag();
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    /**
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        evaluateExpressions();
        return (SKIP_BODY);

    }


    public void doTag() {

        try {

            User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);
            String beanPropertyId;
            Object bean = null;
            try {
                bean = RequestUtils.lookup(pageContext, name, null);
                String beanProperty[] = BeanUtils.getArrayProperty(bean, property);
                beanPropertyId = beanProperty[0];
            } catch (Exception e) {
                beanPropertyId = "";
            }
            String newSql = "";
            if ("simple".equals(type.toLowerCase())) {
                newSql = getSimpleSQL(user);
                log.debug("ExtendedTableSelect-TAG:" + newSql.toString());
                createTagHTML(newSql, beanPropertyId);
            }
            if ("relation".equals(type.toLowerCase())) {
                newSql = getRelationSQL(user);
                log.debug("ExtendedTableSelect-TAG:" + newSql.toString());
                createTagHTML(newSql, beanPropertyId);
            }
            if ("productgroup".equals(type.toLowerCase())) {
                newSql = getProductGroup(user);
                log.debug("ExtendedTableSelect-TAG:" + newSql.toString());
                createTagHTML(newSql, beanPropertyId);
            }
            if ("butnot".equals(type.toLowerCase())) {
                newSql = getButNotSQL(user);
                log.debug("ExtendedTableSelect-TAG:" + newSql.toString());
                createTagHTML(newSql, beanPropertyId);
            }
            if ("categoryandtables".equals(type.toLowerCase())) {
                newSql = getCategoryAndTables(user);
                log.debug("ExtendedTableSelect-TAG:" + newSql.toString());
                categoryAndTablesTagHTML(newSql, beanPropertyId, user);
            }

        } catch (Exception e) {
            log.error("Error retrieving the catalog select tag:", e);
        }
    }

    private String getSimpleSQL(User user) {
        return "SELECT " + primKey + " AS id, " + labelColumn + " AS label FROM " + tableName +
                " WHERE companyid=" + user.getValue("companyId") +
                " ORDER BY " + labelColumn + " ASC; ";
    }

    private String getRelationSQL(User user) {
        return "SELECT " + tableName + "." + primKey + " AS id, " + tableName + "." + labelColumn + " AS label "
                + " FROM " + tableName + ", " + tableRelation + " relation "
                + " WHERE " + tableName + ".companyid = " + user.getValue("companyId")
                + " AND relation." + relationField + " = " + tableName + "." + relationId
                + " AND " + tableRelation + "." + relationField + " = " + withValue
                + " ORDER BY " + labelColumn + " ASC; ";
    }

    private String getProductGroup(User user) {
        return "SELECT " + tableName + "." + primKey + " AS id, " + labelColumn + " AS label "
                + " FROM " + tableName
                + " WHERE " + tableName + ".companyid = " + user.getValue("companyId")
                + " AND " + tableName + "." + relationId + ("".equals(withValue) ? " is null " : "=" + withValue)
                + " ORDER BY " + labelColumn + " ASC; ";
    }

    private String getButNotSQL(User user) {
        return "SELECT " + primKey + " AS id, " + labelColumn + " AS label FROM " + tableName +
                " WHERE companyid=" + user.getValue("companyId")
                + " AND " + tableName + "." + primKey + " <> '" + butNot + "'"
                + " ORDER BY " + labelColumn + " ASC; ";
    }

    private String getCategoryAndTables(User user) {
        return "SELECT " + primKey + " AS id, " + labelColumn + " AS label, tableid as tableid FROM category " +
                "WHERE companyid = " + user.getValue("companyId")
                + " ORDER BY " + labelColumn + " ASC; ";
    }

    private void createTagHTML(String newSql, String beanPropertyId) {
        List result = QueryUtil.i.executeQuery(newSql);
        try {
            JspWriter out = pageContext.getOut();
            Iterator it = result.iterator();
            if (readonly) {
                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    if (item.get("id").equals(beanPropertyId)) {
                        out.println("<INPUT TYPE=\"HIDDEN\"" + " NAME=\"" + property + "\" VALUE=\"" + item.get("id") + "\">");
                        out.print(item.get("label"));
                        break;
                    }
                }

            } else {
                out.println("<SELECT NAME=\"" + property + "\"" + ((styleClass != null) ? " class=\"" +
                        styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : "")
                        + ((onChange != null) ? " onchange=\"" + onChange + "\"" : "") + " >");

                if (firstEmpty) {
                    out.println("<OPTION VALUE=\"\"></OPTION>");
                }
                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    out.println("<OPTION VALUE=\"" + item.get("id") + "\"");

                    if (item.get("id").equals(beanPropertyId)) {
                        out.println(" selected");
                        if ((var != null) && !("".equals(var))) {
                            pageContext.getRequest().setAttribute(var, item.get("id"));
                        }
                        if ((varname != null) && !("".equals(varname))) {
                            pageContext.getRequest().setAttribute(varname, item.get("label"));
                        }
                    }
                    if ((totalItems != null) && !("".equals(totalItems))) {
                        pageContext.setAttribute(totalItems, new Integer(result.size()));
                    }
                    out.println(">");
                    out.println(item.get("label"));
                    out.println("</OPTION>");
                }
                out.println(" </SELECT>");
            }
        } catch (java.io.IOException ex) {
        }
    }

    private void categoryAndTablesTagHTML(String newSql, String beanPropertyId, User user) {
        List result = QueryUtil.i.executeQuery(newSql);
        try {
            JspWriter out = pageContext.getOut();
            Iterator it = result.iterator();
            if (readonly) {
                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    String table = "";
                    if ((item.get("tableid").equals(ContactConstants.CUSTOMER_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.customer");
                    }
                    if ((item.get("tableid").equals(ContactConstants.CONTACTPERSON_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.contactPerson");
                    }
                    if ((item.get("tableid").equals(ContactConstants.PRODUCT_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.product");
                    }
                    if ((item.get("tableid").equals(ContactConstants.ADDRESS_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.address");
                    }

                    if (item.get("id").equals(beanPropertyId)) {
                        out.println("<INPUT TYPE=\"HIDDEN\"" + " NAME=\"" + property + "\" VALUE=\"" + item.get("id") + "\">");
                        out.print(item.get("label") + "(" + table + ")");
                        break;
                    }
                }

            } else {
                out.println("<SELECT NAME=\"" + property + "\"" + ((styleClass != null) ? " class=\"" +
                        styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : "")
                        + ((onChange != null) ? " onchange=\"" + onChange + "\"" : "") + " >");

                if (firstEmpty) {
                    out.println("<OPTION VALUE=\"\"></OPTION>");
                }
                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    String table = "";
                    if ((item.get("tableid").equals(ContactConstants.CUSTOMER_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.customer");
                    }
                    if ((item.get("tableid").equals(ContactConstants.CONTACTPERSON_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.contactPerson");
                    }
                    if ((item.get("tableid").equals(ContactConstants.PRODUCT_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.product");
                    }
                    if ((item.get("tableid").equals(ContactConstants.ADDRESS_CATEGORY))) {
                        table = JSPHelper.getMessage(new Locale(user.getValue("locale").toString()), "Category.address");
                    }

                    out.println("<OPTION VALUE=\"" + item.get("id") + "\"");

                    if (item.get("id").equals(beanPropertyId)) {
                        out.println(" selected");
                        if ((var != null) && !("".equals(var))) {
                            pageContext.getRequest().setAttribute(var, item.get("id"));
                        }
                        if ((varname != null) && !("".equals(varname))) {
                            pageContext.getRequest().setAttribute(varname, item.get("label") + "(" + table + ")");
                        }
                    }
                    if ((totalItems != null) && !("".equals(totalItems))) {
                        pageContext.setAttribute(totalItems, new Integer(result.size()));
                    }
                    out.println(">");
                    out.println(item.get("label") + "(" + table + ")");
                    out.println("</OPTION>");
                }
                out.println(" </SELECT>");
            }
        } catch (java.io.IOException ex) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimKey() {
        return primKey;
    }

    public void setPrimKey(String primKey) {
        this.primKey = primKey;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getLabelColumn() {
        return labelColumn;
    }

    public void setLabelColumn(String labelColumn) {
        this.labelColumn = labelColumn;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getReadOnlyStyle() {
        return readOnlyStyle;
    }

    public void setReadOnlyStyle(String readOnlyStyle) {
        this.readOnlyStyle = readOnlyStyle;
    }

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public boolean isFirstEmpty() {
        return firstEmpty;
    }

    public void setFirstEmpty(boolean firstEmpty) {
        this.firstEmpty = firstEmpty;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public String getButNot() {
        return butNot;
    }

    public void setButNot(String butNot) {
        this.butNot = butNot;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getTableRelation() {
        return tableRelation;
    }

    public void setTableRelation(String tableRelation) {
        this.tableRelation = tableRelation;
    }

    public String getRelationField() {
        return relationField;
    }

    public void setRelationField(String relationField) {
        this.relationField = relationField;
    }

    public String getWithValue() {
        return withValue;
    }

    public void setWithValue(String withValue) {
        this.withValue = withValue;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }
}
