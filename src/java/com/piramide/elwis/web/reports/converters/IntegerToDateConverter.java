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
 * Date: 22-feb-2006
 * Time: 17:02:22
 * To change this template use File | Settings | File Templates.
 */

public class IntegerToDateConverter implements Converter {
    private Log log = LogFactory.getLog(this.getClass());

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);
        DateTimeZone timeZone = (DateTimeZone) map.get("timezone");
        FieldValue fvObj = null;
        Integer date = null;

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            date = Integer.valueOf(fieldPairValue.toString());
            fvObj = new FieldValue(DateUtils.getFormattedDateTimeWithTimeZone(new Long(DateUtils.integerToDateTime(date, timeZone).getMillis()), timeZone, resourceBundleWrapper.getMessage("datePattern")), DateUtils.integerToDate(date));
        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }

        return fvObj;
    }


    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {
        log.debug("Execute viewToDb method with \n" +
                "map =" + map + "\n" +
                "object =" + object + "\n" +
                "resourceBundleWrapper = " + resourceBundleWrapper);

        String pattern = resourceBundleWrapper.getMessage("datePattern");

        Date aDate1 = null;
        Object date = null;
        ResultValue resultValue = null;

        if (object != null && object instanceof String) {
            resultValue = new ResultValue();
            // validating the Date
            String dateString = (String) object;
            if (!GenericValidator.isBlankOrNull(dateString)) {
                try {
                    aDate1 = DateUtils.formatDate(dateString, pattern.trim(), false);
                    date = DateUtils.dateToInteger(aDate1).toString();
                } catch (Exception e) {
                    log.debug("Error in format date:" + e);
                }

                if (date != null) {
                    resultValue.setValue(String.valueOf(date));
                }
            }
        } else if (object != null && object instanceof Date) {
            resultValue = new ResultValue();
            aDate1 = (Date) object;
            date = DateUtils.dateToInteger(aDate1);
            resultValue.setValue(String.valueOf(date));
        }

        if (date == null && object != null) { //invalidDate
            resultValue = new ResultValue();
            resultValue.setValue(null);
            resultValue.setErrorMessage("error.converterDate", true);
            resultValue.setResourceParams(new String[]{pattern});
        }

        return resultValue;
    }

    public String getPatternKey() {
        return "datePattern";
    }

    public boolean usePatternToFormat() {
        return true;
    }
}
