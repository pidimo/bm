package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.CategoryCmd;
import com.piramide.elwis.cmd.catalogmanager.CategoryGroupCmd;
import com.piramide.elwis.cmd.catalogmanager.CategoryValueCmd;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.dto.catalogmanager.CategoryGroupDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        boolean isSaveButtonPressed = isButtonPressed("dto(save)", request);
        boolean isSaveAndNewButtonPresses = isButtonPressed("SaveAndNew", request);

        if (!isSaveButtonPressed && !isSaveAndNewButtonPresses) {
            ActionErrors errors = new ActionErrors();
            settingUpSkipErrors(request, errors);
            settingUpOldValues();
            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);

        validateParentCategory(request, errors);
        validateGroup(request, errors);
        validateDuplicated(request, errors);
        validateCategoryValues(request, errors);
        if (!errors.isEmpty()) {
            settingUpOldValues();
        }

        return errors;
    }


    private void validateDuplicated(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return;
        }

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        String op = (String) getDto("op");
        String table = (String) getDto("table");
        String categoryName = (String) getDto("categoryName");
        String categoryId = null;
        if ("update".equals(op)) {
            categoryId = (String) getDto("categoryId");
        }

        boolean isUpdate = "update".equals(op);

        Map conditions = new HashMap();
        conditions.put("tableid", table);
        conditions.put("companyid", companyId);
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("category",
                "categoryname", categoryName,
                "categoryid", categoryId,
                conditions, isUpdate);

        if (isDuplicated) {
            errors.add("duplicated", new ActionError("msg.Duplicated",
                    categoryName + " - " + JSPHelper.getCategoryTable(table, request)));
        }
    }

    private boolean isButtonPressed(String key, HttpServletRequest request) {
        return null != request.getParameter(key);
    }

    private void settingUpSkipErrors(HttpServletRequest request, ActionErrors errors) {
        request.setAttribute("skipErrors", "true");
        errors.add("returnToForm", new ActionError("Address.error.cityNotFound"));
    }

    private void settingUpOldValues() {
        String hasSubCategories = (String) getDto("hasSubCategories");
        String categoryType = (String) getDto("categoryType");
        if ("true".equals(hasSubCategories) && !Functions.isCategorySingleSelect(categoryType)) {
            setDto("hasSubCategories", "false");
        }
    }

    private void validateParentCategory(HttpServletRequest request, ActionErrors errors) {
        String parentCategoryIdAsString = (String) getDto("parentCategory");
        String actualTableId = (String) getDto("table");

        if (GenericValidator.isBlankOrNull(parentCategoryIdAsString)) {
            return;
        }

        Integer parentCategoryId = Integer.valueOf(parentCategoryIdAsString);

        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("readCategory");
        categoryCmd.putParam("categoryId", parentCategoryId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryCmd, request);
            CategoryDTO dto = (CategoryDTO) resultDTO.get("categoryDTO");
            if (null == dto) {
                return;
            }

            Boolean hasSubCategories = (Boolean) dto.get("hasSubCategories");
            String categoryName = (String) dto.get("categoryName");
            String parentTable = (String) dto.get("table");
            if (!hasSubCategories) {
                errors.add("hasSubCategoriesError",
                        new ActionError("Category.error.ParentCategory.cannotAcceptSubCategories", categoryName));
                setDto("parentCategory", null);
            }
            if (!parentTable.equals(actualTableId)) {
                errors.add("tableError", new ActionError("CategoryGroup.error.tabTableChange", categoryName));
            }
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryCmd.class.getName() + " FAIL");
        }
    }

    private void validateGroup(HttpServletRequest request, ActionErrors errors) {
        String categoryGroupIdAsString = (String) getDto("categoryGroupId");
        String actualTableId = (String) getDto("table");

        if (Functions.isAddressContactPersonCategory(actualTableId)) {
            validateGroup(categoryGroupIdAsString, ContactConstants.ADDRESS_CATEGORY, request, errors);
            validateGroup((String) getDto("secondGroupId"), ContactConstants.CONTACTPERSON_CATEGORY, request, errors);
        } else {
            validateGroup(categoryGroupIdAsString, actualTableId, request, errors);
        }
    }

    private void validateGroup(String categoryGroupIdAsString, String actualTableId, HttpServletRequest request, ActionErrors errors) {

        if (GenericValidator.isBlankOrNull(categoryGroupIdAsString)) {
            return;
        }

        Integer categoryGroupId = Integer.valueOf(categoryGroupIdAsString);
        CategoryGroupCmd categoryGroupCmd = new CategoryGroupCmd();
        categoryGroupCmd.setOp("getCategoryGroup");
        categoryGroupCmd.putParam("categoryGroupId", categoryGroupId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryGroupCmd, request);
            CategoryGroupDTO dto = (CategoryGroupDTO) resultDTO.get("CategoryGroupDTO");
            if (null == dto) {
                return;
            }

            String groupTable = (String) dto.get("table");
            String label = (String) dto.get("label");
            if (!groupTable.equals(actualTableId)) {
                errors.add("tableError", new ActionError("CategoryGroup.error.tabTableChange", label));
            }
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryGroupCmd.class.getName() + " FAIL");
        }
    }

    private void validateCategoryValues(HttpServletRequest request, ActionErrors errors) {
        String[] categoryValueIdsAsArray = request.getParameterValues("dto(categoryValueIds)");
        List<String> categoryValueIds = new ArrayList<String>();
        if (null != categoryValueIdsAsArray) {
            categoryValueIds = Arrays.asList(categoryValueIdsAsArray);
        }

        for (String categoryValueId : categoryValueIds) {
            //validate only selected values
            if (!"true".equals(getDto(categoryValueId))) {
                continue;
            }

            CategoryValueCmd categoryValueCmd = new CategoryValueCmd();
            categoryValueCmd.setOp("existsCategoryValue");
            categoryValueCmd.putParam("categoryValueId", Integer.valueOf(categoryValueId));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(categoryValueCmd, request);
                Boolean result = (Boolean) resultDTO.get("existsCategoryValue");
                log.debug("-> Exists Category " + categoryValueId + " " + result);
                if (!result) {
                    errors.add("element_" + categoryValueId,
                            new ActionError("error.SelectedNotFound",
                                    getDto("categoryValueName_" + categoryValueId)));
                }
            } catch (AppLevelException e) {
                log.debug("->Execute " + CategoryValueCmd.class.getName() + " Fail");
            }
        }
        if (errors.isEmpty()) {
            setDto("categoryValueIds", categoryValueIds);
        }
    }
}
