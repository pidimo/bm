package com.piramide.elwis.web.common.taglib;

import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 25, 2005
 * Time: 4:37:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class WriteTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());

    String id;
    String fieldName;
    String tableName;
    String name;
    String value;

    public WriteTag() {
        this.id = null;
        this.fieldName = null;
        this.tableName = null;
        this.name = null;
        this.value = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        StringBuffer sql = new StringBuffer();
        StringBuffer sql2 = new StringBuffer();
        List result = new ArrayList();
        List res = new ArrayList();
        StringBuffer label = new StringBuffer();
        String id_User = "";

        sql.append("select ").append("addressid").append(" from ").append(tableName).append(" where ").append(id).append("=").append(value);
        result = QueryUtil.i.executeQuery(sql.toString());
        Iterator i = result.iterator();
        while (i.hasNext()) {
            Map map = (Map) i.next();
            id_User = (String) map.get("addressid");
        }
        log.debug("name  is TRUE ... " + value);
        sql2.append("select name1, name2  from address ").append(" where ").append("addressid=").append(id_User);
        res = QueryUtil.i.executeQuery(sql2.toString());
        Iterator iterator = res.iterator();
        while (iterator.hasNext()) {
            Map map = (Map) iterator.next();
            label.append(map.get("name1"));
            if (!"".equals(map.get("name2")) && (map.get("name2") != null)) {
                label.append(", ");
                label.append(map.get("name2"));
            }
        }
        JspWriter out = pageContext.getOut();
        try {
            out.print(label.toString());   //the one that send the values concatenated by commas to intefaz
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

