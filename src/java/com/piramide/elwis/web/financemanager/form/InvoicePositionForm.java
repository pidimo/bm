package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.cmd.catalogmanager.ProductUnitCmd;
import com.piramide.elwis.cmd.financemanager.InvoicePositionCmd;
import com.piramide.elwis.cmd.productmanager.ProductReadLightCmd;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.financemanager.util.InvoicePositionDiscountValueCalculator;
import com.piramide.elwis.web.financemanager.util.InvoicePositionUtil;
import com.piramide.elwis.web.financemanager.util.InvoicePositionValidatorUtil;
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
import java.util.List;

import static com.piramide.elwis.utils.FinanceConstants.InvoiceType.CreditNote;
import static com.piramide.elwis.utils.FinanceConstants.InvoiceType.Invoice;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePosition;
import static com.piramide.elwis.web.financemanager.el.Functions.getInvoicePositionsByContract;
import static com.piramide.elwis.web.financemanager.util.InvoicePositionCommon.i;
import static com.piramide.elwis.web.salesmanager.el.Functions.*;

/**
 * @author Ivan
 * @version 4.3
 */
public class InvoicePositionForm extends DefaultForm {
    private String operation;
    private InvoiceDTO invoiceDTO;

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        String invoiceId = (String) getDto("invoiceId");
        if (!existsInvoice(invoiceId)) {
            return new ActionErrors();
        }

        this.invoiceDTO =
                com.piramide.elwis.web.financemanager.el.Functions.getInvoice(invoiceId, request);

        if (!isSaveButtonPressed(request)) {

            if (Invoice.equal((Integer) invoiceDTO.get("type"))) {
                selectProductContract(request);
                selectPaymentStep(request);
                unSelectProductContract();
            }

            if (CreditNote.equal((Integer) invoiceDTO.get("type"))) {
                selectInvoicePosition(request);
            }

            updatePositionText(request);
            readProductAccountAndUnit(request);
            return setSkipErrors(request);
        }

        this.operation = (String) getDto("op");

        ActionErrors errors = super.validate(mapping, request);

        ActionError vatRatesError = hasValidVatRates(request);
        if (null != vatRatesError) {
            errors.add("vatRatesErrors", vatRatesError);
        }

        ActionError haveInvoicePaymentsError = validateInvoicePayments();
        if (null != haveInvoicePaymentsError) {
            errors.add("haveInvoicePaymentsError", haveInvoicePaymentsError);
        }

        ActionError invoiceNetGrossError = validateInvoiceNetGross(request);
        if (null != invoiceNetGrossError) {
            errors.add("invoiceNetGrossError", invoiceNetGrossError);
        }

        ActionError unitPriceError = validateUnitPrice(request, errors);
        if (null != unitPriceError) {
            errors.add("unitPriceError", unitPriceError);
        }

        ActionError quantityError = validateQuantity(request);
        if (null != quantityError) {
            errors.add("quantityError", quantityError);
        }

        if (errors.isEmpty()) {
            if (Invoice.equal((Integer) invoiceDTO.get("type"))) {
                ActionError productContractError = validateProductContract(request);
                if (null != productContractError) {
                    errors.add("productContractError", productContractError);
                }
            }
            if (CreditNote.equal((Integer) invoiceDTO.get("type"))) {
                ActionError creditNoteTotalAmountError = validateCreditNoteTotalAmountGross(request);
                if (null != creditNoteTotalAmountError) {
                    errors.add("creditNoteTotalAmountError", creditNoteTotalAmountError);
                    return errors;
                }

                ActionError creditNoteProductContratError = validateCreditNoteProductContract(request);
                if (null != creditNoteProductContratError) {
                    errors.add("creditNoteTotalAmountError", creditNoteProductContratError);
                }
            }
        }

        return errors;
    }

    private ActionError validateInvoiceNetGross(HttpServletRequest request) {
        String invoiceNetGross = (String) getDto("invoiceNetGross");
        Integer actualNetGross = (Integer) invoiceDTO.get("netGross");
        if (invoiceNetGross.equals(actualNetGross.toString())) {
            return null;
        }

        if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
            String unitPriceGross = (String) getDto("unitPriceGross");
            setDto("unitPriceGross",
                    new BigDecimal(FormatUtils.unformatDecimalNumber(unitPriceGross, 10, 4, request)));
        }


        if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
            String unitPrice = (String) getDto("unitPrice");
            setDto("unitPrice",
                    new BigDecimal(FormatUtils.unformatDecimalNumber(unitPrice, 10, 4, request)));
        }

        return new ActionError("InvoicePosition.invoiceNetGrossChange.error",
                JSPHelper.getMessage(request, "Invoice.netGross"));
    }

    private ActionError validateCreditNoteTotalAmountGross(HttpServletRequest request) {
        if (null == invoiceDTO.get("creditNoteOfId") ||
                "".equals(invoiceDTO.get("creditNoteOfId").toString().trim())) {
            return null;
        }

        InvoiceDTO relatedInvoiceDTO =
                com.piramide.elwis.web.financemanager.el.Functions.getInvoice(
                        invoiceDTO.get("creditNoteOfId").toString(),
                        request);

        BigDecimal creditNoteTotalAmountGross = (BigDecimal) invoiceDTO.get("totalAmountGross");
        BigDecimal associatedInvoiceOpenAmount = (BigDecimal) relatedInvoiceDTO.get("openAmount");

        BigDecimal actualCreditNotePayment = (BigDecimal) invoiceDTO.get("actualCreditNotePayment");
        if ("update".equals(operation)) {
            BigDecimal modifiedTotalPrice = new BigDecimal((String) getDto("modifiedTotalPrice"));
            associatedInvoiceOpenAmount = associatedInvoiceOpenAmount.add(modifiedTotalPrice);
            creditNoteTotalAmountGross = creditNoteTotalAmountGross.subtract(modifiedTotalPrice);
            actualCreditNotePayment = actualCreditNotePayment.subtract(modifiedTotalPrice);
        }

        BigDecimal price = getInvoicePositionPrice("unitPrice", "unitPriceGross");
        BigDecimal quantity = (BigDecimal) getDto("quantity");
        BigDecimal totalPrice = quantity.multiply(price);

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.setOp("getModifiedTotalPrice");
        invoicePositionCmd.putParam("totalPrice", totalPrice);
        invoicePositionCmd.putParam("vatId", Integer.valueOf(getDto("vatId").toString()));
        invoicePositionCmd.putParam("invoiceId", invoiceDTO.get("invoiceId"));

        try {

            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            BigDecimal modifiedTotalPrice = (BigDecimal) resultDTO.get("getModifiedTotalPrice");

            BigDecimal newCreditNoteTotalAmountGross = creditNoteTotalAmountGross.add(modifiedTotalPrice);
            newCreditNoteTotalAmountGross = newCreditNoteTotalAmountGross.subtract(actualCreditNotePayment);

            String associatedInvoiceNumber = (String) relatedInvoiceDTO.get("number");
            if (newCreditNoteTotalAmountGross.compareTo(associatedInvoiceOpenAmount) == 1) {
                return new ActionError("CreditNote.createInvoicePosition.error", associatedInvoiceNumber);
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private ActionError validateQuantity(HttpServletRequest request) {
        Object quantity = getDto("quantity");
        if (!(quantity instanceof BigDecimal)) {
            return null;
        }

        if (i.isGreaterOrEqualThan(BigDecimal.ZERO, (BigDecimal) quantity)) {
            return new ActionError("error.decimalNumber.positive",
                    JSPHelper.getMessage(request, "InvoicePosition.quantity"));
        }

        return null;
    }

    private ActionError validateUnitPrice(HttpServletRequest request, ActionErrors errors) {
        String price = null;
        String msg = "";
        Integer actualNetGross = (Integer) invoiceDTO.get("netGross");

        if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
            price = (String) getDto("unitPrice");
            msg = "InvoicePosition.unitPrice";
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
            price = (String) getDto("unitPriceGross");
            msg = "InvoicePosition.unitPriceGross";
        }


        if (GenericValidator.isBlankOrNull(price)) {
            return null;
        }

        ActionError decimalValidation =
                FieldChecks.validateDecimalNumber(price, msg, 10, 4, request);

        if (null == decimalValidation) {
            if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
                setDto("unitPrice", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
                setDto("unitPriceGross", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
            }
        }

        return decimalValidation;
    }

    private ActionError validateInvoicePayments() {
        boolean haveInvoicePayments = (Boolean) invoiceDTO.get("haveInvoicePayments");

        if (Invoice.equal((Integer) invoiceDTO.get("type")) &&
                haveInvoicePayments) {
            return new ActionError("InvoicePosition.haveInvoicePaymentsError");
        }

        return null;
    }

    private ActionError hasValidVatRates(HttpServletRequest request) {
        String vatId = (String) getDto("vatId");
        if (GenericValidator.isBlankOrNull(vatId)) {
            return null;
        }

        String invoiceId = (String) getDto("invoiceId");
        if (GenericValidator.isBlankOrNull(invoiceId)) {
            return null;
        }

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("vatId", Integer.valueOf(vatId));
        invoicePositionCmd.putParam("invoiceId", Integer.valueOf(invoiceId));
        invoicePositionCmd.setOp("hasValidVatRates");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            boolean hasValidVatRates = (Boolean) resultDTO.get("hasValidVatRates");
            if (!hasValidVatRates) {
                return new ActionError("InvoicePosition.vatRateError");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL");
        }

        return null;
    }

    private ActionError validateCreditNoteProductContract(HttpServletRequest request) {
        String contractId = (String) getDto("contractId");
        if (GenericValidator.isBlankOrNull(contractId)) {
            return null;
        }

        String payStepId = (String) getDto("payStepId");
        if (GenericValidator.isBlankOrNull(payStepId)) {
            payStepId = null;
        }

        BigDecimal formPrice = getInvoicePositionPrice();
        BigDecimal formQuantity = (BigDecimal) getDto("quantity");
        BigDecimal oldTotalPrice = null;
        BigDecimal oldDiscount = null;
        Integer invoicePositionId = null;
        if ("update".equals(operation)) {
            oldTotalPrice = getInvoicePositionOldTotalPrice();
            oldDiscount = getOldDiscountValue();
            invoicePositionId = new Integer(getDto("positionId").toString());
        }

        List<InvoicePositionDTO> invoicePositions = getInvoicePositionsByContract(new Integer(contractId), request);

        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();
        ActionError totalPriceError = invoicePositionValidatorUtil.validateCreditNoteProductContract(
                invoicePositions,
                invoicePositionId,
                contractId,
                payStepId,
                formPrice,
                formQuantity,
                oldTotalPrice,
                oldDiscount,
                operation,
                request);

        if (null != totalPriceError) {
            return totalPriceError;
        }

        BigDecimal discountValue = invoicePositionValidatorUtil.getCreditNoteDiscountValue();

        setDto("discountValue", discountValue);
        return null;
    }

    private ActionError validateProductContract(HttpServletRequest request) {
        String contractId = (String) getDto("contractId");
        if (GenericValidator.isBlankOrNull(contractId)) {
            return null;
        }

        ActionError contractError = contractValidations(request, contractId);
        if (null != contractError) {
            return contractError;
        }

        ProductContractDTO contractDTO = getProductContract(contractId, request);

        SalePositionDTO salePositionDTO = getSalePositionDTO(contractDTO.get("salePositionId").toString(), request);

        BigDecimal formPrice = getInvoicePositionPrice();
        BigDecimal formQuantity = (BigDecimal) getDto("quantity");

        Integer payStepId = null;
        PaymentStepDTO paymentStepDTO = null;
        BigDecimal paymentStepPrice = null;

        if (i.isPartialFixedContract(contractDTO)) {
            String payStepIdAsString = (String) getDto("payStepId");
            paymentStepDTO = getPaymentStep(payStepIdAsString, request);
            payStepId = (Integer) paymentStepDTO.get("payStepId");
            paymentStepPrice = i.getPaymentStepAmount(contractDTO, paymentStepDTO);
        }

        InvoicePositionUtil invoicePositionUtil = null;

        if ("create".equals(operation)) {
            invoicePositionUtil = new InvoicePositionUtil(
                    salePositionDTO,
                    contractDTO,
                    null,
                    payStepId,
                    paymentStepPrice,
                    formPrice,
                    formQuantity,
                    null,
                    null,
                    request);
        }

        if ("update".equals(operation)) {
            Integer positionId = new Integer((String) getDto("positionId"));
            String oldContractId = (String) getDto("oldContractId");

            if (!contractDTO.get("contractId").toString().equals(oldContractId)) {
                invoicePositionUtil = new InvoicePositionUtil(
                        salePositionDTO,
                        contractDTO,
                        positionId,
                        payStepId,
                        paymentStepPrice,
                        formPrice,
                        formQuantity,
                        null,
                        null,
                        request);
            } else {
                BigDecimal oldDiscountValue = getOldDiscountValue();
                BigDecimal oldTotalPrice = getInvoicePositionOldTotalPrice();
                invoicePositionUtil =
                        new InvoicePositionUtil(
                                salePositionDTO,
                                contractDTO,
                                positionId,
                                payStepId,
                                paymentStepPrice,
                                formPrice,
                                formQuantity,
                                oldTotalPrice,
                                oldDiscountValue,
                                request);
            }
        }
        invoicePositionUtil.showInformation();

        ActionError error = null;
        if (i.isSingleContract(contractDTO)) {
            error = singleContractValidation(contractDTO, invoicePositionUtil, request);
        }

        if (i.isPartialFixedContract(contractDTO)) {
            error = partialFixedContractValidation(contractDTO, paymentStepDTO, invoicePositionUtil, request);
        }

        if (i.isPartialPeriodicContract(contractDTO)) {
            error = partialPeriodicContractValidation(contractDTO, invoicePositionUtil, request);
        }

        if (i.isPeriodicContract(contractDTO)) {
            error = periodicContractValidation(invoicePositionUtil, request);
        }

        if (null != error) {
            return error;
        }

        BigDecimal discountedValue = invoicePositionUtil.getInvoicePositionDiscountedValue();
        BigDecimal remnant = invoicePositionUtil.getRemnant();

        if (BigDecimal.ZERO.compareTo(remnant) != 0) {
            discountedValue = BigDecimalUtils.sum(discountedValue, remnant);
        }
        if (i.isEqual(BigDecimal.ZERO, discountedValue)) {
            discountedValue = null;
        }

        setDto("discountValue", discountedValue);

        if (null != contractDTO.get("discount")) {
            setDto("discount", contractDTO.get("discount"));
        }

        return null;
    }

    private ActionError periodicContractValidation(InvoicePositionUtil invoicePositionUtil,
                                                   HttpServletRequest request) {

        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();
        ActionError error = invoicePositionValidatorUtil.validatePeriodicContract(
                invoicePositionUtil,
                request);

        if (null != error) {
            return error;
        }

        return null;
    }

    private ActionError partialPeriodicContractValidation(ProductContractDTO contractDTO,
                                                          InvoicePositionUtil invoicePositionUtil,
                                                          HttpServletRequest request) {
        Integer positionId = null;
        if ("update".equals(operation)) {
            positionId = new Integer((String) getDto("positionId"));
        }

        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();
        ActionError error = invoicePositionValidatorUtil.validatePartialPeriodicContract(
                contractDTO,
                invoicePositionUtil,
                positionId,
                request);

        if (null != error) {
            return error;
        }

        return null;
    }

    private ActionError partialFixedContractValidation(ProductContractDTO contractDTO,
                                                       PaymentStepDTO paymentStepDTO,
                                                       InvoicePositionUtil invoicePositionUtil,
                                                       HttpServletRequest request) {

        Integer positionId = null;
        if ("update".equals(operation)) {
            positionId = new Integer((String) getDto("positionId"));
        }

        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();
        ActionError error = invoicePositionValidatorUtil.validatePartialFixedContract(
                contractDTO,
                paymentStepDTO,
                invoicePositionUtil,
                positionId,
                request);

        if (null != error) {
            return error;
        }

        return null;
    }

    private ActionError singleContractValidation(ProductContractDTO contractDTO,
                                                 InvoicePositionUtil invoicePositionUtil,
                                                 HttpServletRequest request) {
        Integer positionId = null;
        if ("update".equals(operation)) {
            positionId = new Integer((String) getDto("positionId"));
        }

        InvoicePositionValidatorUtil invoicePositionValidatorUtil = new InvoicePositionValidatorUtil();

        ActionError error = invoicePositionValidatorUtil.validateSingleContract(
                contractDTO,
                invoicePositionUtil,
                positionId,
                request);

        if (null != error) {
            return error;
        }

        return null;
    }


    private ActionError contractValidations(HttpServletRequest request, String contractId) {
        ProductContractDTO contractDTO =
                getProductContract(contractId, request);

        //contract was delete by other user.
        if (null == contractDTO) {
            String contractNumber = (String) getDto("contractNumber");
            clearInvoiceFormInformation();
            return new ActionError("customMsg.NotFound", contractNumber);
        }

        //the address identifiers for contract and invoice are distinct.
        ActionError contractAddressError = validateProductContractAddress(contractDTO);
        if (null != contractAddressError) {
            return contractAddressError;
        }

        ActionError basicContractError = null;
        if ("create".equals(operation)) {
            if (i.isPartialFixedContract(contractDTO)) {
                basicContractError = basicValidationsForPartialFixedContract(contractDTO, request);
            } else {
                basicContractError = openAmountValidation(contractDTO);
            }
        }

        if ("update".equals(operation)) {
            String oldContractId = (String) getDto("oldContractId");

            if (!contractDTO.get("contractId").toString().equals(oldContractId)) {
                if (i.isPartialFixedContract(contractDTO)) {
                    basicContractError = basicValidationsForPartialFixedContract(contractDTO, request);
                } else {
                    basicContractError = openAmountValidation(contractDTO);
                }
            }
        }

        if (null != basicContractError) {
            return basicContractError;
        }

        return null;
    }

    private ActionError basicValidationsForPartialFixedContract(ProductContractDTO contractDTO,
                                                                HttpServletRequest request) {

        ActionError openAmountZeroError = openAmountValidation(contractDTO);
        if (null != openAmountZeroError) {
            return openAmountZeroError;
        }

        String payStepId = (String) getDto("payStepId");
        if (GenericValidator.isBlankOrNull(payStepId)) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, "InvoicePosition.payment"));
        }

        PaymentStepDTO paymentStepDTO = getPaymentStep(payStepId, request);
        if (null == paymentStepDTO) {
            return new ActionError("InvoicePosition.paymentStep.notFoundError");
        }

        Boolean hasInvoicePosition = (Boolean) paymentStepDTO.get("hasInvoicePosition");
        if (hasInvoicePosition) {
            return new ActionError("InvoicePosition.paymentStep.isInvoicedError");
        }

        return null;
    }


    private ActionError openAmountValidation(ProductContractDTO productContractDTO) {
        BigDecimal contractOpenAmount = (BigDecimal) productContractDTO.get("openAmount");
        if (i.isEqual(contractOpenAmount, BigDecimal.ZERO)) {
            return new ActionError("InvoicePosition.productContract.openAmountError",
                    productContractDTO.get("contractNumber"));
        }

        return null;
    }

    private void selectInvoicePosition(HttpServletRequest request) {
        String relatedInvoicePositionId = (String) getDto("relatedInvoicePositionId");
        if (GenericValidator.isBlankOrNull(relatedInvoicePositionId)) {
            return;
        }
        InvoicePositionDTO invoicePositionDTO = getInvoicePosition(relatedInvoicePositionId, request);
        if (null == invoicePositionDTO) {
            return;
        }

        setDto("productId", invoicePositionDTO.get("productId").toString());
        setDto("unitPriceGross", invoicePositionDTO.get("unitPriceGross"));
        setDto("unitPrice", invoicePositionDTO.get("unitPrice"));
        setDto("quantity", invoicePositionDTO.get("quantity"));
        setDto("contractId", invoicePositionDTO.get("contractId"));
        setDto("payStepId", invoicePositionDTO.get("payStepId"));
        setDto("discount", invoicePositionDTO.get("discount"));
        setDto("discountValue", invoicePositionDTO.get("discountValue"));
        setDto("vatId", invoicePositionDTO.get("vatId"));
    }

    private void selectProductContract(HttpServletRequest request) {
        String selectProductContract = (String) getDto("selectProductContract");

        if (!"true".equals(selectProductContract)) {
            return;
        }

        String contractId = (String) getDto("contractId");
        if (GenericValidator.isBlankOrNull(contractId)) {
            return;
        }

        ProductContractDTO productContractDTO = getProductContract(contractId, request);

        SalePositionDTO salePositionDTO = getSalePositionDTO(
                productContractDTO.get("salePositionId").toString(),
                request);

        if (null != salePositionDTO) {
            setDto("productId", salePositionDTO.get("productId").toString());
            setDto("productName", salePositionDTO.get("productName"));
        }

        setDto("disableQuantityField", false);

        if (i.isPartialFixedContract(productContractDTO)) {
            setDto("isRelatedWithPartialFixedContract", true);
            setDto("disableQuantityField", true);
        }

        if (i.isPartialPeriodicContract(productContractDTO)) {
            setDto("disableQuantityField", true);
        }

        setDto("vatId", productContractDTO.get("vatId"));

        Integer positionId = null;
        if ("update".equals(operation)) {
            positionId = new Integer((String) getDto("positionId"));
        }

        setInvoicePositionPriceInForm(productContractDTO, salePositionDTO, positionId, null, null, request);
    }

    private void selectPaymentStep(HttpServletRequest request) {
        String selectPaymentStep = (String) getDto("selectPaymentStep");
        if (!"true".equals(selectPaymentStep)) {
            return;
        }

        String contractId = (String) getDto("contractId");
        ProductContractDTO productContractDTO = getProductContract(contractId, request);
        if (null == productContractDTO) {
            return;
        }

        SalePositionDTO salePositionDTO = getSalePositionDTO(
                productContractDTO.get("salePositionId").toString(),
                request);

        String payStepId = (String) getDto("payStepId");
        if (GenericValidator.isBlankOrNull(payStepId)) {
            return;
        }

        PaymentStepDTO paymentStepDTO = getPaymentStep(payStepId, request);
        if (null == paymentStepDTO) {
            return;
        }

        Integer positionId = null;
        if ("update".equals(operation)) {
            positionId = new Integer((String) getDto("positionId"));
        }


        BigDecimal paymentStepPrice = i.getPaymentStepAmount(productContractDTO, paymentStepDTO);

        setInvoicePositionPriceInForm(productContractDTO,
                salePositionDTO,
                positionId,
                new Integer(payStepId),
                paymentStepPrice,
                request);
    }

    private void unSelectProductContract() {
        String unSelectProductContract = (String) getDto("unSelectProductContract");
        if (!"true".equals(unSelectProductContract)) {
            return;
        }

        setDto("contractId", "");
        setDto("contractNumber", "");
        setDto("discount", "");
        setDto("discountValue", "");
        setDto("payStepId", "");
        setDto("isRelatedWithPartialFixedContract", false);
        setDto("disableQuantityField", false);
    }


    private void updatePositionText(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        String productId = (String) getDto("productId");
        String invoiceId = request.getParameter("invoiceId");
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        if (null == productId || "".equals(productId)) {
            setDto("text", "");
            return;
        }

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("productId", Integer.valueOf(productId));
        invoicePositionCmd.putParam("invoiceId", Integer.valueOf(invoiceId));
        invoicePositionCmd.putParam("companyId", companyId);
        invoicePositionCmd.setOp("getInvoicePositionText");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            String text = (String) resultDTO.get("getInvoicePositionText");
            setDto("text", text);
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL");
        }
    }

    private void readProductAccountAndUnit(HttpServletRequest request) {
        String productId = (String) getDto("productId");
        if (null == productId || "".equals(productId)) {
            return;
        }

        ProductReadLightCmd productReadLightCmd = new ProductReadLightCmd();
        productReadLightCmd.putParam("productId", Integer.valueOf(productId));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productReadLightCmd, request);

            String selectProductContract = (String) getDto("selectProductContract");
            String selectPaymentStep = (String) getDto("selectPaymentStep");
            String selectInvoicePosition = (String) getDto("selectInvoicePosition");
            String unSelectProductContract = (String) getDto("unSelectProductContract");
            if (!"true".equals(selectProductContract) &&
                    !"true".equals(selectPaymentStep) &&
                    !"true".equals(selectInvoicePosition) &&
                    !"true".equals(unSelectProductContract)) {
                setDto("unitPrice", resultDTO.get("price"));
                setDto("unitPriceGross", resultDTO.get("priceGross"));
            }

            if (!"true".equals(unSelectProductContract)) {
                Integer accountId = (Integer) resultDTO.get("accountId");
                setDto("accountId", accountId);
                setDto("vatId", resultDTO.get("vatId"));
                //read unit text
                readProductUnitText((Integer) resultDTO.get("unitId"), request);
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + ProductReadLightCmd.class.getName() + " FAIL");
        }
    }

    private void readProductUnitText(Integer unitId, HttpServletRequest request) {
        if (unitId != null) {
            ProductUnitCmd productUnitCmd = new ProductUnitCmd();
            productUnitCmd.putParam("op", "read");
            productUnitCmd.putParam("unitId", unitId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(productUnitCmd, request);
                setDto("unit", resultDTO.get("unitName"));
            } catch (AppLevelException e) {
                log.error("-> Execute " + ProductUnitCmd.class.getName() + " FAIL");
            }
        } else {
            setDto("unit", "");
        }
    }

    private BigDecimal getInvoicePositionOldTotalPrice() {
        return getInvoicePositionPrice("oldTotalPrice", "oldTotalPriceGross");
    }

    private BigDecimal getInvoicePositionPrice() {
        return getInvoicePositionPrice("unitPrice", "unitPriceGross");
    }

    private BigDecimal getOldDiscountValue() {
        String oldDiscountValue = (String) getDto("oldDiscountValue");
        if (GenericValidator.isBlankOrNull(oldDiscountValue)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(oldDiscountValue);
    }

    private ActionError validateProductContractAddress(ProductContractDTO productContractDTO) {
        Integer addressId = (Integer) invoiceDTO.get("addressId");
        Integer productContractAddressId = (Integer) productContractDTO.get("addressId");

        //productContract and invoice have distinct address identifiers
        if (!productContractAddressId.equals(addressId)) {
            return new ActionError("InvoicePosition.productContract.addressError", getDto("contractNumber"));
        }

        return null;
    }

    private void setInvoicePositionPriceInForm(ProductContractDTO contractDTO,
                                               SalePositionDTO salePositionDTO,
                                               Integer positionId,
                                               Integer payStepId,
                                               BigDecimal paymentStepPrice,
                                               HttpServletRequest request) {

        BigDecimal formQuantity = new BigDecimal("1.00");
        if (i.isSingleContract(contractDTO)) {
            formQuantity = (BigDecimal) salePositionDTO.get("quantity");
        }

        setDto("quantity", formQuantity);

        InvoicePositionDiscountValueCalculator invoicePositionDiscountValueCalculator =
                new InvoicePositionDiscountValueCalculator(
                        contractDTO,
                        salePositionDTO,
                        positionId,
                        payStepId,
                        null,
                        formQuantity,
                        paymentStepPrice,
                        request);

        invoicePositionDiscountValueCalculator.showInformation();

        setDto("discount", invoicePositionDiscountValueCalculator.getContractDiscount());
        setDto("discountValue", invoicePositionDiscountValueCalculator.getSuggestedDiscountValue());

        if (i.useNetCalculations(invoiceDTO)) {
            setDto("unitPrice", invoicePositionDiscountValueCalculator.getSuggestedPrice());
            setDto("unitPriceGross", null);
        }

        if (i.useGrossCalculations(invoiceDTO)) {
            setDto("unitPriceGross", invoicePositionDiscountValueCalculator.getSuggestedPrice());
            setDto("unitPrice", null);
        }
    }

    private void clearInvoiceFormInformation() {
        setDto("contractId", "");
        setDto("payStepId", "");
        setDto("contractNumber", "");
        setDto("productId", "");
        setDto("productName", "");
        setDto("unitPriceGross", "");
        setDto("unitPrice", "");
        setDto("quantity", "");
        setDto("unit", "");
        setDto("discountValue", "");
        setDto("isRelatedWithPartialFixedContract", false);
        setDto("disableQuantityField", false);
    }

    private BigDecimal getInvoicePositionPrice(String netKey, String grossKey) {
        BigDecimal price = null;

        if (i.useNetCalculations(invoiceDTO)) {
            if (getDto(netKey) instanceof BigDecimal) {
                price = (BigDecimal) getDto(netKey);
            } else {
                price = new BigDecimal(getDto(netKey).toString());
            }
        }

        if (i.useGrossCalculations(invoiceDTO)) {
            if (getDto(grossKey) instanceof BigDecimal) {
                price = (BigDecimal) getDto(grossKey);
            } else {
                price = new BigDecimal(getDto(grossKey).toString());
            }
        }

        return price;
    }

    private ActionErrors setSkipErrors(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors.add("emptyError", new ActionError("Admin.Company.new"));
        request.setAttribute("skipErrors", "true");
        return errors;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save") || null != request.getParameter("SaveAndNew");
    }
}
