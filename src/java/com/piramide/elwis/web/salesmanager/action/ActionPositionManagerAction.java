package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yumi
 * @version ActionManagerAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */

public class ActionPositionManagerAction extends ActionManagerAction {
    private Log log = LogFactory.getLog(ActionPositionManagerAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ActionPosition manager action execution...");

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_ACTION, "processid", "contactid",
                request.getParameter("dto(processId)"), request.getParameter("dto(contactId)"), errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(note)")));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        DefaultForm defaultForm = (DefaultForm) form;
        if (request.getParameter("create") == null) {
            if (null != request.getParameter("new")) {
                setNextActionPositionNumber(defaultForm, request);
            }

            forward = super.execute(mapping, defaultForm, request, response); //super execute
        } else {
            setNextActionPositionNumber(defaultForm, request);
            forward = mapping.findForward("create");
        }

        return forward;
    }

    private void setNextActionPositionNumber(DefaultForm defaultForm, HttpServletRequest request) {
        String processId = request.getParameter("dto(processId)");
        String contactId = request.getParameter("dto(contactId)");
        Integer maxNumber = Functions.getNextActionPositionNumber(processId, contactId, request);
        defaultForm.setDto("number", maxNumber);
        defaultForm.setDto("amount", 1);
        defaultForm.setDto("contactId", contactId);
        defaultForm.setDto("processId", processId);
    }
}
