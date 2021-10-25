package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProjectTimeRegistrationAction extends ProjectTimeAction {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = super.execute(mapping, form, request, response);
        if ("SaveAndNew".equals(forward.getName())) {
            String projectId = request.getParameter("projectId");
            if (null != projectId && !"".equals(projectId.trim())) {
                return new ActionForwardParameters().add("dto(projectId)", projectId).forward(forward);
            }
        }
        return forward;
    }

    @Override
    protected ActionForward priorValidations(ActionForm form, HttpServletRequest request, ActionMapping mapping) {
        DefaultForm defaultForm = (DefaultForm) form;
        String changeProject = (String) defaultForm.getDto("changeProject");

        if ("true".equals(changeProject)) {
            String projectId = (String) defaultForm.getDto("projectId");

            if (!GenericValidator.isBlankOrNull(projectId)) {
                setDTOValues(defaultForm, request);
            }

            return mapping.getInputForward();
        }

        return super.priorValidations(defaultForm, request, mapping);
    }

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        String projectId = request.getParameter("projectId");
        if (!GenericValidator.isBlankOrNull(projectId)) {
            return super.validateElementExistence(request, mapping);
        }

        return null;
    }

    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(
                user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("userName", addressName);
        defaultForm.setDto("userId", user.getValue(Constants.USERID));
        defaultForm.setDto("status", ProjectConstants.ProjectTimeStatus.ENTERED.getValue());
        defaultForm.setDto("assigneeId", user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("assigneeName", addressName);

        String projectId = (String) defaultForm.getDto("projectId");
        ProjectDTO projectDTO = com.piramide.elwis.web.projects.el.Functions.getProject(request, projectId);
        Integer toBeInvoiced = (Integer) projectDTO.get("toBeInvoiced");
        if (ProjectConstants.ToBeInvoicedType.ALL_TIMES.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", true);
            defaultForm.setDto("disableInvoiceable", true);
        }
        if (ProjectConstants.ToBeInvoicedType.NO_TIMES.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", false);
            defaultForm.setDto("disableInvoiceable", true);
        }
        if (ProjectConstants.ToBeInvoicedType.DEPENDS.getValue() == toBeInvoiced) {
            defaultForm.setDto("toBeInvoiced", true);
            defaultForm.setDto("disableInvoiceable", false);
        }
    }
}
