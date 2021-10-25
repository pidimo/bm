package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ActionSuperExecute;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: SupportCaseBaseAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SupportCaseBaseAction extends DefaultAction implements ActionSuperExecute {

    User user;
    boolean isExternalUser;

    private boolean validateSupportCase(String caseId, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_SUPPORT_CASE, "caseId",
                caseId, errors, new ActionError("msg.NotFound", request.getParameter("dto(caseTitle)")));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return errors.isEmpty();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug(" ....SupportCaseBaseAction execute function .... !!!");
        ActionForward forward;
        ActionErrors aErrors = new ActionErrors();

        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        String caseId = "";
        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        if (doValidate() && !validateSupportCase(caseId, request)) {
            return mapping.findForward("CaseMainSearch");
        }

        user = RequestUtils.getUser(request);
        ((DefaultForm) form).setDto("caseId", caseId);
        ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
        ((DefaultForm) form).setDto("userType", user.getValue("userType").toString());
        isExternalUser = AdminConstants.EXTERNAL_USER.equals(user.getValue("userType").toString());

        ActionForward actionForward = myExecute(mapping, form, request, response);

        return actionForward;
    }

    protected boolean doValidate() {
        return false;
    }

    public ActionForward myExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return superExecute(mapping, form, request, response);
    }

    public ActionForward superExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.execute(mapping, form, request, response);
    }

    public void mySaveErrors(HttpServletRequest request, ActionErrors errors) {
        saveErrors(request, errors);
    }

    public User getUser() {
        return user;
    }
}