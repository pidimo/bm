package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 21, 2005
 * Time: 4:00:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserOfGroupListAction extends com.piramide.elwis.web.common.action.ListAction {

    private Log log = LogFactory.getLog(UserOfGroupListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)");
        SearchForm listForm = (SearchForm) form;

        ActionErrors errors = new ActionErrors();
        String groupId = null;

        if ("Cancel".equals(request.getParameter("dto(cancel)"))) {
            return mapping.findForward("Cancel");
        }

        if (request.getParameter("userGroupId") != null) {
            groupId = request.getParameter("userGroupId");
        }
        if (request.getAttribute("userGroupId") != null) {
            groupId = request.getAttribute("userGroupId").toString();
        }
        if (groupId != null) {
            errors = ForeignkeyValidator.i.validate(AdminConstants.TABLE_USERGROUP, "userGroupId",
                    groupId, errors, new ActionError("customMsg.NotFound", request.getParameter("groupName")));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);

                return mapping.findForward("Fail");
            }
        } else {
            errors.add("userGroupId", new ActionError("customMsg.NotFound", request.getParameter("groupName")));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        listForm.setParameter("userGroupId", groupId); //adding module userGroupId filter value.

        return super.execute(mapping, form, request, response);
    }
}
