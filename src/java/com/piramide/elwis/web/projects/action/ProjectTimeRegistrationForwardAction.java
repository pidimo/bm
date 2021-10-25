package com.piramide.elwis.web.projects.action;

import com.piramide.elwis.dto.projects.ProjectDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ProjectTimeRegistrationForwardAction extends DefaultForwardAction {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String projectId = (String) defaultForm.getDto("projectId");
        if (GenericValidator.isBlankOrNull(projectId)) {
            return;
        }

        User user = RequestUtils.getUser(request);
        String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(
                user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("userName", addressName);
        defaultForm.setDto("userId", user.getValue(Constants.USERID));
        defaultForm.setDto("status", ProjectConstants.ProjectTimeStatus.ENTERED.getValue());
        defaultForm.setDto("assigneeId", user.getValue(Constants.USER_ADDRESSID));
        defaultForm.setDto("assigneeName", addressName);


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
