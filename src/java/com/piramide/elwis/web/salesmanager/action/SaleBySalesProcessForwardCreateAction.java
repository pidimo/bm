package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SaleBySalesProcessForwardCreateAction extends DefaultForwardAction {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String salesProcessId = (String) defaultForm.getDto("processId");
        SalesProcessDTO salesProcessDTO = Functions.getSalesProcessDTO(salesProcessId, request);
        defaultForm.setDto("addressId", salesProcessDTO.get("addressId"));
        defaultForm.setDto("addressName", salesProcessDTO.get("addressName"));
        defaultForm.setDto("title", salesProcessDTO.get("processName"));

        setDefaultCustomerInfo(defaultForm, request);

        String contactId = (String) defaultForm.getDto("contactId");
        if (GenericValidator.isBlankOrNull(contactId)) {
            return;
        }

        ActionDTO actionDTO = Functions.getActionDTO(contactId, salesProcessId, request);
        defaultForm.setDto("contactPersonId", actionDTO.get("contactPersonId"));
        defaultForm.setDto("netGross", actionDTO.get("netGross"));
        defaultForm.setDto("currencyId", actionDTO.get("currencyId"));

        Boolean haveActionPositions = (Boolean) actionDTO.get("haveActionPositions");
        if (!haveActionPositions) {
            ActionErrors errors = new ActionErrors();
            errors.add("haveActionPositions",
                    new ActionError("Sale.error.actionWithoutPositions",
                            JSPHelper.getMessage(request, "Sale.salesProcessAction")));
            saveErrors(request, errors);
        }
        super.setDTOValues(defaultForm, request);
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = Functions.existSalesProcess(request);
        if (null != errors) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    private void setDefaultCustomerInfo(DefaultForm defaultForm, HttpServletRequest request) {
        Object addressIdObj = defaultForm.getDto("addressId");
        if (addressIdObj != null && !"".equals(addressIdObj.toString().trim())) {
            Functions.setSaleCustomerDefaultValues(defaultForm, Integer.valueOf(addressIdObj.toString()), request);
        }
    }

}
