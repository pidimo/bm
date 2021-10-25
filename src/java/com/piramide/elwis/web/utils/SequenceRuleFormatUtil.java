package com.piramide.elwis.web.utils;

import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public abstract class SequenceRuleFormatUtil {
    private String invalidCharacter = null;
    private int digitPartSize = 0;
    private List<String> validElements = getValidElements();

    protected String format;

    protected abstract List<String> getValidElements();

    protected abstract void setFormatElement(String element);

    private boolean validateContain(String format) {
        char[] formatAsCharArray = format.toCharArray();
        for (char element : formatAsCharArray) {
            if (!validElements.contains(String.valueOf(element))) {
                invalidCharacter = String.valueOf(element);
                return false;
            }
        }
        return true;
    }

    private boolean validateDigitElement(String format) {
        List<String> element = readElements(format, "N");
        if (element.size() == 1) {
            digitPartSize = element.get(0).length();
        }
        return element.size() == 1;
    }

    protected boolean isValidFormat() {
        return validateContain(format) &&
                validateDigitElement(format);
    }

    protected boolean validateElement(String format, String element) {
        List<String> elements = readElements(format, element);
        if (!elements.isEmpty()) {
            setFormatElement(element);
        }

        for (String matchElement : elements) {
            if (matchElement.length() % 2 != 0) {
                return false;
            }
        }

        return true;
    }

    protected List<String> readElements(String format, String element) {
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

    protected int getDigitPartSize() {
        return digitPartSize;
    }

    private boolean safeFind(Matcher matcher) {
        try {
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getInvalidCharacter() {
        return invalidCharacter;
    }

    public String getFormat() {
        return format;
    }

    public List<ActionError> formatValidator(HttpServletRequest request) {
        List<ActionError> formatErrors = new ArrayList<ActionError>();

        if (!isValidFormat()) {
            if (null != getInvalidCharacter()) {
                formatErrors.add(
                        new ActionError("SequenceRule.format.error.invalidCharacter", getInvalidCharacter())
                );
                return formatErrors;
            }

            formatErrors.add(new ActionError("SequenceRule.format.error"));
            return formatErrors;
        }

        if (getDigitPartSize() == 0 || getDigitPartSize() > 10) {
            formatErrors.add(new ActionError("SequenceRule.format.error.invalidNumberDigits"));
        }

        return formatErrors;
    }
}
