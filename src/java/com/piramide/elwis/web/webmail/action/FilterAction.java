package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.EmailUserCmd;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * this class help a create IU to filters
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FilterAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class FilterAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing FilterAction................");

        ActionForward forward;
        forward = null;

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        //get user
        User user = (User) request.getSession().getAttribute("user");
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("hasEmailAccount");
        emailUserCmd.putParam("emailUserId", user.getValue("userId"));
        ResultDTO resultDTO = BusinessDelegate.i.execute(emailUserCmd, request);

        //set userMailId
        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("userMailId", resultDTO.get(UserMailDTO.KEY_USERMAILID));

        forward = super.execute(mapping, form, request, response);

        return forward;
    }
}
