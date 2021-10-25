package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.CategoryFieldValueDTO;
import com.piramide.elwis.dto.catalogmanager.CategoryTabDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryTabCmd extends EJBCommand {
    private Log log = LogFactory.getLog(CategoryTabCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        String op = getOp();

        boolean isRead = true;
        if ("create".equals(op)) {
            isRead = false;
            create();
        }
        if ("update".equals(op)) {
            isRead = false;
            update();
        }
        if ("delete".equals(op)) {
            isRead = false;
            delete();
        }
        if ("getTable".equals(op)) {
            Integer categoryTabId = (Integer) paramDTO.get("categoryTabId");
            getTable(categoryTabId);
        }

        if ("checkCategoryTab".equals(op)) {
            Integer categoryTabId = (Integer) paramDTO.get("categoryTabId");
            String finderName = (String) paramDTO.get("finderName");
            Object[] params = (Object[]) paramDTO.get("params");
            checkCategoryTab(categoryTabId, finderName, params);
        }

        if (isRead) {
            read();
        }

    }

    private void create() {
        CategoryTabDTO dto = new CategoryTabDTO();
        dto.putAll(paramDTO);
        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    private void read() {
        CategoryTabDTO dto = new CategoryTabDTO();
        dto.putAll(paramDTO);

        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences") &&
                "true".equals(paramDTO.get("withReferences").toString())) {
            checkReferences = true;
        }

        CategoryTab categoryTab = (CategoryTab) ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
        if (null != categoryTab) {
            resultDTO.put("canUpdateLabel", true);
            if (null != categoryTab.getCategoryGroups() && !categoryTab.getCategoryGroups().isEmpty()) {
                resultDTO.put("canUpdateLabel", false);
            }
        }
    }

    private void update() {
        CategoryTabDTO dto = new CategoryTabDTO();
        dto.putAll(paramDTO);

        CategoryTab categoryTab = (CategoryTab) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        if (null != categoryTab) {
            resultDTO.put("canUpdateLabel", true);
            if (null != categoryTab.getCategoryGroups() && !categoryTab.getCategoryGroups().isEmpty()) {
                resultDTO.put("canUpdateLabel", false);
            }
        }
    }

    private void delete() {
        CategoryTabDTO dto = new CategoryTabDTO();
        dto.putAll(paramDTO);
        ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
    }

    private String getTable(Integer categoryTabId) {
        CategoryTabHome home =
                (CategoryTabHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYTAB);
        try {
            CategoryTab categoryTab = home.findByPrimaryKey(categoryTabId);
            String table = categoryTab.getTable();
            resultDTO.put("getTable", categoryTab.getTable());
            return table;
        } catch (FinderException e) {
            resultDTO.put("getTable", null);
            log.debug("-> Read CategoryTab categoryTabId=" + categoryTabId + " FAIL");
        }
        return null;
    }


    private void checkCategoryTab(Integer categoryTabId, String finderName, Object[] params) {
        CategoryTabHome categoryTabHome =
                (CategoryTabHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYTAB);

        CategoryFieldValueHome categoryFieldValueHome =
                (CategoryFieldValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYFIELDVALUE);

        try {
            CategoryTab categoryTab = categoryTabHome.findByPrimaryKey(categoryTabId);
            if (null == categoryTab.getCategoryGroups() || categoryTab.getCategoryGroups().isEmpty()) {
                resultDTO.put("errorMessage", "CategoryTab.error.NotGroupsAssigned");
                return;
            }

            boolean hasAssignedCategories = false;
            CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
            for (Object object : categoryTab.getCategoryGroups()) {
                CategoryGroup categoryGroup = (CategoryGroup) object;

                Collection categories = null;
                try {
                    categories = categoryHome.findCategoriesByGroup(categoryGroup.getCategoryGroupId(), categoryGroup.getCompanyId());
                } catch (FinderException e) {
                    log.debug("->Read categories categoryGroupId=" + categoryGroup.getCategoryGroupId() + " FAIL");
                    continue;
                }

                if (null != categories && !categories.isEmpty()) {

                    for (Object categoryObject : categories) {
                        Category category = (Category) categoryObject;
                        if (null == category.getParentCategory()) {
                            hasAssignedCategories = true;
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
                                        hasAssignedCategories = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (hasAssignedCategories) {
                        break;
                    }
                }
            }

            if (!hasAssignedCategories) {
                resultDTO.put("errorMessage", "CategoryTab.error.NotGroupsAssigned");
                return;
            }

            resultDTO.setForward("Success");
        } catch (FinderException e) {
            log.debug("->Read CategoryTab categoryTabId=" + categoryTabId + " FAIL");
            resultDTO.put("errorMessage", "CategoryTab.error.notFound");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
