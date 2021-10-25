package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.CategoryRelation;
import com.piramide.elwis.domain.catalogmanager.CategoryRelationHome;
import com.piramide.elwis.domain.catalogmanager.CategoryValue;
import com.piramide.elwis.domain.catalogmanager.CategoryValueHome;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.dto.catalogmanager.CategoryValueDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan
 * @version $Id: CategoryValueCmd.java 9941 2010-01-13 19:51:26Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class CategoryValueCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String op = getOp();
        boolean isRead = true;
        if ("create".equals(op)) {
            isRead = false;
            create(ctx);
        }
        if ("update".equals(op)) {
            isRead = false;
            update(ctx);
        }
        if ("delete".equals(op)) {
            isRead = false;
            delete(ctx);
        }
        if ("readCategoyValues".equals(op)) {
            isRead = false;
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            readCategoyValues(categoryId);
        }
        if ("existsCategoryValue".equals(op)) {
            isRead = false;
            Integer categoryValueId = (Integer) paramDTO.get("categoryValueId");
            existsCategoryValue(categoryValueId);
        }
        if ("createCategoryRelation".equals(op)) {
            isRead = false;
            Integer categoryValueId = (Integer) paramDTO.get("categoryValueId");
            Integer categoryId = (Integer) paramDTO.get("categoryId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            createCategoryRelation(categoryId, categoryValueId, companyId);
        }
        if (isRead) {
            read(ctx);
        }
    }

    private void readCategoyValues(Integer categoryId) {
        CategoryValueHome categoryValueHome =
                (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);

        List<CategoryValueDTO> categoryValueDTOList = new ArrayList<CategoryValueDTO>();
        try {
            Collection categoryValues = categoryValueHome.findByCategoryId(categoryId);
            if (null != categoryValues) {
                for (Object object : categoryValues) {
                    CategoryValue categoryValue = (CategoryValue) object;
                    CategoryValueDTO dto = new CategoryValueDTO();
                    DTOFactory.i.copyToDTO(categoryValue, dto);
                    categoryValueDTOList.add(dto);
                }
            }
        } catch (FinderException e) {
            log.debug("->Read CategoryValues categoryId=" + categoryId + " FAIL");
        }

        resultDTO.put("categoryValues", categoryValueDTOList);
    }

    private void delete(SessionContext ctx) {
        CategoryValueDTO dto = new CategoryValueDTO(paramDTO);

        CategoryValue categoryValue =
                (CategoryValue) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
        //element not found
        if (resultDTO.isFailure()) {
            return;
        }

        if (null != categoryValue.getCategoryRelations()) {
            List categoryRelations = new ArrayList(categoryValue.getCategoryRelations());

            for (Object object : categoryRelations) {
                CategoryRelation categoryRelation = (CategoryRelation) object;
                try {
                    deleteCategoryRelation(categoryRelation, ctx);
                } catch (RemoveException e) {
                    log.debug("->Delete CategoryRelation categoryId=" + categoryRelation.getCategoryId() +
                            " categoryValueId=" + categoryRelation.getCategoryValueId() + " FAIL");
                    ctx.setRollbackOnly();
                    resultDTO.addResultMessage("CategoryValue.error.cannotDeleteRelation");
                    resultDTO.setForward("Fail");
                    return;
                }
            }
        }

        ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
    }

    private void update(SessionContext ctx) {
        CategoryValueDTO dto = new CategoryValueDTO(paramDTO);
        CategoryValue updateCategoryValue =
                (CategoryValue) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, true, "Fail");

        //version control error.
        if (resultDTO.isFailure()) {
            if (null != updateCategoryValue) {
                if (updateCategoryValue.getCategory().getHasSubCategories()) {
                    readCategoryRelations(updateCategoryValue, ctx);
                }
            }
            return;
        }

        //element not found
        if ("Fail".equals(resultDTO.getForward())) {
            return;
        }

        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("hasSubCategories");
        categoryCmd.putParam("categoryId", updateCategoryValue.getCategoryId());
        categoryCmd.executeInStateless(ctx);

        ResultDTO result = categoryCmd.getResultDTO();
        Boolean hasSubCategories = (Boolean) result.get("hasSubCategories");

        if (hasSubCategories) {
            List<Integer> categoryRelationIds = new ArrayList<Integer>();

            List<CategoryDTO> childrenCategories = (List<CategoryDTO>) result.get("childrenCategories");
            for (CategoryDTO categoryDTO : childrenCategories) {
                Integer childrenCategoryId = (Integer) categoryDTO.get("categoryId");
                if (null != paramDTO.get(childrenCategoryId.toString()) &&
                        "true".equals(paramDTO.get(childrenCategoryId.toString()))) {
                    categoryRelationIds.add(childrenCategoryId);
                }
            }

            if (null != updateCategoryValue.getCategoryRelations()) {
                List categoryRelations = new ArrayList(updateCategoryValue.getCategoryRelations());
                for (Object object : categoryRelations) {
                    CategoryRelation categoryRelation = (CategoryRelation) object;
                    if (childrenCategories.contains(categoryRelation.getCategoryId())) {
                        childrenCategories.remove(categoryRelation.getCategoryId());
                    } else {
                        try {
                            //delete unselected CategoryRelations
                            deleteCategoryRelation(categoryRelation, ctx);
                        } catch (RemoveException e) {
                            log.debug("->Delete CategoryRelation categoryId=" + categoryRelation.getCategoryId() +
                                    " categoryValueId=" + categoryRelation.getCategoryValueId() + " FAIL");
                            ctx.setRollbackOnly();
                        }
                    }
                }
            }
            //create new CategoryRelations
            for (Integer childrenCategoryId : categoryRelationIds) {
                createCategoryRelation(childrenCategoryId,
                        updateCategoryValue.getCategoryValueId(),
                        updateCategoryValue.getCompanyId());
            }
        }
    }

    private void deleteCategoryRelation(CategoryRelation categoryRelation, SessionContext ctx) throws RemoveException {
        boolean isCategoryRelationUsed = categoryRelationIsInUse(categoryRelation.getCategoryValueId(), categoryRelation, ctx);

        if (!isCategoryRelationUsed) {
            categoryRelation.remove();
        }
    }

    private void read(SessionContext ctx) {
        CategoryValueDTO dto = new CategoryValueDTO(paramDTO);

        CategoryValue categoryValue =
                (CategoryValue) ExtendedCRUDDirector.i.read(dto, resultDTO, true);

        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }

        if (null != categoryValue) {
            if (categoryValue.getCategory().getHasSubCategories()) {
                readCategoryRelations(categoryValue, ctx);
            }
        }
    }

    private void readCategoryRelations(CategoryValue categoryValue, SessionContext ctx) {
        if (null == categoryValue.getCategoryRelations()) {
            return;
        }

        for (Object object : categoryValue.getCategoryRelations()) {
            CategoryRelation categoryRelation = (CategoryRelation) object;

            resultDTO.put(categoryRelation.getCategoryId().toString(), "true");

            boolean categoryRelationIsUsed = categoryRelationIsInUse(categoryValue.getCategoryValueId(),
                    categoryRelation,
                    ctx);

            if (categoryRelationIsUsed) {
                resultDTO.put("canUpdateCategory_" + categoryRelation.getCategoryId(), false);
            } else {
                resultDTO.put("canUpdateCategory_" + categoryRelation.getCategoryId(), true);
            }
        }
    }

    private boolean categoryRelationIsInUse(Integer categoryValueId,
                                            CategoryRelation categoryRelation,
                                            SessionContext ctx) {
        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("categoryFieldValueIsInUse");
        categoryCmd.putParam("childCategoryId", categoryRelation.getCategoryId());
        categoryCmd.putParam("categoryValueId", categoryValueId);

        categoryCmd.executeInStateless(ctx);
        return (Boolean) categoryCmd.getResultDTO().get("categoryFieldValueIsInUse");
    }

    private void create(SessionContext ctx) {
        Integer categoryId = Integer.valueOf(paramDTO.get("categoryId").toString());
        CategoryValueDTO dto = new CategoryValueDTO();
        dto.put("categoryId", categoryId);
        dto.put("categoryValueName", paramDTO.get("categoryValueName"));
        dto.put("companyId", Integer.valueOf(paramDTO.get("companyId").toString()));
        dto.put("tableId", paramDTO.get("tableId"));
        dto.put("op", this.getOp());

        CategoryValue categoryValue = (CategoryValue) ExtendedCRUDDirector.i.create(dto, resultDTO, true);

        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("hasSubCategories");
        categoryCmd.putParam("categoryId", categoryId);
        categoryCmd.executeInStateless(ctx);
        ResultDTO result = categoryCmd.getResultDTO();
        Boolean hasSubCategories = (Boolean) result.get("hasSubCategories");

        if (hasSubCategories) {
            List<Integer> categoryRelationIds = new ArrayList<Integer>();

            List<CategoryDTO> childrenCategories = (List<CategoryDTO>) result.get("childrenCategories");
            for (CategoryDTO categoryDTO : childrenCategories) {
                Integer childrenCategoryId = (Integer) categoryDTO.get("categoryId");
                if (null != paramDTO.get(childrenCategoryId.toString()) &&
                        "true".equals(paramDTO.get(childrenCategoryId.toString()))) {
                    categoryRelationIds.add(childrenCategoryId);
                }
            }
            for (Integer childrenCategoryId : categoryRelationIds) {
                createCategoryRelation(childrenCategoryId,
                        categoryValue.getCategoryValueId(),
                        categoryValue.getCompanyId());
            }
        }
    }

    private void createCategoryRelation(Integer categoryId, Integer categoryValueId, Integer companyId) {
        CategoryRelationHome categoryRelationHome =
                (CategoryRelationHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYRELATION);

        try {
            categoryRelationHome.create(categoryId, categoryValueId, companyId);
        } catch (CreateException e) {
            log.debug("->Create CategoryRelation categoryId=" + categoryId +
                    " categoryValueId=" + categoryValueId + " FAIL", e);
        }
    }

    private boolean existsCategoryValue(Integer categoryValueId) {
        CategoryValueHome categoryValueHome =
                (CategoryValueHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORYVALUE);
        boolean result = false;

        try {
            categoryValueHome.findByPrimaryKey(categoryValueId);
            result = true;
        } catch (FinderException e) {
            log.debug("->existsCategoryValue categoryValueId=" + categoryValueId + " " + result);
        }

        resultDTO.put("existsCategoryValue", result);
        return result;
    }

    public boolean isStateful() {
        return false;
    }
}