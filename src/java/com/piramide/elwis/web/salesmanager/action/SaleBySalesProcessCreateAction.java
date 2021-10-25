package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SaleBySalesProcessCreateAction extends SalesProcessManagerAction {

    @Override
    protected boolean updateForm(DefaultForm defaultForm, HttpServletRequest request) {
        if (saveButtonIsPressed(request)) {
            return false;
        }

        String contactId = (String) defaultForm.getDto("contactId");
        if (GenericValidator.isBlankOrNull(contactId)) {
            defaultForm.setDto("contactPersonId", "");
            defaultForm.setDto("netGross", "");
            return true;
        }

        String salesProcessId = (String) defaultForm.getDto("processId");
        ActionDTO actionDTO = Functions.getActionDTO(contactId, salesProcessId, request);
        defaultForm.setDto("contactPersonId", actionDTO.get("contactPersonId"));
        defaultForm.setDto("currencyId", actionDTO.get("currencyId"));
        defaultForm.setDto("netGross", actionDTO.get("netGross"));

        Boolean haveActionPositions = (Boolean) actionDTO.get("haveActionPositions");
        if (!haveActionPositions) {
            ActionErrors errors = new ActionErrors();
            errors.add("haveActionPositions",
                    new ActionError("Sale.error.actionWithoutPositions",
                            JSPHelper.getMessage(request, "Sale.salesProcessAction")));

            saveErrors(request, errors);
        }
        return true;
    }

    private boolean saveButtonIsPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }
}
