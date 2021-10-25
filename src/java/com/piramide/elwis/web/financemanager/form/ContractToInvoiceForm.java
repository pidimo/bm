package com.piramide.elwis.web.financemanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L.
 * Form to validate and manage selected contracts to invoice
 * from contract to invoice list
 *
 * @author Miky
 * @version $Id: ContractToInvoiceForm.java 19-nov-2008 18:06:04 $
 */
public class ContractToInvoiceForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getSelected() {
        List list = (List) this.getDto("selected");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setSelected(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("selected", Arrays.asList(checkArray));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ContractToInvoiceForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("selected") == null) {
            setDto("selected", new ArrayList());
        }

        List idsList = new ArrayList();
        if (Boolean.valueOf(getDto("isCreateAll").toString())) {
            //all button is pressed
            String allPrintIds = (String) getDto("allComposeIds");
            if (!GenericValidator.isBlankOrNull(allPrintIds)) {
                String[] idsArray = allPrintIds.split(",");
                idsList = Arrays.asList(idsArray);
            } else {
                errors.add("emptyAll", new ActionError("Contract.toInvoice.emptyAllError"));
            }
        } else {
            //validate selected
            idsList = (List) getDto("selected");
            if (idsList.isEmpty()) {
                errors.add("empty", new ActionError("Contract.toInvoice.emptyError"));
            }
        }

        if (errors.isEmpty()) {
            //add in request contracts to invoice
            request.setAttribute("contractIdsList", idsList);
            request.setAttribute("dateFilterList", getDto("listDateFilter"));
        }

        return errors;
    }
}

