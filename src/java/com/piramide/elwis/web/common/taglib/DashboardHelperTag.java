package com.piramide.elwis.web.common.taglib;

import org.alfacentauro.fantabulous.controller.ResultList;
import org.alfacentauro.fantabulous.result.FieldHash;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: DashboardHelperTag.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 08-04-2005 03:45:27 PM ivan Exp $
 */
public class DashboardHelperTag extends TagSupport {
    private Log log = LogFactory.getLog(this.getClass());

    private String fantabulousListName;
    private String rowsPerPage;

    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public void release() {
        super.release();
    }

    public int doEndTag() throws JspException {
        doTag();
        return EVAL_PAGE;
    }

    private void doTag() {
        log.debug("Executing dashboardHelper tag...");

        Collection newResult = new ArrayList();
        ResultList resultList = (ResultList) pageContext.getRequest().getAttribute(fantabulousListName);


        Integer rows = Integer.valueOf(this.rowsPerPage);
        if (resultList != null) {
            if (rows.intValue() < resultList.getResult().size()) {

                log.debug("Truncating the result of the list : " + fantabulousListName + "...");
                for (Iterator iterator = resultList.getResult().iterator(); iterator.hasNext();) {
                    FieldHash fieldHash = (FieldHash) iterator.next();

                    if (newResult.size() < rows.intValue()) {
                        newResult.add(fieldHash);
                    } else {
                        break;
                    }
                }

                resultList.setResult(newResult);
            }
        }
    }


    public String getFantabulousListName() {
        return fantabulousListName;
    }

    public void setFantabulousListName(String fantabulousListName) {
        this.fantabulousListName = fantabulousListName;
    }

    public String getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(String rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

}
