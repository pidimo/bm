package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.financemanager.InvoiceCmd;
import com.piramide.elwis.cmd.financemanager.InvoicePaymentCmd;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoicePaymentInOneStepForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        boolean isSaveButtonPressed = isButtonPressed("save", request);
        boolean isSaveAndNewButtonPresses = isButtonPressed("SaveAndNew", request);
        if (!isSaveButtonPressed && !isSaveAndNewButtonPresses) {
            ActionErrors errors = new ActionErrors();
            settingUpSkipErrors(request, errors);
            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);
        validateRequiredBankAccount(errors, request);

        ActionError invoiceIdError = validateInvoiceId(request);
        if (null != invoiceIdError) {
            errors.add("invoiceIdError", invoiceIdError);
        }

        ActionError amountError = validateAmount(request, errors);
        if (null != amountError) {
            errors.add("amountError", amountError);
        }

        return errors;
    }

    private ActionError validateAmount(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return null;
        }

        BigDecimal amount = (BigDecimal) getDto("amount");

        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.putParam("invoiceId", Integer.valueOf(getDto("invoiceId").toString()));
        invoicePaymentCmd.setOp("canPayInvoice");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePaymentCmd, request);
            Boolean canPayInvoice = (Boolean) resultDTO.get("canPayInvoice");
            if (!canPayInvoice) {
                return new ActionError("InvoicePayment.openAmountIsZeroError");
            }

            BigDecimal openAmount = (BigDecimal) resultDTO.get("openAmount");

            if ("create".equals(getDto("op").toString())) {
                if (openAmount.compareTo(amount) == -1) {
                    Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                    String value = (String) FormatUtils.formatingDecimalNumber(openAmount, locale, 10, 2);
                    return new ActionError("error.greaterThan",
                            JSPHelper.getMessage(request, "InvoicePayment.amount"), value);
                }
            }
        } catch (AppLevelException e) {
            log.error("Execute " + InvoicePaymentCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private ActionError validateInvoiceId(HttpServletRequest request) {
        String invoiceNumber = (String) getDto("number");
        if (GenericValidator.isBlankOrNull(invoiceNumber)) {
            return new ActionError("errors.required",
                    JSPHelper.getMessage(request, "InvoicePayment.invoice"));
        }


        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        InvoiceCmd invoiceCmd = new InvoiceCmd();
        invoiceCmd.putParam("number", invoiceNumber);
        invoiceCmd.putParam("companyId", companyId);
        invoiceCmd.setOp("findInvoiceByNumber");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceCmd, request);
            InvoiceDTO invoiceDTO = (InvoiceDTO) resultDTO.get("findInvoiceByNumber");
            if (null == invoiceDTO) {
                if (null != getDto("invoiceId") &&
                        !"".equals(getDto("invoiceId").toString().trim())) {
                    return new ActionError("customMsg.NotFound", invoiceNumber);
                }

                return new ActionError("InvoicePayment.invoiceNotFound", invoiceNumber);
            }

            if (FinanceConstants.InvoiceType.CreditNote.equal((Integer) invoiceDTO.get("type"))) {
                if (null != invoiceDTO.get("creditNoteOfId")) {
                    return new ActionError("InvoicePayment.creditNoteRelated.error");
                }

                /*boolean haveInvoicePayments = (Boolean) invoiceDTO.get("haveInvoicePayments");
                if (haveInvoicePayments) {
                    return new ActionError("InvoicePayment.createCreditNotePayment.error");
                }*/
            }

            setDto("invoiceId", invoiceDTO.get("invoiceId"));
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    /**
     * Bank account required validation, only if lexware module is enabled
     * @param errors
     * @param request
     */
    private void validateRequiredBankAccount(ActionErrors errors, HttpServletRequest request) {

        if (com.piramide.elwis.web.admin.el.Functions.hasAssignedLexwareModule(request)) {
            Object bankAccountId = getDto("bankAccountId");
            if (bankAccountId == null || GenericValidator.isBlankOrNull(bankAccountId.toString())) {
                errors.add("bankAccountError", new ActionError("errors.required", JSPHelper.getMessage(request, "InvoicePayment.bankAccount")));
            }
        }
    }

    private void settingUpSkipErrors(HttpServletRequest request, ActionErrors errors) {
        request.setAttribute("skipErrors", "true");
        errors.add("returnToForm", new ActionError("Address.error.cityNotFound"));
    }

    private boolean isButtonPressed(String key, HttpServletRequest request) {
        return null != request.getParameter(key);
    }

}
