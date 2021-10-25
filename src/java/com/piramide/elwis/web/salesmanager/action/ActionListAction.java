package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version ActionListAction.java, v 2.0 May 13, 2004 10:48:26 AM
 */
public class ActionListAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Action  list action execution...");
        String processId = request.getParameter("dto(processId)");
        String name = request.getParameter("dto(processName)");

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_SALESPROCESS, "processid",
                processId, errors, new ActionError("msg.NotFound", name));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }
        addFilter("processId", request.getParameter("processId"));

        return super.execute(mapping, form, request, response);
    }
}