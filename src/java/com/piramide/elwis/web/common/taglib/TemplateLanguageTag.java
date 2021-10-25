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
 * @author Ivan
 * @version $Id: TemplateLanguageTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TemplateLanguageTag extends TagSupport {
    private Log log = LogFactory.getLog(this.getClass());
    String name;
    String property;
    String styleClass;
    boolean firstEmpty;
    boolean readonly;
    String readOnly;
    String tabindex;

    String tableName1;
    String key1;
    String relationkey;
    String name1;
    String tableName2;
    String key2;
    String name2;

    public TemplateLanguageTag() {
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        styleClass = null;
        firstEmpty = true;
    }

    private Object evalAttr(String attrName, String attrValue, Class attrType)
            throws JspException, NullAttributeException {
        return EvalHelper.eval("TemplateLanguage", attrName, attrValue, attrType, this, pageContext);
    }

    private void evaluateExpressions() throws JspException {
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
            String sql = null;
            sql = " select " + tableName1 + "." + key1 + " as returnedkey, " +
                    tableName1 + "." + name1 + " as " + name1 + ", " +
                    tableName2 + "." + name2 + " as " + name2 +
                    " from " + tableName1 + " , " + tableName2 +
                    " where " + tableName1 + "." + relationkey + " = " + tableName2 + "." + key2 +
                    " AND " + tableName1 + ".companyid ='" + user.getValue("companyId") + "' " +
                    " ORDER BY " + name1 + " ASC; ";

            log.debug("SQL-TAG:" + sql);
            List result = QueryUtil.i.executeQuery(sql);
            try {
                JspWriter out = pageContext.getOut();
                Iterator it = result.iterator();
                out.println("<SELECT NAME=\"" + property + "\"" + ((styleClass != null) ? " class=\"" +
                        styleClass + "\" " : "") + ((tabindex != null) ? " tabindex=\"" + tabindex + "\"" : "") +
                        ((readonly) ? " disabled = \"" + "disabled\"" : "") +
                        " >");
                if (firstEmpty) {
                    out.println("<OPTION VALUE=\"\"></OPTION>");
                }

                while (it.hasNext()) {
                    Map item = (Map) it.next();
                    out.println("<OPTION VALUE=\"" + item.get("returnedkey") + "\"");
                    if (item.get("returnedkey").equals(beanPropertyId)) {
                        out.println("selected");
                    }
                    out.println(">");
                    out.println(item.get(name1) + "\t" + item.get(name2));
                    out.println("</OPTION>");
                }
                out.println(" </SELECT>");

            } catch (java.io.IOException ex) {
            }
        } catch (Exception e) {
            log.error("Error retrieving the catalog select tag:", e);
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

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isFirstEmpty() {
        return firstEmpty;
    }

    public void setFirstEmpty(boolean firstEmpty) {
        this.firstEmpty = firstEmpty;
    }

    public String getTabindex() {
        return tabindex;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getTableName1() {
        return tableName1;
    }

    public void setTableName1(String tableName1) {
        this.tableName1 = tableName1;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getTableName2() {
        return tableName2;
    }

    public void setTableName2(String tableName2) {
        this.tableName2 = tableName2;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getRelationkey() {
        return relationkey;
    }

    public void setRelationkey(String relationkey) {
        this.relationkey = relationkey;
    }
}
