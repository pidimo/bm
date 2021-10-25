package com.piramide.elwis.web.common.taglib;

import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CategorySelectTag.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CategorySelectTag extends BodyTagSupport {

    private Log log = LogFactory.getLog(this.getClass());

    String categoryId;
    String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public CategorySelectTag() {
        this.categoryId = null;
        this.fieldName = null;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int doEndTag() throws JspException {
        doTag();
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    public int doStartTag() throws JspException {
        return (SKIP_BODY);
    }


    public void doTag() {
        String label = "";
        StringBuffer sql = new StringBuffer();
        List result = new ArrayList();

        if (categoryId != "") {
            sql.append("select  categoryname   from category where categoryid =").append(categoryId);
            result = QueryUtil.i.executeQuery(sql.toString());

            Map mapi = (Map) result.get(0);
            label = (String) mapi.get("categoryname");
            JspWriter out = pageContext.getOut();
            try {
                out.print(label);   //the one that send the values concatenated by commas to intefaz

                pageContext.getRequest().setAttribute("dto(title)", label);
                pageContext.getRequest().setAttribute("dto(tableId)", mapi.get("tableid"));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
