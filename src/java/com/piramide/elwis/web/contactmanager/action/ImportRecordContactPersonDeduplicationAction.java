package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.3
 */
public class ImportRecordContactPersonDeduplicationAction extends ImportRecordDeduplicationAction {

    @Override
    protected ActionForward findCancelForward(DefaultForm defaultForm, ActionMapping mapping) {
        return getForwardParameters(defaultForm).forward(mapping.findForward("Cancel"));
    }

    @Override
    protected ActionForward findSuccessForward(Integer importRecordId, Integer profileId, DefaultForm defaultForm, ActionMapping mapping, HttpServletRequest request) {
        Integer parentRecordId = new Integer(defaultForm.getDto("parentRecordId").toString());

        if (Functions.importRecordFixedHasDuplicateContactPerson(parentRecordId)) {
            return getForwardParameters(defaultForm).forward(mapping.findForward("SuccessParent"));
        }
        return mapping.findForward("Success");
    }

    @Override
    protected ActionForward validateImportRecordExistence(Integer importRecordId, DefaultForm defaultForm, HttpServletRequest request, ActionMapping mapping) {
        if (super.validateImportRecordExistence(importRecordId, defaultForm, request, mapping) != null) {
            return getForwardParameters(defaultForm).forward(mapping.findForward("Fail"));
        }
        return null;
    }

    private ActionForwardParameters getForwardParameters(DefaultForm defaultForm) {
        ActionForwardParameters parentForwardParameters = new ActionForwardParameters();
        parentForwardParameters.add("importRecordId", defaultForm.getDto("parentRecordId").toString()).
                add("dto(importRecordId)", defaultForm.getDto("parentRecordId").toString()).
                add("dto(profileId)", defaultForm.getDto("profileId").toString());

        return parentForwardParameters;
    }
}
