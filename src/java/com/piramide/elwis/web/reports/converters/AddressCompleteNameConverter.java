package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.CompoundField;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.reportgenerator.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 22-feb-2006
 * Time: 17:27:16
 * To change this template use File | Settings | File Templates.
 */

public class AddressCompleteNameConverter implements Converter {
    private Log log = LogFactory.getLog(this.getClass());

    private String getValue(String path, Map titusPathMap, Pair[] pairs) {
        String alias = (String) titusPathMap.get(path);
        Object result = CustomReportGeneratorHelper.getPairValue(alias, pairs);
        return (String) (result != null ? result : null);
    }

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper,
                               String fieldTitusPath) {

        CompoundField compoundField = (CompoundField) field;
        StringBuffer buffer = new StringBuffer();
        //log.debug("......... dbToView  function  execute  .........");

        Map values = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);

        Map titusPathWithFields = TitusPathUtil.getTitusPathFieldsFromCompoundField(compoundField, fieldTitusPath);
        String addressType = null;
        String name1 = null;
        String name2 = null;
        String name3 = null;
        boolean name_ = false;

        for (Iterator iterator = titusPathWithFields.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Field f = (Field) entry.getValue();
            if (f.getName().equals("addresstype")) {
                addressType = getValue((String) entry.getKey(), values, pairs);
            } else if (f.getName().equals("name1")) {
                name1 = getValue((String) entry.getKey(), values, pairs);
            } else if (f.getName().equals("name2")) {
                name2 = getValue((String) entry.getKey(), values, pairs);
            } else if (f.getName().equals("name3")) {
                name3 = getValue((String) entry.getKey(), values, pairs);
            }
        }

        if (name1 != null && name1.trim().length() > 0) {
            buffer.append(name1); //name1
            name_ = true;
        }

        if (name_ && name2 != null && name2.trim().length() > 0) {
            if ("1".equals(addressType)) {
                buffer.append(",");
            }
            buffer.append(" ");
            buffer.append(name2);//name2
        }

        if (name_ && "0".equals(addressType) && name3 != null && name3.trim().length() > 0) {
            buffer.append(" ");
            buffer.append(name3);//name3
        }
        return new FieldValue(buffer.toString(), null);
    }

    public ResultValue viewToDb(Map map, Object object, ResourceBundleWrapper resourceBundleWrapper) {
        //log.debug("......... viewToDb   function  execute  .........");
        return new ResultValue(object);
    }

    public String getPatternKey() {
        return null;
    }

    public boolean usePatternToFormat() {
        return false;
    }
}
