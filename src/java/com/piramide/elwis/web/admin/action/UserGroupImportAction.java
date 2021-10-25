package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.UserGroupImportCmd;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.admin.form.UserGroupImportForm;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 20, 2005
 * Time: 10:18:33 AM
 * To change this template use File | Settings | File Templates.
 */

public class UserGroupImportAction extends ListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug(" -- UserGroupImportAction ------------- execute ----- ");
        UserGroupImportForm groupImportForm = (UserGroupImportForm) form;
        ActionForward forward = action.findForward("Fail");
        ActionErrors errors = new ActionErrors();
        UserGroupImportCmd cmd = new UserGroupImportCmd();

        String groupId = null;
        if (request.getParameter("userGroupId") != null) {
            groupId = request.getParameter("userGroupId");

        }

        if ("true".equals(request.getParameter("Import_value"))) {

            ForeignkeyValidator.i.validate(AdminConstants.TABLE_USERGROUP, "userGroupId",
                    groupId, errors, new ActionError("customMsg.NotFound", request.getParameter("groupName")));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return action.findForward("Fail");
            }


            if (request.getParameter("aditionals") != null) {
                cmd.putParam(groupImportForm.getDto());
                BusinessDelegate.i.execute(cmd, request);
                if (cmd.getResultDTO().isFailure()) {
                    errors.add("twoUserGroup", new ActionError("User.RelationUserGroup"));
                    saveErrors(request, errors);
                    forward = action.findForward("Fail");
                } else {
                    if (((Boolean) cmd.getResultDTO().get("hasDeletedItems")).booleanValue()) {
                        errors.add("deletedItems", new ActionError("error.deleteSelected"));
                        saveErrors(request, errors);
                    }
                    forward = super.execute(action, groupImportForm, request, response);
                }
                addFilter("userGroupId", cmd.getResultDTO().get("userGroupId").toString());
            } else if (request.getParameter("aditionals") == null) {
                errors.add("empty", new ActionError("User.RelationUserGroup.delete"));
                saveErrors(request, errors);
                forward = action.findForward("Fail");
            }
        }
        return forward;
    }
}