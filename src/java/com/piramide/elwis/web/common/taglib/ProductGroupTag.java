package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: ProductGroupTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ProductGroupTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());
    String name;
    String property;
    String catalogTable;
    String idColumn;
    String labelColumn;
    String styleClass;
    String tabindex;
    boolean firstEmpty;
    boolean readonly;
    String readOnly;
    String var;
    String varname;
    String readOnlyStyle;
    String idCondition;

    public ProductGroupTag() {
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        catalogTable = null;
        idColumn = null;
        labelColumn = null;
        styleClass = null;
        tabindex = null;
        firstEmpty = true;
        readOnlyStyle = null;
    }

    private Object evalAttr(String attrName, String attrValue, Class attrType)
            throws JspException, NullAttributeException {
        return EvalHelper.eval("productGroup", attrName, attrValue, attrType, this, pageContext);
    }


    private void evaluateExpressions() throws JspException {

        try {
            setIdCondition((String) evalAttr("idCondition", getIdCondition(), java.lang.String.class));
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

            StringBuffer sql = new StringBuffer();
            List result = new ArrayList();

            if (idCondition != null && !"".equals(idCondition)) {

                sql.append("SELECT ").append(idColumn).append(" AS id, ").append(labelColumn).append(" AS label FROM ")
                        .append(catalogTable).append(" WHERE companyid=").append(user.getValue("companyId"))
                        .append(" and parentgroupid = ").append(idCondition);
                sql.append(" ORDER BY ").append(labelColumn).append(" ASC;");
                log.debug("SQL-TAG:" + sql.toString());
                result = QueryUtil.i.executeQuery(sql.toString());
            }

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
                            styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : ""));

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
                        out.println(">");
                        out.println(item.get("label"));
                        out.println("</OPTION>");
                    }
                    out.println(" </SELECT>");
                }
            } catch (java.io.IOException ex) {

            }

        } catch (Exception e) {
            log.error("Error retrieving the catalog select tag:", e);
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCatalogTable() {
        return catalogTable;
    }

    public void setCatalogTable(String catalogTable) {
        this.catalogTable = catalogTable;
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

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public boolean isFirstEmpty() {
        return firstEmpty;
    }

    public void setFirstEmpty(boolean firstEmpty) {
        this.firstEmpty = firstEmpty;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getReadOnlyStyle() {
        return readOnlyStyle;
    }

    public void setReadOnlyStyle(String readOnlyStyle) {
        this.readOnlyStyle = readOnlyStyle;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public String getIdCondition() {
        return idCondition;
    }

    public void setIdCondition(String idCondition) {
        this.idCondition = idCondition;
    }
}
