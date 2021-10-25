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
 * Date: 22-feb-2006
 * Time: 17:44:02
 * To change this template use File | Settings | File Templates.
 */

public class OperatorConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        FieldValue fvObj = null;
        String opeValue = null;
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {

            opeValue = fieldPairValue.toString();

            if ("EQUAL".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.equal"), fieldPairValue);
            } else if ("LESS".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.less"), fieldPairValue);
            } else if ("GREATER".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.greater"), fieldPairValue);
            } else if ("LESS_EQUAL".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.lessEqual"), fieldPairValue);
            } else if ("GREATER_EQUAL".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.greaterEqual"), fieldPairValue);
            } else if ("DISTINCT".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.distinct"), fieldPairValue);
            } else if ("BETWEEN0".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("CampaignCriteria.between.withoutLimit"), fieldPairValue);
            } else if ("BETWEEN1".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("CampaignCriteria.between.withLimit"), fieldPairValue);
            } else if ("IN".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Operator.in"), fieldPairValue);
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
