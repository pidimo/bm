package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SaleBySalesProcessForm extends DefaultForm {
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (!existsSalesProcess()) {
            return new ActionErrors();
        }

        if (!saveButtonIsPressed(request)) {
            ActionErrors errors = new ActionErrors();
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");
            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);

        com.piramide.elwis.web.common.el.Functions.emptyOrOnlyOneSelectedValidation(this, "sentAddressId", "additionalAddressId", "Sale.sentAddress", "Sale.additionalAddress", errors, request);

        ActionError netGrossChangeError = Functions.validateNetGrossField(this, request);
        if (null != netGrossChangeError) {
            errors.add("netGrossChangeError", netGrossChangeError);
        }

        ActionError actionProbabilityError = validateActionProbability(request);
        if (null != actionProbabilityError) {
            errors.add("actionProbabilityError", actionProbabilityError);
        }

        ActionError productsWitoutVatError = validateSalesProcessActionPositions(request);
        if (null != productsWitoutVatError) {
            errors.add("productsWitoutVatError", productsWitoutVatError);
        }

        if (errors.isEmpty()) {
            List<ActionError> errorList = Functions.checkNetGrossChange(this, request);
            for (int i = 0; i < errorList.size(); i++) {
                ActionError actionError = errorList.get(i);
                errors.add("error_" + i, actionError);
            }
        }
        return errors;
    }

    protected boolean existsSalesProcess() {
        String processId = (String) getDto("processId");
        return Functions.existsSalesProcess(processId);
    }

    protected ActionError validateActionProbability(HttpServletRequest request) {
        String contactId = (String) getDto("contactId");
        if (GenericValidator.isBlankOrNull(contactId)) {
            return null;
        }

        String processId = (String) getDto("processId");
        ActionDTO actionDTO = Functions.getActionDTO(contactId, processId, request);
        if (null == actionDTO) {
            return null;
        }

        Integer probability = (Integer) actionDTO.get("probability");
        if (probability != 100) {
            return new ActionError("Sale.error.actionProbabilityError", actionDTO.get("note"));
        }

        return null;
    }

    protected ActionError validateSalesProcessActionPositions(HttpServletRequest request) {
        String contactId = (String) getDto("contactId");
        String processId = (String) getDto("processId");
        if (GenericValidator.isBlankOrNull(contactId) || GenericValidator.isBlankOrNull(processId)) {
            return null;
        }

        List<ProductDTO> productsWithoutVat =
                Functions.getActionPositionProductsWithoutVat(contactId, processId, request);
        if (productsWithoutVat.isEmpty()) {
            return null;
        }

        return new ActionError("Sale.error.actionPositionWithoutVat");
    }

    protected boolean saveButtonIsPressed(HttpServletRequest request) {
        return null != request.getParameter("save") || null != request.getParameter("invoiceFromSale");
    }
}
