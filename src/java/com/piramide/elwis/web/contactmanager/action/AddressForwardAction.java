package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action Class to add params needed for update recent information of address readed
 *
 * @author Fernando Montaño
 * @version $Id: AddressForwardAction.java 10382 2013-10-08 00:13:32Z miguel $
 */
public class AddressForwardAction extends AddressAccessRightSecurityAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("AddressForwardAction executing...");
        DefaultForm defaultForm = (DefaultForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        defaultForm.setDto("userId", user.getValue("userId"));
        defaultForm.setDto("companyId", user.getValue("companyId"));
        defaultForm.setDto("locale", user.getValue("locale"));

        return super.execute(mapping, form, request, response); //execute the command
    }

}
