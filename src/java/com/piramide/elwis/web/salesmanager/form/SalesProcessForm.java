package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Sales process Form handler.
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessForm.java 10013 2010-12-14 18:08:15Z ivan $
 */
public class SalesProcessForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(SalesProcessForm.class);

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    @SuppressWarnings(value = "unchecked")
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);// validating with super class

        ActionError dateError = validateDates(request);
        if (null != dateError) {
            errors.add("dateError", dateError);
        }

        setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));

        CategoryFormUtil categoryFieldValidator = new CategoryFormUtil(this.getDtoMap(), request);
        List<ActionError> categoryErrors = categoryFieldValidator.validateCategoryFields();

        for (int i = 0; i < categoryErrors.size(); i++) {
            errors.add("salesProcessCategoryError_" + i, categoryErrors.get(i));
        }

        if (errors.isEmpty()) {
            getDtoMap().putAll(categoryFieldValidator.getDateOptionsAsInteger());
            getDtoMap().putAll(categoryFieldValidator.getAttachmentsDTOs());
        } else {
            categoryFieldValidator.restoreAttachmentFields();
        }

        /*if (errors.isEmpty()) {
            try {
                Integer startDate = (Integer) getDto("startDate");
                Integer endDate = (Integer) getDto("endDate");
                if (startDate.intValue() > endDate.intValue()) {
                    errors.add("startDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request,
                            "SalesProcess.endDate"), JSPHelper.getMessage(request, "SalesProcess.startDate")));
                }
            } catch (ClassCastException e) {
                //some of both are empty
            }
        }*/

        return errors;
    }


    private ActionError validateDates(HttpServletRequest request) {
        if (null == getDto("startDate") || "".equals(getDto("startDate").toString().trim())) {
            return null;
        }

        if (null == getDto("endDate") || "".equals(getDto("endDate").toString().trim())) {
            return null;
        }

        Integer startDate = (Integer) getDto("startDate");

        Integer endDate = (Integer) getDto("endDate");

        if (startDate > endDate) {
            return new ActionError("Common.greaterThan", JSPHelper.getMessage(request,
                    "SalesProcess.endDate"), JSPHelper.getMessage(request, "SalesProcess.startDate"));
        }

        return null;
    }
}
