package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SaleBySalesProcessCreateAction extends SaleManagerAction {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        if (!"Success".equals(forward.getName())) {
            return forward;
        }

        DefaultForm defaultForm = (DefaultForm) form;
        ActionForwardParameters parameters = new ActionForwardParameters();
        parameters.add("saleId", defaultForm.getDto("saleId").toString()).
                add("dto(saleId)", defaultForm.getDto("saleId").toString());

        return parameters.forward(forward);
    }

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

    @Override
    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("contactId") != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setPrimKey(request.getParameter("contactId"));
            try {
                EJBFactory.i.findEJB(addressDTO); //address already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The active address was deleted by other user...");
                    errors.add("general", new ActionError("Address.NotFound"));
                    saveErrors(request, errors);
                    return mapping.findForward("MainSearch");
                }
            }
        } else { //no contact selected
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return null;
    }

    private boolean saveButtonIsPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }
}
