package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.catalogmanager.PayCondition;
import com.piramide.elwis.domain.catalogmanager.PayConditionHome;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceFreeNumberDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceUtil {

    private Log log = LogFactory.getLog(InvoiceUtil.class);

    public static enum SequenceRuleElements {
        Year("Y"),
        Month("M"),
        Day("D"),
        CustomerNumber("C"),
        DigitNumber("N"),
        SlashSeparator("/"),
        BackSlashSeparator("\\"),
        MinusSeparator("-"),
        UnderscoreSeparator("_");

        String element;

        private SequenceRuleElements(String element) {
            this.element = element;
        }

        public String getElement() {
            return element;
        }

    }

    public static InvoiceUtil i = new InvoiceUtil();


    private InvoiceUtil() {

    }


    public Integer getPaymentDate(Integer invoiceDate, Integer payConditionId, Integer companyId) {
        Date invoiceDateObject = DateUtils.integerToDate(invoiceDate);

        PayConditionHome payConditionHome =
                (PayConditionHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITION);

        Integer payDays = 0;
        PayCondition payCondition = null;
        try {
            payCondition = payConditionHome.findByPrimaryKey(payConditionId);
            payDays = payCondition.getPayDays();
        } catch (FinderException e) {
            log.debug("->Read PayCondition payConditionI=" +
                    payConditionId +
                    " FAIL");
        }

        CompanyHome companyHome =
                (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);

        Integer daysToSend = 0;
        try {
            Company company = companyHome.findByPrimaryKey(companyId);
            daysToSend = company.getInvoiceDaysSend();
        } catch (FinderException e) {
            log.error("-> Read Company companyId=" + companyId + " FAIL");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(invoiceDateObject);
        calendar.add(Calendar.DAY_OF_MONTH, payDays + daysToSend);

        return DateUtils.dateToInteger(calendar.getTime());
    }

    public Integer getReminderDate(Integer paymentDate) {
        Date paymentDateAsDateObject = DateUtils.integerToDate(paymentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paymentDateAsDateObject);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        return DateUtils.dateToInteger(calendar.getTime());
    }

    public Integer updateReminderDate(Invoice invoice, Integer reminderDate, Integer numberOfDays) {
        CompanyHome companyHome =
                (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);

        Integer daysToSend = 0;
        try {
            Company company = companyHome.findByPrimaryKey(invoice.getCompanyId());
            daysToSend = company.getInvoiceDaysSend();
        } catch (FinderException e) {
            log.error("-> Read Company companyId=" + invoice.getCompanyId() + " FAIL");
        }

        Date reminderDateObject = DateUtils.integerToDate(reminderDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reminderDateObject);
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays + daysToSend);

        return DateUtils.dateToInteger(calendar.getTime());
    }

    @Deprecated
    /**The sequence rule should be find by Invoice.sequenceRuleId */
    public SequenceRule getSequenceRule(String format, Integer companyId) {
        SequenceRuleHome sequenceRuleHome = getSequenceRuleHome();
        try {
            return sequenceRuleHome.findByFormat(format, companyId);
        } catch (FinderException e) {
            log.debug("-> Read SequenceRule [format=" + format + ", companyId=" + companyId + "] FAIL");
            return null;
        }
    }

    public void setInvoiceFreeNumber(SequenceRule sequenceRule, Integer invoiceDate, Integer number) {
        InvoiceFreeNumberHome invoiceFreeNumberHome = getInvoiceFreeNumberHome();

        InvoiceFreeNumberDTO invoiceFreeNumberDTO = new InvoiceFreeNumberDTO();

        invoiceFreeNumberDTO.put("number", number);
        invoiceFreeNumberDTO.put("companyId", sequenceRule.getCompanyId());
        invoiceFreeNumberDTO.put("invoiceDate", invoiceDate);
        invoiceFreeNumberDTO.put("ruleFormat", sequenceRule.getFormat());
        invoiceFreeNumberDTO.put("sequenceRuleId", sequenceRule.getNumberId());

        try {
            invoiceFreeNumberHome.create(invoiceFreeNumberDTO);
        } catch (CreateException e) {
            log.error("-> Create InvoiceFreenumber FAIL", e);
        }
    }

    public SequenceRule getSequenceRule(Integer sequenceRuleId) {
        SequenceRuleHome sequenceRuleHome = getSequenceRuleHome();

        try {
            return sequenceRuleHome.findByPrimaryKey(sequenceRuleId);
        } catch (FinderException e) {
            log.debug("-> Generate SequenceNumber sequenceRuleId=" + sequenceRuleId + " FAIL");
            return null;
        }
    }

    public String getInvoiceNumber(SequenceRule sequenceRule,
                                   Integer invoiceDate,
                                   String customerNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.integerToDate(invoiceDate));
        sequenceRule.setLastDate(invoiceDate);

        Integer lastNumber = getNextNumber(sequenceRule, invoiceDate);
        if (null == lastNumber) {
            clearInvoiceFreeNumbers(sequenceRule);
            sequenceRule.setLastNumber(sequenceRule.getStartNumber());
            return buildNumber(sequenceRule.getFormat(), sequenceRule.getLastNumber(), customerNumber, calendar);
        }

        Integer freeNumber = getInvoiceFreeNumber(sequenceRule, invoiceDate);
        if (null != freeNumber) {
            sequenceRule.setLastNumber(freeNumber);
            return buildNumber(sequenceRule.getFormat(), freeNumber, customerNumber, calendar);
        }

        sequenceRule.setLastNumber(lastNumber + 1);
        return buildNumber(sequenceRule.getFormat(), sequenceRule.getLastNumber(), customerNumber, calendar);
    }

    public String getProductContractNumber(Integer companyId, String customerNumber) {
        SequenceRule sequenceRule =
                getSequenceRule(FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.getConstant(), companyId);

        if (null == sequenceRule) {
            return null;
        }

        Integer lastNumber = getProductContractFreeNumber(sequenceRule, sequenceRule.getLastDate());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.integerToDate(sequenceRule.getLastDate()));
        return buildNumber(sequenceRule.getFormat(), lastNumber, customerNumber, calendar);
    }

    public String getActionTypeNumber(Integer sequenceRuleId, String customerNumber) {
        SequenceRule sequenceRule =
                getSequenceRule(sequenceRuleId);

        if (null == sequenceRule) {
            return null;
        }

        Integer lastNumber = getProductContractFreeNumber(sequenceRule, sequenceRule.getLastDate());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.integerToDate(sequenceRule.getLastDate()));
        return buildNumber(sequenceRule.getFormat(), lastNumber, customerNumber, calendar);
    }


    private Integer getProductContractFreeNumber(SequenceRule sequenceRule, Integer sequenceRuleLastDate) {
        Integer actualDate = DateUtils.dateToInteger(new Date());

        Integer lastNumber;
        if (FinanceConstants.SequenceRuleResetType.NoReset.equal(sequenceRule.getResetType())) {
            lastNumber = sequenceRule.getLastNumber();
            if (null == sequenceRuleLastDate) {
                lastNumber = sequenceRule.getStartNumber();
            }

            sequenceRule.setLastNumber(lastNumber + 1);
            sequenceRule.setLastDate(actualDate);
            return lastNumber;
        }

        Integer firstDay = null;
        Integer endDay = null;

        //if sequencerule have daily reset, firstDay and endDay equals to invoiceDate
        if (FinanceConstants.SequenceRuleResetType.Daily.equal(sequenceRule.getResetType())) {
            firstDay = sequenceRuleLastDate;
            endDay = sequenceRuleLastDate;
        }

        if (null != sequenceRuleLastDate) {
            //if sequencerule have Monthly reset
            if (FinanceConstants.SequenceRuleResetType.Monthly.equal(sequenceRule.getResetType())) {
                Map<String, Date> range = DateUtils.getFirstAndEndDayOfMonth(DateUtils.integerToDate(sequenceRuleLastDate));
                firstDay = DateUtils.dateToInteger(range.get("firstDay"));
                endDay = DateUtils.dateToInteger(range.get("endDay"));
            }

            //if sequencerule have Yearly reset
            if (FinanceConstants.SequenceRuleResetType.Yearly.equal(sequenceRule.getResetType())) {
                Map<String, Date> range = DateUtils.getFirstAndEndDayOfYear(DateUtils.integerToDate(sequenceRuleLastDate));
                firstDay = DateUtils.dateToInteger(range.get("firstDay"));
                endDay = DateUtils.dateToInteger(range.get("endDay"));
            }
        }

        if (null == firstDay || null == endDay) {
            lastNumber = sequenceRule.getLastNumber();
            if (null == sequenceRuleLastDate) {
                lastNumber = sequenceRule.getStartNumber();
            }

            sequenceRule.setLastNumber(lastNumber + 1);
            sequenceRule.setLastDate(actualDate);
            return lastNumber;
        }

        if (firstDay <= actualDate && actualDate <= endDay) {
            lastNumber = sequenceRule.getLastNumber();
            sequenceRule.setLastNumber(lastNumber + 1);
        } else {
            lastNumber = sequenceRule.getStartNumber();
            sequenceRule.setLastNumber(lastNumber + 1);
        }

        sequenceRule.setLastDate(actualDate);

        return lastNumber;
    }

    public String getCustomerNumber(Integer companyId) {
        SequenceRule sequenceRule =
                getSequenceRule(FinanceConstants.SequenceRuleType.CUSTOMER.getConstant(), companyId);

        if (null == sequenceRule) {
            return null;
        }

        Integer lastNumber = sequenceRule.getLastNumber();

        sequenceRule.setLastDate(DateUtils.dateToInteger(new Date()));
        sequenceRule.setLastNumber(lastNumber + 1);
        return buildDigitsInRule(sequenceRule.getFormat(), sequenceRule.getLastNumber());
    }

    public String getSupportCaseNumber(Integer companyId) {
        SequenceRule sequenceRule =
                getSequenceRule(FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstant(), companyId);

        if (null == sequenceRule) {
            return null;
        }

        Integer lastNumber = sequenceRule.getLastNumber();

        sequenceRule.setLastDate(DateUtils.dateToInteger(new Date()));
        sequenceRule.setLastNumber(lastNumber + 1);
        return buildDigitsInRule(sequenceRule.getFormat(), sequenceRule.getLastNumber());
    }

    public String getArticleNumber(Integer companyId) {
        SequenceRule sequenceRule =
                getSequenceRule(FinanceConstants.SequenceRuleType.ARTICLE.getConstant(), companyId);

        if (null == sequenceRule) {
            return null;
        }

        Integer lastNumber = sequenceRule.getLastNumber();

        sequenceRule.setLastDate(DateUtils.dateToInteger(new Date()));
        sequenceRule.setLastNumber(lastNumber + 1);
        return buildDigitsInRule(sequenceRule.getFormat(), sequenceRule.getLastNumber());
    }

    public String applyCustomerNumber(String customerNumber, Integer companyId) {
        SequenceRule sequenceRule =
                getSequenceRule(FinanceConstants.SequenceRuleType.CUSTOMER.getConstant(), companyId);
        if (null == sequenceRule) {
            return null;
        }

        Integer number = null;
        try {
            number = Integer.valueOf(customerNumber);
        } catch (NumberFormatException e) {
            log.error("-> Parse customerNumber=" + customerNumber + " to Integer FAIL ", e);
        }

        if (null == number) {
            return null;
        }

        return buildDigitsInRule(sequenceRule.getFormat(), number);

    }

    private SequenceRule getSequenceRule(Integer type, Integer companyId) {
        SequenceRuleHome sequenceRuleHome = getSequenceRuleHome();

        try {
            return sequenceRuleHome.findByType(type, companyId);
        } catch (FinderException e) {
            log.debug("-> Read Customer sequence rule companyId=" + companyId + " FAIL");
        }
        return null;
    }

    public String buildInvoiceNumber(String format, Integer number, String customerNumber, Integer invoiceDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.integerToDate(invoiceDate));
        return buildNumber(format, number, customerNumber, calendar);
    }

    private void clearInvoiceFreeNumbers(SequenceRule sequenceRule) {
        InvoiceFreeNumberHome invoiceFreeNumberHome = getInvoiceFreeNumberHome();
        Collection invoiceFreenumbers = null;
        try {
            invoiceFreenumbers =
                    invoiceFreeNumberHome.findByRangeFormat(sequenceRule.getNumberId(), sequenceRule.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Read InvoiceFreenumbers [format=" + sequenceRule.getFormat() + "] FAIL");
        }

        if (null != invoiceFreenumbers) {
            for (Object object : invoiceFreenumbers) {
                InvoiceFreeNumber invoiceFreeNumber = (InvoiceFreeNumber) object;
                try {
                    invoiceFreeNumber.remove();
                } catch (RemoveException e) {
                    log.error("-> Remove InvoiceFreeNumber FAIL", e);
                }
            }
        }
    }

    private void removeInvoiceFreenumber(SequenceRule sequenceRule, Integer number, Integer invoiceDate) {
        InvoiceFreeNumberHome invoiceFreeNumberHome = getInvoiceFreeNumberHome();

        if (FinanceConstants.SequenceRuleResetType.NoReset.equal(sequenceRule.getResetType())) {
            InvoiceFreeNumber invoiceFreeNumber = null;
            try {
                invoiceFreeNumber = invoiceFreeNumberHome.findInvoiceFreeNumber(
                        sequenceRule.getNumberId(),
                        number, sequenceRule.getCompanyId());
            } catch (FinderException e) {
                log.debug("-> Execute InvoiceFreeNumberHome.findInvoiceFreeNumber FAIL");
            }
            if (null != invoiceFreeNumber) {
                try {
                    invoiceFreeNumber.remove();
                } catch (RemoveException e) {
                    log.error("-> Cannot remove InvoiceFreeNumber ", e);
                }
            }
            return;
        }

        Integer firstDay = null;
        Integer endDay = null;

        //if sequencerule have daily reset, firstDay and endDay equals to invoiceDate
        if (FinanceConstants.SequenceRuleResetType.Daily.equal(sequenceRule.getResetType())) {
            firstDay = invoiceDate;
            endDay = invoiceDate;
        }

        //if sequencerule have Monthly reset
        if (FinanceConstants.SequenceRuleResetType.Monthly.equal(sequenceRule.getResetType())) {
            Map<String, Date> range = DateUtils.getFirstAndEndDayOfMonth(DateUtils.integerToDate(invoiceDate));
            firstDay = DateUtils.dateToInteger(range.get("firstDay"));
            endDay = DateUtils.dateToInteger(range.get("endDay"));
        }

        //if sequencerule have Yearly reset
        if (FinanceConstants.SequenceRuleResetType.Yearly.equal(sequenceRule.getResetType())) {
            Map<String, Date> range = DateUtils.getFirstAndEndDayOfYear(DateUtils.integerToDate(invoiceDate));
            firstDay = DateUtils.dateToInteger(range.get("firstDay"));
            endDay = DateUtils.dateToInteger(range.get("endDay"));
        }

        Collection invoiceFreeNumbers = null;
        try {
            invoiceFreeNumbers = invoiceFreeNumberHome.findInvoiceFreeNumberByRange(
                    sequenceRule.getNumberId(), firstDay,
                    endDay, number, sequenceRule.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Execute InvoiceFreeNumberHome.findInvoiceFreeNumberByRange FAIL");
        }

        if (null != invoiceFreeNumbers) {
            for (Object object : invoiceFreeNumbers) {
                InvoiceFreeNumber invoiceFreeNumber = (InvoiceFreeNumber) object;
                try {
                    invoiceFreeNumber.remove();
                } catch (RemoveException e) {
                    log.error("-> Cannot remove InvoiceFreeNumber ", e);
                }
            }
        }
    }

    private Integer getInvoiceFreeNumber(SequenceRule sequenceRule, Integer invoiceDate) {
        InvoiceFreeNumberHome invoiceFreeNumberHome = getInvoiceFreeNumberHome();

        if (FinanceConstants.SequenceRuleResetType.NoReset.equal(sequenceRule.getResetType())) {
            try {
                Integer freeNumber = invoiceFreeNumberHome.selectGetNumber(sequenceRule.getNumberId(),
                        sequenceRule.getCompanyId());
                log.debug("-> Available number for format=" +
                        sequenceRule.getFormat() +
                        " freeNumber=" + freeNumber);
                if (null != freeNumber) {
                    removeInvoiceFreenumber(sequenceRule, freeNumber, invoiceDate);
                }
                return freeNumber;
            } catch (FinderException e) {
                log.debug("-> No available numbers for sequenceRuleId=" + sequenceRule.getNumberId());
                return null;
            }
        }

        Integer firstDay = null;
        Integer endDay = null;

        //if sequencerule have daily reset, firstDay and endDay equals to invoiceDate
        if (FinanceConstants.SequenceRuleResetType.Daily.equal(sequenceRule.getResetType())) {
            firstDay = invoiceDate;
            endDay = invoiceDate;
        }

        //if sequencerule have Monthly reset
        if (FinanceConstants.SequenceRuleResetType.Monthly.equal(sequenceRule.getResetType())) {
            Map<String, Date> range = DateUtils.getFirstAndEndDayOfMonth(DateUtils.integerToDate(invoiceDate));
            firstDay = DateUtils.dateToInteger(range.get("firstDay"));
            endDay = DateUtils.dateToInteger(range.get("endDay"));
        }

        //if sequencerule have Yearly reset
        if (FinanceConstants.SequenceRuleResetType.Yearly.equal(sequenceRule.getResetType())) {
            Map<String, Date> range = DateUtils.getFirstAndEndDayOfYear(DateUtils.integerToDate(invoiceDate));
            firstDay = DateUtils.dateToInteger(range.get("firstDay"));
            endDay = DateUtils.dateToInteger(range.get("endDay"));
        }

        try {

            Integer freeNumber = invoiceFreeNumberHome.selectGetNumberByRange(sequenceRule.getNumberId(),
                    firstDay,
                    endDay,
                    sequenceRule.getCompanyId());
            log.debug("-> Available number for format=" +
                    sequenceRule.getFormat() + " startDate=" + firstDay + " endDate=" + endDay +
                    " freeNumber=" + freeNumber);
            if (null != freeNumber) {
                removeInvoiceFreenumber(sequenceRule, freeNumber, invoiceDate);
            }
            return freeNumber;
        } catch (FinderException e) {
            log.debug("-> No available numbers for sequenceRuleId=" +
                    sequenceRule.getNumberId() + " invoiceDate=" + invoiceDate);
            return null;
        }
    }

    private Integer getNextNumber(SequenceRule sequenceRule, Integer invoiceDate) {
        InvoiceHome invoiceHome = getInvoiceHome();
        Integer lastnumber;
        if (FinanceConstants.SequenceRuleResetType.NoReset.equal(sequenceRule.getResetType())) {
            try {
                lastnumber = invoiceHome.selectGetLastRuleNumberByRuleFormat(
                        sequenceRule.getNumberId(),
                        sequenceRule.getCompanyId());
                log.debug("-> Next Number for [format=" +
                        sequenceRule.getFormat() + ", companyId=" + sequenceRule.getCompanyId() +
                        "] = " + lastnumber + " OK ");
                return lastnumber;
            } catch (FinderException e) {
                log.debug("-> Next Number for [format=" +
                        sequenceRule.getFormat() + ", companyId=" + sequenceRule.getCompanyId() +
                        "] FAIL");
                return null;
            }
        }

        if (FinanceConstants.SequenceRuleResetType.Daily.equal(sequenceRule.getResetType())) {
            try {
                lastnumber = invoiceHome.selectGetLastRuleNumberByInvoiceDate(invoiceDate,
                        invoiceDate, sequenceRule.getNumberId(), sequenceRule.getCompanyId());
                log.debug("-> Next Number for [startDate=" + invoiceDate +
                        ", endDate=" + invoiceDate +
                        ", format=" + sequenceRule.getFormat() +
                        ", companyId=" + sequenceRule.getCompanyId() + "] = " +
                        lastnumber + " OK ");
                return lastnumber;
            } catch (FinderException e) {
                log.debug("-> Next Number in Daily ResetType FAIL");
                return null;
            }
        }

        if (FinanceConstants.SequenceRuleResetType.Monthly.equal(sequenceRule.getResetType())) {
            try {
                Map<String, Date> range = DateUtils.getFirstAndEndDayOfMonth(DateUtils.integerToDate(invoiceDate));
                Integer firstDay = DateUtils.dateToInteger(range.get("firstDay"));
                Integer endDay = DateUtils.dateToInteger(range.get("endDay"));
                lastnumber = invoiceHome.selectGetLastRuleNumberByInvoiceDate(firstDay,
                        endDay,
                        sequenceRule.getNumberId(),
                        sequenceRule.getCompanyId());
                log.debug("-> Next Number for [startDate=" + firstDay +
                        ", endDate=" + endDay +
                        ", format=" + sequenceRule.getFormat() +
                        ", companyId=" + sequenceRule.getCompanyId() + "] = " +
                        lastnumber + " OK ");
                return lastnumber;
            } catch (FinderException e) {
                log.debug("-> Next Number in Monthly ResetType FAIL");
                return null;
            }
        }

        if (FinanceConstants.SequenceRuleResetType.Yearly.equal(sequenceRule.getResetType())) {
            try {
                Map<String, Date> range = DateUtils.getFirstAndEndDayOfYear(DateUtils.integerToDate(invoiceDate));
                Integer firstDay = DateUtils.dateToInteger(range.get("firstDay"));
                Integer endDay = DateUtils.dateToInteger(range.get("endDay"));
                lastnumber = invoiceHome.selectGetLastRuleNumberByInvoiceDate(firstDay,
                        endDay,
                        sequenceRule.getNumberId(),
                        sequenceRule.getCompanyId());
                log.debug("-> Next Number for [startDate=" + firstDay +
                        ", endDate=" + endDay +
                        ", format=" + sequenceRule.getFormat() +
                        ", companyId=" + sequenceRule.getCompanyId() + "] = " +
                        lastnumber + " OK ");
                return lastnumber;
            } catch (FinderException e) {
                log.debug("-> Next Number in Yearly ResetType FAIL");
                return null;
            }
        }

        return null;
    }

    public BigDecimal calculateTotalAmountGross(Invoice invoice) {
        if (FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross())) {
            return calculateTotalAmountGrossForNetPrices(invoice);
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            return calculateTotalAmountGrossForGrossPrices(invoice);
        }

        return null;
    }

    public BigDecimal calculateTotalAmountGrossForNetPrices(Invoice invoice) {
        Collection invoiceVats = invoice.getInvoiceVats();

        BigDecimal totalAmountGross = new BigDecimal(0);
        if (null == invoiceVats) {
            return totalAmountGross;
        }

        for (Object object : invoiceVats) {
            InvoiceVat invoiceVat = (InvoiceVat) object;
            BigDecimal modifiedValue =
                    calculateTotalPriceGrossForInvoicePositions(
                            invoiceVat.getAmount(),
                            invoiceVat.getVatRate());
            totalAmountGross = BigDecimalUtils.sum(totalAmountGross, modifiedValue);
        }

        return totalAmountGross;
    }

    public BigDecimal calculateTotalAmountGrossForGrossPrices(Invoice invoice) {
        Collection invoicePositions = getInvoicePositions(invoice);
        BigDecimal totalAmountGross = new BigDecimal(0);
        for (Object object : invoicePositions) {
            InvoicePosition invoicePosition = (InvoicePosition) object;
            if (null == invoicePosition.getTotalPriceGross()) {
                continue;
            }

            totalAmountGross = totalAmountGross.add(invoicePosition.getTotalPriceGross());
        }

        return totalAmountGross;
    }

    public BigDecimal calculateTotalPriceGrossForInvoicePositions(BigDecimal totalPrice,
                                                                  BigDecimal vatRate) {
        BigDecimal percent = vatRate.divide(new BigDecimal(100));
        return BigDecimalUtils.sum(totalPrice, BigDecimalUtils.multiply(totalPrice, percent));
    }

    public BigDecimal calculateTotalPriceForInvoicePositions(BigDecimal totalGrossPrice,
                                                             BigDecimal vatRate) {
        BigDecimal vatRateAsPercentage = vatRate.divide(new BigDecimal(100));
        BigDecimal factor = (new BigDecimal(1)).add(vatRateAsPercentage);

        return totalGrossPrice.divide(factor, 25, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalAmountNet(Invoice invoice) {

        if (FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross())) {
            return calculateTotalAmountNetForNetPrices(invoice);
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            return calculateTotalAmountNetForGrossPrices(invoice);
        }

        return null;
    }

    private BigDecimal calculateTotalAmountNetForNetPrices(Invoice invoice) {
        Collection invoicePositions = getInvoicePositions(invoice);

        BigDecimal totalAmount = new BigDecimal(0);
        for (Object object : invoicePositions) {
            InvoicePosition invoicePosition = (InvoicePosition) object;
            if (null == invoicePosition.getTotalPrice()) {
                continue;
            }

            totalAmount = BigDecimalUtils.sum(totalAmount, invoicePosition.getTotalPrice());
        }

        return totalAmount;
    }

    private BigDecimal calculateTotalAmountNetForGrossPrices(Invoice invoice) {
        Collection invoiceVats = invoice.getInvoiceVats();
        BigDecimal totalAmount = new BigDecimal(0);
        if (null == invoiceVats) {
            return totalAmount;
        }

        for (Object object : invoiceVats) {
            InvoiceVat invoiceVat = (InvoiceVat) object;
            totalAmount = BigDecimalUtils.sum(totalAmount, invoiceVat.getAmount());
        }

        return totalAmount;
    }

    public BigDecimal calculateOpenAmount(Invoice invoice) {
        BigDecimal totalPaid = getTotalPaid(invoice.getInvoiceId(), invoice.getCompanyId());
        return invoice.getTotalAmountGross().subtract(totalPaid);
    }

    public BigDecimal getTotalPaid(Integer invoiceId, Integer companyId) {
        InvoicePaymentHome invoicePaymentHome =
                (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        BigDecimal result = new BigDecimal(0);

        Collection invoicePayments;
        try {
            invoicePayments = invoicePaymentHome.findByInvoiceId(invoiceId, companyId);
        } catch (FinderException e) {
            return result;
        }

        for (Object object : invoicePayments) {
            InvoicePayment invoicePayment = (InvoicePayment) object;
            result = result.add(invoicePayment.getAmount());
        }
        return result;
    }

    private Collection getInvoicePositions(Invoice invoice) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        Collection invoicePositions = new ArrayList();
        try {
            invoicePositions = invoicePositionHome.findByInvoiceId(invoice.getInvoiceId(),
                    invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("-> Read invoicePositions invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }
        return invoicePositions;
    }

    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private String buildNumber(String ruleFormat, Integer lastNumber, String customerNumber, Calendar calendar) {
        String dayOfMonth = getDayOfMonth(calendar);
        String monthOfYear = getMonthOfYear(calendar);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String smallYear = getSmallYear(calendar.get(Calendar.YEAR));
        String digits = getDigitPart(ruleFormat);
        String customerNumberPattern = getCustomerPart(ruleFormat);


        String number = ruleFormat.replaceAll("YYYY", year).
                replaceAll("YY", smallYear).
                replaceAll("DD", dayOfMonth).
                replaceAll("MM", monthOfYear).
                replaceAll(digits, buildDigitsInRule(digits, lastNumber));
        if (null != customerNumberPattern) {
            number = number.replaceAll(customerNumberPattern,
                    buildDigitsInCustomerNumber(customerNumberPattern, customerNumber));
        }

        return number;
    }

    private String buildDigitsInCustomerNumber(String pattern, String customerNumber) {
        if (null == customerNumber) {
            return pattern.replaceAll("C", "0");
        }

        if (pattern.length() < customerNumber.length()) {
            return customerNumber;
        }

        char[] patternArray = pattern.toCharArray();
        char[] numbersArray = customerNumber.toCharArray();
        char[] result = new char[patternArray.length];

        int changeIndex = patternArray.length - numbersArray.length;

        int j = 0;
        for (int i = 0; i < patternArray.length; i++) {
            if (i < changeIndex) {
                result[i] = '0';
                continue;
            }

            result[i] = numbersArray[j];
            j++;
        }
        return new String(result);
    }

    private String buildDigitsInRule(String pattern, Integer number) {

        if (pattern.length() < number.toString().length()) {
            return number.toString();
        }

        char[] numberDigits = number.toString().toCharArray();
        char[] availableDigits = pattern.toCharArray();
        char[] result = new char[availableDigits.length];

        int changeIndex = availableDigits.length - numberDigits.length;

        int j = 0;
        for (int i = 0; i < availableDigits.length; i++) {
            if (i < changeIndex) {
                result[i] = '0';
                continue;
            }

            result[i] = numberDigits[j];
            j++;
        }
        return new String(result);
    }

    private String getSmallYear(int year) {
        return String.valueOf(year).substring(2);
    }

    private String getMonthOfYear(Calendar calendar) {
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        if (monthOfYear < 10) {
            return "0" + monthOfYear;
        }

        return String.valueOf(monthOfYear);
    }

    private String getDayOfMonth(Calendar calendar) {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth < 10) {
            return "0" + dayOfMonth;
        }

        return String.valueOf(dayOfMonth);
    }

    private String getDigitPart(String ruleFormat) {
        return readElements(ruleFormat, SequenceRuleElements.DigitNumber.getElement()).get(0);
    }

    private String getCustomerPart(String ruleFormat) {
        List<String> pattern =
                readElements(ruleFormat, SequenceRuleElements.CustomerNumber.getElement());
        if (pattern.size() == 0) {
            return null;
        }

        return pattern.get(0);
    }

    private List<String> readElements(String format, String element) {
        final String regex = "(" + element + "*)";

        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(format);
        List<String> elements = new ArrayList<String>();
        while (safeFind(matcher)) {
            String match = matcher.group(0);
            if (null != match && !"".equals(match.trim())) {
                elements.add(match);
            }
        }
        return elements;
    }

    private boolean safeFind(Matcher matcher) {
        try {
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private SequenceRuleHome getSequenceRuleHome() {
        return
                (SequenceRuleHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_SEQUENCERULE);
    }

    private InvoiceHome getInvoiceHome() {
        return (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
    }

    private AddressHome getAddressHome() {
        return (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
    }

    private InvoiceFreeNumberHome getInvoiceFreeNumberHome() {
        return (InvoiceFreeNumberHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEFREENUMBER);
    }
}
