package com.piramide.elwis.web.reports.converters;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.converter.Converter;
import com.jatun.titus.listgenerator.structure.converter.FieldValue;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper;
import com.jatun.titus.reportgenerator.util.Pair;
import com.piramide.elwis.utils.FinanceConstants;

import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: InvoiceTypeConverter.java 12-feb-2009 17:19:13 $
 */
public class InvoiceTypeConverter implements Converter {

    public FieldValue dbToView(Field field, Map map, Pair[] pairs, ResourceBundleWrapper resourceBundleWrapper, String titusPath) {

        Map titusPathWithAlias = (Map) map.get(CustomReportGeneratorConstants.FIELD_PATH_ALIAS_MAP);
        String fieldAlias = (String) titusPathWithAlias.get(titusPath);
        FieldValue fvObj = null;
        String rowValue = null;

        Object fieldPairValue = CustomReportGeneratorHelper.getPairValue(fieldAlias, pairs);

        if (fieldPairValue != null && !"".equals(fieldPairValue)) {
            rowValue = fieldPairValue.toString();

            if (FinanceConstants.InvoiceType.Invoice.getConstantAsString().equals(rowValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Invoice.type.invoice"), fieldPairValue);
            } else if (FinanceConstants.InvoiceType.CreditNote.getConstantAsString().equals(rowValue)) {
                fvObj = new FieldValue(resourceBundleWrapper.getMessage("Invoice.type.creditNote"), fieldPairValue);
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