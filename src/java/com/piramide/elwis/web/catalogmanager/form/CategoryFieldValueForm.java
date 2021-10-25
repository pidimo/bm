package com.piramide.elwis.web.catalogmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryFieldValueForm extends DefaultForm {


    public static List<String> getPageCategories(HttpServletRequest request) {
        String[] pageCategoryIds = new String[]{};
        if (null != request.getParameterValues("dto(pageCategories)")) {
            pageCategoryIds = request.getParameterValues("dto(pageCategories)");
        }
        return Arrays.asList(pageCategoryIds);
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        setDto("pageCategoryIds", getPageCategories(request));

        ActionErrors errors = super.validate(mapping, request);


        CategoryFormUtil formUtil = new CategoryFormUtil(this.getDtoMap(), request);
        List<ActionError> errorList = formUtil.validateCategoryFields();
        for (ActionError error : errorList) {
            errors.add("categoryError", error);
        }

        if (errors.isEmpty()) {
            getDtoMap().putAll(formUtil.getDateOptionsAsInteger());
            getDtoMap().putAll(formUtil.getAttachmentsDTOs());
        }
        return errors;
    }
}
