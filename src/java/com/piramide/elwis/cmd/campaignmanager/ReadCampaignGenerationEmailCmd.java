package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.webmailmanager.ReadEmailCmd;
import com.piramide.elwis.cmd.webmailmanager.util.HtmlCodeUtil;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignGenAttach;
import com.piramide.elwis.domain.common.Attachment;
import com.piramide.elwis.domain.common.AttachmentHome;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ReadCampaignGenerationEmailCmd extends ReadEmailCmd {
    private Log log = LogFactory.getLog(ReadCampaignGenerationEmailCmd.class);

    @Override
    protected void read(Integer mailId,
                        Integer userMailId,
                        Integer readerUserMailId,
                        Boolean filterInvalidRecipients,
                        Boolean createCommunications,
                        Boolean changeToReadState,
                        SessionContext ctx) {
        super.read(mailId, userMailId, readerUserMailId, filterInvalidRecipients, createCommunications, changeToReadState, ctx);
    }

    @Override
    protected void readBody(Integer mailId, SessionContext ctx) {
        resultDTO.put("body", this.getBody(null, ctx));
        resultDTO.put("attachments", this.getAttachments(null, ctx));
    }

    @Override
    protected List<AttachDTO> getAttachments(Mail email, SessionContext ctx) {
        Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
        CampaignActivityContact activityContact = getCampaignActivityContact(contactId, ctx);

        AttachmentHome attachmentHome = (AttachmentHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_ATTACHMENT);

        List<AttachDTO> result = new ArrayList<AttachDTO>();
        List attachmentList = new ArrayList();
        for (Iterator iterator = activityContact.getCampaignGeneration().getGenerationAttachs().iterator(); iterator.hasNext();) {
            CampaignGenAttach campaignGenAttach = (CampaignGenAttach) iterator.next();

            Attachment attachment;
            try {
                attachment = attachmentHome.findByPrimaryKey(campaignGenAttach.getAttachmentId());
            } catch (FinderException e) {
                log.debug("Can't found attachment " + campaignGenAttach.getAttachmentId());
                continue;
            }

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.put("attachId", attachment.getAttachmentId());
            attachDTO.put("freeTextId", attachment.getFreeTextId());
            attachDTO.put("attachFile", attachment.getFileName());
            attachDTO.put("visible", false);
            attachDTO.put("size", attachment.getFileSize());
            result.add(attachDTO);
        }
        if (attachmentList.size() > 0) {
            resultDTO.put("hasAttachs", "true");//For a draft
        }
        resultDTO.put("genAttachs", attachmentList);

        return result;
    }

    @Override
    protected String getBody(Mail email, SessionContext ctx) {
        Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");

        GenerationCommunicationTextReadCmd textReadCmd = new GenerationCommunicationTextReadCmd();
        textReadCmd.putParam("contactId", contactId);
        textReadCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = textReadCmd.getResultDTO();

        if (!textReadCmd.getResultDTO().isFailure()) {
            ArrayByteWrapper textWrapper = (ArrayByteWrapper) myResultDTO.get("freeText");
            String newBody = new String(textWrapper.getFileData());
            resultDTO.put("bodyType", WebMailConstants.BODY_TYPE_HTML);
            return HtmlCodeUtil.processAttributesForLink(newBody);
        }

        return "";
    }

    private CampaignActivityContact getCampaignActivityContact(Integer contactId, SessionContext ctx) {
        CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
        campGenCommunicationCmd.putParam("contactId", contactId);
        campGenCommunicationCmd.executeInStateless(ctx);
        return campGenCommunicationCmd.getCampaignActivityContact();
    }
}
