package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.MainCommunicationAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CommunicationSalesProcessAction extends MainCommunicationAction {
    protected ActionForward checkMainReferences(HttpServletRequest request, ActionMapping mapping) {
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

        return null;
    }

    @Override
    protected void hasCreatedCommunications(DefaultForm form, HttpServletRequest request) {
        List<Integer> communicationIdentifiers = (List<Integer>) form.getDto("communicationIdentifiers");
        if (null != communicationIdentifiers && communicationIdentifiers.isEmpty()) {
            super.hasCreatedCommunications(form, request);
            return;
        }

        Boolean hasCreatedSalesProcessContacts =
                (Boolean) form.getDto("hasCreatedSalesProcessContacts");
        if (null == hasCreatedSalesProcessContacts) {
            return;
        }

        if (hasCreatedSalesProcessContacts) {
            return;
        }

        ActionErrors errors = new ActionErrors();
        errors.add("NoCommunicationsCreatedForSalesProcess",
                new ActionError("Communication.msg.NoCommunicationsCreatedForSalesProcess"));
        saveErrors(request.getSession(), errors);
    }
}
