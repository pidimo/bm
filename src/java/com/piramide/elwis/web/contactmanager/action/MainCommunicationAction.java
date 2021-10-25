package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.EmployeeUtilCmd;
import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.catalogmanager.util.WebDocumentExecuteUtil;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil;
import com.piramide.elwis.web.webmail.delegate.EmailServiceDelegate;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class MainCommunicationAction extends DefaultAction {
    public Log log = LogFactory.getLog(MainCommunicationAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return findCancelForward(request, mapping);
        }

        ActionForward mainReferences = checkMainReferences(request, mapping);
        if (null != mainReferences) {
            return mainReferences;
        }

        User user = RequestUtils.getUser(request);

        DefaultForm communicationForm = (DefaultForm) form;
        communicationForm.setDto("companyId", user.getValue("companyId"));

        String communicationType = (String) communicationForm.getDto("type");
        log.debug("-> CommunicationType :" + communicationType);

        ActionForward forward = null;
        boolean isActionProcessed = false;
        if (CommunicationTypes.OTHER.equals(communicationType)) {
            isActionProcessed = true;
            forward = processOtherCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.MEETING.equals(communicationType)) {
            isActionProcessed = true;
            forward = processMeetingCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.FAX.equals(communicationType)) {
            isActionProcessed = true;
            forward = processFaxCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.LETTER.equals(communicationType)) {
            isActionProcessed = true;
            forward = processLetterCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.PHONE.equals(communicationType)) {
            isActionProcessed = true;
            forward = processPhoneCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.EMAIL.equals(communicationType)) {
            isActionProcessed = true;
            forward = processEmailCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.DOCUMENT.equals(communicationType)) {
            isActionProcessed = true;
            forward = processDocumentCommunication(communicationForm, request, response, mapping);
        }
        if (CommunicationTypes.WEB_DOCUMENT.equals(communicationType)) {
            isActionProcessed = true;
            forward = processWebDocumentCommunication(communicationForm, request, response, mapping);
        }

        if (!isActionProcessed) {
            forward = super.execute(mapping, form, request, response);
        }

        return findSuccessRedirectForward(forward, request, mapping);
    }

    protected ActionForward findSuccessRedirectForward(ActionForward forward, HttpServletRequest request, ActionMapping mapping) {
        return forward;
    }

    protected ActionForward findCancelForward(HttpServletRequest request, ActionMapping mapping) {
        return mapping.findForward("Cancel");
    }

    protected ActionForward checkMainReferences(HttpServletRequest request, ActionMapping mapping) {
        return mapping.findForward("MainSearch");
    }


    protected ActionForward processDocumentCommunication(DefaultForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         ActionMapping mapping) throws Exception {
        log.debug("-> Process Document communication OK");
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward processOtherCommunication(DefaultForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      ActionMapping mapping) throws Exception {
        log.debug("-> Process Other communication OK");
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward processLetterCommunication(DefaultForm form,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       ActionMapping mapping) throws Exception {
        log.debug("-> Process Letter communication OK");
        return processFaxAndLetterCommunication(form, request, response, mapping);
    }

    protected ActionForward processFaxCommunication(DefaultForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    ActionMapping mapping) throws Exception {
        log.debug("-> Process Fax communication OK");
        return processFaxAndLetterCommunication(form, request, response, mapping);
    }

    protected ActionForward processPhoneCommunication(DefaultForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      ActionMapping mapping) throws Exception {
        log.debug("-> Process Phone communication OK");
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward processMeetingCommunication(DefaultForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        ActionMapping mapping) throws Exception {
        log.debug("-> Process Meeting communication OK");
        return super.execute(mapping, form, request, response);
    }

    protected ActionForward processEmailCommunication(DefaultForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      ActionMapping mapping) throws Exception {
        log.debug("-> Process Email communication OK");
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userAddressId = (Integer) user.getValue("userAddressId");
        form.setDto("elwisUserId", userId);

        MailFormHelper emailHelper = new MailFormHelper();
        String op = (String) form.getDto("op");

        String changeTelecomType = (String) form.getDto("changeCommunicationType");
        if ("create".equals(op) || "true".equals(changeTelecomType)) {
            Integer emailUserChecker = emailHelper.isValidEmailUser(userId, request);
            if (-1 == emailUserChecker) {
                return mapping.findForward("NoUserMail");
            }
        }

        ActionForward forward = super.execute(mapping, form, request, response);
        if (!"create".equals(op)) {
            String replyOperation = request.getParameter("replyOperation");
            if ("REPLY".equals(replyOperation) ||
                    "REPLYALL".equals(replyOperation) ||
                    "FORWARD".equals(replyOperation)) {
                MailFormHelper mailFormHelper = new MailFormHelper();
                mailFormHelper.processEmail(request, response, form);
                form.setDto("redirect", true);
            }

            emailHelper.setDefaultEmailAccount(userId, companyId, request, form);
            form.setDto("dateStart", DateUtils.dateToInteger(new Date()));

            if (isEmployee(userAddressId, request) && null == form.getDto("employeeId")) {
                form.setDto("employeeId", userAddressId);
            }

            if (null != form.getDto("mailAccountId")) {
                String actualEmailAccount = form.getDto("mailAccountId").toString();
                emailHelper.setDefaultSignature(Integer.valueOf(actualEmailAccount), userId, form, request);
            }
        }

        if ("create".equals(op)) {
            hasCreatedCommunications(form, request);
        }

        deliveryEmail(userId, forward, op, request);

        return forward;
    }

    protected void hasCreatedCommunications(DefaultForm form, HttpServletRequest request) {
        List<Integer> communicationIdentifiers = (List<Integer>) form.getDto("communicationIdentifiers");
        if (null != communicationIdentifiers && communicationIdentifiers.isEmpty()) {
            ActionErrors errors = new ActionErrors();
            errors.add("NoCommunicationsCreated", new ActionError("Communication.msg.NoCommunicationsCreated"));
            saveErrors(request.getSession(), errors);
        }
    }

    protected void deliveryEmail(Integer userMailId,
                                 ActionForward forward,
                                 String op,
                                 HttpServletRequest request) {
        if (!"create".equals(op)) {
            return;
        }

        if (!"Success".equals(forward.getName())) {
            return;
        }

        EmailServiceDelegate.i.sentEmails(userMailId);
    }

    private ActionForward processFaxAndLetterCommunication(DefaultForm form,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           ActionMapping mapping) throws Exception {
        log.debug("-> Process Fax or Letter Communication ");

        boolean isGenerateButtonPressed = CommunicationFieldValidatorUtil.
                isButtonPressed(CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey(), request);

        if (isGenerateButtonPressed) {

            boolean isCampaignGenCommunication = Functions.isCampaignGenerationCommunication((String) form.getDto("contactId"));
            if (isCampaignGenCommunication) {
                return generateCampaignGenerationCommunication(form, request, response, mapping);
            }

            String rebuildDocument = (String) form.getDto("rebuildDocument");

            if (com.piramide.elwis.web.contactmanager.el.Functions.communicationHasDocument((String) form.getDto("contactId"))
                    && rebuildDocument != null && "".equals(rebuildDocument)) {

                ActionErrors errors = new ActionErrors();
                errors.add("msgChange", new ActionError("Document.msg.documentRebuild", JSPHelper.getMessage(request, "Document.generate")));
                saveErrors(request, errors);
                request.setAttribute("rebuild", "true");
                form.setDto("rebuildDocument", "true"); //flag to rebuild the document
                return new ActionForward(mapping.getInput());
            }

            if ("true".equals(rebuildDocument)) {
                form.setDto("rebuild", "true");
            }

            User user = RequestUtils.getUser(request);
            form.setDto(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID).toString());
            EJBCommand documentCmd = getGenerateDocumentCmd();
            documentCmd.putParam(form.getDtoMap());
            documentCmd.putParam("companyId", user.getValue(Constants.COMPANYID).toString());
            documentCmd.putParam("locale", request.getLocale().toString());
            documentCmd.putParam("generate", "true");

            ResultDTO resultDTO = BusinessDelegate.i.execute(documentCmd, request);
            if (resultDTO.isFailure() || resultDTO.hasResultMessage()) {
                log.debug("-> Generate Document FAIL ");
                saveErrors(request, MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO));
                return new ActionForward(
                        resultDTO.containsKey("actionForward") ?
                                (String) resultDTO.get("actionForward") :
                                mapping.getInput());
            }
            log.debug("-> Execute " + documentCmd.getClass().getName() + " OK, resultDTO:" + resultDTO);
            form.getDtoMap().putAll(resultDTO);
            StringBuffer sb = new StringBuffer();
            StringBuffer param = new StringBuffer(request.getContextPath());
            param.append("/contacts/Download.do");
            param.append("?dto(type)=comm");
            param.append("&dto(fid)=").append(resultDTO.get("fid"));
            sb.append("onLoad=\"location.href ='").append(response.encodeRedirectURL(param.toString())).append("'\"");

            if (resultDTO.containsKey("update")) {
                request.getSession().setAttribute("UPDATE_FIELDS", resultDTO.getAsString("update"));
            }

            request.getSession().setAttribute("jsLoad", new String(sb));
            return mapping.findForward("Success");
        }

        return super.execute(mapping, form, request, response);
    }

    private ActionForward processWebDocumentCommunication(DefaultForm form,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           ActionMapping mapping) throws Exception {
        log.debug("-> Process Web document Communication... " + form.getDtoMap());

        boolean isGenerateButtonPressed = CommunicationFieldValidatorUtil.isButtonPressed(CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey(), request);

        if (isGenerateButtonPressed) {

            String rebuildDocument = (String) form.getDto("rebuildDocument");

            if (com.piramide.elwis.web.contactmanager.el.Functions.communicationHasDocument((String) form.getDto("contactId"))
                    && rebuildDocument != null && "".equals(rebuildDocument)) {

                ActionErrors errors = new ActionErrors();
                errors.add("msgChange", new ActionError("Document.msg.webDocumentRebuild", JSPHelper.getMessage(request, "Document.generate")));
                saveErrors(request, errors);
                request.setAttribute("rebuild", "true");
                form.setDto("rebuildDocument", "true"); //flag to rebuild the document
                return new ActionForward(mapping.getInput());
            }

            //set default values to generate web document
            form.setDto("webGenerateUUID", UUID.randomUUID().toString());
            form.setDto("isWebDocumentGenerate", "true");

            ActionForward forward = super.execute(mapping, form, request, response);

            log.debug("After of cmd execute...................." + request.getAttribute("dto"));
            if ("Success".equals(forward.getName())) {

                Map resultMap = (Map) request.getAttribute("dto");

                Integer contactId = null;
                if (resultMap.get("contactId") != null) {
                    contactId = new Integer(resultMap.get("contactId").toString());
                }

                if (contactId != null) {
                    WebDocumentExecuteUtil documentExecuteUtil = new WebDocumentExecuteUtil();
                    ActionErrors errors = documentExecuteUtil.generateDocumentUrl(contactId, mapping, request);

                    if (errors.isEmpty()) {
                        String js = "onLoad=\"window.open(\'" + documentExecuteUtil.getUrl() + "\');\"";
                        //because action is redirect
                        request.getSession().setAttribute("jsLoad", js);
                    } else {
                        saveErrors(request.getSession(), errors);
                    }
                }
            }

            return forward;
        }

        return super.execute(mapping, form, request, response);
    }

    private boolean isEmployee(Integer userAddressId, HttpServletRequest request) {
        EmployeeUtilCmd employeeUtilCmd = new EmployeeUtilCmd();
        employeeUtilCmd.setOp("isEmployee");
        employeeUtilCmd.putParam("userAddressId", userAddressId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(employeeUtilCmd, request);
            return (Boolean) resultDTO.get("isEmployee");
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmployeeUtilCmd.class.getName() + " FAIL");
            return false;
        }
    }

    protected EJBCommand getGenerateDocumentCmd() {
        return new GenerateDocumentCmd();
    }

    private ActionForward generateCampaignGenerationCommunication(DefaultForm form,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response,
                                                                  ActionMapping mapping) throws Exception {

        StringBuffer sb = new StringBuffer();
        StringBuffer param = new StringBuffer(request.getContextPath());
        param.append("/campaign/Download/GenerationCommunication/Document.do");
        param.append("?dto(contactId)=").append(form.getDto("contactId"));
        sb.append("onLoad=\"location.href ='").append(response.encodeRedirectURL(param.toString())).append("'\"");

        request.getSession().setAttribute("jsLoad", new String(sb));
        return mapping.findForward("Success");
    }

}
