package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.ProjectConstants;
import static com.piramide.elwis.utils.ProjectConstants.ProjectTimeStatus;
import com.piramide.elwis.utils.ProjectUserPermissionUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.projects.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectUserPermissionValidator {
    private ActionErrors errors = new ActionErrors();
    private String projectId;
    private ProjectDTO projectDTO;

    private WorkFlow workFlow = new WorkFlow();

    public ProjectUserPermissionValidator(String projectId, HttpServletRequest request) {
        this.projectId = projectId;
        projectDTO = Functions.getProject(request, projectId);
        fillStructure();
    }

    private void fillStructure() {

        WorkFlowAction view = new WorkFlowAction("viewButton", ProjectTimeStatus.ENTERED, new OwnerUserValidator());
        WorkFlowAction edit = new WorkFlowAction("editButton", ProjectTimeStatus.ENTERED, new OwnerUserValidator());
        WorkFlowAction release = new WorkFlowAction("releaseButton", ProjectTimeStatus.RELEASED, new OwnerUserValidator());
        WorkFlowStatus enteredStatus = new WorkFlowStatus(ProjectTimeStatus.ENTERED);
        enteredStatus.addWorkFlowAction(view);
        enteredStatus.addWorkFlowAction(edit);
        enteredStatus.addWorkFlowAction(release);

        WorkFlowAction viewRelease = new WorkFlowAction("viewButton", ProjectTimeStatus.RELEASED, new OwnerUserViewConfirmValidator());
        WorkFlowAction disable = new WorkFlowAction("disableButton", ProjectTimeStatus.ENTERED, new OwnerUserValidator());
        WorkFlowAction editRelease = new WorkFlowAction("editButton", ProjectTimeStatus.RELEASED, new OwnerUserConfirmValidator());
        WorkFlowAction confirm = new WorkFlowAction("confirmButton", ProjectTimeStatus.CONFIRMED, new ConfirmValidator());
        confirm.setDataUpdater(new ConfirmedDataUpdater());
        WorkFlowAction noConfirm = new WorkFlowAction("noConfirmButton", ProjectTimeStatus.NOT_CONFIRMED, new ConfirmValidator());
        WorkFlowStatus releaseStatus = new WorkFlowStatus(ProjectTimeStatus.RELEASED);
        releaseStatus.addWorkFlowAction(viewRelease);
        releaseStatus.addWorkFlowAction(disable);
        releaseStatus.addWorkFlowAction(editRelease);
        releaseStatus.addWorkFlowAction(confirm);
        releaseStatus.addWorkFlowAction(noConfirm);

        WorkFlowAction viewConfirmed = new WorkFlowAction("viewButton", ProjectTimeStatus.CONFIRMED, new OwnerUserViewConfirmValidator());
        WorkFlowAction editConfirmed = new WorkFlowAction("editButton", ProjectTimeStatus.CONFIRMED, new ConfirmValidator());
        WorkFlowAction invoiced = new WorkFlowAction("invoiceButton", ProjectTimeStatus.INVOICED, new AdminValidator());
        WorkFlowStatus confirmedStatus = new WorkFlowStatus(ProjectTimeStatus.CONFIRMED);
        confirmedStatus.addWorkFlowAction(viewConfirmed);
        confirmedStatus.addWorkFlowAction(editConfirmed);
        confirmedStatus.addWorkFlowAction(noConfirm);
        confirmedStatus.addWorkFlowAction(invoiced);

        WorkFlowAction viewNoConfirmed = new WorkFlowAction("viewButton", ProjectTimeStatus.NOT_CONFIRMED, new OwnerUserViewConfirmValidator());
        WorkFlowAction editNoConfirmed = new WorkFlowAction("editButton", ProjectTimeStatus.RELEASED, new OwnerUserConfirmValidator());
        WorkFlowAction confirmNoConfirmed = new WorkFlowAction("confirmButton", ProjectTimeStatus.CONFIRMED, new ConfirmValidator());
        confirmNoConfirmed.setDataUpdater(new ConfirmedDataUpdater());
        WorkFlowStatus noConfirmedStatus = new WorkFlowStatus(ProjectTimeStatus.NOT_CONFIRMED);
        noConfirmedStatus.addWorkFlowAction(viewNoConfirmed);
        noConfirmedStatus.addWorkFlowAction(editNoConfirmed);
        noConfirmedStatus.addWorkFlowAction(confirmNoConfirmed);

        workFlow.addWorkFlowStatus(enteredStatus);
        workFlow.addWorkFlowStatus(releaseStatus);
        workFlow.addWorkFlowStatus(confirmedStatus);
        workFlow.addWorkFlowStatus(noConfirmedStatus);

    }

    public ActionForward validateProjectUserPermissions(HttpServletRequest request,
                                                        DefaultForm form,
                                                        ActionMapping mapping) {
        ActionForward forward;

        if (null != (forward = validateNewButtonPermissions(form, request, mapping))) {
            return forward;
        }

        if (null != (forward = generalWorkFlowValidations(form, request, mapping))) {
            return forward;
        }

        return null;
    }

    private ActionForward generalWorkFlowValidations(DefaultForm form,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (isNewButtonPressed(request) || isSaveAndNewButtonPressed(request)) {
            return null;
        }

        String status = (String) form.getDto("status");
        List<ActionError> workFlowErrors = workFlow.apply(status, form, request);
        if (null == workFlowErrors) {
            return null;
        }

        int i = 0;
        for (ActionError error : workFlowErrors) {
            errors.add("WorkFlowError" + i, error);
            i++;
        }

        return mapping.findForward("Fail");
    }

    private ActionError validateProjectStatus(HttpServletRequest request) {
        if (ProjectConstants.ProjectStatus.OPENED.getValue() == ((Integer) projectDTO.get("status"))) {
            return null;
        }

        String label = com.piramide.elwis.web.catalogmanager.el.Functions.searchLabel(
                Functions.getProjectStatuses(request),
                projectDTO.get("status").toString());

        return new ActionError("ProjectTime.projectStatus.error", label);
    }

    public ActionForward validateNewButtonPermissions(HttpServletRequest request,
                                                      ActionMapping mapping) {
        return validateNewButtonPermissions(new DefaultForm(), request, mapping);

    }

    public ActionForward validateNewButtonPermissions(DefaultForm defaultForm, HttpServletRequest request,
                                                      ActionMapping mapping) {
        if (!isNewButtonPressed(request) &&
                !isSaveAndNewButtonPressed(request) &&
                !isReleaseButtonPressed(request) &&
                !isReleaseAndNewButtonPressed(request)) {
            return null;
        }

        ActionError projectStatusError = validateProjectStatus(request);
        if (null != projectStatusError) {
            errors.add("projectStatusError", projectStatusError);
            return mapping.findForward("Fail");
        }

        Boolean hasTimeLimit = (Boolean) projectDTO.get("hasTimeLimit");
        if (hasTimeLimit) {
            BigDecimal totalInvoice = (BigDecimal) projectDTO.get("totalInvoice");
            BigDecimal plannedInvoice = (BigDecimal) projectDTO.get("plannedInvoice");
            BigDecimal totalNoInvoice = (BigDecimal) projectDTO.get("totalNoInvoice");
            BigDecimal plannedNoInvoice = (BigDecimal) projectDTO.get("plannedNoInvoice");
            if ((null != totalInvoice && plannedInvoice.compareTo(totalInvoice) == 0) &&
                    (null != totalNoInvoice && plannedNoInvoice.compareTo(totalNoInvoice) == 0)) {
                errors.add("timescompleted", new ActionError("ProjectTime.projectTime.completed"));
                return mapping.findForward("Fail");
            }
        }

        if (Functions.hasProjectUserPermission(projectId, ProjectUserPermissionUtil.Permission.NEW, request)) {
            if (isReleaseButtonPressed(request)) {
                defaultForm.setDto("status", ProjectConstants.ProjectTimeStatus.RELEASED.getAsString());
            }

            if (isReleaseAndNewButtonPressed(request)) {
                defaultForm.setDto("status", ProjectConstants.ProjectTimeStatus.RELEASED.getAsString());
                request.getParameterMap().remove("releaseAndNewButton");
                request.getParameterMap().put("SaveAndNew", "Save and new");
            }

            return null;
        }


        errors.add("permissionDisabled", new ActionError("ProjectTime.permissionRequired",
                JSPHelper.getMessage(request, "ProjectAssignee.permission.new")));

        return mapping.findForward("Fail");
    }

    private boolean isNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("newButton");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }

    private boolean isReleaseButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("releaseButton");
    }

    private boolean isReleaseAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("releaseAndNewButton");
    }

    public ActionErrors getErrors() {
        return errors;
    }
}
