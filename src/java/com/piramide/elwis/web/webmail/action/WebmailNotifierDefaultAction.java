package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class WebmailNotifierDefaultAction extends DefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        ActionForward forward;
        if (null != (forward = priorValidations(defaultForm, request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, defaultForm, request, response);

        setPageAttributes(defaultForm, request);

        return forward;
    }

    private ActionForward priorValidations(DefaultForm defaultForm,
                                           HttpServletRequest request,
                                           ActionMapping mapping) {
        //check userMail accessRights
        boolean hasExecutePermission = Functions.hasAccessRight(request, "MAIL", "EXECUTE");
        if (!hasExecutePermission) {
            return mapping.findForward("Fail");
        }


        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        MailFormHelper mailFormHelper = new MailFormHelper();
        boolean isWebmailUser = mailFormHelper.isWebmailUser(request);
        if (!isWebmailUser) {
            return mapping.findForward("Fail");
        }

        //read new emails by folder, to show pop-up notifier
        List<Map> newEmailsByFolder = com.piramide.elwis.web.webmail.el.Functions.getNewEmailsByFolder(userId.toString(), request);

        //setting up userMailId in defaultForm object
        defaultForm.setDto("userMailId", userId);
        defaultForm.setDto("newEmailsByFolder", newEmailsByFolder);

        return null;
    }

    protected void setPageAttributes(DefaultForm defaultForm, HttpServletRequest request) {
        Integer selectedFolderId = getSelectedFolderId(request.getSession().getAttribute("folderView"));

        request.setAttribute("selectedFolderId", selectedFolderId);

        String hasViewWebmailList = request.getParameter("hasViewWebmailList");
        if ("true".equals(hasViewWebmailList)) {
            List<Integer> folderIdentifiers = (List<Integer>) defaultForm.getDto("folderIdentifiers");
            if (null != folderIdentifiers && null != selectedFolderId && folderIdentifiers.contains(selectedFolderId)) {
                request.setAttribute("hasViewWebmailList", true);
            }
        }

        String hasViewWebmailFolders = request.getParameter("hasViewWebmailFolders");
        if ("true".equals(hasViewWebmailFolders)) {
            request.setAttribute("hasViewWebmailFolders", true);
        }

        String enableFolderSelector = request.getParameter("enableFolderSelector");
        if (!"true".equals(enableFolderSelector)) {
            request.setAttribute("enableFolderSelector", false);
        }
    }

    private Integer getSelectedFolderId(Object object) {
        if (null == object) {
            return null;
        }

        if (object instanceof Integer) {
            return (Integer) object;
        }

        try {
            return new Integer(object.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
