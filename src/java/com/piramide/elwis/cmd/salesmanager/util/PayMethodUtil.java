package com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.math.BigDecimal;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: PayMethodUtil.java 28-nov-2008 11:28:25 $
 */
public class PayMethodUtil {
    private static Log log = LogFactory.getLog(PayMethodUtil.class);

    private static DateTime fixPeriodicMethodEndDatePlus(DateTime endDateTime) {
        return endDateTime.plusDays(1);
    }

    private static DateTime fixPeriodicMethodEndDateMinus(DateTime endDateTime) {
        return endDateTime.minusDays(1);
    }

    public static boolean isExactMonthPeriod(DateTime startDateTime, DateTime endDateTime) {
        boolean isExact = false;

        if (startDateTime.isBefore(endDateTime)) {
            Period period = new Period(startDateTime, endDateTime);
            if ((period.getYears() > 0 || period.getMonths() > 0) && period.getWeeks() == 0 && period.getDays() == 0) {
                isExact = true;
            }
        }
        return isExact;
    }

    /**
     * Verify if the start date is an match period start
     * @param startDate start date
     * @param payPeriod period
     * @return true or false
     */
    public static boolean isMatchPeriodStart(DateTime startDate, int payPeriod) {
        boolean isMatchStart = false;

        if (isFirstDayOfMonth(DateUtils.dateToInteger(startDate)) && isMatchPeriod(payPeriod)) {
            int month = startDate.getMonthOfYear();

            //i.e: if payPeriod = 3 => month = {1,4,7,10} (january, april, july, october)
            if (payPeriod == 1 || month % payPeriod == 1) {
                isMatchStart = true;
            }
        }
        return isMatchStart;
    }

    public static boolean isMatchPeriodEnd(DateTime endDate, int payPeriod) {
        DateTime startNextDate = endDate.plusDays(1);
        return isMatchPeriodStart(startNextDate, payPeriod);

    }

    /**
     * Verify if the period is an match period
     * @param payPeriod period
     * @return true or false
     */
    public static boolean isMatchPeriod(int payPeriod) {
        int yearMonths = 12;
        if (payPeriod < yearMonths) {
            //this can be: 1=monthly, 3=quarterly, 6=half yearly
            return (yearMonths % payPeriod == 0);
        } else {
            //this can be: 12=yearly, 24=2 years, 36=3 years, 60=5 years 
            return (payPeriod % yearMonths == 0);
        }
    }

    /**
     * Verify if can be convert in match period
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param payPeriod period
     * @return true or false
     */
    public static boolean canBeConvertedInStartMatchPeriod(Integer startDate, Integer invoicedUntil, int payPeriod) {
        DateTime startDateTime = getInvoiceStartDateTime(startDate, invoicedUntil);
        return (isMatchPeriod(payPeriod) && isFirstDayOfMonth(DateUtils.dateToInteger(startDateTime)) && !isMatchPeriodStart(startDateTime, payPeriod));
    }

    /**
     * Get the months required to convert this invoice period in match period
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @return remain months
     */
    public static int remainMonthsToMatchPeriod(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod) {
        int remain = 0;
        DateTime startDateTime = getInvoiceStartDateTime(startDate, invoicedUntil);
        DateTime endDateTime = (contractEndDate != null) ? DateUtils.integerToDateTime(contractEndDate) : null;

        if (isFirstDayOfMonth(DateUtils.dateToInteger(startDateTime)) && canBeConvertedInStartMatchPeriod(startDate, invoicedUntil, payPeriod)) {
            DateTime newStartDate = startDateTime.toDateTime();
            while (!isMatchPeriodStart(newStartDate, payPeriod) && (endDateTime == null || newStartDate.isBefore(endDateTime))) {
                newStartDate = newStartDate.plusMonths(1);
                remain++;
            }
        }
        return remain;
    }

    /**
     * Get the total months required to convert this invoice period in match period from the start date
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @return total remain months
     */
    public static int calculateInvoiceTotalRemainMonthsToMatchPeriod(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod) {
        int remainMonths = remainMonthsToMatchPeriod(startDate, invoicedUntil, contractEndDate, payPeriod);
        return calculatePeriodicMonthsInvoiced(startDate, invoicedUntil) + remainMonths;
    }

    /**
     * Get the real invoice start date
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @return DateTime
     */
    public static DateTime getInvoiceStartDateTime(Integer startDate, Integer invoicedUntil) {
        DateTime startDateTime ;
        if (invoicedUntil != null) {
            startDateTime = fixPeriodicMethodEndDatePlus(DateUtils.integerToDateTime(invoicedUntil));
        } else {
            startDateTime = DateUtils.integerToDateTime(startDate);
        }
        return startDateTime;
    }

    public static boolean isLastDayOfMonth(Integer dateTimeAsInteger) {
        DateTime dateTime = DateUtils.integerToDateTime(dateTimeAsInteger);
        int lasDayOfMonth = dateTime.dayOfMonth().withMaximumValue().getDayOfMonth();
        int actualDay = dateTime.getDayOfMonth();
        return lasDayOfMonth == actualDay;
    }

    public static boolean isFirstDayOfMonth(Integer dateTimeAsInteger) {
        DateTime dateTime = DateUtils.integerToDateTime(dateTimeAsInteger);
        return dateTime.getDayOfMonth() == 1;
    }

    private static DateTime addMonths(DateTime dateTime, int months) {
        return dateTime.plusMonths(months);
    }

    /**
     * Get calendar exact months between startDate and invoicedUntil
     * @param startDate the start date
     * @param invoicedUntil valid contract periodic invoiced until. This should be : "Start date + (N calendar months - 1 day)".
     * @return months in range
     */
    public static int calculatePeriodicMonthsInvoiced(Integer startDate, Integer invoicedUntil) {
        int months = 0;
        if (invoicedUntil != null) {
            DateTime dateTimeStart = DateUtils.integerToDateTime(startDate);
            DateTime dateTimeEnd = fixPeriodicMethodEndDatePlus(DateUtils.integerToDateTime(invoicedUntil));
            months = calculateMonthsInRange(dateTimeStart, dateTimeEnd);
        }
        return months;
    }

    /**
     * calculate months in range
     * @param startDate periodic start date
     * @param filterDate any date until where should be invoiced the contract, this can be peridic end date to calculate exact motnhs.
     * @return months
     */
    public static int calculateMonthsUntilFilterDate(Integer startDate, Integer filterDate) {
        int months = 0;
        DateTime dateTimeStart = DateUtils.integerToDateTime(startDate);
        DateTime dateTimeEnd = fixPeriodicMethodEndDatePlus(DateUtils.integerToDateTime(filterDate));
        months = calculateMonthsInRange(dateTimeStart, dateTimeEnd);
        return months;
    }

    /**
     * Calculate months in range
     * @param startDateTime any data
     * @param endDateTime any date
     * @return months
     */
    public static int calculateMonthsInRange(DateTime startDateTime, DateTime endDateTime) {
        int totalMonths = 0;
        if (startDateTime.isBefore(endDateTime)) {

            Period period = new Period(startDateTime, endDateTime);
            if (period.getYears() > 0) {
                totalMonths = (period.getYears() * 12);
            }
            totalMonths = totalMonths + period.getMonths();
        }
        return totalMonths;
    }

    /**
     * Is valid periodic end date if make good the rule: "Start date + (N calendar months - 1 day)".
     * So valid exmaples can be:
     *     if startDate=20090201 => periodicEndDate=20090228
     *     if startDate=20090201 => periodicEndDate=20090331
     *     if startDate=20090202 => periodicEndDate=20090301
     *     if startDate=20090202 => periodicEndDate=20090401
     * @param startDate start date
     * @param periodicEndDate end date
     * @return true or false
     */
    public static boolean isValidPeriodicEndDate(Integer startDate, Integer periodicEndDate) {
        DateTime dateTimeStart = DateUtils.integerToDateTime(startDate);
        DateTime dateTimeEnd = fixPeriodicMethodEndDatePlus(DateUtils.integerToDateTime(periodicEndDate));
        return isExactMonthPeriod(dateTimeStart, dateTimeEnd);
    }

    /**
     * Calculate remain months only if is last invoice and months is less than Pay period (invoiceMonths < payPeriod). Can not be applied an complet period.
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @return remain months
     */
    public static int calculatePeriodicLastInvoiceMonths(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod) {
        int remainMonths = 0;
        if (contractEndDate != null) {
            int invoicedMonths = calculatePeriodicMonthsInvoiced(startDate, invoicedUntil);
            int allMonths = calculatePeriodicMonthsInvoiced(startDate, contractEndDate);

            int missingPeriod = (allMonths - invoicedMonths) / payPeriod;
            if (missingPeriod == 0) {
                DateTime startDateTime = DateUtils.integerToDateTime(startDate);
                Integer newInvoiceUntil = invoicedUntil;
                while (newInvoiceUntil < contractEndDate) {
                    remainMonths++;
                    DateTime dateTimeInvoiced = startDateTime.plusMonths(invoicedMonths + remainMonths);
                    newInvoiceUntil = DateUtils.dateToInteger(fixPeriodicMethodEndDateMinus(dateTimeInvoiced));
                }
            }
        }
        return remainMonths;
    }

    /**
     * Verify if is last invoice respect to contract end date.
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @return true or false
     */
    public static boolean isPeriodicLastInvoice(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod) {
        return calculatePeriodicLastInvoiceMonths(startDate, invoicedUntil, contractEndDate, payPeriod) > 0;
    }

    /**
     * Get num of exact periods between invoiced until date and filter date
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param filterDate filter date to invoice the contract
     * @param payPeriod period
     * @return exact periods
     */
    public static int getExactPeriodsBetweenInvoiceUntilAndFilterDate(Integer startDate, Integer invoicedUntil, Integer filterDate, int payPeriod) {
        int invoicedMonths = calculatePeriodicMonthsInvoiced(startDate, invoicedUntil);
        int allMonths = calculateMonthsUntilFilterDate(startDate, filterDate);
        int periods = (allMonths - invoicedMonths) / payPeriod;
        return periods;
    }

    private static int calculatePeriodicInvoiceMonthsInOnePeriod(Integer startDate, Integer invoicedUntil, Integer contractEndDate, Integer nextInvoiceDate, int payPeriod, Integer filterDate) {
        int periodicMonths = 0;

        if (ProcessContractPayMethodUtil.isValidPeriodicContractToInvoice(nextInvoiceDate, invoicedUntil, contractEndDate, filterDate)) {

            if (contractEndDate != null && isPeriodicLastInvoice(startDate, invoicedUntil, contractEndDate, payPeriod)) {
                periodicMonths = calculatePeriodicLastInvoiceMonths(startDate, invoicedUntil, contractEndDate, payPeriod);
            } else {
                periodicMonths = payPeriod;
            }
        }
        return periodicMonths;
    }

    /**
     * Calculate all months until the last invoce until date from start date. This is the contract invoice process until the filter date.
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @param withMatchCalendar if should be match calendar
     * @param filterDate filter date to invoice the contract. Any date until where should be invoiced the contract, this can be peridic end date to calculate exact motnhs.
     * @param nextInvoiceDate next invoice date
     * @param invoiceDelay delay in days
     * @return all months until new invoiced until date
     */
    public static int calculateInvoiceTotalMonthsForPeriodicMethod(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod, boolean withMatchCalendar, Integer filterDate, Integer nextInvoiceDate, Integer invoiceDelay) {
        log.debug("calculateTotalMonthsForPeriodicMethod................................." + startDate);
        DateTime startDateTime = DateUtils.integerToDateTime(startDate);

        if (nextInvoiceDate == null) {
            nextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(invoiceDelay, startDate, invoicedUntil);
        }

        int periodicMonth;
        if (canBeConvertedInStartMatchPeriod(startDate, invoicedUntil, payPeriod) && withMatchCalendar) {
            periodicMonth = remainMonthsToMatchPeriod(startDate, invoicedUntil, contractEndDate, payPeriod);
        } else {
            periodicMonth = calculatePeriodicInvoiceMonthsInOnePeriod(startDate, invoicedUntil, contractEndDate, nextInvoiceDate, payPeriod, filterDate);
        }

        int totalMonths = 0;
        Integer newInvoicedUntil = invoicedUntil;
        Integer newNextInvoiceDate = nextInvoiceDate;

        if (periodicMonth > 0) {
            totalMonths = calculatePeriodicMonthsInvoiced(startDate, invoicedUntil) + periodicMonth;

            DateTime newInvoicedUntilDateTime = fixPeriodicMethodEndDateMinus(addMonths(startDateTime, totalMonths));
            newInvoicedUntil = DateUtils.dateToInteger(newInvoicedUntilDateTime);
            newNextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(invoiceDelay, startDate, newInvoicedUntil);
        }

        while (ProcessContractPayMethodUtil.isValidPeriodicContractToInvoice(newNextInvoiceDate, newInvoicedUntil, contractEndDate, filterDate)) {

            periodicMonth = calculatePeriodicInvoiceMonthsInOnePeriod(startDate, newInvoicedUntil, contractEndDate, newNextInvoiceDate, payPeriod, filterDate);

            totalMonths = calculatePeriodicMonthsInvoiced(startDate, newInvoicedUntil) + periodicMonth;
            DateTime newInvoicedUntilDateTime = fixPeriodicMethodEndDateMinus(addMonths(startDateTime, totalMonths));

            newInvoicedUntil = DateUtils.dateToInteger(newInvoicedUntilDateTime);
            newNextInvoiceDate = ProcessContractPayMethodUtil.calculatePeriodicNextInvoiceDate(invoiceDelay, startDate, newInvoicedUntil);
        }

        log.debug("Total Moths in periodic:" + totalMonths);
        return calculatePeriodicMonthsInvoiced(startDate, newInvoicedUntil);
    }

    /**
     * Calculate the next invoice until date. This is the contract invoice process until the filter date.
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param contractEndDate contract end date
     * @param payPeriod period
     * @param withMatchCalendar if should be match calendar
     * @param filterDate filter date to invoice the contract. Any date until where should be invoiced the contract, this can be peridic end date to calculate exact motnhs.
     * @return next invoiced until date
     */
    public static Integer calculatePeriodicNextInvoicedUntil(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod, boolean withMatchCalendar, Integer filterDate, Integer nextInvoiceDate, Integer invoiceDelay) {
        DateTime startDateTime = DateUtils.integerToDateTime(startDate);
        int totalMonths = calculateInvoiceTotalMonthsForPeriodicMethod(startDate, invoicedUntil, contractEndDate, payPeriod, withMatchCalendar, filterDate, nextInvoiceDate, invoiceDelay);

        DateTime newInvoicedUntilDateTime = fixPeriodicMethodEndDateMinus(addMonths(startDateTime, totalMonths));
        return DateUtils.dateToInteger(newInvoicedUntilDateTime);
    }

    /**
     * Apply only one period and return the new invoiced until date
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param payPeriod period
     * @return new invoiced until
     */
    public static Integer applyPayPeriodType(Integer startDate, Integer invoicedUntil, int payPeriod) {
        return applyPayPeriodType(startDate, invoicedUntil, payPeriod, 1);
    }

    /**
     *
     * Apply N periods and return the new invoiced until date
     * @param startDate contract start date
     * @param invoicedUntil contract invoiced until date
     * @param payPeriod period
     * @param periodsToApply N periods to apply
     * @return new invoiced until
     */
    public static Integer applyPayPeriodType(Integer startDate, Integer invoicedUntil, int payPeriod, int periodsToApply) {
        DateTime startDateTime = DateUtils.integerToDateTime(startDate);
        int periodMonths = (payPeriod * periodsToApply);
        int totalMonths = calculatePeriodicMonthsInvoiced(startDate, invoicedUntil) + periodMonths;
        DateTime newInvoicedUntilDateTime = fixPeriodicMethodEndDateMinus(addMonths(startDateTime, totalMonths));
        return DateUtils.dateToInteger(newInvoicedUntilDateTime);
    }

    /**
     * Verify if single contract open amount is valid, this is invoicePosition.totalPrice == contract.openAmount
     * @param openAmount open amount of single contract
     * @param salePositionQuantity quantity in sale position
     * @return tru or false
     */
    public static boolean isValidSingleOpenAmount(BigDecimal openAmount, BigDecimal salePositionQuantity) {
        BigDecimal positionUnitPrice = BigDecimalUtils.divide(openAmount, salePositionQuantity);
        BigDecimal positionTotalPrice = BigDecimalUtils.multiply(positionUnitPrice, salePositionQuantity);

        return openAmount.doubleValue() == positionTotalPrice.doubleValue();
    }

    /**
     * Verify if pay period is multiple of price period
     * @param payPeriod
     * @param pricePeriod
     * @return true or false
     */
    public static boolean isMultiplePayPeriodOfPricePeriod(Integer payPeriod, Integer pricePeriod) {
        boolean isMultiple = false;
        if (payPeriod > 0 && pricePeriod > 0) {
            int module = payPeriod % pricePeriod;
            isMultiple = (module == 0);
        }
        return isMultiple;
    }

    /**
     * Calculate the quantity and price for invoice from periodic product contracts, this should be:
     * if is match calendar period and pay period is not multiple of price period; quantity = monthsToInvoice , price = pricePerMonth
     * otherwise; quantity = monthsToInvoice/pricePeriod , price = price.
     *
     * return array BigDecimal[{quantity},{price}]
     *
     * @param monthsToInvoice periodic months to invoice
     * @param pricePeriod price period
     * @param price price
     * @param pricePerMonth price per month
     * @return BigDecimal[{quantity},{price}]
     */
    public static BigDecimal[] getPeriodicQuantityPriceForInvoice(Integer monthsToInvoice, Integer pricePeriod, BigDecimal price, BigDecimal pricePerMonth) {
        BigDecimal quantity;
        BigDecimal unitPrice;
        boolean monthsIsMultiple = isMultiplePayPeriodOfPricePeriod(monthsToInvoice, pricePeriod);
        if (!monthsIsMultiple) {
            quantity = BigDecimal.valueOf(monthsToInvoice);
            unitPrice = pricePerMonth;
        } else {
            quantity = BigDecimalUtils.divide(new BigDecimal(monthsToInvoice), new BigDecimal(pricePeriod));
            unitPrice = price;
        }

        BigDecimal quantityPrice[] = new BigDecimal[2];
        quantityPrice[0] = quantity;
        quantityPrice[1] = unitPrice;

        return quantityPrice;
    }

    /**
     * Verify if should make match calendar and the result remain months to match is not multiple of price period
     *
     * @param startDate
     * @param invoicedUntil
     * @param contractEndDate
     * @param payPeriod
     * @param withMatchCalendar
     * @param pricePeriod
     * @return true or false
     */
    public static boolean isMatchCalendarWithoutMultipleOfPricePeriod(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int payPeriod, boolean withMatchCalendar, Integer pricePeriod) {
        if (withMatchCalendar && canBeConvertedInStartMatchPeriod(startDate, invoicedUntil, payPeriod)) {
            int remainMonths = remainMonthsToMatchPeriod(startDate, invoicedUntil, contractEndDate, payPeriod);
            if (remainMonths > 0 && !isMultiplePayPeriodOfPricePeriod(remainMonths, pricePeriod)) {
                return true;
            }
        }
        return false;
    }

}
