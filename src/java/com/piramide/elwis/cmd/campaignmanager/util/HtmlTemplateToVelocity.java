package com.piramide.elwis.cmd.campaignmanager.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is aimed to convert an html template with the contact/contactperson/employee/company variables
 * defined through campaign template into a velocity template.
 * Basically replace the Ids found in the template and replace it with velocity expressions corresponding
 * to such ids.
 *
 * @author Fernando
 * @version $Id: HtmlTemplateToVelocity.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class HtmlTemplateToVelocity {
    private static final String PATTERN = "<label[^>/]*id=\"([a-zA-Z_0-9]*)\"[^>/]*>(?:\\s*<[^>]*>)*([^<]*)";
    private static final String TELECOMTYPEID_PATTERN = "<label[^>/]*id=\"[a-zA-Z]*_tel_([0-9]*)\"[^>/]*>(?:\\s*<[^>]*>)*([^<]*)";

    /**
     * Gets an HTML campaign template and returns an Velocity template as result of variable expressions replacing.
     * This use a pattern defined in the variable PATTERN, which return 3 groups when matchs, the group 0 contains the exact match
     * for the pattern, the group 1 contains the value of the ID of the label tag, and the group 2 contains the most
     * inner content inside the label tag that matches the query.
     * The replace basically get replace the group 2 with the group 1 (enclosed a ${}).
     *
     * @param template the html campaing template
     * @return the string representing the Velocity template.
     */
    public static StringBuilder buildVelocityTemplate(StringBuilder template) {
        Matcher valueMatcher = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE).matcher(template);
        while (safeFind(valueMatcher)) {
            template.replace(valueMatcher.start(2), valueMatcher.end(2), "${" + valueMatcher.group(1) + "}");
        }
        return template;
    }

    /**
     * This method is used to catch some exception that can happen when the find in a Matcher is invoked.
     * If some error happens when performinf the "find" then a false is returned, because there was a problem,
     * so nothing has been found.
     *
     * @param matcher the matcher
     * @return true if find success, false otherwise.
     */
    private static boolean safeFind(Matcher matcher) {
        try {
            return matcher.find();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * This is a utility method which finds all telecomtype ids in the given template and return a Map which contains
     * the telecomtype id (an integer) and its respectives string values (visible value) in the template.
     * <p/>
     * This method must be used before the html template is converted to velocity template.
     *
     * @param template the html template containing variables.
     * @return return a Map having telecomtype id as key and a list of strings of its values visible names
     *         in the template.
     */
    public static Map<Integer, List<String>> findTelecomTypeIdsVariableInTemplate(StringBuilder template) {
        Matcher valueMatcher = Pattern.compile(TELECOMTYPEID_PATTERN, Pattern.CASE_INSENSITIVE).matcher(template);
        Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
        int telecomTypeId;
        List<String> stringValues;
        while (valueMatcher.find()) {
            telecomTypeId = Integer.parseInt(valueMatcher.group(1));
            if (result.containsKey(telecomTypeId)) {
                result.get(telecomTypeId).add(valueMatcher.group(2));
            } else {
                stringValues = new LinkedList<String>();
                stringValues.add(valueMatcher.group(2));
                result.put(telecomTypeId, stringValues);
            }
        }
        return result;
    }


}
