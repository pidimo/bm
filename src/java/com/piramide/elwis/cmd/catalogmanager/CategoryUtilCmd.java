package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.CategoryFieldValueDTO;
import com.piramide.elwis.dto.catalogmanager.CategoryTabDTO;
import com.piramide.elwis.dto.catalogmanager.categoryUtil.WebCategoryGroup;
import com.piramide.elwis.dto.catalogmanager.categoryUtil.WebCategoryTab;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.CategoryUtil;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Author: ivan
 * Date: Oct 9, 2006 - 11:18:53 AM
 */
public class CategoryUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public static final String categoryDtoConstant = "category_";

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(SessionContext ctx) " + paramDTO);
        String op = this.getOp();

        if ("readCategoryRelations".equals(op)) {
            Integer categoryValueId = (Integer) paramDTO.get("categoryValueId");
            readCategoryRelations(categoryValueId);
        }

        if ("checkRelation".equals(op)) {
            Integer categoryValueId = (Integer) paramDTO.get("categoryValueId");
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            checkRelation(categoryValueId, categoryId);
        }

        if ("downloadAttach".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer attachId = (Integer) paramDTO.get("attachId");
            downloadAttach(attachId, companyId);
        }

        if ("readCategoryWithoutGroups".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            String table = (String) paramDTO.get("table");
            readCategoryWithoutGroups(table, companyId);
        }
        if ("readGroupsWithoutTabs".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            String table = (String) paramDTO.get("table");
            readGroupsWithoutTabs(table, companyId);
        }

        if ("readChildrenCategories".equals(op)) {
            Integer categoryId = Integer.valueOf(paramDTO.get("categoryValueId").toString());
            readChildrenCategories(categoryId);
        }
        if ("buildCategoryTabs".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            String table = (String) paramDTO.get("table");
            String finderName = (String) paramDTO.get("finderName");
            Object[] params = (Object[]) paramDTO.get("params");
            buildCategoryTabs(table, companyId, finderName, params);
        }
        if ("buildCategoryTab".equals(op)) {
            Integer categoryTabId = (Integer) paramDTO.get("categoryTabId");
            buildCategoryTab(categoryTabId);
        }
        if ("buildUI".equals(op)) {
            buildIU();
        }
        if ("readCategory".equals(op)) {
            readCategory();
        }
        if ("readCategoryValue".equals(op)) {
            readCategoryValue();
        }
        if ("createValues".equals(op)) {
            createCategoryFieldValues();
        }
        if ("readCAtegoryFieldValues".equals(op)) {
            readCategoryFieldValues();
        }
        if ("updateValues".equals(op)) {
            updateValues();
        }
        if ("deleteValues".equals(op)) {
            deleteValues();
        }

    }

    private boolean checkRelation(Integer categoryValueId,
                                  Integer categoryId) {

        List<Integer> relations = readCategoryRelations(categoryValueId);
        Boolean result = relations.contains(categoryId);
        resultDTO.put("checkRelation", result);
        return result;
    }

    private void readCategoryWithoutGroups(String table, Integer companyId) {
        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        List<Integer> categoriesIds = new ArrayList<Integer>();
        try {
            String secondTable = (CategoryComposedUtil.paramHasSecondTable(paramDTO) ? CategoryComposedUtil.getSecondTable(paramDTO) : table);
            Collection categories = categoryHome.selectCategoriesWithOutGroup(table, secondTable, companyId);
            for (Object object : categories) {
                Category category = (Category) object;
                categoriesIds.add(category.getCategoryId());
            }
        } catch (FinderException e) {
            log.debug("->Execute selectCategoriesWithOutGroup FAIL");
        }
        resultDTO.put("categoriesWithoutGroup", categoriesIds);
    }

    private void readGroupsWithoutTabs(String table, Integer companyId) {
        CategoryGroupHome categoryGroupHome =
                (CategoryGroupHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYGROUP);

        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        List<WebCategoryGroup> webGroups = new ArrayList<WebCategoryGroup>();
        Collection groups = null;
        try {
            groups = categoryGroupHome.findGroupsWithoutTabs(table, companyId);
        } catch (FinderException e) {
            log.debug("->Execute findGroupsWithoutTabs table=" + table + " companyId=" + companyId + " FAIL");
        }
        if (null != groups) {
            for (Object object : groups) {
                CategoryGroup categoryGroup = (CategoryGroup) object;

                Collection categories = null;
                try {
                    categories = categoryHome.findCategoriesByGroup(categoryGroup.getCategoryGroupId(), categoryGroup.getCompanyId());
                } catch (FinderException e) {
                    log.debug("->Read categories categoryGroupId=" + categoryGroup.getCategoryGroupId() + " FAIL");
                }

                if (null == categories || categories.isEmpty()) {
                    continue;
                }

                WebCategoryGroup webCategoryGroup = new WebCategoryGroup();
                webCategoryGroup.setCategoryGroupId(categoryGroup.getCategoryGroupId());
                webCategoryGroup.setLabel(categoryGroup.getLabel());
                webCategoryGroup.setTable(categoryGroup.getTable());

                boolean containOnlySubcategories = true;
                if (null != categories) {
                    for (Object categoryObject : categories) {
                        Category category = (Category) categoryObject;
                        webCategoryGroup.addCategory(category.getCategoryId());
                        if (null == category.getParentCategory()) {
                            containOnlySubcategories = false;
                        }
                    }
                }
                webCategoryGroup.setOnlySubCategories(containOnlySubcategories);
                webGroups.add(webCategoryGroup);
            }
        }

        resultDTO.put("categoryGroups", webGroups);
    }


    private void buildCategoryTabs(String table, Integer companyId, String finderName, Object[] params) {
        CategoryTabHome tabHome =
                (CategoryTabHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYTAB);

        CategoryFieldValueHome categoryFieldValueHome =
                (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        List<CategoryTabDTO> categoryTabs = new ArrayList<CategoryTabDTO>();
        try {
            Collection tabs = tabHome.findTabsByTable(table, companyId);
            if (null != tabs) {
                for (Object object : tabs) {
                    CategoryTab tab = (CategoryTab) object;

                    boolean showTab = false;
                    if (null != tab.getCategoryGroups() && !tab.getCategoryGroups().isEmpty()) {
                        for (Object groupObject : tab.getCategoryGroups()) {
                            CategoryGroup categoryGroup = (CategoryGroup) groupObject;

                            Collection categories = null;
                            try {
                                categories = categoryHome.findCategoriesByGroup(categoryGroup.getCategoryGroupId(), categoryGroup.getCompanyId());
                            } catch (FinderException e) {
                                log.debug("->Read categories categoryGroupId=" + categoryGroup.getCategoryGroupId() + " FAIL");
                            }

                            if (null != categories && !categories.isEmpty()) {
                                for (Object categoryObject : categories) {
                                    Category category = (Category) categoryObject;
                                    if (null == category.getParentCategory()) {
                                        showTab = true;
                                        break;
                                    } else {
                                        Collection relations = category.getCategoryRelations();
                                        if (null != relations) {
                                            for (Object obj : relations) {
                                                CategoryRelation categoryRelation = (CategoryRelation) obj;
                                                categoryRelation.getCategoryValueId();
                                                LinkedList orderedParams = new LinkedList();
                                                orderedParams.addAll(Arrays.asList(params));
                                                orderedParams.add(category.getCompanyId());
                                                orderedParams.add(categoryRelation.getCategoryValueId());

                                                Collection values =
                                                        (Collection) EJBFactory.i.callFinder(
                                                                new CategoryFieldValueDTO(),
                                                                finderName,
                                                                orderedParams.toArray());
                                                if (null != values && !values.isEmpty()) {
                                                    showTab = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (showTab) {
                                    break;
                                }
                            }
                        }
                    }

                    if (showTab) {
                        CategoryTabDTO dto = new CategoryTabDTO();
                        DTOFactory.i.copyToDTO(tab, dto);
                        categoryTabs.add(dto);
                    }
                }
            }

        } catch (FinderException e) {
            log.debug("->Read Tabs table=" + table + " companyId=" + companyId + " FAIL");
        }

        resultDTO.put("categoryTabs", categoryTabs);
        log.debug("->Read categoryTabs table=" + table + " companyId=" + companyId + " OK");
    }

    private void buildCategoryTab(Integer categoryTabId) {
        CategoryTabHome categoryTabHome =
                (CategoryTabHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYTAB);

        CategoryGroupHome categoryGroupHome =
                (CategoryGroupHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYGROUP);

        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        CategoryTab categoryTab = null;
        try {
            categoryTab = categoryTabHome.findByPrimaryKey(categoryTabId);
        } catch (FinderException e) {
            log.debug("->Read CategoryTab categoryTabId=" + categoryTabId + " FAIL");
        }

        if (null != categoryTab) {
            WebCategoryTab webCategoryTab = new WebCategoryTab();
            webCategoryTab.setCategoryTabId(categoryTab.getCategoryTabId());
            webCategoryTab.setSequence(categoryTab.getSequence());
            webCategoryTab.setTable(categoryTab.getTable());

            Collection categoryGroups = null;
            try {
                categoryGroups =
                        categoryGroupHome.findGroupsByTab(categoryTab.getCategoryTabId(),
                                categoryTab.getCompanyId());
            } catch (FinderException e) {
                log.debug("->Read CategoryGroups categoryTabId=" + categoryTab.getCategoryTabId() + " FAIL");
            }

            if (null != categoryGroups) {
                for (Object object : categoryGroups) {
                    CategoryGroup categoryGroup = (CategoryGroup) object;

                    Collection categories = null;
                    try {
                        categories = categoryHome.findCategoriesByGroup(categoryGroup.getCategoryGroupId(), categoryGroup.getCompanyId());
                    } catch (FinderException e) {
                        log.debug("->Read categories categoryGroupId=" + categoryGroup.getCategoryGroupId() + " FAIL");
                    }

                    if (null == categories || categories.isEmpty()) {
                        continue;
                    }

                    WebCategoryGroup webCategoryGroup = new WebCategoryGroup();
                    webCategoryGroup.setCategoryGroupId(categoryGroup.getCategoryGroupId());
                    webCategoryGroup.setLabel(categoryGroup.getLabel());
                    webCategoryGroup.setTable(categoryGroup.getTable());

                    boolean containOnlySubcategories = true;
                    if (null != categories) {
                        for (Object categoryObject : categories) {
                            Category category = (Category) categoryObject;
                            webCategoryGroup.addCategory(category.getCategoryId());
                            if (null == category.getParentCategory()) {
                                containOnlySubcategories = false;
                            }
                        }
                    }
                    webCategoryGroup.setOnlySubCategories(containOnlySubcategories);
                    webCategoryTab.addCategoryGroup(webCategoryGroup);
                }
            }

            resultDTO.put("categoryTab", webCategoryTab);
        }
    }

    private void readChildrenCategories(Integer categoryValueId) {
        CategoryValueHome categoryValueHome =
                (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        CategoryValue categoryValue = null;
        try {
            categoryValue = categoryValueHome.findByPrimaryKey(categoryValueId);
        } catch (FinderException e) {
            log.debug("->Read CategoryValue categoryValueId=" + categoryValueId);
        }

        if (null == categoryValue) {
            resultDTO.put("childrenCategories", new ArrayList<Integer>());
            return;
        }

        CategoryRelationHome categoryRelationHome =
                (CategoryRelationHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYRELATION);

        List<Integer> childrenCategoryIds = new ArrayList<Integer>();
        try {
            Collection childrenCategories =
                    categoryRelationHome.findByCategoryValue(categoryValue.getCategoryValueId(), categoryValue.getCompanyId());
            if (null != childrenCategories) {
                for (Object object : childrenCategories) {
                    CategoryRelation categoryRelation = (CategoryRelation) object;
                    childrenCategoryIds.add(categoryRelation.getCategoryId());
                }
            }
        } catch (FinderException e) {
            log.debug("->Read CategoryRelations categoryValueId" + categoryValueId + " FAIL");
        }
        resultDTO.put("childrenCategoryIds", childrenCategoryIds);
    }


    private void deleteValues() {
        String finderName = (String) paramDTO.get("finderName");
        Object[] params = ((List) paramDTO.get("params")).toArray();

        //recover all values can be assigned
        CategoryFieldValueDTO dto = new CategoryFieldValueDTO();
        Collection values = new ArrayList((Collection) EJBFactory.i.callFinder(dto, finderName, params));

        for (Iterator it = values.iterator(); it.hasNext();) {
            CategoryFieldValue v = (CategoryFieldValue) it.next();
            try {
                v.remove();
            } catch (RemoveException re) {
                log.error("Cannot remove CategoryFieldValue Object because \n", re);
            }
        }
    }

    private void updateValues() {

        //read sourceValues and companyId
        List<Map> sourceValues = (List<Map>) paramDTO.get("sourceValues");
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        String finderName = (String) paramDTO.get("finderName");
        Object[] params = ((List) paramDTO.get("params")).toArray();

        //filter categoryFields from paramDTO
        List<Map> categoryFields = CategoryUtil.recoverCategoryValues(paramDTO);

        //recover all values can be assigned
        CategoryFieldValueDTO dto = new CategoryFieldValueDTO();
        Collection values = new ArrayList((Collection) EJBFactory.i.callFinder(dto, finderName, params));

        if ("true".equals(paramDTO.get("checkVersion"))) {
            Integer actualVersion = calculateGeneraVesion(values);
            Integer pageVersion = Integer.valueOf(paramDTO.get("categoryTabVersion").toString());
            log.debug("-> actualVersion " + actualVersion);
            if (!actualVersion.equals(pageVersion)) {
                resultDTO.addResultMessage("Common.error.concurrency");
                resultDTO.setForward("Fail");
                return;
            }
        }

        //list that contain all elements can be deleted
        List elementsToDelete = new ArrayList();

        //for every element can exists in UI
        for (Map m : categoryFields) {
            int type = new Integer(m.get("type").toString());
            int categoryId = new Integer(m.get("categoryId").toString());
            Object value = m.get("value");
            String attachId = (String) m.get("attachId");

            //update  old values
            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() != type) {
                updateSingleSelectValues(values,
                        categoryId,
                        type,
                        value,
                        attachId,
                        companyId,
                        sourceValues,
                        elementsToDelete);
            } else {
                List<String> keys = (List<String>) value;
                updateCompoundSelectValues(values,
                        categoryId,
                        keys,
                        sourceValues,
                        companyId,
                        elementsToDelete);
            }
        }

        List subCategoriesToDelete = new ArrayList();
        for (int i = 0; i < elementsToDelete.size(); i++) {
            CategoryFieldValue categoryFieldValue = (CategoryFieldValue) elementsToDelete.get(i);

            if (null == categoryFieldValue.getCategoryValueId()) {
                continue;
            }

            List<Integer> relations = readCategoryRelations(categoryFieldValue.getCategoryValueId());
            if (!relations.isEmpty()) {
                for (Object object : values) {
                    CategoryFieldValue element = (CategoryFieldValue) object;
                    if (relations.contains(element.getCategoryId()) && !elementsToDelete.contains(element)) {
                        subCategoriesToDelete.add(element);
                    }
                }
            }
        }

        elementsToDelete.addAll(subCategoriesToDelete);

        elementsToDelete = removeDuplicatedElements(elementsToDelete);

        //remove unused elements
        for (int i = 0; i < elementsToDelete.size(); i++) {
            CategoryFieldValue v = (CategoryFieldValue) elementsToDelete.get(i);

            if (null != v.getFreeTextId()) {
                try {
                    Integer freeTextId = v.getFreeTextId();
                    v.setFreeTextId(null);
                    deleteFreeText(freeTextId);
                } catch (RemoveException e) {
                    log.debug("->Delete Freetext for category field value FAIL,\n" +
                            "now cannot delete catefory field value", e);
                    continue;
                }
            }

            if (null != v.getAttachId()) {
                try {
                    Integer freeTextId = v.getAttachId();
                    v.setAttachId(null);
                    deleteFreeText(freeTextId);
                } catch (RemoveException e) {
                    log.debug("->Delete Freetext for category field value FAIL,\n" +
                            "now cannot delete catefory field value", e);
                    continue;
                }
            }

            try {
                v.remove();
            } catch (RemoveException re) {
                log.error("Cannot remove categoryFieldValue because \n", re);
            }
        }
    }

    private List<Integer> readCategoryRelations(Integer categoryValueId) {
        List<Integer> result = new ArrayList<Integer>();
        CategoryValueHome categoryValueHome =
                (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);

        CategoryValue categoryValue = null;
        try {
            categoryValue = categoryValueHome.findByPrimaryKey(categoryValueId);
        } catch (FinderException e) {
            log.debug("->Read CategoryValue categoryValueId=" + categoryValueId + " FAIL");
        }

        if (null == categoryValue) {
            resultDTO.put("categoryValueRelations", result);
            return result;
        }

        if (null != categoryValue.getCategoryRelations()) {
            Collection relations = categoryValue.getCategoryRelations();
            for (Object object : relations) {
                CategoryRelation categoryRelation = (CategoryRelation) object;
                result.add(categoryRelation.getCategoryId());
            }
        }
        resultDTO.put("categoryValueRelations", result);
        return result;
    }

    private void updateCompoundSelectValues(Collection actualCategoryFieldValues,
                                            int categoryId,
                                            List<String> keys,
                                            List<Map> sourceValues,
                                            int companyId,
                                            List elementsToDelete) {

        for (Object actualCategoryFieldValue : actualCategoryFieldValues) {
            CategoryFieldValue v = (CategoryFieldValue) actualCategoryFieldValue;
            if (v.getCategoryId() == categoryId && !elementsToDelete.contains(v)) {
                elementsToDelete.add(v);
            }
        }
        createMultipleValues(categoryId, keys, sourceValues, companyId);
    }


    private void updateSingleSelectValues(Collection actualCategoryFieldValues,
                                          int categoryId,
                                          int type,
                                          Object value,
                                          String attachId,
                                          int companyId,
                                          List<Map> sourceValues,
                                          List elementsToDelete) {
        boolean exists = false;

        for (Object actualCategoryFieldValue : actualCategoryFieldValues) {
            CategoryFieldValue v = (CategoryFieldValue) actualCategoryFieldValue;

            if (v.getCategoryId() == categoryId) {
                exists = true;

                if ("".equals(value.toString().trim())) {
                    if (null == attachId) {
                        elementsToDelete.add(v);
                    }
                    break;
                }

                //the value is scheduled for remove
                if (elementsToDelete.contains(v)) {
                    exists = false;
                    break;
                }

                //update single select value
                if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == type) {
                    Integer actualValue = v.getCategoryValueId();
                    Integer newValue = new Integer(value.toString());

                    if (!actualValue.equals(newValue)) {
                        log.debug("->Change value for categoryId=" + categoryId);
                        elementsToDelete.add(v);

                        List<Integer> relationIds = readCategoryRelations(v.getCategoryValueId());
                        log.debug("->Category relations=" + relationIds);

                        List relatedValues = filterRelatedValues(relationIds, (List) actualCategoryFieldValues);
                        log.debug("->Number of values associated " + relatedValues.size());
                        elementsToDelete.addAll(relatedValues);

                        createSingleValues(categoryId, type, value, sourceValues, companyId);
                    }
                }

                //update date value
                if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == type) {
                    v.setDateValue(new Integer(value.toString()));
                }

                //update decimal value
                if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == type) {
                    v.setDecimalValue(new BigDecimal(value.toString()));
                }

                //update integer value
                if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == type) {
                    v.setIntegerValue(new Integer(value.toString()));
                }

                //updade text value
                if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == type) {
                    v.setStringValue(value.toString());
                }

                //update link value
                if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == type) {
                    v.setLinkValue(value.toString());
                }

                //update freeText value
                if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == type) {
                    byte[] content = value.toString().getBytes();
                    updateFreeText(v.getFreeTextId(), content);
                }

                //update attachment value
                if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == type) {
                    ArrayByteWrapper wrapper = (ArrayByteWrapper) value;
                    v.setFilename(wrapper.getFileName());
                    updateFreeText(v.getAttachId(), wrapper.getFileData());
                }

                //update version for categoryFieldvalue
                v.setVersion(v.getVersion() + 1);
                break;
            }
        }
        if (!exists) {
            createSingleValues(categoryId, type, value, sourceValues, companyId);
        }
    }

    private List filterRelatedValues(List<Integer> relationIds, List categoryFieldValues) {
        List result = new ArrayList();
        for (Object object : categoryFieldValues) {
            CategoryFieldValue categoryFieldValue = (CategoryFieldValue) object;
            if (relationIds.contains(categoryFieldValue.getCategoryId())) {
                result.add(categoryFieldValue);
            }
        }

        return result;
    }

    /**
     * This method reads the values corresponding to the dynamic fields according to its label
     * (products, clients, etc)
     * the recovered objects are of CategoryFieldValue type, soon to be process according to their category
     * and to be assigned to its corresponding part in the IU user interface.
     * that is to say, that believes in resultDTO
     * the key and value  KEY = VALUE
     * <p/>
     * KEY = ?category_Category.categoryId_Category.categotyType_Category.categoryName?
     * VALUE = according to the type of category, can be whole, decimal, date, chain,
     * the Identifier of a CategoryValue object (simple selection)
     * or a set of identifiers of CategoryValues objects (multiple selection).
     * <p/>
     * This method make his work, it is necessary that in the Object paramDTO is sent
     * the name to him of the Finder method and the all values for finder method can be executed
     * <p/>
     * for example
     * findByProductId (to recover the values of a certain product)
     * and params are list (productId,companyId) an a list object
     */
    private void readCategoryFieldValues() {

        LangTextHome h2 = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        CategoryHome h = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        //recover from paramDTO finderName and params in order to execute finder method
        String finderName = (String) paramDTO.get("finderName");
        Object[] params = ((List) paramDTO.get("params")).toArray();

        //executes finder method
        CategoryFieldValueDTO dto = new CategoryFieldValueDTO();
        Collection catFieldValues = (Collection) EJBFactory.i.callFinder(dto, finderName, params);

        Map cache = new HashMap();

        Integer generalVesion = calculateGeneraVesion(catFieldValues);
        for (Iterator it = catFieldValues.iterator(); it.hasNext();) {
            CategoryFieldValue v = (CategoryFieldValue) it.next();

            //read category associated to value "v"
            Category c = null;
            try {
                c = h.findByPrimaryKey(v.getCategoryId());
            } catch (FinderException e) {
            }
            if (null != c) {
                // read the default translation for category name
                String translation = "";
                try {
                    translation = h2.findByIsDefault(c.getLangTextId()).getText();
                } catch (FinderException e) {
                    translation = c.getCategoryName();
                }

                //setting up object value according type of category
                Object value = null;
                if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getCategoryValueId();
                }
                if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getCategoryValueId();
                }
                if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getDateValue();
                }
                if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getDecimalValue();
                }
                if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getIntegerValue();
                }
                if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getStringValue();
                }
                if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == c.getCategoryType()) {
                    value = v.getLinkValue();
                }
                if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == c.getCategoryType()) {
                    value = null;
                    if (null != v.getFreeTextId()) {
                        value = readFreeText(v.getFreeTextId());
                    }
                }
                if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == c.getCategoryType()) {
                    value = null;
                    if (null != v.getAttachId()) {
                        value = v.getAttachId();
                    }
                }
                //creates map object for the User interface
                putInResultDTOValues(c.getCategoryId(), c.getCategoryType(), translation, value, cache);
            }
        }
        resultDTO.putAll(cache);
        resultDTO.put("categoryTabVersion", generalVesion);
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

    private Integer createFreeText(byte[] content, Integer companyId) {
        if (null == content) {
            return null;
        }

        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        try {
            FreeText freeText = freeTextHome.create(content, companyId, FreeTextTypes.FREETEXT_CATEGORYFIELDVALUE);
            return freeText.getFreeTextId();
        } catch (CreateException e) {
            log.debug("->Create FreeText companyId=" + companyId + " FAIL", e);
        }

        return null;
    }


    private void updateFreeText(Integer freeTextId, byte[] content) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        try {
            FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
            freeText.setValue(content);
        } catch (FinderException e) {
            log.debug("->Read FreeText freeTextId=" + freeTextId + " FAIL");
        }
    }

    private void deleteFreeText(Integer freeTextId) throws RemoveException {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        try {
            FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);
            freeText.remove();
        } catch (FinderException e) {
            log.debug("->Read FreeText freeTextId=" + freeTextId + " FAIL");
        }
    }

    private void downloadAttach(Integer freeTextId, Integer companyId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        CategoryFieldValueHome categoryFieldValueHome =
                (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);
        try {
            FreeText freeText = freeTextHome.findByPrimaryKey(freeTextId);

            InputStream in = new ByteArrayInputStream(freeText.getValue());

            CategoryFieldValue categoryFieldValue = categoryFieldValueHome.findByAttachId(freeTextId, companyId);

            resultDTO.put("attach", in);
            resultDTO.put("attachSize", freeText.getValue().length);
            resultDTO.put("filename", categoryFieldValue.getFilename());
        } catch (FinderException e) {
            log.debug("->Read FreeText freeTextId=" + freeTextId + " FAIL");
            resultDTO.put("attach", null);
        }
    }

    /**
     * This method constructs the key = value  according to the values that arrive to him
     *
     * @param categoryId id of category for select the value in UI
     * @param type       type of category helps to render UI element
     * @param name       name of category if exists error can be showed
     * @param value      value can be selected or showed in UI
     * @param m          Map that contain all key=value elements
     * @return Map that contain all key=value elements
     */
    private Map putInResultDTOValues(int categoryId, int type, String name, Object value, Map m) {

        String dtoName = String.valueOf(categoryId);
        //checks the type of category
        if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == type) {
            //construct multiple select element values for UI
            List l = (List) m.get(dtoName);
            if (null != l) {
                if (!l.contains(value)) {
                    l.add(value);
                }
            } else {
                l = new ArrayList();
                l.add(value);
            }
            m.put(dtoName, l);
        } else {
            //construct single select or single elements for UI
            m.put(dtoName, value);
        }
        return m;
    }

    /**
     * This method creates <code>CategoryFieldValues</code> assigned to dinaminc field according to label
     * (product, client, etc)
     * <p/>
     * in order to make its work thid method requires in the paramDTO sent a List of map object under key "sourceValues"
     * example for create products values
     * <p/>
     * [{identifier = productId, value=aaa} , ...]
     * <p/>
     * identifier is the key that represents productId
     * and value is the value of product can be assigned this values
     */
    private void createCategoryFieldValues() {
        //read sourceValues and companyId
        List<Map> soruceValues = (List<Map>) paramDTO.get("sourceValues");
        Integer companyId = (Integer) paramDTO.get("companyId");

        //filter category fields from paramDTO
        List<Map> categoryFields = CategoryUtil.recoverCategoryValues(paramDTO);
        log.debug("--------<" + categoryFields + ">---------");
        if (null != categoryFields) {
            for (Map m : categoryFields) {
                int categoryId = new Integer((String) m.get("categoryId"));
                int type = new Integer((String) m.get("type"));
                Object value = m.get("value");

                //creates all values for multiple select element
                if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == type) {
                    List<String> l = (List<String>) value;
                    createMultipleValues(categoryId, l, soruceValues, companyId);
                } else
                //create single select according to type of category
                {
                    createSingleValues(categoryId, type, value, soruceValues, companyId);
                }
            }
        }
    }


    /**
     * This method create categoryFieldValue objects for single elements ( integer, date, decimal, etc)
     *
     * @param categoryId   categoryId can be value is assigned
     * @param type         type of category to select the field the value is stored ( integer, date, decimal, string, single select)
     * @param value        value can ve stored
     * @param sourceValues general fields
     * @param companyId    company to that the value belongs
     */
    private void createSingleValues(int categoryId, int type, Object value, List<Map> sourceValues, int companyId) {
        if (!"".equals(value.toString().trim())) {
            CategoryFieldValueDTO dto = new CategoryFieldValueDTO();
            dto.put("categoryId", categoryId);
            dto.put("companyId", companyId);

            for (Map m : sourceValues) {
                String identifier = (String) m.get("identifier");
                int identifierValue = (Integer) m.get("value");
                dto.put(identifier, identifierValue);
            }

            CategoryFieldValue v = (CategoryFieldValue) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            if (null != v) {
                if (CatalogConstants.CategoryType.DATE.getConstantAsInt() == type) {
                    v.setDateValue(new Integer(value.toString()));
                }
                if (CatalogConstants.CategoryType.DECIMAL.getConstantAsInt() == type) {
                    v.setDecimalValue(new BigDecimal(value.toString()));
                }
                if (CatalogConstants.CategoryType.INTEGER.getConstantAsInt() == type) {
                    v.setIntegerValue(new Integer(value.toString()));
                }
                if (CatalogConstants.CategoryType.TEXT.getConstantAsInt() == type) {
                    v.setStringValue(value.toString());
                }
                if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == type) {
                    v.setCategoryValueId(new Integer(value.toString()));
                }
                if (CatalogConstants.CategoryType.LINK_VALUE.getConstantAsInt() == type) {
                    v.setLinkValue(value.toString());
                }
                if (CatalogConstants.CategoryType.FREE_TEXT.getConstantAsInt() == type) {
                    byte[] content = value.toString().getBytes();
                    v.setFreeTextId(createFreeText(content, v.getCompanyId()));
                }
                if (CatalogConstants.CategoryType.ATTACH.getConstantAsInt() == type) {
                    ArrayByteWrapper arrayByteWrapper = (ArrayByteWrapper) value;
                    Integer attachId = createFreeText(arrayByteWrapper.getFileData(), v.getCompanyId());
                    v.setFilename(arrayByteWrapper.getFileName());
                    v.setAttachId(attachId);

                }
            }
        }
    }

    /**
     * This method create categoryFieldValue objects for multiple elements ( integer, date, decimal, etc)
     *
     * @param categoryId   categoryId can be value is assigned
     * @param keys         all keys that are selected
     * @param sourceValues general fields
     * @param companyId    company to that the value belongs
     */
    private void createMultipleValues(int categoryId, List<String> keys, List<Map> sourceValues, int companyId) {
        for (String key : keys) {
            int keyValueAsInt = new Integer(key);

            CategoryFieldValueDTO dto = new CategoryFieldValueDTO();
            dto.put("categoryId", categoryId);
            dto.put("companyId", companyId);

            for (Map m : sourceValues) {
                String identifier = (String) m.get("identifier");
                int identifierValue = (Integer) m.get("value");
                dto.put(identifier, identifierValue);
            }

            CategoryFieldValue v = (CategoryFieldValue) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            if (null != v) {
                v.setCategoryValueId(keyValueAsInt);
            }
        }
    }

    private void readCategoryValue() {
        boolean existsAllCategoryValues = true;
        CategoryValueHome h = (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        List<String> categoryValuesKeys = (List<String>) paramDTO.get("categoryValuesKeys");
        if (null != categoryValuesKeys) {
            for (String s : categoryValuesKeys) {
                int key = -1;
                try {
                    key = new Integer(s);
                } catch (NumberFormatException nfe) {

                }
                if (-1 != key) {
                    try {
                        h.findByPrimaryKey(key);
                    } catch (FinderException e) {
                        existsAllCategoryValues = false;
                        break;
                    }
                }
            }
        } else {
            existsAllCategoryValues = false;
        }

        resultDTO.put("existsAllCategoryValues", existsAllCategoryValues);
    }

    private void readCategory() {
        int categoryId = (Integer) paramDTO.get("categoryId");

        CategoryHome h = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        try {
            Category c = h.findByPrimaryKey(categoryId);
            Map dbCategory = new HashMap();
            dbCategory.put("categoryType", c.getCategoryType());
            dbCategory.put("categoryId", c.getCategoryId());
            resultDTO.put("dbCategory", dbCategory);
            resultDTO.put("existsCategory", true);
        } catch (FinderException e) {
            resultDTO.put("existsCategory", false);
        }
    }

    /**
     * This method reads all the categories by company, according to the label that these have
     */
    private void buildIU() {
        int companyId = (Integer) paramDTO.get("companyId");
        String label = (String) paramDTO.get("label");
        CategoryHome cH = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        List categories = new ArrayList();

        try {
            categories = new ArrayList(cH.findByTableId(label, companyId));
        } catch (FinderException e) {
        }
        if (!categories.isEmpty()) {
            prepareIUStructure(categories);
        }

    }

    private Integer calculateGeneraVesion(Collection values) {
        Integer result = 1;
        if (null != values) {
            for (Object object : values) {
                CategoryFieldValue categoryFieldValue = (CategoryFieldValue) object;

                result += categoryFieldValue.getVersion();
            }
        }

        return result;
    }

    /**
     * This method prepares the structure can help to render IU in view, the structure is
     * an List of Map objects and this structure is returned in ResultDTO object under key
     * "categoryStructure"
     *
     * @param l List of categories can be render in user interface
     */
    private void prepareIUStructure(List l) {
        LangTextHome h = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        List<Map> r = new ArrayList<Map>();
        for (int i = 0; i < l.size(); i++) {
            Map m = new HashMap();

            Category c = (Category) l.get(i);

            //search all categoryValues for compound select or single select categories
            List<Map> values = null;
            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == c.getCategoryType() ||
                    CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == c.getCategoryType()) {
                values = getValuesOfCategory(c);
            }

            //check default translation of category if no exists then show categoryName
            String translation = "";
            try {
                translation = h.findByIsDefault(c.getLangTextId()).getText();
            } catch (FinderException e) {
                translation = c.getCategoryName();
            }

            m.put("categoryName", translation);
            m.put("categoryType", c.getCategoryType());
            m.put("categoryId", c.getCategoryId());
            m.put("companyId", c.getCompanyId());
            m.put("values", values);

            r.add(m);
        }

        resultDTO.put("categoryStructure", r);
    }

    /**
     * this method reads the CategoryValue objects that belong to a certain category,
     * soon creates a structure (ready of Map objects)
     * in which are the main values of categoryValues (identifier and labels).
     *
     * @param c categoria de la cual se leeran los objetos CategoryValue que esta tenga asignada
     * @return List of Map objects that contains information over categoryValues
     */
    private List<Map> getValuesOfCategory(Category c) {
        List<Map> r = new ArrayList<Map>();
        CategoryValueHome h = (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        try {
            List l = new ArrayList(h.findByCategoryId(c.getCategoryId()));
            for (int i = 0; i < l.size(); i++) {
                CategoryValue v = (CategoryValue) l.get(i);

                Map m = new HashMap();
                m.put("categoryValueId", v.getCategoryValueId());
                m.put("categoryValueName", v.getCategoryValueName());
                r.add(m);
            }
        } catch (FinderException e) {
        }

        return r;
    }

    private boolean categoryGroupHasCategories(CategoryGroup categoryGroup) {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        Collection categories = null;
        try {
            categories = categoryHome.findCategoriesByGroup(categoryGroup.getCategoryGroupId(), categoryGroup.getCompanyId());
        } catch (FinderException e) {
            log.debug("->Read categories categoryGroupId=" + categoryGroup.getCategoryGroupId() + " FAIL");
        }

        return (categories != null && !categories.isEmpty());
    }


    private List removeDuplicatedElements(List elementsToDeleteList) {
        List<Integer> idCache = new ArrayList<Integer>();
        List result = new ArrayList();

        for (int i = 0; i < elementsToDeleteList.size(); i++) {
            CategoryFieldValue categoryFieldValue = (CategoryFieldValue) elementsToDeleteList.get(i);
            Integer id = categoryFieldValue.getFieldValueId();
            if (!idCache.contains(id)) {
                idCache.add(id);
                result.add(categoryFieldValue);
            }
        }

        return result;
    }

    public boolean isStateful() {
        return false;
    }
}
