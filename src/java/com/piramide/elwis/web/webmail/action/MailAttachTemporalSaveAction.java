package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.ComposeTemporalMailCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Action to save mail attachs as temporal, JSON data is send as response
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class MailAttachTemporalSaveAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailAttachTemporalSaveAction................" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        setDefaultDTOFields(defaultForm, request);

        ActionError actionError = validate(defaultForm, request);
        if (null != actionError) {
            processErrorResponse(request, response, actionError);
            return null;
        }

        ComposeTemporalMailCmd cmd = new ComposeTemporalMailCmd();
        cmd.setOp("saveTempAttach");
        cmd.putParam(defaultForm.getDtoMap());

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            processExecuteResponse(request, response, resultDTO);
        } catch (Exception e) {
            log.error("Error in execute save attach cmd..", e);
            processErrorResponse(request, response, new ActionError("Mail.saveAttach.unexpectedError"));
        }

        return null;
    }

    private void setDefaultDTOFields(DefaultForm defaultForm, HttpServletRequest request) {

        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        defaultForm.setDto("companyId", user.getValue("companyId"));
        defaultForm.setDto("userTimeZone", dateTimeZone);
    }

    private ActionError validate(DefaultForm defaultForm, HttpServletRequest request) {
        log.debug("Form dto values:" +defaultForm.getDtoMap());

        ActionError actionError = null;

        Object mailId = defaultForm.getDto("tempMailId");
        if (mailId == null || GenericValidator.isBlankOrNull(mailId.toString())) {
            actionError = new ActionError("Mail.saveAttach.unexpectedError");
        }

        if (actionError == null) {
            actionError = validateAttachAndSetInDTO(defaultForm, request);
        }

        return actionError;
    }


    private ActionError validateAttachAndSetInDTO(DefaultForm defaultForm, HttpServletRequest request) {
        ActionError actionError = null;

        AttachFormHelper attachFormHelper = new AttachFormHelper();
        actionError = attachFormHelper.validateFileInputAttach(defaultForm, request);

        log.debug("Attach to upload......" + attachFormHelper.getAttachments());

        if (actionError == null) {
            List<ArrayByteWrapper> wrapperList = attachFormHelper.buildNewAttachmentStructure();
            if (!wrapperList.isEmpty()) {
                //put the attach
                defaultForm.setDto("temporalAttachWrapper", wrapperList.get(0));
            }
        }

        return actionError;
    }

    private void processExecuteResponse(HttpServletRequest request, HttpServletResponse response, Map resultDtoValues) {

        if (resultDtoValues.get("tempAttachId") != null) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ajaxForward", "Success");
            jsonObject.put("tempAttachId", resultDtoValues.get("tempAttachId"));
            jsonObject.put("attachName", resultDtoValues.get("attachName"));
            jsonObject.put("attachSize", resultDtoValues.get("attachSize"));

            setDataInResponse(response, jsonObject.toJSONString());
        } else {
            processErrorResponse(request, response, new ActionError("Mail.saveAttach.unexpectedError"));
        }
    }

    private void processErrorResponse(HttpServletRequest request, HttpServletResponse response, ActionError actionError) {
        processErrorResponse(response, JSPHelper.getMessage(request, actionError.getKey(), actionError.getValues()));
    }

    private void processErrorResponse(HttpServletResponse response, String errorCause) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", errorCause);

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