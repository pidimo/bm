package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.MainCommunicationAction;
import com.piramide.elwis.web.salesmanager.util.ActionFormUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version ActionManagerAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */

public class ActionManagerAction extends MainCommunicationAction {

    @SuppressWarnings(value = "unchecked")
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DefaultForm actionForm = (DefaultForm) form;

        if (request.getParameter("dto(op)") == null
                && request.getParameter("cmd") == null) {
            actionForm.getDtoMap().putAll(ActionFormUtil.i.getDefaultValuesForCreate(request));
            actionForm.getDtoMap().putAll(ActionFormUtil.i.getDefaultValuesFromSessionUser(request));
            return mapping.findForward("Success");
        }

        User user = RequestUtils.getUser(request);
        actionForm.setDto(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID).toString());

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName())) {
            if (actionForm.getDto("contactId") != null) {
                return new ActionForwardParameters().add("dto(contactId)", actionForm.getDto("contactId").toString()).
                        forward(forward);
            } else {
                return forward;
            }
        }

        return forward;
    }

    protected ActionForward checkMainReferences(HttpServletRequest request,
                                                ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("processId") != null) {
            errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_SALESPROCESS, "processid",
                    request.getParameter("processId"), errors, new ActionError("SalesProcess.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("processid", new ActionError("SalesProcess.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        if (request.getParameter("dto(processId)") != null &&
                request.getParameter("dto(contactId)") != null) {
            errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_ACTION, "processid", "contactid",
                    request.getParameter("dto(processId)"), request.getParameter("dto(contactId)"),
                    errors, new ActionError("msg.NotFound", request.getParameter("dto(note)")));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("Fail");
            }
        }
        return null;
    }
}
