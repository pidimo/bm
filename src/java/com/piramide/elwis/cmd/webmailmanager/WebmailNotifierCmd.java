package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.EmailAccountErrorDTO;
import com.piramide.elwis.dto.webmailmanager.EmailAccountErrorDetailDTO;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class WebmailNotifierCmd extends EJBCommand {
    private Log log = LogFactory.getLog(WebmailNotifierCmd.class);

    @Override
    public void executeInStateless(SessionContext context) {
        boolean isRead = true;
        if (isRead) {
            Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            List<Map> newEmailsByFolders = (List<Map>) paramDTO.get("newEmailsByFolder");
            read(userMailId, companyId, newEmailsByFolders);
        }
    }

    private void read(Integer userMailId, Integer companyId, List<Map> newEmailsByFolders) {
        UserMail userMail = getUserMail(userMailId);
        if (null == userMail) {
            return;
        }

        EmailAccountErrorHome emailAccountErrorHome =
                (EmailAccountErrorHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERROR);
        List errorMessages = new ArrayList();
        try {
            errorMessages = (List) emailAccountErrorHome.findByUserMailId(userMailId, companyId);
        } catch (FinderException e) {
            log.debug("-> No Error Messages for userMailId=" + userMailId + " companyId=" + companyId);
        }

        List<EmailAccountErrorDTO> smtpErrorMessages = new ArrayList<EmailAccountErrorDTO>();
        List<EmailAccountErrorDTO> popErrorMessages = new ArrayList<EmailAccountErrorDTO>();


        for (Object object : errorMessages) {
            EmailAccountError errorMessage = (EmailAccountError) object;
            EmailAccountErrorDTO dto = new EmailAccountErrorDTO();
            DTOFactory.i.copyToDTO(errorMessage, dto);
            if (null != errorMessage.getMailAccountId()) {
                String accountEmail = getAccountEmail(errorMessage.getMailAccountId());
                dto.put("accountEmail", accountEmail);
            }

            if (isSmtpErrorMessage(errorMessage.getErrorType())) {
                dto.put("errorDetailList", readErrorDetailDTOs(errorMessage.getEmailAccountErrorId(), companyId));
                smtpErrorMessages.add(dto);
            }

            if (isPopErrorMessage(errorMessage.getErrorType())) {
                popErrorMessages.add(dto);
            }

            try {
                errorMessage.remove();
            } catch (RemoveException e) {
                log.debug("->Remove EmailAccountError FAIL ", e);
            }
        }

        if (!smtpErrorMessages.isEmpty()) {
            resultDTO.put("smtpErrorMessages", smtpErrorMessages);
        }
        if (!popErrorMessages.isEmpty()) {
            resultDTO.put("popErrorMessages", popErrorMessages);
        }

        readNewEmails(userMail, newEmailsByFolders);
    }

    private void readNewEmails(UserMail userMail, List<Map> newEmailsByFolders) {

        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        List<FolderDTO> result = new ArrayList<FolderDTO>();
        List<Integer> folderIdentifiers = new ArrayList<Integer>();

        Integer emailCounter = 0;
        for (Map data : newEmailsByFolders) {
            String folderName = (String) data.get("folderName");
            String folderId = (String) data.get("folderId");
            List<String> newEmailIds = (List<String>) data.get("newEmailIds");

            FolderDTO folderDTO = new FolderDTO();
            folderDTO.put("folderName", folderName);
            folderDTO.put("folderId", Integer.valueOf(folderId));
            folderDTO.put("size", newEmailIds.size());
            emailCounter += newEmailIds.size();

            result.add(folderDTO);
            folderIdentifiers.add(Integer.valueOf(folderId));

            for (String emailId : newEmailIds) {
                try {
                    Mail mail = mailHome.findByPrimaryKey(Integer.valueOf(emailId));
                    mail.setIsNewEmail(false);
                } catch (FinderException e) {
                    log.debug("-> Change IsNewEmail field in Mail Object FAIL");
                }
            }
        }

        if (!result.isEmpty()) {
            resultDTO.put("folderIdentifiers", folderIdentifiers);
            resultDTO.put("emailCounter", emailCounter);
            if (userMail.getShowPopNotification()) {
                resultDTO.put("newEmails", result);
            }
        }
    }

    private List<EmailAccountErrorDetailDTO> readErrorDetailDTOs(Integer emailAccountErrorId, Integer companyId) {
        List<EmailAccountErrorDetailDTO> errorDetailDTOList = new ArrayList<EmailAccountErrorDetailDTO>();

        EmailAccountErrorDetailHome emailAccountErrorDetailHome = (EmailAccountErrorDetailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERRORDETAIL);
        Collection errorDetails;
        try {
            errorDetails = emailAccountErrorDetailHome.findByEmailAccountErrorId(emailAccountErrorId, companyId);
        } catch (FinderException e) {
            errorDetails = new ArrayList();
        }
        for (Iterator iterator = errorDetails.iterator(); iterator.hasNext();) {
            EmailAccountErrorDetail emailAccountErrorDetail = (EmailAccountErrorDetail) iterator.next();
            EmailAccountErrorDetailDTO dto = new EmailAccountErrorDetailDTO();
            DTOFactory.i.copyToDTO(emailAccountErrorDetail, dto);

            errorDetailDTOList.add(dto);
        }

        return errorDetailDTOList;
    }

    private void updateNewEmails(List newEmails) {
        for (int i = 0; i < newEmails.size(); i++) {
            Mail email = (Mail) newEmails.get(i);
            email.setIsNewEmail(false);
        }
    }

    private boolean isSmtpErrorMessage(Integer errorType) {
        return WebMailConstants.EmailAccountErrorType.SMTP_SENDFAILED.getConstant() == errorType ||
                WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION.getConstant() == errorType ||
                WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER.getConstant() == errorType ||
                WebMailConstants.EmailAccountErrorType.SMTP_SERVICE.getConstant() == errorType;
    }

    private boolean isPopErrorMessage(Integer errorType) {
        return WebMailConstants.EmailAccountErrorType.POP_AUTHENTICATION.getConstant() == errorType ||
                WebMailConstants.EmailAccountErrorType.POP_PROVIDER.getConstant() == errorType ||
                WebMailConstants.EmailAccountErrorType.POP_SERVICE.getConstant() == errorType;
    }

    private UserMail getUserMail(Integer userMailId) {
        UserMailHome userMailHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        try {
            return userMailHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            return null;
        }
    }

    private String getAccountEmail(Integer mailAccountId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            return mailAccountHome.findByPrimaryKey(mailAccountId).getEmail();
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
