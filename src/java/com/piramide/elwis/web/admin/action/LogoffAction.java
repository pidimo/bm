package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.MailTrayCmd;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Log off from application Action
 *
 * @author Fernando Montaño
 * @version $Id: LogoffAction.java 12538 2016-04-12 20:54:18Z miguel $
 */
public class LogoffAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Execute the logoff action
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        //emptyTrash
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userMailId = Integer.valueOf(user.getValue("userId").toString());

        UserMailDTO dto = new UserMailDTO();
        dto.put(UserMailDTO.KEY_USERMAILID, userMailId);
        UserMail userMail = null;
        ResultDTO resultDTO = new ResultDTO();
        userMail = (UserMail) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
        boolean haveMailDeletePermission = Functions.hasAccessRight(request, "MAIL", "DELETE");
        if (userMail != null && !resultDTO.isFailure() &&
                userMail.getEmptyTrashLogout().booleanValue() &&
                haveMailDeletePermission) {
            MailTrayCmd mailTrayCmd = new MailTrayCmd();
            mailTrayCmd.putParam("userMailId", userMailId);
            mailTrayCmd.putParam("op", "emptyTrashOnExit");
            mailTrayCmd.putParam("companyId", Integer.valueOf(user.getValue("companyId").toString()));
            BusinessDelegate.i.execute(mailTrayCmd, request);
        }

        if ("true".equals(request.getParameter("keepUserSessionAsActive"))) {
            user.setCloseSessionsWhenInvalidate(false);
        }

        log.debug("Logoff Action executing... Thank you for use Elwis");
        request.getSession().invalidate();
        return mapping.findForward("Logoff");
    }
}
