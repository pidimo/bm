package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.CategoryCmd;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryValueForm extends DefaultForm {
    private Log log = LogFactory.getLog(CategoryValueForm.class);

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String[] categoryIds = new String[]{};
        if (null != request.getParameterValues("dto(categoryIds)")) {
            categoryIds = request.getParameterValues("dto(categoryIds)");
        }

        List<String> pageCategoryIds = Arrays.asList(categoryIds);

        ActionErrors errors = super.validate(mapping, request);
        if (null != pageCategoryIds) {
            pageCategoryIds = filterCategoryIds(pageCategoryIds);
            log.debug("->Read categoryIds from JSP page " + pageCategoryIds);
            validateForeignCategories(request, errors, pageCategoryIds);
            validateParentCategories(request, errors, pageCategoryIds);
        }

        return errors;
    }

    private List<String> filterCategoryIds(List<String> pageCategoryIds) {
        List<String> result = new ArrayList<String>();
        for (String pageId : pageCategoryIds) {
            if (null != getDto(pageId) && "true".equals(getDto(pageId))) {
                result.add(pageId);
            }
        }

        return result;
    }

    private boolean validateOwnerCategoryHasSubCategories(HttpServletRequest request, ActionErrors errors) {
        String categoryId = (String) getDto("categoryId");
        boolean hasSubCategories = "true".equals(getDto("hasSubCategories").toString());
        boolean dbOwnerCategoryHasSubCategories = JSPHelper.hasSubCategories(categoryId, request);
        if (hasSubCategories && !dbOwnerCategoryHasSubCategories) {
            errors.add("ownerCategoryNotSubCategories",
                    new ActionError("Category.error.ParentCategory.cannotAcceptSubCategories",
                            getDto("categoryName")));
        }
        return hasSubCategories && dbOwnerCategoryHasSubCategories;
    }

    private void validateForeignCategories(HttpServletRequest request,
                                           ActionErrors errors,
                                           List<String> pageCategoryIds) {
        for (String categoryIdFromPage : pageCategoryIds) {
            ForeignkeyValidator.i.validate("category",
                    "categoryid",
                    categoryIdFromPage,
                    errors,
                    new ActionError("error.SelectedNotFound", getDto("categoryName_" + categoryIdFromPage)));
        }
    }

    private void validateParentCategories(HttpServletRequest request,
                                          ActionErrors errors, List<String> pageCategoryIds) {
        String categoryId = (String) getDto("categoryId");
        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("readCategory");

        for (String pageChildrenCategoryId : pageCategoryIds) {
            categoryCmd.putParam("categoryId", Integer.valueOf(pageChildrenCategoryId));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(categoryCmd, request);
                CategoryDTO childrenDTO = (CategoryDTO) resultDTO.get("categoryDTO");
                Integer childrenParentCategoryId = (Integer) childrenDTO.get("parentCategory");
                if (null == childrenParentCategoryId ||
                        !categoryId.equals(childrenParentCategoryId.toString())) {
                    errors.add("parentCategoryError",
                            new ActionError("CategoryValue.error.subCategory",
                                    getDto("categoryName_" + pageChildrenCategoryId)));
                    break;
                }
            } catch (AppLevelException e) {
                log.debug("-> Execute " + CategoryCmd.class.getName() + " FAIL", e);
            }
        }
    }

    /*private void validateCategoryUsages(HttpServletRequest request,
                                        ActionErrors errors) {

        List<CategoryDTO> childrenCategories = (List<CategoryDTO>) request.getAttribute("childrenCategories");
        if (null != childrenCategories)
            for (CategoryDTO categoryDTO : childrenCategories) {
                Integer childrenCategoryId = (Integer) categoryDTO.get("categoryId");
                Integer childrenParentCategoryId = (Integer) categoryDTO.get("parentCategory");
                if ("true".equals(getDto(childrenCategoryId.toString())))

            }
    }*/
}
