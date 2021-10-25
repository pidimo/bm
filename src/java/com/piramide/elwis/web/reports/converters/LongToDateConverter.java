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
import com.piramide.elwis.utils.ReportConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 21-feb-2006
 * Time: 13:18:43
 * To change this template use File | Settings | File Templates.
 */

public class LongToDateConverter implements Converter {
    private Log log = LogFactory.getLog(this.getClass());

    // 1125676288281 -> 12/09/2006 04:35
    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {
        log.debug(" ... execute dbToView FUNCTION ...");
        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        DateTimeZone timeZone = (DateTimeZone) map.get("timezone");
        String typeView = String.valueOf(map.get(ReportConstants.LONGDATE_AS_SHORT_KEY));
        FieldValue fvObj = null;
        Long date = null;

        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            date = Long.valueOf(fieldPairValue.toString());

            if (ReportConstants.LONGDATE_AS_SHORT_VALUE.equals(typeView)) {
                fvObj = new FieldValue(DateUtils.getFormattedDateTimeWithTimeZone(fieldPairValue.toString(), timeZone, resourceBundleWrapper.getMessage("datePattern")), fieldPairValue);
            } else {
                fvObj = new FieldValue(DateUtils.getFormattedDateTimeWithTimeZone(fieldPairValue.toString(), timeZone, resourceBundleWrapper.getMessage("dateTimePattern")), fieldPairValue);
            }
        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }
        return fvObj;
    }

    //12/09/2006 -> 1125676288281
    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {
        log.debug(" ... execute viewToDb ** FUNCTION ...LongToDateConverter.");
        DateTimeZone zone = (DateTimeZone) map.get("timezone");
        String pattern = resourceBundleWrapper.getMessage("datePattern");
        ResultValue resultValue = null;
        Object ret = null;
        Date aDate1 = null;
        DateTime createDateTime = null;

        if (object != null && object instanceof String) {
            resultValue = new ResultValue();

            // validating the Date
            String dateString = (String) object;
            if (!GenericValidator.isBlankOrNull(dateString)) {
                try {
                    aDate1 = DateUtils.formatDate(dateString, pattern.trim(), false);
                } catch (Exception e) {
                    log.debug("Error in format date:" + e);
                }

                if (aDate1 != null) {
                    createDateTime = DateUtils.integerToDateTime(DateUtils.dateToInteger(aDate1), zone);
                }
                if (createDateTime != null) {
                    ret = new Long(createDateTime.getMillis());
                    resultValue.setValue(String.valueOf(ret));
                }
            }

        } else if (object != null && object instanceof Date) {
            resultValue = new ResultValue();
            aDate1 = (Date) object;
            /*createDateTime = DateUtils.integerToDateTime(DateUtils.dateToInteger(aDate1), zone);*/
            /*createDateTime.withMillis(aDate1.getTime());*/
            createDateTime = new DateTime(aDate1.getTime(), zone);

            if (createDateTime != null) {
                ret = new Long(createDateTime.getMillis());
                resultValue.setValue(String.valueOf(ret));
            }
        }

        if (ret == null && object != null) {
            resultValue = new ResultValue();
            resultValue.setValue(null);
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