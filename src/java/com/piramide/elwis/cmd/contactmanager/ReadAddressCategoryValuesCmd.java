package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.CategoryValueDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Read all category values related to address/contact person, this to contact summary report
 *
 * @author Miky
 * @version $Id: ReadAddressCategoryValuesCmd.java 22-jul-2008 11:42:01 $
 */
public class ReadAddressCategoryValuesCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ReadAddressCategoryValuesCmd....." + paramDTO);

        boolean isContactPerson = ("isContactPerson".equals(getOp()));

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String isoLanguage = null;
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer contactPersonId = null;
        if (isContactPerson) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        CategoryFieldValueHome fieldValueHome = (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        //executes finder method
        Collection catFieldValues = null;
        try {
            if (isContactPerson) {
                catFieldValues = fieldValueHome.findByAddressIdAndContactPersonId(addressId, contactPersonId, companyId);
            } else {
                catFieldValues = fieldValueHome.findByAddressId(addressId, companyId);
            }
        } catch (FinderException e) {
            log.debug("Not found values.. " + e);
            catFieldValues = new ArrayList();
        }

        List<Map> categoryValuesList = new ArrayList<Map>();
        for (Iterator it = catFieldValues.iterator(); it.hasNext();) {
            CategoryFieldValue fieldValue = (CategoryFieldValue) it.next();

            //read category associated
            Category category = null;
            try {
                category = categoryHome.findByPrimaryKey(fieldValue.getCategoryId());
            } catch (FinderException e) {
                log.debug("Not found category...");
            }
            if (category != null) {

                //setting up object value according type of category
                Object value = null;
                if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()
                        || CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == category.getCategoryType()) {
                    CategoryValue categoryValue = (CategoryValue) ExtendedCRUDDirector.i.read(new CategoryValueDTO(fieldValue.getCategoryValueId()), new ResultDTO(), false);
                    if (categoryValue != null) {
                        value = categoryValue.getCategoryValueName();
                    }

                } else if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getDateValue();
                } else if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == category.getCategoryType()) {
                    value = fieldValue.getDecimalValue();
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

                addCategoryValue(categoryValuesList, category, value, isoLanguage);
            }
        }

        resultDTO.put("listCategoryValues", categoryValuesList);
    }

    private void addCategoryValue(List<Map> categoryValueList, Category category, Object value, String isoLanguage) {

        boolean exist = false;
        if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType()) {
            //construct multiple element values
            for (Map categoryMap : categoryValueList) {
                if (category.getCategoryId().equals(categoryMap.get("categoryId"))) {
                    exist = true;
                    categoryMap.put("value", categoryMap.get("value") + "\n" + value);
                    break;
                }
            }
        }

        if (!exist) {
            Map categoryMap = new HashMap();
            categoryMap.put("categoryId", category.getCategoryId());
            categoryMap.put("name", getCategoryName(category, isoLanguage));
            categoryMap.put("type", category.getCategoryType());
            categoryMap.put("value", value);

            categoryValueList.add(categoryMap);
        }
    }

    private String readFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome =
                (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        String text = "";
        try {
            FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
            text = new String(freeText.getValue());
        } catch (FinderException e) {
            log.debug("->Read FreeText freeTextId=" + freeTextId + " FAIL");
        }

        return text;
    }

    private String getCategoryName(Category category, String isoLanguage) {
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        // read the default translation for category name
        String translation = "";
        try {
            translation = langTextHome.findByLangTextIdAndLanguageRelatedToUI(category.getLangTextId(), isoLanguage).getText();
        } catch (FinderException e) {
            try {
                translation = langTextHome.findByIsDefault(category.getLangTextId()).getText();
            } catch (FinderException e1) {
                translation = category.getCategoryName();
            }
        }
        return translation;
    }


    public boolean isStateful() {
        return false;
    }
}
