package com.piramide.elwis.utils;

import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author : ivan
 * @version : $Id ReportHelper ${time}
 */

public class ReportHelper {
    private static Log log = LogFactory.getLog(ReportHelper.class);

    public static String getOnlyOneResource(String value,
                                            String firstResourceValue,
                                            String secondResourceValue,
                                            String firstConstant,
                                            String secondConstant) {
        String result = "";
        if (value.equals(firstConstant)) {
            return firstResourceValue;
        }
        if (value.equals(secondConstant)) {
            return secondResourceValue;
        }
        return result;
    }

    public static String getDefault(String value, String defaultConstant, String resourceValue) {
        String result = "";
        if (defaultConstant.equals(value)) {
            return resourceValue;
        }
        return result;
    }

    public static String getRuleTypeResource(String value,
                                             String dailyValue,
                                             String weeklyValue,
                                             String monthlyValue,
                                             String yearlyValue,
                                             String dailyConstant,
                                             String weeklyConstant,
                                             String monthlyConstant,
                                             String yearlyConstant) {
        String result = "";
        if (value.equals(dailyConstant)) {
            return dailyValue;
        } else if (value.equals(weeklyConstant)) {
            return weeklyValue;
        } else if (value.equals(monthlyConstant)) {
            return monthlyValue;
        } else if (value.equals(yearlyConstant)) {
            return yearlyValue;
        }
        return result;
    }


    public static String getTaskUserStatus(String value,
                                           String progress,
                                           String notStarted,
                                           String concluded,
                                           String deferred,
                                           String check,
                                           String progressConstant,
                                           String notStartedConstant,
                                           String concludedConstant,
                                           String deferredConstant,
                                           String checkConstant) {
        String result = "";
        if (value.equals(progressConstant)) {
            return progress;
        } else if (value.equals(notStartedConstant)) {
            return notStarted;
        } else if (value.equals(concludedConstant)) {
            return concluded;
        } else if (value.equals(deferredConstant)) {
            return deferred;
        } else if (value.equals(checkConstant)) {
            return check;
        }

        return result;
    }

    public static String getPayMethodResource(String value,
                                              String simpleValue,
                                              String partialValue,
                                              String periodicValue,
                                              String simpleConstant,
                                              String partialConstant,
                                              String periodicConstant) {
        String result = "";
        if (value.equals(simpleConstant)) {
            return simpleValue;
        } else if (value.equals(partialConstant)) {
            return partialValue;
        } else if (value.equals(periodicConstant)) {
            return periodicValue;
        }
        return result;
    }

    public static String getMediaTypeResource(String value,
                                              String phoneValue,
                                              String meetingValue,
                                              String faxValue,
                                              String letterValue,
                                              String mailValue,
                                              String otherValue,
                                              String documentValue,
                                              String webDocumentValue,
                                              String phoneConstant,
                                              String meetingConstant,
                                              String faxConstant,
                                              String letterConstant,
                                              String mailConstant,
                                              String otherConstant,
                                              String documentConstant,
                                              String webDocumentConstant) {
        String result = "";
        if (value.equals(phoneConstant)) {
            return phoneValue;
        } else if (value.equals(meetingConstant)) {
            return meetingValue;
        } else if (value.equals(faxConstant)) {
            return faxValue;
        } else if (value.equals(letterConstant)) {
            return letterValue;
        } else if (value.equals(mailConstant)) {
            return mailValue;
        } else if (value.equals(otherConstant)) {
            return otherValue;
        } else if (value.equals(documentConstant)) {
            return documentValue;
        } else if (value.equals(webDocumentConstant)) {
            return webDocumentValue;
        }
        return result;
    }

    /**
     * Concatenate a string to a field
     *
     * @param value
     * @param string
     * @return null
     */
    public static String getConcatenatedString(String value, String string) {
        String result = "";
        if (value != null) {
            result = value + " " + string;
        }
        return (result);
    }

    public static Double getPonderatedValue(String value, String probability) {
        Double res = null;

        if (value != null && value.toString().length() > 0 &&
                probability != null && probability.toString().length() > 0) {
            double v = Double.parseDouble(value);
            double p = Double.parseDouble(probability);
            double div = Double.parseDouble("100");
            res = new Double((double) (v * p / div));
        }
        return (res);
    }

    public static String getGroupingDate(String date, String groupingPattern, String pattern, String locale) {
        String res = "";
        if (date != null && date.length() > 0) {
            Date date_var = DateUtils.formatDate(date, pattern);
            if (date_var != null) {
                res = DateUtils.parseDate(date_var, groupingPattern, new Locale(locale));
            }
        }
        return (res);
    }

    public static String getTelecomPhoneContacts(String contactId, String contactPersonId) {
        StringBuffer res = new StringBuffer();
        StringBuffer label = new StringBuffer();
        List resp = new ArrayList();

        if (contactPersonId != null && !"".equals(contactPersonId)) {
            res.append("select telecomnumber from telecom as telecom join telecomtype as telecomtype ")
                    .append(" on telecom.telecomtypeid = telecomtype.telecomtypeid ")
                    .append(" where telecom.predetermined='1' and telecomtype.type = 'PHONE' ")
                    .append(" and telecom.addressid=").append(contactId).append(" and telecom.contactpersonid=")
                    .append(contactPersonId).append(";");
        } else {
            res.append("select telecomnumber from telecom as telecom join telecomtype as telecomtype ")
                    .append(" on telecom.telecomtypeid = telecomtype.telecomtypeid ")
                    .append(" where telecom.predetermined='1' and telecomtype.type = 'PHONE' ")
                    .append(" and telecom.addressid=").append(contactId).append(" and telecom.contactpersonid is null;");
        }
        resp = QueryUtil.i.executeQuery(res.toString());
        Iterator ite = resp.iterator();
        label = new StringBuffer();
        while (ite.hasNext()) {
            Map item = (Map) ite.next();
            label.append(item.get("telecomnumber"));
            if (ite.hasNext()) {
                label.append(", ");
            }
        }
        return (label.toString());
    }

    public static String getTelecomEmailContacts(String contactId, String contactPersonId) {
        StringBuffer res = new StringBuffer();
        StringBuffer label = new StringBuffer();
        List resp = new ArrayList();
        if (contactPersonId != null && !"".equals(contactPersonId)) {
            res.append("select telecomnumber from telecom as telecom join telecomtype as telecomtype ")
                    .append(" on telecom.telecomtypeid = telecomtype.telecomtypeid ")
                    .append(" where telecom.predetermined='1' and telecomtype.type = 'EMAIL' ")
                    .append(" and telecom.addressid=").append(contactId).append(" and telecom.contactpersonid=")
                    .append(contactPersonId).append(";");
        } else {
            res.append("select telecomnumber from telecom as telecom join telecomtype as telecomtype ")
                    .append(" on telecom.telecomtypeid = telecomtype.telecomtypeid ")
                    .append(" where telecom.predetermined='1' and telecomtype.type = 'EMAIL' ")
                    .append(" and telecom.addressid=").append(contactId).append(" and telecom.contactpersonid is null;");
        }
        resp = QueryUtil.i.executeQuery(res.toString());
        Iterator ite = resp.iterator();
        label = new StringBuffer();

        while (ite.hasNext()) {
            Map item = (Map) ite.next();
            label.append(item.get("telecomnumber"));
            if (ite.hasNext()) {
                label.append(", ");
            }
        }

        return (label.toString());
    }

    public static BigDecimal getValueAsBigDecimal(Object value) {
        BigDecimal decimal = null;
        if (value != null) {
            try {
                decimal = new BigDecimal(String.valueOf(value));
            } catch (Exception e) {
                log.error("Error in create BigDecimal from:" + value + " " + e);
            }
        }
        return decimal;
    }

    /**
     * get negative BigDecimal, this is uesd from report template
     *
     * @param bigDecimalValue value
     * @return BigDecimal
     */
    public static BigDecimal getValueAsNegativeBigDecimal(String bigDecimalValue) {
        BigDecimal value = getValueAsBigDecimal(bigDecimalValue);
        if (value != null) {
            value = value.multiply(BigDecimal.valueOf(-1));
        }
        return value;
    }

    /**
     * Get the status label of a project
     *
     * @param status        The status
     * @param enteredLabel  The "entered" label
     * @param openedLabel   The "opened" label
     * @param closedLabel   The "closed" label
     * @param finishedLabel The "finished" label
     * @param invoicedlabel The "invoiced" label
     * @return the status label
     */
    public static String getProjectStatusLabel(String status, String enteredLabel,
                                               String openedLabel,
                                               String closedLabel,
                                               String finishedLabel,
                                               String invoicedlabel) {
        int statusValue = Integer.valueOf(String.valueOf(status));
        String res = "";
        if (statusValue == ProjectConstants.ProjectStatus.ENTERED.getValue()) {
            res = enteredLabel;
        } else if (statusValue == ProjectConstants.ProjectStatus.OPENED.getValue()) {
            res = openedLabel;
        } else if (statusValue == ProjectConstants.ProjectStatus.CLOSED.getValue()) {
            res = closedLabel;
        } else if (statusValue == ProjectConstants.ProjectStatus.FINISHED.getValue()) {
            res = finishedLabel;
        } else {
            res = invoicedlabel;
        }
        return (res);
    }

    /**
     * Get the resource for the project time status
     *
     * @param status            ProjectTime status
     * @param enteredLabel      Entered label
     * @param releasedLabel     Released label
     * @param confirmedLabel    confirmed label
     * @param notConfirmedLabel Not confirmed label
     * @param invoicedLabel     Invoiced label
     * @return
     */
    public static String getProjectTimeStatus(String status, String enteredLabel,
                                              String releasedLabel,
                                              String confirmedLabel,
                                              String notConfirmedLabel,
                                              String invoicedLabel) {
        String res = "";
        int statusValue = Integer.valueOf(String.valueOf(status));
        if (statusValue == ProjectConstants.ProjectTimeStatus.ENTERED.getValue()) {
            res = enteredLabel;
        } else if (statusValue == ProjectConstants.ProjectTimeStatus.RELEASED.getValue()) {
            res = releasedLabel;
        } else if (statusValue == ProjectConstants.ProjectTimeStatus.CONFIRMED.getValue()) {
            res = confirmedLabel;
        } else if (statusValue == ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getValue()) {
            res = notConfirmedLabel;
        } else if (statusValue == ProjectConstants.ProjectTimeStatus.INVOICED.getValue()) {
            res = invoicedLabel;
        }
        return (res);
    }

    /**
     * Substract two decimal amounts
     *
     * @param firstAmount  firstAmount
     * @param secondAmount secondAmount
     * @return The substraction. If one is null, then null
     */
    public static Double substractDecimalAmounts(Object firstAmount, Object secondAmount) {
        Double result = null;
        if (firstAmount != null && firstAmount.toString().length() > 0 &&
                secondAmount != null && secondAmount.toString().length() > 0) {
            result = new Double(firstAmount.toString()) - (new Double(secondAmount.toString()));
        }
        return result;
    }

    public static String getAsString(String text) {
        String result = text;
        if (text != null) {
            result = new String(text.getBytes());
        }
        return (result);
    }

    public static Double getAsDouble(Object text) {
        Double result = null;
        if (text != null) {
            try {
                result = new Double(text.toString());
            } catch (Exception e) {
                log.debug("Error in parse to Double.." + text + "- " + e);
            }
        }
        return (result);
    }

    /**
     * Get double value if conditions is true
     * @param value value
     * @param variableCondition condition value
     * @param staticCondition condition value
     * @return Double
     */
    public static Double getDoubleValueByCondition(String value, String variableCondition, String staticCondition) {
        Double result = null;
        if (variableCondition != null && variableCondition.equals(staticCondition)) {
            result = getAsDouble(value);
        }
        return result;
    }

    public static String getProductTypeNameByCondition(String typeName, String productTypeType, String eventTypeLabel) {
        String productTypeName = typeName;

        if (ProductConstants.ProductTypeType.EVENT.equal(productTypeType)) {
            productTypeName = eventTypeLabel.replace("(", "[");
            productTypeName = productTypeName.replace(")", "]");
        }
        return productTypeName;
    }

    public static String getCompanyTypeLabel(String type, String regularLabel, String trialLabel, String demoLabel) {
        String res = "";

        if (AdminConstants.CompanyType.REGULAR.equal(type)) {
            res = regularLabel;
        } else if (AdminConstants.CompanyType.TRIAL.equal(type)) {
            res = trialLabel;
        } else if (AdminConstants.CompanyType.DEMO.equal(type)) {
            res = demoLabel;
        }
        return res;
    }

}


