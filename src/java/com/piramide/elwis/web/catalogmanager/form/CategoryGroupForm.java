package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryGroupForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String changeTableId = (String) getDto("changeTableId");
        if ("true".equals(changeTableId)) {
            ActionErrors errors = new ActionErrors();
            settingUpSkipErrors(request, errors);
            return errors;
        }
        ActionErrors errors = super.validate(mapping, request);
        validateCategoryTab(request, errors);
        validateDuplicated(request, errors);

        return errors;
    }

    private void validateCategoryTab(HttpServletRequest request, ActionErrors errors) {
        String categoryTabId = (String) getDto("categoryTabId");
        if (!GenericValidator.isBlankOrNull(categoryTabId) &&
                errors.isEmpty()) {
            String groupTable = (String) getDto("table");
            CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
            categoryTabCmd.setOp("getTable");
            categoryTabCmd.putParam("categoryTabId", Integer.valueOf(categoryTabId));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(categoryTabCmd, request);
                String tabTable = (String) resultDTO.get("getTable");
                if (!groupTable.equals(tabTable)) {
                    errors.add("ChangeTabTable", new ActionError("CategoryGroup.error.tabTableChange",
                            JSPHelper.getMessage(request, "CategoryGroup.categoryTabId")));
                }
            } catch (AppLevelException e) {
                log.debug("-> Execute " + CategoryTabCmd.class.getName() + " FAIL", e);
            }
        }
    }

    private void validateDuplicated(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return;
        }

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        String op = (String) getDto("op");
        String table = (String) getDto("table");
        String label = (String) getDto("label");
        String categoryGroupId = null;
        if ("update".equals(op)) {
            categoryGroupId = (String) getDto("categoryGroupId");
        }

        boolean isUpdate = "update".equals(op);

        Map conditions = new HashMap();
        conditions.put("tableid", table);
        conditions.put("companyid", companyId);
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("categorygroup",
                "label", label,
                "categorygroupid", categoryGroupId,
                conditions, isUpdate);

        if (isDuplicated) {
            errors.add("duplicated", new ActionError("msg.Duplicated",
                    label + " - " + JSPHelper.getCategoryTable(table, request)));
        }
    }


    private void settingUpSkipErrors(HttpServletRequest request, ActionErrors errors) {
        request.setAttribute("skipErrors", "true");
        errors.add("returnToForm", new ActionError("Address.error.cityNotFound"));
    }
}
