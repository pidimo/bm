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
 * Action execute cmd, and put in response JSON data, this is used from ajax request
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class ComposeEmailAutoSaveAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ComposeEmailAutoSaveAction................" + request.getParameterMap());

        //add op to execute cmd
        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("op", "autosaveEmail");

        super.execute(mapping, defaultForm, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");

        if (dtoValues.get("tempMailId") != null) {
            processSuccessResponse(response, dtoValues);
        } else {
            processErrorResponse(response, "Unexpected error");
        }

        return null;
    }

    private void processSuccessResponse(HttpServletResponse response, Map dtoValues) {
        Integer mailId = null;
        if (dtoValues.get("tempMailId") != null) {
            mailId = (Integer) dtoValues.get("tempMailId");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", (mailId != null) ? "Success" : "Fail");
        jsonObject.put("tempMailId", mailId);

        setDataInResponse(response, jsonObject.toJSONString());
    }

    private void processErrorResponse(HttpServletResponse response, String errorCause) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", "Fail");
        jsonObject.put("message", errorCause);

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