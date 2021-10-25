package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.ReadAttachCmd;
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
import java.io.IOException;
import java.io.InputStream;

/**
 * AlfaCentauro Team
 * This class implements the download of one attach
 *
 * @author Alvaro
 * @version $Id: ReadAttachAction.java 9161 2009-05-01 20:14:03Z ivan $
 */
public class ReadAttachAction extends WebmailDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        ReadAttachCmd readAttachCmd = new ReadAttachCmd();
        readAttachCmd.putParam("userMailId", defaultForm.getDto("userMailId"));
        readAttachCmd.putParam("attachId", defaultForm.getDto("attachId"));

        ResultDTO resultDTO = BusinessDelegate.i.execute(readAttachCmd, request);
        String fileName = resultDTO.get("attachName").toString();
        int fileSize = Integer.parseInt(resultDTO.get("attachSize").toString());
        InputStream in = (InputStream) resultDTO.get("attachStream");

        ServletOutputStream outStream = response.getOutputStream();

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);
        try {
            MimeTypeUtil.copy(outStream, in);
        } catch (IOException e) {
            log.debug("-> Write Attachment in " + ServletOutputStream.class.getName() + " Object FAIL ", e);
        } finally {
            in.close();
            outStream.flush();
        }

        return (null);
    }

}
