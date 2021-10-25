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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Titus
 * @version SupplierSelectTag.java, v 2.0 May 10, 2004 7:38:05 PM
 */
public class SupplierSelectTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());
    String name;
    String property;
    String styleClass;
    String tabindex;
    boolean firstEmpty;
    boolean readonly;
    String readOnly;
    String readOnlyStyle;
    String var;
    String withAddress = null;
    String aliasAddess = null;
    private String value;

    public SupplierSelectTag() {
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        styleClass = null;
        tabindex = null;
        firstEmpty = true;
    }

    private Object evalAttr(String attrName, String attrValue, Class attrType)
            throws JspException, NullAttributeException {
        return EvalHelper.eval("supplierSelect", attrName, attrValue, attrType, this, pageContext);
    }


    private void evaluateExpressions() throws JspException {
        try {
            setWithAddress((String) evalAttr("withAddress", getWithAddress(), String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            log.debug("ReadOnly:" + readOnly);
            readonly = ((Boolean) evalAttr("readOnly", getReadOnly(), Boolean.class)).booleanValue();
        } catch (NullAttributeException ex) {
        }
        try {
            setValue((String) evalAttr("value", getValue(), String.class));
        } catch (NullAttributeException ex) {
        }

    }

    public int doEndTag() throws JspException {
        doTag();
        return (EVAL_PAGE);
    }

    /**
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        evaluateExpressions();
        return (SKIP_BODY);

    }

    public void release() {
        super.release();
        setWithAddress(null);
    }


    public void doTag() {

        StringBuffer sql = new StringBuffer();

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

            log.debug("beanId = " + beanPropertyId);
            log.debug("property = " + property);
            log.debug("styleClass = " + styleClass);
            log.debug("tabindex = " + tabindex);
            log.debug("firstEmpty = " + firstEmpty);
            log.debug("companyId = " + user.getValue("companyId"));

            sql.append("SELECT supplierid AS id, ").append(" a.name1 as lastname, a.name2 as name ")
                    .append(" FROM  supplier as s, address as a").append(" WHERE s.companyid=")
                    .append(user.getValue("companyId"))
                    .append(" and a.addressid = s.supplierid ");

            if (withAddress != null) {
                sql.append(" AND ")
                        .append((aliasAddess == null) ? "addressid" : aliasAddess)
                        .append("=")
                        .append(withAddress);
            }
            sql.append(" ORDER BY ").append("a.name1, a.name2  ASC;");
            log.debug("SQL-TAG:" + sql.toString());
            List result = QueryUtil.i.executeQuery(sql.toString());

            try {
                JspWriter out = pageContext.getOut();
                Iterator it = result.iterator();
                out.println("<SELECT NAME=\"" + property + "\"" + ((styleClass != null) ? " class=\"" +
                        styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : "") +
                        ((readonly) ? " disabled = \"" + "disabled\"" : "")
                        + " >");

                if (firstEmpty) {
                    out.println("<OPTION VALUE=\"\"></OPTION>");
                }

                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    out.println("<OPTION VALUE=\"" + item.get("id") + "\"");
                    if ("".equals(beanPropertyId) || beanPropertyId == null) {
                        beanPropertyId = value;
                    }

                    if (item.get("id").equals(beanPropertyId)) {
                        out.println(" selected");
                        if ((var != null) && !("".equals(var))) {
                            pageContext.getRequest().setAttribute(var, item.get("id"));
                        }
                    }
                    out.println(">");
                    out.println(item.get("lastname") + "" + ((item.get("name") != null) ? ", " + item.get("name") : ""));
                    out.println("</OPTION>");
                }
                out.println(" </SELECT>");
            } catch (java.io.IOException ex) {

            }
        } catch (Exception e) {
            log.error("Error retrieving the employeeByOffice select tag:", e);
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
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

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getReadOnlyStyle() {
        return readOnlyStyle;
    }

    public void setReadOnlyStyle(String readOnlyStyle) {
        this.readOnlyStyle = readOnlyStyle;
    }

    public String getWithAddress() {
        return withAddress;
    }

    public void setWithAddress(String withAddress) {
        this.withAddress = withAddress;
    }

    public String getAliasAddess() {
        return aliasAddess;
    }

    public void setAliasAddess(String aliasAddess) {
        this.aliasAddess = aliasAddess;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
