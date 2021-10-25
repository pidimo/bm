package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.admin.session.UserSessionLogUtil;
import com.piramide.elwis.web.common.UserBannedData;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Action to manage the functionality of banned other user connected
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.1
 */
public class BannedPreviousUserConnectedAction extends DefaultAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing BannedPreviousUserConnectedAction................" + request.getParameterMap());

        HttpSession session = request.getSession(false); //it does not creates a new session.
        User user = RequestUtils.getUser(request);

        if (session != null && user != null) {

            Map otherUserInfoMap = UserSessionLogUtil.otherUserConnectedInfo((Integer) user.getValue("userId"), session.getId());

            if (otherUserInfoMap.get("otherUserSessionId") != null) {
                //banned the other user coneccted, put to the context in the format userID-sessionId
                ((Map) getServlet().getServletContext().getAttribute("bannedUser"))
                        .put(user.getValue("userId") + "-" + otherUserInfoMap.get("otherUserSessionId"),
                                new UserBannedData("1", request.getRemoteAddr()));
            }

            //User log tracking, start session logs
            UserSessionLogUtil.startSession(user, session.getId(), false, request);
        }

        return mapping.findForward("Success");
    }
}
