package test.com.piramide.elwis.cmd.salesmanager.util;

import com.piramide.elwis.cmd.salesmanager.util.PayMethodUtil;
import com.piramide.elwis.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Jatun S.R.L.
 * Class to test methods in PayMethodUtil 
 *
 * @author Miky
 * @version $Id: PayMethodUtilTest.java  23-oct-2009 14:28:54$
 */
public class PayMethodUtilTest {
    private static Log log = LogFactory.getLog(PayMethodUtilTest.class);
    private String PATTERN = "dd.MM.yyyy";

    @Test
    public void testIsValidPeriodicContractEndDate() {
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090227));
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090329));
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090429));

        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090131, 20090227));
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090131, 20090330));
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090131, 20090429));
        Assert.assertTrue(PayMethodUtil.isValidPeriodicEndDate(20090131, 20090530));

        Assert.assertFalse(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090226));
        Assert.assertFalse(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090228));
        Assert.assertFalse(PayMethodUtil.isValidPeriodicEndDate(20090130, 20090330));

        //print valid end dates
        int[] startDates = {20090101, 20090102, 20090128, 20090129, 20090130, 20090131};
        printValidPeriodicEndDate(startDates);
    }

    private void printValidPeriodicEndDate(int[] startDates) {

        for (int i = 0; i < startDates.length; i++) {
            int startDate = startDates[i];
            DateTime dateTime = DateUtils.integerToDateTime(startDate);
            Integer nextStartDate = startDate;

            System.out.println("# If start date = " + DateUtils.parseDate(startDate, PATTERN));
            int count = 1;
            while (count <= 10) {
                dateTime = dateTime.plusDays(1);
                Integer endDate = DateUtils.dateToInteger(dateTime);
                if (PayMethodUtil.isValidPeriodicEndDate(startDate, endDate)) {
                    System.out.println("Invoice " + count + ": "+DateUtils.parseDate(nextStartDate, PATTERN)+" to " + DateUtils.parseDate(endDate, PATTERN));
                    nextStartDate = DateUtils.dateToInteger(dateTime.plusDays(1));
                    count++;
                }
            }
        }
    }

    @Test
    public void testCalculatePeriodicMonthsInvoiced() {
        Assert.assertEquals(1, PayMethodUtil.calculatePeriodicMonthsInvoiced(20090130, 20090227));
        Assert.assertEquals(2, PayMethodUtil.calculatePeriodicMonthsInvoiced(20090130, 20090329));
        Assert.assertEquals(3, PayMethodUtil.calculatePeriodicMonthsInvoiced(20090130, 20090429));
        Assert.assertEquals(11, PayMethodUtil.calculatePeriodicMonthsInvoiced(20090130, 20091229));
        Assert.assertEquals(36, PayMethodUtil.calculatePeriodicMonthsInvoiced(20090101, 20111231));

        //invoice process
//        invoiceProcess(20090130, null, null, 1, 100);
//        invoiceProcess(20090101, 20090331, null, 3, 25);
//        invoiceProcess(20090201, null, null, 5, 25);
        invoiceProcess(20090130, null, null, 1, 10);
    }

    private void invoiceProcess(Integer startDate, Integer invoicedUntil, Integer contractEndDate, int period, int numInvoices) {
        int genInvoices = 0;
        System.out.println("INVOCIE PROCESS WITH Start date:" + startDate + " invoiceUntil:" + invoicedUntil + " contract end date:" + contractEndDate + " period:" + period + " invoices:" + numInvoices);

        while (genInvoices < numInvoices && (invoicedUntil == null || contractEndDate == null || invoicedUntil.intValue() < contractEndDate)) {

            //Validations
            if (invoicedUntil != null && !PayMethodUtil.isValidPeriodicEndDate(startDate, invoicedUntil)) {
                System.out.println("Fail: not valid incoice until.." + invoicedUntil);
                return;
            }
            if (contractEndDate != null && !PayMethodUtil.isValidPeriodicEndDate(startDate, contractEndDate)) {
                System.out.println("Fail: not valid end date.." + contractEndDate);
                return;
            }

            int addMonths = period;
            if (invoicedUntil != null) {
                addMonths = addMonths + PayMethodUtil.calculatePeriodicMonthsInvoiced(startDate, invoicedUntil);
            }
//            System.out.println("MONTHS:" + addMonths);

            DateTime dateTimeStart = DateUtils.integerToDateTime(startDate);
            DateTime dateTimeInvoiced = dateTimeStart.plusMonths(addMonths);
            invoicedUntil = DateUtils.dateToInteger(dateTimeInvoiced.minusDays(1));

            genInvoices++;
            System.out.println(genInvoices + " Invoiced To: " + DateUtils.parseDate(invoicedUntil, PATTERN));
        }
    }

    @Test
    public void testCalculatePeriodicLastInvoiceMonths() {

        Assert.assertEquals(0, PayMethodUtil.calculatePeriodicLastInvoiceMonths(20090112, null, 20090911, 3));
        Assert.assertEquals(0, PayMethodUtil.calculatePeriodicLastInvoiceMonths(20090112, 20090411, 20090911, 3));
        Assert.assertEquals(2, PayMethodUtil.calculatePeriodicLastInvoiceMonths(20090112, 20090711, 20090911, 3));
        Assert.assertEquals(0, PayMethodUtil.calculatePeriodicLastInvoiceMonths(20090112, 20090711, 20091011, 3));

        Assert.assertFalse(PayMethodUtil.isPeriodicLastInvoice(20090112, 20090411, 20090911, 3));
        Assert.assertTrue(PayMethodUtil.isPeriodicLastInvoice(20090112, 20090711, 20090911, 3));
    }

    @Test
    public void testGetExactPeriodsBetweenInvoiceUntilAndFilterDate() {

        Assert.assertEquals(1, PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(20090112, null, 20090630, 3));
        Assert.assertEquals(0, PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(20090112, 20090411, 20090630, 3));

        Assert.assertEquals(2, PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(20090112, 20090411, 20091011, 3));

        Assert.assertEquals(1, PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(20090101, null, 20090227, 1));
        Assert.assertEquals(2, PayMethodUtil.getExactPeriodsBetweenInvoiceUntilAndFilterDate(20090101, null, 20090228, 1));
    }

    @Test
    public void testCalculateInvoiceTotalMonthsForPeriodicMethod() {
        Assert.assertEquals(5, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, null, null, 1, false, 20090515, null, null));
        Assert.assertEquals(5, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, 20090228, null, 1, false, 20090515, null, null));

        //with end date
        Assert.assertEquals(9, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, null, 20091130, 3, false, 20090731, null, null));
        Assert.assertEquals(11, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, 20090930, 20091130, 3, false, 20091001, null, null));
        Assert.assertEquals(11, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, null, 20091130, 3, false, 20091130, null, null));
        Assert.assertEquals(9, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, null, 20090930, 3, false, 20090930, null, null));

        //contract end date < filter date
        Assert.assertEquals(8, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, null, 20090831, 3, false, 20090930, null, null));
        Assert.assertEquals(8, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090101, 20090731, 20090831, 3, false, 20090930, null, null));

        //any date as start date
        Assert.assertEquals(4, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090212, null, null, 2, false, 20090530, null, null));
        Assert.assertEquals(4, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090212, 20090411, null, 2, false, 20090530, null, null));
        Assert.assertEquals(5, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090212, 20090411, null, 3, false, 20090530, null, null));
        Assert.assertEquals(6, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090212, 20090411, 20091011, 2, false, 20090701, null, null));

        //with match calendar
        Assert.assertEquals(2, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090201, null, null, 3, true, 20090201, null, null));
        Assert.assertEquals(3, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090201, null, null, 3, false, 20090201, null, null));
        Assert.assertEquals(5, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090201, null, null, 3, true, 20090430, null, null));
        Assert.assertEquals(2, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090501, 20090531, null, 3, true, 20090601, null, null));
        Assert.assertEquals(4, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090501, 20090531, null, 3, false, 20090601, null, null));
        Assert.assertEquals(5, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090501, 20090531, null, 3, true, 20090701, null, null));

        Assert.assertEquals(2, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090501, null, 20090630, 3, true, 20090701, null, null));
        Assert.assertEquals(3, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090501, null, 20090731, 3, true, 20090801, null, null));

        //with last days
        Assert.assertEquals(1, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090130, null, null, 1, false, 20090201, null, null));
        Assert.assertEquals(3, PayMethodUtil.calculateInvoiceTotalMonthsForPeriodicMethod(20090130, null, null, 1, false, 20090401, null, null));

    }

    @Test
    public void testApplyPayPeriodType() {
        Assert.assertEquals(Integer.valueOf(20090131), PayMethodUtil.applyPayPeriodType(20090101, null, 1));
        Assert.assertEquals(Integer.valueOf(20090211), PayMethodUtil.applyPayPeriodType(20090112, null, 1));
        Assert.assertEquals(Integer.valueOf(20090411), PayMethodUtil.applyPayPeriodType(20090112, null, 3));
        Assert.assertEquals(Integer.valueOf(20090511), PayMethodUtil.applyPayPeriodType(20090112, 20090211, 3));

        //last day
        Assert.assertEquals(Integer.valueOf(20090227), PayMethodUtil.applyPayPeriodType(20090130, null, 1));
        Assert.assertEquals(Integer.valueOf(20090227), PayMethodUtil.applyPayPeriodType(20090131, null, 1));
        Assert.assertEquals(Integer.valueOf(20090329), PayMethodUtil.applyPayPeriodType(20090130, null, 2));
        Assert.assertEquals(Integer.valueOf(20090330), PayMethodUtil.applyPayPeriodType(20090131, null, 2));
        Assert.assertEquals(Integer.valueOf(20090429), PayMethodUtil.applyPayPeriodType(20090131, null, 3));

        Assert.assertEquals(Integer.valueOf(20090530), PayMethodUtil.applyPayPeriodType(20090131, 20090227, 3));
    }
}
