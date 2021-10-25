package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: AccessRightAction.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 17-05-2005 04:41:02 PM ivan Exp $
 */
public class AccessRightAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing AccessRigthAction...");

        ActionErrors errors = new ActionErrors();
        DefaultForm myForm = (DefaultForm) form;

        Integer roleId = Integer.valueOf((String) myForm.getDto("roleId"));

        //check foreing key
        ForeignkeyValidator.i.validate(AdminConstants.TABLE_ROLE, "roleId",
                roleId, errors, new ActionError("customMsg.NotFound", request.getParameter("roleName")));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Failure");
        }

        return super.execute(mapping, form, request, response);
    }
}
