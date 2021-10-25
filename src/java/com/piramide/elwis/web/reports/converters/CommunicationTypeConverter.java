package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.utils.CommunicationTypes;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 07-mar-2006
 * Time: 9:59:33
 * To change this template use File | Settings | File Templates.
 */
public class CommunicationTypeConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        FieldValue fvObj = null;
        String opeValue = null;

        Object aux = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (aux != null && !"".equals(aux)) {
            opeValue = aux.toString();

            if (("0").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.phone"), aux);
            } else if (("1").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.meeting"), aux);
            } else if (("2").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.fax"), aux);
            } else if (("3").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.letter"), aux);
            } else if (("4").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.other"), aux);
            } else if (("5").equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.email"), aux);
            } else if ("6".equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.document"), aux);
            } else if (CommunicationTypes.WEB_DOCUMENT.equals(opeValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Communication.type.webDocument"), aux);
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

