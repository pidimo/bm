package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.web.common.util.JSPHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 24-mar-2006
 * Time: 11:46:00
 * To change this template use File | Settings | File Templates.
 */
public class SystemLanguageConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);
        FieldValue fvObj = null;
        Integer opeValue = null;
        Locale locale = (Locale) map.get("locale");
        JSPHelper jspHelper = new JSPHelper();
        ArrayList list = new ArrayList();

        /*for (Iterator iterator = SystemLanguage.systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String iso = (String) iterator.next();
            list.add(new LabelValueBean(JSPHelper.getMessage(locale, (String) SystemLanguage.systemLanguages.get(iso)),
                    iso));
        }

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            opeValue = Integer.valueOf(fieldPairValue.toString());

            if (new Integer(0).equals(opeValue))
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("User.externalUser"), fieldPairValue);
            else if (new Integer(1).equals(opeValue))
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("User.intenalUser"), fieldPairValue);
        } else
            fvObj = new FieldValue("", fieldPairValue);*/

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
