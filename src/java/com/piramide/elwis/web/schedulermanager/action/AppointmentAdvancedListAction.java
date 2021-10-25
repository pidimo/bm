package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.web.schedulermanager.form.AppointmentListForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: AppointmentAdvancedListAction.java 16-feb-2009 18:56:43
 */
public class AppointmentAdvancedListAction extends AppointmentCreateListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("AppointmentCreateListAction................................... execute...");
        AppointmentListForm searchForm = (AppointmentListForm) form;

        if (searchForm.getParameter("addressId") != null) {
            request.setAttribute("addressId", searchForm.getParameter("addressId"));
        }

        return (super.execute(mapping, form, request, response));
    }
}
