package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
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
 * Jatun S.R.L.
 *
 * @author Alvaro Sejas
 * @version 4.2.2
 */
public class ReadOperationAction extends WebmailDefaultAction {
    private Log log = LogFactory.getLog(this.getClass());


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ReadOperationAction executing....................................");
        DefaultForm defaultForm = (DefaultForm) form;
        User user = RequestUtils.getUser(request);
        defaultForm.setDto("userMailId", user.getValue("userId"));
        defaultForm.setDto("companyId", user.getValue("companyId"));
        defaultForm.setDto("mailIds", new Object[]{defaultForm.getDto("mailId")});
        defaultForm.setDto("op_parameter", defaultForm.getDto("folderId"));

        super.execute(mapping, form, request, response);

        String nextMailIndex = request.getParameter("nextMailIndex");
        String nextMailId = request.getParameter("nextMailId");
        if (GenericValidator.isBlankOrNull(nextMailId)
                || GenericValidator.isBlankOrNull(nextMailIndex)) {
            ActionForward cancelAction = mapping.findForward("CancelToMailTray");
            if (WebMailNavigationUtil.isAvancedSearch(request)) {
                cancelAction = mapping.findForward("CancelToMailAdvancedSearch");
            } else if (WebMailNavigationUtil.isSearch(request)) {
                cancelAction = mapping.findForward("CancelToMailSearch");
            }
            return cancelAction;
        }

        //because is there moved mail to other folder, the mail index so should be minus one
        Integer mailIndex = Integer.valueOf(nextMailIndex) - 1;

        return new ActionForwardParameters().
                add("dto(mailId)", nextMailId).
                add("mailIndex", mailIndex.toString()).forward(mapping.findForward("Next"));

    }
}

