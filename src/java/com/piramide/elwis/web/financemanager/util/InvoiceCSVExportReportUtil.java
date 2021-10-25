package com.piramide.elwis.web.financemanager.util;

import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Util to generate the CSV export invoice data
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class InvoiceCSVExportReportUtil {
    private static Log log = LogFactory.getLog(InvoiceCSVExportReportUtil.class);
    public final static String EXPORTDATATYPE_CUSTOMER = "1";
    public final static String EXPORTDATATYPE_PARTNER = "2";

    public static String getColumnValue(Object columnValue) {
        String value = "";
        if (columnValue != null) {
            value = columnValue.toString();
        }
        return value;
    }

    public static String getCustomerNumber(String customerNumber, String debitorNumber, String exportDataTypeParam) {
        String number = customerNumber;
        if (isPartnerExportData(exportDataTypeParam)) {
            number = debitorNumber;
        }
        return number;
    }

    public static String getAddressName(String customerName, String debitorName, String exportDataTypeParam) {
        String name = customerName;
        if (isPartnerExportData(exportDataTypeParam)) {
            name = debitorName;
        }
        return name;
    }

    public static String getCompanyName(String addressName, String addressType) {
        String name = "";
        if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(addressType)) {
            name = addressName;
        }
        return name;
    }

    public static String getPrivatePersonName(String name, String addressType, String exportDataTypeParam) {
        if (isCustomerExportData(exportDataTypeParam) && ContactConstants.ADDRESSTYPE_PERSON.equals(addressType)) {
            return name;
        }
        return "";
    }

    public static String getEmptyValue(String value) {
        return "";
    }

    public static String formatStringColumnValue(String value) {
        String formattedValue = "";
        if (value != null) {
            //delete some special character considered in csv export
            value = value.replaceAll("\"", "");
            value = value.replaceAll(";", "");
            formattedValue = value;
        }
        return formattedValue;
    }

    public static String getMonthOfDate(Object dateAsInt) {
        String month = "";
        if (dateAsInt != null && dateAsInt.toString().length() > 0) {
            DateTime dateTime = DateUtils.integerToDateTime(Integer.valueOf(dateAsInt.toString()));
            int monthOfYear = dateTime.monthOfYear().get();
            month = String.valueOf(monthOfYear);
        }
        return month;
    }

    public static String calculatePaymentAmount(Object amountObj, String invoiceType, String locale, String decimalPattern) {
        String value = "";
        BigDecimal amount = getObjAsBigDecimal(amountObj);
        if (amount != null) {
            if (isCreditNote(invoiceType)) {
                amount = amount.multiply(BigDecimal.valueOf(-1));
            }
            value = FormatUtils.formatDecimal(amount, new Locale(locale), decimalPattern);
        }
        return value;
    }

    public static String calculatePositionAmount(Object totalPriceObj, Object totalPriceGrossObj, String invoiceNetGross, String invoiceType, Object vatRate, String locale, String decimalPattern) {
        String value = "";

        if (FinanceConstants.NetGrossFLag.NET.getConstantAsString().equals(invoiceNetGross)) {
            BigDecimal totalPrice = getObjAsBigDecimal(totalPriceObj);
            if (totalPrice != null) {
                BigDecimal vatRateValue = new BigDecimal(vatRate.toString());
                BigDecimal totalGross = applyVatRateToAmount(totalPrice, vatRateValue);

                if (isCreditNote(invoiceType)) {
                    totalGross = totalGross.multiply(BigDecimal.valueOf(-1));
                }
                value = FormatUtils.formatDecimal(totalGross, new Locale(locale), decimalPattern);
            }

        } else if (FinanceConstants.NetGrossFLag.GROSS.getConstantAsString().equals(invoiceNetGross)) {
            BigDecimal totalPriceGross = getObjAsBigDecimal(totalPriceGrossObj);
            if (totalPriceGross != null) {
                if (isCreditNote(invoiceType)) {
                    totalPriceGross = totalPriceGross.multiply(BigDecimal.valueOf(-1));
                }
                value = FormatUtils.formatDecimal(totalPriceGross, new Locale(locale), decimalPattern);
            }
        }
        return value;
    }

    private static BigDecimal applyVatRateToAmount(BigDecimal amount, BigDecimal vatRate) {
        return InvoiceUtil.i.calculateTotalPriceGrossForInvoicePositions(amount, vatRate);
    }

    private static boolean isCreditNote(String invoiceType) {
        return FinanceConstants.InvoiceType.CreditNote.equal(invoiceType);
    }

    private static BigDecimal getObjAsBigDecimal(Object value) {
        if (value != null && value.toString().length() > 0) {
            return new BigDecimal(value.toString());
        }
        return null;
    }


    private static boolean isPartnerExportData(String exportDataType) {
        return EXPORTDATATYPE_PARTNER.equals(exportDataType);
    }

    private static boolean isCustomerExportData(String exportDataType) {
        return EXPORTDATATYPE_CUSTOMER.equals(exportDataType);
    }

    public static List<String> getCustomersWithoutCustomerNumber(Integer startDate, Integer endDate, HttpServletRequest request) {
        List<String> result = new ArrayList<String>();

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String listName = "invoiceCustomerDataExportList";

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/finance");
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        if (startDate != null) {
            fantabulousUtil.addSearchParameter("startInvoiceDate", startDate.toString());
        }
        if (endDate != null) {
            fantabulousUtil.addSearchParameter("endInvoiceDate", endDate.toString());
        }

        List<Map> resultSetList = fantabulousUtil.getData(request, listName);
        Map customerNamesMap = new HashMap();
        for (Map resultSetMap : resultSetList) {
            String addressId = (String) resultSetMap.get("addressId");
            String customerNumber = (String) resultSetMap.get("customerNumber");
            String customerName = (String) resultSetMap.get("addressName");

            if (customerNumber == null || customerNumber.trim().length() == 0) {
                customerNamesMap.put(addressId, customerName);
            }
        }

        if (!customerNamesMap.isEmpty()) {
            result.addAll(customerNamesMap.values());
        }
        return result;
    }

    public static List<String> getSequenceRuleWithoutPartnerNumber(Integer startDate, Integer endDate, HttpServletRequest request) {
        List<String> result = new ArrayList<String>();

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String listName = "invoicePartnerDataExportExtendedList";

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/finance");
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        if (startDate != null) {
            fantabulousUtil.addSearchParameter("startInvoiceDate", startDate.toString());
        }
        if (endDate != null) {
            fantabulousUtil.addSearchParameter("endInvoiceDate", endDate.toString());
        }

        List<Map> resultSetList = fantabulousUtil.getData(request, listName);
        Map sequenceRuleNamesMap = new HashMap();
        for (Map resultSetMap : resultSetList) {
            String numberId = (String) resultSetMap.get("numberId");
            String ruleLabel = (String) resultSetMap.get("sequenceRuleLabel");
            String debitorAddressId = (String) resultSetMap.get("addressId");
            String debitorNumber = (String) resultSetMap.get("debitorNumber");

            if (GenericValidator.isBlankOrNull(debitorAddressId) || GenericValidator.isBlankOrNull(debitorNumber)) {
                sequenceRuleNamesMap.put(numberId, ruleLabel);
            }
        }

        if (!sequenceRuleNamesMap.isEmpty()) {
            result.addAll(sequenceRuleNamesMap.values());
        }
        return result;
    }

    private static List<Map> executeInvoiceCustomerExportList(Integer startDate, Integer endDate, Integer currencyId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String listName = "invoiceCustomerExportList";

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/finance");
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        if (startDate != null) {
            fantabulousUtil.addSearchParameter("startInvoiceDate", startDate.toString());
        }
        if (endDate != null) {
            fantabulousUtil.addSearchParameter("endInvoiceDate", endDate.toString());
        }
        if (currencyId != null) {
            fantabulousUtil.addSearchParameter("currencyId", currencyId.toString());
        }

        List<Map> resultSetList = fantabulousUtil.getData(request, listName);

        return resultSetList;
    }

    public static Map validateInvoicePositionExportedData(Integer startDate, Integer endDate, HttpServletRequest request, String exportDataType) {
        Map errorDataResultMap = new HashMap();

        List<Map> resultSetList = executeInvoiceCustomerExportList(startDate, endDate, null, request);

        Map customerNamesMap = new HashMap();
        Map sequenceRuleNamesMap = new HashMap();
        Map productNamesMap = new HashMap();

        for (Map resultSetMap : resultSetList) {

            String addressId = (String) resultSetMap.get("addressId");
            String customerNumber = (String) resultSetMap.get("customerNumber");
            String customerName = (String) resultSetMap.get("addressName");

            String numberId = (String) resultSetMap.get("numberId");
            String ruleLabel = (String) resultSetMap.get("sequenceRuleLabel");
            String debitorName = (String) resultSetMap.get("debitorName");
            String debitorNumber = (String) resultSetMap.get("debitorNumber");

            String productId = (String) resultSetMap.get("productId");
            String productName = (String) resultSetMap.get("productName");
            String accountNumber = (String) resultSetMap.get("accountNumber");

            if (isPartnerExportData(exportDataType)) {
                if (GenericValidator.isBlankOrNull(debitorNumber) || GenericValidator.isBlankOrNull(debitorName)) {
                    sequenceRuleNamesMap.put(numberId, ruleLabel);
                }
            } else {
                if (GenericValidator.isBlankOrNull(customerNumber)) {
                    customerNamesMap.put(addressId, customerName);
                }
            }

            if (GenericValidator.isBlankOrNull(accountNumber)) {
                productNamesMap.put(productId, productName);
            }
        }

        if (!customerNamesMap.isEmpty()) {
            errorDataResultMap.put("customerWithoutNumberList", customerNamesMap.values());
        }

        if (!sequenceRuleNamesMap.isEmpty()) {
            errorDataResultMap.put("sequenceRuleWithoutPartnerList", sequenceRuleNamesMap.values());
        }

        if (!productNamesMap.isEmpty()) {
            errorDataResultMap.put("productWithoutAccountList", productNamesMap.values());
        }

        return errorDataResultMap;
    }

    public static List<Integer> getInvoicePositionExportedIds(Integer startDate, Integer endDate, HttpServletRequest request) {
        List<Integer> result = new ArrayList<Integer>();

        List<Map> resultSetList = executeInvoiceCustomerExportList(startDate, endDate, null, request);
        for (Map resultSetMap : resultSetList) {
            Object positionId = resultSetMap.get("positionId");
            if (positionId != null && positionId.toString().length() > 0) {
                result.add(Integer.valueOf(positionId.toString()));
            }
        }
        return result;
    }

    private static List<Map> executeInvoiceCustomerPaymentExportList(Integer startDate, Integer endDate, Integer currencyId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String listName = "invoiceCustomerPaymentExportList";

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/finance");
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        if (startDate != null) {
            fantabulousUtil.addSearchParameter("startInvoiceDate", startDate.toString());
        }
        if (endDate != null) {
            fantabulousUtil.addSearchParameter("endInvoiceDate", endDate.toString());
        }
        if (currencyId != null) {
            fantabulousUtil.addSearchParameter("currencyId", currencyId.toString());
        }

        List<Map> resultSetList = fantabulousUtil.getData(request, listName);

        return resultSetList;
    }

    public static Map validateInvoicePaymentExportedData(Integer startDate, Integer endDate, HttpServletRequest request, String exportDataType) {
        Map errorDataResultMap = new HashMap();

        List<Map> resultSetList = executeInvoiceCustomerPaymentExportList(startDate, endDate, null, request);

        Map customerNamesMap = new HashMap();
        Map sequenceRuleNamesMap = new HashMap();
        Map bankAccountNamesMap = new HashMap();
        Map invoiceNumbersMap = new HashMap();

        for (Map resultSetMap : resultSetList) {

            String addressId = (String) resultSetMap.get("addressId");
            String customerNumber = (String) resultSetMap.get("customerNumber");
            String customerName = (String) resultSetMap.get("addressName");

            String numberId = (String) resultSetMap.get("numberId");
            String ruleLabel = (String) resultSetMap.get("sequenceRuleLabel");
            String debitorName = (String) resultSetMap.get("debitorName");
            String debitorNumber = (String) resultSetMap.get("debitorNumber");

            String invoiceId = (String) resultSetMap.get("invoiceId");
            String invoiceNumber = (String) resultSetMap.get("invoiceNumber");
            String bankAccountId = (String) resultSetMap.get("bankAccountId");
            String bankAccountDescription = (String) resultSetMap.get("bankAccountDescription");
            String bookkeepingAccount = (String) resultSetMap.get("bookkeepingAccount");

            if (isPartnerExportData(exportDataType)) {
                if (GenericValidator.isBlankOrNull(debitorNumber) || GenericValidator.isBlankOrNull(debitorName)) {
                    sequenceRuleNamesMap.put(numberId, ruleLabel);
                }
            } else {
                if (GenericValidator.isBlankOrNull(customerNumber)) {
                    customerNamesMap.put(addressId, customerName);
                }
            }

            if (GenericValidator.isBlankOrNull(bookkeepingAccount)) {
                if (GenericValidator.isBlankOrNull(bankAccountId)) {
                    invoiceNumbersMap.put(invoiceId, invoiceNumber);
                } else {
                    bankAccountNamesMap.put(bankAccountId, bankAccountDescription);
                }
            }
        }

        if (!customerNamesMap.isEmpty()) {
            errorDataResultMap.put("customerWithoutNumberList", customerNamesMap.values());
        }

        if (!sequenceRuleNamesMap.isEmpty()) {
            errorDataResultMap.put("sequenceRuleWithoutPartnerList", sequenceRuleNamesMap.values());
        }

        if (!bankAccountNamesMap.isEmpty()) {
            errorDataResultMap.put("bankAccountWithoutAccountList", bankAccountNamesMap.values());
        }

        if (!invoiceNumbersMap.isEmpty()) {
            errorDataResultMap.put("paymentWithoutBankAccountList", invoiceNumbersMap.values());
        }

        return errorDataResultMap;
    }

    public static List<Integer> getInvoicePaymentExportedIds(Integer startDate, Integer endDate, HttpServletRequest request) {
        List<Integer> result = new ArrayList<Integer>();

        List<Map> resultSetList = executeInvoiceCustomerPaymentExportList(startDate, endDate, null, request);

        for (Map resultSetMap : resultSetList) {
            Object paymentId = resultSetMap.get("paymentId");
            if (paymentId != null && paymentId.toString().length() > 0) {
                result.add(Integer.valueOf(paymentId.toString()));
            }
        }
        return result;
    }

}
