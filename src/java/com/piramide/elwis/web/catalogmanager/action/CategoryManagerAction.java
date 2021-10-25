package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryManagerAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        DefaultForm defaultForm = (DefaultForm) form;
        String op = (String) defaultForm.getDto("op");

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("create".equals(op) && !"SaveAndNew".equals(forward.getName())) {
            Integer categoryType = (Integer) defaultForm.getDto("categoryType");
            if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstantAsInt() == categoryType ||
                    CatalogConstants.CategoryType.SINGLE_SELECT.getConstantAsInt() == categoryType) {
                ActionForwardParameters parameters = new ActionForwardParameters();
                parameters.add("dto(categoryId)", defaultForm.getDto("categoryId").toString())
                        .add("dto(op)", "read");
                return parameters.forward(mapping.findForward("Success_"));
            }
        }

        return forward;
    }
}
