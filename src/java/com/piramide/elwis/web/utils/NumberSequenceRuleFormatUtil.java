package com.piramide.elwis.web.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class NumberSequenceRuleFormatUtil extends SequenceRuleFormatUtil {

    public NumberSequenceRuleFormatUtil(String format) {
        this.format = format;
    }

    protected List<String> getValidElements() {
        return Arrays.asList("N");
    }

    protected void setFormatElement(String element) {
    }
}
