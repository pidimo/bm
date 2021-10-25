package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CommunicationContactAction extends MainCommunicationAction {

    protected ActionForward checkMainReferences(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("contactId") != null) {
            errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_ADDRESS, "addressid",
                    request.getParameter("contactId"), errors, new ActionError("Address.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    @Override
    protected void hasCreatedCommunications(DefaultForm form, HttpServletRequest request) {
        if (null != form.getDto("processId")) {
            Boolean hasCreatedSalesProcessContacts =
                    (Boolean) form.getDto("hasCreatedSalesProcessContacts");
            if (null != hasCreatedSalesProcessContacts && !hasCreatedSalesProcessContacts) {
                ActionErrors errors = new ActionErrors();
                errors.add("NoCommunicationsCreatedForSalesProcess",
                        new ActionError("Communication.contact.msg.NoCommunicationsCreatedForSalesProcess"));
                saveErrors(request.getSession(), errors);
            }
        }
        super.hasCreatedCommunications(form, request);
    }

    @Override
    protected ActionForward findSuccessRedirectForward(ActionForward forward, HttpServletRequest request, ActionMapping mapping) {
        if (forward != null) {
            if (isFromCommunicationOverview(request) && "Success".equals(forward.getName())) {
                return mapping.findForward("CommOverviewSearch");
            }
        }

        return super.findSuccessRedirectForward(forward, request, mapping);
    }

    @Override
    protected ActionForward findCancelForward(HttpServletRequest request, ActionMapping mapping) {
        if (isFromCommunicationOverview(request)) {
            return mapping.findForward("CommOverviewSearch");
        } else {
            return super.findCancelForward(request, mapping);
        }
    }

    private boolean isFromCommunicationOverview(HttpServletRequest request) {
        return "true".equals(request.getParameter("isFromCommunicationOverview"));
    }
}
