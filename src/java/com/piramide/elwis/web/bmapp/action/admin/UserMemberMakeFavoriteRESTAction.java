package com.piramide.elwis.web.bmapp.action.admin;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action to manage favorite members
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class UserMemberMakeFavoriteRESTAction extends DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  UserMemberMakeFavoriteRESTAction..." + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;

        String contactPersonId = request.getParameter("contactPersonId");
        Boolean isFavorite = null;

        if ("true".equals(request.getParameter("markMemberAsFavorite"))) {
            isFavorite = Boolean.TRUE;
        } else if ("false".equals(request.getParameter("markMemberAsFavorite"))) {
            isFavorite = Boolean.FALSE;
        }

        ActionForward forward;
        if (isFavorite != null && !GenericValidator.isBlankOrNull(contactPersonId)) {
            User user = RequestUtils.getUser(request);

            defaultForm.setDto("op", "makeAsFavorite");
            defaultForm.setDto("addressId", contactPersonId);
            defaultForm.setDto("companyId", user.getValue(Constants.COMPANYID));
            defaultForm.setDto("isFavoriteWVApp", isFavorite);

            forward = super.execute(mapping, defaultForm, request, response);
        } else {
            forward = mapping.findForward("Fail");
        }

        return forward;
    }
}
