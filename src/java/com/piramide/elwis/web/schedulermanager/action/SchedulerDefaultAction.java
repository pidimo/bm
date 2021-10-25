package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: SchedulerDefaultAction.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 04-05-2005 11:32:08 AM ivan Exp $
 */
public class SchedulerDefaultAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing SchedulerDefaultAction... ");

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        log.debug("Setting up in form the userId and companyId values...");
        DefaultForm newForm = (DefaultForm) form;
        newForm.setDto("userId", user.getValue("userId"));
        newForm.setDto("companyId", user.getValue("companyId"));

        return super.execute(mapping, newForm, request, response);
    }
}
