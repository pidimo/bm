package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.cmd.reports.ReportRoleCmd;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.reports.form.ReportRoleForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRoleImportListAction extends ReportListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ReportRoleForm reportRoleForm = (ReportRoleForm) form;

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        ActionForward actionForward = super.execute(mapping, form, request, response);

        //add button pressed
        if (null != reportRoleForm.getDto("addRoles") && !"MainSearch".equals(actionForward.getName())) {
            ActionErrors errors = new ActionErrors();
            checkRoleIdsSize(request, reportRoleForm, errors);
            checkRolesExistence(request, reportRoleForm, errors);
            checkDuplicated(request, reportRoleForm, errors);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            } else {
                addRole(request, reportRoleForm);
                return mapping.findForward("SuccessAdded");
            }
        }
        //clear old list parameters
        if ("MainSearch".equals(actionForward.getName())) {
            request.getParameterMap().clear();
        }

        return actionForward;
    }

    private void addRole(HttpServletRequest request, ReportRoleForm form) {
        Integer reportId = new Integer(form.getDto("reportId").toString());
        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue("companyId");

        ReportRoleCmd reportRoleCmd = new ReportRoleCmd();
        reportRoleCmd.setOp("create");
        reportRoleCmd.putParam("reportId", reportId);
        reportRoleCmd.putParam("roleIds", roleIds);
        reportRoleCmd.putParam("companyId", companyId);
        try {
            BusinessDelegate.i.execute(reportRoleCmd, request);
        } catch (AppLevelException e) {
            log.debug("Cannot execute execute create on ReportRoleCmd...");
        }
    }

    private ActionErrors checkRoleIdsSize(HttpServletRequest request,
                                          ReportRoleForm form,
                                          ActionErrors errors) {
        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());
        if (roleIds.isEmpty()) {
            errors.add("selectedElementsControl", new ActionError("Role.selectAtLeastOneToAdd"));
        }
        return errors;
    }

    private ActionErrors checkRolesExistence(HttpServletRequest request,
                                             ReportRoleForm form,
                                             ActionErrors errors) {

        Integer reportId = new Integer(form.getDto("reportId").toString());
        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());
        ReportRoleCmd reportRoleCmd = new ReportRoleCmd();
        reportRoleCmd.setOp("checkRolesExistence");
        reportRoleCmd.putParam("reportId", reportId);
        reportRoleCmd.putParam("roleIds", roleIds);
        try {
            ResultDTO existenceControl = BusinessDelegate.i.execute(reportRoleCmd, request);
            Boolean allRolesExists = (Boolean) existenceControl.get("allRolesExists");
            if (!allRolesExists) {
                errors.add("existenceControl", new ActionError("ReportRole.rolesDeleted"));
            }
        } catch (AppLevelException e) {
            log.debug("Cannot execute execute checkRolesExistence on ReportRoleCmd...");
        }

        return errors;
    }

    private ActionErrors checkDuplicated(HttpServletRequest request, ReportRoleForm form,
                                         ActionErrors errors) {

        Integer reportId = new Integer(form.getDto("reportId").toString());
        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());

        ReportRoleCmd reportRoleCmd = new ReportRoleCmd();
        reportRoleCmd.setOp("checkDuplicated");
        reportRoleCmd.putParam("reportId", reportId);
        reportRoleCmd.putParam("roleIds", roleIds);
        try {
            ResultDTO duplicatedControlResult = BusinessDelegate.i.execute(reportRoleCmd, request);
            Boolean duplicated = (Boolean) duplicatedControlResult.get("duplicated");
            if (duplicated) {
                String duplicatedRoleName = (String) duplicatedControlResult.get("duplicatedRoleName");
                errors.add("duplicatedControl", new ActionError("msg.Duplicated", duplicatedRoleName));
            }
        } catch (AppLevelException e) {
            log.debug("Cannot execute checkDuplicated on ReportRoleCmd...");
        }
        return errors;
    }

    private List<Integer> parseRoleIds(Object[] array) {
        List<Integer> result = new ArrayList<Integer>();
        for (Object obj : array) {
            Integer value = new Integer(obj.toString());
            result.add(value);
        }
        return result;
    }
}
