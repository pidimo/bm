package com.piramide.elwis.web.contactmanager.action;

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
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Feb 2, 2005
 * Time: 1:47:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPositionManagerAction extends com.piramide.elwis.web.contactmanager.action.ActionManagerAction {
    private Log log = LogFactory.getLog(ActionManagerAction.class);

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
    }
}