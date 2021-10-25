package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.LanguageCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action Forward class, used to put predefined telecomtypes into addressform
 *
 * @author Fernando Montaño
 * @version $Id: AddressCreateForwardAction.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class AddressCreateForwardAction extends CheckEntriesForwardAction {
    private Log log = LogFactory.getLog(AddressCreateForwardAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Address-ContactPerson; CreateForwardAction executing...");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        LanguageCmd cmd = new LanguageCmd();

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("languageId", cmd.getLanguageByDefault(new Integer(user.getValue("companyId").toString())));
        defaultForm.setDto("telecomMap", AddressContactPersonHelper.getDefaultTelecomTypes(request));

        return super.execute(mapping, form, request, response); //execute the command
    }
}
