package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.CategoryGroup;
import com.piramide.elwis.domain.catalogmanager.CategoryGroupHome;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.dto.catalogmanager.CategoryGroupDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryGroupCmd extends EJBCommand {
    private Log log = LogFactory.getLog(CategoryGroupCmd.class);

    public void executeInStateless(SessionContext ctx) {
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
        if ("getCategoryGroup".equals(op)) {
            isRead = false;
            Integer categoryGroupId = (Integer) paramDTO.get("categoryGroupId");
            getCategoryGroup(categoryGroupId);
        }

        if (isRead) {
            read();
        }
    }

    private void create() {
        CategoryGroupDTO dto = new CategoryGroupDTO();
        dto.putAll(paramDTO);
        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    private void read() {
        CategoryGroupDTO dto = new CategoryGroupDTO();
        dto.putAll(paramDTO);

        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences") &&
                "true".equals(paramDTO.get("withReferences").toString())) {
            checkReferences = true;
        }

        CategoryGroup categoryGroup =
                (CategoryGroup) ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
        if (null != categoryGroup) {
            resultDTO.put("canUpdateLabel", !categoryGroupHasCategories(categoryGroup));
        }
    }

    private void update() {
        CategoryGroupDTO dto = new CategoryGroupDTO();
        dto.putAll(paramDTO);

        CategoryGroup categoryGroup =
                (CategoryGroup) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        if (null != categoryGroup) {
            resultDTO.put("canUpdateLabel", !categoryGroupHasCategories(categoryGroup));
        }
    }

    private void delete() {
        CategoryGroupDTO dto = new CategoryGroupDTO();
        dto.putAll(paramDTO);
        ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
    }

    private void getCategoryGroup(Integer categoryGroupId) {
        try {
            CategoryGroup categoryGroup = readCategoryGroup(categoryGroupId);
            CategoryGroupDTO dto = new CategoryGroupDTO();
            DTOFactory.i.copyToDTO(categoryGroup, dto);
            resultDTO.put("CategoryGroupDTO", dto);
        } catch (FinderException e) {
            log.debug("-> Read CategoryGroup categoryGroupId=" + categoryGroupId + " FAIL");
            resultDTO.put("CategoryGroupDTO", null);
        }
    }

    private CategoryGroup readCategoryGroup(Integer categoryGroupId) throws FinderException {
        CategoryGroupHome categoryGroupHome =
                (CategoryGroupHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYGROUP);

        return categoryGroupHome.findByPrimaryKey(categoryGroupId);
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


    public boolean isStateful() {
        return false;
    }
}
