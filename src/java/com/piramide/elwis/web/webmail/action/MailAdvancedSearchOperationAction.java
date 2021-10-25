package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.webmail.form.MailAdvancedSearchOperationForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Jatun S.R.L.
 * This class helps with the moveTo operation in the MailAdvancedSearch
 *
 * @author Alvaro Sejas
 * @version 4.2.2
 */
public class MailAdvancedSearchOperationAction extends WebmailDefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        MailAdvancedSearchOperationForm mailAdvancedSearchForm = (MailAdvancedSearchOperationForm) form;
        log.debug("MailAdvancedSearchOperationAction executing................. button: " + mailAdvancedSearchForm.getMoveToButton());
        //Logic to move mails to another folder...
        Integer moveToFolderId = mailAdvancedSearchForm.getMoveToFolderId();
        Object moveToButton = mailAdvancedSearchForm.getMoveToButton();
        Object moveToTrash = mailAdvancedSearchForm.getMoveToTrash();
        Object emptyFolderToTrash = mailAdvancedSearchForm.getEmptyFolderToTrash();
        Object[] selectedMails = mailAdvancedSearchForm.getSelectedMails();

        if (moveToButton != null && moveToButton.toString().equals("true")
                && moveToFolderId != null && moveToFolderId > 0 && selectedMails != null && selectedMails.length > 0) {
            log.debug("Moving mails......... mails: " + Arrays.toString(selectedMails) + " toFolder: " + moveToFolderId);
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Integer userMailId = new Integer(user.getValue("userId").toString());

            mailAdvancedSearchForm.setDto("userMailId", userMailId);
            mailAdvancedSearchForm.setDto("companyId", user.getValue("companyId"));
            mailAdvancedSearchForm.setDto("op", "moveTo");
            mailAdvancedSearchForm.setDto("mailIds", selectedMails);
            mailAdvancedSearchForm.setDto("op_parameter", moveToFolderId);
        } else if (moveToTrash != null) {
            log.debug("Moving the following mails to the trash folders: " + Arrays.toString(selectedMails));
            mailAdvancedSearchForm.setDto("mailIds", selectedMails);
            mailAdvancedSearchForm.setDto("op", "moveToTrash");
        } else if (emptyFolderToTrash != null) {
            log.debug("move all to trash...");

            request.setAttribute("emailIdentifiersAdvancedList", mailAdvancedSearchForm.getEmailIdentifiersAsList());
            request.setAttribute("emailIdentifiersAdvancedSize", mailAdvancedSearchForm.getEmailIdentifiersAsList().size());

            return mapping.findForward("toMoveAllEmailsConfirmation");
        }
        return (super.execute(mapping, form, request, response));
    }
}
