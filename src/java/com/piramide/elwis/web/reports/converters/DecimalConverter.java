package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.validator.NumberFormatValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 24-feb-2006
 * Time: 12:05:26
 * To change this template use File | Settings | File Templates.
 */

public class DecimalConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        Locale locale = (Locale) map.get("locale");
        FieldValue fvObj = null;
        Double fieldValue = null;
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            fieldValue = Double.valueOf(fieldPairValue.toString());
            fvObj = new FieldValue(FormatUtils.formatingDecimalNumber(fieldValue, locale, getMaxIntPart(), getMaxFloatPart()), fieldPairValue);
        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }
        return fvObj;
    }


    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {

        ResultValue resultValue = null;
        Locale locale = (Locale) map.get("locale");
        int result = -9;
        boolean withErrors = false;
        String value = (String) object;
        Log log = LogFactory.getLog(this.getClass());

        if (!GenericValidator.isBlankOrNull(value)) {

            try {
                resultValue = new ResultValue();
                result = NumberFormatValidator.i.validatePositive(value, locale, getMaxIntPart(), getMaxFloatPart());

                if (result == NumberFormatValidator.INVALID) {
                    resultValue.setValue(null);
                    resultValue.setErrorMessage("error.decimalNumberConverter.invalid", true);
                    withErrors = true;
                } else if (result == NumberFormatValidator.OUT_OF_RANGE) {
                    resultValue.setValue(null);
                    resultValue.setErrorMessage("errors.decimalNumberConverter.NumberOutOfRange", true);
                    withErrors = true;
                } else if (result == NumberFormatValidator.ONLY_POSITIVE) {
                    resultValue.setValue(null);
                    resultValue.setErrorMessage("errors.decimalNumberConverter.NumberPositive", true);
                    withErrors = true;
                } else {
                    withErrors = false;
                }

            } catch (Exception e) {
                log.debug("Error validating DecimalNumber " + e);
            }
            //put the correct BigDecimal
            if (!withErrors) {
                resultValue = new ResultValue();
                resultValue.setValue(FormatUtils.unformatingDecimalNumber(object.toString(), locale,
                        getMaxIntPart(),
                        getMaxFloatPart()));
            }
        }
        return resultValue;
    }

    public String getPatternKey() {
        return "numberFormat.2DecimalPlaces";
    }

    public boolean usePatternToFormat() {
        return true;
    }

    /**
     * The max integer digits
     * @return int
     */
    protected int getMaxIntPart() {
        return 10;
    }

    /**
     * The max floating point digits
     * @return int
     */
    protected int getMaxFloatPart() {
        return 2;
    }
}