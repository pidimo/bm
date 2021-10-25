package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.form.NotificationForm;
import com.piramide.elwis.web.admin.session.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 22, 2005
 * Time: 4:26:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class NotificationAction extends com.piramide.elwis.web.common.action.DefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  NotificationAction executeFunction  --------");

        NotificationForm notificationForm = (NotificationForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        notificationForm.setDto("userId", user.getValue("userId"));
        notificationForm.setDto("view", request.getParameter("view"));

        return super.execute(mapping, notificationForm, request, response);
    }
}
