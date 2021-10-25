package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import net.java.dev.strutsejb.web.DefaultForm;
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
public class CategoryTabForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        validateDuplicated(request, errors);
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
        String label = (String) getDto("label");
        String categoryTabId = null;
        if ("update".equals(op)) {
            categoryTabId = (String) getDto("categoryTabId");
        }

        boolean isUpdate = "update".equals(op);

        Map conditions = new HashMap();
        conditions.put("tableid", table);
        conditions.put("companyid", companyId);
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("categorytab",
                "label", label,
                "categorytabid", categoryTabId,
                conditions, isUpdate);

        if (isDuplicated) {
            errors.add("duplicated", new ActionError("msg.Duplicated",
                    label + " - " + JSPHelper.getCategoryTable(table, request)));
        }
    }
}
