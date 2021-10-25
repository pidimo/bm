package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * Action to download campaign generation attach
 *
 * @author Miky
 * @version $Id: DownloadGenerationAttachAction.java 9161 2009-05-01 20:14:03Z ivan $
 */
public class DownloadGenerationAttachAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DownloadGenerationAttachAction..." + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        String fileName = defaultForm.getDto("attachName").toString();

        DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
        downloadCmd.putParam("freeTextId", defaultForm.getDto("freeTextId"));

        ResultDTO resultDTO = BusinessDelegate.i.execute(downloadCmd, request);
        ArrayByteWrapper textByteWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
        int fileSize = textByteWrapper.getFileData().length;

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(textByteWrapper.getFileData());
        os.flush();
        os.close();

        return null;
    }
}
