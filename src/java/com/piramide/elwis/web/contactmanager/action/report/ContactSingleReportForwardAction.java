package com.piramide.elwis.web.contactmanager.action.report;

import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * This action loads the rowsPerPage user preference, in order to be used as communication limit in the report
 *
 * @author alvaro
 * @version $Id: ContactSingleReportForwardAction.java 08-sep-2009 20:41:33
 */
public class ContactSingleReportForwardAction extends ForwardAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContactSingleReportForwardAction..............");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        if (user.getValue("rowsPerPage") != null && user.getValue("rowsPerPage").toString().length() > 0) {
            ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
            reportGeneratorForm.setParameter("communicationsLimit", user.getValue("rowsPerPage"));
        }
        return (super.execute(mapping, form, request, response));
    }
}
