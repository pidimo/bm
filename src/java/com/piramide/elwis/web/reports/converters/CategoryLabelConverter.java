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
 * Date: 21-mar-2006
 * Time: 17:01:50
 * To change this template use File | Settings | File Templates.
 */
public class CategoryLabelConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        FieldValue fvObj = null;
        Integer opeValue = null;

        Object aux = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (aux != null && !"".equals(aux)) {
            opeValue = Integer.valueOf(aux.toString());

            if (new Integer(1).equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Category.customer"), aux);
            } else if (new Integer(2).equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Category.contactPerson"), aux);
            } else if (new Integer(3).equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Category.product"), aux);
            } else if (new Integer(4).equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Category.address"), aux);
            }
        } else {
            fvObj = new FieldValue("", aux);
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

