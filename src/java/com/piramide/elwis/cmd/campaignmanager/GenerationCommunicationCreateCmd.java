package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityContactDTO;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.dto.webmailmanager.MailContactDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Cmd to create campaign generation related communications
 *
 * @author Miky
 * @version $Id: GenerationCommunicationCreateCmd.java 10320 2013-02-26 20:02:58Z miguel $
 */
public class GenerationCommunicationCreateCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing GenerationCommunicationCreateCmd.........." + paramDTO);

        if ("campaignLightGeneration".equals(getOp())) {
            campaignLightGenerationProcess(ctx);
        } else {
            activityCampaignGenerationProcess(ctx);
        }
    }

    private void activityCampaignGenerationProcess(SessionContext ctx) {
        Integer generationId = new Integer(paramDTO.get("generationId").toString());
        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        log.debug("generationId:" + generationId);
        if (generationId != null) {
            //create communications
            createCommunications(activityId, companyId, generationId, ctx);
        }
    }

    private void campaignLightGenerationProcess(SessionContext ctx) {
        Integer generationId = new Integer(paramDTO.get("generationId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        log.debug("generationId:" + generationId);
        if (generationId != null) {
            //create communications
            createCommunications(null, companyId, generationId, ctx);
        }
    }

    private void createCommunications(Integer activityId, Integer companyId, Integer generationId, SessionContext ctx) {
        List<Map> addressInfoList = (List<Map>) paramDTO.get("addressInfoList");
        Date currentDate = new Date();

        boolean isEmailComm = paramDTO.containsKey("isEmailComm");
        Mail mail = null;

        for (Map infoMap : addressInfoList) {

            //create communication
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.put("addressId", infoMap.get("addressId"));
            contactDTO.put("contactPersonId", infoMap.get("contactPersonId"));
            contactDTO.put("dateStart", DateUtils.dateToInteger(currentDate));
            contactDTO.put("companyId", companyId);
            contactDTO.put("employeeId", infoMap.get("employeeId"));
            contactDTO.put("inOut", ContactConstants.CommunicationType.OUTGOING.getTypeAsInt()); // out by default;
            contactDTO.put("isAction", Boolean.FALSE);
            contactDTO.put("note", paramDTO.get("note"));
            contactDTO.put("status", isEmailComm ? "0" : "1"); // readed by default to letter;
            contactDTO.put("type", isEmailComm ? CommunicationTypes.EMAIL : CommunicationTypes.LETTER);

            Contact contact = (Contact) ExtendedCRUDDirector.i.create(contactDTO, resultDTO, false);

            if (contact != null) {
                CampaignActivityContactDTO activityContactDTO = new CampaignActivityContactDTO();
                activityContactDTO.put("campaignId", paramDTO.get("campaignId"));
                activityContactDTO.put("contactId", contact.getContactId());
                activityContactDTO.put("companyId", companyId);
                activityContactDTO.put("activityId", activityId);
                activityContactDTO.put("generationId", generationId);
                if (isEmailComm) {
                    activityContactDTO.put("fromEmail", infoMap.get("fromEmail"));
                }

                ExtendedCRUDDirector.i.create(activityContactDTO, resultDTO, false);

                //create email contact
                if (isEmailComm) {

                    if (mail == null) {
                        mail = createMail(companyId, paramDTO.get("note").toString(), ctx);
                        if (mail == null) {
                            log.debug("Mail can't create...");
                            ctx.setRollbackOnly();
                            return;
                        }
                    }

                    MailContactDTO mailContactDTO = new MailContactDTO();
                    mailContactDTO.put("companyId", companyId);
                    mailContactDTO.put("contactId", contact.getContactId());
                    mailContactDTO.put("mailId", mail.getMailId());
                    mailContactDTO.put("email", infoMap.get("toEmail"));

                    ExtendedCRUDDirector.i.create(mailContactDTO, resultDTO, false);
                }
            }
        }
    }

    private Mail createMail(Integer companyId, String subject, SessionContext ctx) {
        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer userMailId = userId;

        MailAccountHome mailAccountHome = (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        MailAccount mailAccount = null;
        try {
            mailAccount = mailAccountHome.findDefaultAccount(userMailId, companyId);
        } catch (FinderException e) {
            log.debug("Can't find default user email account" + e);
            boolean hasAccount = false;
            try {
                Collection accounts = mailAccountHome.findAccountsByUserMailAndCompany(userMailId, companyId);
                if (!accounts.isEmpty()) {
                    hasAccount = true;
                    mailAccount = (MailAccount) accounts.iterator().next();
                }
            } catch (FinderException e1) {
                log.debug("Can't find user email account" + e);
            }

            if (!hasAccount) {
                ctx.setRollbackOnly();
                resultDTO.addResultMessage("Campaign.activity.emailGenerate.accountNotFound");
                resultDTO.setResultAsFailure();
                return null;
            }
        }

        // extracts the sent folder
        FolderHome folderHome = (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);

        Folder folder = null;
        try {
            folder = folderHome.findByFolderType(userId, new Integer(WebMailConstants.FOLDER_SENDITEMS), companyId);
        } catch (FinderException e) {
            log.debug("Can't find send items folder...");
            ctx.setRollbackOnly();
            return null;
        }


        MailDTO mailDTO = new MailDTO();
        mailDTO.put("companyId", companyId);
        //mailDTO.put("bodyId", body.getBodyId());
        mailDTO.put("folderId", folder.getFolderId());

        mailDTO.put("mailPriority", WebMailConstants.MAIL_PRIORITY_DEFAULT);
        mailDTO.put("mailState", MailStateUtil.addSendState());
        mailDTO.put("mailSubject", subject);
        mailDTO.put("mailFrom", mailAccount.getEmail());
        mailDTO.put("mailAccount", mailAccount.getEmail());

        mailDTO.put("mailSize", new Integer(0));
        mailDTO.put("mailPersonalFrom", mailAccount.getEmail());
        mailDTO.put("sentDate", new Long(System.currentTimeMillis()));
        mailDTO.put("incomingOutgoing", WebMailConstants.OUT_VALUE);
        mailDTO.put("hidden", Boolean.TRUE);

        Mail mail = (Mail) ExtendedCRUDDirector.i.create(mailDTO, resultDTO, false);
        mail.setIsCompleteEmail(true);
        return mail;
    }

}
