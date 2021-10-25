package com.piramide.elwis.web.utils;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class VoucherSequenceRuleFormatUtil extends SequenceRuleFormatUtil {
    private int contractNumberPartSize = 0;
    private boolean containYearElement = false;
    private boolean containMonthElement = false;
    private boolean containDayElement = false;
    private String startNumber;
    private String resetType;

    public VoucherSequenceRuleFormatUtil(String format,
                                         String startNumber,
                                         String resetType) {
        this.format = format;
        this.startNumber = startNumber;
        this.resetType = resetType;
    }

    protected List<String> getValidElements() {
        return Arrays.asList("Y", "M", "D", "C", "N", "\\", "/", "-", "_");
    }

    private boolean validateContractNumber(String format) {
        List<String> element = readElements(format, "C");
        if (element.size() == 1) {
            contractNumberPartSize = element.get(0).length();
        }
        return element.size() <= 1;
    }

    protected int getContractNumberPartSize() {
        return contractNumberPartSize;
    }

    protected void setFormatElement(String element) {
        if ("Y".equals(element)) {
            containYearElement = true;
        }
        if ("M".equals(element)) {
            containMonthElement = true;
        }
        if ("D".equals(element)) {
            containDayElement = true;
        }
    }

    @Override
    protected boolean isValidFormat() {
        return super.isValidFormat() &&
                validateElement(format, "Y") &&
                validateElement(format, "M") &&
                validateElement(format, "D") &&
                validateContractNumber(format);
    }

    @Override
    public List<ActionError> formatValidator(HttpServletRequest request) {
        List<ActionError> formatErrors = new ArrayList<ActionError>();

        if (GenericValidator.isBlankOrNull(format)) {
            return formatErrors;
        }

        if (GenericValidator.isBlankOrNull(startNumber)) {
            return formatErrors;
        }

        if (GenericValidator.isBlankOrNull(resetType)) {
            return formatErrors;
        }

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

        if (FinanceConstants.SequenceRuleResetType.Yearly.getConstantAsString().equals(resetType)) {
            if (!containYearElement) {
                formatErrors.add(
                        new ActionError("SequenceRule.format.YearlyPatternError",
                                JSPHelper.getMessage(request, "SequenceRule.resetType.yearly"))
                );
            }
        }
        if (FinanceConstants.SequenceRuleResetType.Monthly.getConstantAsString().equals(resetType)) {
            if (!containYearElement
                    || !containMonthElement) {
                formatErrors.add(
                        new ActionError("SequenceRule.format.MonthlyPatternError",
                                JSPHelper.getMessage(request, "SequenceRule.resetType.monthly"))
                );
            }
        }
        if (FinanceConstants.SequenceRuleResetType.Daily.getConstantAsString().equals(resetType)) {
            if (!containYearElement
                    || !containMonthElement
                    || !containDayElement) {
                formatErrors.add(
                        new ActionError("SequenceRule.format.DailyPatternError",
                                JSPHelper.getMessage(request, "SequenceRule.resetType.daily"))
                );
            }
        }

        if (getDigitPartSize() == 0 || getDigitPartSize() > 10) {
            formatErrors.add(new ActionError("SequenceRule.format.error.invalidNumberDigits"));
        }

        if (getContractNumberPartSize() > 0 && getContractNumberPartSize() > 10) {
            formatErrors.add(new ActionError("SequenceRule.format.error.invalidCustomerNumberDigits"));
        }

        return formatErrors;
    }
}
