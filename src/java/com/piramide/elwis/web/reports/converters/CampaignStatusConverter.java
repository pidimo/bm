package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 23-feb-2006
 * Time: 11:46:58
 * To change this template use File | Settings | File Templates.
 */

public class CampaignStatusConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        String value = null;
        FieldValue fvObj = null;
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            value = fieldPairValue.toString();
            if (("1").equals(value)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Campaign.preparation"), fieldPairValue);
            } else if (("2").equals(value)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Campaign.sent"), fieldPairValue);
            } else if (("3").equals(value)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Campaign.cancel"), fieldPairValue);
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
