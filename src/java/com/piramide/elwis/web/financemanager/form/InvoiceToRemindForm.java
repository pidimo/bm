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
 *
 * @author Miky
 * @version $Id: InvoiceToRemindForm.java 04-nov-2008 12:59:43 $
 */
public class InvoiceToRemindForm extends DefaultForm {
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
        log.debug("Excecuting validate InvoiceToRemindForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("selected") == null) {
            setDto("selected", new ArrayList());
        }

        List invoiceIds = new ArrayList();
        if (Boolean.valueOf(getDto("isCreateAll").toString())) {
            //all button is pressed
            String allPrintIds = (String) getDto("allInvoiceIds");
            if (!GenericValidator.isBlankOrNull(allPrintIds)) {
                String[] printArray = allPrintIds.split(",");
                invoiceIds = Arrays.asList(printArray);
            } else {
                errors.add("emptyAll", new ActionError("Invoice.toRemind.emptyAllError"));
            }
        } else {
            //validate selected
            invoiceIds = (List) getDto("selected");
            if (invoiceIds.isEmpty()) {
                errors.add("empty", new ActionError("Invoice.toRemind.emptyError"));
            }
        }

        if (errors.isEmpty()) {
            //add in request invoices to remind
            request.setAttribute("invoiceIdList", invoiceIds);
        }

        return errors;
    }
}

