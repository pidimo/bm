package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.utils.TelecomType;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 27-mar-2006
 * Time: 9:41:17
 * To change this template use File | Settings | File Templates.
 */
public class TelecomtypeConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        FieldValue fvObj = null;
        String opeValue = null;
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {

            opeValue = fieldPairValue.toString();

            if (TelecomType.PHONE_TYPE.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("TelecomType.type.phone"), fieldPairValue);
            } else if (TelecomType.EMAIL_TYPE.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("TelecomType.type.email"), fieldPairValue);
            } else if (TelecomType.FAX_TYPE.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("TelecomType.type.fax"), fieldPairValue);
            } else if (TelecomType.OTHER_TYPE.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("TelecomType.type.other"), fieldPairValue);
            } else if (TelecomType.LINK_TYPE.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("TelecomType.type.link"), fieldPairValue);
            }

        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }

        return fvObj;
    }

    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {
        return new ResultValue(object);
    }

    public String getPatternKey() {
        return null;
    }

    public boolean usePatternToFormat() {
        return false;
    }
}
