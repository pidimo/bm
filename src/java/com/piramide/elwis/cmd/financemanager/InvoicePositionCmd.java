package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.productmanager.ProductReadLightCmd;
import com.piramide.elwis.cmd.salesmanager.ProductContractUtilCmd;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.productmanager.ProductText;
import com.piramide.elwis.domain.productmanager.ProductTextHome;
import com.piramide.elwis.domain.salesmanager.PaymentStep;
import com.piramide.elwis.domain.salesmanager.PaymentStepHome;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.ProductContractHome;
import com.piramide.elwis.dto.financemanager.InvoicePositionDTO;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoicePositionCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoicePositionCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        if ("canDeleteInvoicePositionFromCreditNote".equals(getOp())) {
            isRead = false;
            Integer invoiceId = EJBCommandUtil.i.getValueAsInteger(this, "invoiceId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            canDeleteInvoicePositionFromCreditNote(invoiceId, companyId);
        }

        if ("updateProductContractOpenAmount".equals(getOp())) {
            isRead = false;
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            updateProductContractOpenAmount(contractId, companyId, ctx);
        }

        if ("getInvoicePositions".equals(getOp())) {
            isRead = false;
            Integer invoiceId = EJBCommandUtil.i.getValueAsInteger(this, "invoiceId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getInvoicePositions(invoiceId, companyId);
        }

        if ("getInvoicePositionsByContract".equals(getOp())) {
            isRead = false;
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getInvoicePositionsByContract(contractId, companyId);
        }

        if ("getInvoicePosition".equals(getOp())) {
            isRead = false;
            Integer positionId = EJBCommandUtil.i.getValueAsInteger(this, "positionId");
            getInvoicePosition(positionId);
        }

        if ("getModifiedTotalPrice".equals(getOp())) {
            isRead = false;
            BigDecimal totalPrice = (BigDecimal) paramDTO.get("totalPrice");
            Integer vatId = (Integer) paramDTO.get("vatId");
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            getModifiedTotalPrice(totalPrice, invoiceId, vatId);
        }
        if ("getLastPositionNumber".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getLastPositionNumber(invoiceId, companyId);
        }
        if ("hasValidVatRates".equals(getOp())) {
            isRead = false;
            Integer vatId = (Integer) paramDTO.get("vatId");
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            hasValidVatRates(vatId, invoiceId);
        }
        if ("getInvoicePositionText".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer productId = (Integer) paramDTO.get("productId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getInvoicePositionText(invoiceId, productId, companyId);
        }
        if ("getProductTextByAddress".equals(getOp())) {
            isRead = false;
            Integer addressId = (Integer) paramDTO.get("addressId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer productId = (Integer) paramDTO.get("productId");
            getProductTextByAddress(addressId, productId, companyId);
        }
        if ("create".equals(getOp())) {
            isRead = false;
            create(getInvoicePositionDTO(), ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            InvoicePositionDTO dto = getInvoicePositionDTO();
            update(dto, ctx);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            InvoicePositionDTO dto = getInvoicePositionDTO();
            delete(dto, ctx);
        }
        if (isRead) {
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            InvoicePositionDTO dto = getInvoicePositionDTO();
            read(dto, checkReferences, ctx);
        }
    }

    private void read(InvoicePositionDTO invoicePositionDTO,
                      boolean checkReferences,
                      SessionContext ctx) {
        InvoicePosition invoicePosition =
                (InvoicePosition) ExtendedCRUDDirector.i.read(invoicePositionDTO, resultDTO, checkReferences);
        if (null == invoicePosition) {
            return;
        }

        if (checkReferences &&
                hasInvoicePayments(invoicePosition.getInvoiceId(), invoicePosition.getCompanyId(), ctx) &&
                !isCreditNote(invoicePosition.getInvoice())) {

            /*if (isCreditNote(invoicePosition.getInvoice())) {
                resultDTO.addResultMessage("InvoicePosition.CreditNotePaymentsError");
            }*/
            if (isInvoice(invoicePosition.getInvoice())) {
                resultDTO.addResultMessage("InvoicePosition.haveInvoicePaymentsError");
            }

            resultDTO.setForward("Fail");
            return;
        }

        if (null != invoicePosition.getFreetextId()) {
            resultDTO.put("text", new String(invoicePosition.getFinanceFreeText().getValue()));
        }

        //apply vatrate to invoice position total price
        BigDecimal modifiedTotalPrice = getModifiedTotalPrice(invoicePosition);

        resultDTO.put("oldTotalPrice", invoicePosition.getTotalPrice());
        resultDTO.put("oldTotalPriceGross", invoicePosition.getTotalPriceGross());
        resultDTO.put("oldDiscountValue", invoicePosition.getDiscountValue());

        //used in InvoicePositionForm to validate total values when edit InvoicePosition
        resultDTO.put("modifiedTotalPrice", modifiedTotalPrice);

        //read the relation with product contract only if exists
        if (null != invoicePosition.getContractId()) {
            boolean isRelatedWithPartialFixedContract = false;
            boolean disableQuantityField = false;

            ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
            productContractUtilCmd.setOp("getProductContractDTO");
            productContractUtilCmd.putParam("contractId", invoicePosition.getContractId());
            productContractUtilCmd.executeInStateless(ctx);

            ProductContractDTO productContractDTO =
                    (ProductContractDTO) productContractUtilCmd.getResultDTO().get("getProductContractDTO");

            if (null != productContractDTO) {
                resultDTO.put("contractNumber", productContractDTO.get("contractNumber"));
                resultDTO.put("oldContractId", invoicePosition.getContractId());


                //read paymentstep relation only if exists
                if (null != invoicePosition.getPayStepId()) {
                    productContractUtilCmd.setOp("getPaymentStepDTO");
                    productContractUtilCmd.putParam("paymentStepId", invoicePosition.getPayStepId());
                    productContractUtilCmd.executeInStateless(ctx);

                    PaymentStepDTO paymentStepDTO =
                            (PaymentStepDTO) productContractUtilCmd.getResultDTO().get("getPaymentStepDTO");
                    if (null != paymentStepDTO) {
                        resultDTO.put("payDate", paymentStepDTO.get("payDate"));
                        resultDTO.put("payStepId", invoicePosition.getPayStepId());
                        isRelatedWithPartialFixedContract = true;
                        disableQuantityField = true;
                    }
                }
            }

            //used in jsp page to enable or disable the product contract change option
            resultDTO.put("isRelatedWithPartialFixedContract", isRelatedWithPartialFixedContract);

            //user in user interface to enable or disable quantity field
            resultDTO.put("disableQuantityField", disableQuantityField);
        }

    }

    protected void create(InvoicePositionDTO invoicePositionDTO, String text, SessionContext ctx) {
        FinanceFreeText freeText = null;
        if (null != text && !"".equals(text.trim())) {
            freeText = createFreeText(text, (Integer) invoicePositionDTO.get("companyId"));
        }

        if (null != freeText) {
            invoicePositionDTO.put("freetextId", freeText.getFreeTextId());
        }


        InvoicePosition invoicePosition =
                (InvoicePosition) ExtendedCRUDDirector.i.create(invoicePositionDTO, resultDTO, false);

        //update invoiceVats for invoice
        InvoiceVatCmd invoiceVatCmd = new InvoiceVatCmd();
        invoiceVatCmd.putParam("companyId", invoicePosition.getCompanyId());
        invoiceVatCmd.putParam("invoiceId", invoicePosition.getInvoiceId());
        invoiceVatCmd.setOp("update");
        invoiceVatCmd.executeInStateless(ctx);

        //update invoice total amounts
        updateInvoiceAmounts(invoicePosition.getInvoiceId());

        //update credit note payments
        updateCreditNotePayments(invoicePosition.getInvoiceId(), ctx);

        //update the open amount for related product contract
        if (null != invoicePosition.getContractId()) {
            updateProductContractOpenAmount(invoicePosition.getContractId(), invoicePosition.getCompanyId(), ctx);
        }
    }

    protected void create(InvoicePositionDTO invoicePositionDTO, SessionContext ctx) {
        String text = (String) paramDTO.get("text");

        create(invoicePositionDTO, text, ctx);
    }

    private void updateCreditNotePayments(Integer invoiceId, SessionContext ctx) {
        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.setOp("updateCreditNotePayments");
        invoicePaymentCmd.putParam("invoiceId", invoiceId);

        invoicePaymentCmd.executeInStateless(ctx);
    }

    private void update(InvoicePositionDTO invoicePositionDTO, SessionContext ctx) {


        InvoicePosition invoicePosition =
                (InvoicePosition) ExtendedCRUDDirector.i.update(invoicePositionDTO, resultDTO, false, true, true, "Fail");

        if (null == invoicePosition) {
            return;
        }

        if (resultDTO.isFailure()) {
            //version error, put new freetext
            if (null != invoicePosition.getFreetextId()) {
                resultDTO.put("text", new String(invoicePosition.getFinanceFreeText().getValue()));
            }

            //read new product name
            ProductReadLightCmd productReadLightCmd = new ProductReadLightCmd();
            productReadLightCmd.putParam("productId", invoicePosition.getProductId());
            productReadLightCmd.executeInStateless(ctx);
            String newProductName = (String) productReadLightCmd.getResultDTO().get("productName");
            resultDTO.put("productName", newProductName);
            return;
        }

        String text = (String) paramDTO.get("text");
        if (null == invoicePosition.getFreetextId()) {
            if (null != text && !"".equals(text)) {
                FinanceFreeText freeText = createFreeText(text, invoicePosition.getCompanyId());
                invoicePosition.setFreetextId(freeText.getFreeTextId());
            }
        } else if (null != text) {
            invoicePosition.getFinanceFreeText().setValue(text.getBytes());
        }

        //update invoiceVats for invoice
        InvoiceVatCmd invoiceVatCmd = new InvoiceVatCmd();
        invoiceVatCmd.putParam("companyId", invoicePosition.getCompanyId());
        invoiceVatCmd.putParam("invoiceId", invoicePosition.getInvoiceId());
        invoiceVatCmd.setOp("update");
        invoiceVatCmd.executeInStateless(ctx);

        //update invoice total amounts
        updateInvoiceAmounts(invoicePosition.getInvoiceId());

        //update credit note payments
        updateCreditNotePayments(invoicePosition.getInvoiceId(), ctx);

        //update the open amount for related product contract
        Integer oldContractId = (Integer) invoicePositionDTO.get("oldContractId");
        if (null != invoicePosition.getContractId()) {
            updateProductContractOpenAmount(invoicePosition.getContractId(), invoicePosition.getCompanyId(), ctx);
            if (null != oldContractId && !invoicePosition.getContractId().equals(oldContractId)) {
                updateProductContractOpenAmount(oldContractId, invoicePosition.getCompanyId(), ctx);
            }
        } else {
            if (null != oldContractId) {
                updateProductContractOpenAmount(oldContractId, invoicePosition.getCompanyId(), ctx);
            }
        }

        //to show alert messages after of update
        resultDTO.put("invoiceId", invoicePosition.getInvoiceId());
        resultDTO.put("contractId", invoicePosition.getContractId());
    }

    private void updateProductContractOpenAmount(Integer contractId, Integer companyId, SessionContext ctx) {
        PaymentStepHome paymentStepHome =
                (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        List paymentSteps = new ArrayList();
        try {
            paymentSteps = (List) paymentStepHome.findByContractId(contractId, companyId);
        } catch (FinderException e) {
            //
        }

        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.setOp("updateOpenAmount");
        productContractUtilCmd.putParam("contractId", contractId);
        productContractUtilCmd.putParam("companyId", companyId);
        for (int i = 0; i < paymentSteps.size(); i++) {
            PaymentStep paymentStep = (PaymentStep) paymentSteps.get(i);
            String keyId = "payStepId_" + (i + 1);
            String amountId = "payAmount_" + (i + 1);
            String payDateId = "payDate_" + (i + 1);
            productContractUtilCmd.putParam(keyId, paymentStep.getPayStepId());
            productContractUtilCmd.putParam(amountId, paymentStep.getPayAmount().toString());
            productContractUtilCmd.putParam(payDateId, paymentStep.getPayDate());
        }

        productContractUtilCmd.executeInStateless(ctx);
    }

    private void delete(InvoicePositionDTO invoicePositionDTO, SessionContext ctx) {
        InvoicePosition invoicePosition =
                (InvoicePosition) ExtendedCRUDDirector.i.read(invoicePositionDTO, resultDTO, true);

        if (null == invoicePosition) {
            resultDTO.setForward("Fail");
            return;
        }

        Integer invoiceId = invoicePosition.getInvoiceId();
        Integer companyId = invoicePosition.getCompanyId();

        if (hasInvoicePayments(invoiceId, companyId, ctx) && !isCreditNote(invoicePosition.getInvoice())) {
            resultDTO.addResultMessage("InvoicePosition.haveInvoicePaymentsError");
            resultDTO.setForward("Fail");
            return;
        }

        Integer contractId = invoicePosition.getContractId();

        ExtendedCRUDDirector.i.delete(invoicePositionDTO, resultDTO, true, "Fail");

        //update invoiceVats for invoice
        InvoiceVatCmd invoiceVatCmd = new InvoiceVatCmd();
        invoiceVatCmd.putParam("companyId", companyId);
        invoiceVatCmd.putParam("invoiceId", invoiceId);
        invoiceVatCmd.setOp("update");
        invoiceVatCmd.executeInStateless(ctx);

        //update invoice total amounts
        updateInvoiceAmounts(invoiceId);

        //update credit note payments
        updateCreditNotePayments(invoiceId, ctx);

        //update open amount for related product contract
        if (null != contractId) {
            updateProductContractOpenAmount(contractId, companyId, ctx);
        }
    }

    private BigDecimal getModifiedTotalPrice(BigDecimal value, Integer invoiceId, Integer vatId) {
        Invoice invoice = searchInvoice(invoiceId);

        BigDecimal vatRate;
        try {
            vatRate = getVatRate(vatId, invoiceId);
        } catch (FinderException e) {
            log.debug("-> Return TotalPrice because vatRate has been deleted.");
            resultDTO.put("getModifiedTotalPrice", value);
            return value;
        }

        BigDecimal totalPrice = getModifiedTotalPrice(value, vatRate, invoice);
        resultDTO.put("getModifiedTotalPrice", totalPrice);
        return totalPrice;
    }

    private BigDecimal getModifiedTotalPrice(InvoicePosition invoicePosition) {
        Invoice invoice = searchInvoice(invoicePosition.getInvoiceId());

        if (FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross())) {
            return getModifiedTotalPrice(invoicePosition.getTotalPrice(),
                    invoicePosition.getVatRate(),
                    invoice);
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            return getModifiedTotalPrice(invoicePosition.getTotalPriceGross(),
                    invoicePosition.getVatRate(),
                    invoice);
        }

        return invoicePosition.getTotalPrice();
    }

    private BigDecimal getModifiedTotalPrice(BigDecimal value, BigDecimal vatRate, Invoice invoice) {
        BigDecimal totalPrice = value;

        if (FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross())) {
            totalPrice =
                    InvoiceUtil.i.calculateTotalPriceGrossForInvoicePositions(
                            value, vatRate);
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            totalPrice = value;
        }

        return totalPrice;
    }

    private Integer getLastPositionNumber(Integer invoiceId, Integer companyId) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Integer result = null;
        try {
            result = invoicePositionHome.selectMaxPositionNumber(invoiceId, companyId);
        } catch (FinderException e) {
            log.debug("-> Execute selectMaxPositionNumber in InvoicePositionHome invoiceId=" + invoiceId + " FAIL");
        }

        resultDTO.put("getLastPositionNumber", result);
        return result;
    }

    private boolean hasInvoicePayments(Integer invoiceId, Integer companyId, SessionContext ctx) {
        InvoicePaymentCmd invoicePaymentCmd = new InvoicePaymentCmd();
        invoicePaymentCmd.putParam("invoiceId", invoiceId);
        invoicePaymentCmd.putParam("companyId", companyId);
        invoicePaymentCmd.setOp("haveInvoicePayments");
        invoicePaymentCmd.executeInStateless(ctx);
        ResultDTO customResultDTO = invoicePaymentCmd.getResultDTO();
        return (Boolean) customResultDTO.get("haveInvoicePayments");
    }

    private FinanceFreeText createFreeText(String text, Integer companyId) {
        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_INVOICEPOSITION);
        } catch (CreateException e) {
            log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
        }
        return freeText;
    }

    private boolean hasValidVatRates(Integer vatId, Integer invoiceId) {
        boolean hasValidVatRates;
        try {
            hasValidVatRates = null != getVatRate(vatId, invoiceId);
        } catch (FinderException e) {
            hasValidVatRates = false;
        }

        resultDTO.put("hasValidVatRates", hasValidVatRates);

        return hasValidVatRates;
    }

    private BigDecimal getVatRate(Integer vatId, Integer invoiceId) throws FinderException {
        InvoiceHome invoiceHome = getInvoiceHome();
        Invoice invoice = invoiceHome.findByPrimaryKey(invoiceId);
        Integer invoiceDate = invoice.getInvoiceDate();

        VatRateHome vatRateHome =
                (VatRateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VATRATE);

        BigDecimal value = null;
        Collection varRates = vatRateHome.findByVatId(vatId);
        for (Object object : varRates) {
            VatRate vatRate = (VatRate) object;
            if (vatRate.getValidFrom() <= invoiceDate) {
                value = vatRate.getVatRate();
            }
        }

        return value;
    }

    private InvoicePositionDTO getInvoicePositionDTO() {
        InvoicePositionDTO dto = new InvoicePositionDTO();

        EJBCommandUtil.i.setValueAsInteger(this, dto, "positionId");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "accountId");
        dto.put("companyId", paramDTO.get("companyId"));
        EJBCommandUtil.i.setValueAsInteger(this, dto, "contractId");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "invoiceId");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "number");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "payStepId");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "productId");
        EJBCommandUtil.i.setValueAsInteger(this, dto, "salePositionId");

        dto.put("quantity", paramDTO.get("quantity"));
        dto.put("unit", paramDTO.get("unit"));
        EJBCommandUtil.i.setValueAsInteger(this, dto, "vatId");

        EJBCommandUtil.i.setValueAsBigDecimal(this, dto, "unitPriceGross");

        EJBCommandUtil.i.setValueAsBigDecimal(this, dto, "unitPrice");

        EJBCommandUtil.i.setValueAsBigDecimal(this, dto, "totalPrice");
        EJBCommandUtil.i.setValueAsBigDecimal(this, dto, "totalPriceGross");
        EJBCommandUtil.i.setValueAsBigDecimal(this, dto, "discountValue");


        //the value is used to detect changes with product contract relation.
        EJBCommandUtil.i.setValueAsInteger(this, dto, "oldContractId");

        dto.put("version", paramDTO.get("version"));

        //use addNotFoundMsgTo(ResultDTO resultDTO) in InvoicePositionDTO 
        dto.put("productName", paramDTO.get("productName"));

        //discount
        Object discountObj = paramDTO.get("discount");
        BigDecimal discountPercent = null;
        if (discountObj != null && discountObj.toString().length() > 0) {
            discountPercent = (discountObj instanceof BigDecimal) ? (BigDecimal) discountObj : new BigDecimal(discountObj.toString());
            dto.put("discount", discountPercent);
        }

        if (null != dto.get("unitPrice") &&
                !"delete".equals(getOp())) {
            BigDecimal unitPrice = (BigDecimal) dto.get("unitPrice");
            BigDecimal quantity = (BigDecimal) dto.get("quantity");
            dto.put("totalPrice", BigDecimalUtils.multiply(unitPrice, quantity));
        }

        if (null != dto.get("unitPriceGross") && !"delete".equals(getOp())) {
            BigDecimal unitPriceGross = (BigDecimal) dto.get("unitPriceGross");
            BigDecimal quantity = (BigDecimal) dto.get("quantity");
            dto.put("totalPriceGross", BigDecimalUtils.multiply(unitPriceGross, quantity));
        }

        if (null != getValueAsInteger("vatId") &&
                null != getValueAsInteger("invoiceId") &&
                !"delete".equals(getOp())) {
            try {
                dto.put("vatRate", getVatRate(getValueAsInteger("vatId"), getValueAsInteger("invoiceId")));
            } catch (FinderException e) {
                log.error("->Read VatRate invoiceId=" + getValueAsInteger("invoiceId") + " vatId" +
                        getValueAsInteger("vatId") + " FAIL");
            }
        }
        dto.put("withReferences", paramDTO.get("withReferences"));

        log.debug("-> Work on" + dto + " OK");
        return dto;
    }

    private Integer getValueAsInteger(String key) {
        Integer value = null;
        if (null != paramDTO.get(key) &&
                !"".equals(paramDTO.get(key).toString().trim())) {
            try {
                value = Integer.valueOf(paramDTO.get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + paramDTO.get(key) + " FAIL");
            }
        }

        return value;
    }

    private void updateInvoiceAmounts(Integer invoiceId) {
        try {
            Invoice invoice = getInvoice(invoiceId);
            //calculate total amount gross
            invoice.setTotalAmountGross(InvoiceUtil.i.calculateTotalAmountGross(invoice));
            //calculate total amount net
            invoice.setTotalAmountNet(InvoiceUtil.i.calculateTotalAmountNet(invoice));
            //calculate open amount, it always must be done at the end because the totals may have changed
            invoice.setOpenAmount(InvoiceUtil.i.calculateOpenAmount(invoice));
        } catch (FinderException e) {
            log.error("-> Read Invoice invoiceId=" + invoiceId + " FAIL");
        }
    }

    private Invoice searchInvoice(Integer invoiceId) {
        try {
            return getInvoice(invoiceId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Invoice getInvoice(Integer invoiceId) throws FinderException {
        InvoiceHome invoiceHome = getInvoiceHome();
        return invoiceHome.findByPrimaryKey(invoiceId);
    }

    private String getInvoicePositionText(Integer invoiceId,
                                          Integer productId,
                                          Integer companyId) {
        String result = "";
        Integer addressId;
        try {
            addressId = getInvoice(invoiceId).getAddressId();
        } catch (FinderException e) {
            log.debug("->Read Invoice addressId=" + invoiceId + " FAIL");
            resultDTO.put("getInvoicePositionText", result);
            return result;
        }

        Integer addressLanguageId = null;
        AddressHome addressHome = getAddressHome();
        try {
            addressLanguageId = addressHome.findByPrimaryKey(addressId).getLanguageId();
        } catch (FinderException e) {
            log.debug("->Read Address addressId=" + addressId + " FAIL");
        }

        result = getProductText(productId, addressLanguageId, companyId);
        resultDTO.put("getInvoicePositionText", result);
        return result;
    }

    private String getProductTextByAddress(Integer addressId, Integer productId, Integer companyId) {
        String result = "";

        if (null == addressId) {
            resultDTO.put("getProductTextByAddress", result);
            return result;
        }

        Integer addressLanguageId = null;
        AddressHome addressHome = getAddressHome();
        try {
            addressLanguageId = addressHome.findByPrimaryKey(addressId).getLanguageId();
        } catch (FinderException e) {
            log.debug("->Execute AddressHome.findByPrimaryKey[" + addressId + "] FAIL");
        }

        result = getProductText(productId, addressLanguageId, companyId);
        resultDTO.put("getProductTextByAddress", result);
        return result;
    }

    private String getProductText(Integer productId, Integer languageId, Integer companyId) {
        ProductTextHome productTextHome = getProductTextHome();
        if (null != languageId) {
            try {
                ProductText productText =
                        productTextHome.findProductTextByLanguageId(productId, languageId, companyId);
                String text = new String(productText.getProductFreeText().getValue());
                if (!"".equals(text.trim())) {
                    return text;
                }
            } catch (FinderException e) {
                log.debug("-> Read ProductText productId=" +
                        productId + " languageId=" + languageId + " FAIL");
            }
        }
        try {
            ProductText productText =
                    productTextHome.findDefaultProductText(productId, companyId);

            String text = new String(productText.getProductFreeText().getValue());
            if (!"".equals(text.trim())) {
                return text;
            }

        } catch (FinderException e) {
            log.debug("-> Read Default ProductText productId=" +
                    productId + " companyId=" + companyId + " FAIL");
            return getProductNameTranslation(productId, languageId);
        }
        return "";
    }

    private String getProductNameTranslation(Integer productId, Integer languageId) {
        ProductHome productHome = getProductHome();

        try {
            Product product = productHome.findByPrimaryKey(productId);
            if (null != product.getLangTextId() && null != languageId) {
                return getLangText(product.getLangTextId(), languageId);
            }

            return product.getProductName();
        } catch (FinderException e) {
            log.debug("->Read Product productId=" + productId + " FAIL");
        }
        return "";
    }

    private String getLangText(Integer langTextId, Integer languageId) {
        LangTextHome langTextHome = getLangTextHome();

        LangTextPK langTextPK = new LangTextPK();
        langTextPK.langTextId = langTextId;
        langTextPK.languageId = languageId;

        LangText langText = null;
        try {
            langText = langTextHome.findByPrimaryKey(langTextPK);
            //return translation by language
            if (!"".equals(langText.getText().trim())) {
                return langText.getText();
            }
        } catch (FinderException e) {
            log.debug("->Read LangText langTextPK=" + langTextPK + " FAIL");
        }

        try {
            langText = langTextHome.findByIsDefault(langTextId);
            //return default translation
            if (!"".equals(langText.getText().trim())) {
                return langText.getText();
            }
        } catch (FinderException e) {
            log.debug("->Read Default LangText langTextId=" + languageId + " FAIL");
        }

        return "";
    }

    /**
     * finds all <code>InvoicePosition</code> objects associated to <code>Invoice</code> with identifier equal to
     * <code>invoiceId</code> parameter.
     * After of this, puts in <code>resultDTO</code> object a <code>List</code> that contain all
     * <code>InvoicePosition</code> that was found.
     *
     * @param invoiceId <code>Integer</code> objet that is the <code>Invoice</code> itentifier.
     * @param companyId <code>Integer</code> object that is the company identifier.
     * @return <code>List</code> that contain all <code>InvoicePositionDTO</code>.
     */
    protected List getInvoicePositions(Integer invoiceId, Integer companyId) {
        InvoicePositionHome invoicePositionHome = getInvoicePositionHome();
        List<InvoicePositionDTO> result = new ArrayList<InvoicePositionDTO>();

        try {
            List invoicePositions =
                    (List) invoicePositionHome.findByInvoiceId(invoiceId, companyId);
            for (int i = 0; i < invoicePositions.size(); i++) {
                InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);

                InvoicePositionDTO invoicePositionDTO = new InvoicePositionDTO();
                String produtName = getProductNameTranslation(invoicePosition.getProductId(), null);
                invoicePositionDTO.put("productName", produtName);

                DTOFactory.i.copyToDTO(invoicePosition, invoicePositionDTO);
                Boolean isCreditNote = false;
                if (isCreditNote(invoicePosition.getInvoice())) {
                    isCreditNote = true;
                }
                invoicePositionDTO.put("isCreditNote", isCreditNote);

                result.add(invoicePositionDTO);
            }
        } catch (FinderException e) {
            //
        }
        resultDTO.put("getInvoicePositions", result);

        return result;
    }

    protected List getInvoicePositionsByContract(Integer contractId, Integer companyId) {
        InvoicePositionHome invoicePositionHome = getInvoicePositionHome();
        List<InvoicePositionDTO> result = new ArrayList<InvoicePositionDTO>();
        try {
            List invoicePositions =
                    (List) invoicePositionHome.findByContractId(contractId, companyId);
            for (int i = 0; i < invoicePositions.size(); i++) {
                InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);

                InvoicePositionDTO invoicePositionDTO = new InvoicePositionDTO();
                String produtName = getProductNameTranslation(invoicePosition.getProductId(), null);
                invoicePositionDTO.put("productName", produtName);

                DTOFactory.i.copyToDTO(invoicePosition, invoicePositionDTO);
                Boolean isCreditNote = false;
                if (isCreditNote(invoicePosition.getInvoice())) {
                    isCreditNote = true;
                }
                invoicePositionDTO.put("isCreditNote", isCreditNote);

                result.add(invoicePositionDTO);
            }
        } catch (FinderException e) {
            //
        }
        resultDTO.put("getInvoicePositionsByContract", result);

        return result;
    }

    protected void canDeleteInvoicePositionFromCreditNote(Integer invoiceId, Integer companyId) {

        InvoicePositionHome invoicePositionHome = getInvoicePositionHome();
        List invoicePositions = new ArrayList();
        try {
            invoicePositions =
                    (List) invoicePositionHome.findByInvoiceId(invoiceId, companyId);
        } catch (FinderException e) {
            //
        }

        boolean result = true;
        for (int i = 0; i < invoicePositions.size(); i++) {
            InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);

            if (null == invoicePosition.getContractId()) {
                continue;
            }

            ProductContract contract = getProductContract(invoicePosition.getContractId());
            BigDecimal openAmount = contract.getOpenAmount();

            BigDecimal totalPaid = BigDecimal.ZERO;
            if (useInvoiceNetCalculations(invoicePosition.getInvoice())) {
                totalPaid = invoicePosition.getTotalPrice();
            }

            if (useInvoiceGrossCalculations(invoicePosition.getInvoice())) {
                totalPaid = invoicePosition.getTotalPriceGross();
            }

            result = BigDecimalUtils.subtract(openAmount, totalPaid).compareTo(BigDecimal.ZERO) >= 0;
            if (result) {
                break;
            }
        }

        resultDTO.put("canDeleteInvoicePositionFromCreditNote", result);
    }

    protected void getInvoicePosition(Integer positionId) {
        InvoicePosition invoicePosition = findInvoicePosition(positionId);
        if (null != invoicePosition) {
            InvoicePositionDTO dto = new InvoicePositionDTO();
            DTOFactory.i.copyToDTO(invoicePosition, dto);
            resultDTO.put("getInvoicePosition", dto);
        } else {
            resultDTO.put("getInvoicePosition", null);
        }
    }

    protected InvoicePosition findInvoicePosition(Integer positionId) {
        InvoicePositionHome invoicePositionHome = getInvoicePositionHome();
        try {
            return invoicePositionHome.findByPrimaryKey(positionId);
        } catch (FinderException e) {
            //
        }
        return null;
    }

    private BigDecimal divideBigDecimal(BigDecimal amount, BigDecimal divisor) {
        return amount.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getPercentage(BigDecimal amount, BigDecimal percent) {
        return divideBigDecimal(amount.multiply(percent), new BigDecimal(100));
    }

    private BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        return amount.subtract(getPercentage(amount, discountPercent));
    }


    public boolean isStateful() {
        return false;
    }

    private InvoiceHome getInvoiceHome() {
        return (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
    }

    private InvoicePositionHome getInvoicePositionHome() {
        return (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
    }

    private AddressHome getAddressHome() {
        return (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
    }

    private ProductTextHome getProductTextHome() {
        return (ProductTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTTEXT);
    }

    private ProductHome getProductHome() {
        return (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
    }

    private LangTextHome getLangTextHome() {
        return (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
    }

    private boolean isCreditNote(Invoice invoice) {
        return FinanceConstants.InvoiceType.CreditNote.equal(invoice.getType());
    }

    private boolean isCreditNoteRelatedToContract(InvoicePosition invoicePosition) {
        return isCreditNote(invoicePosition.getInvoice()) && invoicePosition.getContractId() != null;
    }

    private boolean isInvoice(Invoice invoice) {
        return FinanceConstants.InvoiceType.Invoice.equal(invoice.getType());
    }

    private ProductContract getProductContract(Integer contractId) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            return productContractHome.findByPrimaryKey(contractId);
        } catch (FinderException e) {
            return null;
        }
    }

    private boolean useInvoiceNetCalculations(Invoice invoice) {
        return FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross());
    }

    private boolean useInvoiceGrossCalculations(Invoice invoice) {
        return FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross());
    }
}

