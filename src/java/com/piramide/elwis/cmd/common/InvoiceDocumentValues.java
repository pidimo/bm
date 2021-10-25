package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.catalogmanager.Currency;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Jatun S.R.L.
 * Read values for invoice documents
 *
 * @author Miky
 * @version $Id: InvoiceDocumentValues.java 13-sep-2008 11:44:14 $
 */
public class InvoiceDocumentValues {
    private final static Log log = LogFactory.getLog(InvoiceDocumentValues.class);

    private String isoLanguage;

    public InvoiceDocumentValues(String isoLanguage) {
        this.isoLanguage = isoLanguage;
    }

    public static String[] getFieldNames() {
        return VariableConstants.INVOICE_FIELDS;
    }

    public Object[] getValues(Invoice invoice, Integer languageId) {
        String[] invoiceValues = new String[VariableConstants.INVOICE_FIELDS.length];

        for (int i = 0; i < VariableConstants.INVOICE_FIELDS.length; i++) {
            invoiceValues[i] = "";
        }

        invoiceValues[VariableConstants.FIELD_INVOICE_NUMBER] = validValue(invoice.getNumber());
        invoiceValues[VariableConstants.FIELD_INVOICE_DATE] = dateValidValue(invoice.getInvoiceDate());

        if (invoice.getPayConditionId() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_PAY_CONDITION_TEXT] = validValue(getPayCondTextTranslation(invoice.getPayConditionId(), languageId));
        }
        if (invoice.getCurrencyId() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_CURRENCY] = validValue(getCurrency(invoice.getCurrencyId()));
        }
        invoiceValues[VariableConstants.FIELD_INVOICE_PAYMENT_DATE] = dateValidValue(invoice.getPaymentDate());

        if (invoice.getNotesId() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_PAYMENT_TEXT] = validValue(new String(invoice.getFinanceFreeText().getValue()));
        }
        if (invoice.getOpenAmount() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_OPEN_AMOUNT] = decimalValidValue(invoice.getOpenAmount());
        }
        if (invoice.getTotalAmountNet() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_TOTAL_AMOUNT_NET] = decimalValidValue(invoice.getTotalAmountNet());
        }
        if (invoice.getTotalAmountGross() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_TOTAL_AMOUNT_GROSS] = decimalValidValue(invoice.getTotalAmountGross());
        }

        if (invoice.getReminderLevel() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_REMINDER_LAST_LEVEL] = validValue(getReminderLastLevelName(invoice));
        }
        if (invoice.getReminderDate() != null) {
            invoiceValues[VariableConstants.FIELD_INVOICE_REMINDER_NEXT_DATE] = dateValidValue(invoice.getReminderDate());
        }

        if (invoice.getType() != null) {
            if (FinanceConstants.InvoiceType.Invoice.getConstant() == invoice.getType()) {
                invoiceValues[VariableConstants.FIELD_INVOICE_VOUCHER_TYPE] = validValue(SystemTemplateValues.getInvoiceVoucherType(isoLanguage));
            } else if (FinanceConstants.InvoiceType.CreditNote.getConstant() == invoice.getType()) {
                invoiceValues[VariableConstants.FIELD_INVOICE_VOUCHER_TYPE] = validValue(SystemTemplateValues.getCreditNoteVoucherType(isoLanguage));
            }
        }

        return invoiceValues;
    }

    public Object[] getReminderValues(InvoiceReminder reminder, Integer languageId) {
        Invoice invoice = reminder.getInvoice();
        Object[] reminderValues = getValues(invoice, languageId);

        if (reminder.getDate() != null) {
            reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_DATE] = dateValidValue(reminder.getDate());
        }

        reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_LEVEL_TITLE] = validValue(getReminderLevelName(reminder.getReminderLevel()));

        if (reminder.getDescriptionId() != null) {
            reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_TEXT] = validValue(new String(reminder.getDescription().getValue()));
        }

        reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_NEXT_PAYMENT_DATE] = calculateNextReminderDate(reminder);

        if (reminder.getReminderLevel().getFee() != null) {
            reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_FEE] = decimalValidValue(reminder.getReminderLevel().getFee());
        }

        if (invoice.getOpenAmount() != null || reminder.getReminderLevel().getFee() != null) {
            BigDecimal openAmountPlusFee = null;
            if (invoice.getOpenAmount() != null && reminder.getReminderLevel().getFee() != null) {
                openAmountPlusFee = invoice.getOpenAmount().add(reminder.getReminderLevel().getFee());
            } else if (invoice.getOpenAmount() != null) {
                openAmountPlusFee = invoice.getOpenAmount();
            } else if (reminder.getReminderLevel().getFee() != null) {
                openAmountPlusFee = reminder.getReminderLevel().getFee();
            }
            reminderValues[VariableConstants.FIELD_INVOICE_REMINDER_OPENAMOUNTPLUSFEE] = decimalValidValue(openAmountPlusFee);
        }

        return reminderValues;
    }

    public Object[][] getInvoicePositionValues(Invoice invoice, Integer languageId) {
        Object[][] matrix = null;
        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        Collection positions = new ArrayList();
        try {
            positions = invoicePositionHome.findByInvoiceId(invoice.getInvoiceId(), invoice.getCompanyId());
        } catch (FinderException e) {
            log.debug("Fail positions find..." + e);
        }

        if (positions.isEmpty()) {
            //initialize with empty values
            matrix = new Object[1][VariableConstants.INVOICE_POSITION_FIELDS.length];
            for (int i = 0; i < VariableConstants.INVOICE_POSITION_FIELDS.length; i++) {
                matrix[0][i] = "";
            }
        } else {
            matrix = new Object[positions.size()][VariableConstants.INVOICE_POSITION_FIELDS.length];
        }

        int j = 0;
        // Calculate invoice positions for generate document
        for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
            InvoicePosition invoicePosition = (InvoicePosition) iterator.next();
            System.arraycopy(getPositionValue(invoice, invoicePosition, languageId), 0, matrix[j++], 0, VariableConstants.INVOICE_POSITION_FIELDS.length);
        }

        return matrix;
    }

    private Object[] getPositionValue(Invoice invoice, InvoicePosition invoicePosition, Integer languageId) {
        String[] positionValue = new String[VariableConstants.INVOICE_POSITION_FIELDS.length];

        for (int i = 0; i < VariableConstants.INVOICE_POSITION_FIELDS.length; i++) {
            positionValue[i] = "";
        }

        positionValue[VariableConstants.FIELD_INVOICE_POSITION_NUMBER] = validValue(invoicePosition.getNumber() != null ? invoicePosition.getNumber().toString() : null);

        Product product = readProduct(invoicePosition.getProductId());
        if (product != null) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_NAME] = validValue(getProductName(product, languageId));
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_PRODUCTNUMBER] = validValue(product.getProductNumber());
        }

        if (invoicePosition.getFreetextId() != null) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_TEXT] = new String(invoicePosition.getFinanceFreeText().getValue());
        }

        if (invoicePosition.getQuantity() != null) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_QUANTITY] = decimalValidValue(invoicePosition.getQuantity());
        }

        positionValue[VariableConstants.FIELD_INVOICE_POSITION_VAT_NAME] = validValue(getVatLabel(invoicePosition.getVatId()));
        if (invoicePosition.getVatRate() != null) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_VAT_RATE] = decimalValidValue(invoicePosition.getVatRate());
        }

        if (invoicePosition.getUnit() != null) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_UNIT] = validValue(invoicePosition.getUnit());
        }

        if (invoice.getNetGross() != null && FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_PRICE] = decimal4DigitsValidValue(invoicePosition.getUnitPriceGross());
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_TOTAL] = decimalValidValue(invoicePosition.getTotalPriceGross());

        } else {
            //this is net prices
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_PRICE] = decimal4DigitsValidValue(invoicePosition.getUnitPrice());
            positionValue[VariableConstants.FIELD_INVOICE_POSITION_TOTAL] = decimalValidValue(invoicePosition.getTotalPrice());
        }

        return positionValue;
    }

    public Object[][] getInvoiceVatValues(Invoice invoice) {
        Object[][] matrix = null;

        Collection invoiceVats = invoice.getInvoiceVats();
        if (invoiceVats.isEmpty()) {
            matrix = new Object[1][VariableConstants.INVOICE_VAT_FIELDS.length];
            for (int i = 0; i < VariableConstants.INVOICE_VAT_FIELDS.length; i++) {
                matrix[0][i] = "";
            }
        } else {
            matrix = new Object[invoiceVats.size()][VariableConstants.INVOICE_VAT_FIELDS.length];
        }

        int j = 0;
        // Calculate for generate document
        for (Iterator iterator = invoiceVats.iterator(); iterator.hasNext();) {
            InvoiceVat invoiceVat = (InvoiceVat) iterator.next();
            System.arraycopy(getVatValue(invoiceVat), 0, matrix[j++], 0, VariableConstants.INVOICE_VAT_FIELDS.length);
        }
        return matrix;
    }

    private Object[] getVatValue(InvoiceVat invoiceVat) {
        String[] vatValue = new String[VariableConstants.INVOICE_VAT_FIELDS.length];

        for (int i = 0; i < VariableConstants.INVOICE_VAT_FIELDS.length; i++) {
            vatValue[i] = "";
        }

        vatValue[VariableConstants.FIELD_INVOICE_VAT_NAME] = validValue(getVatLabel(invoiceVat.getVatId()));
        vatValue[VariableConstants.FIELD_INVOICE_VAT_RATE] = decimalValidValue(invoiceVat.getVatRate());
        vatValue[VariableConstants.FIELD_INVOICE_VAT_AMOUNT_TO] = decimalValidValue(invoiceVat.getAmount());

        BigDecimal amount = BigDecimalUtils.getPercentage(invoiceVat.getAmount(), invoiceVat.getVatRate());
        vatValue[VariableConstants.FIELD_INVOICE_VAT_AMOUNT] = decimalValidValue(amount);

        return vatValue;
    }

    private String validValue(String value) {
        return value != null && value.trim().length() > 0 ? value : "";
    }

    private String dateValidValue(Integer dateAsInteger) {
        String dateValue = "";
        if (dateAsInteger != null) {
            String pattern = SystemPattern.getDatePattern(isoLanguage);
            dateValue = DateUtils.parseDate(DateUtils.integerToDate(dateAsInteger), pattern);
        }
        return dateValue;
    }

    private String decimalValidValue(BigDecimal decimal) {
        String value = "";
        if (decimal != null) {
            String decimalPattern = SystemPattern.getDecimalPattern(isoLanguage);
            value = validValue(FormatUtils.formatDecimal(decimal, new Locale(isoLanguage), decimalPattern));
        }
        return value;
    }

    private String decimal4DigitsValidValue(BigDecimal decimal) {
        String value = "";
        if (decimal != null) {
            String decimalPattern = SystemPattern.getDecimalPattern4Digits(isoLanguage);
            value = validValue(FormatUtils.formatDecimal(decimal, new Locale(isoLanguage), decimalPattern));
        }
        return value;
    }

    public static Integer getDefaultLanguageId(Address address, Address contactPerson, Address company, Address currentUser) {
        Integer languageId = null;
        Language language = getDefaultLanguage(address, contactPerson, company, currentUser);
        if (language != null) {
            languageId = language.getLanguageId();
        }
        log.debug(" languageId ..." + languageId);
        return languageId;
    }

    public static String getDefaultIsoLang(Address address, Address contactPerson, Address company, Address currentUser) {
        String isoLang_ = null;
        Language language = getDefaultLanguage(address, contactPerson, company, currentUser);
        if (language != null) {
            isoLang_ = language.getLanguageIso();
        }
        log.debug("ISO language ..." + isoLang_);
        return isoLang_;
    }

    private static Language getDefaultLanguage(Address address, Address contactPerson, Address company, Address currentUser) {
        log.debug("Contact person:" + contactPerson);
        Language language = null;
        boolean isPerson = ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType());
        if (!isPerson && contactPerson != null && contactPerson.getLanguage() != null) {
            language = contactPerson.getLanguage();
        } else if (address.getLanguage() != null) {
            language = address.getLanguage();
        } else if (company.getLanguage() != null) {
            language = company.getLanguage();
        } else if (currentUser.getLanguage() != null) {
            language = currentUser.getLanguage();
        }

        return language;
    }

    private String getPayCondTextTranslation(Integer payConditionId, Integer languageId) {
        String text = null;
        PayConditionHome payConditionHome = (PayConditionHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITION);

        try {
            PayCondition payCondition = payConditionHome.findByPrimaryKey(payConditionId);
            for (Iterator iterator = payCondition.getPayConditionTexts().iterator(); iterator.hasNext();) {
                PayConditionText payConditionText = (PayConditionText) iterator.next();
                if (payConditionText.getLanguageId().equals(languageId)) {
                    text = new String(payConditionText.getFreeText().getValue());
                }
            }
            if (text == null) {
                text = payCondition.getPayConditionName();
            }
        } catch (FinderException e) {
            log.debug("Not found PayCondititon entity... " + e);
        }

        return text;
    }

    private String getCurrency(Integer currencyId) {
        String text = null;
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        try {
            Currency currency = currencyHome.findByPrimaryKey(currencyId);
            text = currency.getCurrencyName();
        } catch (FinderException e) {
            log.debug("Not found Currency entity... " + e);
        }
        return text;
    }

    private String getVatLabel(Integer vatId) {
        VatHome vatHome = (VatHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VAT);
        try {
            Vat vat = vatHome.findByPrimaryKey(vatId);
            return vat.getVatLabel();
        } catch (FinderException e) {
            log.debug("Not found Vat entity... " + e);
        }
        return null;
    }

    private Product readProduct(Integer productId) {
        Product product = null;
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            product = productHome.findByPrimaryKey(productId);
        } catch (FinderException e) {
            log.debug("Not found Product:" + productId);
        }
        return product;
    }

    private String getProductName(Product product, Integer languageId) {
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        String translation = null;

        if (product == null) {
            return null;
        }

        try {
            Collection langTexts = langTextHome.findLangTexts(product.getLangTextId());
            for (Iterator iterator = langTexts.iterator(); iterator.hasNext();) {
                LangText langText = (LangText) iterator.next();
                if (langText.getLanguageId().equals(languageId)) {
                    translation = langText.getText();
                    break;
                }
                if (langText.getIsDefault() != null && langText.getIsDefault()) {
                    translation = langText.getText();
                }
            }
        } catch (FinderException e) {
            log.debug("Not found lang texts..." + e);
        }
        if (translation == null) {
            translation = product.getProductName();
        }

        return translation;
    }

    private String getReminderLastLevelName(Invoice invoice) {
        String levelName = null;
        if (invoice.getReminderLevel() != null) {
            for (Iterator iterator = invoice.getInvoiceReminders().iterator(); iterator.hasNext();) {
                InvoiceReminder reminder = (InvoiceReminder) iterator.next();
                ReminderLevel reminderLevel = reminder.getReminderLevel();
                if (invoice.getReminderLevel().equals(reminderLevel.getLevel())) {
                    levelName = getReminderLevelName(reminderLevel);
                    break;
                }
            }
        }
        return levelName;
    }

    private String getReminderLevelName(ReminderLevel reminderLevel) {
        String levelName = null;
        if (reminderLevel != null) {
            levelName = reminderLevel.getName();
        }
        return levelName;
    }

    private String calculateNextReminderDate(InvoiceReminder reminder) {
        Integer nextDate = null;
        if (reminder.getDate() != null) {
            Date date = DateUtils.integerToDate(reminder.getDate());
            nextDate = DateUtils.dateToInteger(date, reminder.getReminderLevel().getNumberOfDays());
        }
        return dateValidValue(nextDate);
    }

    public String getIsoLanguage() {
        return isoLanguage;
    }

    public void setIsoLanguage(String isoLanguage) {
        this.isoLanguage = isoLanguage;
    }
}
