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
 * Time: 18:52:03
 * To change this template use File | Settings | File Templates.
 */

public class PublishedValueConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);
        FieldValue fvObj = null;
        String opeValue = null;

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            opeValue = fieldPairValue.toString();

            if (("0").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Article.owner"), fieldPairValue);
            } else if (("1").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Article.company"), fieldPairValue);
            } else if (("2").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Article.public"), fieldPairValue);
            }
        } else {
            fvObj = new FieldValue("", fieldPairValue);
        }

        return fvObj;  //To change body of implemented methods use File | Settings | File Templates.
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
