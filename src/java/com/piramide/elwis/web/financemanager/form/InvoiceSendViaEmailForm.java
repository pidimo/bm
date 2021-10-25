package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.admin.CompanyReadLightCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Form to validate invoices to send via email
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class InvoiceSendViaEmailForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate InvoiceSendViaEmailForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        validateCompanyInvoiceTemplate(errors, request);

        return errors;
    }

    private void validateCompanyInvoiceTemplate(ActionErrors errors, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());

        //get the default configured template for send invoice via email
        CompanyReadLightCmd companyReadLightCmd = new CompanyReadLightCmd();
        companyReadLightCmd.putParam("companyId", companyId);

        Integer templateId = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadLightCmd, null);

            if (resultDTO.containsKey("invoiceMailTemplateId") && resultDTO.get("invoiceMailTemplateId") != null) {
                templateId = (Integer) resultDTO.get("invoiceMailTemplateId");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }

        if (templateId == null) {
            errors.add("template", new ActionError("Invoice.sendViaEmail.error.invoiceTemplate"));
        }
    }
}

