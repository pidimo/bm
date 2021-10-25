package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.financemanager.InvoicePaymentCmd;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.salesmanager.el.Functions;
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
 * @author: ivan
 * <p/>
 * Jatun S.R.L
 */
public class InvoicePaymentForm extends DefaultForm {

    private InvoiceDTO invoiceDTO;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String invoiceId = (String) getDto("invoiceId");

        if (!Functions.existsInvoice(invoiceId)) {
            return new ActionErrors();
        }

        this.invoiceDTO =
                com.piramide.elwis.web.financemanager.el.Functions.getInvoice(invoiceId, request);

        ActionErrors errors = new ActionErrors();
        ActionError creditNoteError = validateCreditNoteInvoiceRelation();
        if (null != creditNoteError) {
            errors.add("creditNoteError", creditNoteError);
            return errors;
        }

        errors = super.validate(mapping, request);

        validateRequiredBankAccount(errors, request);

        ActionError amountError = validateAmount(request, errors);
        if (null != amountError) {
            errors.add("amountError", amountError);
        }

        if (errors.isEmpty()) {
            setInvoiceRelatedToCreditNoteText(request);
        }

        return errors;
    }


    private ActionError validateCreditNoteInvoiceRelation() {
        if (FinanceConstants.InvoiceType.Invoice.equal((Integer) invoiceDTO.get("type"))) {
            return null;
        }

        if (null != invoiceDTO.get("creditNoteOfId")) {
            return new ActionError("InvoicePayment.creditNoteRelated.error");
        }

        String op = (String) getDto("op");
        if ("create".equals(op)) {
            /*boolean haveInvoicePayments = (Boolean) invoiceDTO.get("haveInvoicePayments");
            if (haveInvoicePayments) {
                return new ActionError("InvoicePayment.createCreditNotePayment.error");
            }*/
        }
        return null;
    }

    private void setInvoiceRelatedToCreditNoteText(HttpServletRequest request) {
        if (FinanceConstants.InvoiceType.Invoice.equal((Integer) invoiceDTO.get("type"))) {
            return;
        }

        String message = JSPHelper.getMessage(request, "InvoicePayment.text.invoiceMessage",
                invoiceDTO.get("number"));

        setDto("relatedInvoiceText", message);
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

            if ("update".equals(getDto("op").toString())) {
                BigDecimal oldAmount = new BigDecimal(getDto("oldAmount").toString());
                BigDecimal newOpenAmount = openAmount.add(oldAmount);
                if (newOpenAmount.compareTo(amount) == -1) {
                    Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                    String value = (String) FormatUtils.formatingDecimalNumber(newOpenAmount, locale, 10, 2);
                    return new ActionError("error.greaterThan",
                            JSPHelper.getMessage(request, "InvoicePayment.amount"), value);
                }
            }

        } catch (AppLevelException e) {
            log.error("Execute " + InvoicePaymentCmd.class.getName() + " FAIL", e);
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
}
