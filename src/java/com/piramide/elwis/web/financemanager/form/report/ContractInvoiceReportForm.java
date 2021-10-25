package com.piramide.elwis.web.financemanager.form.report;

import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ContractInvoiceReportForm.java 03-feb-2009 18:22:02 $
 */
public class ContractInvoiceReportForm extends ReportGeneratorForm {
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        log.debug("Excecuting validate ContractInvoiceReportForm......." + getParams());
        ActionErrors errors = new ActionErrors();
        errors = super.validate(actionMapping, request);

        if (errors.isEmpty()) {
            Integer fromDate = new Integer(getParameter("fromDate").toString());
            Integer toDate = new Integer(getParameter("toDate").toString());

            if (fromDate >= toDate) {
                errors.add("date", new ActionError("ContractToInvoice.dateError.range", JSPHelper.getMessage(request, "Common.to"), JSPHelper.getMessage(request, "Common.from")));
            }
        }

        return errors;
    }
}
