package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.MailUtilCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Action to move mail to folder via ajax
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class MailMoveToFolderAjaxAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailMoveToFolderAjaxAction................" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("op", "deleteTempAttach");

        Object mailId = defaultForm.getDto("mailId");
        Object folderId = defaultForm.getDto("folderId");

        ResultDTO resultDTO = new ResultDTO();
        if (mailId != null && !GenericValidator.isBlankOrNull(mailId.toString())
                && folderId != null && !GenericValidator.isBlankOrNull(folderId.toString())) {

            MailUtilCmd mailUtilCmd = new MailUtilCmd();
            mailUtilCmd.setOp("moveToFolder");
            mailUtilCmd.putParam("mailId", mailId);
            mailUtilCmd.putParam("folderId",folderId);

            try {
                resultDTO = BusinessDelegate.i.execute(mailUtilCmd, request);
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
        }

        processExecuteResponse(response, resultDTO);

        return null;
    }

    private void processExecuteResponse(HttpServletResponse response, Map resultDtoValues) {
        Boolean moveSuccess = (Boolean) resultDtoValues.get("isSuccessMailMove");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", (moveSuccess != null && moveSuccess) ? "Success" : "Fail");

        setDataInResponse(response, jsonObject.toJSONString());
    }

    private void setDataInResponse(HttpServletResponse response, String jsonData) {
        log.debug("Response Value:\n" + jsonData);

        response.setContentType("application/json");
        try {
            PrintWriter write = response.getWriter();
            write.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}