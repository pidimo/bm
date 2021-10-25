package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.DownloadPictureCmd;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
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
 *
 * @author ivan
 * @version $Id: DownloadPictureAction.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 21-04-2005 10:12:26 AM ivan Exp $
 */
public class DownloadPictureAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DefaultForm defaultForm = (DefaultForm) form;

        //executes the command that read attach
        DownloadPictureCmd cmd = new DownloadPictureCmd();
        cmd.putParam("attachId", defaultForm.getDto("attachId"));
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
        InputStream input = (InputStream) resultDTO.get("image");
        String fileName = (String) resultDTO.get("fileName");
        Integer fileSize = (Integer) resultDTO.get("fileSize");

        if (null == input) {
            return null;
        }

        ServletOutputStream out = response.getOutputStream();

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);
        try {
            MimeTypeUtil.copy(out, input);
        } catch (IOException e) {
            log.debug("-> Write Image in " + ServletOutputStream.class.getName() + " Object FAIL ", e);
        } finally {
            input.close();
            out.flush();
        }

        return null;
    }
}
