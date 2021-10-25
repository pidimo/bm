package com.piramide.elwis.web.bmapp.action.webmail;

import com.piramide.elwis.cmd.webmailmanager.ReadEmailCmd;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.7
 */
public class ReplySendRESTAction extends ComposeEmailRESTAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ReplySendRESTAction..." + request.getParameterMap());

        //process html
        DefaultForm defaultForm = (DefaultForm) form;
        if (WebMailConstants.BODY_TYPE_HTML.equals(defaultForm.getDto("bodyType"))) {
            String replyBody = processReplyHtmlBody(defaultForm, request, response);
            defaultForm.setDto("body", replyBody);
        }

        return super.execute(mapping, defaultForm, request, response);
    }

    private String processReplyHtmlBody(DefaultForm defaultForm, HttpServletRequest request, HttpServletResponse response) {
        String replyBody = null;

        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        ReadEmailCmd readEmailCmd = new ReadEmailCmd();
        readEmailCmd.putParam("mailId", defaultForm.getDto("mailId"));
        readEmailCmd.putParam("userMailId", sessionUserId);

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(readEmailCmd, null);
        } catch (AppLevelException e) {
            log.debug("Error executing ReadEmailCmd cmd", e);
        }

        if (resultDTO != null) {
            if (resultDTO.get("bodyType") != null && WebMailConstants.BODY_TYPE_HTML.equals(resultDTO.get("bodyType").toString())) {

                DefaultForm tempDefaultForm = new DefaultForm();
                tempDefaultForm.getDtoMap().putAll(resultDTO);

                MailFormHelper mailFormHelper = new MailFormHelper();
                String mailBody = mailFormHelper.processHtmlBody(tempDefaultForm, request, response);

                replyBody = getFormattedReplyMessage(defaultForm) + mailBody;

                //rewrite img attach
                reWriteImageAttachments(resultDTO, defaultForm);
            }
        }

        return replyBody;
    }

    private String getFormattedReplyMessage(DefaultForm defaultForm) {
        String message = "";
        if (defaultForm.getDto("body") != null) {
            message = "<p>" + defaultForm.getDto("body").toString() + "</p>";
        }
        return message;
    }

    private void reWriteImageAttachments(ResultDTO resultDTO, DefaultForm defaultForm) {

        if (resultDTO.get("attachments") != null) {
            List<AttachDTO> attachDTOList = (List<AttachDTO>) resultDTO.get("attachments");
            for (AttachDTO attachDTO : attachDTOList) {

                //only rewrite image as attach
                Boolean visible = (Boolean) attachDTO.get("visible");
                if (visible != null && visible) {
                    String attachId = attachDTO.get("attachId").toString();

                    defaultForm.setDto(AttachFormHelper.ATTACHID_KEY + attachId, attachId);
                    defaultForm.setDto(AttachFormHelper.ATTACHFILE_KEY + attachId, attachDTO.get("attachFile"));
                    defaultForm.setDto(AttachFormHelper.VISIBLE_KEY + attachId, visible.toString());
                }
            }
        }
    }
}
