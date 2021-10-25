package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Download temporal image
 *
 * @author Miky
 * @version $Id: DownloadTemporalImageAction.java 2009-05-25 06:30:38 PM $
 */
public class DownloadTemporalImageAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DownloadTemporalImageAction ...." + request.getParameterMap());

        String imageStoreId = request.getParameter("imageStoreId");

        ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
        imageStoreCmd.putParam("op", "download");
        imageStoreCmd.putParam("imageStoreId", imageStoreId);

        ResultDTO resultDTO = BusinessDelegate.i.execute(imageStoreCmd, request);
        if (resultDTO.isFailure()) {
            log.debug("Fail Download Document");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        log.debug("Sending document");
        ArrayByteWrapper wrapper = (ArrayByteWrapper) resultDTO.get("imageWrapper");
        String fileName = wrapper.getFileName();
        int fileSize = wrapper.getFileData().length;

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(wrapper.getFileData());
        os.flush();
        os.close();
        return null;
    }
}