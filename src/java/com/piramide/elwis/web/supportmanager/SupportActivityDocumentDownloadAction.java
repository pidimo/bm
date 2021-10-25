package com.piramide.elwis.web.supportmanager;

import com.piramide.elwis.web.contactmanager.action.CommunicationDocumentDownloadAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class SupportActivityDocumentDownloadAction extends CommunicationDocumentDownloadAction {
    @Override
    protected void saveNotFoundError(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors.add("ContactNotFound", new ActionError("SupportActivity.error.communicationNotFound"));
        saveErrors(request, errors);
    }
}
