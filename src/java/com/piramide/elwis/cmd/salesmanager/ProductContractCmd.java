package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.salesmanager.util.ProcessContractPayMethodUtil;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public abstract class ProductContractCmd extends SaleManagerCmd {
    private Log log = LogFactory.getLog(ProductContractCmd.class);
    private static final BigDecimal oneHundred = new BigDecimal(100);

    protected List<PaymentStepDTO> getPaymentStepDTOs(Integer installment) {
        final String keyId = "payStepId_";
        final String amountId = "payAmount_";
        final String payDateId = "payDate_";

        List<PaymentStepDTO> dtos = new ArrayList<PaymentStepDTO>();

        for (int i = 1; i <= installment; i++) {
            Integer payStepId = EJBCommandUtil.i.getValueAsInteger(this, keyId + i);
            String payAmount = (String) paramDTO.get(amountId + i);
            Integer payDate = EJBCommandUtil.i.getValueAsInteger(this, payDateId + i);
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");


            PaymentStepDTO paymentStepDTO = new PaymentStepDTO();
            paymentStepDTO.put("companyId", companyId);
            paymentStepDTO.put("payStepId", payStepId);
            paymentStepDTO.put("payDate", payDate);

            if (null != payAmount && !"".equals(payAmount)) {
                paymentStepDTO.put("payAmount", new BigDecimal(payAmount));
            }

            /*
            * use to update paymentamount when price of contract has changed and this paymentStep have invoiceposition
            * this property is setting in productContractForm, when validate paymentSteps values,
            * if value is true, payamount is updated.
            * */
            paymentStepDTO.put("canUpdatePayAmount", paramDTO.get("canUpdatePayAmount_" + i));


            dtos.add(paymentStepDTO);
        }

        return dtos;

    }

    protected ProductContractDTO getProductContractDTO() {
        ProductContractDTO productContractDTO = new ProductContractDTO();

        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "contractId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "addressId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "contactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "amounType");
        productContractDTO.put("companyId", paramDTO.get("companyId"));
        productContractDTO.put("grouping", paramDTO.get("grouping"));
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "contractEndDate");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "contractTypeId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "currencyId");
        productContractDTO.put("discount", paramDTO.get("discount"));
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "installment");
        productContractDTO.put("matchCalendarPeriod", EJBCommandUtil.i.getValueAsInteger(this, "matchCalendarPeriod"));
        productContractDTO.put("openAmount", paramDTO.get("openAmount"));
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "orderDate");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "payConditionId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "payMethod");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "payPeriod");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "payStartDate");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "invoicedUntil");
        productContractDTO.put("price", paramDTO.get("price"));
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "pricePeriod");
        EJBCommandUtil.i.setValueAsBigDecimal(this, productContractDTO, "pricePerMonth");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "productId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "salePositionId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "sellerId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "vatId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "version");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "netGross");
        productContractDTO.put("contractNumber", paramDTO.get("contractNumber"));
        productContractDTO.put("daysToRemind", paramDTO.get("daysToRemind"));
        productContractDTO.put("invoiceDelay", paramDTO.get("invoiceDelay"));
        productContractDTO.put("reminderTime", paramDTO.get("reminderTime"));
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "sentAddressId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "sentContactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, productContractDTO, "additionalAddressId");
        productContractDTO.put("cancelledContract", paramDTO.get("cancelledContract"));

        //use in CRUD utility
        productContractDTO.put("withReferences", paramDTO.get("withReferences"));

        //use in ui to show product or address name
        productContractDTO.put("addressName", paramDTO.get("addressName"));
        productContractDTO.put("productName", paramDTO.get("productName"));

        return productContractDTO;
    }

    protected void defineContractNextInvoiceDate(ProductContract contract) {
        Integer nextInvoiceDate = null;

        if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {
            nextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(contract.getInvoiceDelay(), contract.getPayStartDate(), contract.getInvoicedUntil());
        }

        contract.setNextInvoiceDate(nextInvoiceDate);
    }

    protected BigDecimal buildPayAmount(BigDecimal discount_N, BigDecimal totalPrice_A) {
        if (null == discount_N) {
            discount_N = new BigDecimal(0.0);
        }

        return totalPrice_A.multiply(oneHundred).divide(oneHundred.subtract(discount_N));
    }

    protected Collection getStepsAlreadyPaid(ProductContract productContract) {
        List<PaymentStep> result = new ArrayList<PaymentStep>();

        Collection paymentSteps = productContract.getPaymentSteps();
        if (null != paymentSteps) {
            for (Object object : paymentSteps) {
                PaymentStep paymentStep = (PaymentStep) object;
                if (null != getPositiveInvoicePosition(paymentStep)) {
                    result.add(paymentStep);
                }
            }
        }
        return result;
    }

    protected PaymentStep getPaymentStep(Integer payStepId) {
        PaymentStepHome paymentStepHome =
                (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        try {
            return paymentStepHome.findByPrimaryKey(payStepId);
        } catch (FinderException e) {
            log.debug("-> Read PaymentStep [payStepId=" + payStepId + "] FAIL");
            return null;
        }
    }


    protected void removePaymentSteps(Integer contractId, Integer companyId, SessionContext ctx) {
        try {
            removePaymentSteps(contractId, companyId);
        } catch (RemoveException e) {
            ctx.setRollbackOnly();
        }
    }

    /**
     * @param contractId
     * @param companyId
     * @throws RemoveException
     */
    protected void removePaymentSteps(Integer contractId, Integer companyId) throws RemoveException {
        PaymentStepHome paymentStepHome =
                (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        List paymentSteps = null;
        try {
            paymentSteps = (List) paymentStepHome.findByContractId(contractId, companyId);
        } catch (FinderException e) {
            log.debug("-> Read PaymentSteps [contractId=" + contractId + ", companyId=" + companyId + "] FAIL");
        }
        if (null != paymentSteps) {
            for (int i = 0; i < paymentSteps.size(); i++) {
                PaymentStep paymentStep = (PaymentStep) paymentSteps.get(i);
                paymentStep.remove();
            }
        }
    }

    /**
     * Calculates the total amount that was paid, according to the <code>InvoicePosition</code> objects associated to the
     * <code>ProductContract</code>.
     *
     * @param productContract <code>ProductContract</code> object to calculate the total paid.
     * @return <code>BigDecimal</code> that is the total paid.
     */
    protected BigDecimal getTotalPaid(ProductContract productContract) {
        List<InvoicePosition> invoicePositions = getAllInvoicePositions(productContract);

        return getTotalPaid(invoicePositions);
    }

    protected BigDecimal getTotalPaid(List invoicePositions) {
        BigDecimal total = new BigDecimal(0.0);
        for (int i = 0; i < invoicePositions.size(); i++) {
            InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);
            BigDecimal totalPrice_A = getTotalPrice(invoicePosition);

            if (isCreditNote(invoicePosition.getInvoice())) {
                totalPrice_A = BigDecimalUtils.multiply(totalPrice_A, new BigDecimal(-1));
            }

            total = total.add(totalPrice_A);
        }

        return total;
    }

    /**
     * Sum the total prices (net or gross) of the <code>InvoicePosition</code> objects, this method does not apply
     * any conversion for the total prices.
     *
     * @param invoicePositions <code>List</code> that contains <code>InvoicePosition</code> objects whose total prices
     *                         to be added.
     * @return <code>BigDecimal</code> object that is the result of the sum.
     */
    protected BigDecimal sumInvoicePositions(List invoicePositions) {
        BigDecimal total = new BigDecimal(0.0);
        for (int i = 0; i < invoicePositions.size(); i++) {
            InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);
            BigDecimal totalPrice = getInvoicePositionTotalPrice(invoicePosition);

            if (isCreditNote(invoicePosition.getInvoice())) {
                totalPrice = BigDecimalUtils.multiply(totalPrice, new BigDecimal(-1));
            }

            total = total.add(totalPrice);
        }

        return total;
    }

    protected BigDecimal getAbsoluteTotalPrice(InvoicePosition invoicePosition) {
        return getTotalPrice(invoicePosition).abs();
    }

    private BigDecimal getTotalPrice(InvoicePosition invoicePosition) {
        if (null == invoicePosition.getDiscount()) {
            if (FinanceConstants.NetGrossFLag.NET.equal(invoicePosition.getInvoice().getNetGross())) {
                return invoicePosition.getTotalPrice();
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(invoicePosition.getInvoice().getNetGross())) {
                return invoicePosition.getTotalPriceGross();
            }
        }

        return ProcessContractPayMethodUtil.fixInvoicePositionTotalPrice(invoicePosition);
    }

    private BigDecimal getInvoicePositionTotalPrice(InvoicePosition invoicePosition) {
        if (FinanceConstants.NetGrossFLag.NET.equal(invoicePosition.getInvoice().getNetGross())) {
            return invoicePosition.getTotalPrice();
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoicePosition.getInvoice().getNetGross())) {
            return invoicePosition.getTotalPriceGross();
        }

        return null;
    }

    protected BigDecimal divideBigDecimal(BigDecimal amount, BigDecimal divisor) {
        return BigDecimalUtils.divide(amount, divisor);
    }

    protected BigDecimal calculateOpenAmount(BigDecimal price, BigDecimal totalPaid) {
        return BigDecimalUtils.subtract(price, totalPaid);
    }

    protected BigDecimal calculateInvoicedPaymentStepValue(PaymentStep invoicedPaymentStep,
                                                           ProductContract productContract) {
        List invoicePositions = getInvoicePositions(invoicedPaymentStep, productContract);

        BigDecimal total = new BigDecimal(0.0);
        for (int i = 0; i < invoicePositions.size(); i++) {
            InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);
            BigDecimal totalPrice_A = getTotalPrice(invoicePosition);

            if (FinanceConstants.InvoiceType.CreditNote.equal(invoicePosition.getInvoice().getType())) {
                totalPrice_A = BigDecimalUtils.multiply(totalPrice_A, new BigDecimal(-1));
            }

            total = total.add(totalPrice_A);
        }

        if (SalesConstants.AmounType.AMOUNT.equal(productContract.getAmounType())) {
            return total;
        }

        if (SalesConstants.AmounType.PERCENTAGE.equal(productContract.getAmounType())) {
            BigDecimal asPercentage = BigDecimalUtils.divide(
                    total.multiply(oneHundred), productContract.getPrice()
            );

            return asPercentage;
        }

        return null;
    }

    protected ProductContract getProductContract(Integer contractId) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            return productContractHome.findByPrimaryKey(contractId);
        } catch (FinderException e) {
            //
        }
        return null;
    }

    protected boolean toBeInvoiced(ContractType contractType) {
        return contractType.getTobeInvoiced();
    }

    protected boolean toBeInvoiced(Integer contractTypeId) {
        ContractTypeHome contractTypeHome = (ContractTypeHome)
                EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_CONTRACTTYPE);

        try {
            ContractType contractType = contractTypeHome.findByPrimaryKey(contractTypeId);
            return contractType.getTobeInvoiced();
        } catch (FinderException e) {
            //
        }
        return false;
    }
}
