package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

import static net.java.dev.strutsejb.dto.EJBFactory.i;

/**
 * Cmd to manage autosave email as draft
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class ComposeTemporalMailCmd extends ComposeMailCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ComposeTemporalMailCmd...." + paramDTO);

        String op = getOp();
        if ("autosaveEmail".equals(op)) {
            processTemporaEmail(ctx);
        } else if ("saveTempAttach".equals(op)) {
            addTemporalAttach();
        } else if ("deleteTempAttach".equals(op)) {
            removeTemporalAttach();
        } else if ("readTempAttachs".equals(op)) {
            readMailTemporalAttachs();
        }

    }

    private boolean hasBasicDataToProcessTemporalEmail() {
        Integer mailAccountId = EJBCommandUtil.i.getValueAsInteger(this, "mailAccountId");
        return mailAccountId != null;
    }

    private void processTemporaEmail(SessionContext ctx) {
        if (!hasBasicDataToProcessTemporalEmail()) {
            log.debug("Not exist basic data to process temporal email...");
            return;
        }

        Integer tempMailId = EJBCommandUtil.i.getValueAsInteger(this, "tempMailId");

        //add default draft temp email
        paramDTO.put("isDraftEmail", "true");
        paramDTO.put("isDraftTemp", Boolean.TRUE);

        Mail email = null;

        if (tempMailId == null) {
            email = createTemporalEmail(ctx);
        } else {
            email = updateTemporalEmail(tempMailId, ctx);
        }

        if (email != null) {
            resultDTO.put("tempMailId", email.getMailId());
        }
    }

    private void addTemporalAttach() {
        Integer tempMailId = EJBCommandUtil.i.getValueAsInteger(this, "tempMailId");
        ArrayByteWrapper attachWrapper = (ArrayByteWrapper) paramDTO.get("temporalAttachWrapper");

        if (attachWrapper != null && tempMailId != null) {

            Mail email = getEmail(tempMailId);
            if (email != null) {
                Attach attach = createTemporalAttach(email.getMailId(), email.getCompanyId(), attachWrapper);
                if (attach != null) {
                    resultDTO.put("tempAttachId", attach.getAttachId());
                    resultDTO.put("attachName", attach.getAttachName());
                    resultDTO.put("attachSize", attach.getSize());
                }
            }
        }
    }

    private void removeTemporalAttach() {
        Integer attachId = EJBCommandUtil.i.getValueAsInteger(this, "attachId");

        AttachHome attachHome = (AttachHome) i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        Boolean isRemoved = false;
        if (attachId != null) {
            try {
                Attach attach = attachHome.findByPrimaryKey(attachId);
                attach.remove();
                isRemoved = true;
            } catch (FinderException e) {
                log.debug("Not found mail attach..." + attachId, e);
            } catch (RemoveException e) {
                log.debug("Error in remove mail attach..." + attachId, e);
            }
        }

        resultDTO.put("tempAttachIsRemoved", isRemoved);
    }

    private void readMailTemporalAttachs() {
        Integer tempMailId = EJBCommandUtil.i.getValueAsInteger(this, "tempMailId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        List<AttachDTO> resultList = new ArrayList<AttachDTO>();

        if (tempMailId != null && companyId != null) {
            AttachHome attachHome = (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
            List attachments = null;
            try {
                attachments = (List) attachHome.findByMailId(tempMailId, companyId);
            } catch (FinderException e) {
                attachments = new ArrayList();
            }

            if (attachments != null) {
                for (int i = 0; i < attachments.size(); i++) {
                    Attach attachment = (Attach) attachments.get(i);
                    if (attachment.getVisible() != null && !attachment.getVisible()) {
                        AttachDTO attachDTO = new AttachDTO();
                        DTOFactory.i.copyToDTO(attachment, attachDTO);
                        resultList.add(attachDTO);
                    }
                }
            }
        }

        resultDTO.put("mailTemporalAttachList", resultList);
    }

    private Mail createTemporalEmail(SessionContext ctx) {
        log.debug("Temp email not exist, so create this.................");

        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        List toRecipient = extractMailsByType("to");
        List ccRecipient = extractMailsByType("cc");
        List bccRecipient = extractMailsByType("bcc");

        Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
        UserMail userMail = getUserMail(userMailId);

        List<AttachDTO> oldAttachments = new ArrayList<AttachDTO>();
        if (paramDTO.get("oldAttachments") != null) {
            oldAttachments = (List<AttachDTO>) paramDTO.get("oldAttachments");
        }
        List<ArrayByteWrapper> newAttachments = new ArrayList<ArrayByteWrapper>();
        if (paramDTO.get("newAttachments") != null) {
            newAttachments = (List<ArrayByteWrapper>) paramDTO.get("newAttachments");
        }

        Mail email = saveEmail(toRecipient,
                ccRecipient,
                bccRecipient,
                userMailId,
                oldAttachments,
                newAttachments,
                companyId,
                ctx);

        //the message is saved in draft folder
        sendMailToFolder(email, WebMailConstants.FOLDER_DRAFTITEMS, userMail.getUserMailId());

        return email;
    }

    private Mail updateTemporalEmail(Integer mailId, SessionContext ctx) {
        log.debug("Temp email exist, so update this.................");

        List toRecipient = extractMailsByType("to");
        List ccRecipient = extractMailsByType("cc");
        List bccRecipient = extractMailsByType("bcc");

        Mail email = updateEmail(mailId);

        if (email != null) {
            int bodySize = updateBody(email);

            int toRecipientSize = updateSendMailAddress(email, toRecipient, WebMailConstants.TO_TYPE_DEFAULT);
            int ccRecipientSize = updateSendMailAddress(email, ccRecipient, WebMailConstants.TO_TYPE_CC);
            int bccRecipientSize = updateSendMailAddress(email, bccRecipient, WebMailConstants.TO_TYPE_BCC);

            //process temporal images
            Integer temporalImagesSize = attachTemporalImages(email, ctx);

            int subjectSize = 0;
            if (null != email.getMailSubject()) {
                subjectSize = email.getMailSubject().length();
            }

            Integer attachSize = calculateMailAttachsSize(email.getMailId());

            int totalSize = toRecipientSize + ccRecipientSize + bccRecipientSize + bodySize + subjectSize + temporalImagesSize + attachSize;

            email.setMailSize(totalSize);
            email.setHaveAttachments((attachSize > 0));

            email.setSaveEmail(saveEmailInSentFolder());
            email.setCreateContact(createOutCommunication());
        }

        return email;
    }

    private Integer calculateMailAttachsSize(Integer mailId) {
        Integer size = null;
        AttachHome attachHome = (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
        try {
            size = attachHome.selectSumSizeByMailId(mailId);
        } catch (FinderException e) {
            log.debug("Error in execute select sum size....", e);
        }

        if (size == null) {
            size = 0;
        }
        return size;
    }

    private Mail updateEmail(Integer mailId) {
        DateTimeZone dateTimeZone = (DateTimeZone) paramDTO.get("userTimeZone");
        DateTime dateTime = new DateTime(dateTimeZone);

        Integer signatureId = EJBCommandUtil.i.getValueAsInteger(this, "signatureId");
        Integer mailAccountId = EJBCommandUtil.i.getValueAsInteger(this, "mailAccountId");

        MailAccount mailAccount = getMailAccount(mailAccountId);

        MailDTO emailDTO = new MailDTO();
        emailDTO.put("mailId", mailId);
        emailDTO.put("mailPriority", paramDTO.get("mailPriority"));
        emailDTO.put("mailState", MailStateUtil.addSendState());
        emailDTO.put("mailSubject", paramDTO.get("mailSubject"));
        emailDTO.put("mailFrom", mailAccount.getEmail());
        emailDTO.put("mailAccount", mailAccount.getEmail());
        emailDTO.put("mailPersonalFrom", mailAccount.getEmail());
        if (null != mailAccount.getPrefix() && !"".equals(mailAccount.getPrefix().trim())) {
            emailDTO.put("mailPersonalFrom", mailAccount.getPrefix());
        }
        emailDTO.put("sentDate", dateTime.getMillis());
        emailDTO.put("signatureId", signatureId);

        return (Mail) ExtendedCRUDDirector.i.update(emailDTO, resultDTO, false, false, false, "Fail");
    }

    private int updateBody(Mail email) {
        int size = 0;

        String newBody = "";
        if (paramDTO.get("body") != null) {
            newBody = paramDTO.getAsString("body");
        }

        email.getBody().setBodyContent(newBody.getBytes());
        size = newBody.getBytes().length;
        return size;
    }

    private int updateSendMailAddress(Mail mail, List recipient, String type) {
        int size = 0;

        if (null == recipient) {
            return size;
        }

        List<MailRecipient> currentMailRecipientList = findRecipients(mail, new Integer(type));
        List<Integer> existMailRecipientIds = new ArrayList<Integer>();

        for (Object object : recipient) {
            Map emailAddress = (Map) object;
            String email = ((String) emailAddress.get("email")).toLowerCase();

            MailRecipient mailRecipient = alreadyMailRecipientRegistered(email, currentMailRecipientList);
            if (mailRecipient != null) {
                existMailRecipientIds.add(mailRecipient.getMailRecipientId());

                size += mailRecipient.getEmail().getBytes().length + (mailRecipient.getPersonalName() != null ? mailRecipient.getPersonalName().getBytes().length : 0);
            } else {
                //if no exist, create the recipient
                List newRecipientList = new ArrayList();
                newRecipientList.add(emailAddress);

                size += createSendMailAddress(mail, newRecipientList, type);
            }
        }

        //remove not exist recipients
        removeNotExistRecipients(existMailRecipientIds, currentMailRecipientList);

        return size;
    }

    private MailRecipient alreadyMailRecipientRegistered(String emailAddress, List<MailRecipient> currentMailRecipientList) {
        for (MailRecipient mailRecipient : currentMailRecipientList) {
            if (mailRecipient.getEmail().equals(emailAddress)) {
                return mailRecipient;
            }
        }
        return null;
    }

    private List<MailRecipient> findRecipients(Mail email, Integer type) {
        MailRecipientHome mailRecipientHome = (MailRecipientHome) i.getEJBLocalHome(WebMailConstants.JNDI_MAILRECIPIENT);

        List<MailRecipient> recipients = new ArrayList<MailRecipient>();
        try {
            Collection ejbCollection = mailRecipientHome.findByMailIdAndToType(email.getMailId(), type, email.getCompanyId());
            recipients.addAll(ejbCollection);
        } catch (FinderException e) {
            log.debug("Error in find recipients Email mailId=" + email.getMailId() + " not contain recipients of type" + type);
        }
        return recipients;
    }

    private void removeNotExistRecipients(List<Integer> existMailRecipientIds, List<MailRecipient> currentMailRecipientList) {
        for (Iterator<MailRecipient> iterator = currentMailRecipientList.iterator(); iterator.hasNext(); ) {
            MailRecipient mailRecipient = iterator.next();
            boolean notExist = true;
            for (int i = 0; i < existMailRecipientIds.size(); i++) {
                Integer mailRecipientId = existMailRecipientIds.get(i);
                if (mailRecipientId.equals(mailRecipient.getMailRecipientId())) {
                    notExist = false;
                    break;
                }
            }

            if (notExist) {
                try {
                    mailRecipient.remove();
                } catch (RemoveException e) {
                    log.debug("Error in delete recipient...", e);
                }
            }
        }
    }

    private Attach createTemporalAttach(Integer mailId, Integer companyId, ArrayByteWrapper attachWrapper) {
        AttachHome attachHome = (AttachHome) i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
        Attach newAttach = null;
        if (attachWrapper != null) {
            try {
                newAttach = attachHome.create(companyId, mailId, attachWrapper.getFileName(), attachWrapper.getFileData());
                newAttach.setVisible(false);
            } catch (CreateException e) {
                log.error("-> Create new Attachment Fail", e);
            }
        }
        return newAttach;
    }

}
