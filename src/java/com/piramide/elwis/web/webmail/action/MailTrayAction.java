package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.MailTrayCmd;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.webmail.el.Functions;
import com.piramide.elwis.web.webmail.form.MailTrayForm;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class helps to the view of the mail tray
 *
 * @author Alvaro
 * @version $Id: MailTrayAction.java 12724 2017-12-18 21:21:31Z miguel $
 */
public class MailTrayAction extends WebmailListAction {
    private Log log = LogFactory.getLog(this.getClass());

    private boolean isSearch = false;
    private String searchFolderId = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailTrayAction..................... " + request.getParameterMap());

        request.getSession().removeAttribute("mailId");
        request.getSession().removeAttribute("saveSendItem");
        request.getSession().removeAttribute("mailListDispatched");

        Object returning = request.getParameter("returning");
        MailTrayForm f = (MailTrayForm) form;

        Object mailFilter = f.getMailFilter();
        boolean isDelete = false;
        boolean moveAllEmailstoTrash = false;

        if (mailFilter != null && returning == null) {
            String strMailFilter = mailFilter.toString();
            if (strMailFilter.equals(WebMailConstants.MAIL_FILTERS[0])) {
                f.setParameter("mailPriority", "");
            } else if (strMailFilter.equals(WebMailConstants.MAIL_FILTERS[1])) {
                f.setParameter("mailState2", String.valueOf(MailStateUtil.READ));
            } else if (strMailFilter.equals(WebMailConstants.MAIL_FILTERS[2])) {
                f.setParameter("mailPriority", WebMailConstants.MAIL_PRIORITY_HIGHT);
            } else if (strMailFilter.equals(WebMailConstants.MAIL_FILTERS[3])) {
                f.setParameter("mailState2", String.valueOf(MailStateUtil.ANSWERED));
            } else if (strMailFilter.equals(WebMailConstants.MAIL_FILTERS[4])) {
                f.setParameter("mailState", String.valueOf(MailStateUtil.ANSWERED));
            }
            request.setAttribute("mailFilter", strMailFilter);
        }
        //Read the userMailId
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        Object folderId = f.getFolderId();

        Integer userMailId = new Integer(user.getValue("userId").toString());
        DTO dto = new DTO();
        dto.put("userMailId", userMailId);
        dto.put("companyId", user.getValue("companyId"));
        dto.put("folderId", folderId);

        Object moveTo = f.getMoveTo();
        Object moveToButton = f.getMoveToButton();
        Object markAs = f.getMarkAs();
        Object delete = f.getDelete();
        Object toEmptyFolder = f.getToEmptyFolder();
        Object emptyFolder = f.getEmptyFolder();
        Object moveToTrash = f.getMoveToTrash();
        Object emptyFolderToTrash = f.getEmptyFolderToTrash();

        Object[] selectedMails = f.getSelectedMails();
        if (moveToButton != null && moveTo != null && (moveTo != null && moveTo.toString().length() > 0) && selectedMails.length > 0) {
            dto.put("op", "moveTo");
            dto.put("mailIds", selectedMails);
            dto.put("op_parameter", moveTo);
        } else if (markAs != null && (markAs != null && markAs.toString().length() > 0) && selectedMails.length > 0) {
            dto.put("op", "markAs");
            dto.put("mailIds", selectedMails);
            dto.put("op_parameter", markAs);
        } else if (delete != null && selectedMails.length > 0) {
            dto.put("op", "readMailsToDelete");
            dto.put("mailIds", selectedMails);
            isDelete = true;
        } else if (emptyFolder != null) {
            dto.put("op", "readFolderToEmpty");
            dto.put("toEmptyFolder", toEmptyFolder);
            isDelete = true;
        } else if (emptyFolderToTrash != null) {
            dto.put("op", "emptyFolderToTrash");
            if (!f.getEmailIdentifiersAsList().isEmpty()) {
                dto.put("emailIdentifiers", f.getEmailIdentifiersAsList());
            }
            moveAllEmailstoTrash = true;
        } else if (moveToTrash != null) {
            dto.put("mailIds", selectedMails);
            dto.put("op", "moveToTrash");
        }

        MailTrayCmd mailTrayCmd = new MailTrayCmd();
        mailTrayCmd.putParam(dto);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO = BusinessDelegate.i.execute(mailTrayCmd, request);

        if (!isDelete) {
            if (!isSearch) {
                if (folderId != null && !GenericValidator.isBlankOrNull(folderId.toString())) {
                    if (returning == null) {
                        f.setParameter("mailFolderId", f.getFolderId().toString());
                    }
                    request.getSession().setAttribute("folderView", f.getFolderId().toString());
                } else {
                    if (returning == null) {
                        f.setParameter("mailFolderId", resultDTO.get("folderId").toString());
                    }
                    request.getSession().setAttribute("folderView", resultDTO.get("folderId").toString());
                    folderId = resultDTO.get("folderId").toString();
                }
            }
            f.setDto(resultDTO);
            f.setMoveTo(null);
            f.setMarkAs(null);
            super.execute(mapping, form, request, response);

            //For the mailFrom
            String folderColumnToShow;
            org.alfacentauro.fantabulous.controller.ResultList rs;
            if (!isSearch) {
                folderColumnToShow = Functions.getFolderColumnToShow(folderId.toString());
                rs = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("mailTrayList");
            } else {
                if (searchFolderId != null) {
                    folderColumnToShow = Functions.getFolderColumnToShow(searchFolderId);
                } else {
                    folderColumnToShow = WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString();
                }

                rs = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("searchMailList");
                if (!rs.getResult().isEmpty()) {
                    readEmailIdentifiers(request, "searchMailList", userMailId, form);
                } else {
                    moveAllEmailstoTrash = false;
                }
            }

            WebMailNavigationUtil.setComposedValuesInResultList(request, rs, folderColumnToShow);

            WebMailNavigationUtil.setMailIndex(rs);
        } else {
            ArrayList readMails = (ArrayList) resultDTO.get("readMails");
            if (readMails != null) {
                for (int i = 0; i < readMails.size(); i++) {
                    MailDTO mailDTO = (MailDTO) readMails.get(i);
                    mailDTO.put("mailTos", JSPHelper.getName_Emails((ArrayList) mailDTO.get("mailTos")));
                    mailDTO.put("mailTosCC", JSPHelper.getName_Emails((ArrayList) mailDTO.get("mailTosCC")));
                    mailDTO.put("mailFrom", JSPHelper.getName_Email((HashMap) mailDTO.get("mailFrom")));
                }
            }
            f.setDto(resultDTO);
        }
        ActionForward actionForward = new ActionForward();
        if (isDelete) {
            actionForward = mapping.findForward("toDeleteConfirmation");
        } else {
            if (moveAllEmailstoTrash) {
                actionForward = mapping.findForward("toMoveAllEmailsConfirmation");
            } else {
                actionForward = mapping.findForward("Success");
            }
        }
        return (actionForward);
    }

    public void readEmailIdentifiers(HttpServletRequest request, String listName, Integer userMailId, ActionForm form) {
        FantabulousManager fantabulousManager =
                FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), request);

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

        SearchForm searchForm = (SearchForm) form;
        Parameters parameters = new Parameters();
        parameters.addSearchParameters(getParameters(searchForm.getParams()));
        parameters.addSearchParameter("userMailId", String.valueOf(userMailId));
        parameters.addSearchParameter("companyId", String.valueOf(user.getValue("companyId")));


        Collection result = org.alfacentauro.fantabulous.controller.Controller.getList(list, parameters);
        String emailIds = "";

        for (Object object : result) {
            org.alfacentauro.fantabulous.result.FieldHash fieldHash =
                    (org.alfacentauro.fantabulous.result.FieldHash) object;
            emailIds += fieldHash.get("MAILID").toString() + ",";
        }

        request.setAttribute("emailIdentifiers", emailIds);
    }


    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public void setSearchFolderId(String searchFolderId) {
        this.searchFolderId = searchFolderId;
    }
}