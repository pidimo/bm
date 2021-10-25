package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Manages functionality of user: read,create,update,delete an import user
 *
 * @author Titus
 * @version UserManagerAction.java, v 2.0 Jun 23, 2004 10:28:18 AM
 */
public class UserManagerAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping action,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("******     UserManagerAction execution  ...");

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (action.findForward("Cancel"));
        }

        //user in the session
        //cheking if working product was not deleted by other user.
        log.debug("userId for user session  = " + request.getParameter("dto(userId)"));
        ActionErrors errors = new ActionErrors();

        if (!SchedulerConstants.EMPTY_VALUE.equals(request.getParameter("dto(employeeName)")) &&
                SchedulerConstants.EMPTY_VALUE.equals(request.getParameter("dto(addressName)"))) {
            errors = ForeignkeyValidator.i.validate(AdminConstants.TABLE_USER, "userid",
                    request.getParameter("dto(userId)"), errors, new ActionError("msg.NotFound",
                            request.getParameter("dto(employeeName)")));
        }
        if (SchedulerConstants.EMPTY_VALUE.equals(request.getParameter("dto(employeeName)")) &&
                !SchedulerConstants.EMPTY_VALUE.equals(request.getParameter("dto(addressName)"))) {
            errors = ForeignkeyValidator.i.validate(AdminConstants.TABLE_USER, "userid",
                    request.getParameter("dto(userId)"), errors, new ActionError("msg.NotFound",
                            request.getParameter("dto(addressName)")));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return action.findForward("MainSearch");
        }

        User uss = (User) request.getSession().getAttribute(Constants.USER_KEY);

        ((DefaultForm) form).setDto("currentUserId", uss.getValue(Constants.USERID).toString());
        ((DefaultForm) form).setDto("companyId", uss.getValue(Constants.COMPANYID).toString());

        return super.execute(action, form, request, response);
    }

}
