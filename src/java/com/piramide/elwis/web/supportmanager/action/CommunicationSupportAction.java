package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.supportmanager.SupportCaseCommunicationCmd;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.MainCommunicationAction;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CommunicationSupportAction extends MainCommunicationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String caseId = "";
        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        User user = RequestUtils.getUser(request);
        ((DefaultForm) form).setDto("caseId", caseId);
        ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
        ((DefaultForm) form).setDto("userType", user.getValue("userType").toString());

        return super.execute(mapping, form, request, response);
    }

    protected ActionForward checkMainReferences(HttpServletRequest request, ActionMapping mapping) {
        String caseId = "";
        if (request.getParameter("caseId") != null && !"".equals(request.getParameter("caseId"))) {
            caseId = request.getParameter("caseId");
        } else {
            caseId = request.getParameter("dto(caseId)");
        }

        ActionErrors errors = new ActionErrors();

        errors = ForeignkeyValidator.i.validate(SupportConstants.TABLE_SUPPORT_CASE, "caseId",
                caseId, errors, new ActionError("SupportCase.error.notFound"));
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("CaseMainSearch");
        }
        return null;
    }

    protected EJBCommand getGenerateDocumentCmd() {
        return new SupportCaseCommunicationCmd();
    }
}
