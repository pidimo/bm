package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Ivan
 * @version $Id: CategoryCmd.java 9941 2010-01-13 19:51:26Z ivan $
 */

public class CategoryCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        if ("create".equals(getOp())) {
            isRead = false;
            create(ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(ctx);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }
        if ("readCategory".equals(getOp())) {
            isRead = false;
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            try {
                readCategory(categoryId, ctx);
            } catch (FinderException e) {
                log.debug("-> Read Category categoryId=" + categoryId + " FAIL");
            }
        }

        if ("categoryFieldValueIsInUse".equals(getOp())) {
            isRead = false;
            Integer childCategoryId = EJBCommandUtil.i.getValueAsInteger(this, "childCategoryId");
            Integer categoryValueId = EJBCommandUtil.i.getValueAsInteger(this, "categoryValueId");
            categoryFieldValueIsInUse(childCategoryId, categoryValueId);
        }

        if ("hasSubCategories".equals(getOp())) {
            isRead = false;
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            hasSubCategories(categoryId, ctx);
        }

        if ("isChildrenCategoryInOtherTab".equals(getOp())) {
            isRead = false;
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            Integer childrenCategoryId = (Integer) paramDTO.get("childrenCategoryId");
            isChildrenCategoryInOtherTab(categoryId, childrenCategoryId, ctx);
        }

        if (isRead) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * This method is the one in charge to create a category, to establish his first translation
     * and to keep the description from the category in the table freetext.
     *
     * @param ctx is the object which is required to execute other commands in this case
     *            <code>GeneralTranslationCmd</code>.
     */
    private void create(SessionContext ctx) {
        CategoryDTO dto = new CategoryDTO(paramDTO);
        dto.put("hasSubCategories", hasSubCategoriesFromParamDTO());

        Category category = (Category) ExtendedCRUDDirector.i.create(dto, resultDTO, true);
        if (null != category) {
            settingUpFirstTranslation(ctx, category);
            FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, category, "FreeText", FreeTextHome.class,
                    CatalogConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_CATEGORY, "descriptionText");

            CategoryDTO newCategoryDTO = new CategoryDTO();
            DTOFactory.i.copyToDTO(category, newCategoryDTO);
            resultDTO.putAll(newCategoryDTO);

            List<String> categoryValueIds = (List<String>) paramDTO.get("categoryValueIds");
            log.debug("-> Category has Relations with values " + categoryValueIds);
            for (String categoryValueId : categoryValueIds) {
                if (null == paramDTO.get(categoryValueId)) {
                    continue;
                }

                if (null != paramDTO.get(categoryValueId) &&
                        !"true".equals(paramDTO.get(categoryValueId).toString())) {
                    continue;
                }

                CategoryValueCmd categoryValueCmd = new CategoryValueCmd();
                categoryValueCmd.setOp("createCategoryRelation");
                categoryValueCmd.putParam("companyId", category.getCompanyId());
                categoryValueCmd.putParam("categoryId", category.getCategoryId());
                categoryValueCmd.putParam("categoryValueId", Integer.valueOf(categoryValueId));
                categoryValueCmd.executeInStateless(ctx);
            }
        }
    }

    /**
     * This method updates the category fields
     *
     * @param ctx is the object which is required to execute other commands in this case
     *            <code>GeneralTranslationCmd</code>.
     */
    private void update(SessionContext ctx) {

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.putAll(paramDTO);

        Category oldCategory = (Category) ExtendedCRUDDirector.i.read(categoryDTO, resultDTO, false);

        if (null == oldCategory) {
            resultDTO.setForward("Fail");
            log.debug("-> Read Category for categoryId=" + categoryDTO.getPrimKey() + " FAIL");
            return;
        }

        Integer categoryType = Integer.valueOf(paramDTO.get("categoryType").toString());
        String table = paramDTO.get("table").toString();
        if ((!oldCategory.getCategoryType().equals(categoryType) || !oldCategory.getTable().equals(table)) &&
                (hasAssignedCategoryFieldValues(oldCategory) || hasAssignedCategoryValues(oldCategory))) {
            resultDTO.addResultMessage("Category.error.type.cannotChange");
            resultDTO.setForward("toUpdate");
            read();
            return;
        }

        Boolean hasSubCategories = hasSubCategoriesFromParamDTO();
        if (oldCategory.getHasSubCategories() &&
                !oldCategory.getHasSubCategories().equals(hasSubCategories) &&
                !oldCategory.getChildrenCategories().isEmpty()) {
            resultDTO.addResultMessage("Category.error.hasSubCategories.cannotChange");
            resultDTO.setForward("toUpdate");
            read();
            return;
        }

        Integer categoryGroupId = null;
        if (null != paramDTO.get("categoryGroupId") && !"".equals(paramDTO.get("categoryGroupId").toString())) {
            categoryGroupId = Integer.valueOf(paramDTO.get("categoryGroupId").toString());
        }

        Integer parentCategoryId = null;
        if (null != paramDTO.get("parentCategory") && !"".equals(paramDTO.get("parentCategory").toString())) {
            parentCategoryId = Integer.valueOf(paramDTO.get("parentCategory").toString());
        }

        if (null != oldCategory.getParentCategory() &&
                !oldCategory.getParentCategory().equals(parentCategoryId) &&
                hasAssignedCategoryRelations(oldCategory)) {
            resultDTO.addResultMessage("Category.error.parentCategory.cannotChange");
            resultDTO.setForward("toUpdate");
            read();
            return;
        }

        CategoryDTO dto = new CategoryDTO(paramDTO);
        dto.put("hasSubCategories", hasSubCategoriesFromParamDTO());
        dto.put("categoryGroupId", categoryGroupId);

        //update category fields
        Category updatedCategory = (Category) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");

        //if exists problems to update category (version control failure)
        if (resultDTO.isFailure()) {
            List<String> categoryValueIds = (List<String>) paramDTO.get("categoryValueIds");
            for (String categoryValueId : categoryValueIds) {
                resultDTO.put(categoryValueId, null);
            }
            read();
            return;
        }

        //setting up the first translation if category cannot have translation
        settingUpFirstTranslation(ctx, updatedCategory);

        //update category translation
        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, updatedCategory, "FreeText", FreeTextHome.class,
                CatalogConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_CATEGORY, "descriptionText");

        List<String> actualElements = new ArrayList<String>();
        List<CategoryRelation> relationsToDelete = new ArrayList<CategoryRelation>();

        if (null != updatedCategory.getCategoryRelations()) {
            for (Object object : updatedCategory.getCategoryRelations()) {
                CategoryRelation relation = (CategoryRelation) object;

                Integer categoryValueId = relation.getCategoryValueId();

                if (null != paramDTO.get(categoryValueId.toString())) {
                    actualElements.add(categoryValueId.toString());
                    continue;
                }

                if (!hasCategoryValueUssages(
                        relation.getCategoryValueId(),
                        updatedCategory.getParentCategoryObj().getCategoryFieldValues())
                        ) {
                    relationsToDelete.add(relation);
                }
            }
        }

        for (int i = 0; i < relationsToDelete.size(); i++) {
            CategoryRelation relation = relationsToDelete.get(i);
            try {
                relation.remove();
            } catch (RemoveException e) {
                log.debug("Cannot delete CategoryRelation object categoryValueId=" + relation.getCategoryValueId() +
                        " categoryId=" + relation.getCategoryId());
            }
        }

        List<String> categoryValueIds = (List<String>) paramDTO.get("categoryValueIds");
        log.debug("-> Category has Relations with values " + categoryValueIds);

        for (String categoryValueId : categoryValueIds) {
            if (actualElements.contains(categoryValueId)) {
                continue;
            }

            if (null == paramDTO.get(categoryValueId)) {
                continue;
            }

            if (null != paramDTO.get(categoryValueId) &&
                    !"true".equals(paramDTO.get(categoryValueId).toString())) {
                continue;
            }

            CategoryValueCmd categoryValueCmd = new CategoryValueCmd();
            categoryValueCmd.setOp("createCategoryRelation");
            categoryValueCmd.putParam("companyId", updatedCategory.getCompanyId());
            categoryValueCmd.putParam("categoryId", updatedCategory.getCategoryId());
            categoryValueCmd.putParam("categoryValueId", Integer.valueOf(categoryValueId));

            categoryValueCmd.executeInStateless(ctx);
        }
    }

    /**
     * This method deletes category, categoryValues and usages in categoryfieldvalues
     */
    private void delete() {
        CategoryDTO dto = new CategoryDTO(paramDTO);

        Category category = (Category) ExtendedCRUDDirector.i.read(dto, resultDTO, true);

        if (null != category && !resultDTO.isFailure()) {


            boolean canDeleteParentCategory =
                    (null != category.getChildrenCategories() &&
                            !category.getChildrenCategories().isEmpty()) &&
                            (hasAssignedCategoryFieldValues(category) ||
                                    hasAssignedCategoryRelations(category));

            if (canDeleteParentCategory) {
                resultDTO.addResultMessage("Category.error.cannotDeleteParent");
                resultDTO.setForward("Fail");
                return;
            }


            Collection categoryFieldValues = category.getCategoryFieldValues();
            if (null != categoryFieldValues) {
                Iterator categoryFieldValuesIterator = categoryFieldValues.iterator();
                while (categoryFieldValuesIterator.hasNext()) {
                    CategoryFieldValue categoryFieldValue = (CategoryFieldValue) categoryFieldValuesIterator.next();
                    try {
                        categoryFieldValue.remove();
                        log.debug("-> Delete CategoryFieldValue object for categoryId=" +
                                category.getCategoryId() +
                                " OK");
                    } catch (RemoveException e) {
                        log.debug("-> Delete CategoryFieldValue object for categoryId=" +
                                category.getCategoryId() +
                                " FAIL");
                    }
                    categoryFieldValuesIterator = categoryFieldValues.iterator();
                }
            }

            Collection relations = category.getCategoryRelations();
            if (null != relations) {
                Iterator iterator = relations.iterator();
                while (iterator.hasNext()) {
                    CategoryRelation relation = (CategoryRelation) iterator.next();

                    try {
                        relation.remove();
                        log.debug("-> Delete CategoryRelation object for categoryId=" +
                                category.getCategoryId() +
                                " OK");
                    } catch (RemoveException e) {
                        log.debug("-> Delete CategoryRelation object for categoryId=" +
                                category.getCategoryId() +
                                " Fail", e);
                    }

                    iterator = relations.iterator();
                }
            }

            Collection categoryValues = category.getCategoryValue();
            if (null != categoryValues) {
                Iterator categoryValuesIterator = categoryValues.iterator();
                while (categoryValuesIterator.hasNext()) {
                    CategoryValue categoryValue = (CategoryValue) categoryValuesIterator.next();
                    try {
                        categoryValue.remove();
                        log.debug("-> Delete CategoryValue object for categoryId=" +
                                category.getCategoryId() + " OK");
                    } catch (RemoveException e) {
                        log.debug("-> Delete CategoryValue object for categoryId=" +
                                category.getCategoryId() + " FAIL");
                    }
                    categoryValuesIterator = categoryValues.iterator();
                }
            }

            //deleting category
            ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");

            if (!"Fail".equals(resultDTO.getForward())) {
                int langtextId = -1;
                String p = (String) paramDTO.get("langTextId");
                try {
                    langtextId = (null != p && !"".equals(p) ? Integer.valueOf(p) : -1);
                } catch (NumberFormatException nfe) {
                }
                if (-1 != langtextId) {

                    //search translations associated to category
                    LangTextHome home = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
                    List l = new ArrayList();
                    try {
                        l = new ArrayList(home.findByLangTextId(langtextId));
                    } catch (FinderException e) {
                    }
                    for (int i = 0; i < l.size(); i++) {
                        LangText ltx = (LangText) l.get(i);
                        try {
                            //delete all translations asociated to category
                            ltx.remove();
                        } catch (RemoveException e) {
                        }
                    }
                }
            }
        } else {
            resultDTO.setForward("Fail");
        }
    }


    /**
     * This method read all information of the category, checks is the category have translations assigned, checks if
     * exists usages of this category on label option ( products, conatact person, customer or contact) and checks if
     * exists usages of type assigned to category ( have categoryValues assigned, have integer or date or text values
     * assigned) and setting up in ResultDTO :
     * <p/>
     * "haveFirstTranslation" true if the category have translations false in other case
     * "canUpdateLabel" true if can be update label of category  from user interface, false in other case
     * "canUpdateType" true if can be update type of category from user interface false in other case.
     * "categoryValuesSize" returns the size of categoryvalues collection if collection is not null and size > 0
     */
    private void read() {
        CategoryDTO dto = new CategoryDTO();
        dto.put("categoryId", paramDTO.get("categoryId"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        dto.put("categoryName", paramDTO.get("categoryName"));

        boolean checkReferences = true;
        if (null != paramDTO.get("withReferences")) {
            checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
        }

        //read category fields
        Category category = (Category) ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);

        if (null == category) {
            return;
        }

        //check if exists first translation assingned
        resultDTO.put("haveFirstTranslation", haveFirstTranslation(category.getLangTextId()));

        //check if the field label have values assigned
        resultDTO.put("canUpdateLabel",
                (!hasAssignedCategoryFieldValues(category)) && !hasAssignedCategoryValues(category));

        //check if the field type have values assigned
        resultDTO.put("canUpdateType", !hasAssignedCategoryFieldValues(category) && !hasAssignedCategoryValues(category));

        //check if category have children categories assigned
        boolean canUpdateHasSubCategories = true;
        if (null != category.getChildrenCategories() && !category.getChildrenCategories().isEmpty()) {
            canUpdateHasSubCategories = false;
        }
        resultDTO.put("canUpdateHasSubCategories", canUpdateHasSubCategories);

        //check if category have categoryRelations assigned
        resultDTO.put("canUpdateParentCategory", !hasAssignedCategoryRelations(category));

        //read the description of category
        FreeTextCmdUtil.i.doCRUD(paramDTO,
                resultDTO,
                category,
                "FreeText",
                FreeTextHome.class,
                CatalogConstants.JNDI_FREETEXT,
                FreeTextTypes.FREETEXT_CATEGORY,
                "descriptionText");

        //check ussages of category, only on delete forward can be executed
        String checkUssages = (String) paramDTO.get("checkUssages");
        if ("true".equals(checkUssages)) {
            //check categoryValues assigned to category
            if (null != category.getCategoryValue() && category.getCategoryValue().size() > 0) {
                resultDTO.put("categoryValuesSize", category.getCategoryValue().size());
                resultDTO.addResultMessage("Category.error.values", String.valueOf(category.getCategoryValue().size()));
            }

            //check if category is parent
            boolean canDeleteParentCategory =
                    (null != category.getChildrenCategories() &&
                            !category.getChildrenCategories().isEmpty()) &&
                            (hasAssignedCategoryFieldValues(category) ||
                                    hasAssignedCategoryRelations(category));

            if (canDeleteParentCategory) {
                resultDTO.addResultMessage("Category.error.cannotDeleteParent");
                resultDTO.setForward("Fail");
                return;
            }
        }

        if (null != category.getCategoryRelations()) {
            for (Object object : category.getCategoryRelations()) {
                CategoryRelation relation = (CategoryRelation) object;
                resultDTO.put(relation.getCategoryValueId().toString(), "true");

                if (null != category.getCategoryFieldValues() &&
                        hasCategoryValueUssages(relation.getCategoryValueId(),
                                category.getParentCategoryObj().getCategoryFieldValues()) &&
                        null != category.getCategoryFieldValues() && !category.getCategoryFieldValues().isEmpty()) {
                    resultDTO.put("canUpdateCategoryValue_" + relation.getCategoryValueId(), false);
                }
            }
        }

    }

    private Boolean hasSubCategoriesFromParamDTO() {
        return null != paramDTO.get("hasSubCategories") && "true".equals(paramDTO.get("hasSubCategories").toString());
    }

    /**
     * This method looks for the first associated translation the category, on the basis of langtextId,
     * establishes the language associated to the translation in the object <code>resultDTO</code>
     *
     * @param langtextId id of the translation associated
     * @return true if exists one translarion , false in other case
     */
    private boolean haveFirstTranslation(Integer langtextId) {

        boolean r;
        LangTextHome ltxHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        try {

            LangText ltx = ltxHome.findByIsDefault(langtextId);

            resultDTO.put("languageId", ltx.getLanguageId());

            r = true;
        } catch (FinderException e) {
            r = false;
        }

        return r;
    }

    private Category readCategory(Integer categoryId, SessionContext ctx) throws FinderException {
        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        Category category = categoryHome.findByPrimaryKey(categoryId);

        //check translation to category
        String translation = "";

        if (null != category) {
            LangTextHome langtextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

            try {
                translation = langtextHome.findByIsDefault(category.getLangTextId()).getText();
            } catch (FinderException e) {
                translation = category.getCategoryName();
            }
        }

        CategoryDTO dto = new CategoryDTO();
        DTOFactory.i.copyToDTO(category, dto);
        dto.put("categoryName", translation);

        List<Map> values = new ArrayList<Map>();
        Collection categoryValues = category.getCategoryValue();
        if (null != categoryValues) {
            for (Object object : categoryValues) {
                CategoryValue value = (CategoryValue) object;
                String relations = readCategoryValueRelations(value.getCategoryValueId(), ctx);
                Map valueDTO = new HashMap();
                valueDTO.put("categoryValueId", value.getCategoryValueId());
                valueDTO.put("relations", relations);
                values.add(valueDTO);
            }
        }

        dto.put("hasParticipantInRelations", (null != category.getParentCategory() &&
                null != category.getCategoryRelations() &&
                !category.getCategoryRelations().isEmpty()));

        dto.put("categoryValues", values);

        resultDTO.put("categoryDTO", dto);
        return category;
    }

    private boolean isChildrenCategoryInOtherTab(Integer categoryId,
                                                 Integer childrenCategoryId,
                                                 SessionContext ctx) {
        boolean result = false;

        try {
            Category category = readCategory(categoryId, ctx);
            Category childrenCategory = readCategory(childrenCategoryId, ctx);

            Integer categoryTabId = null;
            Integer childrenCategoryTabId = null;

            if (CategoryComposedUtil.useSecondGroupId(paramDTO, childrenCategory)) {
                if (null != category.getSecondGroupId() && null != category.getSecondCategoryGroup().getCategoryTabId()) {
                    categoryTabId = category.getSecondCategoryGroup().getCategoryTabId();
                }

                if (null != childrenCategory.getSecondGroupId() && null != childrenCategory.getSecondCategoryGroup().getCategoryTabId()) {
                    childrenCategoryTabId = childrenCategory.getSecondCategoryGroup().getCategoryTabId();
                }

            } else {
                if (null != category.getCategoryGroupId() &&
                        null != category.getCategoryGroup().getCategoryTabId()) {
                    categoryTabId = category.getCategoryGroup().getCategoryTabId();
                }

                if (null != childrenCategory.getCategoryGroupId() &&
                        null != childrenCategory.getCategoryGroup().getCategoryTabId()) {
                    childrenCategoryTabId = childrenCategory.getCategoryGroup().getCategoryTabId();
                }
            }

            if (null != categoryTabId &&
                    null != childrenCategoryTabId &&
                    !categoryTabId.equals(childrenCategoryTabId)) {
                result = true;
            }

            if (null != categoryTabId && null == childrenCategoryTabId) {
                result = true;
            }

            if (null == categoryTabId && null != childrenCategoryTabId) {
                result = true;
            }

        } catch (FinderException e) {
            log.debug("-> Read Children or Parent category FAIL");
        }
        resultDTO.put("isChildrenCategoryInOtherTab", result);

        return result;
    }

    private String readCategoryValueRelations(Integer categoryValueId, SessionContext ctx) {
        CategoryUtilCmd categoryUtilCmd = new CategoryUtilCmd();
        categoryUtilCmd.setOp("readCategoryRelations");
        categoryUtilCmd.putParam("categoryValueId", categoryValueId);
        categoryUtilCmd.executeInStateless(ctx);
        List<Integer> relations = (List<Integer>) categoryUtilCmd.getResultDTO().get("categoryValueRelations");

        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        String result = "";
        int index = 0;
        for (Integer categoryId : relations) {
            String childrenGroup = "";
            try {
                Category category = categoryHome.findByPrimaryKey(categoryId);
                if (CategoryComposedUtil.useSecondGroupId(paramDTO, category)) {
                    if (null != category.getSecondGroupId()) {
                        childrenGroup = category.getSecondGroupId().toString();
                    }
                } else {
                    if (null != category.getCategoryGroupId()) {
                        childrenGroup = category.getCategoryGroupId().toString();
                    }
                }
            } catch (FinderException e) {
                log.debug("->Read Children category Fail");
            }
            result += categoryId + ":" + childrenGroup;
            if (index < relations.size() - 1) {
                result += ",";
            }
            index++;
        }

        return result;
    }


    private void hasSubCategories(Integer categoryId, SessionContext ctx) {
        List<CategoryDTO> childrenCategories = new ArrayList<CategoryDTO>();

        try {
            Category category = readCategory(categoryId, ctx);
            resultDTO.put("hasSubCategories", category.getHasSubCategories());
            if (category.getHasSubCategories() && null != category.getChildrenCategories()) {
                for (Object object : category.getChildrenCategories()) {
                    Category childrenCategory = (Category) object;
                    CategoryDTO dto = new CategoryDTO();
                    DTOFactory.i.copyToDTO(childrenCategory, dto);
                    childrenCategories.add(dto);
                }
            }
        } catch (FinderException e) {
            log.debug("->Read Category categoryId=" + categoryId + " FAIL");
            resultDTO.put("hasSubCategories", false);
        }

        resultDTO.put("childrenCategories", childrenCategories);
    }

    private boolean hasAssignedCategoryValues(Category category) {
        return (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == category.getCategoryType() ||
                CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == category.getCategoryType()) &&
                (null != category.getCategoryValue() && !category.getCategoryValue().isEmpty());
    }

    private boolean hasAssignedCategoryRelations(Category category) {
        return (null != category.getCategoryRelations() && !category.getCategoryRelations().isEmpty());
    }


    /**
     * Search <code>CategoryValue</code> ussages in the <code>CategoryFieldValue</code> objects associated to
     * parent <code>Category</code> of child <code>Category</code>.
     * <p/>
     * If the <code>Category</code> object associated to <code>childCategoryId</code> does not have a parent
     * <code>Category</code> the method return <code>false</code> by default.
     * <p/>
     * The same happen if the <code>Category</code> object associated to <code>childCategoryId</code> does not
     * exist in the database.
     *
     * @param childCategoryId <code>Category</code> identifier that have a parent <code>Category</code> assigned.
     * @param categoryValueId <code>CategoryValue</code> identifier.
     * @return true if the <code>CategoryValue</code> is used in the <code>CategoryFieldValue</code> objects
     *         associated to parent <code>Category</code>.
     */
    private boolean categoryFieldValueIsInUse(Integer childCategoryId, Integer categoryValueId) {
        Category childCategory = getCategory(childCategoryId);
        if (null == childCategory) {
            resultDTO.put("categoryFieldValueIsInUse", false);
            return false;
        }

        Category parentCategory = childCategory.getParentCategoryObj();
        if (null == parentCategory) {
            resultDTO.put("categoryFieldValueIsInUse", false);
            return false;
        }

        boolean result = hasCategoryValueUssages(categoryValueId, parentCategory.getCategoryFieldValues());
        resultDTO.put("categoryFieldValueIsInUse", result);
        return result;
    }

    private boolean hasAssignedCategoryFieldValues(Category category) {
        return (null != category.getCategoryFieldValues() && !category.getCategoryFieldValues().isEmpty());
    }

    private boolean hasCategoryValueUssages(Integer categoryValueId, Collection categoryFieldValues) {
        for (Object object : categoryFieldValues) {
            CategoryFieldValue categoryFieldValue = (CategoryFieldValue) object;
            if (categoryValueId.equals(categoryFieldValue.getCategoryValueId())) {
                return true;
            }
        }

        return false;
    }


    /**
     * This method creates the first translation and it associates it to the category
     *
     * @param ctx      <code>SessionContext</code> required to execute <code>EJBCommand</code> objects
     * @param category Object category in which the translation was created
     */
    public void settingUpFirstTranslation(SessionContext ctx, Category category) {
        GeneralTranslationCmd cmd = new GeneralTranslationCmd();
        String operation = this.getOp();

        if (null == category.getLangTextId()) {
            operation = "create";
        }

        Integer langTextId = cmd.setFirstTranslation(ctx,
                paramDTO.get("categoryName").toString(),
                Integer.valueOf(paramDTO.get("languageId").toString()),
                category.getLangTextId(),
                Integer.valueOf(paramDTO.get("companyId").toString()),
                operation);

        if ("create".equals(this.getOp()) || "create".equals(operation)) {
            category.setLangTextId(langTextId);
        }
    }

    private Category getCategory(Integer categoryId) {
        CategoryHome categoryHome =
                (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        try {
            return categoryHome.findByPrimaryKey(categoryId);
        } catch (FinderException e) {
            return null;
        }
    }
}
