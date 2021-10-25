package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.MailAccountCmd;
import com.piramide.elwis.cmd.webmailmanager.util.HtmlCodeUtil;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignGenAttach;
import com.piramide.elwis.domain.campaignmanager.CampaignGeneration;
import com.piramide.elwis.domain.common.Attachment;
import com.piramide.elwis.domain.common.AttachmentHome;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Cmd to read Email related to campaign generation
 *
 * @author Miky
 * @version $Id: ReadCampaignGenerationMailCmd.java 9703 2009-09-12 15:46:08Z fernando $
 * @deprecated replaced by ReadCampaignGenerationEmailCmd
 */
public class ReadCampaignGenerationMailCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReadCampaignGenerationMailCmd....." + paramDTO);

        Integer contactId = new Integer(paramDTO.get("contactId").toString());

        if ("readGenMailBody".equals(getOp())) {
            readMailBody(contactId, ctx);

        } else {
            Integer mailId = new Integer(paramDTO.get("mailId").toString());
            Integer userMailId = new Integer(paramDTO.get("userMailId").toString());

            CampaignActivityContact activityContact = getCampaignActivityContact(contactId, ctx);

            Mail mail = readMail(mailId);
            searchFromEmail(mail, userMailId, ctx);
            readMailBody(contactId, ctx);
            readAttachs(activityContact.getCampaignGeneration());
            resultDTO.put("mailId", mailId);
            //sender from email
            resultDTO.put("mailFrom", activityContact.getFromEmail());
        }

    }

    private CampaignActivityContact getCampaignActivityContact(Integer contactId, SessionContext ctx) {
        CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
        campGenCommunicationCmd.putParam("contactId", contactId);
        campGenCommunicationCmd.executeInStateless(ctx);
        return campGenCommunicationCmd.getCampaignActivityContact();
    }

    private Mail readMail(Integer mailId) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.put("mailId", mailId);
        Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, resultDTO);
        ///resultDTO.put("emailForm",mail.getMailFrom());
        return (mail);
    }

    private void searchFromEmail(Mail mail, Integer userMailId, SessionContext ctx) {
        MailAccountCmd accountCmd = new MailAccountCmd();
        accountCmd.setOp("searchAccountByEmail");
        accountCmd.putParam("email", mail.getMailFrom());
        accountCmd.putParam("userMailId", userMailId);
        accountCmd.putParam("companyId", mail.getCompanyId());
        accountCmd.executeInStateless(ctx);
        ResultDTO accountResult = accountCmd.getResultDTO();
        Integer mailAccountId = (Integer) accountResult.get("mailAccountId");
        if (null != mailAccountId) {
            resultDTO.put("mailAccountId", mailAccountId.toString());
        }
    }

    private void readMailBody(Integer contactId, SessionContext ctx) {
        GenerationCommunicationTextReadCmd textReadCmd = new GenerationCommunicationTextReadCmd();
        textReadCmd.putParam("contactId", contactId);
        textReadCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = textReadCmd.getResultDTO();

        if (!textReadCmd.getResultDTO().isFailure()) {
            ArrayByteWrapper textWrapper = (ArrayByteWrapper) myResultDTO.get("freeText");
            String newBody = new String(textWrapper.getFileData());
            newBody = HtmlCodeUtil.processAttributesForLink(newBody);

            resultDTO.put("body", newBody);
            resultDTO.put("html", "true");
            resultDTO.put("bodyType", WebMailConstants.BODY_TYPE_HTML);
        }
    }

    private void readAttachs(CampaignGeneration campaignGeneration) {

        AttachmentHome attachmentHome = (AttachmentHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_ATTACHMENT);

        List attachmentList = new ArrayList();
        for (Iterator iterator = campaignGeneration.getGenerationAttachs().iterator(); iterator.hasNext();) {
            CampaignGenAttach campaignGenAttach = (CampaignGenAttach) iterator.next();

            Attachment attachment = null;
            try {
                attachment = attachmentHome.findByPrimaryKey(campaignGenAttach.getAttachmentId());
            } catch (FinderException e) {
                log.debug("Can't found attachment " + campaignGenAttach.getAttachmentId());
                continue;
            }

            Map attachmentMap = new HashMap();
            attachmentMap.put("attachmentId", attachment.getAttachmentId());
            attachmentMap.put("freeTextId", attachment.getFreeTextId());
            attachmentMap.put("attachName", attachment.getFileName());
            //attachmentMap.put("attachVisibility", (attachVisibility != null ? attachVisibility.toString() : "false"));
            attachmentMap.put("attachSize", attachment.getFileSize());
            attachmentList.add(attachmentMap);
        }
        if (attachmentList.size() > 0) {
            resultDTO.put("hasAttachs", "true");//For a draft
        }
        resultDTO.put("genAttachs", attachmentList);

    }

}
