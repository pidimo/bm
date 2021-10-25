package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.catalogmanager.PayConditionCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceReadCmd;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if (!isSaveButtonPressed(request) && !isGenerateButtonPressed(request)) {
            ActionErrors errors = new ActionErrors();
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");

            if (null != getDto("copyOfButtonPressed") &&
                    "true".equals(getDto("copyOfButtonPressed").toString())) {
                readCopyOfInvoice(request);
            }

            if ("true".equals(getDto("isUpdateCustomerInfo"))) {
                updateDefaultCustomerInfo(request);
            }

            if (null != getDto("changeInvoiceType") &&
                    "true".equals(getDto("changeInvoiceType").toString())) {
                updateSequenceRule(request);
            }

            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);

        com.piramide.elwis.web.common.el.Functions.emptyOrOnlyOneSelectedValidation(this, "sentAddressId", "additionalAddressId", "Invoice.sentAddress", "Invoice.additionalAddress", errors, request);

        ActionError payConditionError = validatePaymentCondition(request);
        if (null != payConditionError) {
            errors.add("payConditionError", payConditionError);
        }

        ActionError creditNoteError = validateCreditNote(request);
        if (null != creditNoteError) {
            errors.add("creditNoteError", creditNoteError);
        }

        ActionError invoiceDateChange;
        String confirmationMsg = (String) getDto("confirmationMsg");
        if (!"true".equals(confirmationMsg) &&
                null != (invoiceDateChange = validateInvoiceDateChange(request, errors))) {
            errors.add("invoiceDateChange", invoiceDateChange);
            request.setAttribute("confirmationMsg", true);
        }

        ActionError netGrossChangeError = validateNetGrossChange(request);
        if (null != netGrossChangeError) {
            errors.add("netGrossChangeError", netGrossChangeError);
        }

        ActionError invoiceNumberDuplicatedError;
        if ("true".equals(confirmationMsg) &&
                null != (invoiceNumberDuplicatedError = validateInvoiceNumberDuplicated(request, errors))) {
            errors.add("invoiceNumberDuplicatedError", invoiceNumberDuplicatedError);
            request.setAttribute("confirmationMsg", true);
        }

        //this always to the end
        if (errors.isEmpty() && showGenerateConfirmation(request)) {
            errors.add("confirmation", new ActionError("Invoice.document.generate.confirmation"));
        }

        return errors;
    }

    private ActionError validateNetGrossChange(HttpServletRequest request) {
        String netGross = (String) getDto("netGross");
        if (GenericValidator.isBlankOrNull(netGross)) {
            return null;
        }

        String op = (String) getDto("op");
        if ("update".equals(op)) {
            return validateNetGrossChangeForUpdate(request, netGross);
        }

        if ("create".equals(op)) {
            return validateNetGrossChangeForCreate(request, netGross);
        }

        return null;
    }

    private ActionError validateNetGrossChangeForCreate(HttpServletRequest request, String netGross) {
        String copyOfInvoiceId = (String) getDto("copyOfInvoiceId");
        if (GenericValidator.isBlankOrNull(copyOfInvoiceId)) {
            return null;
        }

        if (FinanceConstants.InvoiceType.CreditNote.equal(getDto("type").toString())) {
            return null;
        }

        ActionError error = canChangeNetGross(request, copyOfInvoiceId, netGross, false);
        if (null != error) {
            return new ActionError("Invoice.netGrossChange.copyInvalidInvoicePosition.error",
                    JSPHelper.getMessage(request, "Invoice.netGross"), getDto("number"));
        }

        return null;
    }

    private ActionError validateNetGrossChangeForUpdate(HttpServletRequest request, String netGross) {
        return canChangeNetGross(request, getDto("invoiceId").toString(), netGross, true);
    }

    private ActionError canChangeNetGross(HttpServletRequest request,
                                          String invoiceId,
                                          String netGross,
                                          boolean validatePayments) {
        User user = RequestUtils.getUser(request);

        InvoiceCmd invoiceCmd = new InvoiceCmd();
        invoiceCmd.setOp("canChangeNetGross");
        invoiceCmd.putParam("invoiceId", Integer.valueOf(invoiceId));
        invoiceCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        invoiceCmd.putParam("netGross", Integer.valueOf(netGross));
        invoiceCmd.putParam("validatePayments", validatePayments);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceCmd, request);
            if (resultDTO.isFailure()) {
                return new ActionError((String) resultDTO.get("errorKey"),
                        JSPHelper.getMessage(request, "Invoice.netGross"));
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private ActionError validateInvoiceNumberDuplicated(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return null;
        }

        Integer invoiceDate = (Integer) getDto("invoiceDate");

        String ruleNumber = (String) getDto("ruleNumber");
        String ruleFormat = (String) getDto("ruleFormat");
        String customerNumber = (String) getDto("customerNumber");

        String newInvoiceNumber =
                InvoiceUtil.i.buildInvoiceNumber(ruleFormat, Integer.valueOf(ruleNumber), customerNumber, invoiceDate);

        User user = RequestUtils.getUser(request);
        Map conditions = new HashMap();
        conditions.put("companyId", user.getValue(Constants.COMPANYID));
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("invoice",
                "number", newInvoiceNumber,
                "invoiceId", getDto("invoiceId").toString(), conditions, true);

        if (isDuplicated) {
            return new ActionError("Invoice.error.numberDuplicated", newInvoiceNumber);
        }

        return null;
    }

    private ActionError validateInvoiceDateChange(HttpServletRequest request, ActionErrors errors) {
        if (!"update".equals(getDto("op"))) {
            return null;
        }

        if (!errors.isEmpty()) {
            return null;
        }

        Integer invoiceDate = (Integer) getDto("invoiceDate");
        String entityInvoiceDate = (String) getDto("entityInvoiceDate");

        if (entityInvoiceDate.equals(invoiceDate.toString())) {
            return null;
        }


        String ruleNumber = (String) getDto("ruleNumber");
        String ruleFormat = (String) getDto("ruleFormat");
        String customerNumber = (String) getDto("customerNumber");

        String newInvoiceNumber =
                InvoiceUtil.i.buildInvoiceNumber(ruleFormat, Integer.valueOf(ruleNumber), customerNumber, invoiceDate);


        String actualInvoiceNumber = (String) getDto("number");
        if (actualInvoiceNumber.equals(newInvoiceNumber)) {
            return null;
        }

        log.debug("->Actual InvoiceNumber " + actualInvoiceNumber);
        log.debug("->New InvoiceNumber " + newInvoiceNumber);
        return new ActionError("Invoice.warn.invoiceDateChange");
    }

    protected void updateSequenceRule(HttpServletRequest request) {
        String type = (String) getDto("type");
        if (GenericValidator.isBlankOrNull(type)) {
            return;
        }

        CompanyDTO companyDTO =
                com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);

        if (FinanceConstants.InvoiceType.Invoice.getConstantAsString().equals(type)) {
            setDto("sequenceRuleId", companyDTO.get("sequenceRuleIdForInvoice"));
        }

        if (FinanceConstants.InvoiceType.CreditNote.getConstantAsString().equals(type)) {
            setDto("sequenceRuleId", companyDTO.get("sequenceRuleIdForCreditNote"));
        }

    }

    protected void readCopyOfInvoice(HttpServletRequest request) {
        String copyOfInvoiceId = (String) getDto("copyOfInvoiceId");
        setDto("hasRelation", "");
        if (FinanceConstants.InvoiceType.CreditNote.equal(getDto("type").toString())) {
            copyOfInvoiceId = (String) getDto("creditNoteOfId");
            setDto("hasRelation", "true");
        }


        if (null == copyOfInvoiceId ||
                "".equals(copyOfInvoiceId)) {
            setDto("hasRelation", "");
            return;
        }


        InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
        invoiceReadCmd.putParam("invoiceId", Integer.valueOf(copyOfInvoiceId));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReadCmd, request);
            if (resultDTO.isFailure()) {
                return;
            }

            setDto("addressId", null != resultDTO.get("addressId") ?
                    resultDTO.get("addressId").toString() : "");
            setDto("addressName", resultDTO.get("addressName"));
            setDto("contactPersonId", null != resultDTO.get("contactPersonId") ?
                    resultDTO.get("contactPersonId").toString() : "");
            setDto("currencyId", null != resultDTO.get("currencyId") ?
                    resultDTO.get("currencyId").toString() : "");
            setDto("payConditionId", null != resultDTO.get("payConditionId") ?
                    resultDTO.get("payConditionId").toString() : "");
            setDto("templateId", null != resultDTO.get("templateId") ?
                    resultDTO.get("templateId").toString() : "");
            setDto("notes", resultDTO.get("notes"));
            setDto("netGross", null != resultDTO.get("netGross") ?
                    resultDTO.get("netGross").toString() : "");
            setDto("sentAddressId", null != resultDTO.get("sentAddressId") ? resultDTO.get("sentAddressId").toString() : "");
            setDto("sentContactPersonId", null != resultDTO.get("sentContactPersonId") ? resultDTO.get("sentContactPersonId").toString() : "");
            setDto("additionalAddressId", null != resultDTO.get("additionalAddressId") ? resultDTO.get("additionalAddressId").toString() : "");

            setDto("title", null != resultDTO.get("title") ? resultDTO.get("title") : "");

            if (resultDTO.get("sequenceRuleId") != null && !"".equals(resultDTO.get("sequenceRuleId"))) {
                setDto("sequenceRuleId", resultDTO.get("sequenceRuleId"));
            } else {
                updateSequenceRule(request);
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceReadCmd.class.getName() + " FAIL", e);
        }
    }

    private ActionError validateCreditNote(HttpServletRequest request) {
        String op = (String) getDto("op");

        String type = (String) getDto("type");
        if (GenericValidator.isBlankOrNull(type)) {
            return null;
        }

        if (FinanceConstants.InvoiceType.Invoice.equal(type)) {
            return null;
        }

        String creditNoteOfId = (String) getDto("creditNoteOfId");
        if (GenericValidator.isBlankOrNull(creditNoteOfId)) {
            return null;
        }

        //no creditNote validations because exists relation for creditNote
        String hasRelation = (String) getDto("hasRelation");
        if ("update".equals(op) && "true".equals(hasRelation)) {
            return null;
        }

        InvoiceDTO invoiceDTO =
                com.piramide.elwis.web.financemanager.el.Functions.getInvoice(creditNoteOfId, request);
        if (null == invoiceDTO) {
            return null;
        }

        BigDecimal invoiceOpenAmount = (BigDecimal) invoiceDTO.get("openAmount");

        BigDecimal totalAmountGross = new BigDecimal(0);
        if ("update".equals(op)) {
            String creditNoteId = (String) getDto("invoiceId");

            totalAmountGross = new BigDecimal(getDto("totalAmountGross").toString());

            InvoiceDTO creditNoteDTO =
                    com.piramide.elwis.web.financemanager.el.Functions.getInvoice(creditNoteId, request);
            if (null == creditNoteDTO) {
                return null;
            }

            //String hasRelation = (String) getDto("hasRelation");

            boolean haveInvoicePayments = (Boolean) creditNoteDTO.get("haveInvoicePayments");
            if (haveInvoicePayments && "false".equals(hasRelation)) {
                return new ActionError("CreditNote.havePayments.error");
            }
        }

        if (totalAmountGross.compareTo(invoiceOpenAmount) == 1) {
            return new ActionError("CreditNote.relateInvoice.error", invoiceDTO.get("number"));
        }

        String invoicePaymentMessage = JSPHelper.getMessage(request, "InvoicePayment.text.invoiceMessage");
        String creditNotePaymentMessage =
                JSPHelper.getMessage(request, "InvoicePayment.text.creditNoteMessage", invoiceDTO.get("number"));

        //Messages are displayed on the comments of payments,
        //see InvoiceCreateCmd.createPaymentsForCreditNote method
        setDto("invoicePaymentMessage", invoicePaymentMessage);
        setDto("creditNotePaymentMessage", creditNotePaymentMessage);

        return null;
    }

    private ActionError validatePaymentCondition(HttpServletRequest request) {
        String payConditionId = (String) getDto("payConditionId");

        if (GenericValidator.isBlankOrNull(payConditionId)) {
            return null;
        }

        PayConditionCmd payConditionCmd = new PayConditionCmd();
        payConditionCmd.putParam("payConditionId", Integer.valueOf(payConditionId));
        payConditionCmd.setOp("isValidPayCondition");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(payConditionCmd, request);
            boolean isValidPayCondition = (Boolean) resultDTO.get("isValidPayCondition");
            if (!isValidPayCondition && !resultDTO.isFailure()) {
                return new ActionError("Invoice.error.invalidPayCondition");
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + PayConditionCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private void updateDefaultCustomerInfo(HttpServletRequest request) {
        String addressId = (String) getDto("addressId");
        if (null == addressId ||
                "".equals(addressId.trim())) {
            return;
        }

        CustomerDTO customerDTO = Functions.getCustomer(Integer.valueOf(addressId), request);
        if (null != customerDTO) {

            if (null != customerDTO.get("payConditionId") && !"".equals(customerDTO.get("payConditionId").toString().trim())) {
                setDto("payConditionId", customerDTO.get("payConditionId"));
            }

            setDto("sentAddressId", null != customerDTO.get("invoiceAddressId") ? customerDTO.get("invoiceAddressId").toString() : "");
            setDto("sentContactPersonId", null != customerDTO.get("invoiceContactPersonId") ? customerDTO.get("invoiceContactPersonId").toString() : "");
            setDto("additionalAddressId", null != customerDTO.get("additionalAddressId") ? customerDTO.get("additionalAddressId").toString() : "");
        }
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save");
    }

    private boolean isGenerateButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("generate");
    }

    /**
     * verifiy if already exist document generated related to this invoice
     *
     * @param request
     * @return boolean
     */
    private boolean showGenerateConfirmation(HttpServletRequest request) {
        boolean res = false;
        if (getDto("reGenerate") == null) {
            if (isGenerateButtonPressed(request) && !GenericValidator.isBlankOrNull((String) getDto("documentId"))) {
                res = true;
                request.setAttribute("msgConfirmation", true);
            }
        }
        return res;
    }
}

