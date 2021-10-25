package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.cmd.catalogmanager.PayConditionCmd;
import com.piramide.elwis.cmd.contactmanager.ReadCompanyContractReminderUserInfoCmd;
import com.piramide.elwis.cmd.salesmanager.util.PayMethodUtil;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductContractForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        log.debug("ProductContractForm validation execution... " + getDtoMap());

        if (isCancelContractButtonPressed(request)) {
            setDto("cancelContractProcess", "true");
            return new ActionErrors();
        }

        if (!existMainElement()) {
            return new ActionErrors();
        }

        if (!isSaveButtonPressed(request)) {
            return updateForm(request);
        }

        ActionErrors errors = super.validate(mapping, request);

        //define boolean values
        if (super.getDto("cancelledContract") != null) {
            super.setDto("cancelledContract", new Boolean(true));
        } else {
            super.setDto("cancelledContract", new Boolean(false));
        }

        com.piramide.elwis.web.common.el.Functions.emptyOrOnlyOneSelectedValidation(this, "sentAddressId", "additionalAddressId", "ProductContract.sentAddress", "ProductContract.additionalAddress", errors, request);

        ActionError discountError;
        if (null != (discountError = validateDiscountAsPercentage(request))) {
            errors.add("discountError", discountError);
        }

        ActionError payConditionError = validatePaymentCondition(request);
        if (null != payConditionError) {
            errors.add("payConditionError", payConditionError);
        }

        String payMethod = (String) getDto("payMethod");
        if (!GenericValidator.isBlankOrNull(payMethod)) {

            if (SalesConstants.PayMethod.Single.equal(payMethod)) {
                addErrors(validateSinglePaymentCondition(request), errors);
            }

            if (SalesConstants.PayMethod.Periodic.getConstantAsString().equals(payMethod)) {
                addErrors(validatePeriodicPayment(request), errors);
            }

            if (SalesConstants.PayMethod.PartialPeriodic.getConstantAsString().equals(payMethod)) {
                addErrors(validatePartialPeriodicPayment(request), errors);
            }

            if (SalesConstants.PayMethod.PartialFixed.getConstantAsString().equals(payMethod)) {
                addErrors(validatePartialFixedPayment(request), errors);
            }
        }

        if (errors.isEmpty()) {
            String openAmount = (String) getDto("openAmount");
            if (!GenericValidator.isBlankOrNull(openAmount)) {

                BigDecimal openAmountUnformatted = new BigDecimal(openAmount);
                setDto("openAmount", openAmountUnformatted);
            }

            String discount = (String) getDto("discount");
            if (!GenericValidator.isBlankOrNull(discount)) {
                BigDecimal discountUnformatted = new BigDecimal(discount);
                setDto("discount", discountUnformatted);
            }

            if (SalesConstants.PayMethod.Periodic.getConstantAsString().equals(payMethod)) {
                //calculate price per month
                setDto("pricePerMonth", calculatePricePerMonth());
            }

        }
        return errors;
    }

    private List<ActionError> validateSinglePaymentCondition(HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();

        String openAmonutAsString = (String) getDto("openAmount");

        if (!GenericValidator.isBlankOrNull(openAmonutAsString)) {
            String unformattedOpenAmount = unformatDecimalValue(openAmonutAsString, request);
            setDto("openAmount", unformattedOpenAmount);
        }

        if (null == getDto("price") || !(getDto("price") instanceof BigDecimal)) {
            return errors;
        }

        String salePositionId = (String) getDto("salePositionId");
        SalePositionDTO saleposition = Functions.getSalePositionDTO(salePositionId, request);

        String op = (String) getDto("op");
        if ("create".equals(op)) {
            BigDecimal valueToValidate = (BigDecimal) getDto("price");

            if (!PayMethodUtil.isValidSingleOpenAmount(valueToValidate, (BigDecimal) saleposition.get("quantity"))) {
                ActionError invalidPriceError = new ActionError("ProductContract.SingleContract.unitPriceError");
                errors.add(invalidPriceError);
            }
        }
        if ("update".equals(op)) {
            String hasInvoicePositions = (String) getDto("hasInvoicePositions");
            if (!"true".equals(hasInvoicePositions)) {
                BigDecimal valueToValidate = (BigDecimal) getDto("price");

                if (!PayMethodUtil.isValidSingleOpenAmount(valueToValidate, (BigDecimal) saleposition.get("quantity"))) {
                    ActionError invalidPriceError = new ActionError("ProductContract.SingleContract.unitPriceError");
                    errors.add(invalidPriceError);
                }
            }
        }

        return errors;
    }

    private ActionError validateTotalPrice(HttpServletRequest request) {
        String op = (String) getDto("op");
        if ("create".equals(op)) {
            return null;
        }

        if (null == getDto("price") || !(getDto("price") instanceof BigDecimal)) {
            return null;
        }

        BigDecimal totalPaid = new BigDecimal(getDto("totalPaid").toString());
        BigDecimal price = new BigDecimal(getDto("price").toString());
        if (price.compareTo(totalPaid) == -1) {
            return new ActionError("ProductContract.error.PriceLessThanTotalPaid");
        }

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

    private ActionError validateDiscountAsPercentage(HttpServletRequest request) {
        Object dtoObject = getDto("discount");
        if (null == dtoObject) {
            return null;
        }

        ActionError error = validateDecimalField(dtoObject.toString(), "discount", "Contract.discount", request, false);
        if (error != null) {
            return error;
        }

        if (GenericValidator.isBlankOrNull(dtoObject.toString())) {
            return null;
        }

        BigDecimal discount = new BigDecimal(unformatDecimalValue(dtoObject.toString(), request));
        if (discount.compareTo((new BigDecimal(99.99)).setScale(2, RoundingMode.HALF_UP)) == 1
                || discount.compareTo(new BigDecimal(0.0)) == -1) {
            return new ActionError("errors.decimal.percent_range_less_oneHundred", JSPHelper.getMessage(request, "Contract.discount"));
        }

        return null;
    }

    private List<ActionError> validatePartialFixedPayment(HttpServletRequest request) {

        String openAmount = (String) getDto("openAmount");
        if (!GenericValidator.isBlankOrNull(openAmount)) {
            String unformattedOpenAmount = unformatDecimalValue(openAmount, request);
            setDto("openAmount", unformattedOpenAmount);
        }

        List<ActionError> staticFieldsValidationList = validateStaticFieldsForPartialFixedPayment(request);
        if (!staticFieldsValidationList.isEmpty()) {
            return staticFieldsValidationList;
        }

        Integer installment = new Integer(getDto("installment").toString());
        List<ActionError> dinamicFieldsValidationList =
                validateDinamicFieldsForPartialFixedPayment(installment, request);

        if (!dinamicFieldsValidationList.isEmpty()) {
            return dinamicFieldsValidationList;
        }

        Integer amounType = Integer.valueOf(getDto("amounType").toString());
        ActionError payAmountValuesError;
        if (null != (payAmountValuesError = validatePayAmounts(installment, amounType, request))) {
            List<ActionError> errorList = new ArrayList<ActionError>();
            errorList.add(payAmountValuesError);
            return errorList;
        }


        return new ArrayList<ActionError>();
    }

    private List<ActionError> validateStaticFieldsForPartialFixedPayment(HttpServletRequest request) {
        List<ActionError> errorList = new ArrayList<ActionError>();

        ActionError priceError = validateTotalPrice(request);
        if (null != priceError) {
            errorList.add(priceError);
        }

        String amounType = (String) getDto("amounType");
        if (GenericValidator.isBlankOrNull(amounType)) {
            errorList.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "ProductContract.amountType")));
        }

        String installment = (String) getDto("installment");
        if (GenericValidator.isBlankOrNull(installment)) {
            errorList.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "ProductContract.installment")));
        } else {
            Integer installmentAsInteger = GenericTypeValidator.formatInt(installment);
            if (null == installmentAsInteger || installmentAsInteger < 1) {
                errorList.add(new ActionError("errors.integerPositive",
                        JSPHelper.getMessage(request, "ProductContract.installment")));
            } else if (installmentAsInteger > 100) {
                errorList.add(new ActionError("error.greaterThan",
                        JSPHelper.getMessage(request, "ProductContract.installment"), "100"));
            }
        }

        return errorList;
    }

    private List<ActionError> validateDinamicFieldsForPartialFixedPayment(Integer installment,
                                                                          HttpServletRequest request) {
        List<ActionError> errorList = new ArrayList<ActionError>();
        final String firstPayAmount = (String) getDto("payAmount_1");
        ActionError firstPayAmountError = validateDecimalField(firstPayAmount,
                "payAmount_1",
                "ProductContract.error.firstPayAmountRequired", request, true);

        if (null != firstPayAmountError) {
            errorList.add(firstPayAmountError);
        }

        final String firstPayDate = (String) getDto("payDate_1");
        ActionError firstPayDateError = validateDateField(firstPayDate,
                "payDate_1",
                "ProductContract.error.firstPayDateRequired", request, true);
        if (null != firstPayDateError) {
            errorList.add(firstPayDateError);
        }

        for (int i = 2; i <= installment; i++) {
            String payAmount_i = (String) getDto("payAmount_" + i);
            String payDate_i = (String) getDto("payDate_" + i);

            ActionError payAmountError = validateDecimalField(payAmount_i,
                    "payAmount_" + i, "ProductContract.paymentStep.amount", request, false);
            if (null != payAmountError) {
                errorList.add(payAmountError);
            }

            ActionError payDateError = validateDateField(payDate_i,
                    "payDate_" + i, "ProductContract.paymentStep.payDate", request, false);
            if (null != payDateError) {
                errorList.add(payDateError);
            }
        }

        return errorList;
    }

    private ActionError validatePayAmounts(Integer installment, Integer amounType, HttpServletRequest request) {
        Object priceObject = getDto("price");
        if (GenericValidator.isBlankOrNull(priceObject.toString())) {
            return null;
        }

        //exists error price;
        if (priceObject instanceof String) {
            return null;
        }

        BigDecimal price = (BigDecimal) priceObject;

        String op = (String) getDto("op");
        if ("update".equals(op) && SalesConstants.AmounType.PERCENTAGE.equal(amounType)) {
            for (int i = 1; i <= installment; i++) {
                String hasInvoicePosition_i = (String) getDto("hasInvoicePosition_" + i);
                if (!"true".equals(hasInvoicePosition_i)) {
                    continue;
                }

                BigDecimal paymentValue = new BigDecimal(getDto("totalPriceFromInvoicePosition_" + i).toString());
                BigDecimal newPaymentPercent = getPercentage(price, paymentValue);
                setDto("payAmount_" + i, newPaymentPercent.toString());

                setDto("canUpdatePayAmount_" + i, true);
            }
        }

        boolean existsEmptyValues = false;
        BigDecimal sum = new BigDecimal(0);
        for (int i = 1; i <= installment; i++) {
            String payAmount_i = (String) getDto("payAmount_" + i);
            if (GenericValidator.isBlankOrNull(payAmount_i)) {
                existsEmptyValues = true;
                continue;
            }
            sum = sum.add(new BigDecimal(payAmount_i));
        }

        if (SalesConstants.AmounType.AMOUNT.equal(amounType)) {
            if (existsEmptyValues && (sum.compareTo(price) == 0 || sum.compareTo(price) == 1)) {
                return new ActionError("ProductContract.error.payStepOutOfRange");
            }
            if (!existsEmptyValues) {
                if (sum.compareTo(price) == 1) {
                    return new ActionError("ProductContract.error.payAmounOutOfRange");
                }

                if (sum.compareTo(price) == -1) {
                    return new ActionError("ProductContract.error.payAmounLessOfRange");
                }
            }
            if (!existsEmptyValues && sum.compareTo(price) != 0) {

            }
        }
        if (SalesConstants.AmounType.PERCENTAGE.getConstant() == amounType) {
            if (existsEmptyValues &&
                    (sum.compareTo(new BigDecimal(100)) == 0 || sum.compareTo(new BigDecimal(100)) == 1)) {
                return new ActionError("ProductContract.error.payStepOutOfRangePercentage");
            }

            if (!existsEmptyValues) {
                if (sum.compareTo(new BigDecimal(100)) == 1) {
                    return new ActionError("ProductContract.error.payAmounOutOfRangePercentage");
                }

                if (sum.compareTo(new BigDecimal(100)) == -1) {
                    return new ActionError("ProductContract.error.payAmounLessOfRangePercentage");
                }
            }
        }

        return null;
    }

    private List<ActionError> validatePartialPeriodicPayment(HttpServletRequest request) {
        List<ActionError> errorList = new ArrayList<ActionError>();

        String openAmount = (String) getDto("openAmount");
        if (!GenericValidator.isBlankOrNull(openAmount)) {
            String unformattedOpenAmount = unformatDecimalValue(openAmount, request);
            setDto("openAmount", unformattedOpenAmount);
        }


        String payPeriod = (String) getDto("payPeriod");
        ActionError payPeriodError = validateNumberField(payPeriod,
                "payPeriod", "ProductContract.payPeriod", request, true);
        if (null != payPeriodError) {
            errorList.add(payPeriodError);
        }

        ActionError priceError = validateTotalPrice(request);
        if (null != priceError) {
            errorList.add(priceError);
        }

        String payStartDate = (String) getDto("payStartDate");

        ActionError payStartDateError = validateDateField(payStartDate,
                "payStartDate", "ProductContract.payStartDate", request, true);
        if (null != payStartDateError) {
            errorList.add(payStartDateError);
        }

        String invoicedUntil = (String) getDto("invoicedUntil");
        ActionError invoiceUntilError = validateDateField(invoicedUntil,
                "invoicedUntil", "ProductContract.invoiceUntil", request, false);
        if (null != invoiceUntilError) {
            errorList.add(invoiceUntilError);
        }

        if (!errorList.isEmpty()) {
            return errorList;
        }

        Integer payStartDateAsInteger = (Integer) getDto("payStartDate");
        if (PayMethodUtil.isFirstDayOfMonth(payStartDateAsInteger)) {
            if (null != getDto("invoicedUntil") &&
                    (getDto("invoicedUntil") instanceof Integer) &&
                    null != getDto("payStartDate") && (getDto("payStartDate") instanceof Integer)) {
                Integer invoicedUntilAsInteger = (Integer) getDto("invoicedUntil");

                ActionError invoiceUntilValidation = validateInvoicedUntil(request,
                        invoicedUntilAsInteger,
                        payStartDateAsInteger, null);
                if (null != invoiceUntilValidation) {
                    errorList.add(invoiceUntilValidation);
                }
            }

            String installment = (String) getDto("installment");
            if (GenericValidator.isBlankOrNull(installment)) {
                errorList.add(new ActionError("errors.required",
                        JSPHelper.getMessage(request, "ProductContract.installment")));
            } else {
                Integer installmentAsInteger = GenericTypeValidator.formatInt(installment);
                if (null == installmentAsInteger || installmentAsInteger < 0) {
                    errorList.add(new ActionError("errors.integerPositive",
                            JSPHelper.getMessage(request, "ProductContract.installment")));
                }
            }
        } else {
            if (null != getDto("invoicedUntil") &&
                    (getDto("invoicedUntil") instanceof Integer)) {

                Integer invoiceUntilDateAsInteger = (Integer) getDto("invoicedUntil");
                if (!PayMethodUtil.isValidPeriodicEndDate(payStartDateAsInteger, invoiceUntilDateAsInteger)) {

                    Date suggestedDate = getValidEndDayForPeriodicContracts(
                            invoiceUntilDateAsInteger,
                            payStartDateAsInteger);

                    errorList.add(new ActionError("ProductContract.error.invoiceUntilDateInvalid",
                            formatDateValue(payStartDateAsInteger, request),
                            formatDateValue(DateUtils.dateToInteger(suggestedDate), request)));
                }
            }
        }
        return errorList;
    }

    private List<ActionError> validatePeriodicPayment(HttpServletRequest request) {
        List<ActionError> errorList = new ArrayList<ActionError>();

        String pricePeriod = (String) getDto("pricePeriod");
        ActionError pricePeriodError = validateNumberField(pricePeriod,
                "pricePeriod", "ProductContract.pricePeriod", request, true);
        if (null != pricePeriodError) {
            errorList.add(pricePeriodError);
        }

        String payPeriod = (String) getDto("payPeriod");
        Integer payPeriodAsInt = null;

        ActionError payPeriodError = validateNumberField(payPeriod,
                "payPeriod", "ProductContract.payPeriod", request, true);
        if (null != payPeriodError) {
            errorList.add(payPeriodError);
        } else {
            payPeriodAsInt = Integer.valueOf(payPeriod);

            if (pricePeriodError == null) {
                //validate pay period as multiple of price period and > 0
                if (!PayMethodUtil.isMultiplePayPeriodOfPricePeriod(payPeriodAsInt, Integer.valueOf(pricePeriod))) {
                    errorList.add(new ActionError("ProductContract.error.payPeriodMultiple",
                            JSPHelper.getMessage(request, "ProductContract.payPeriod"),
                            JSPHelper.getMessage(request, "ProductContract.pricePeriod")));
                }
            }
        }

        String payStartDate = (String) getDto("payStartDate");
        ActionError payStartDateError = validateDateField(payStartDate,
                "payStartDate", "ProductContract.payStartDate", request, true);
        if (null != payStartDateError) {
            errorList.add(payStartDateError);
        }

        String contractEndDate = (String) getDto("contractEndDate");
        ActionError contractEndDateError = validateDateField(contractEndDate,
                "contractEndDate", "ProductContract.contractEndDate", request, false);
        if (null != contractEndDateError) {
            errorList.add(contractEndDateError);
        }

        String invoicedUntil = (String) getDto("invoicedUntil");
        ActionError invoiceUntilError = validateDateField(invoicedUntil,
                "invoicedUntil", "ProductContract.invoiceUntil", request, false);
        if (null != invoiceUntilError) {
            errorList.add(invoiceUntilError);
        }

        //validate contract reminder
        ActionError reminderError = validatePeriodicContractEndRemider(request);
        if (reminderError != null) {
            errorList.add(reminderError);
        }

        //validate invoice delay
        ActionError invoiceDelayError = validatePeriodicInvoiceDelay(payPeriodAsInt, request);
        if (invoiceDelayError != null) {
            errorList.add(invoiceDelayError);
        }

        if (!errorList.isEmpty()) {
            return errorList;
        }

        Integer payStartDateAsInteger = (Integer) getDto("payStartDate");

        //by default match calendar should be disabled in UI
        setDto("showMatchCalendarPeriod", "false");

        if (PayMethodUtil.isFirstDayOfMonth(payStartDateAsInteger)) {

            boolean isMatchPeriod = PayMethodUtil.isMatchPeriod(Integer.valueOf(payPeriod));

            if (isMatchPeriod && !PayMethodUtil.isMatchPeriodStart(DateUtils.integerToDateTime(payStartDateAsInteger),
                    Integer.valueOf(payPeriod))) {
                setDto("showMatchCalendarPeriod", "true");
            }

            Integer contractEndDateAsInteger = null;
            if (null != getDto("contractEndDate") &&
                    (getDto("contractEndDate") instanceof Integer)) {
                contractEndDateAsInteger = (Integer) getDto("contractEndDate");

                if (payStartDateAsInteger >= contractEndDateAsInteger) {
                    errorList.add(new ActionError("error.greaterThan",
                            JSPHelper.getMessage(request, "ProductContract.payStartDate"),
                            JSPHelper.getMessage(request, "ProductContract.contractEndDate")));
                }

                if (!PayMethodUtil.isLastDayOfMonth(contractEndDateAsInteger) && errorList.isEmpty()) {
                    Date suggestedDate = getLastDayOfMonth(contractEndDateAsInteger);

                    errorList.add(new ActionError("ProductContract.error.contractEndDateInvalid",
                            formatDateValue(payStartDateAsInteger, request),
                            formatDateValue(DateUtils.dateToInteger(suggestedDate), request)));
                }
            }

            if (null != getDto("invoicedUntil") &&
                    (getDto("invoicedUntil") instanceof Integer)) {
                Integer invoicedUntilAsInteger = (Integer) getDto("invoicedUntil");
                ActionError invoiceUntilValidation = validateInvoicedUntil(request,
                        invoicedUntilAsInteger,
                        payStartDateAsInteger, contractEndDateAsInteger);
                if (null != invoiceUntilValidation) {
                    errorList.add(invoiceUntilValidation);
                } else {
                    if (!"true".equals(getDto("showMatchCalendarPeriod"))) {
                        if (isMatchPeriod &&
                                !PayMethodUtil.isMatchPeriodEnd(DateUtils.integerToDateTime(invoicedUntilAsInteger),
                                        Integer.valueOf(payPeriod))) {
                            setDto("showMatchCalendarPeriod", "true");
                        }
                    }
                }
            }

            String matchCalendarPeriod = (String) getDto("matchCalendarPeriod");
            if (null != getDto("showMatchCalendarPeriod")
                    && "true".equals(getDto("showMatchCalendarPeriod").toString())
                    && GenericValidator.isBlankOrNull(matchCalendarPeriod)) {
                errorList.add(new ActionError("errors.required",
                        JSPHelper.getMessage(request, "ProductContract.mathCalendarPeriod")));
                setDto("matchCalendarPeriod", SalesConstants.MatchCalendarPeriod.NO.getConstant());
            }
        } else {
            if (null != getDto("invoicedUntil") &&
                    (getDto("invoicedUntil") instanceof Integer)) {
                Integer invoiceUntilDateAsInteger = (Integer) getDto("invoicedUntil");
                if (!PayMethodUtil.isValidPeriodicEndDate(payStartDateAsInteger, invoiceUntilDateAsInteger)) {
                    Date suggestedDate = getValidEndDayForPeriodicContracts(
                            invoiceUntilDateAsInteger,
                            payStartDateAsInteger);

                    errorList.add(new ActionError("ProductContract.error.invoiceUntilDateInvalid",
                            formatDateValue(payStartDateAsInteger, request),
                            formatDateValue(DateUtils.dateToInteger(suggestedDate), request)));
                }
            }

            if (null != getDto("contractEndDate") &&
                    (getDto("contractEndDate") instanceof Integer)) {
                Integer contractEndDateAsInteger = (Integer) getDto("contractEndDate");

                if (payStartDateAsInteger >= contractEndDateAsInteger) {
                    errorList.add(new ActionError("error.greaterThan",
                            JSPHelper.getMessage(request, "ProductContract.payStartDate"),
                            JSPHelper.getMessage(request, "ProductContract.contractEndDate")));
                }

                if (!PayMethodUtil.isValidPeriodicEndDate(payStartDateAsInteger, contractEndDateAsInteger) &&
                        errorList.isEmpty()) {
                    Date suggestedDate = getValidEndDayForPeriodicContracts(
                            contractEndDateAsInteger,
                            payStartDateAsInteger);

                    errorList.add(new ActionError("ProductContract.error.contractEndDateInvalid",
                            formatDateValue(payStartDateAsInteger, request),
                            formatDateValue(DateUtils.dateToInteger(suggestedDate), request)));
                }
            }
        }

        //if match calendar is disabled, null this
        if ("false".equals(getDto("showMatchCalendarPeriod"))) {
            setDto("matchCalendarPeriod", null);
        }

        return errorList;
    }

    private ActionError validateInvoicedUntil(HttpServletRequest request,
                                              Integer invoicedUntilAsInteger,
                                              Integer payStartDate,
                                              Integer contractEndDate) {
        if (null == invoicedUntilAsInteger) {
            return null;
        }

        if (!PayMethodUtil.isLastDayOfMonth(invoicedUntilAsInteger)) {
            Date suggestedDate = getLastDayOfMonth(invoicedUntilAsInteger);

            return new ActionError("ProductContract.error.invoiceUntilDateInvalid",
                    formatDateValue(payStartDate, request),
                    formatDateValue(DateUtils.dateToInteger(suggestedDate), request));

        }

        if (payStartDate >= invoicedUntilAsInteger) {
            return new ActionError("error.lessThan",
                    JSPHelper.getMessage(request, "ProductContract.invoiceUntil"),
                    JSPHelper.getMessage(request, "ProductContract.payStartDate"));
        }

        if (null != contractEndDate && invoicedUntilAsInteger > contractEndDate) {
            return new ActionError("error.greaterThan",
                    JSPHelper.getMessage(request, "ProductContract.invoiceUntil"),
                    JSPHelper.getMessage(request, "ProductContract.contractEndDate"));
        }
        return null;
    }

    /**
     * Validate contract end reminder
     *
     * @param request request
     * @return ActionError
     */
    private ActionError validatePeriodicContractEndRemider(HttpServletRequest request) {
        String daysToRemind = (String) getDto("daysToRemind");
        String contractEndDate = null;
        if (getDto("contractEndDate") != null) {
            contractEndDate = String.valueOf(getDto("contractEndDate"));
        }

        //initialize reminder time
        setDto("reminderTime", "");

        if (!GenericValidator.isBlankOrNull(daysToRemind)) {

            if (GenericValidator.isBlankOrNull(contractEndDate)) {
                return new ActionError("ProductContract.error.reminder.endDateRequired",
                        JSPHelper.getMessage(request, "ProductContract.endRemiderDays"), JSPHelper.getMessage(request, "ProductContract.contractEndDate"));
            }

            ActionError numberError = validateNumberField(daysToRemind, "daysToRemind", "ProductContract.endRemiderDays", request, false);
            if (numberError != null) {
                return numberError;
            }

            //validate limit
            int daysToRemindAsInt = Integer.valueOf(daysToRemind);
            if (daysToRemindAsInt > 200) {
                return new ActionError("ProductContract.error.reminder.limit200", JSPHelper.getMessage(request, "ProductContract.endRemiderDays"));
            }

            //validate company email reminder
            boolean hasCompanyValidEmailReminder = com.piramide.elwis.web.common.el.Functions.isValidContractEndEmailReminderInCompany(request);
            if (!hasCompanyValidEmailReminder) {
                return new ActionError("ProductContract.error.companyEmailReminder");
            } else if (reminderTimeIsPassedDate(Integer.valueOf(contractEndDate), Integer.valueOf(daysToRemind), request)) {
                return new ActionError("ProductContract.error.reminderIsPassedDate");
            }

            //define reminder time
            Long contractRemiderTime = calculateContractEndReminderTime(Integer.valueOf(contractEndDate), Integer.valueOf(daysToRemind), request);
            setDto("reminderTime", (contractRemiderTime != null) ? contractRemiderTime.toString() : "");
        }

        return null;
    }

    private ActionError validatePeriodicInvoiceDelay(Integer payPeriod, HttpServletRequest request) {
        String invoiceDelay = (String) getDto("invoiceDelay");

        if (!GenericValidator.isBlankOrNull(invoiceDelay)) {

            if (!GenericValidator.isInt(invoiceDelay)) {
                return new ActionError("errors.integer", JSPHelper.getMessage(request, "ProductContract.invoiceDelay"));
            }

            //validate delay limit
            Integer invoiceDelayAsInt = Integer.valueOf(invoiceDelay);
            if (payPeriod != null) {
                Integer delayLimit = BigDecimalUtils.multiply(BigDecimal.valueOf(30.5), BigDecimal.valueOf(payPeriod)).intValue();

                if (invoiceDelayAsInt > delayLimit || invoiceDelayAsInt < (delayLimit * -1)) {
                    return new ActionError("ProductContract.error.invoiceDelayLimit", delayLimit);
                }
            }

        }

        return null;
    }

    /**
     * Calculate reminder time as millis with time zone of current user, this is: endDate - reminderDays
     *
     * @param contractEndDate end date
     * @param daysToRemind    before reminder days
     * @param request         request
     * @return Long
     */
    private Long calculateContractEndReminderTime(Integer contractEndDate, Integer daysToRemind, HttpServletRequest request) {
        Long contractRemiderTime = null;
        if (contractEndDate != null && daysToRemind != null) {
            User user = RequestUtils.getUser(request);
            String userTimeZone = (user.getValue("dateTimeZone") != null) ? user.getValue("dateTimeZone").toString() : null;

            DateTimeZone dateTimeZone = (!GenericValidator.isBlankOrNull(userTimeZone)) ? DateTimeZone.forID(userTimeZone) : DateTimeZone.getDefault();

            DateTime dateTime = DateUtils.integerToDateTime(contractEndDate, dateTimeZone);
            contractRemiderTime = dateTime.minusDays(daysToRemind).getMillis();
        }
        return contractRemiderTime;
    }

    /**
     * Verify if el reminder time is in passed date time
     *
     * @param contractEndDate end date
     * @param daysToRemind    before reminder days
     * @param request         request
     * @return true or false
     */
    private boolean reminderTimeIsPassedDate(Integer contractEndDate, Integer daysToRemind, HttpServletRequest request) {
        boolean isInPassedDate = false;

        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());
        String userTimeZone = (user.getValue("dateTimeZone") != null) ? user.getValue("dateTimeZone").toString() : null;

        DateTimeZone dateTimeZone = (!GenericValidator.isBlankOrNull(userTimeZone)) ? DateTimeZone.forID(userTimeZone) : DateTimeZone.getDefault();
        DateTime currentUserDateTime = new DateTime(dateTimeZone);

        Long contractRemiderTime = calculateContractEndReminderTime(contractEndDate, daysToRemind, request);

        if (contractRemiderTime != null && contractEndDate.intValue() >= DateUtils.dateToInteger(currentUserDateTime)) {
            String reminderUserTimeZone = null;

            ReadCompanyContractReminderUserInfoCmd reminderUserInfoCmd = new ReadCompanyContractReminderUserInfoCmd();
            reminderUserInfoCmd.putParam("companyId", companyId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(reminderUserInfoCmd, null);
                if (resultDTO.containsKey("timeZone")) {
                    reminderUserTimeZone = (String) resultDTO.get("timeZone");
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd..");
            }

            DateTimeZone reminderUserDateTimeZone = (reminderUserTimeZone != null) ? DateTimeZone.forID(reminderUserTimeZone) : DateTimeZone.getDefault();

            //move the current date to user reminder time zone
            DateTime userReminderDateTime = new DateTime(reminderUserDateTimeZone);

            if (userReminderDateTime.getMillis() >= contractRemiderTime) {
                isInPassedDate = true;
            }
        }
        return isInPassedDate;
    }

    protected boolean existMainElement() {
        String salePositionId = (String) getDto("salePositionId");
        return GenericValidator.isBlankOrNull(salePositionId) || Functions.existsSalePosition(salePositionId);
    }

    protected ActionErrors updateForm(HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        errors.add("emptyError", new ActionError("Admin.Company.new"));
        request.setAttribute("skipErrors", "true");

        String addressId = (String) getDto("addressId");
        String payConditionId = (String) getDto("payConditionId");

        CustomerDTO customerDTO = null;
        if (!GenericValidator.isBlankOrNull(addressId)) {
            customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(Integer.valueOf(addressId), request);
        }

        //update paycondition only has no selected another pay condition
        if (GenericValidator.isBlankOrNull(payConditionId)) {
            if (null != customerDTO && null != customerDTO.get("payConditionId") && !"".equals(customerDTO.get("payConditionId").toString().trim())) {
                setDto("payConditionId", customerDTO.get("payConditionId"));
            }
        }

        if (isSubmitFromSearchContactPopup(request)) {
            Functions.setCustomerProductContractDefaultValues(this, customerDTO);
        }

        //update price according to netgross field
        String changeNetGross = (String) getDto("changeNetGross");
        if (null != changeNetGross && "true".equals(changeNetGross)) {
            String netGross = (String) getDto("netGross");
            String salePositionId = (String) getDto("salePositionId");
            Functions.updateNetGrossPricesInProductContractForm(this,
                    netGross,
                    salePositionId,
                    request);
        }


        //reset installment and payPeriod attributes because this fields
        //are used in ajax request
        String changePayMethod = (String) getDto("changePayMethod");

        if (null != changePayMethod &&
                "true".equals(changePayMethod.trim())) {
            setDto("installment", "");
            setDto("payPeriod", "");

            // update open amount when paymethod change
            String payMethod = (String) getDto("payMethod");

            if (GenericValidator.isBlankOrNull(payMethod) ||
                    SalesConstants.PayMethod.Periodic.getConstantAsString().equals(payMethod)) {
                setDto("openAmount", new BigDecimal(0.0));
            }

            if (SalesConstants.PayMethod.PartialPeriodic.getConstantAsString().equals(payMethod) ||
                    SalesConstants.PayMethod.Single.getConstantAsString().equals(payMethod) ||
                    SalesConstants.PayMethod.PartialFixed.getConstantAsString().equals(payMethod)) {
                String price = (String) getDto("price");

                ActionError priceError = validateDecimalField(price, "price", "Contract.price", request, true);
                if (null == priceError) {
                    setDto("openAmount", new BigDecimal((String) getDto("price")));
                }
                if (SalesConstants.PayMethod.PartialFixed.getConstantAsString().equals(payMethod)) {
                    setDto("installment", 1);
                    setDto("steptsInvoicedCounter", 0);
                }

            }
        } else {
            setDto("grouping", "");
            setDto("groupingHidden", "");
        }

        return errors;
    }

    private ActionError validateDecimalField(String value,
                                             String dtoKey,
                                             String resourceKey,
                                             HttpServletRequest request,
                                             boolean isRequired) {
        String resource = JSPHelper.getMessage(request, resourceKey);

        if (isRequired && GenericValidator.isBlankOrNull(value)) {
            return new ActionError("errors.required", resource);
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return null;
        }

        ActionError decimalValidation = FieldChecks.validateDecimalNumber(value, resourceKey, 10, 2, request);
        if (null == decimalValidation) {
            setDto(dtoKey, unformatDecimalValue(value, request));
        }

        return decimalValidation;
    }

    private ActionError validateNumberField(String value,
                                            String dtoKey,
                                            String resourceKey,
                                            HttpServletRequest request,
                                            boolean isRequired) {
        String resource = JSPHelper.getMessage(request, resourceKey);

        if (isRequired && GenericValidator.isBlankOrNull(value)) {
            return new ActionError("errors.required", resource);
        }

        if (!GenericValidator.isInt(value)) {
            return new ActionError("errors.integer", resource);
        }

        Integer number = new Integer(value);
        if (number <= 0) {
            return new ActionError("errors.integerPositive", resource);
        }

        return null;
    }

    private ActionError validateDateField(String value,
                                          String dtoKey,
                                          String resourceKey,
                                          HttpServletRequest request,
                                          boolean isRequired) {
        String resource = JSPHelper.getMessage(request, resourceKey);

        if (isRequired && GenericValidator.isBlankOrNull(value)) {
            return new ActionError("errors.required", resource);
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return null;
        }

        ActionError dateValidation = FieldChecks.validateDate(value, resourceKey, request);
        if (null == dateValidation) {
            setDto(dtoKey, unformatDateValue(value, request));
        }

        return dateValidation;
    }

    private String unformatDecimalValue(String value, HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        return FormatUtils.unformatingDecimalNumber(value, locale, 10, 2).toString();
    }

    private String formatDateValue(Integer dateAsInteger, HttpServletRequest request) {
        return DateUtils.parseDate(dateAsInteger, JSPHelper.getMessage(request, "datePattern"));
    }

    private Integer unformatDateValue(String date, HttpServletRequest request) {
        Date d = DateUtils.formatDate(date, JSPHelper.getMessage(request, "datePattern"));
        return DateUtils.dateToInteger(d);
    }

    private boolean isSaveButtonPressed
            (HttpServletRequest
                    request) {
        return null != request.getParameter("save");
    }

    private boolean isCancelContractButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("cancelContractButton");
    }

    private boolean isSubmitFromSearchContactPopup(HttpServletRequest request) {
        return "searchAddress".equals(request.getParameter("submitPopupName"));
    }

    private void addErrors
            (List<ActionError> errorList, ActionErrors
                    errors) {
        int i = 0;
        for (ActionError error : errorList) {
            errors.add("error_" + i, error);
            i++;
        }
    }

    private BigDecimal getPercentage(BigDecimal productContractPrice, BigDecimal invoicePositionTotalPrice) {
        final BigDecimal oneHundred = new BigDecimal(100);
        return divideBigDecimal(invoicePositionTotalPrice.multiply(oneHundred), productContractPrice);
    }

    private BigDecimal divideBigDecimal(BigDecimal amount, BigDecimal divisor) {
        return amount.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private Date getLastDayOfMonth(Integer dateAsInteger) {
        Date date = DateUtils.integerToDate(dateAsInteger);
        Map<String, Date> result = DateUtils.getFirstAndEndDayOfMonth(date);

        return result.get("endDay");
    }

    private Date getValidEndDayForPeriodicContracts(Integer endDateAsInteger, Integer startDateAsInteger) {
        Date startDate = DateUtils.integerToDate(startDateAsInteger);
        Date endDate = DateUtils.integerToDate(endDateAsInteger);

        Map<String, Date> startDateReference = DateUtils.getFirstAndEndDayOfMonth(startDate);
        Date endDay = startDateReference.get("endDay");
        if (startDateAsInteger.equals(DateUtils.dateToInteger(endDay))) {
            Map<String, Date> endDateReference = DateUtils.getFirstAndEndDayOfMonth(endDate);
            Date lastDay = endDateReference.get("endDay");

            int[] endDateInformation = DateUtils.getYearMonthDay(DateUtils.dateToInteger(lastDay));
            int endDateMonth = endDateInformation[1];
            int endDateYear = endDateInformation[0];
            int endDateDay = endDateInformation[2];
            return DateUtils.intToDate(endDateYear, endDateMonth, endDateDay - 1);
        } else {
            int[] endDateInformation = DateUtils.getYearMonthDay(endDateAsInteger);
            int endDateMonth = endDateInformation[1];
            int endDateYear = endDateInformation[0];

            int[] startDateInformation = DateUtils.getYearMonthDay(startDateAsInteger);
            int startDateDay = startDateInformation[2];


            return DateUtils.intToDate(endDateYear, endDateMonth, startDateDay - 1);
        }
    }

    private BigDecimal calculatePricePerMonth() {
        BigDecimal pricePerMonth = null;

        Object priceObj = getDto("price");
        Object pricePeriodObj = getDto("pricePeriod");

        if (priceObj != null && pricePeriodObj != null
                && !GenericValidator.isBlankOrNull(priceObj.toString())
                && !GenericValidator.isBlankOrNull(pricePeriodObj.toString())) {
            pricePerMonth = BigDecimalUtils.divide(new BigDecimal(priceObj.toString()), new BigDecimal(pricePeriodObj.toString()));
        }

        return pricePerMonth;
    }
}
