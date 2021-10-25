package com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.domain.financemanager.Invoice;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * Util to process contract payment methods
 *
 * @author Miky
 * @version $Id: ContractPaymentMethodUtil.java 24-nov-2008 14:54:12 $
 */
public class ProcessContractPayMethodUtil {
    private static Log log = LogFactory.getLog(ProcessContractPayMethodUtil.class);

    private ProductContract contract;
    private Integer filterDate;
    private DateTimeZone dateTimeZone;
    private ArrayList<PaymentStep> paymentStepList;

    public ProcessContractPayMethodUtil(ProductContract contract, DateTimeZone dateTimeZone, Integer filterDate) throws InvalidContractException {

        this.contract = contract;
        this.filterDate = filterDate;
        if (dateTimeZone != null) {
            this.dateTimeZone = dateTimeZone;
        } else {
            this.dateTimeZone = DateTimeZone.getDefault();
        }
        this.paymentStepList = new ArrayList<PaymentStep>();
        if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
            setValidPartialFixedStepsToInvoice();
        }

        validContract(contract, filterDate);
    }

    public ContractInvoiceData processContract() throws InvalidContractException, PeriodicPaymentException {
        ContractInvoiceData contractData = null;

        if (validContract(contract, filterDate)) {
            if (SalesConstants.PayMethod.Single.getConstant() == contract.getPayMethod()) {
                contractData = processSingleMethod();
            } else if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {
                contractData = processPeriodicMethod();
            } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
                contractData = processPartialPeriodicMethod();
            } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
                contractData = processPartialFixedMethod();
            }
        }

        return contractData;
    }

    private ContractInvoiceData processSingleMethod() throws InvalidContractException {
        ContractInvoiceData contractData = new ContractInvoiceData();

        BigDecimal price = contract.getOpenAmount();
        BigDecimal quantity;

        SalePosition salePosition = getSalePosition(contract.getSalePositionId());
        if (salePosition != null) {
            validateSingleContractPrice(price, salePosition);
            quantity = salePosition.getQuantity();
            setCommonPriceValues(BigDecimalUtils.divide(price, quantity), contractData, quantity, price, true);
        } else {
            quantity = new BigDecimal(1);
            setCommonPriceValues(price, contractData, quantity, price, true);
        }

        return contractData;
    }

    private ContractInvoiceData processPeriodicMethod() throws PeriodicPaymentException {
        ContractInvoiceData contractData = new ContractInvoiceData();

        validatePeriodicContract();

        boolean withMatchCalendar = (contract.getMatchCalendarPeriod() != null && SalesConstants.MatchCalendarPeriod.YES.getConstant() == contract.getMatchCalendarPeriod());
        int invoicedMonths = PayMethodUtil.calculatePeriodicMonthsInvoiced(contract.getPayStartDate(), contract.getInvoicedUntil());

        int totalMonths;
        if (PayMethodUtil.isMatchCalendarWithoutMultipleOfPricePeriod(contract.getPayStartDate(), contract.getInvoicedUntil(), contract.getContractEndDate(), contract.getPayPeriod(), withMatchCalendar, contract.getPricePeriod())) {
            totalMonths = PayMethodUtil.calculateInvoiceTotalRemainMonthsToMatchPeriod(contract.getPayStartDate(), contract.getInvoicedUntil(), contract.getContractEndDate(), contract.getPayPeriod());
        } else {
            totalMonths = PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(contract.getPayStartDate(), contract.getInvoicedUntil(), contract.getContractEndDate(), contract.getPayPeriod(), withMatchCalendar, filterDate, contract.getNextInvoiceDate(), contract.getInvoiceDelay());
        }

        log.debug("Total months in periodic from start date until filter date:" + totalMonths);
        int monthsToInvoice = totalMonths - invoicedMonths;

        //if months to invoice is not multiple but is greater of price period
        if (monthsToInvoice > contract.getPricePeriod() && !PayMethodUtil.isMultiplePayPeriodOfPricePeriod(monthsToInvoice, contract.getPricePeriod())) {
            int monthsToMatchMultiple = monthsToInvoice % contract.getPricePeriod();
            monthsToInvoice = monthsToInvoice - monthsToMatchMultiple;
        }

        DateTime startDateTime = DateUtils.integerToDateTime(contract.getPayStartDate());
        DateTime newInvoicedUntilDateTime = fixInvoicedUntilMinus(addMonths(startDateTime, (invoicedMonths + monthsToInvoice)));
        Integer newInvoicedUntil = DateUtils.dateToInteger(newInvoicedUntilDateTime);
        DateTime invoicedFromDate = PayMethodUtil.getInvoiceStartDateTime(contract.getPayStartDate(), contract.getInvoicedUntil());

        //calculate quantity for periodic
        BigDecimal quantityPrice[] = PayMethodUtil.getPeriodicQuantityPriceForInvoice(monthsToInvoice, contract.getPricePeriod(), contract.getPrice(), contract.getPricePerMonth());
        BigDecimal quantity = quantityPrice[0];
        BigDecimal price = quantityPrice[1];

        BigDecimal invoicePrice = BigDecimalUtils.multiply(price, quantity);

        setCommonPriceValues(price, contractData, quantity, invoicePrice);
        contractData.setInvoicedFrom(DateUtils.dateToInteger(invoicedFromDate));
        contractData.setPeriodicMonths(quantity.intValue());
        contractData.setInvoicedUntil(newInvoicedUntil);
        contractData.setPricePerMonth(contract.getPricePerMonth());

        return contractData;
    }

    public boolean isPeriodicNotInvoiced(ProductContract contractUpdated) {
        boolean isNotInvoiced = false;
        if (SalesConstants.PayMethod.Periodic.getConstant() == contractUpdated.getPayMethod()) {
            try {
                isNotInvoiced = validContract(contractUpdated, filterDate);
            } catch (InvalidContractException e) {
                //can't be invoice the periodic contract
                isNotInvoiced = false;
            }
        }
        return isNotInvoiced;
    }

    public ContractInvoiceData processNextPeriodic(ProductContract contractUpdated) throws PeriodicPaymentException {
        log.debug("Next periodic with invoiceduntil:" + contractUpdated.getInvoicedUntil());
        if (isPeriodicNotInvoiced(contractUpdated)) {
            this.contract = contractUpdated;
            return processPeriodicMethod();
        }
        return null;
    }

    private ContractInvoiceData processPartialPeriodicMethod() throws PeriodicPaymentException {
        ContractInvoiceData contractData = new ContractInvoiceData();

        validatePartialPeriodicContract();

        DateTime invoicedFromDate = PayMethodUtil.getInvoiceStartDateTime(contract.getPayStartDate(), contract.getInvoicedUntil());
        contractData.setInvoicedFrom(DateUtils.dateToInteger(invoicedFromDate));

        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Collection invoicePositions = null;
        try {
            invoicePositions = invoicePositionHome.findByContractId(contract.getContractId(), contract.getCompanyId());
        } catch (FinderException e) {
            log.debug("Not found invoice position for this contract...", e);
            invoicePositions = new ArrayList();
        }

        BigDecimal paidInstallment = new BigDecimal(0);
        BigDecimal amountPaid = new BigDecimal(0);
        for (Iterator iterator = invoicePositions.iterator(); iterator.hasNext();) {
            InvoicePosition invoicePosition = (InvoicePosition) iterator.next();

            if (FinanceConstants.InvoiceType.Invoice.equal(invoicePosition.getInvoice().getType())) {
                paidInstallment = paidInstallment.add(BigDecimal.ONE);
            }
            amountPaid = amountPaid.add(fixInvoicePositionTotalPriceByType(invoicePosition));
        }

        BigDecimal installmentNotPaid = (BigDecimal.valueOf(contract.getInstallment()).subtract(paidInstallment));

        BigDecimal invoicePrice;
        boolean isLastInvoice = false;
        if (installmentNotPaid.intValue() > 1) {
            invoicePrice = BigDecimalUtils.divide(contract.getPrice().subtract(amountPaid), installmentNotPaid);
        } else {
            invoicePrice = contract.getOpenAmount();
            isLastInvoice = true;
        }

        Integer newInvoicedUntil = PayMethodUtil.applyPayPeriodType(contract.getPayStartDate(), contract.getInvoicedUntil(), contract.getPayPeriod());

        setCommonPriceValues(invoicePrice, contractData, new BigDecimal(1), invoicePrice, isLastInvoice);
        contractData.setInvoicedUntil(newInvoicedUntil);
        contractData.setPeriodicMonths(contract.getPayPeriod());

        return contractData;
    }

    public boolean isPartialPeriodicNotInvoiced(ProductContract contractUpdated) {
        if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contractUpdated.getPayMethod()) {
            if (contractUpdated.getOpenAmount().compareTo(BigDecimal.ZERO) == 1
                    && contractUpdated.getPayStartDate() <= filterDate
                    && (contractUpdated.getInvoicedUntil() == null || contractUpdated.getInvoicedUntil() < filterDate)) {
                return true;
            }
        }
        return false;
    }

    public ContractInvoiceData processNextPartialPeriodic(ProductContract contractUpdated) throws PeriodicPaymentException {
        log.debug("Next partial periodic with open amount:" + contractUpdated.getOpenAmount());
        if (isPartialPeriodicNotInvoiced(contractUpdated)) {
            this.contract = contractUpdated;
            return processPartialPeriodicMethod();
        }
        return null;
    }

    private ContractInvoiceData processPartialFixedMethod() throws InvalidContractException {
        ContractInvoiceData contractData = null;
        if (isPartialFixedNotInvoiced()) {
            PaymentStep paymentStep = paymentStepList.get(0);
            contractData = processPartialFixedMethod(paymentStep);
            //remove step invoiced
            paymentStepList.remove(0);
        } else {
            throw new InvalidContractException("Contract has not steps to invoice. Partial Fixed method is invalid...");
        }

        return contractData;
    }

    private ContractInvoiceData processPartialFixedMethod(PaymentStep paymentStep) throws InvalidContractException {
        ContractInvoiceData contractData = new ContractInvoiceData();
        boolean isLastInvoice = false;

        BigDecimal price;
        if (SalesConstants.AmounType.AMOUNT.getConstant() == contract.getAmounType()) {
            price = paymentStep.getPayAmount();
        } else if (SalesConstants.AmounType.PERCENTAGE.getConstant() == contract.getAmounType()) {
            price = BigDecimalUtils.getPercentage(contract.getPrice(), paymentStep.getPayAmount());
            isLastInvoice = isLastInvoiceForPartialFixedPercent(contract);
        } else {
            throw new InvalidContractException("Partial Fixed method is invalid...");
        }

        setCommonPriceValues(price, contractData, new BigDecimal(1), price, isLastInvoice);
        contractData.setPaymentStepInvoiced(paymentStep);

        return contractData;
    }

    public boolean isPartialFixedNotInvoiced() {
        //log.debug("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF:" + paymentStepList);
        return (!paymentStepList.isEmpty());
    }

    public ContractInvoiceData processNextPartialFixed(ProductContract contractUpdated) throws InvalidContractException {
        log.debug("Next partial Fixed with open amount:" + contractUpdated.getOpenAmount());
        if (isPartialFixedNotInvoiced()) {
            this.contract = contractUpdated;
            return processPartialFixedMethod();
        }
        return null;
    }

    private void setValidPartialFixedStepsToInvoice() {
        PaymentStepHome paymentStepHome = (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);
        try {
            Collection paymentSteps = paymentStepHome.findByContractIdWithFilterDate(contract.getContractId(), filterDate);
            for (Iterator iterator = paymentSteps.iterator(); iterator.hasNext();) {
                PaymentStep paymentStep = (PaymentStep) iterator.next();
                if (!paymentStepHasInvoiced(paymentStep)) {
                    paymentStepList.add(paymentStep);
                }
            }
        } catch (FinderException e) {
            log.debug("Error in find paymentSteps to contract..." + contract.getContractId() + " with filter date " + filterDate);
        }
    }

    private boolean validContract(ProductContract contract, Integer filterDate) throws InvalidContractException {
        boolean isValid = false;

        if (SalesConstants.PayMethod.Single.getConstant() == contract.getPayMethod()) {
            if (contract.getOpenAmount().compareTo(BigDecimal.ZERO) == 1) {
                isValid = true;
            }
        } else if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {

            if (isValidPeriodicContractToInvoice(contract.getNextInvoiceDate(), contract.getInvoicedUntil(), contract.getContractEndDate(), filterDate)) {
                isValid = true;
            }
        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
            if (contract.getOpenAmount().compareTo(BigDecimal.ZERO) == 1
                    && contract.getPayStartDate() <= filterDate
                    && (contract.getInvoicedUntil() == null || contract.getInvoicedUntil() < filterDate)) {
                isValid = true;
            }
        } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == contract.getPayMethod()) {
            isValid = true;
        }

        if (!isValid) {
            throw new InvalidContractException("Contract:" + contract.getContractId() + " Invalid with pay method....." + contract.getPayMethod());
        }
        return isValid;
    }

    public static boolean isValidPeriodicContractToInvoice(Integer nextInvoiceDate, Integer invoicedUntil, Integer contractEndDate, Integer filterDate) {
        boolean isValid = false;

        if ((nextInvoiceDate < filterDate || (invoicedUntil == null && nextInvoiceDate <= filterDate))
                && (contractEndDate == null || invoicedUntil == null || contractEndDate > invoicedUntil)) {
            isValid = true;
        }
        return isValid;
    }

    private boolean validateSingleContractPrice(BigDecimal invoicePrice, SalePosition salePosition) throws InvalidContractException {
        boolean isValid = true;

        if (SalesConstants.PayMethod.Single.getConstant() == contract.getPayMethod()) {
            if (salePosition != null) {
                if (!PayMethodUtil.isValidSingleOpenAmount(invoicePrice, salePosition.getQuantity())) {
                    isValid = false;
                    throw new InvalidContractException("Single Contract open amount is invalid. contract:" + contract.getContractId());
                }
            }
        }
        return isValid;
    }

    private boolean validatePeriodicContract() throws PeriodicPaymentException {
        log.debug("Validate periodic contrat with invoicedUntil " + contract.getInvoicedUntil());

        List<String> errorMessageKeys = new ArrayList<String>();
        if (SalesConstants.PayMethod.Periodic.getConstant() == contract.getPayMethod()) {

            if (contract.getPayStartDate() == null) {
                errorMessageKeys.add("ProductContract.error.payStartDateEmpty");
            }
            if (contract.getInvoicedUntil() != null && !PayMethodUtil.isValidPeriodicEndDate(contract.getPayStartDate(), contract.getInvoicedUntil())) {
                errorMessageKeys.add("ProductContract.error.invoicedUntilInvalid");
            }
            if (contract.getContractEndDate() != null && !PayMethodUtil.isValidPeriodicEndDate(contract.getPayStartDate(), contract.getContractEndDate())) {
                errorMessageKeys.add("ProductContract.error.productContractEndDateInvalid");
            }
        }

        if (!errorMessageKeys.isEmpty()) {
            PeriodicPaymentException periodicPaymentException = new PeriodicPaymentException("Periodic contract  is not valid..." + errorMessageKeys);
            periodicPaymentException.setErrorMessageKeys(errorMessageKeys);
            throw periodicPaymentException;
        }
        return errorMessageKeys.isEmpty();
    }

    private boolean validatePartialPeriodicContract() throws PeriodicPaymentException {
        List<String> errorMessageKeys = new ArrayList<String>();
        if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == contract.getPayMethod()) {
            if (contract.getPayStartDate() == null) {
                errorMessageKeys.add("ProductContract.error.payStartDateEmpty");
            }
            if (contract.getInvoicedUntil() != null && !PayMethodUtil.isValidPeriodicEndDate(contract.getPayStartDate(), contract.getInvoicedUntil())) {
                errorMessageKeys.add("ProductContract.error.invoicedUntilInvalid");
            }
        }
        if (!errorMessageKeys.isEmpty()) {
            PeriodicPaymentException periodicPaymentException = new PeriodicPaymentException("Partial Periodic contract is not valid..." + errorMessageKeys);
            periodicPaymentException.setErrorMessageKeys(errorMessageKeys);
            throw periodicPaymentException;
        }

        return errorMessageKeys.isEmpty();
    }

    private DateTime addMonths(DateTime dateTime, int months) {
        return dateTime.plusMonths(months);
    }


    private DateTime fixInvoicedUntilPlus(DateTime dateTime) {
        return dateTime.plusDays(1);
    }

    private DateTime fixInvoicedUntilMinus(DateTime dateTime) {
        return dateTime.minusDays(1);
    }

    private DateTime fixContractEndDate(DateTime dateTime) {
        return dateTime.plusDays(1);
    }

    private SalePosition getSalePosition(Integer salePositionId) {
        SalePosition salePosition = null;
        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        if (salePositionId != null) {
            try {
                salePosition = salePositionHome.findByPrimaryKey(salePositionId);
            } catch (FinderException e) {
                log.debug("Not found sale position..." + salePositionId, e);
            }
        }
        return salePosition;
    }

    private boolean paymentStepHasInvoiced(PaymentStep paymentStep) {
        return getPaymentStepInvoicePosition(paymentStep) != null;
    }

    private static InvoicePosition getPaymentStepInvoicePosition(PaymentStep paymentStep) {
        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        InvoicePosition invoicePosition = null;
        try {
            invoicePosition = invoicePositionHome.findByPayStepId(paymentStep.getPayStepId(), paymentStep.getCompanyId());
        } catch (FinderException e) {
            log.debug("Not found invoice position related to paymentStep:" + paymentStep.getPayStepId());
        }
        return invoicePosition;
    }

    private BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        BigDecimal discount = BigDecimalUtils.getPercentage(amount, discountPercent);
        return BigDecimalUtils.subtract(amount, discount);
    }

    private BigDecimal calculateUnitPrice(BigDecimal fixedPrice, ProductContract contract) {
        if (contract.getDiscount() != null) {
            fixedPrice = applyDiscount(fixedPrice, contract.getDiscount());
        }
        return fixedPrice;
    }

    /**
     * Fix invoice position total price, if this is Credit Note multiply by (-1) to return as negative value the total price.
     * otherwise return as positive value
     *
     * @param invoicePosition invoice position (Invoice or credit note)
     * @return BigDecimal
     */
    private static BigDecimal fixInvoicePositionTotalPriceByType(InvoicePosition invoicePosition) {
        BigDecimal totalPriceFixed = fixInvoicePositionTotalPrice(invoicePosition);

        if (FinanceConstants.InvoiceType.CreditNote.equal(invoicePosition.getInvoice().getType())) {
            totalPriceFixed = BigDecimalUtils.multiply(totalPriceFixed, new BigDecimal(-1));
        }
        return totalPriceFixed;
    }

    /**
     * Fix invoice position total price when this have discount
     *
     * @param invoicePosition invoice position
     * @return BigDecimal
     */
    public static BigDecimal fixInvoicePositionTotalPrice(InvoicePosition invoicePosition) {
        BigDecimal totalPriceFixed;
        Invoice invoice = invoicePosition.getInvoice();
        BigDecimal positionTotalPrice;
        if (invoice.getNetGross() != null && FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            positionTotalPrice = invoicePosition.getTotalPriceGross();
        } else {
            //this is net prices
            positionTotalPrice = invoicePosition.getTotalPrice();
        }

        if (invoicePosition.getDiscountValue() != null) {
            totalPriceFixed = positionTotalPrice.add(invoicePosition.getDiscountValue());
        } else {
            totalPriceFixed = positionTotalPrice;
        }
        return totalPriceFixed;
    }

    /**
     * Verifiy if contract partial periodic as percent has only last invoice
     *
     * @param productContract contract
     * @return true or false
     */
    private boolean isLastInvoiceForPartialFixedPercent(ProductContract productContract) {
        boolean isLastInvoice = false;

        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        if (SalesConstants.PayMethod.PartialFixed.getConstant() == productContract.getPayMethod() &&
                SalesConstants.AmounType.PERCENTAGE.getConstant() == productContract.getAmounType()) {

            Collection paySteps = productContract.getPaymentSteps();
            BigDecimal sumPercent = new BigDecimal(0);

            int paidInstallment = 0;
            for (Object object : paySteps) {
                PaymentStep paymentStep = (PaymentStep) object;

                if (paymentStep.getPayAmount() != null) {
                    sumPercent = sumPercent.add(paymentStep.getPayAmount());

                    Collection invoicePositions = null;
                    try {
                        invoicePositions = invoicePositionHome.findByPaymentStepAndContract(paymentStep.getContractId(), paymentStep.getPayStepId(), paymentStep.getCompanyId());
                    } catch (FinderException e) {
                        log.debug("Not found invoice position for this contract with pay steps...", e);
                        invoicePositions = new ArrayList();
                    }

                    for (Iterator iterator = invoicePositions.iterator(); iterator.hasNext();) {
                        InvoicePosition invoicePosition = (InvoicePosition) iterator.next();
                        if (FinanceConstants.InvoiceType.Invoice.equal(invoicePosition.getInvoice().getType())) {
                            paidInstallment++;
                            break;
                        }
                    }
                }
            }

            int installmentNotPaid = contract.getInstallment() - paidInstallment;
            if (installmentNotPaid == 1 && sumPercent.doubleValue() == 100) {
                isLastInvoice = true;
            }
        }

        return isLastInvoice;
    }


    public static BigDecimal calculatePartialFixedOpenAmount(ProductContract productContract) {
        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Collection paySteps = productContract.getPaymentSteps();
        BigDecimal amountPaid = new BigDecimal(0);

        for (Object object : paySteps) {
            PaymentStep paymentStep = (PaymentStep) object;
            Collection invoicePositions = null;
            try {
                invoicePositions = invoicePositionHome.findByPaymentStepAndContract(paymentStep.getContractId(), paymentStep.getPayStepId(), paymentStep.getCompanyId());
            } catch (FinderException e) {
                log.debug("Not found invoice position for this contract with pay steps...", e);
                invoicePositions = new ArrayList();
            }

            for (Iterator iterator = invoicePositions.iterator(); iterator.hasNext();) {
                InvoicePosition invoicePosition = (InvoicePosition) iterator.next();
                amountPaid = amountPaid.add(fixInvoicePositionTotalPriceByType(invoicePosition));
            }
        }
        return BigDecimalUtils.subtract(productContract.getPrice(), amountPaid);
    }

    public static BigDecimal calculatePartialPeriodicOpenAmount(ProductContract productContract) {

        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Collection invoicePositions = null;
        try {
            invoicePositions = invoicePositionHome.findByContractId(productContract.getContractId(), productContract.getCompanyId());
        } catch (FinderException e) {
            log.debug("Not found invoice position for this contract...", e);
            invoicePositions = new ArrayList();
        }

        BigDecimal amountPaid = new BigDecimal(0);
        for (Iterator iterator = invoicePositions.iterator(); iterator.hasNext();) {
            InvoicePosition invoicePosition = (InvoicePosition) iterator.next();
            amountPaid = amountPaid.add(fixInvoicePositionTotalPriceByType(invoicePosition));
        }

        return BigDecimalUtils.subtract(productContract.getPrice(), amountPaid);
    }

    public static BigDecimal calculateSingleOpenAmount(ProductContract productContract) {

        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Collection invoicePositions = null;
        try {
            invoicePositions = invoicePositionHome.findByContractId(productContract.getContractId(), productContract.getCompanyId());
        } catch (FinderException e) {
            log.debug("Not found invoice position for this contract...", e);
            invoicePositions = new ArrayList();
        }

        BigDecimal amountPaid = new BigDecimal(0);
        for (Iterator iterator = invoicePositions.iterator(); iterator.hasNext();) {
            InvoicePosition invoicePosition = (InvoicePosition) iterator.next();
            amountPaid = amountPaid.add(fixInvoicePositionTotalPriceByType(invoicePosition));
        }

        return BigDecimalUtils.subtract(productContract.getPrice(), amountPaid);
    }

    private void setCommonPriceValues(BigDecimal grossUnitPrice, ContractInvoiceData invoiceData, BigDecimal quantity, BigDecimal invoicePrice) {
        setCommonPriceValues(grossUnitPrice, invoiceData, quantity, invoicePrice, false);
    }

    private void setCommonPriceValues(BigDecimal grossUnitPrice, ContractInvoiceData invoiceData, BigDecimal quantity, BigDecimal invoicePrice, boolean isLastInvoiceForContract) {
        BigDecimal unitPrice = calculateUnitPrice(grossUnitPrice, contract);

        BigDecimal discountValue = null;
        if (contract.getDiscount() != null) {
            BigDecimal unitPriceDiscounted = BigDecimalUtils.getPercentage(grossUnitPrice, contract.getDiscount());
            discountValue = BigDecimalUtils.multiply(unitPriceDiscounted, quantity);
            invoiceData.setUnitPriceDiscounted(unitPriceDiscounted);
        }

        //stimate the totals and fix the reamin if is required 
        BigDecimal positionTotal = BigDecimalUtils.multiply(unitPrice, quantity);
        if (discountValue != null) {
            positionTotal = BigDecimalUtils.sum(positionTotal, discountValue);
        }

        BigDecimal totalToBeInvoiced = invoicePrice;
        if (isLastInvoiceForContract) {
            totalToBeInvoiced = readCurrentContractOpenAmount(contract);
        }

        if (totalToBeInvoiced.doubleValue() != positionTotal.doubleValue()) {
            //here should be fixed to that both be equals
            log.debug("Price to Invoce not equal to position price, this required round reamin.....  totalToBeInvoiced:" + totalToBeInvoiced + " positionTotal:" + positionTotal);

            BigDecimal remain = BigDecimalUtils.subtract(totalToBeInvoiced, positionTotal);

            if (discountValue != null) {
                //add remain to discount value
                discountValue = BigDecimalUtils.sum(discountValue, remain);
            } else if (quantity.doubleValue() == 1) {
                //add remain to unitPrice, only if quantity = 1 (unitPrice * 1 = unitPrice), this generally to partial fixed as percent
                unitPrice = BigDecimalUtils.sum(unitPrice, remain);
            }
        }

        invoiceData.setContractPrice(grossUnitPrice);
        invoiceData.setUnitPrice(unitPrice);
        invoiceData.setQuantity(quantity);
        invoiceData.setInvoicePrice(invoicePrice);
        invoiceData.setDiscountValue(discountValue);
    }

    private BigDecimal readCurrentContractOpenAmount(ProductContract productContract) {
        BigDecimal currentOpenAmount = null;
        if (SalesConstants.PayMethod.Single.getConstant() == productContract.getPayMethod()) {
            currentOpenAmount = calculateSingleOpenAmount(productContract);
        } else if (SalesConstants.PayMethod.Periodic.getConstant() == productContract.getPayMethod()) {
            currentOpenAmount = null;
        } else if (SalesConstants.PayMethod.PartialPeriodic.getConstant() == productContract.getPayMethod()) {
            currentOpenAmount = calculatePartialPeriodicOpenAmount(productContract);
        } else if (SalesConstants.PayMethod.PartialFixed.getConstant() == productContract.getPayMethod()) {
            currentOpenAmount = calculatePartialFixedOpenAmount(productContract);
        }

        return currentOpenAmount;
    }

    public static Integer calculatePeriodicNextInvoiceDate(Integer invoiceDelay, Integer payStartDate, Integer invoicedUntil) {

        Integer nextInvoiceDate = payStartDate;
        if (invoicedUntil != null) {
            nextInvoiceDate = invoicedUntil;
        }

        if (invoiceDelay != null && invoiceDelay != 0) {
            DateTime dateTime = DateUtils.integerToDateTime(nextInvoiceDate);

            DateTime nextInvoiceDateTime = dateTime.plusDays(invoiceDelay);
            nextInvoiceDate = DateUtils.dateToInteger(nextInvoiceDateTime);

            log.debug("Apply periodic delay " + invoiceDelay + ",  nextInvoiceDate " + nextInvoiceDate);
        }
        return nextInvoiceDate;
    }
}
