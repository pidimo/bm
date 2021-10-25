package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.cmd.financemanager.InvoiceDocumentCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceReminderDocumentCmd;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 * Class to generate documents related with invoices
 *
 * @author Miky
 * @version $Id: GenerateDocumentUtil.java 18-sep-2008 15:05:39 $
 */
public class InvoiceGenerateDocumentUtil {
    private Log log = LogFactory.getLog(this.getClass());

    private Integer invoiceDocumentId;
    private Integer reminderDocumentId;
    private ActionErrors errors;

    public InvoiceGenerateDocumentUtil() {
        errors = new ActionErrors();
        invoiceDocumentId = null;
    }

    /**
     * Generate and save invoice document in data base
     *
     * @param invoiceId
     * @param mapping
     * @param request
     * @return errors
     */
    public ActionErrors generateInvoiceDocument(Integer invoiceId, ActionMapping mapping, HttpServletRequest request) {
        log.debug("Generate with invoiceId:" + invoiceId);
        errors = new ActionErrors();

        User user = RequestUtils.getUser(request);

        InvoiceDocumentCmd documentCmd = new InvoiceDocumentCmd();
        documentCmd.putParam("invoiceId", invoiceId);
        documentCmd.putParam("userId", user.getValue("userId"));
        documentCmd.putParam("companyId", user.getValue("companyId"));
        documentCmd.putParam("userAddressId", user.getValue("userAddressId"));
        documentCmd.putParam("requestLocale", user.getValue("locale"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(documentCmd, request);
            log.debug("RES:" + resultDTO);
            if (resultDTO.isFailure()) {
                errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            } else {
                invoiceDocumentId = new Integer(resultDTO.get("documentId").toString());
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
            errors.add("application", new ActionError("Invoice.document.generate.error"));
        }
        return errors;
    }

    /**
     * Generate and save reminder document
     *
     * @param reminderId
     * @param mapping
     * @param request
     * @return
     */
    public ActionErrors generateReminderDocument(Integer reminderId, ActionMapping mapping, HttpServletRequest request) {
        log.debug("Generate with reminderId:" + reminderId);
        errors = new ActionErrors();

        User user = RequestUtils.getUser(request);

        InvoiceReminderDocumentCmd reminderDocumentCmd = new InvoiceReminderDocumentCmd();
        reminderDocumentCmd.putParam("reminderId", reminderId);
        reminderDocumentCmd.putParam("userId", user.getValue("userId"));
        reminderDocumentCmd.putParam("companyId", user.getValue("companyId"));
        reminderDocumentCmd.putParam("userAddressId", user.getValue("userAddressId"));
        reminderDocumentCmd.putParam("requestLocale", user.getValue("locale"));
        reminderDocumentCmd.putParam("reminderLabel", JSPHelper.getMessage(request, "InvoiceReminder.reminder"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderDocumentCmd, request);
            log.debug("RES:" + resultDTO);
            if (resultDTO.isFailure()) {
                errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            } else {
                reminderDocumentId = new Integer(resultDTO.get("documentId").toString());
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
            errors.add("application", new ActionError("InvoiceReminder.document.generate.error"));
        }
        return errors;
    }

    public Integer getInvoiceDocumentId() {
        return invoiceDocumentId;
    }

    public Integer getReminderDocumentId() {
        return reminderDocumentId;
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
