package com.piramide.elwis.web.contactmanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Mar 29, 2005
 * Time: 1:59:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class BankAccountAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }
        ActionForward forward = mapping.findForward("Success");
        DefaultForm defaultForm = (DefaultForm) form;
        String value = (String) defaultForm.getDto("value");

        if (value != null && !"".equals(value)) {
            defaultForm.setDto("bankId", value);
            defaultForm.setDto("value", null);
            forward = new ActionForward(mapping.getInput());
        } else {
            ActionErrors errors = defaultForm.validate(mapping, request);

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("Fail"));
            } else {
                forward = super.execute(mapping, defaultForm, request, response);
            }
        }
        return forward;
    }
}
