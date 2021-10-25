package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.cmd.contactmanager.ContactReadCmd;
import com.piramide.elwis.cmd.contactmanager.ContactUpdateCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContactHome;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContactPK;
import com.piramide.elwis.domain.campaignmanager.CampaignGeneration;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityContactDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;

/**
 * @author: ivan
 * Date: 01-11-2006: 11:30:22 AM
 */
public class CampaignCommunicationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            try {
                create(ctx);
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
        }
        if ("update".equals(getOp())) {
            isRead = false;
            try {
                update(ctx);
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(ctx);
        }
        if ("removeCampaignCommunication".equals(getOp())) {
            isRead = false;
            removeCampaignCommunication();
        }
        if ("setRelation".equals(getOp())) {
            isRead = false;
            setRelation();
        }
        if (isRead) {
            read(ctx);
        }
    }


    private void delete(SessionContext ctx) {
        int companyId = new Integer(paramDTO.get("companyId").toString());
        //because in CamapaignManagerAction class the companyid as String can be setting up
        paramDTO.put("companyId", companyId);
        Integer campaignId = new Integer(paramDTO.getAsString("campaignId"));
        Integer contactId = new Integer(paramDTO.getAsString("contactId"));
        CampaignActivityContactPK pk = new CampaignActivityContactPK(campaignId, contactId);

        CampaignActivityContactHome h =
                (CampaignActivityContactHome)
                        EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);
        CampaignActivityContact c = null;
        try {
            c = h.findByPrimaryKey(pk);
        } catch (FinderException e) {
            e.printStackTrace();
        }
        if (null != c) {
            //Integer companyId = c.getCompanyId();
            Integer generationId = c.getGenerationId();
            try {
                c.remove();

                ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
                paramDTO.remove("campaignId");
                contactDeleteCmd.putParam(paramDTO);
                contactDeleteCmd.putParam("contactId", contactId.toString());
                contactDeleteCmd.putParam("companyId", companyId);

                contactDeleteCmd.executeInStateless(ctx);
                if (contactDeleteCmd.getResultDTO().isFailure()) {
                    Iterator iterator = contactDeleteCmd.getResultDTO().getResultMessages();
                    while (iterator.hasNext()) {
                        resultDTO.addResultMessage((ResultMessage) iterator.next());
                    }
                    resultDTO.setResultAsFailure();
                    return;
                }

                if (isGenerationCommunicationAndDelete(generationId)) {
                    ResultDTO deleteResultDTO = deleteCampaignGeneration(generationId, ctx);
                    if (deleteResultDTO.isFailure()) {
                        resultDTO.putAll(deleteResultDTO);
                        resultDTO.setResultAsFailure();
                        resultDTO.setForward("Fail");
                    }
                }
            } catch (RemoveException e) {
                e.printStackTrace();
            }
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            resultDTO.setForward("Fail");
            return;
        }
    }

    private void read(SessionContext ctx) {
        int companyId = new Integer(paramDTO.get("companyId").toString());
        //because in CamapaignManagerAction class the companyid as String can be setting up
        paramDTO.put("companyId", companyId);
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
        Integer contactId = new Integer(paramDTO.get("contactId").toString());
        CampaignActivityContactPK pk = new CampaignActivityContactPK(campaignId, contactId);

        CampaignActivityContactHome h =
                (CampaignActivityContactHome)
                        EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);
        CampaignActivityContact c = null;
        try {
            c = h.findByPrimaryKey(pk);
        } catch (FinderException e) {
            log.debug("Object CampaignContact not found ...");
        }

        if (c != null) {
            /*if (supportContact.getActivityId() != null) {
                resultDTO.put("activityId", supportContact.getActivityId());
                SupportCaseActivity activity = (SupportCaseActivity) EJBFactory.i.findEJB(new SupportCaseActivityDTO(supportContact.getActivityId()));
                if (activity.getIsOpen().intValue() != SupportConstants.OPENTYPE_OPEN)
                    if ("true".equals(paramDTO.get("withReferences"))) {
                        resultDTO.addResultMessage("SupportCaseActivity.msg.referencedCommunication", paramDTO.get("note"));
                        resultDTO.setResultAsFailure();
                        return;
                    } else
                        resultDTO.put("READ_ONLY", "true");
            }*//**/

            ContactReadCmd contactCmd = new ContactReadCmd();
            paramDTO.remove("caseId");
            paramDTO.remove("activityId");
            contactCmd.putParam(paramDTO);
            contactCmd.executeInStateless(ctx);
            ResultDTO contactResultDTO = contactCmd.getResultDTO();
            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO((Integer) contactResultDTO.get("addressId")));
            resultDTO.put("contact", address.getName());
            resultDTO.putAll(contactResultDTO);
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            return;
        }
    }

    private void removeCampaignCommunication() {
        Integer campaignId = new Integer(paramDTO.getAsString("campaignId"));
        Integer contactId = new Integer(paramDTO.getAsString("contactId"));
        CampaignActivityContactPK pk = new CampaignActivityContactPK(campaignId, contactId);

        CampaignActivityContactHome h =
                (CampaignActivityContactHome)
                        EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);
        CampaignActivityContact c = null;
        try {
            c = h.findByPrimaryKey(pk);
            c.remove();
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
        } catch (RemoveException e) {
            resultDTO.setResultAsFailure();
        }
    }

    private EJBCommand getEJBCommand() {
        if ("true".equals(paramDTO.get("generate")) || "true".equals(paramDTO.get("build"))) {
            return new GenerateDocumentCmd();
        }

        return new ContactCreateCmd();
    }

    private void create(SessionContext ctx) throws AppLevelException {
        EJBCommand command = getEJBCommand();
        command.putParam(paramDTO);
        command.putParam("isAction", false);
        command.executeInStateless(ctx);

        ResultDTO commandResultDTO = command.getResultDTO();
        resultDTO.putAll(commandResultDTO);
        if (commandResultDTO.hasResultMessage() &&
                (commandResultDTO.isFailure() || "FailSendMail".equals(commandResultDTO.getForward()))) {
            for (Iterator iterator = commandResultDTO.getResultMessages(); iterator.hasNext();) {
                ResultMessage message = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(message);
            }
            resultDTO.setResultAsFailure();
            return;
        }

        Integer campaignId = EJBCommandUtil.i.getValueAsInteger(this, "campaignId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        List<Integer> communicationIdentifiers =
                (List<Integer>) commandResultDTO.get("communicationIdentifiers");

        log.debug("->communicationIdentifiers : " + communicationIdentifiers);
        if (null == communicationIdentifiers) {
            int contactId = (Integer) commandResultDTO.get("contactId");

            CampaignActivityContactDTO dto = new CampaignActivityContactDTO();
            dto.put("campaignId", campaignId);
            dto.put("contactId", contactId);
            dto.put("companyId", companyId);
            dto.put("activityId", paramDTO.get("activityId"));

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            return;
        }

        CampaignActivityContactDTO dto;
        for (Integer contactId : communicationIdentifiers) {
            dto = new CampaignActivityContactDTO();
            dto.put("campaignId", campaignId);
            dto.put("contactId", contactId);
            dto.put("companyId", companyId);
            dto.put("activityId", paramDTO.get("activityId"));

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        }
    }

    private void setRelation() {
        int companyId = new Integer(paramDTO.get("companyId").toString());
        int contactId = new Integer(paramDTO.get("contactId").toString());
        int campaignId = new Integer(paramDTO.get("campaignId").toString());

        CampaignActivityContactDTO dto = new CampaignActivityContactDTO();
        dto.put("campaignId", campaignId);
        dto.put("contactId", contactId);
        dto.put("companyId", companyId);
        dto.put("activityId", paramDTO.get("activityId"));

        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }


    private void update(SessionContext ctx) throws AppLevelException {
        int companyId = new Integer(paramDTO.get("companyId").toString());
        //because in CamapaignManagerAction class the companyid as String can be setting up
        //paramDTO.put("companyId", companyId);
        EJBCommand cmd = null;

        if ("true".equals(paramDTO.get("generate")) || "true".equals(paramDTO.get("build"))) {
            log.debug("Is GENERATE!!!");
            cmd = new GenerateDocumentCmd();
        } else {
            cmd = new ContactUpdateCmd();
        }
        log.debug("updateCommunication:" + paramDTO);
        cmd.putParam(paramDTO);
        cmd.executeInStateless(ctx);

        if (cmd instanceof GenerateDocumentCmd) {
            if (cmd.getResultDTO().isFailure()) {
                Iterator it = cmd.getResultDTO().getResultMessages();
                while (it.hasNext()) {
                    ResultMessage resultMessage = (ResultMessage) it.next();
                    resultDTO.addResultMessage(resultMessage);
                }
                resultDTO.setForward("Fail");
                return;
            }

            if ("Fail".equals(cmd.getResultDTO().getForward()) || null != cmd.getResultDTO().get("objNotFound")) {
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
                resultDTO.putAll(cmd.getResultDTO());
                resultDTO.setForward("Fail");
                return;
            }
        }

        if (cmd instanceof ContactUpdateCmd) {
            if (cmd.getResultDTO().isFailure()) {
                resultDTO.addResultMessage("Common.error.concurrency");
                paramDTO.setOp("read");
                read(ctx);
                resultDTO.setForward("Fail_");
            }

            if ("Fail".equals(cmd.getResultDTO().getForward()) || null != cmd.getResultDTO().get("objNotFound")) {
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Fail");
                return;
            }
        }


        log.debug("After execute Dyna CMD");

        ResultDTO contactResultDTO = cmd.getResultDTO();
        resultDTO.putAll(contactResultDTO);
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
                log.debug("related activity contacts:" + campaignGeneration.getCampaignActivityContacts().size());
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

    public boolean isStateful() {
        return false;
    }
}
