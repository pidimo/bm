package com.piramide.elwis.web.common.el;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.catalogmanager.FreeTextComposeNameCmd;
import com.piramide.elwis.cmd.contactmanager.ReadCompanyContractReminderUserInfoCmd;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.dynamicsearch.el.DynamicSearchUtil;
import com.piramide.elwis.web.common.htmlfilter.ImageStoreUpdateSrcImgTagFilter;
import com.piramide.elwis.web.common.util.*;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.ResponseUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * JSP 2.0 Functions.
 * <p/>
 *
 * @author Fernando Montaño
 * @version $Id: Functions.java 12683 2017-05-23 23:12:51Z miguel $
 */

public class Functions {


    private static Log log = LogFactory.getLog(Functions.class);

    public static String filterAjaxResponse(String value) {
        if (value == null) {
            return null;
        }

        char content[] = new char[value.length()];

        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case 128: //
                    result.append("&#8364;");
                    break;

                default:
                    result.append(content[i]);
                    break;
            }
        }

        return result.toString();
    }

    /**
     * Apply JavaScriptEncoder.encode() encoding to text and replace euro character encoded
     * by html decimal representation, this is necessary when '' character is recovered by AJAX management
     *
     * @param text report column label
     * @return String js encoded
     * @deprecated use ajaxResponseFilter(), js encode is not nescessary with UTF8
     */
    public static String jsEncodeAndAjaxFilter(String text) {
        String result = JavaScriptEncoder.encode(text);

        //replace euro character by html decimal representation
        char euroCharacter = 128; //''
        String euroCharacterEncoded = JavaScriptEncoder.encode(String.valueOf(euroCharacter));
        result = result.replaceAll(euroCharacterEncoded, "&#8364;"); // &#8364; is decimal representation in html
        return result;
    }


    /**
     * Filter the special characters, like < and > which are
     * used to represent tags. This functionc converts < to &gt; ...and so on. This is required when send
     * response as xml whit AJAX
     *
     * @param value xml value
     * @return String
     */
    public static String ajaxResponseFilter(String value) {
        return ResponseUtils.filter(value);
    }


    public static String parseAJAXText(String value) {

        if (value == null) {
            return null;
        }

        char content[] = new char[value.length()];

        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case 128: //''
                    result.append("&#0128;");
                    break;

                default:
                    result.append(content[i]);
                    break;
            }
        }

        return result.toString();
    }

    public static java.lang.String divide(java.lang.Integer number, java.lang.Integer div) {
        return String.valueOf(number / div);
    }

    public static Object mapElement(Map map, String key) {
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public static String concat(String a, String b) {
        return new String(new StringBuffer(a).append(b));
    }

    public static boolean hasAccessRight(javax.servlet.ServletRequest servletRequest, String functionality, String permission) {
        //log.debug("hasAccessRight Function checking ...");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        if (user.getSecurityAccessRights().containsKey(functionality)) {
            Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
            return PermissionUtil.hasAccessRight(accessRight, permission);
        } else {
            return false;
        }
    }

    public static List checkPermissionAllowed(java.lang.Byte permission, java.util.Collection roleAccessRights,
                                              java.lang.Integer actualfunctionId, java.lang.Integer roleId) {

        List actualPermissions = new ArrayList();
        for (Iterator iterator = roleAccessRights.iterator(); iterator.hasNext(); ) {
            Map o = (Map) iterator.next();
            if (o.get("functionId").equals(actualfunctionId) && o.get("roleId").equals(roleId)) {
                actualPermissions = PermissionUtil.extractPermissions(((Byte) o.get("permission")).intValue());
            }
        }

        List result = new ArrayList();
        List permissions = PermissionUtil.getListStringPermissions();

        for (int i = 0; i < permissions.size(); i++) {
            String s = (String) permissions.get(i);
            if (PermissionUtil.hasAccessRight(permission, s)) {
                Map permissionMap = new HashMap();
                if (actualPermissions.contains(new Integer(PermissionUtil.convertPermissionToInt(s)))) {
                    permissionMap.put("isChecked", Boolean.valueOf("true"));
                } else {
                    permissionMap.put("isChecked", Boolean.valueOf("false"));
                }

                permissionMap.put("value", new Integer(PermissionUtil.convertPermissionToInt(s)));
                permissionMap.put("stringValue", s);
                result.add(permissionMap);
            }
        }

        List sortList = new ArrayList();

        for (int i = 0; i < PermissionUtil.getListStringPermissions().size(); i++) {
            Map blank = new HashMap();
            blank.put("blank", new Boolean(true));
            sortList.add(blank);
        }

        for (int i = 0; i < result.size(); i++) {
            Map map = (Map) result.get(i);

            if (PermissionUtil.PERMISSION_VIEW.equals(map.get("stringValue"))) {
                sortList.remove(0);
                sortList.add(0, map);
            } else {

                if (PermissionUtil.PERMISSION_CREATE.equals(map.get("stringValue"))) {
                    sortList.remove(1);
                    sortList.add(1, map);
                } else {
                    if (PermissionUtil.PERMISSION_UPDATE.equals(map.get("stringValue"))) {
                        sortList.remove(2);
                        sortList.add(2, map);
                    } else {
                        if (PermissionUtil.PERMISSION_DELETE.equals(map.get("stringValue"))) {
                            sortList.remove(3);
                            sortList.add(3, map);
                        } else {

                            if (PermissionUtil.PERMISSION_EXECUTE.equals(map.get("stringValue"))) {
                                sortList.remove(4);
                                sortList.add(4, map);
                            }
                        }
                    }
                }
            }
        }

        return sortList;
    }


    public static Object findValue(java.lang.String key, java.lang.String dtoId, java.lang.String dtoKeyName, java.util.List list) {
        Object value = null;
        Map map = new HashMap();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Map o = (Map) list.get(i);
                if (!o.isEmpty() && o.get(dtoKeyName).toString().equals(dtoId) && o.containsKey(key)) {
                    map = o;
                }
            }
            value = map.get(key);
        }
        return value;
    }

    /**
     * Check if the user has at least one of the functionalities of the map of functionalities.
     *
     * @param servletRequest
     * @param functionalities map of keys: Functionalities / Values: permissions
     * @return boolean
     */
    public static boolean checkSomeAccessRights(javax.servlet.ServletRequest servletRequest, Map functionalities) {
        log.debug("hasAccessRight Function checking..");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        boolean hasAtLeastOne = false;
        for (Iterator iterator = functionalities.keySet().iterator(); iterator.hasNext(); ) {
            String functionality = (String) iterator.next();
            if (user.getSecurityAccessRights().containsKey(functionality)) {
                Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
                if (PermissionUtil.hasAccessRight(accessRight, functionalities.get(functionality).toString())) {
                    return true;
                }
            }
        }
        return hasAtLeastOne;
    }

    /**
     * Encode a value
     *
     * @param string value to encode
     * @return return the string encoded
     */
    public static String encode(java.lang.String string) {
        try {
            return URLEncoder.encode(string, Constants.CHARSET_ENCODING);
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding the string", e);
            return string;
        }
    }

    public static String decode(String string) {
        try {
            return URLDecoder.decode(string, Constants.CHARSET_ENCODING);
        } catch (UnsupportedEncodingException e) {
            log.error("Error decode the string", e);
            return string;
        }
    }

    /**
     * Return a internationalizated value for given resourcekey
     *
     * @param key value to encode
     * @return return the value for the resource
     */
    public static String getMessage(javax.servlet.ServletRequest servletRequest, java.lang.String key) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return JSPHelper.getMessage(request, key);
    }

    /**
     * Increments simultaing ++ operator on jstl a variable name from request attribute and return it incremented.
     *
     * @return return incremented by one number representation
     */
    public static String incrementByOne(javax.servlet.jsp.PageContext pageContext, java.lang.String attributeName) {
        int currentValue = Integer.valueOf(String.valueOf(pageContext.getRequest().getAttribute(attributeName))).intValue();
        int incrementedValue = currentValue + 1;
        pageContext.getRequest().setAttribute(attributeName, new Integer(incrementedValue));
        return String.valueOf(incrementedValue);
    }

    /**
     * Check if the user has access to configuration module. It checks if every category which has
     * a map of elements of menu is empty or not.
     *
     * @param map the configuration menu map
     * @return true if has some access, false if has no any access.
     */
    public static boolean checkConfigurationMenuAccess(Map map) {
        boolean result = false;
        for (Iterator iterator = map.values().iterator(); iterator.hasNext(); ) {
            Map categoryMap = (Map) iterator.next();
            if (categoryMap.size() > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * return the first value of a linked hashmap
     *
     * @param map the map
     * @return the first value
     */
    public static Object getFirstValueOfLinkedMap(Map map) {
        Object result = null;
        for (Iterator iterator = map.values().iterator(); iterator.hasNext(); ) {
            Object element = iterator.next();
            result = element;
            break;
        }
        return result;
    }

    /**
     * obtain a list of all time zones
     *
     * @param servletRequest
     * @return list with all time zones
     */
    public static List getTimeZones(javax.servlet.ServletRequest servletRequest) {
        log.debug("Executing getTimeZones Function...........................");

        List listTimeZone = new ArrayList();
        Collection timeZoneCollec = (Collection) DateTimeZone.getAvailableIDs();
        int numId = 2; //num of Ids wath provoke exception
        TimeZoneData[] zones = new TimeZoneData[timeZoneCollec.size() - numId];

        Iterator it = timeZoneCollec.iterator();
        int i = 0;
        while (it.hasNext()) {
            String id = (String) it.next();
            if (!id.equals("Asia/Makassar") && !id.equals("Asia/Ujung_Pandang")) {  //this Ids provoke exception
                zones[i++] = new TimeZoneData(id, DateTimeZone.forID(id));
            }
        }
        Arrays.sort(zones);

        for (int j = 0; j < zones.length; j++) {
            TimeZoneData zone = zones[j];
            String label = zone.getStandardOffsetStr() + " " + zone.getID();
            LabelValueBean labelValueBean = new LabelValueBean(label, zone.getID());

            listTimeZone.add(labelValueBean);
        }
        return listTimeZone;
    }

    public static boolean checkSchedulerPermission(Byte permissions, String type) {
        return SchedulerPermissionUtil.hasSchedulerAccessRight(permissions, type);
    }


    public static String checkSchedulerGrantAccess(javax.servlet.ServletRequest servletRequest, String s) {
        log.debug("Execute checkSchedulerGrantAccess... ");

        String r = "";
        List result = new ArrayList();
        int value = 0;
        if (s != null && !"".equals(s)) {
            value = Integer.valueOf(s).intValue();
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        result = SchedulerPermissionUtil.extractPermissions(value);

        for (int i = 0; i < result.size(); i++) {
            Integer permission = (Integer) result.get(i);

            if (SchedulerPermissionUtil.ANONYM == permission.intValue()) {
                r = JSPHelper.getMessage(request, "Scheduler.grantAccess.anonym") + r;
            }
            if (SchedulerPermissionUtil.DELETE == permission.intValue()) {
                r = JSPHelper.getMessage(request, "Scheduler.grantAccess.delete") + r;
            }
            if (SchedulerPermissionUtil.EDIT == permission.intValue()) {
                r = JSPHelper.getMessage(request, "Scheduler.grantAccess.edit") + r;
            }
            if (SchedulerPermissionUtil.ADD == permission.intValue()) {
                r = JSPHelper.getMessage(request, "Scheduler.grantAccess.add") + r;
            }
            if (SchedulerPermissionUtil.READ == permission.intValue()) {
                r = JSPHelper.getMessage(request, "Scheduler.grantAccess.read") + r;
            }
            if (i < result.size() - 1) {
                r = ", " + r;
            }
        }
        return r;
    }

    /**
     * Return an date formatted to show the current date in the time zone of the user.
     *
     * @param servletRequest the request
     * @return a String formatted.
     */
    public static String getUserLocalizedCurrentDate(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        DateTimeFormatter formatter = DateTimeFormat.fullDate();
        formatter = formatter.withLocale((Locale) Config.get(request.getSession(), Config.FMT_LOCALE));
        formatter = formatter.withZone((DateTimeZone) user.getValue("dateTimeZone"));
        return formatter.print(new DateTime());
    }

    /**
     * Get current date as integer, the date is calculated from DateTime taking care or DateTimeZone
     *
     * @param servletRequest the request
     * @return a The current date integer representation
     */
    public static Integer getCurrentDateAsInteger(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        return DateUtils.dateToInteger(new DateTime((DateTimeZone) user.getValue("dateTimeZone")));
    }

    /**
     * returns the current time millis
     *
     * @return the string millis
     */
    public static synchronized String getCurrentTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * this method converts the text to html, the only thing doing here is to replace the return carriage
     * by <br> html tag.
     *
     * @param value
     * @return the value converted to be displayed as HTML
     */
    public static synchronized String convertTextToHtml(String value) {
        return ResponseUtils.filter(value).replaceAll("\n", "<br>");
    }

    /**
     * This function filter the special characters that belogs to html, like < and > which are
     * used to represent html tags. This functionc converts < to &gt; ...and so on.
     *
     * @param value the string value to be filtered
     * @return the string filtered to be write in the html.
     */
    public static String filterForHtml(String value) {
        return ResponseUtils.filter(value);
    }

    /**
     * Executes the String.replaceAll over a string
     *
     * @param value       the source string
     * @param regex       the regular expression
     * @param replacement the replacement string
     * @return result of the replacement
     */
    public static String replaceAll(String value, String regex, String replacement) {
        return value.replaceAll(regex, replacement);
    }


    /**
     * This method builds a set of parameters on the basis of the information contained in this DefaultForm,
     * this parameters is used to be sent AJAX request ( type POST )
     * <p/>
     * example
     * <p/>
     * defaultForm.dtoMap = {"companyId"=1, "addressId"=234, "name"=xxxxx}
     * <p/>
     * result = "companyId"=+encodeURI('1')+&+"addressId"=+encodeURI('234')+&+"name"=+encodeURI('xxxxx')+&+"auxparam"=+encodeURI('none')
     *
     * @param defaultForm Form than contain information  to build parameters
     * @return Parameters to be used in ajax request
     */
    public static String buildParametersForAjaxRequest(DefaultForm defaultForm) {
        if (null == defaultForm) {
            return "";
        }

        String requestParameters = "";
        Map formMap = defaultForm.getDtoMap();

        for (Object object : formMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String key = entry.getKey().toString();

            Object value = formMap.get(key);
            if (null != value) {
                String paramValue = "";

                if (value instanceof List) {
                    paramValue = buildListParameter((List) value);
                }

                if (value instanceof Number) {
                    paramValue = value.toString();
                }

                if (value instanceof Boolean) {
                    paramValue = value.toString();
                }

                if (value instanceof String) {
                    paramValue = com.piramide.elwis.web.common.el.Functions.encode(value.toString());
                }

                if (!"".equals(paramValue)) {
                    requestParameters += "\"" + key + "=\"" + "+encodeURI('" + paramValue + "')+\"&\"+";
                }
            }
        }

        requestParameters += "\"auxparam=\"+encodeURI('none')";
        return requestParameters;
    }

    public static void setValuesFromAjaxRequest(PageContext pageContext, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //process special parameters
        //tabindex
        String tabIndex = request.getParameter("tabIndex");
        if (null != tabIndex && !"".equals(tabIndex.trim())) {
            try {
                request.setAttribute("tabIndex", Integer.valueOf(tabIndex));
                request.getParameterMap().remove("tabIndex");
            } catch (NumberFormatException e) {
                log.debug("-> Setting up tabIndex [tabIndex=" + tabIndex + "] FAIL");
            }
        }

        //operation
        String op = request.getParameter("op");
        if (null != op && !"".equals(op.trim())) {
            request.setAttribute("op", op);
            request.getParameterMap().remove("op");
        }

        Map requestParameters = request.getParameterMap();
        DefaultForm defaultForm = new DefaultForm();

        for (Object object : requestParameters.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String parameterKey = entry.getKey().toString();
            String value = request.getParameter(parameterKey);

            //list Value
            if (value.indexOf('[') == 0 && value.indexOf(']') == value.length() - 1) {
                List valueAsList = buildListFromParameter(value);
                defaultForm.setDto(parameterKey, valueAsList);
                continue;
            }

            //is float
            if (value.indexOf('.') != -1) {
                try {
                    Float floatValue = Float.valueOf(value);
                    defaultForm.setDto(parameterKey, floatValue);
                    continue;
                } catch (NumberFormatException nfe) {
                }
            }

            //is integer
            try {
                Integer integerValue = Integer.valueOf(value);
                defaultForm.setDto(parameterKey, integerValue);
                continue;
            } catch (NumberFormatException nfe) {
            }

            //is string
            defaultForm.setDto(parameterKey, com.piramide.elwis.web.common.el.Functions.decode(value));
        }

        pageContext.setAttribute("org.apache.struts.taglib.html.BEAN", defaultForm, 2);

    }


    private static String buildListParameter(List list) {
        if (null == list) {
            return "[]";
        }

        String result = "";
        int index = 0;
        for (Object object : list) {
            result += object;
            if (index < list.size() - 1) {
                result += ",";
            }

            index++;
        }
        return "[" + result + "]";
    }

    private static List buildListFromParameter(String listParam) {
        if ("[]".equals(listParam)) {
            return new ArrayList();
        }

        String listElements = listParam.replaceAll("(\\[)", "").replaceAll("(\\])", "").trim();
        String[] listValues = listElements.split("(,)");
        if (null == listValues) {
            return new ArrayList();
        }

        return Arrays.asList(listValues);

    }

    public static boolean isInteger(String param) {
        try {
            new Integer(param);
            return true;
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Map getMaxOfString(HttpServletRequest request, String listName, String module) {
        User user = RequestUtils.getUser(request);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", user.getValue(Constants.COMPANYID).toString());
        fantabulousUtil.setModule(module);
        List<Map> data = fantabulousUtil.getData(request, listName);
        log.debug("-> DATA " + data);

        if (null == data || data.isEmpty()) {
            return null;
        }

        return data.get(0);
    }

    /**
     * Returns the value for a given system configuration property
     *
     * @param propertyKey the property key to recover
     * @return the value of the given property
     */
    public static String getConfigurationPropertyValue(java.lang.String propertyKey) {
        return ConfigurationFactory.getConfigurationManager().getValue(propertyKey);
    }

    /**
     * Update the download image store url in "src" attribute to img tags
     *
     * @param body     html body
     * @param response response
     * @param request  request
     * @return String
     */
    public static String updateImageStoreDownloadUrlForImgTags(String body, HttpServletResponse response, HttpServletRequest request) {
        String newBody = body;
        if (body != null) {
            //filter html body by store image id
            ImageStoreUpdateSrcImgTagFilter imgTagFilter = new ImageStoreUpdateSrcImgTagFilter(response, request);
            HtmlEmailParser parser = new HtmlEmailDOMParser();
            parser.addFilter(imgTagFilter);
            try {
                parser.parseHtml(body);
                newBody = parser.getHtml().toString();
                //get only body content
                newBody = HTMLParserUtil.getHtmlDocumentBodyContent(newBody);

                log.debug("New body processed by img tags:" + newBody);
            } catch (Exception e) {
                log.debug("Error in filter for img tag..", e);
            }
        }
        return newBody;
    }

    /**
     * Get document name from freetext id, this may be used to communications, template, campaign template
     *
     * @param freeTextId id
     * @return String
     */
    public static String getFreTextDocumentName(Integer freeTextId) {
        String docName = null;

        FreeTextComposeNameCmd composeNameCmd = new FreeTextComposeNameCmd();
        composeNameCmd.putParam("freeTextId", freeTextId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(composeNameCmd, null);
            if (resultDTO.containsKey("documentName")) {
                docName = resultDTO.get("documentName").toString();
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd.." + FreeTextComposeNameCmd.class);
        }
        return docName;
    }

    /**
     * Validate the company contract end reminder email, this should be associated with an company user employee
     *
     * @param reminderEmail email
     * @param request       request
     * @return true or false
     */
    public static boolean isValidContractEndEmailReminderInCompany(String reminderEmail, HttpServletRequest request) {
        boolean isValidEmail = false;

        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());

        ReadCompanyContractReminderUserInfoCmd reminderUserInfoCmd = new ReadCompanyContractReminderUserInfoCmd();
        reminderUserInfoCmd.putParam("companyId", companyId);
        reminderUserInfoCmd.putParam("reminderEmail", reminderEmail);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(reminderUserInfoCmd, null);
            isValidEmail = (Boolean) resultDTO.get("existContractReminderUser");
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...");
        }
        return isValidEmail;
    }

    /**
     * Validate the company contract end reminder email, this should be associated with an company user employee
     *
     * @param request request
     * @return true or false
     */
    public static boolean isValidContractEndEmailReminderInCompany(HttpServletRequest request) {
        return isValidContractEndEmailReminderInCompany(null, request);
    }

    public static Map getSystemLanguages() {
        return SystemLanguage.systemLanguages;
    }

    public static boolean emptyOrOnlyOneSelectedValidation(DefaultForm defaultForm, String property1, String property2, String resourceKey1, String resourceKey2, ActionErrors errors, HttpServletRequest request) {
        boolean valid = true;
        Object value1 = defaultForm.getDto(property1);
        Object value2 = defaultForm.getDto(property2);

        if (value1 != null && value2 != null && !GenericValidator.isBlankOrNull(value1.toString()) && !GenericValidator.isBlankOrNull(value2.toString())) {
            if (errors != null) {
                errors.add("bothSelectedError", new ActionError("Common.error.bothSelected", JSPHelper.getMessage(request, resourceKey1), JSPHelper.getMessage(request, resourceKey2)));
            }
            valid = false;
        }

        return valid;
    }

    public static List getDynamicSearchPropertiesList(String dynamicSearchName, HttpServletRequest request) {
        return DynamicSearchUtil.getDynamicSearchFieldList(dynamicSearchName, request);
    }

    public static List getDashboardBirthdayViewTypes(HttpServletRequest request) {
        List result = new ArrayList();

        result.add(new LabelValueBean(JSPHelper.getMessage(request, Constant.BirthdayViewType.ALL_CONTACTS.getResource()), Constant.BirthdayViewType.ALL_CONTACTS.getConstant().toString()));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, Constant.BirthdayViewType.SELECTED_EMPLOYEE.getResource()), Constant.BirthdayViewType.SELECTED_EMPLOYEE.getConstant().toString()));
        return result;
    }

    public static boolean isBootstrapUIMode(HttpServletRequest request) {
        Object mode = request.getSession().getAttribute("isBootstrapUI");

        if ((mode != null) && ((Boolean) mode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the server name and application context
     * i.e: to request url "http://localhost:8080/bm/campaign/List.do"
     * the result should be "http://localhost:8080/bm"
     * @param request the request
     * @return String
     */
    public static String getServerDomainContext(HttpServletRequest request) {
        String serverDomainContext = null;

        String originalRequestURL = request.getRequestURL().toString();
        String servletPath = request.getServletPath();

        if (originalRequestURL.indexOf(servletPath) > 0) {
            serverDomainContext = originalRequestURL.substring(0, originalRequestURL.indexOf(servletPath));
        }

        log.debug("Server domain + context:" + serverDomainContext);
        return serverDomainContext;
    }

    /**
     * Get help url from resource key, if not exist get url from configuration property 'elwis.help.URL'
     * @param resourceKey resource key
     * @param request request
     * @return String
     */
    public static String getContextDependantHelpUrl(String resourceKey, HttpServletRequest request) {
        String helpUrl = null;

        if (!GenericValidator.isBlankOrNull(resourceKey)) {
            helpUrl = JSPHelper.getMessage(request, resourceKey);
        }

        if (GenericValidator.isBlankOrNull(helpUrl) || helpUrl.contains(resourceKey)) {
            helpUrl = ConfigurationFactory.getConfigurationManager().getValue("elwis.help.URL");
        }

        return helpUrl;
    }


}
