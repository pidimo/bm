package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.contactmanager.CommunicationDocumentDownloadCmd;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class CommunicationDocumentDownloadAction extends DefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ContactDTO communication = getDocument(request);
        if (null == communication) {
            saveNotFoundError(request);
            return mapping.findForward("Fail");
        }

        InputStream inputStream = (InputStream) communication.get("inputStream");
        if (null == inputStream) {
            return mapping.findForward("Fail");
        }

        String fileName = (String) communication.get("documentFileName");
        if (null == fileName) {
            fileName = "unknownFileName";
        }

        Integer fileSize = (Integer) communication.get("inputStreamSize");

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        try {
            MimeTypeUtil.copy(os, inputStream);
        } catch (IOException e) {
            //
        } finally {
            os.flush();
            inputStream.close();
        }

        return null;
    }

    private ContactDTO getDocument(HttpServletRequest request) {
        CommunicationDocumentDownloadCmd cmd = new CommunicationDocumentDownloadCmd();
        cmd.setOp("getDocument");
        cmd.putParam("contactId", request.getParameter("communicationId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);

            return (ContactDTO) resultDTO.get("getDocument");
        } catch (AppLevelException e) {
            //
        }

        return null;
    }

    protected void saveNotFoundError(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors.add("ContactNotFound", new ActionError("Communication.error.notFound"));
        saveErrors(request, errors);
    }
}
