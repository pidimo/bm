package com.piramide.elwis.web.admin.action.report;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id CompanyReportAction ${time}
 */
public class CompanyReportAction extends AdminExetendedReportAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        SearchForm myform = (SearchForm) form;
        /*if (null != myform.getParameter("isActive"))
            myform.setParameter("active", "1");
        else
            myform.setParameter("active", "0");
            if (null != myform.getParameter("isTrial"))
            myform.setParameter("trial", "1");
        else
            myform.setParameter("trial", "0");*/

        if ("1".equals(request.getParameter("parameter(isActive)"))) {
            addFilter("active", "1");
        }
        if ("2".equals(request.getParameter("parameter(isActive)"))) {
            addFilter("active", "0");
        }

        return super.execute(mapping, myform, request, response);
    }

}
