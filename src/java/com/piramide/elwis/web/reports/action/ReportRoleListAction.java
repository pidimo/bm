package com.piramide.elwis.web.reports.action;

import com.piramide.elwis.cmd.reports.ReportRoleCmd;
import com.piramide.elwis.dto.admin.RoleDTO;
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
public class ReportRoleListAction extends ReportListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ReportRoleForm reportRoleForm = (ReportRoleForm) form;

        ActionForward forward = super.execute(mapping, form, request, response);

        //delete selected items button pressed
        if (null != reportRoleForm.getDto("delete") && !"MainSearch".equals(forward.getName())) {
            ActionErrors erros = new ActionErrors();
            readSelectedRoles(request, reportRoleForm, erros);

            if (!erros.isEmpty()) {
                saveErrors(request, erros);
                return mapping.findForward("Fail");
            }
            return mapping.findForward("DeleteSelectedItems");
        }
        //clear old list parameters
        if ("MainSearch".equals(forward.getName())) {
            request.getParameterMap().clear();
        }

        return forward;
    }

    private void readSelectedRoles(HttpServletRequest request,
                                   ReportRoleForm form,
                                   ActionErrors errors) {

        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());
        checkRoleIdsSize(request, form, errors);
        if (errors.isEmpty()) {
            ReportRoleCmd reportRoleCmd = new ReportRoleCmd();
            reportRoleCmd.setOp("readSelected");
            reportRoleCmd.putParam("roleIds", roleIds);
            try {
                ResultDTO readResult = BusinessDelegate.i.execute(reportRoleCmd, request);
                List<RoleDTO> roleDTOs = (List<RoleDTO>) readResult.get("selectedRoles");
                if (null != roleDTOs && !roleDTOs.isEmpty()) {
                    request.setAttribute("selectedRoles", roleDTOs);
                } else {
                    errors.add("AllSelectedElementsDeleted", new ActionError("customMsg.itemNotFound"));
                }
            } catch (AppLevelException e) {
                log.debug("Cannot execute execute readSelected on ReportRoleCmd...");
            }
        }
    }

    private ActionErrors checkRoleIdsSize(HttpServletRequest request,
                                          ReportRoleForm form,
                                          ActionErrors errors) {
        List<Integer> roleIds = parseRoleIds(form.getSelectedRoles());
        if (roleIds.isEmpty()) {
            errors.add("selectedElementsControl", new ActionError("Role.selectAtLeastOneToDelete"));
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
