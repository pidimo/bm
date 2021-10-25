package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.webmail.el.Functions;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Jatun S.R.L.
 * This class implemets the view logic for  the mails advanced search
 *
 * @author Alvaro Sejas
 */
public class MailAdvancedSearchAction extends WebmailListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailAdvancedSearchAction..................................");
        SearchForm searchForm = (SearchForm) form;
        Object mailStateParam = searchForm.getParameter("mailStateParam");
        //managing the mail state filters for the list
        if (mailStateParam != null && mailStateParam.toString().length() > 0) {
            if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_UNREAD)) {
                searchForm.setParameter("mailState2", String.valueOf(MailStateUtil.READ));
            } else if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_READ)) {
                searchForm.setParameter("mailState", String.valueOf(MailStateUtil.READ));
            } else if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_UNANSWERED)) {
                searchForm.setParameter("mailState2", String.valueOf(MailStateUtil.ANSWERED));
            } else if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_ANSWERED)) {
                searchForm.setParameter("mailState", String.valueOf(MailStateUtil.ANSWERED));
            } else if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_UNFORWARD)) {
                searchForm.setParameter("mailState2", String.valueOf(MailStateUtil.FORWARD));
            } else if (mailStateParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_FORWARD)) {
                searchForm.setParameter("mailState", String.valueOf(MailStateUtil.FORWARD));
            }
        }

        Object mailToParam = searchForm.getParameter("mailTo");
        if (mailToParam != null && !GenericValidator.isBlankOrNull(mailToParam.toString())) {
            searchForm.setParameter("mailTo", mailToParam.toString());
            searchForm.setParameter("mailPersonalTo", mailToParam.toString());
            searchForm.setParameter("enableToRecipientFilter", "true");
        }

        Object mailToCCParam = searchForm.getParameter("mailToCC");
        if (mailToCCParam != null && !GenericValidator.isBlankOrNull(mailToCCParam.toString())) {
            searchForm.setParameter("mailToCC", mailToCCParam.toString());
            searchForm.setParameter("mailPersonalToCC", mailToCCParam.toString());
            searchForm.setParameter("enableCcRecipientFilter", "true");
        }
        //Attach size filter
        Object startSizeRange = searchForm.getParameter("startSizeRange");
        if (startSizeRange != null && startSizeRange.toString().length() > 0) {
            Integer startRangeInt = (new Integer(startSizeRange.toString()));
            if (startRangeInt.equals(new Integer(1))) {
                startRangeInt = 0;
            }
            startRangeInt = startRangeInt * 1024;
            searchForm.setParameter("startSizeRangeBytes", startRangeInt.toString());
        }

        Object endSizeRange = searchForm.getParameter("endSizeRange");
        if (endSizeRange != null && endSizeRange.toString().length() > 0) {
            Integer endRangeInt = (new Integer(endSizeRange.toString()) * 1024) + 1023;
            searchForm.setParameter("endSizeRangeBytes", endRangeInt.toString());
        }

        Object mailAttachParam = searchForm.getParameter("mailAttach");
        if (mailAttachParam != null && mailAttachParam.toString().length() > 0) {
            if (mailAttachParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_WITHATTACH)) {
                searchForm.setParameter("haveAttachment", "1");
            } else if (mailAttachParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_WITHOUTATTACH)) {
                searchForm.setParameter("haveAttachment", "0");
            }
        }
        //The mail has communications? (relations with mailcontact)
        Object mailCommunicationParam = searchForm.getParameter("mailCommunication");
        if (mailCommunicationParam != null && mailCommunicationParam.toString().length() > 0) {
            if (mailCommunicationParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_WITHCOMMUNICATION)) {
                searchForm.setParameter("haveCommunications", "true");
            } else if (mailCommunicationParam.toString().equals(WebMailConstants.MAIL_ADVANCEDFILTER_WITHOUTCOMMUNICATION)) {
                searchForm.setParameter("haveNotCommunications", "true");
            }
        }

        ActionForward actionForward = super.execute(mapping, form, request, response);
        String folderColumnToShow;
        org.alfacentauro.fantabulous.controller.ResultList rs;
        //Column to show
        Object searchFolderId = searchForm.getParameter("mailFolderId");
        if (searchFolderId != null && searchFolderId.toString().length() > 0) {
            folderColumnToShow = Functions.getFolderColumnToShow(searchFolderId.toString());
        } else {
            folderColumnToShow = WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString();
        }

        rs = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("advancedSearchMailList");
        if (rs != null && rs.getResult() != null) {
            //Adding some column for the list
            WebMailNavigationUtil.setComposedValuesInResultList(request, rs, folderColumnToShow);
            //Add the mailIndex (for readMail pagging)
            WebMailNavigationUtil.setMailIndex(rs);

            if (!rs.getResult().isEmpty()) {
                readEmailIdentifiers(request, searchForm);
            }
        }
        return (actionForward);
    }

    private void readEmailIdentifiers(HttpServletRequest request, ActionForm form) {
        String listName = "advancedSearchMailList";

        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), request);
        ListStructure list = null;
        try {
            list = fantabulousManager.getList(listName);
        } catch (ListStructureNotFoundException e) {
            log.debug("->Read List " + listName + " In Fantabulous structure Fail");
        }

        if (null == list) {
            return;
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userMailId = new Integer(user.getValue("userId").toString());

        SearchForm searchForm = (SearchForm) form;
        Parameters parameters = new Parameters();
        parameters.addSearchParameters(getParameters(searchForm.getParams()));
        parameters.addSearchParameter("userMailId", String.valueOf(userMailId));
        parameters.addSearchParameter("companyId", String.valueOf(user.getValue("companyId")));

        Collection result = org.alfacentauro.fantabulous.controller.Controller.getList(list, parameters);
        String emailIds = "";

        for (Object object : result) {
            org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) object;
            emailIds += fieldHash.get("MAILID").toString() + ",";
        }

        request.setAttribute("emailIdentifiersAdvanced", emailIds);
    }

}
