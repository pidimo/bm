package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignDocumentGenerateCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignDocumentGenerateAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignDocumentGenerateAction extends Action {

    private Log log = LogFactory.getLog(this.getClass());


    private ActionForward validCampaign(ActionMapping mapping, HttpServletRequest request) {

        log.debug("Validating generation process for current campaign...");
        //user in the session

        //cheking if working product was not deleted by other user.
        log.debug("campaignId for user session  = " + request.getParameter("campaignId"));
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        //activity concurrency validation
        if (null != request.getParameter("activityId")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "activityid",
                    request.getParameter("activityId"), errors, new ActionError("Campaign.activity.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("Fail_");
            }
        } else {
            errors.add("activityid", new ActionError("Campaign.activity.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("Fail_");
        }
        return null;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Campaign Generate Action...." + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;
        log.debug("FORM:" + defaultForm.getDtoMap());

        boolean isEmail = defaultForm.getDto("email") != null;
        //validation
        ActionForward forward = validCampaign(mapping, request);
        if (forward != null) {
            return forward;
        }

        CampaignDocumentGenerateCmd.stop.put(request.getSession().getId(), Boolean.FALSE);
        CampaignDocumentGenerateCmd cmd = new CampaignDocumentGenerateCmd();
        cmd.putParam(((DefaultForm) form).getDtoMap());
        User user = (User) request.getSession().getAttribute("user");
        log.debug("User session..");
        log.debug("param DTO from form.." + cmd.getParamDTO());
        log.debug("EMAIL:" + defaultForm.getDto("email"));
        cmd.putParam(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID));
        cmd.putParam("session", request.getSession().getId());
        cmd.putParam("email", isEmail ? "true" : "false");
        cmd.putParam("userId", user.getValue("userId"));
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam("templateId", defaultForm.getDto("templateId"));
        cmd.putParam("campaignId", request.getParameter("campaignId"));
        cmd.putParam("languageId", defaultForm.getDto("language"));
        cmd.putParam("languageName", defaultForm.getDto("languageName"));
        log.debug("AttachsIDS:" + defaultForm.getDto("listIdAttach"));
        cmd.putParam("listIdAttach", defaultForm.getDto("listIdAttach"));
        cmd.putParam("page", defaultForm.getDto("page"));
        cmd.putParam("locale", request.getLocale().toString());
        cmd.putParam("subject", defaultForm.getDto("subject"));
        cmd.putParam("responsibleType", defaultForm.getDto("responsibleType"));
        cmd.putParam("activityId", request.getParameter("activityId"));

////nuevo en la intefaz YUMI
        if (defaultForm.getDto("to") != null) {
            cmd.putParam("telecomTypeId", defaultForm.getDto("to"));
        }
        cmd.putParam("from", defaultForm.getDto("mail"));
        log.debug("ParamDTO:" + cmd.getParamDTO());
        cmd.executeInStateless(null);

        ResultDTO resultDTO = cmd.getResultDTO();
        if (resultDTO.isFailure()) {
            log.debug("Fail Download Document");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);
            request.setAttribute("hasErrors", "true");
            if (resultDTO.containsKey("NoConnectException")) {
                return new ActionForwardParameters().add("dto(campaignId)", request.getParameter("campaignId")).forward(mapping.findForward("NotConfiguration"));
            }
            return mapping.findForward("Fail");
        }


        if (!isEmail) {
            ArrayByteWrapper wordDoc = cmd.getWordDoc();

            log.debug("Extract document");
            if (wordDoc != null) {
                byte[] doc = wordDoc.getFileData();

                log.debug("Sending document");
                String fileName = "Letter_.doc";
                int fileSize = doc.length;
                MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);
                ServletOutputStream os = response.getOutputStream();
                os.write(doc);
                os.flush();
                os.close();
                return null;
            }

        }

        String js = "onLoad=\"window.open('" + response.encodeRedirectURL(request.getContextPath() + "/campaign/Campaign/SuccessEmails.do") + "','', 'width=420,height=180,top=200,left=350');\"";
        request.setAttribute("jsLoad", js);

        return new ActionForwardParameters().add("dto(campaignId)", request.getParameter("campaignId")).forward(mapping.findForward("GoBack"));
    }
}
