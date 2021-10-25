package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.cmd.supportmanager.DownloadSupportAttachCmd;
import com.piramide.elwis.dto.supportmanager.SupportAttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id DownloadSupportAttachAction ${time}
 */
public class DownloadSupportAttachAction extends SupportCaseBaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DefaultForm myForm = (DefaultForm) form;
        Integer attachId = Integer.valueOf(myForm.getDto("attachId").toString());
        String supportAttachName = myForm.getDto("supportAttachName").toString();

        ResultDTO myResultDTO;
        DownloadSupportAttachCmd cmd = new DownloadSupportAttachCmd();
        cmd.putParam("attachId", attachId);
        cmd.putParam("supportAttachName", supportAttachName);


        myResultDTO = BusinessDelegate.i.execute(cmd, request);
        if (!((Boolean) myResultDTO.get("Failure")).booleanValue()) {

            ArrayByteWrapper wrapper = (ArrayByteWrapper) myResultDTO.get("fileDownload");
            String fileName = myResultDTO.get("fileName").toString();
            int fileSize = (Integer) myResultDTO.get("fileSize");

            MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(wrapper.getFileData());

            outStream.flush();
            outStream.close();
        } else {
            SupportAttachDTO dto = new SupportAttachDTO();
            dto.put("supportAttachName", supportAttachName);
            dto.addNotFoundMsgTo(myResultDTO);
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, myResultDTO);
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }
        return null;
    }
}
