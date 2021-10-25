package com.piramide.elwis.cmd.common;

import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FormatUtils;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Read category values for generate document
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class ReadCategoryDocumentValues {

    private Integer companyId;
    private Locale locale;

    public ReadCategoryDocumentValues(Integer companyId, Locale locale) {
        this.companyId = companyId;
        this.locale = locale;
    }

    public Map getAddressCategoryValues(Integer addressId, String fieldPrefix) throws FinderException {
        Map fieldMap = new HashMap();

        CategoryFieldValueHome categoryFieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        Collection collection = categoryFieldValueHome.findByAddressId(addressId, companyId);
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            CategoryFieldValue categoryFieldValue = (CategoryFieldValue) iterator.next();

            Category category = findCategory(categoryFieldValue.getCategoryId());
            if (category != null) {
                String value = readCategoryValue(categoryFieldValue, category);
                addFieldValue(fieldMap, fieldPrefix, category, value);
            }
        }

        return fieldMap;
    }

    private void addFieldValue(Map fieldMap, String fieldPrefix, Category category, String value) {

        String fieldKey = composeFieldKey(fieldPrefix, category.getCategoryId());

        if (fieldMap.containsKey(fieldKey) && CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()) {
            fieldMap.put(fieldKey, fieldMap.get(fieldKey) + "\n" + value);
        } else {
            fieldMap.put(fieldKey, value);
        }
    }

    private String composeFieldKey(String fieldPrefix, Integer categoryId) {
        return fieldPrefix + categoryId.toString();
    }

    private String readCategoryValue(CategoryFieldValue fieldValue, Category category) throws FinderException {
        Object value = null;

        if (category != null) {

            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()
                    || CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == category.getCategoryType()) {

                CategoryValue categoryValue = findCategoryValue(fieldValue.getCategoryValueId());
                if (categoryValue != null) {
                    value = categoryValue.getCategoryValueName();
                }
            } else if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == category.getCategoryType()) {
                value = dateValidValue(fieldValue.getDateValue(), locale.getLanguage());

            } else if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == category.getCategoryType()) {
                value = decimalValidValue(fieldValue.getDecimalValue(), locale.getLanguage());

            } else if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == category.getCategoryType()) {
                value = fieldValue.getIntegerValue();

            } else if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == category.getCategoryType()) {
                value = fieldValue.getStringValue();

            } else if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == category.getCategoryType()) {
                value = fieldValue.getLinkValue();

            } else if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == category.getCategoryType()) {
                value = null;
                if (null != fieldValue.getFreeTextId()) {
                    value = readFreeText(fieldValue.getFreeTextId());
                }
            } else if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == category.getCategoryType()) {
                value = "";
                if (null != fieldValue.getFilename()) {
                    value = fieldValue.getFilename();
                }
            }
        }

        return validValue(value);
    }

    private String validValue(Object value) {
        return value != null ? value.toString() : "";
    }

    private String dateValidValue(Date date, String isoLanguage) {
        String dateValue = "";
        if (date != null) {
            dateValue = DateUtils.parseDate(date, SystemPattern.getDatePattern(isoLanguage));
        }
        return dateValue;
    }

    private String dateValidValue(Integer dateAsInteger, String isoLanguage) {
        String dateValue = "";
        if (dateAsInteger != null) {
            dateValue = dateValidValue(DateUtils.integerToDate(dateAsInteger), isoLanguage);
        }
        return dateValue;
    }

    private String decimalValidValue(BigDecimal decimal, String isoLanguage) {
        String value = "";
        if (decimal != null) {
            String decimalPattern = SystemPattern.getDecimalPattern(isoLanguage);
            value = validValue(FormatUtils.formatDecimal(decimal, new Locale(isoLanguage), decimalPattern));
        }
        return value;
    }

    private String readFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        String text = "";
        if (freeTextId != null) {
            try {
                FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
                if (freeText.getValue() != null) {
                    text = new String(freeText.getValue());
                }
            } catch (FinderException e) {
            }
        }
        return text;
    }

    private Category findCategory(Integer categoryId) throws FinderException {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        return categoryHome.findByPrimaryKey(categoryId);
    }

    private CategoryValue findCategoryValue(Integer categoryValueId) throws FinderException {
        CategoryValueHome categoryValueHome = (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        return categoryValueHome.findByPrimaryKey(categoryValueId);
    }
}
