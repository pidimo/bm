package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.EmailUserCmd;
import com.piramide.elwis.dto.webmailmanager.MailAccountDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class MailAccountForm extends WebmailDefaultForm {
    private Integer userMailId;
    private Integer companyId;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        companyId = (Integer) user.getValue(Constants.COMPANYID);
        userMailId = (Integer) user.getValue(Constants.USERID);

        ActionErrors errors = super.validate(mapping, request);
        ActionError popAccountError = validatePopAccount(request);
        if (null != popAccountError) {
            errors.add("popAccountError", popAccountError);
        }

        List<ActionError> smtpAccountErrors = validateSMTPAccount(request);
        if (!smtpAccountErrors.isEmpty()) {
            errors = addErrors(smtpAccountErrors, errors);
        }

        ActionError validateForwardEmail = validateForwardEmail(request);
        if (null != validateForwardEmail) {
            errors.add("validateForwardEmail", validateForwardEmail);
        }

        //setting up folderNames in dto for general purposses
        setDto("uiFolderNames", JSPHelper.getSystemFolderNames(request));
        return errors;

    }

    /**
     * Validates the Pop account configuration.
     * <p/>
     * - Verifies all required data for pop configuration.
     * - Check duplicated pop configurations by user.
     * - Check if the pop configuration is available in the company.
     * - Check connection with pop server.
     *
     * @param request <code>HttpServletRequest</code> object, used for general purposes.
     * @return <code>ActionError</code> if some validation fails or null if all validations passed successful.
     */
    private ActionError validatePopAccount(HttpServletRequest request) {
        String email = (String) getDto("email");
        String popUser = (String) getDto("login");
        String password = (String) getDto("password");
        String popServer = (String) getDto("serverName");
        String popPort = (String) getDto("serverPort");

        if (GenericValidator.isBlankOrNull(email)
                || GenericValidator.isBlankOrNull(popUser)
                || GenericValidator.isBlankOrNull(password)
                || GenericValidator.isBlankOrNull(popServer)
                || GenericValidator.isBlankOrNull(popPort)) {
            return null;
        }

        String operation = (String) getDto("op");

        setDto("userMailId", userMailId);

        //update mailAccountId field to integer type
        Integer mailAccountId = (null != getDto("mailAccountId") ?
                new Integer(getDto("mailAccountId").toString()) : null);
        setDto("mailAccountId", mailAccountId);

        Integer useSSLConnection = (!GenericValidator.isBlankOrNull((String) getDto("useSSLConnection"))
                ? new Integer((String) getDto("useSSLConnection")) : null);
        setDto("useSSLConnection", useSSLConnection);


        //update boolean value for defaultAccount field to Boolean type
        Boolean defaultAccount = (null != getDto("defaultAccount") &&
                "true".equals(getDto("defaultAccount").toString()));
        setDto("defaultAccount", defaultAccount);

        //verifies if the pop account its registred for the actual user
        boolean isDuplicated = Functions.isDuplicatedPOPAccont(popUser,
                popServer,
                operation,
                mailAccountId,
                userMailId,
                companyId,
                request);
        if (isDuplicated) {
            return new ActionError("MailAccount.pop.msg.duplicated", popUser, popServer);
        }

        //verifies if the pop account does not use by another user in the company
        ResultMessage isAvailableAccountMessage = Functions.isAvailablePOPAccount(popUser,
                popServer,
                userMailId,
                companyId,
                request);
        if (null != isAvailableAccountMessage) {
            return new ActionError(isAvailableAccountMessage.getKey(), isAvailableAccountMessage.getParams());
        }

        //verifies the connection with pop server
        if ("create".equals(operation) || "true".equals(getDto("popOptionChanged"))) {
            ResultMessage popServerConnectionMessage = Functions.popServerConnection(popUser,
                    popServer,
                    password,
                    popPort,
                    useSSLConnection,
                    userMailId,
                    mailAccountId,
                    operation,
                    request);
            if (null != popServerConnectionMessage) {
                return new ActionError(popServerConnectionMessage.getKey(), popServerConnectionMessage.getParams());
            }
        }

        return null;
    }

    /**
     * Validates the Smtp account configuration.
     * <p/>
     * - Verifies all required data for smtp configuration.
     * - Check duplicated smtp configurations by user.
     * - Check if the smtp configuration is available in the company.
     * - Check connection with smtp server.
     *
     * @param request <code>HttpServletRequest</code> object, used for general purposes.
     * @return <code>List</code> of <code>ActionError</code> if some validation fails or
     *         null if all validations passed successful.
     */
    private List<ActionError> validateSMTPAccount(HttpServletRequest request) {
        String operation = (String) getDto("op");
        String smtpServer = (String) getDto("smtpServer");
        String smtpPort = (String) getDto("smtpPort");
        String email = (String) getDto("email");

        String smtpAuthentication = "false";
        if (null != getDto("smtpAuthentication") &&
                "true".equals(getDto("smtpAuthentication").toString())) {
            smtpAuthentication = "true";
        }

        Integer smtpSSL = (!GenericValidator.isBlankOrNull((String) getDto("smtpSSL"))
                ? new Integer((String) getDto("smtpSSL")) : null);
        setDto("smtpSSL", smtpSSL);


        String usePOPConfiguration = "false";
        if (null != getDto("usePOPConfiguration") &&
                "true".equals(getDto("usePOPConfiguration").toString())) {
            usePOPConfiguration = "true";
        }

        Integer mailAccountId = (null != getDto("mailAccountId") ?
                new Integer(getDto("mailAccountId").toString()) : null);

        List<ActionError> errors = new ArrayList<ActionError>();

        if ("true".equals(usePOPConfiguration)) {
            String popUser = (String) getDto("login");
            String popPassword = (String) getDto("password");
            if (GenericValidator.isBlankOrNull(email)
                    || GenericValidator.isBlankOrNull(smtpServer)
                    || GenericValidator.isBlankOrNull(smtpPort)
                    || GenericValidator.isBlankOrNull(popUser)
                    || GenericValidator.isBlankOrNull(popPassword)) {
                return new ArrayList<ActionError>();
            }

            if ("create".equals(operation) || "true".equals(getDto("smtpOptionChanged"))) {
                ActionError connectionError = smtpServerConnection(email,
                        popUser,
                        popPassword,
                        smtpServer,
                        smtpPort,
                        operation,
                        Boolean.valueOf(smtpAuthentication),
                        userMailId,
                        mailAccountId,
                        smtpSSL,
                        request);

                if (null != connectionError) {
                    errors.add(connectionError);
                    return errors;
                }
            }
        } else {
            String smtpUser = "";
            String smtpPassword = "";

            if ("true".equals(smtpAuthentication)) {
                smtpUser = (String) getDto("smtpLogin");
                smtpPassword = (String) getDto("smtpPassword");
                if (GenericValidator.isBlankOrNull(smtpUser)) {
                    errors.add(new ActionError("errors.required",
                            JSPHelper.getMessage(request, "MailAccount.smtpLogin")));
                }

                if (GenericValidator.isBlankOrNull(smtpPassword)) {
                    errors.add(new ActionError("errors.required",
                            JSPHelper.getMessage(request, "MailAccount.smtpPassword")));
                }


                if (GenericValidator.isBlankOrNull(email)
                        || GenericValidator.isBlankOrNull(smtpServer)
                        || GenericValidator.isBlankOrNull(smtpPort)
                        || GenericValidator.isBlankOrNull(smtpUser)
                        || GenericValidator.isBlankOrNull(smtpPassword)) {
                    return errors;
                }

                boolean isDuplicated = Functions.isDuplicatedSMTPAccount(smtpUser,
                        smtpServer,
                        operation,
                        mailAccountId,
                        userMailId,
                        companyId,
                        request);
                if (isDuplicated) {
                    errors.add(new ActionError("MailAccount.smtp.msg.duplicated", smtpUser, smtpServer));
                    return errors;
                }

                //verifies if the smtp account does not use by another user in the company
                ResultMessage isAvailableAccountMessage = Functions.isAvailableSMTPAccount(smtpUser,
                        smtpServer,
                        userMailId,
                        companyId,
                        request);
                if (null != isAvailableAccountMessage) {
                    errors.add(new ActionError(isAvailableAccountMessage.getKey(), isAvailableAccountMessage.getParams()));
                    return errors;
                }
            }
            if ("create".equals(operation) || "true".equals(getDto("smtpOptionChanged"))) {
                ActionError connectionError = smtpServerConnection(email,
                        smtpUser,
                        smtpPassword,
                        smtpServer,
                        smtpPort,
                        operation,
                        Boolean.valueOf(smtpAuthentication),
                        userMailId,
                        mailAccountId,
                        smtpSSL,
                        request);

                if (null != connectionError) {
                    errors.add(connectionError);
                    return errors;
                }
            }
        }

        return errors;
    }

    private ActionError smtpServerConnection(String email,
                                             String smtpUser,
                                             String smtpPassword,
                                             String smtpServer,
                                             String smtpPort,
                                             String formOperation,
                                             Boolean smtpAuthentication,
                                             Integer userMailId,
                                             Integer mailAccountId,
                                             Integer smtpSSL,
                                             HttpServletRequest request) {
        WebMailConstants.EmailAccountErrorType smtpValidationError =
                Functions.smtpServerConnection(email,
                        smtpUser,
                        smtpPassword,
                        smtpServer,
                        smtpPort,
                        formOperation,
                        Boolean.valueOf(smtpAuthentication),
                        userMailId,
                        mailAccountId,
                        smtpSSL,
                        request);

        if (null != smtpValidationError)

        {
            if (WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION.equals(smtpValidationError)) {
                return new ActionError("WebmailAccount.smtp.authenticationError");
            } else if (WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER.equals(smtpValidationError)) {
                return new ActionError("WebmailAccount.smtp.providerError");
            } else {
                return new ActionError("WebmailAccount.smtp.serviceError");
            }
        }

        return null;
    }

    private ActionErrors addErrors(List<ActionError> errorList, ActionErrors errors) {
        for (int i = 0; i < errorList.size(); i++) {
            errors.add("error_" + i, errorList.get(i));
        }

        return errors;
    }

    private ActionError validateForwardEmail(HttpServletRequest request) {
        String forwardEmail = (String) getDto("forwardEmail");
        if (GenericValidator.isBlankOrNull(forwardEmail)) {
            return null;
        }

        User user = RequestUtils.getUser(request);
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("getMailAccounts");
        emailUserCmd.putParam("userMailId", user.getValue(Constants.USERID));
        emailUserCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(emailUserCmd, request);
            List<MailAccountDTO> accounts = (List<MailAccountDTO>) resultDTO.get("getMailAccounts");
            for (MailAccountDTO dto : accounts) {
                if (forwardEmail.equals(dto.get("email"))) {
                    return new ActionError("MailAccount.forwardEmail.error");
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmailUserCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

}
