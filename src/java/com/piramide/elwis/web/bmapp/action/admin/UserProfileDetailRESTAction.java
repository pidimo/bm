package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.web.admin.el.Functions;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action to get the user profile information to mobile app
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class UserProfileDetailRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  UserProfileDetailRESTAction..." + request.getParameterMap());

        ActionForward forward = null;
        DefaultForm defaultForm = (DefaultForm) form;
        log.debug("FORM DTO FROM REST:::::::" + defaultForm.getDtoMap());

        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");

        //define user values as contact person
        Map userMap = Functions.getUserMap(userId);

        if (userMap.get("mobileOrganizationId") != null) {
            Map organizationInfoMap = com.piramide.elwis.web.contactmanager.el.Functions.getAddressMap(userMap.get("mobileOrganizationId"));

            ActionForwardParameters forwardParameters = new ActionForwardParameters();
            forwardParameters.add("contactId", organizationInfoMap.get("addressId").toString()).
                    add("dto(addressId)", organizationInfoMap.get("addressId").toString()).
                    add("dto(contactPersonId)", userMap.get("addressId").toString()).
                    add("dto(addressType)", organizationInfoMap.get("addressType").toString());

            forward = forwardParameters.forward(mapping.findForward("Success"));
        } else {

            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("User.mobile.error.emptyOrganization"));
            saveErrors(request, errors);

            forward = mapping.findForward("Fail");
        }

        return forward;
    }

}
