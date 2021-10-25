package com.piramide.elwis.service.sales.utils;


import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FormatUtils;
import com.piramide.elwis.utils.SalesConstants;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Jatun S.R.L.
 * Class to wrapper the contract end reminder message.
 *
 * @author Miky
 * @version $Id: ContractEndReminderMessage.java  09-nov-2009 15:20:33$
 */
public class ContractEndReminderMessage {
    private static final String SUBJECT_KEY = "ContractReminder.Msg.subject";
    private static final String BODY_DESCRIPTION_KEY = "ContractReminder.Msg.bodyDescription";
    private static final String CONTRACT_DETAIL_BEY = "ContractReminder.Msg.contractDetail";
    private static final String OPTIONS_DETAIL_BEY = "ContractReminder.Msg.optionsDetail";
    //conrtact detail
    private static final String NUMBER_KEY = "ContractReminder.Msg.number";
    private static final String CONTACT_KEY = "ContractReminder.Msg.contact";
    private static final String CONTACT_PERSON_KEY = "ContractReminder.Msg.contactPerson";
    private static final String PRODUCT_KEY = "ContractReminder.Msg.product";
    private static final String PRICE_KEY = "ContractReminder.Msg.monthlyPrice";
    private static final String DISCOUNT_KEY = "ContractReminder.Msg.discount";
    private static final String SELLER_KEY = "ContractReminder.Msg.seller";
    private static final String CURRENCY_KEY = "ContractReminder.Msg.currency";
    private static final String PAY_METHOD_KEY = "ContractReminder.Msg.payMethod";
    private static final String PAY_PERIOD_KEY = "ContractReminder.Msg.payPeriod";
    private static final String PAY_STARTDATE_KEY = "ContractReminder.Msg.payStartDate";
    private static final String INVOICED_UNTIL_KEY = "ContractReminder.Msg.invoicedUntil";
    private static final String CONTRACT_ENDDATE_KEY = "ContractReminder.Msg.contractEndDate";

    private static final String DATE_PATTERN_KEY = "datePattern";
    private static final String DECIMAL_PATTERN_KEY = "numberFormat.2DecimalPlaces";

    private String subject;
    private Integer daysBeforeReminder;

    private String number;
    private String contactName;
    private String contactPersonName;
    private String productName;
    private BigDecimal price;
    private BigDecimal discount;
    private String sellerName;
    private String currency;
    private String payMethod;
    private Integer payPeriod;
    private Integer payStartDate;
    private Integer invocedUntil;
    private Integer contractEndDate;


    private ResourceBundle resourceBundle;
    private Locale locale;


    public ContractEndReminderMessage(String isoLanguage, Integer daysBeforeReminder) {

        if (isoLanguage != null) {
            resourceBundle = ResourceBundle.getBundle(SalesConstants.SALES_SERVICE_RESOURCES, new Locale(isoLanguage));
            locale = new Locale(isoLanguage);
        } else {
            resourceBundle = ResourceBundle.getBundle(SalesConstants.SALES_SERVICE_RESOURCES);
            locale = Locale.getDefault();
        }

        this.subject = resourceBundle.getString(SUBJECT_KEY);
        this.daysBeforeReminder = daysBeforeReminder;
    }

    /**
     * Returns the Message to be sent by email.
     *
     * @return the message formatted
     */
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatString(BODY_DESCRIPTION_KEY, new Object[]{daysBeforeReminder})).append("\n");
        sb.append("\n").append(resourceBundle.getString(CONTRACT_DETAIL_BEY)).append("\n");

        addMessage(NUMBER_KEY, number, sb);
        addMessage(CONTACT_KEY, contactName, sb);
        addMessage(CONTACT_PERSON_KEY, contactPersonName, sb);
        addMessage(PRODUCT_KEY, productName, sb);
        addMessage(PRICE_KEY, price, sb);
        addMessage(DISCOUNT_KEY, discount, sb);
        addMessage(SELLER_KEY, sellerName, sb);
        addMessage(CURRENCY_KEY, currency, sb);
        sb.append(resourceBundle.getString(PAY_METHOD_KEY)).append("\n");

        sb.append("\n").append(resourceBundle.getString(OPTIONS_DETAIL_BEY)).append("\n");
        addMessage(PAY_PERIOD_KEY, payPeriod, sb);
        addDateMessage(PAY_STARTDATE_KEY, payStartDate, sb);
        addDateMessage(INVOICED_UNTIL_KEY, invocedUntil, sb);
        addDateMessage(CONTRACT_ENDDATE_KEY, contractEndDate, sb);

        return new String(sb);
    }

    private void addMessage(String key, String value, StringBuilder sb) {
        addKeyValueMessage(key, value, sb);
    }

    private void addMessage(String key, Integer integerValue, StringBuilder sb) {
        addKeyValueMessage(key, integerValue, sb);
    }

    private void addMessage(String key, BigDecimal decimalValue, StringBuilder sb) {
        if (decimalValue != null) {
            addKeyValueMessage(key, formatDecimalValue(decimalValue), sb);
        }
    }

    private void addDateMessage(String key, Integer dateAsInteger, StringBuilder sb) {
        if (dateAsInteger != null) {
            addKeyValueMessage(key, formatDateValue(dateAsInteger), sb);
        }
    }

    private void addKeyValueMessage(String key, Object value, StringBuilder sb) {
        if (key != null && value != null) {
            sb.append(formatString(key, new Object[]{value})).append("\n");
        }
    }

    private String formatDecimalValue(BigDecimal decimal) {
        String value = null;
        if (decimal != null) {
            value = FormatUtils.formatDecimal(decimal, locale, resourceBundle.getString(DECIMAL_PATTERN_KEY));
        }
        return value;
    }

    private String formatDateValue(Integer dateAsInteger) {
        String dateValue = null;
        if (dateAsInteger != null) {
            dateValue = DateUtils.parseDate(dateAsInteger, resourceBundle.getString(DATE_PATTERN_KEY));
        }
        return dateValue;
    }

    /**
     * Format an string with params
     *
     * @param key    the resource key
     * @param params the object params
     * @return formatted string
     */
    private String formatString(String key, Object[] params) {
        MessageFormat formatString = new MessageFormat(resourceBundle.getString(key));
        return formatString.format(params);
    }

    /*
    * Getter and Setter
    */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(Integer payPeriod) {
        this.payPeriod = payPeriod;
    }

    public Integer getPayStartDate() {
        return payStartDate;
    }

    public void setPayStartDate(Integer payStartDate) {
        this.payStartDate = payStartDate;
    }

    public Integer getInvocedUntil() {
        return invocedUntil;
    }

    public void setInvocedUntil(Integer invocedUntil) {
        this.invocedUntil = invocedUntil;
    }

    public Integer getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Integer contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
}
