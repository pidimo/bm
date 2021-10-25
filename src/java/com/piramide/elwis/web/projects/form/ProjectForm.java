package com.piramide.elwis.web.projects.form;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Project form bean
 *
 * @author Fernando Montao
 * @version $Id: ProjectForm.java 2009-02-22 19:30:45 $
 */
public class ProjectForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        //validate only when save button is pressed
        if (!isSaveButtonPressed(request)) {
            return new ActionErrors();
        }

        ActionErrors errors = super.validate(mapping, request);

        ActionError datesError = validateDates(request);
        if (null != datesError) {
            errors.add("datesError", datesError);
        }

        if ("update".equals(getDto("op"))) {
            ActionError plannedInvoiceValuesError = validatePlannedInvoiceValues(request);
            if (null != plannedInvoiceValuesError) {
                errors.add("plannedInvoiceValuesError", plannedInvoiceValuesError);
            }
        }

        return errors;
    }

    private ActionError validateDates(HttpServletRequest request) {
        Object endDate = getDto("endDate");
        if (!(endDate instanceof Integer)) {
            return null;
        }

        Object startDate = getDto("startDate");
        if (!(startDate instanceof Integer)) {
            return null;
        }

        if (((Integer) endDate) < ((Integer) startDate)) {
            return new ActionError("Common.greaterThanOrEqual",
                    JSPHelper.getMessage(request, "Project.endDate"),
                    JSPHelper.getMessage(request, "Project.startDate"));
        }

        return null;
    }

    private ActionError validatePlannedInvoiceValues(HttpServletRequest request) {
        if (!Functions.existsProject(getDto("projectId"))) {
            return null;
        }

        Object hasTimeLimit = getDto("hasTimeLimit");
        if (null != hasTimeLimit && !"true".equals(hasTimeLimit.toString())) {
            return null;
        }

        ActionError plannedInvoiceError = validateInvoicedValues(request,
                "plannedInvoice", "totalInvoice", "Project.plannedInvoice", "Project.totalInvoice");
        if (null != plannedInvoiceError) {
            return plannedInvoiceError;
        }

        ActionError plannedNoInvoiceError = validateInvoicedValues(request,
                "plannedNoInvoice", "totalNoInvoice", "Project.plannedNoInvoice", "Project.totalNoInvoice");
        if (null != plannedNoInvoiceError) {
            return plannedNoInvoiceError;
        }

        return null;
    }

    private ActionError validateInvoicedValues(HttpServletRequest request,
                                               String key,
                                               String dtoKey,
                                               String resourceKeyOne,
                                               String resourceKeyTwo) {
        Object pageValue = getDto(key);
        if (null == pageValue || !(pageValue instanceof BigDecimal)) {
            return null;
        }

        ProjectDTO projectDTO = Functions.getProject(request, getDto("projectId").toString());

        BigDecimal dtoValue = (BigDecimal) projectDTO.get(dtoKey);
        if (null == dtoValue) {
            return null;
        }

        if (dtoValue.compareTo((BigDecimal) pageValue) == 1) {
            return new ActionError("error.lessThan",
                    JSPHelper.getMessage(request, resourceKeyOne),
                    JSPHelper.getMessage(request, resourceKeyTwo));
        }

        return null;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveButton");
    }

}