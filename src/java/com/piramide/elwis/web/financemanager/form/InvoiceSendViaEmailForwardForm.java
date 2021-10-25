package com.piramide.elwis.web.financemanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.2
 */
public class InvoiceSendViaEmailForwardForm extends DefaultForm {
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
        log.debug("Excecuting validate InvoiceSendViaEmailForwardForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        String invoiceIds = null;

        if (getDto("isAllSendInvoice") != null && Boolean.valueOf(getDto("isAllSendInvoice").toString())) {
            invoiceIds = (String) getDto("allSendInvoiceIds");
        } else {
            List<String> invoiceIdList = (List<String>) this.getDto("selected");
            if (invoiceIdList != null) {
                for (String id : invoiceIdList) {
                    if (invoiceIds != null) {
                        invoiceIds = invoiceIds + "," + id;
                    } else {
                        invoiceIds = id;
                    }
                }
            }
        }

        if (GenericValidator.isBlankOrNull(invoiceIds)) {
            errors.add("empty", new ActionError("Invoice.sendViaEmail.emptyError"));
        }

        if (errors.isEmpty()) {
            request.setAttribute("invoiceIdsToSend", invoiceIds);
            request.setAttribute("totalInvoiceIds", countInvoicesToSend(invoiceIds));
        }

        return errors;
    }

    private Integer countInvoicesToSend(String invoiceIds) {
        Integer total = 0;

        if (!GenericValidator.isBlankOrNull(invoiceIds)) {
            String[] idsArray = invoiceIds.split(",");
            total = idsArray.length;
        }
        return total;
    }

}

