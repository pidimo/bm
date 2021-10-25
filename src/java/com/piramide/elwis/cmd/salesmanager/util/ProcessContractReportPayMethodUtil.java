package com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Jatun S.R.L.
 * process contract in range of date (fromdate, todate) to calculate th total that
 * will be invoiced
 *
 * @author Miky
 * @version $Id: ProcessContractReportPayMethodUtil.java 17-dic-2008 15:30:54 $
 */
public class ProcessContractReportPayMethodUtil {
    private Log log = LogFactory.getLog(this.getClass());

    private BigDecimal price;
    private BigDecimal discount;

    private BigDecimal totalGross;
    private BigDecimal totalDiscount;
    private BigDecimal totalNet;

    private Integer fromDate;
    private Integer toDate;

    private DateTimeZone dateTimeZone;

    public ProcessContractReportPayMethodUtil(BigDecimal price, BigDecimal discount, Integer fromDate, Integer toDate) {

        this.price = price;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.fromDate = fromDate;
        this.toDate = toDate;

        totalGross = BigDecimal.ZERO;
        totalNet = BigDecimal.ZERO;
        totalDiscount = BigDecimal.ZERO;

        this.dateTimeZone = DateTimeZone.getDefault();
    }


    public void processSingleMethod() {
        calculateTotals(price);
    }

    public void processPeriodicMethod(Integer payStartDate, Integer contractEndDate, Integer invoicedUntilDate, Integer payPeriod, Integer matchCalendarPeriod, Integer totalIncomeType, Integer pricePeriod, BigDecimal pricePerMonth, Integer nextInvoiceDate, Integer invoiceDelay) {

        if (!isValidPeriodicContract(payStartDate, contractEndDate, invoicedUntilDate)) {
            return; //this is invalid
        }

        boolean withMatchCalendar = matchCalendarPeriod != null && SalesConstants.MatchCalendarPeriod.YES.getConstant() == matchCalendarPeriod;
        int totalMonths;
        if (SalesConstants.ContractIncome.REAL_INCOME.equal(totalIncomeType)) {
            totalMonths = calculateMonthsForPeriodicRealIncome(payStartDate, invoicedUntilDate, contractEndDate, payPeriod, withMatchCalendar, nextInvoiceDate, invoiceDelay);
        } else {
            totalMonths = calculateMonthsForPeriodicVirtualIncome(payStartDate, invoicedUntilDate, contractEndDate);
        }
        log.debug("Total months in periodic until filter date:" + totalMonths);

        BigDecimal invoicePrice;
        if (totalMonths > pricePeriod && !PayMethodUtil.isMultiplePayPeriodOfPricePeriod(totalMonths, pricePeriod)) {
            int monthsToMatchMultiple = totalMonths % pricePeriod;
            invoicePrice = calculatePeriodicInvoicePrice(monthsToMatchMultiple, pricePeriod, price, pricePerMonth);
            invoicePrice = BigDecimalUtils.sum(invoicePrice, calculatePeriodicInvoicePrice((totalMonths - monthsToMatchMultiple), pricePeriod, price, pricePerMonth));

        } else {
            invoicePrice = calculatePeriodicInvoicePrice(totalMonths, pricePeriod, price, pricePerMonth);
        }

        calculateTotals(invoicePrice);
    }

    private BigDecimal calculatePeriodicInvoicePrice(Integer monthsToInvoice, Integer pricePeriod, BigDecimal price, BigDecimal pricePerMonth) {
        //calculate quantity for periodic
        BigDecimal quantityPrice[] = PayMethodUtil.getPeriodicQuantityPriceForInvoice(monthsToInvoice, pricePeriod, price, pricePerMonth);
        BigDecimal quantity = quantityPrice[0];
        BigDecimal periodicPrice = quantityPrice[1];

        BigDecimal invoicePrice = BigDecimalUtils.multiply(periodicPrice, quantity);
        return invoicePrice;
    }

    private boolean isValidPeriodicContract(Integer payStartDate, Integer contractEndDate, Integer invoicedUntilDate) {
        boolean isValid = true;
        if (payStartDate == null) {
            isValid = false;
        }
        if (invoicedUntilDate != null && !PayMethodUtil.isValidPeriodicEndDate(payStartDate, invoicedUntilDate)) {
            isValid = false;
        }
        if (contractEndDate != null && !PayMethodUtil.isValidPeriodicEndDate(payStartDate, contractEndDate)) {
            isValid = false;
        }
        return isValid;
    }

    private int calculateMonthsForPeriodicRealIncome(Integer payStartDate, Integer invoicedUntilDate, Integer contractEndDate, Integer payPeriod, boolean withMatchCalendar, Integer nextInvoiceDate, Integer invoiceDelay) {
        Integer invoiceUntilInReportRange = getInvoiceUntilInReportRange(payStartDate, invoicedUntilDate, contractEndDate, payPeriod, withMatchCalendar, nextInvoiceDate, invoiceDelay);

        Integer newNextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(invoiceDelay, payStartDate, invoiceUntilInReportRange);

        if (contractEndDate != null) {
            if (invoiceUntilInReportRange != null && invoiceUntilInReportRange >= contractEndDate) {
                return 0;
            }
        }

        log.debug("DATE CALCULATE START:" + invoiceUntilInReportRange);

        int invoicedMonths = PayMethodUtil.calculatePeriodicMonthsInvoiced(payStartDate, invoiceUntilInReportRange);

        int totalMonths = PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(payStartDate, invoiceUntilInReportRange, contractEndDate, payPeriod, withMatchCalendar, toDate, newNextInvoiceDate, invoiceDelay);
        int monthsToInvoice = totalMonths - invoicedMonths;
        log.debug("calculateMonthsForPeriodicRealIncome...." + monthsToInvoice);
        return monthsToInvoice;
    }

    private Integer getInvoiceUntilInReportRange(Integer payStartDate, Integer invoicedUntilDate, Integer contractEndDate, Integer payPeriod, boolean withMatchCalendar, Integer nextInvoiceDate, Integer invoiceDelay) {
        Integer invoiceUntilInToRange;
        DateTime invoicedFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoicedUntilDate);
        if (DateUtils.dateToInteger(invoicedFromDate) < fromDate) {
            Integer fromDateFixed = DateUtils.dateToInteger(DateUtils.integerToDateTime(fromDate).minusDays(1)); //make as valid periodic end date

            invoiceUntilInToRange = PayMethodUtil.calculatePeriodicNextInvoicedUntil(payStartDate, invoicedUntilDate, contractEndDate, payPeriod, withMatchCalendar, fromDateFixed, nextInvoiceDate, invoiceDelay);
        } else {
            invoiceUntilInToRange = invoicedUntilDate;
        }

        return invoiceUntilInToRange;
    }


    private int calculateMonthsForPeriodicVirtualIncome(Integer payStartDate, Integer invoicedUntilDate, Integer contractEndDate) {
        DateTime fromDateTime = DateUtils.integerToDateTime(fromDate);
        DateTime fixedToDateTime = DateUtils.integerToDateTime(toDate).plusDays(1); //this is last day of month, add 1 day to manage exact period

        DateTime invoicedFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoicedUntilDate);
        if (DateUtils.dateToInteger(invoicedFromDate) > fromDate) {
            fromDateTime = invoicedFromDate;
        }

        if (contractEndDate != null) {
            if (contractEndDate <= fromDate) {
                return 0;
            }
            if (contractEndDate < toDate) {
                fixedToDateTime = fixContractEndDate(DateUtils.integerToDateTime(contractEndDate));
            }
        }

        int months = PayMethodUtil.calculateMonthsInRange(fromDateTime, fixedToDateTime);
        log.debug("calculateMonthsForPeriodicVirtualIncome..." + months);
        return months;
    }

    public void processPartialPeriodicMethod(Integer payStartDate, Integer invoicedUntilDate, Integer payPeriod, Integer installment, Integer totalIncomeType) {
        int alreadyPaidInstallment = calculateAlreadyInstallmentPaid(payStartDate, invoicedUntilDate, payPeriod);

        if (payStartDate == null) {
            return; //this is invalid
        }
        if (invoicedUntilDate != null && !PayMethodUtil.isValidPeriodicEndDate(payStartDate, invoicedUntilDate)) {
            return; //this is invalid
        }
        if (alreadyPaidInstallment >= installment) {
            return; //this has paid of the all
        }

        BigDecimal invoicePrice;
        if (SalesConstants.ContractIncome.REAL_INCOME.equal(totalIncomeType)) {
            invoicePrice = calculateRealIncomeForPartialPeriodic(payStartDate,invoicedUntilDate, payPeriod, installment, alreadyPaidInstallment);
        } else {
            invoicePrice = calculateVirtualIncomeForPartialPeriodic(payStartDate,invoicedUntilDate, payPeriod, installment, alreadyPaidInstallment);
        }

        calculateTotals(invoicePrice);
    }

    private BigDecimal calculateRealIncomeForPartialPeriodic(Integer payStartDate, Integer invoicedUntilDate, Integer payPeriod, Integer installment, int alreadyPaidInstallemt) {
        int remainderInstallment = installment - alreadyPaidInstallemt;
        BigDecimal incomePrice = BigDecimal.ZERO;
        int installmentOutRange = 0;

        Integer invoiceUntilInReportRange = invoicedUntilDate;
        DateTime invoiceFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoicedUntilDate);

        if (DateUtils.dateToInteger(invoiceFromDate) < fromDate) {
            installmentOutRange = getInstallmentPaidInRangeDate(payStartDate, invoicedUntilDate, payPeriod, integerDateMinusDay(fromDate), remainderInstallment);
            invoiceUntilInReportRange = PayMethodUtil.applyPayPeriodType(payStartDate, invoicedUntilDate, payPeriod, installmentOutRange);
        }

        int installmentToPaid = getInstallmentPaidInRangeDate(payStartDate, invoiceUntilInReportRange, payPeriod, toDate, (remainderInstallment - installmentOutRange));
        if (installmentToPaid > 0) {
            if (installment == installmentToPaid) {
                incomePrice = price;
            } else {
                incomePrice = BigDecimalUtils.multiply(BigDecimalUtils.divide(price, new BigDecimal(installment)), new BigDecimal(installmentToPaid));
            }
        }
        return incomePrice;
    }

    private BigDecimal calculateVirtualIncomeForPartialPeriodic(Integer payStartDate, Integer invoicedUntilDate, Integer payPeriod, Integer installment, int alreadyPaidInstallemt) {
        int remainderInstallment = installment - alreadyPaidInstallemt;
        BigDecimal incomePrice = BigDecimal.ZERO;
        int installmentOutRange = 0;

        Integer invoiceUntilInReportRange = invoicedUntilDate;
        DateTime invoiceFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoicedUntilDate);

        if (DateUtils.dateToInteger(invoiceFromDate) < fromDate) {
            installmentOutRange = getInstallmentPaidInRangeDate(payStartDate, invoicedUntilDate, payPeriod, integerDateMinusDay(fromDate), remainderInstallment);
            invoiceUntilInReportRange = PayMethodUtil.applyPayPeriodType(payStartDate, invoicedUntilDate, payPeriod, installmentOutRange);
        }

        //calculate for start out range
        BigDecimal partialPrice = BigDecimalUtils.divide(price, new BigDecimal(installment));
        DateTime newInvoiceFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoiceUntilInReportRange);

        if (newInvoiceFromDate.isAfter(invoiceFromDate) && DateUtils.dateToInteger(newInvoiceFromDate) > fromDate) {
            Integer filterEndDate = invoiceUntilInReportRange;
            if (invoiceUntilInReportRange > toDate) {
                filterEndDate = toDate;
            }
            int months = PayMethodUtil.calculateMonthsUntilFilterDate(fromDate, filterEndDate);
            incomePrice = BigDecimalUtils.multiply(BigDecimalUtils.divide(partialPrice, BigDecimal.valueOf(payPeriod)), BigDecimal.valueOf(months));
        }

        //calculate in range
        if (installmentOutRange < remainderInstallment) {
            int installmentToPaid = getInstallmentPaidInRangeDate(payStartDate, invoiceUntilInReportRange, payPeriod, toDate, (remainderInstallment - installmentOutRange));
            if (installmentToPaid > 0) {
                Integer untilDate = PayMethodUtil.applyPayPeriodType(payStartDate, invoiceUntilInReportRange, payPeriod, installmentToPaid);
                while (untilDate > toDate && installmentToPaid > 0) {
                    installmentToPaid--;
                    untilDate = PayMethodUtil.applyPayPeriodType(payStartDate, invoiceUntilInReportRange, payPeriod, installmentToPaid);
                }

                if (installment == installmentToPaid) {
                    incomePrice = BigDecimalUtils.sum(incomePrice, price);
                } else {
                    BigDecimal rangePrice = BigDecimalUtils.multiply(BigDecimalUtils.divide(price, new BigDecimal(installment)), new BigDecimal(installmentToPaid));
                    incomePrice = BigDecimalUtils.sum(incomePrice, rangePrice);
                }

                //calculate to the end of range
                if ((installmentToPaid + installmentOutRange) < remainderInstallment && untilDate < toDate) {
                    Integer fixedStartDate = DateUtils.dateToInteger(fixInvoicedUntilPlus(DateUtils.integerToDateTime(untilDate)));
                    int months = PayMethodUtil.calculateMonthsUntilFilterDate(fixedStartDate, toDate);

                    BigDecimal endPrice = BigDecimalUtils.multiply(BigDecimalUtils.divide(partialPrice, BigDecimal.valueOf(payPeriod)), BigDecimal.valueOf(months));
                    incomePrice = BigDecimalUtils.sum(incomePrice, endPrice);
                }
            }
        }

        return incomePrice;
    }

    private int getInstallmentPaidInRangeDate(Integer payStartDate, Integer invoicedUntilDate, Integer payPeriod, Integer filterDate, Integer contractInstallment) {
        log.debug("GET INSTALLMENT TO PAID WITH: " + contractInstallment + "->" + payStartDate + "--" + invoicedUntilDate + "--" + filterDate);
        int installmentPaid = 0;

        //verify if input date is valid (range, installments)
        DateTime invoiceFromDate = PayMethodUtil.getInvoiceStartDateTime(payStartDate, invoicedUntilDate);
        if (DateUtils.dateToInteger(invoiceFromDate) > filterDate || contractInstallment <= 0) {
            return 0;
        }

        int periods = PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(payStartDate, invoicedUntilDate, filterDate, payPeriod);
        if (periods > 0) {
            if (periods > contractInstallment) {
                installmentPaid = contractInstallment;
            } else {
                installmentPaid = periods;
            }
        }

        Integer newInvoiceUntil = PayMethodUtil.applyPayPeriodType(payStartDate, invoicedUntilDate, payPeriod, installmentPaid);
        while (newInvoiceUntil < filterDate && installmentPaid < contractInstallment) {
            newInvoiceUntil = PayMethodUtil.applyPayPeriodType(payStartDate, newInvoiceUntil, payPeriod);
            installmentPaid++;
        }
        return installmentPaid;
    }

    private int calculateAlreadyInstallmentPaid(Integer payStartDate, Integer invoicedUntilDate, Integer payPeriod) {
        int invoicedMonths = PayMethodUtil.calculatePeriodicMonthsInvoiced(payStartDate, invoicedUntilDate);
        int alreadyPaidInstallment = invoicedMonths / payPeriod;
        return alreadyPaidInstallment;
    }

    public void processPartialFixedMethod(Integer amountType, BigDecimal payAmount) {

        if (SalesConstants.AmounType.AMOUNT.getConstant() == amountType) {
            calculateTotals(payAmount);
        } else if (SalesConstants.AmounType.PERCENTAGE.getConstant() == amountType) {
            BigDecimal invoicePrice = getPercentage(price, payAmount);
            calculateTotals(invoicePrice);
        }
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

    private Integer integerDateMinusDay(Integer integerDate) {
        DateTime dateTime = DateUtils.integerToDateTime(integerDate);
        return DateUtils.dateToInteger(dateTime.minusDays(1));
    }

    private BigDecimal roundBigDecimal(BigDecimal amount) {
        if (amount != null) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    private BigDecimal divideBigDecimal(BigDecimal amount, BigDecimal divisor) {
        return amount.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getPercentage(BigDecimal amount, BigDecimal percent) {
        return divideBigDecimal(amount.multiply(percent), new BigDecimal(100));
    }

    private BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        BigDecimal discounted = getPercentage(amount, discountPercent);
        totalDiscount = totalDiscount.add(discounted);
        return amount.subtract(discounted);
    }

    private void calculateTotals(BigDecimal amount) {
        totalGross = totalGross.add(amount);
        totalNet = totalNet.add(applyDiscount(amount, discount));
    }

    public BigDecimal getTotalGross() {
        return roundBigDecimal(totalGross);
    }

    public BigDecimal getTotalDiscount() {
        return roundBigDecimal(totalDiscount);
    }

    public BigDecimal getTotalNet() {
        return roundBigDecimal(totalNet);
    }
}
