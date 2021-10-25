package com.piramide.elwis.web.common.util;

import com.piramide.elwis.cmd.catalogmanager.CategoryCmd;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.webmail.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This utility class allows to use some functions in JSP pages and Web Tier Classes.
 *
 * @author Fernando
 * @version $Id: JSPHelper.java 12701 2017-07-20 22:21:17Z miguel $
 */

public class JSPHelper {

    private static Log log = LogFactory.getLog(JSPHelper.class);

    private static MessageResources messages = PropertyMessageResources.getMessageResources(Constants.APPLICATION_RESOURCES);

    /**
     * Return a internationalized list of Constants Telecomtypes for this application.
     * The value returned is used to fill a HTML select in JSP page.
     *
     * @param request the page request
     * @return the internationalied arraylist of org.apache.struts.util.LabelValueBean objects.
     */

    public static ArrayList getContactTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        log.debug("Locale is = " + locale);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact.customer"), Integer.toString(CodeUtil.customer)));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact.supplier"), Integer.toString(CodeUtil.supplier)));

        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * Return Language i18n names
     */

    public static ArrayList getLanguageList(HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        log.debug("Locale is = " + locale);

        return getLanguageList(locale);
    }

    /**
     * Login languages list
     */
    public static ArrayList getLanguageList(Locale locale) {
        ArrayList list = new ArrayList();
        log.debug("Locale is = " + locale);
        for (Iterator iterator = SystemLanguage.systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String iso = (String) iterator.next();
            list.add(new LabelValueBean(JSPHelper.getMessage(locale, (String) SystemLanguage.systemLanguages.get(iso)),
                    iso));
        }
        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * Return a i18n message for key and locale respective
     *
     * @param locale the locale
     * @param key    the key
     * @return String
     */
    public static String getMessage(Locale locale, String key) {

        String value = messages.getMessage(locale, key);
        if (value != null) {
            return value.trim();
        } else {
            return "???" + key + "???";
        }
    }

    /**
     * Return a i18n message for key and locale respective
     *
     * @param locale the locale
     * @param key    the key
     * @param param0 the param0 for the resource
     * @return String  the message
     */
    public static String getMessage(Locale locale, String key, Object param0) {
        String value = messages.getMessage(locale, key, param0);
        if (value != null) {
            return value.trim();
        } else {
            return "???" + key + "???";
        }
    }

    /**
     * Return a internationalized list of Constants CategoryValues for this application.
     * The value returned is used to fill a HTML select in JSP page.
     *
     * @param request the page HttpServletRequest .
     * @return the internationalied arraylist of org.apache.struts.util.LabelValueBean objects.
     */

    public static ArrayList getCategoryTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        log.debug("Locale is = " + locale);

        for (Iterator iterator = CategoryTypes.categorytypes.keySet().iterator(); iterator.hasNext();) {
            String category = (String) iterator.next();
            list.add(new LabelValueBean(JSPHelper.getMessage(locale, (String) CategoryTypes.categorytypes.get(category)),
                    category));
        }
        return SortUtils.orderByProperty(list, "label");
    }


    /**
     * Return a internationalized list of Constants StatusValues(Mailing) for this application.
     * The value returned is used to fill a HTML select in JSP page.
     *
     * @return the internationalied arraylist of org.apache.struts.util.LabelValueBean objects.
     */


    public static ArrayList getStatusList(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.preparation"),
                CampaignConstants.PREPARATION));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.sent"),
                CampaignConstants.SENT)); //2
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.cancel"),
                CampaignConstants.CANCELED)); //3

        return list;
    }


    public static ArrayList getPriorityStatusList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.notInit"),
                SchedulerConstants.NOTSTARTED)); //2
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.Deferred"),
                SchedulerConstants.DEFERRED));//4
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.InProgress"),
                SchedulerConstants.INPROGRESS));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.ToCheck"),
                SchedulerConstants.CHECK)); //5
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.Task.Concluded"),
                SchedulerConstants.CONCLUDED)); //3
        return list; //this list does not need to do sorted.
    }

    public static List getPriorityStatusListForTaskComponent(Object obj) {
        HttpServletRequest request = (HttpServletRequest) obj;
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.notInit"),
                SchedulerConstants.NOTSTARTED)); //2
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.Deferred"),
                SchedulerConstants.DEFERRED));//4
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.InProgress"),
                SchedulerConstants.INPROGRESS));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.ToCheck"),
                SchedulerConstants.CHECK)); //5
        return list; //this list does not need to do sorted.
    }


    /**
     * Return a internationalized list of Pay Method for this application.
     * The value returned is used to fill a HTML select in JSP page.
     *
     * @param request the page request
     * @return the internationalied arraylist of org.apache.struts.util.LabelValueBean objects.
     */
    public static List getPayMethodList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payMethod.single"), ContactPayment.SINGLE));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payMethod.partial"), ContactPayment.PARTIAL));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payMethod.periodic"), ContactPayment.PERIODIC));
        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * Return a internationalized list of Pay Method for this application.
     * The value returned is used to fill a HTML select in JSP page.
     *
     * @param request the page request
     * @return the internationalied arraylist of org.apache.struts.util.LabelValueBean objects.
     */
    public static ArrayList getPayPeriodList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payPeriod.monthly"), ContactPayment.MONTHLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payPeriod.quarter"), ContactPayment.QUARTER));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payPeriod.semiyearly"), ContactPayment.SEMIYEARLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "contact.payment.payPeriod.yearly"), ContactPayment.YEARLY));
        return list;
    }

    public static Locale getLocale(HttpServletRequest request) {
        return (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
    }

    /**
     * dd
     * Return a  i18n message obtaining of request locale
     *
     * @param request the page request
     * @param key     the key
     * @return the string value for key
     */
    public static String getMessage(HttpServletRequest request, String key) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        String value = messages.getMessage(locale, key);
        if (value != null) {
            return value.trim();
        } else {
            return "???" + key + "???";
        }
    }

    /**
     * dd
     * Return a  i18n message obtaining of request locale
     *
     * @param request the page request
     * @param key     the key
     * @param paramO  param
     * @return the string value for key
     */
    public static String getMessage(HttpServletRequest request, String key, Object paramO) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        String value = messages.getMessage(locale, key, paramO);
        if (value != null) {
            return value.trim();
        } else {
            return "???" + key + "???";
        }
    }

    /**
     * Return a  i18n message obtaining of request locale
     *
     * @param request the page request
     * @param key     the key
     * @param params  params[]
     * @return the string value for key
     */
    public static String getMessage(HttpServletRequest request, String key, Object params[]) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        String value = messages.getMessage(locale, key, params);
        if (value != null) {
            return value.trim();
        } else {
            return "???" + key + "???";
        }
    }

    /**
     * Chop an String to given maxlength and add to end of string ... if maxleght is overloaded.
     *
     * @param fullValue the string to chop
     * @param maxLength max length for compare and chop
     * @return the chopped string
     */
    public static String chopString(String fullValue, int maxLength) {

        String choppedValue = null;
        boolean isChopped = false;

        // trim the string if a maxLength or maxWords is defined
        if (maxLength > 0 && fullValue.length() > maxLength) {
            choppedValue = fullValue.substring(0, maxLength - 3) + "...";
            isChopped = true;
        }

        if (isChopped) {
            return choppedValue;
        } else {
            return fullValue;
        }

    }

    public static ArrayList getActiveList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "AdvancedSearch.active"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact.inactive"), "0"));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getTrialList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.yes"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.no"), "0"));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getAddressTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact.person"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact.organization"), "0"));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getCampaignWithoutContactPersonList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.onlyContactPerson"), "1"));
        list.add(new LabelValueBean("", "2"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.onlyWithContactPerson"), "0"));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getCampaignOnlyCompaniesList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.onlyPersons"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Campaign.onlyCompanies"), "0"));
        list.add(new LabelValueBean(" ", "2"));
        return SortUtils.orderByProperty(list, "label");
    }
/*for webMail*/

    public static ArrayList getSearchTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Contact"), "contact"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "ContactPerson"), "contactPerson"));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getCriteriaNumberTypeOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.equal"), "EQUAL"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.lessThan"), "LESS"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.greaterThan"), "GREATER"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.notEqual"), "DISTINCT"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.between"), "BETWEEN"));

        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getCriteriaSelectTypeOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.equal"), "EQUAL"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.oneOf"), "IN"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.notEqual"), "DISTINCT"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.notOneOf"), "NOTIN"));

        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getCriteriaTextTypeOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.contain"), "CONTAIN"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.equal"), "EQUAL"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.notEqual"), "DISTINCT"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.NOT_CONTAIN.getResource()), CampaignConstants.CriteriaComparator.NOT_CONTAIN.getConstant()));

        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getContactTypeOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.equal"), "ANDBIT"));

        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getProductInUseOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.filter.op.equal"), "EQUAL"));

        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getFieldRelationExistsOperatorList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.RELATION_EXISTS.getResource()), CampaignConstants.CriteriaComparator.RELATION_EXISTS.getConstant()));
        return SortUtils.orderByProperty(list, "label");
    }

    public static String getCriteriaOperator(String operator, HttpServletRequest request) {
        StringBuffer response = new StringBuffer();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        log.debug("operator: " + operator);

        if ("EQUAL".equals(operator) || "ANDBIT".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.equal"));
        } else if ("LESS".equals(operator) || "LESS_EQUAL".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.lessThan"));
        } else if ("GREATER".equals(operator) || "GREATER_EQUAL".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.greaterThan"));
        } else if ("DISTINCT".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.notEqual"));
        } else if ("BETWEEN".equals(operator) || "BETWEEN0".equals(operator) || "BETWEEN1".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Common.between"));
        } else if ("IN".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.oneOf"));
        } else if ("CONTAIN".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.contain"));
        } else if (CampaignConstants.CriteriaComparator.NOT_CONTAIN.getConstant().equals(operator)) {
            response.append(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.NOT_CONTAIN.getResource()));
        } else if ("NOTIN".equals(operator)) {
            response.append(JSPHelper.getMessage(locale, "Report.filter.op.notOneOf"));
        } else if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(operator)) {
            response.append(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.RELATION_EXISTS.getResource()));
        }

        return response.toString();
    }

    /**
     * parse the string that contains CampaignCriterionSimple values
     *
     * @param value the string to parsing
     * @return list (operator, value) or (opetator, value1, values2)
     */
    public static List getCriteriaValues(String value) {
        List result = null;
        StringBuffer buffer = new StringBuffer(value);


        int separatorType = buffer.lastIndexOf(":");
        String type = buffer.substring(0, separatorType);
        int separatorCategory = buffer.lastIndexOf("|");

        if ("single".equals(type)) {
            result = new ArrayList(2);
            int separator = buffer.lastIndexOf("#");
            String operator = buffer.substring(separatorCategory + 1, separator);

            String singleValue = buffer.substring(separator + 1, buffer.length());
            result.add(operator);
            result.add(singleValue);
        }
        if ("range".equals(type)) {
            result = new ArrayList(3);
            int separatorRangeValue1 = buffer.indexOf("%");
            int separatorOperator1 = buffer.lastIndexOf("&");
            String rangeFrom = buffer.substring(separatorCategory + 1, separatorRangeValue1);
            String operator1 = buffer.substring(separatorRangeValue1 + 1, separatorOperator1); // get operator
            String rangeTo = buffer.substring(separatorOperator1 + 1, buffer.length());

            result.add(operator1);
            result.add(rangeFrom);
            result.add(rangeTo);
        }
        return result;
    }

    public static String putSimpleCriteriaValueDate(String operator, String value) {

        return "single:category|" + operator + "#" + value;
    }

    public static String putSimpleRangeCriteriaValueDate(String operator, String from, String to) {

        return "range:category|" + from + "%" + operator + "&" + to;
    }

    public static List defaultProbabilities() {
        List list = new LinkedList();
        list.add(new LabelValueBean("0", "0"));
        list.add(new LabelValueBean("10", "10"));
        list.add(new LabelValueBean("20", "20"));
        list.add(new LabelValueBean("30", "30"));
        list.add(new LabelValueBean("40", "40"));
        list.add(new LabelValueBean("50", "50"));
        list.add(new LabelValueBean("60", "60"));
        list.add(new LabelValueBean("70", "70"));
        list.add(new LabelValueBean("80", "80"));
        list.add(new LabelValueBean("90", "90"));
        list.add(new LabelValueBean("100", "100"));
        return list;
    }

    public static List defaultCommunicationTypes(javax.servlet.ServletRequest servletRequest) {
        ArrayList list = new ArrayList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.fax"), CommunicationTypes.FAX));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.letter"), CommunicationTypes.LETTER));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.phone"), CommunicationTypes.PHONE));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.meeting"), CommunicationTypes.MEETING));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.other"), CommunicationTypes.OTHER));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.email"), CommunicationTypes.EMAIL));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.document"), CommunicationTypes.DOCUMENT));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Communication.type.webDocument"), CommunicationTypes.WEB_DOCUMENT));

        return SortUtils.orderByProperty(list, "label");
    }

    public static List defaultMediaTypes(javax.servlet.ServletRequest servletRequest) {
        List list = defaultCommunicationTypes(servletRequest);

        for (int i = 0; i < list.size(); i++) {
            LabelValueBean labelValueBean = (LabelValueBean) list.get(i);
            if (CommunicationTypes.WEB_DOCUMENT.equals(labelValueBean.getValue())) {
                list.remove(i);
            }
        }
        return list;
    }

    public static List actionDiscountTypes(javax.servlet.ServletRequest servletRequest) {
        ArrayList list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "SalesProcessAction.percent_discount"), SalesConstants.DISCOUNT_PERCENT.toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "SalesProcessAction.amount_discount"), SalesConstants.DISCOUNT_AMOUNT.toString()));
        return SortUtils.orderByProperty(list, "label");
    }

    public static String getTelecomType(String constant, HttpServletRequest request) {
        List listOfTelecomTypes = JSPHelper.getTelecomTypeTypes(request);
        String result = "";

        for (int i = 0; i < listOfTelecomTypes.size(); i++) {
            LabelValueBean item = (LabelValueBean) listOfTelecomTypes.get(i);
            String value = item.getValue();
            if (value.equals(constant)) {
                result = item.getLabel();
            }
        }
        return result;
    }

    public static List getTelecomTypeTypes(HttpServletRequest request) {
        List result = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "TelecomType.type.phone"), TelecomType.PHONE_TYPE));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "TelecomType.type.email"), TelecomType.EMAIL_TYPE));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "TelecomType.type.fax"), TelecomType.FAX_TYPE));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "TelecomType.type.other"), TelecomType.OTHER_TYPE));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "TelecomType.type.link"), TelecomType.LINK_TYPE));

        return SortUtils.orderByProperty((ArrayList) result, "label");
    }

    public static String getSystemLanguage(String constant, HttpServletRequest request) {
        List listOfSystemLanguages = JSPHelper.getLanguageList(request);
        String result = "";
        for (int i = 0; i < listOfSystemLanguages.size(); i++) {
            LabelValueBean labelValueBean = (LabelValueBean) listOfSystemLanguages.get(i);
            String value = labelValueBean.getValue();
            if (value.equals(constant)) {
                result = labelValueBean.getLabel();
            }
        }
        return result;
    }

    public static List getMessageSearchConstantsForWebmail(HttpServletRequest request) {
        List result = new ArrayList();

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Mail.from"), WebMailConstants.SEARCH_ONLY_FROM));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.search.entireMessage"), WebMailConstants.SEARCH_ALL_MESSAGE));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Mail.subject"), WebMailConstants.SEARCH_ONLY_SUBJECT));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Mail.to"), WebMailConstants.SEARCH_ONLY_TO));
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Mail.body"), WebMailConstants.SEARCH_ONLY_CONTENT));

        return result;
    }

    public static List getFolderSearchConstantsForWebmail(HttpServletRequest request) {
        List result = new ArrayList();

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        result.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.search.allFolders"), WebMailConstants.SEARCH_ALL_FOLDERS));
        return result;
    }

    /**
     * Compose the body mail header for a reply or a forward
     *
     * @param request
     * @param from
     * @param to
     * @param subject
     * @param isHtml
     * @return a string
     */
    public static String createRedirectHeader(HttpServletRequest request, String from, String to, String cc, String subject, String sentDate,
                                              boolean isHtml) {
        String res = "";

        String originalMessageLabel = "==========" + JSPHelper.getMessage(request, "Mail.originalMessage") + "==========";
        String fromLabel = "\n" + JSPHelper.getMessage(request, "Mail.from") + ": ";
        String toLabel = "\n" + JSPHelper.getMessage(request, "Mail.to") + ": ";
        String ccLabel = "\n" + JSPHelper.getMessage(request, "Mail.Cc") + ": ";
        String subjectLabel = "\n" + JSPHelper.getMessage(request, "Mail.subject") + ": ";
        String dateLabel = "\n" + JSPHelper.getMessage(request, "Mail.date") + ": ";
        String mailDate = "";

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        String localeStr = user.getValue("locale").toString();
        String pattern = JSPHelper.getMessage(new Locale(localeStr), "Webmail.mail.dateTimePatternTimeZone");
        if (sentDate != null) {
            mailDate = Functions.getFormattedDateTimeWithTimeZoneAndLocale(sentDate, dateTimeZone, pattern, localeStr);
        }

        String separator = "\n\n ";
        res = originalMessageLabel + fromLabel + from + toLabel + to;
        if (cc != null && cc.length() > 0) {
            res += ccLabel + cc;
        }
        res += subjectLabel + subject + dateLabel + mailDate + separator;
        if (isHtml) {
            Pattern strMatch = Pattern.compile("\n");
            Matcher m = strMatch.matcher(res);
            res = m.replaceAll("<br>");
        }

        return (res);
    }

    public static ArrayList getMarkAsList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.read"), WebMailConstants.MarkAs.READ.getConstant()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.unRead"), WebMailConstants.MarkAs.UNREAD.getConstant()));
        return SortUtils.orderByProperty(list, "label");
    }

    public static ArrayList getMailTrayFilterList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.all"), WebMailConstants.MAIL_FILTER_ALL));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.unRead"), WebMailConstants.MAIL_FILTER_UNREAD));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.highPriority"), WebMailConstants.MAIL_FILTER_HIGHPRIORITY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.notAnswered"), WebMailConstants.MAIL_FILTER_NOTANSWERED));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.answered"), WebMailConstants.MAIL_FILTER_ANSWERED));
        return list;
    }

    public static String getName_Email(HashMap hm) {
        String res = "";
        Object name = hm.get("name");
        Object email = hm.get("email");
        if (name != null && name.toString().length() > 0 && (email != null && !name.equals(email))) {
            res = "\"" + name + "\" <" + email + ">";
        } else {
            res = email.toString();
        }
        return (res);
    }

    public static String getName_Emails(ArrayList data) {
        String res = "";
        for (int i = 0; i < data.size(); i++) {
            HashMap hm = new HashMap();
            hm = (HashMap) data.get(i);
            res += getName_Email(hm);
            if (i < data.size() - 1) {
                res += ", ";
            }
        }
        return (res);
    }

    public static String replaceBracketsByHtml(String str) {
        String res = "";
        res = str.replaceAll("[<]", "&lt;");
        res = res.replaceAll("[>]", "&gt;");
        return (res);
    }

    /*Sheduler .....*/

    public static ArrayList getDaysWeekList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.monday"), SchedulerConstants.MONDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.tuesday"), SchedulerConstants.TUESDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.wednesday"), SchedulerConstants.WEDNESDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.thursday"), SchedulerConstants.THURSDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.friday"), SchedulerConstants.FRIDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.saturday"), SchedulerConstants.SATURDAY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Day.sunday"), SchedulerConstants.SUNDAY));
        return list;
    }

    public static ArrayList getNumberWeekList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.1"), new Integer(SchedulerConstants.MONTHLY_OCCUR_1ST).toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.2"), new Integer(SchedulerConstants.MONTHLY_OCCUR_2ST).toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.3"), new Integer(SchedulerConstants.MONTHLY_OCCUR_3RD).toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.4"), new Integer(SchedulerConstants.MONTHLY_OCCUR_4TH).toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.5"), new Integer(SchedulerConstants.MONTHLY_OCCUR_5TH).toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.last"), new Integer(SchedulerConstants.MONTHLY_OCCUR_LAST).toString()));
        return list;
    }

    public static String[] getOnlyNumberWeekList(HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        return new String[]{JSPHelper.getMessage(locale, "Appointment.NumberDay.1"),
                JSPHelper.getMessage(locale, "Appointment.NumberDay.2"),
                JSPHelper.getMessage(locale, "Appointment.NumberDay.3"),
                JSPHelper.getMessage(locale, "Appointment.NumberDay.4"),
                JSPHelper.getMessage(locale, "Appointment.NumberDay.5"),
                JSPHelper.getMessage(locale, "Appointment.NumberDay.last")};
    }


    public static String[] getMonthDays(HttpServletRequest request) {
        String[] days = new String[31];
        for (int i = 0; i <= 30; i++) {
            days[i] = Integer.toString(i + 1);
        }
        return days;
    }

    public static ArrayList getHolidayTypes(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "holiday.byDate"), "0"));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "holiday.byOccurrence"), "1"));
        return list;
    }

    public static ArrayList getNumberDayList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.1"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.2"), "2"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.3"), "3"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.4"), "4"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.5"), "5"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.6"), "6"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.7"), "7"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.8"), "8"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.9"), "9"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.10"), "10"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.11"), "11"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.12"), "12"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.13"), "13"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.14"), "14"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.15"), "15"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.16"), "16"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.17"), "17"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.18"), "18"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.19"), "19"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.20"), "20"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.21"), "21"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.22"), "22"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.23"), "23"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.24"), "24"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.25"), "25"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.26"), "26"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.27"), "27"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.28"), "28"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.29"), "29"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.30"), "30"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.NumberDay.31"), "31"));
        return list;
    }

    public static ArrayList getMonthYearList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.january"), "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.february"), "2"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.march"), "3"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.april"), "4"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.may"), "5"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.june"), "6"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.july"), "7"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.august"), "8"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.september"), "9"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.october"), "10"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.november"), "11"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.Month.december"), "12"));
        return list;
    }

    public static List getMonthNames(HttpServletRequest request) {
        ArrayList list = new ArrayList(12);
        String[] months = getOnlyMonthNames(request);
        for (int i = 0; i < months.length - 1; i++) {
            list.add(new LabelValueBean(months[i], Integer.toString(i + 1)));
        }
        return list;
    }

    public static String[] getOnlyMonthNames(HttpServletRequest request) {
        ArrayList list = new ArrayList(12);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);

        return dateFormatSymbols.getMonths();
    }

    public static List getDayNames(HttpServletRequest request, boolean beginInMonday) {
        ArrayList list = new ArrayList(12);
        String[] dayNames = getOnlyDayNames(request, beginInMonday);
        for (int i = 0; i < dayNames.length; i++) {
            list.add(new LabelValueBean(dayNames[i], Integer.toString(i + 1)));
        }
        return list;
    }

    public static String[] getOnlyDayNames(HttpServletRequest request, boolean beginInMonday) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);

        String[] weekNames = dateFormatSymbols.getWeekdays();
        String[] dayNames = new String[7];

        if (beginInMonday) {
            System.arraycopy(weekNames, 2, dayNames, 0, 6);
            dayNames[6] = weekNames[1];//Sunday push in last day
        } else {
            System.arraycopy(weekNames, 1, dayNames, 0, 7);
        }
        return dayNames;
    }

    public static ArrayList getReminderTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.minutes"), SchedulerConstants.REMINDER_TYPE_MINUTES));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.hours"), SchedulerConstants.REMINDER_TYPE_HOURS));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.day"), SchedulerConstants.REMINDER_TYPE_DAYS));
        return list;
    }

    /**
     * Method that returns scheduler DayFragment constants
     *
     * @param request
     * @return a list
     */
    public static List getDayFragmentationOptions(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.dayFragmentation.15Min"),
                SchedulerConstants.DAY_FRAGMENTATION_15MIN));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.dayFragmentation.30Min"),
                SchedulerConstants.DAY_FRAGMENTATION_30MIN));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.dayFragmentation.45Min"),
                SchedulerConstants.DAY_FRAGMENTATION_45MIN));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.dayFragmentation.1Hour"),
                SchedulerConstants.DAY_FRAGMENTATION_1H));

        return list;
    }

    /**
     * Method that returns a list in which are the hours of a day (0h... 23h)
     *
     * @return list that contain (hora, valor)
     */
    public static List getDayOfWorkOptions() {
        ArrayList list = new ArrayList();
        int initialValue = 0;
        int finalValue = 24;

        for (int i = initialValue; i < finalValue; i++) {
            String cad = String.valueOf(i) + ":00";
            if (i < 10) {
                cad = "0" + String.valueOf(i) + ":00";
            }

            list.add(new LabelValueBean(cad, String.valueOf(i)));
        }

        return list;
    }

    public static List getEndDayOfWorkOptions() {
        ArrayList list = new ArrayList();
        int initialValue = 0;
        int finalValue = 24;

        for (int i = initialValue; i <= finalValue; i++) {
            String cad = String.valueOf(i) + ":00";
            if (i < 10) {
                cad = "0" + String.valueOf(i) + ":00";
            }

            list.add(new LabelValueBean(cad, String.valueOf(i)));
        }

        return list;
    }

    public static List getCalendarDefaultViewOptions(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.daily"),
                SchedulerConstants.RECURRENCE_DAILY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.weekly"),
                SchedulerConstants.RECURRENCE_WEEKLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.monthly"),
                SchedulerConstants.RECURRENCE_MONTHLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.yearly"),
                SchedulerConstants.RECURRENCE_YEARLY));
        return list;
    }

    public static List getHours() {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean("00", "0"));
        list.add(new LabelValueBean("01", "1"));
        list.add(new LabelValueBean("02", "2"));
        list.add(new LabelValueBean("03", "3"));
        list.add(new LabelValueBean("04", "4"));
        list.add(new LabelValueBean("05", "5"));
        list.add(new LabelValueBean("06", "6"));
        list.add(new LabelValueBean("07", "7"));
        list.add(new LabelValueBean("08", "8"));
        list.add(new LabelValueBean("09", "9"));
        list.add(new LabelValueBean("10", "10"));
        list.add(new LabelValueBean("11", "11"));
        list.add(new LabelValueBean("12", "12"));
        list.add(new LabelValueBean("13", "13"));
        list.add(new LabelValueBean("14", "14"));
        list.add(new LabelValueBean("15", "15"));
        list.add(new LabelValueBean("16", "16"));
        list.add(new LabelValueBean("17", "17"));
        list.add(new LabelValueBean("18", "18"));
        list.add(new LabelValueBean("19", "19"));
        list.add(new LabelValueBean("20", "20"));
        list.add(new LabelValueBean("21", "21"));
        list.add(new LabelValueBean("22", "22"));
        list.add(new LabelValueBean("23", "23"));
        return list;
    }

    public static List getIntervalMin() {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean("00", "00"));
        list.add(new LabelValueBean("15", "15"));
        list.add(new LabelValueBean("30", "30"));
        list.add(new LabelValueBean("45", "45"));
        return list;
    }

    public static List getInterval5Minutes() {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean("00", "0"));
        list.add(new LabelValueBean("05", "5"));
        list.add(new LabelValueBean("10", "10"));
        list.add(new LabelValueBean("15", "15"));
        list.add(new LabelValueBean("20", "20"));
        list.add(new LabelValueBean("25", "25"));
        list.add(new LabelValueBean("30", "30"));
        list.add(new LabelValueBean("35", "35"));
        list.add(new LabelValueBean("40", "40"));
        list.add(new LabelValueBean("45", "45"));
        list.add(new LabelValueBean("50", "50"));
        list.add(new LabelValueBean("55", "55"));
        return list;
    }

    public static List getTimeBefore() {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean("05", "05"));
        list.add(new LabelValueBean("15", "15"));
        list.add(new LabelValueBean("30", "30"));
        list.add(new LabelValueBean("45", "45"));
        return list;
    }

    public static List getMinutes() {
        ArrayList list = new ArrayList();

        for (int i = 0; i < 60; i++) {
            String value = String.valueOf(i);
            String label = String.valueOf(i);
            if (i < 10) {
                label = "0" + label;
            }
            list.add(new LabelValueBean(label, value));
        }
        return list;
    }

    public static List getReturnList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnDay"),
                SchedulerConstants.RETURN_DAY));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnWeek"),
                SchedulerConstants.RETURN_WEEK));// 2
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnMonth"),
                SchedulerConstants.RETURN_MONTH));//3
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnYear"),
                SchedulerConstants.RETURN_YEAR));// 4
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnSearchList"),
                SchedulerConstants.RETURN_SEARCHLIST));// 5
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnAdvancedSearchList"),
                SchedulerConstants.RETURN_ADVANCED_SEARCHLIST));//  6
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnParticipantList"),
                SchedulerConstants.RETURN_TAB_PARTICIPANTS));//7
        return list;
    }

    public static List getReturnDeleteList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnDay"),
                SchedulerConstants.RETURN_DAY));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnWeek"),
                SchedulerConstants.RETURN_WEEK));// 2
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnMonth"),
                SchedulerConstants.RETURN_MONTH));//3
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnYear"),
                SchedulerConstants.RETURN_YEAR));// 4
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnSearchList"),
                SchedulerConstants.RETURN_SEARCHLIST));// 5
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Appointment.returnAdvancedSearchList"),
                SchedulerConstants.RETURN_ADVANCED_SEARCHLIST));//  6
        return list;
    }

    /**
     * setting the system folder names  by type in arrayList
     *
     * @param request
     * @return list that contains type of folder and name
     */
    public static List getSystemFolderNames(HttpServletRequest request) {
        List folderNames = new ArrayList();
        HashMap inbox = new HashMap();
        HashMap draftItems = new HashMap();
        HashMap sendItems = new HashMap();
        HashMap trash = new HashMap();
        HashMap outbox = new HashMap();

        inbox.put("type", WebMailConstants.FOLDER_INBOX);
        inbox.put("name", JSPHelper.getMessage(request, "Webmail.folder.inbox"));
        draftItems.put("type", WebMailConstants.FOLDER_DRAFTITEMS);
        draftItems.put("name", JSPHelper.getMessage(request, "Webmail.folder.draftItems"));
        sendItems.put("type", WebMailConstants.FOLDER_SENDITEMS);
        sendItems.put("name", JSPHelper.getMessage(request, "Webmail.folder.sendItems"));
        trash.put("type", WebMailConstants.FOLDER_TRASH);
        trash.put("name", JSPHelper.getMessage(request, "Webmail.folder.trash"));
        outbox.put("type", WebMailConstants.FOLDER_OUTBOX);
        outbox.put("name", JSPHelper.getMessage(request, "Webmail.folder.outbox"));

        folderNames.add(inbox);
        folderNames.add(draftItems);
        folderNames.add(sendItems);
        folderNames.add(trash);
        folderNames.add(outbox);

        return folderNames;
    }

    /*      Type user internal or external ( employees, address )*/

    public static ArrayList getUserTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "User.intenalUser"), AdminConstants.INTERNAL_USER));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "User.externalUser"), AdminConstants.EXTERNAL_USER));//0
        return list;
    }

    public static ArrayList getProductInUserList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "product.inUse"), AdminConstants.INTERNAL_USER));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "product.notInUse"), AdminConstants.EXTERNAL_USER));//0
        return list;
    }

    public static String getActionHistoryValue(HttpServletRequest request, String id) {
        String value = "";
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if (id.equals(SupportConstants.CREATE_ARTICLE))    //0
        {
            value = JSPHelper.getMessage(locale, "History.createArticle");
        } else if (id.equals(SupportConstants.UPDATE_ARTICLE)) //  1
        {
            value = JSPHelper.getMessage(locale, "History.updateArticle");
        } else if (id.equals(SupportConstants.CREATE_LINK))  //  3
        {
            value = JSPHelper.getMessage(locale, "History.createLink");
        } else if (id.equals(SupportConstants.UPDATE_LINK)) //  4
        {
            value = JSPHelper.getMessage(locale, "History.updateLink");
        } else if (id.equals(SupportConstants.DELETE_LINK))  // 5
        {
            value = JSPHelper.getMessage(locale, "History.deleteLink");
        } else if (id.equals(SupportConstants.CREATE_ATTACH)) // 6
        {
            value = JSPHelper.getMessage(locale, "History.createAttach");
        } else if (id.equals(SupportConstants.UPDATE_ATTACH))  // 7
        {
            value = JSPHelper.getMessage(locale, "History.updateAttach");
        } else if (id.equals(SupportConstants.DELETE_ATTACH))   //8
        {
            value = JSPHelper.getMessage(locale, "History.deleteAttach");
        } else if (id.equals(SupportConstants.CREATE_COMMENT)) //  12
        {
            value = JSPHelper.getMessage(locale, "History.createComment");
        } else if (id.equals(SupportConstants.UPDATE_COMMENT)) //   13
        {
            value = JSPHelper.getMessage(locale, "History.updateComment");
        } else if (id.equals(SupportConstants.DELETE_COMMENT)) //  14
        {
            value = JSPHelper.getMessage(locale, "History.deleteComment");
        } else if (id.equals(SupportConstants.CREATE_RELATION)) // 9
        {
            value = JSPHelper.getMessage(locale, "History.createRelation");
        } else if (id.equals(SupportConstants.UPDATE_RELATION)) //10
        {
            value = JSPHelper.getMessage(locale, "History.updateRelation");
        } else if (id.equals(SupportConstants.DELETE_RELATION))  //11
        {
            value = JSPHelper.getMessage(locale, "History.deleteRelation");
        }

        return value;
    }

    public static ArrayList getPublishedQuestionList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Question.yes"), SupportConstants.YES));//0
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Question.no"), SupportConstants.NO));//1
        return list;
    }

    public static ArrayList getStageTypesState(HttpServletRequest request, boolean hasOpenState) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if (!hasOpenState) {
            list.add(new LabelValueBean(JSPHelper.getMessage(locale, "State.stage.open"),  //0
                    Integer.toString(SupportConstants.OPEN_STATE)));
        }
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "State.stage.progress"),
                Integer.toString(SupportConstants.PROGRESS_STATE)));//1
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "State.stage.close"),
                Integer.toString(SupportConstants.CLOSE_STATE)));//2

        return list;
    }

    public static ArrayList getAnswerList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Question.responded"), SupportConstants.YES));//0
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Question.noResponded"), SupportConstants.NO));//1
        return list;
    }

    /*  select for module reports */

    public static ArrayList getRecurrenceList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.haveRecurrence"),
                SchedulerConstants.ISRECURRENCE));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.notHaveRecurrence"),
                SchedulerConstants.NOTRECURRENCE));
        return list;
    }

    public static ArrayList getReminderList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.haveReminder"),
                SchedulerConstants.ISREMINDER));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.noHaveReminder"),
                SchedulerConstants.NOTREMINDER));
        return list;
    }

    public static ArrayList getRecurTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.daily"),
                SchedulerConstants.RECURRENCE_DAILY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.weekly"),
                SchedulerConstants.RECURRENCE_WEEKLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.monthly"),
                SchedulerConstants.RECURRENCE_MONTHLY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.calendarView.yearly"),
                SchedulerConstants.RECURRENCE_YEARLY));
        return list;
    }

    public static ArrayList getNotificationList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.haveNotification"),
                SchedulerConstants.HAVE_NOTIFICATION));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Scheduler.noHaveNotification"),
                SchedulerConstants.NOT_HAVE_NOTIFICATION));
        return list;
    }

    public static ArrayList getActiveOrNoList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.active"),
                "1"));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.inactive"),
                "2"));

        return list;
    }

    public static ArrayList getInOutCommunicationList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("", ""));

        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Document.in"),
                ContactConstants.IN_VALUE));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Document.out"),
                ContactConstants.OUT_VALUE));
        return list;
    }

    public static final List getReportType(HttpServletRequest request) {
        List myList = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        myList.add(new LabelValueBean("", ""));
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.type.single"),
                ReportConstants.SINGLE_TYPE.toString()));//3
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.type.summary"),
                ReportConstants.SUMMARY_TYPE.toString()));//1
        /*myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "Report.type.matrix"),
                ReportConstants.MATRIX_TYPE.toString()));//2

                */
        return myList;
    }

    public static final List getColumnGroupOrder(HttpServletRequest request) {
        List myList = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "ColumnGroup.order.ascending"),
                ReportConstants.ASCENDING_ORDER.toString()));
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "ColumnGroup.order.descending"),
                ReportConstants.DESCENDING_ORDER.toString()));
        return myList;
    }

    public static List getOperatorList(javax.servlet.ServletRequest servletRequest) {
        ArrayList list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        list.add(new LabelValueBean("=", ReportConstants.EQUAL.toString()));
        list.add(new LabelValueBean("<>", ReportConstants.DISTINCT.toString()));
        list.add(new LabelValueBean("<", ReportConstants.LESS.toString()));
        list.add(new LabelValueBean(">", ReportConstants.GREATER.toString()));
        list.add(new LabelValueBean("<=", ReportConstants.LESS_EQUAL.toString()));
        list.add(new LabelValueBean(">=", ReportConstants.GREATER_EQUAL.toString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.between"), ReportConstants.BETWEEN.toString()));
        return SortUtils.orderByProperty(list, "label");
    }

    public static final List getGroupDateList(HttpServletRequest request) {
        List myList = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "ColumnGroup.groupDateBy.week"),
                ReportConstants.GROUP_DATE_BY_WEEK.toString()));
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "ColumnGroup.groupDateBy.month"),
                ReportConstants.GROUP_DATE_BY_MONTH.toString()));
        myList.add(new LabelValueBean(JSPHelper.getMessage(locale, "ColumnGroup.groupDateBy.year"),
                ReportConstants.GROUP_DATE_BY_YEAR.toString()));
        return myList;
    }

    public static final List getReportStatus(HttpServletRequest request) {
        List myList = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        myList.add(
                new LabelValueBean(JSPHelper.getMessage(locale, "Report.status.preparation"),
                        ReportConstants.REPORT_STATUS_PREPARATION.toString())
        );
        myList.add(
                new LabelValueBean(JSPHelper.getMessage(locale, "Report.status.ready"),
                        ReportConstants.REPORT_STATUS_READY.toString())
        );
        return myList;
    }


    public static final List getActivityStatusList(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> r = new ArrayList<LabelValueBean>();
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Activity.status.planned"),
                CampaignConstants.ActivityStatus.PLANNED.getConstantAsString()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Activity.status.InProgress"),
                CampaignConstants.ActivityStatus.IN_PROGRESS.getConstantAsString()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Activity.status.concluded"),
                CampaignConstants.ActivityStatus.CONCLUDED.getConstantAsString()));

        return r;
    }

    public static final List getTemplateDocumentType(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> r = new ArrayList<LabelValueBean>();
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.documentType.html"),
                CampaignConstants.DocumentType.HTML.getConstantAsString()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.documentType.word"),
                CampaignConstants.DocumentType.WORD.getConstantAsString()));
        return r;
    }

    public static final Integer getIndexPositionFromTab(String resourceKey, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LinkedHashMap m = (LinkedHashMap) request.getAttribute("tabItems");
        List values = new ArrayList(m.values());
        Object o = m.get(resourceKey);
        return values.indexOf(o);
    }

    public static List getCategoryTablesList(javax.servlet.ServletRequest servletRequest) {
        return getCategoryAllTypeList((HttpServletRequest) servletRequest);
    }

    public static List getCategoryTablesListExtended(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ArrayList result = (ArrayList) getCategoryTablesList((HttpServletRequest) servletRequest);
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.addressContactPerson"), ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY));
        return SortUtils.orderByProperty(result, "label");
    }

    public static ArrayList getCategoryAllTypeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.customer"),
                ContactConstants.CUSTOMER_CATEGORY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.contactPerson"),
                ContactConstants.CONTACTPERSON_CATEGORY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.address"),
                ContactConstants.ADDRESS_CATEGORY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.product"),
                ContactConstants.PRODUCT_CATEGORY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.salesProcess"),
                ContactConstants.SALES_PROCESS_CATEGORY));
        list.add(new LabelValueBean(JSPHelper.getMessage(locale, "Category.salePosition"),
                ContactConstants.SALE_POSITION_CATEGORY));
        return SortUtils.orderByProperty(list, "label");
    }

    public static final List getCategoryTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> r = new ArrayList<LabelValueBean>();
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.compoundSelect"),
                CatalogConstants.CategoryType.COMPOUND_SELECT.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.singleSelect"),
                CatalogConstants.CategoryType.SINGLE_SELECT.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.date"),
                CatalogConstants.CategoryType.DATE.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.decimal"),
                CatalogConstants.CategoryType.DECIMAL.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.integer"),
                CatalogConstants.CategoryType.INTEGER.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.text"),
                CatalogConstants.CategoryType.TEXT.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.linkValue"),
                CatalogConstants.CategoryType.LINK_VALUE.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.freeText"),
                CatalogConstants.CategoryType.FREE_TEXT.getConstant()));
        r.add(new LabelValueBean(JSPHelper.getMessage(request, "Category.type.attach"),
                CatalogConstants.CategoryType.ATTACH.getConstant()));
        return r;
    }

    public static String getCategoryType(Integer constant, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.compoundSelect");
        }
        if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.singleSelect");
        }
        if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.date");
        }
        if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.decimal");
        }
        if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.integer");
        }
        if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.text");
        }
        if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.linkValue");
        }
        if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.freeText");
        }
        if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == constant) {
            return JSPHelper.getMessage(request, "Category.type.attach");
        }
        return null;
    }

    public static String getCategoryTable(String constant, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (ContactConstants.CUSTOMER_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.customer");
        }
        if (ContactConstants.CONTACTPERSON_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.contactPerson");
        }
        if (ContactConstants.PRODUCT_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.product");
        }
        if (ContactConstants.ADDRESS_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.address");
        }
        if (ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.addressContactPerson");
        }
        if (ContactConstants.SALE_POSITION_CATEGORY.equals(constant)) {
            return JSPHelper.getMessage(request, "Category.salePosition");
        }

        return null;
    }

    public static Boolean hasSubCategories(String categoryId, javax.servlet.ServletRequest servletRequest) {
        Boolean result = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CategoryCmd cmd = new CategoryCmd();
        cmd.setOp("hasSubCategories");
        cmd.putParam("categoryId", Integer.valueOf(categoryId.toString()));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            result = (Boolean) resultDTO.get("hasSubCategories");
            if (result) {
                List<CategoryDTO> childrenCategories = (List<CategoryDTO>) resultDTO.get("childrenCategories");
                request.setAttribute("childrenCategories", childrenCategories);
            }
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryCmd.class.getName() + " FAIL", e);
        }

        return result;
    }

    public static ArrayList getInboxStateFilter(Object obj) {
        ArrayList l = new ArrayList();
        l.add(new LabelValueBean(JSPHelper.getMessage((HttpServletRequest) obj, "Webmail.tray.unRead"), WebMailConstants.DASHBOARD_MAIL_FILTER_UNREAD));
        l.add(new LabelValueBean(JSPHelper.getMessage((HttpServletRequest) obj, "Webmail.tray.all"), WebMailConstants.DASHBOARD_MAIL_FILTER_ALL));
        return l;
    }

    public static void filterMultipleSelectValues(Map categoryAsMap, List keys, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.debug("categoryUtilAsMap " + categoryAsMap);
        log.debug("KEYS.... " + keys);
        Map selectedElements = new HashMap();
        Map allElements = new HashMap();

        selectedElements.putAll(categoryAsMap);
        allElements.putAll(categoryAsMap);

        List selectedValues = new ArrayList();
        List l = (List) selectedElements.get("values");
        if (null != l) {
            for (int i = 0; i < l.size(); i++) {
                Map value = (Map) l.get(i);
                if (null != keys && keys.contains(value.get("categoryValueId"))) {
                    selectedValues.add(value);
                }
            }
        }
        selectedElements.put("values", selectedValues);

        log.debug("Selected Elements " + selectedElements);

        List residualElements = new ArrayList();
        List l2 = (List) allElements.get("values");
        if (null != l2) {
            for (int i = 0; i < l2.size(); i++) {
                Map value = (Map) l2.get(i);
                if (null != keys && !keys.contains(value.get("categoryValueId"))) {
                    residualElements.add(value);
                }
                if (null == keys) {
                    residualElements.add(value);
                }
            }
        }
        allElements.put("values", residualElements);
        log.debug(" the rest elements " + allElements);

        request.setAttribute("selectedElements", selectedElements);
        request.setAttribute("residualElements", allElements);
    }

    /**
     * @param request
     * @return
     */
    public static ArrayList getTaskStatusFilterList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.taskList.all"),
                SchedulerConstants.SHOW_ALL));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.taskList.onlyNotClosed"),
                SchedulerConstants.SHOW_NOT_CLOSED));
        return list;
    }
}