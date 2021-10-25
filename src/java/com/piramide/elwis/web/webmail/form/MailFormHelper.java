package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.EmailUserCmd;
import com.piramide.elwis.cmd.webmailmanager.MailAccountCmd;
import com.piramide.elwis.cmd.webmailmanager.SignatureCmd;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.dto.webmailmanager.MailAccountDTO;
import com.piramide.elwis.dto.webmailmanager.MailRecipientDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class MailFormHelper {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isWebmailUser(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("isWebmailUser");
        emailUserCmd.putParam("userId", user.getValue(Constants.USERID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(emailUserCmd, request);
            return (Boolean) resultDTO.get("isWebmailUser");
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmailUserCmd.class.getName() + " FAIL ", e);
        }

        return false;
    }

    public void processEmail(HttpServletRequest request, HttpServletResponse response, DefaultForm defaultForm) {
        User user = RequestUtils.getUser(request);
        MailFormHelper helper = new MailFormHelper();
        helper.readUserConfiguration((Integer) user.getValue(Constants.USERID), request, defaultForm);

        helper.setMailAccountDefaultSetting(defaultForm.getDto("mailAccountId"), request, defaultForm);

        String bodyType = String.valueOf(defaultForm.getDto("bodyType"));
        Boolean useHtmlEditor = (Boolean) defaultForm.getDto("useHtmlEditor");
        Boolean replyMode = (Boolean) defaultForm.getDto("replyMode");

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        List<Map> recipients = (List<Map>) defaultForm.getDto("recipientsWithContact");
        List<Integer> addressIdList = (List<Integer>) defaultForm.getDto("contactAddressIds");
        defaultForm.setDto("dynamicHiddens", emailRecipientHelper.getDynamicHiddens(recipients));
        defaultForm.setDto("addressList", emailRecipientHelper.buildIdList(addressIdList));


        if (WebMailConstants.BODY_TYPE_TEXT.equals(bodyType)) {
            defaultForm.setDto("body", processTextBody(replyMode, useHtmlEditor, defaultForm, request));
        }

        if (WebMailConstants.BODY_TYPE_HTML.equals(bodyType)) {
            defaultForm.setDto("body", processHtmlBody(defaultForm, request, response));
        }

        processRecipients(request, defaultForm);
    }

    private void processRecipients(HttpServletRequest request, DefaultForm defaultForm) {
        String replyMessage = JSPHelper.getMessage(request, "Webmail.compose.replyShort");
        String forwardMessage = JSPHelper.getMessage(request, "Webmail.compose.forwardShort");

        String subject = (String) defaultForm.getDto("mailSubject");
        String from = (String) defaultForm.getDto("from");

        String replyOperation = request.getParameter("replyOperation");
        if ("FORWARD".equals(replyOperation)) {
            defaultForm.setDto("mailState", MailStateUtil.addForwardState());
            defaultForm.setDto("mailSubject", forwardMessage + ": " + subject);
            defaultForm.getDtoMap().remove("to");
            defaultForm.getDtoMap().remove("cc");
            defaultForm.getDtoMap().remove("dynamicHiddens");
            defaultForm.getDtoMap().remove("addressList");
            return;
        }

        if ("REPLY".equals(replyOperation)) {
            defaultForm.setDto("mailState", MailStateUtil.addAnsweredState());
            defaultForm.setDto("mailSubject", replyMessage + ": " + subject);
            defaultForm.setDto("to", from);
            defaultForm.getDtoMap().remove("cc");
            disableAttachmentCheckBoxes(defaultForm);
            return;
        }

        if ("REPLYALL".equals(replyOperation)) {
            String toWithOutAccount = (String) defaultForm.getDto("toWithOutAccount");
            String mailFrom = (String) defaultForm.getDto("mailFrom");
            if (!"".equals(toWithOutAccount)) {
                if (null != defaultForm.getDto("mailAccount") &&
                        !from.contains(defaultForm.getDto("mailAccount").toString()) &&
                        !toWithOutAccount.contains(mailFrom)) {
                    toWithOutAccount = toWithOutAccount + ", " + from;
                }
            } else {
                toWithOutAccount = from;
            }

            String ccWithOutAccount = (String) defaultForm.getDto("ccWithOutAccount");
            defaultForm.setDto("cc", ccWithOutAccount);

            defaultForm.setDto("to", toWithOutAccount);
            defaultForm.setDto("mailState", MailStateUtil.addAnsweredState());
            defaultForm.setDto("mailSubject", replyMessage + ": " + subject);
            disableAttachmentCheckBoxes(defaultForm);
        }
    }

    private void disableAttachmentCheckBoxes(DefaultForm defaultForm) {
        List<AttachDTO> attachments = (List<AttachDTO>) defaultForm.getDto("attachments");
        if (null != attachments) {
            for (AttachDTO dto : attachments) {
                Integer attachId = (Integer) dto.get("attachId");
                Boolean attachChecked = (Boolean) defaultForm.getDto(attachId.toString());
                if (null != attachChecked && attachChecked) {
                    defaultForm.setDto(attachId.toString(), false);
                }
            }
        }
    }

    public List<MailRecipientDTO> getInvalidRecipients(DefaultForm defaultForm) {
        List<MailRecipientDTO> invalidRecipients =
                (List<MailRecipientDTO>) defaultForm.getDto("invalidRecipients");
        if (null != invalidRecipients && !invalidRecipients.isEmpty()) {
            return invalidRecipients;
        }

        return null;
    }

    public String processHtmlBody(DefaultForm defaultForm,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        String body = processBodyImages(defaultForm, request, response);

        String header = getRedirectHeader(request, defaultForm, true);
        return getInitialBodyEmptyHtml() + header + applyBlockQuote(body);
    }

    private String getInitialBodyEmptyHtml() {
        return "<br /><br />";
    }

    private String processBodyImages(DefaultForm defaultForm,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        String body = (String) defaultForm.getDto("body");
        List<AttachDTO> attachments = (List<AttachDTO>) defaultForm.getDto("attachments");

        AttachFormHelper attachFormHelper = new AttachFormHelper();
        body = attachFormHelper.updateBody(body, attachments, request, response);

        return body;
    }

    public void updateHtmlBodyForDraftEmail(DefaultForm defaultForm,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        String body = processBodyImages(defaultForm, request, response);
        defaultForm.setDto("body", body);
    }

    private String processTextBody(Boolean replyMode,
                                   Boolean useHtmlEditor,
                                   DefaultForm defaultForm,
                                   HttpServletRequest request) {
        String body = (String) defaultForm.getDto("body");

        if (!replyMode && useHtmlEditor) {
            body = replaceInBody(body, "<br/>");
            String header = getRedirectHeader(request, defaultForm, true);
            return getInitialBodyEmptyHtml() + header + applyBlockQuote(body);
        }

        String header = getRedirectHeader(request, defaultForm, false);
        body = replaceInBody(body, "\n>");
        return "\n\n" + header + body;
    }

    private String replaceInBody(String body, String cad) {
        Pattern pattern = Pattern.compile("(\\n\\r|\\n)");
        Matcher matcher = pattern.matcher(body);
        return cad + matcher.replaceAll(cad);
    }

    private String applyBlockQuote(String body) {
        return "<blockquote style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #003366 2px solid\">" +
                body +
                "</blockquote>";
    }

    private String getRedirectHeader(HttpServletRequest request, DefaultForm defaultForm, Boolean htmlHeader) {
        String from = (String) defaultForm.getDto("from");
        String to = (String) defaultForm.getDto("to");
        String cc = (String) defaultForm.getDto("cc");
        String subject = (String) defaultForm.getDto("mailSubject");
        String sentDate = String.valueOf(defaultForm.getDto("sentDate"));

        if (htmlHeader) {
            from = from.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            to = to.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            cc = cc.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }

        return JSPHelper.createRedirectHeader(request,
                from,
                to,
                cc,
                subject,
                sentDate,
                htmlHeader);
    }

    public void setDefaultSignature(Integer emailAccountId,
                                    Integer emailUserId,
                                    DefaultForm form,
                                    HttpServletRequest request) {
        SignatureCmd signatureCmd = new SignatureCmd();
        signatureCmd.setOp("getDefaultSignature");
        signatureCmd.putParam("mailAccountId", emailAccountId);
        signatureCmd.putParam("userMailId", emailUserId);
        try {
            ResultDTO signatureResult = BusinessDelegate.i.execute(signatureCmd, request);
            Integer signature = (Integer) signatureResult.get("signatureId");
            form.setDto("signatureId", signature);
            log.debug("-> SettingUp default signatureId=" + signature + " OK");
        } catch (AppLevelException e) {
            log.error("-> Execute " + SignatureCmd.class.getName() + " FAIL", e);
            form.setDto("signature", "");
        }
    }


    /**
     * This method check if the user has enabled automatic download configurated in
     * webmail settings.
     *
     * @param request HttpServletRequest Object to read session user information.
     * @return true if the user have enabled the automatic download emails in webmail configuration false
     *         in other case.
     */
    public Boolean isAutomaticDownloadConfigurated(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue(Constants.USERID);

        UserMailDTO userMailDTO = readUserConfiguration(userId, request);
        if (null == userMailDTO) {
            return false;
        }


        Boolean backgroundDownload = (Boolean) userMailDTO.get("backgroundDownload");
        return null != backgroundDownload && backgroundDownload;
    }


    /**
     * Executes 'isValidEmailUser' operation  in EmailUserCmd and return an Integer value acording to
     * the reply  the command.
     * <p/>
     * the posible values returned by the method are:
     * A)  1 : That is the emailUserId belongs to a valid webmail user, and him has accounts configurated.
     * B)  0 : That is the emailUserId belongs to a valid webmail user, however him has not accounts configurated.
     * C) -1 : That is the emailUserId not belonging to any valid webmail user.
     *
     * @param emailUserId Webmail user identifier
     * @param request     HttpServletRequest Object to execute the command.
     * @return Integer value that represents the webmail user state.
     */
    public Integer isValidEmailUser(Integer emailUserId, HttpServletRequest request) {
        Integer result = -1;

        EmailUserCmd cmd = new EmailUserCmd();
        cmd.setOp("isValidEmailUser");
        cmd.putParam("emailUserId", emailUserId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            Boolean isValidEmailUser = (Boolean) resultDTO.get("isValidEmailUser");
            if (null != isValidEmailUser && isValidEmailUser) {
                result = 0;

                Boolean hasEmailAccounts = (Boolean) resultDTO.get("hasEmailAccounts");
                if (null != hasEmailAccounts && hasEmailAccounts) {
                    result = 1;
                }
            }
        } catch (AppLevelException e) {
            log.debug("-> Execute " + EmailUserCmd.class.getName() + " FAIL", e);
        }

        return result;
    }

    public boolean useHtmlEditor(Integer userId, HttpServletRequest request) {
        Boolean result = false;
        EmailUserCmd cmd = new EmailUserCmd();
        cmd.setOp("useHtmlEditor");
        cmd.putParam("emailUserId", userId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            result = (Boolean) resultDTO.get("useHtmlEditor");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + EmailUserCmd.class.getName() + " FAIL", e);
        }
        return result;
    }

    public void setMailAccountDefaultSetting(Object mailAccountIdObj, HttpServletRequest request, DefaultForm form) {
        log.debug("read and set default settings for mailAccount:" + mailAccountIdObj);

        Integer mailAccountId = null;
        try {
            mailAccountId = Integer.valueOf(mailAccountIdObj.toString());
        } catch (Exception e) {
            log.error("Number format error in mailAccountId:" + mailAccountIdObj);
        }

        if (mailAccountId != null) {
            MailAccountCmd cmd = new MailAccountCmd();
            cmd.setOp("read");
            cmd.putParam("mailAccountId", mailAccountId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                Boolean createOutCommunication = (Boolean) resultDTO.get("createOutCommunication");

                if (null == form.getDto("createOutCommunication")) {
                    form.setDto("createOutCommunication", createOutCommunication);
                    if (createOutCommunication) {
                        form.setDto("saveSendItem", true);
                    }
                }

            } catch (AppLevelException e) {
                log.error("-> Execute " + MailAccountCmd.class.getName() + " FAIL");
            }
        }
    }


    public void readUserConfiguration(Integer userId,
                                      HttpServletRequest request,
                                      DefaultForm form) {
        EmailUserCmd cmd = new EmailUserCmd();
        cmd.setOp("readUserConfiguration");
        cmd.putParam("emailUserId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            Boolean saveInSentItems = (Boolean) resultDTO.get("saveSendItem");
            Boolean useHtmlEditor = (Boolean) resultDTO.get("useHtmlEditor");
            Boolean replyMode = (Boolean) resultDTO.get("replyMode");

            if (null == form.getDto("saveSendItem")) {
                form.setDto("saveSendItem", saveInSentItems);
            }

            form.setDto("replyMode", replyMode);

            form.setDto("editMode", useHtmlEditor);
            form.setDto("useHtmlEditor", useHtmlEditor);
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmailUserCmd.class.getName() + " FAIL");
        }
    }

    public UserMailDTO readUserConfiguration(Integer userId,
                                             HttpServletRequest request) {
        EmailUserCmd cmd = new EmailUserCmd();
        cmd.setOp("readUserConfiguration");
        cmd.putParam("emailUserId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            UserMailDTO userMailDTO = new UserMailDTO();
            userMailDTO.putAll(resultDTO);
            return userMailDTO;
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmailUserCmd.class.getName() + " FAIL");
        }

        return null;
    }

    /**
     * Set the default email account in <code>DefaultForm</code> object, if the <code>DefaultForm</code> contain
     * a <code>mailAccountId</code> object, the value validated with the user email accounts,
     * in other case the default account is setting in <code>DefaultForm</code>
     *
     * @param userId    email user identifier
     * @param companyId company identifier
     * @param request   <code>HttpServletRequest</code>   object used to execute <code>MailAccountCmd</code> command.
     * @param form      <code>DefaultForm</code> object to set the default account value
     */
    public void setDefaultEmailAccount(Integer userId,
                                       Integer companyId,
                                       HttpServletRequest request,
                                       DefaultForm form) {

        Object mailAccountId = form.getDto("mailAccountId");

        //find the default account associated to user
        MailAccountCmd accountCmd = new MailAccountCmd();
        accountCmd.setOp("getUserAccounts");
        accountCmd.putParam("emailUserId", userId);
        accountCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(accountCmd, request);

            if (null != mailAccountId) {
                List<MailAccountDTO> mailAccounts = (List<MailAccountDTO>) resultDTO.get("getUserAccounts");
                for (int i = 0; i < mailAccounts.size(); i++) {
                    MailAccountDTO dataBaseMailAccount = mailAccounts.get(i);
                    Integer dbMailAccountId = (Integer) dataBaseMailAccount.get("mailAccountId");
                    if (dbMailAccountId.toString().equals(mailAccountId.toString())) {
                        form.setDto("mailFrom", dbMailAccountId);
                        form.setDto("mailAccountId", dbMailAccountId);
                        return;
                    }
                }
            }

            MailAccountDTO dto = (MailAccountDTO) resultDTO.get("defaultEmailAccount");
            if (null != dto) {
                form.setDto("mailFrom", dto.get("mailAccountId"));
                form.setDto("mailAccountId", dto.get("mailAccountId"));
            } else {
                form.setDto("mailFrom", "");
                form.setDto("mailAccountId", null);
            }

        } catch (AppLevelException e) {
            log.debug("-> Cannot execute " + MailAccountCmd.class.getName() + " ", e);
        }
    }

    public Integer getUserMailId(Integer userId, HttpServletRequest request) {
        ResultDTO resultDTO = new ResultDTO();
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("hasWebmailModuleConfiguration");
        emailUserCmd.putParam("userId", userId);
        try {
            resultDTO = BusinessDelegate.i.execute(emailUserCmd, request);
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmailUserCmd.class.getName() + " FAIL");
        }
        return (Integer) resultDTO.get(UserMailDTO.KEY_USERMAILID);
    }
}
