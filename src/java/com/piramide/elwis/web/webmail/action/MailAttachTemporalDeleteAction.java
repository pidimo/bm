package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
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
 * Action to delete an temporal attach, and set JSON as response
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class MailAttachTemporalDeleteAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailAttachTemporalDeleteAction................" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("op", "deleteTempAttach");

        super.execute(mapping, defaultForm, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");

        processExecuteResponse(response, dtoValues);

        return null;
    }

    private void processExecuteResponse(HttpServletResponse response, Map resultDtoValues) {
        Boolean removeSuccess = (Boolean) resultDtoValues.get("tempAttachIsRemoved");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", (removeSuccess != null && removeSuccess) ? "Success" : "Fail");

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