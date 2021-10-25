package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.campaignmanager.CampaignGenerationDeleteCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.supportmanager.SupportCaseCommunicationCmd;
import com.piramide.elwis.cmd.webmailmanager.CommunicationManagerCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContactHome;
import com.piramide.elwis.domain.campaignmanager.CampaignGeneration;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * @author Yumi
 * @version $Id: ContactDeleteCmd.java 10285 2012-11-28 05:59:23Z miguel $
 */

public class ContactDeleteCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(ContactDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if (paramDTO.containsKey("caseId")) {
            SupportCaseCommunicationCmd cmd = new SupportCaseCommunicationCmd();
            Integer caseId = new Integer(paramDTO.get("caseId").toString());
            Integer contactId = new Integer(paramDTO.get("contactId").toString());
            try {
                cmd.deleteSupportContact(caseId, contactId);
            } catch (AppLevelException e) {
                ctx.setRollbackOnly();
                resultDTO.setForward("Fail");
                return;
            }
        }

        ContactDTO contactDTO = new ContactDTO(paramDTO);

        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        Contact contact = null;
        try {
            contact = contactHome.findByPrimaryKey(new Integer(paramDTO.get("contactId").toString()));
            if (contact != null) {
                deleteEmailCommunications(contact.getContactId(), contact.getCompanyId(), ctx);

                CampaignActivityContactHome h =
                        (CampaignActivityContactHome)
                                EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);
                try {
                    CampaignActivityContact c = h.findByContactId(contact.getContactId());
                    if (null != c) {
                        Integer generationId = c.getGenerationId();
                        EJBFactory.i.removeEJB(c);
                        if (isGenerationCommunicationAndDelete(generationId)) {
                            ResultDTO deleteResultDTO = deleteCampaignGeneration(generationId, ctx);
                            if (deleteResultDTO.isFailure()) {
                                resultDTO.putAll(deleteResultDTO);
                                resultDTO.setResultAsFailure();
                                resultDTO.setForward("Fail");
                            }
                        }
                    }
                } catch (FinderException fe) {
                    log.debug("The contact " + contact.getContactId() + " not have a Campaign relation");
                }

                try {
                    ContactFreeText freeText = contact.getContactFreeText();
                    contact.remove();
                    if (freeText != null) {
                        freeText.remove();
                    }
                } catch (RemoveException e) {
                    ctx.setRollbackOnly();
                    log.debug("Error removing the contact, it seems was deleted by other user");
                    resultDTO.isClearingForm = true;
                    resultDTO.setResultAsFailure();
                    resultDTO.setForward("Fail");
                    contactDTO.addNotFoundMsgTo(resultDTO);
                }
            }
        } catch (FinderException e) {
            log.debug(" ... communication not found ....");
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            return;
        }
        //Contact contact = (Contact) EJBFactory.i.findEJB(contactDTO);

    }

    public boolean isStateful() {
        return false;
    }

    private void deleteEmailCommunications(Integer contactId, Integer companyId, SessionContext ctx) {
        CommunicationManagerCmd commManagerCmd = new CommunicationManagerCmd();
        commManagerCmd.setOp("deleteEmailContact");
        commManagerCmd.putParam("contactId", contactId);
        commManagerCmd.putParam("companyId", companyId);
        commManagerCmd.executeInStateless(ctx);
    }

    /**
     * Verify if communication is related with campaign generation and if this can be deleted
     *
     * @param generationId
     * @return true or false
     */
    private boolean isGenerationCommunicationAndDelete(Integer generationId) {
        if (generationId != null) {
            CampaignGeneration campaignGeneration = (CampaignGeneration) ExtendedCRUDDirector.i.read(new CampaignGenerationDTO(generationId), new ResultDTO(), false);
            if (campaignGeneration != null) {
                log.debug("related generation activity contacts:" + campaignGeneration.getCampaignActivityContacts().size());
                return campaignGeneration.getCampaignActivityContacts().isEmpty() && campaignGeneration.getCampaignSentLog() == null;
            }
        }
        return false;
    }

    /**
     * delete campaign generation realated with communication
     *
     * @param generationId
     * @param ctx
     * @return resultDTO
     */
    private ResultDTO deleteCampaignGeneration(Integer generationId, SessionContext ctx) {
        CampaignGenerationDeleteCmd generationDeleteCmd = new CampaignGenerationDeleteCmd();
        generationDeleteCmd.putParam("generationId", generationId);
        generationDeleteCmd.executeInStateless(ctx);
        return generationDeleteCmd.getResultDTO();
    }

}
