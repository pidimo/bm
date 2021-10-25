package com.piramide.elwis.web.common.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorResources;
import org.apache.struts.validator.Resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Util to write jQuery validation rules of struts form validations
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.1
 */
public class JQueryValidationFunctions {

    private static Log log = LogFactory.getLog(JQueryValidationFunctions.class);

    /**
     * Get the struts validation form properties
     * @param formName
     * @param request
     * @return list
     */
    public static List<Map> getStrutsFormValidationItems(String formName, HttpServletRequest request) {

        List<Map> result = new ArrayList<Map>();

        if (!GenericValidator.isBlankOrNull(formName)) {
            ServletContext servletContext = request.getSession().getServletContext();
            ValidatorResources validatorResources = Resources.getValidatorResources(servletContext, request);
            Locale locale = Resources.getLocale(request);

            Form form = validatorResources.get(locale, formName);

            if (form != null) {
                for (Object o : form.getFields()) {
                    Field field = (Field) o;
                    List<String> depends = new ArrayList<String>();

                    if (field.getDepends() != null) {
                        StringTokenizer dependsTokenizer = new StringTokenizer(field.getDepends(), ",");
                        while (dependsTokenizer.hasMoreTokens()) {
                            String depend = dependsTokenizer.nextToken().trim();
                            depends.add(depend);
                        }
                    }

                    Map map = new HashMap();
                    map.put("property", field.getProperty());
                    map.put("dependsList", depends);

                    result.add(map);
                }
            }
        }

        return result;
    }

    /**
     * Write the jquery validation rules for struts form properties
     * @param formName
     * @param request
     * @return Map
     */
    public static Map writeJQueryValidationRules(String formName, HttpServletRequest request) {
        Map resultMap = new HashMap();

        List<Map> formPropertiesList = getStrutsFormValidationItems(formName, request);

        StringBuffer buffer = new StringBuffer();
        List<Map> mapList = new ArrayList<Map>();

        for (Map map : formPropertiesList) {
            String property = (String) map.get("property");
            List<String> depends = (List<String>) map.get("dependsList");
            String validationRules = writePropertyRules(depends);

            if (validationRules != null) {
                buffer = addValidationRule(buffer, property, validationRules);
                mapList.add(map);
            }
        }

        //add rules info
        resultMap.put("jQueryValidationRules", buffer.toString());
        resultMap.put("propertyRulesList", mapList);

        return resultMap;
    }

    private static StringBuffer addValidationRule(StringBuffer buffer, String property, String rules) {
        if (property != null && rules != null) {
            buffer = addCommaSeparator(buffer);
            buffer.append("\"dto(").append(property).append(")\": { \n");
            buffer.append(rules);
            buffer.append("\n}");
        }

        return buffer;
    }

    private static String writePropertyRules(List<String> propertyDepends) {
        String validationRules = null;

        if (propertyDepends != null) {
            StringBuffer buffer = new StringBuffer();
            for (String depend : propertyDepends) {
                if ("required".equals(depend)) {
                    buffer = addRequiredRule(buffer);
                }
            }

            if (buffer.length() > 0) {
                validationRules = buffer.toString();
            }
        }

        return validationRules;
    }

    private static StringBuffer addCommaSeparator(StringBuffer buffer) {
        if (buffer.length() > 0) {
            buffer.append(", \n");
        }
        return buffer;
    }

    private static StringBuffer addRequiredRule(StringBuffer buffer) {
        buffer = addCommaSeparator(buffer);
        return buffer.append("required: true");
    }

}
