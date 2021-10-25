package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 18, 2005
 * Time: 11:32:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class HistoryValuesTag extends TagSupport {

    private Log log = LogFactory.getLog(this.getClass());

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    String actionValue;

    public String getActionValue() {
        return actionValue;
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
        String view = "";
        view = JSPHelper.getActionHistoryValue((HttpServletRequest) pageContext.getRequest(), actionValue);
        JspWriter out = pageContext.getOut();
        try {
            out.print(view);   //the one that send the values concatenated by commas to intefaz
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

