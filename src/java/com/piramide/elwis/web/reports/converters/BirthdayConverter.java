package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 06-mar-2006
 * Time: 15:43:44
 * To change this template use File | Settings | File Templates.
 */

public class BirthdayConverter implements Converter {
    private Log log = LogFactory.getLog(this.getClass());

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        DateTimeZone timeZone = DateTimeZone.forID((String) map.get("timeZone"));
        FieldValue fvObj = null;
        Integer date = null;
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {

            date = Integer.valueOf(fieldPairValue.toString());

            if (fieldPairValue.toString().length() == 8) {
                fvObj = new FieldValue(DateUtils.getFormattedDateTimeWithTimeZone(new Long(DateUtils.integerToDateTime(date, timeZone).getMillis()), timeZone, resourceBundleWrapper.getMessage("datePattern")), fieldPairValue);
            } else {
                fvObj = new FieldValue(DateUtils.getFormattedDateTimeWithTimeZone(new Long(DateUtils.integerToDateTime(date, timeZone).getMillis()), timeZone, resourceBundleWrapper.getMessage("withoutYearPattern")), fieldPairValue);
            }

        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }

        return fvObj;
    }


    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {

        String pattern = resourceBundleWrapper.getMessage("datePattern");
        String patternWithoutYear = resourceBundleWrapper.getMessage("withoutYearPattern");
        Date aDate1 = null;
        Object date = null;
        ResultValue resultValue = null;


        if (object != null && object instanceof String) {
            resultValue = new ResultValue();
            // validating the Date
            String dateString = (String) object;
            if (!GenericValidator.isBlankOrNull(dateString)) {
                try {
                    if (dateString.length() == pattern.trim().length()) {
                        aDate1 = DateUtils.formatDate(dateString, pattern.trim(), false);
                        date = DateUtils.dateToInteger(aDate1).toString();
                    }/* else {
                        aDate1 = DateUtils.formatDate(dateString, patternWithoutYear.trim(), true);
                        date = DateUtils.dateToIntegerWithoutYear(aDate1);
                    }*/ //comment because in filter not use MM/DD pattern to date type
                } catch (Exception e) {
                    log.debug("Error in format date:" + e);
                }
            }

            if (date != null) {
                resultValue.setValue(String.valueOf(date));
            }

        } else if (object != null && object instanceof Date) {
            resultValue = new ResultValue();
            aDate1 = (Date) object;
            date = DateUtils.dateToInteger(aDate1);
            resultValue.setValue(String.valueOf(date));
        }

        if (date == null && object != null) {
            resultValue = new ResultValue();
            resultValue.setValue(null);
            /*resultValue.setErrorMessage("Address.error.Person.birthday",true);
            resultValue.setResourceParams(new String[]{pattern, patternWithoutYear});*/ //comment because not use MM/DD pattern in the validation
            resultValue.setErrorMessage("error.converterDate", true);
            resultValue.setResourceParams(new String[]{pattern});
        }

        return resultValue;
    }

    public String getPatternKey() {
        return null;
    }

    public boolean usePatternToFormat() {
        return false;
    }
}















