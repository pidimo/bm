package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.DownloadEmailCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class DownloadEmailAction extends DefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        String mailId = (String) defaultForm.getDto("mailId");

        DownloadEmailCmd downloadEmailCmd = new DownloadEmailCmd();
        downloadEmailCmd.putParam("mailId", mailId);
        ResultDTO resultDTO = BusinessDelegate.i.execute(downloadEmailCmd, request);

        if ("Fail".equals(resultDTO.getForward())) {
            ActionErrors errors = new ActionErrors();
            errors.add("notFound", new ActionError("Webmail.email.notFound"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        ArrayByteWrapper data = (ArrayByteWrapper) resultDTO.get("data");
        String fileName = (String) resultDTO.get("fileName");
        Integer fileSize = (Integer) resultDTO.get("fileSize");

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream outStream = response.getOutputStream();

        outStream.write(data.getFileData());

        outStream.flush();
        outStream.close();

        return null;
    }
}
