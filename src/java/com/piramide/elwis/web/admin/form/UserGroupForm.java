package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.cmd.admin.UserGroupCmd;
import com.piramide.elwis.cmd.admin.UserGroupReadCmd;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class UserGroupForm extends DefaultForm {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Execute UserGroupForm validation..." + request.getParameterMap());
        log.debug("Form map:" + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        //validate assigned groups
        if (errors.isEmpty()) {
            validateGroupType(errors, request);
        }
        return errors;
    }

    private void validateGroupType(ActionErrors errors, HttpServletRequest request) {
        String op = (String) this.getDto("op");
        if ("update".equals(op)) {

            Integer userGroupId = new Integer(this.getDto("userGroupId").toString());
            Integer oldGroupType = null;

            UserGroupCmd userGroupCmd = new UserGroupCmd();
            userGroupCmd.setOp("read");
            userGroupCmd.putParam("userGroupId", userGroupId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupCmd, request);
                if (!resultDTO.isFailure() && resultDTO.get("groupType") != null) {
                    oldGroupType = new Integer(resultDTO.get("groupType").toString());
                }

            } catch (AppLevelException e) {
                log.debug("Error in execute " + UserGroupCmd.class.getName() + " FAIL ", e);
            }

            if (oldGroupType != null && !oldGroupType.toString().equals(this.getDto("groupType"))) {
                if (AdminConstants.UserGroupType.DATA_ACCESS_SECURITY.equal(oldGroupType)) {
                    validateDataAccessSecurityGroupAssigned(userGroupId, errors, request);
                } else if (AdminConstants.UserGroupType.SCHEDULER.equal(oldGroupType)) {
                    validateSchedulerGroupAssigned(userGroupId, errors, request);
                } else if (AdminConstants.UserGroupType.ARTICLE.equal(oldGroupType)) {
                    validateArticleSecurityGroupAssigned(userGroupId, errors, request);
                }
            }
        }
    }

    private void validateDataAccessSecurityGroupAssigned(Integer userGroupId, ActionErrors errors, HttpServletRequest request) {

        UserGroupReadCmd userGroupReadCmd = new UserGroupReadCmd();
        userGroupReadCmd.setOp("groupAssignedInDataAccess");
        userGroupReadCmd.putParam("userGroupId", userGroupId);

        Boolean hasAssigned = Boolean.FALSE;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupReadCmd, request);
            if (resultDTO.get("hasAssignedDataAccess") != null) {
                hasAssigned = (Boolean) resultDTO.get("hasAssignedDataAccess");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + UserGroupReadCmd.class.getName() + " FAIL", e);
        }

        if (hasAssigned) {
            errors.add("assignedError", new ActionError("UserGroup.type.changeError.dataAccess", JSPHelper.getMessage(request, "UserGroup.groupType")));
        }
    }

    private void validateSchedulerGroupAssigned(Integer userGroupId, ActionErrors errors, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        UserGroupReadCmd userGroupReadCmd = new UserGroupReadCmd();
        userGroupReadCmd.setOp("groupAssignedInScheduler");
        userGroupReadCmd.putParam("userGroupId", userGroupId);
        userGroupReadCmd.putParam("companyId", user.getValue("companyId"));

        Boolean hasAssigned = Boolean.FALSE;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupReadCmd, request);
            if (resultDTO.get("hasAssignedScheduler") != null) {
                hasAssigned = (Boolean) resultDTO.get("hasAssignedScheduler");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + UserGroupReadCmd.class.getName() + " FAIL", e);
        }

        if (hasAssigned) {
            errors.add("assignedError", new ActionError("UserGroup.type.changeError.scheduler", JSPHelper.getMessage(request, "UserGroup.groupType")));
        }
    }

    private void validateArticleSecurityGroupAssigned(Integer userGroupId, ActionErrors errors, HttpServletRequest request) {

        UserGroupReadCmd userGroupReadCmd = new UserGroupReadCmd();
        userGroupReadCmd.setOp("groupAssignedInArticle");
        userGroupReadCmd.putParam("userGroupId", userGroupId);

        Boolean hasAssigned = Boolean.FALSE;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupReadCmd, request);
            if (resultDTO.get("hasAssignedArticle") != null) {
                hasAssigned = (Boolean) resultDTO.get("hasAssignedArticle");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + UserGroupReadCmd.class.getName() + " FAIL", e);
        }

        if (hasAssigned) {
            errors.add("assignedError", new ActionError("UserGroup.type.changeError.article", JSPHelper.getMessage(request, "UserGroup.groupType")));
        }
    }


}
