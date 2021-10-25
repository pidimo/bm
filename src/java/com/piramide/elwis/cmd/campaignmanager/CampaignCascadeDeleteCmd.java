package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.domain.schedulermanager.TaskHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Cmd to delete an campaign in cascade. This remove all relations
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.2
 */
public class CampaignCascadeDeleteCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public int getTransactionTimeout() {
        return 18000;//five hours as limit
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CampaignCascadeDeleteCmd........" + paramDTO);
        String op = this.getOp();

        if ("campaignCascadeDelete".equals(op)) {
            Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
            deleteCampaign(campaignId, ctx);

        } else if ("cascadeDeleteByRange".equals(op)) {
            deleteCampaignsByRangeDates(ctx);
        }
    }

    private void deleteCampaignsByRangeDates(SessionContext ctx) {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer startRecordDate = new Integer(paramDTO.get("startRecordDate").toString());
        Integer endRecordDate = new Integer(paramDTO.get("endRecordDate").toString());

        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        Collection collection;
        try {
            collection = campaignHome.findByCompanyIdAndRecordDate(companyId, startRecordDate, endRecordDate);
        } catch (FinderException e) {
            log.debug("Error in find campaigns by record date....", e);
            collection = new ArrayList();
        }

        List<String> campaignNamesList = new ArrayList<String>();

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Campaign campaign = (Campaign) iterator.next();
            String campaignName = campaign.getCampaignName();

            deleteCampaign(campaign.getCampaignId(), ctx);

            if (existErrorsInDelete()) {
                break;
            } else {
                campaignNamesList.add(campaignName);
            }
        }

        if (existErrorsInDelete()) {
            log.info("Some errors occur when trying delete campaigns by record date...");
            logResultMessages(resultDTO);

            resultDTO.setForward("Fail");
            ctx.setRollbackOnly();
        } else {
            resultDTO.put("deletedCampaignList", campaignNamesList);
            resultDTO.put("totalCampaignDeleted", campaignNamesList.size());
        }
    }

    /**
     * Delete an campaign and all relations
     * @param campaignId id
     * @param ctx context
     */
    private void deleteCampaign(Integer campaignId, SessionContext ctx) {

        paramDTO.remove("op");
        Campaign campaign = findCampaign(campaignId);

        if (campaign != null) {
            log.debug("Removing campaign in cascade....... " + campaign.getCampaignName());

            deleteCampActivityContacts(campaign, ctx);
            deleteCampGenerationSentLogs(campaign, ctx);
            disableCampaignActivityRelations(campaign);

            CampaignDeleteCmd campaignDeleteCmd = new CampaignDeleteCmd();
            campaignDeleteCmd.setOp("delete");
            campaignDeleteCmd.putParam(paramDTO);
            campaignDeleteCmd.putParam("campaignId", campaignId);
            campaignDeleteCmd.putParam("isCampaignCascadeDelete", "true");
            campaignDeleteCmd.executeInStateless(ctx);

            EJBCommandUtil.i.processCmdResultDTOErrorMessages(resultDTO, campaignDeleteCmd.getResultDTO());

            //check errors, and rollback
            if (existErrorsInDelete()) {
                log.info("Some errors occur when trying delete campaign in cascade");
                logResultMessages(resultDTO);

                resultDTO.setForward("Fail");
                ctx.setRollbackOnly();
            }
        }
    }

    /**
     * Remove all campaign communications
     * @param campaign
     * @param ctx
     */
    private void deleteCampActivityContacts(Campaign campaign, SessionContext ctx) {
        if (existErrorsInDelete()) {
            return;
        }

        CampaignActivityContactHome campaignActivityContactHome = (CampaignActivityContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNACTIVITYCONTACT);
        Collection collection = null;
        try {
            collection = campaignActivityContactHome.findByCampaignId(campaign.getCampaignId());
        } catch (FinderException e) {
            log.debug("Error in find campaignActivityContact by campaing " + campaign.getCampaignId(), e);
            collection = new ArrayList();
        }

        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            CampaignActivityContact campaignActivityContact = (CampaignActivityContact) iterator.next();

            CampaignCommunicationCmd campaignCommunicationCmd = new CampaignCommunicationCmd();
            campaignCommunicationCmd.setOp("delete");
            campaignCommunicationCmd.putParam("companyId", campaignActivityContact.getCompanyId());
            campaignCommunicationCmd.putParam("campaignId", campaignActivityContact.getCampaignId().toString());
            campaignCommunicationCmd.putParam("contactId", campaignActivityContact.getContactId().toString());
            campaignCommunicationCmd.executeInStateless(ctx);

            EJBCommandUtil.i.processCmdResultDTOErrorMessages(resultDTO, campaignCommunicationCmd.getResultDTO());
        }
    }

    private void deleteCampGenerationSentLogs(Campaign campaign, SessionContext ctx) {
        if (existErrorsInDelete()) {
            return;
        }

        CampaignGenerationHome generationHome = (CampaignGenerationHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNGENERATION);
        Collection campGenerations = null;
        try {
            campGenerations = generationHome.findByCampaignId(campaign.getCampaignId());
        } catch (FinderException e) {
            log.debug("Error in execute finder by campaign..." + campaign.getCampaignId(), e);
            campGenerations = new ArrayList();
        }

        for (Iterator iterator = campGenerations.iterator(); iterator.hasNext(); ) {
            CampaignGeneration campaignGeneration = (CampaignGeneration) iterator.next();

            CampaignSentLog campaignSentLog = campaignGeneration.getCampaignSentLog();
            if (campaignSentLog != null) {
                CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
                campaignSentLogCmd.setOp("delete");
                campaignSentLogCmd.putParam("campaignSentLogId", campaignSentLog.getCampaignSentLogId());
                campaignSentLogCmd.putParam("generationId", campaignSentLog.getGenerationId());

                campaignSentLogCmd.executeInStateless(ctx);

                EJBCommandUtil.i.processCmdResultDTOErrorMessages(resultDTO, campaignSentLogCmd.getResultDTO());
            }
        }
    }

    private void disableCampaignActivityRelations(Campaign campaign) {
        if (existErrorsInDelete()) {
            return;
        }

        for (Iterator iterator = campaign.getCampaignActivity().iterator(); iterator.hasNext(); ) {
            CampaignActivity campaignActivity = (CampaignActivity) iterator.next();

            disableCampaignActivityTask(campaignActivity.getActivityId());
            disableCampaignActivitySalesProcess(campaignActivity.getActivityId());
        }
    }

    private void disableCampaignActivityTask(Integer activityId) {
        TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);
        if (activityId != null) {
            Collection activityTasks;
            try {
                activityTasks = taskHome.findByActivityId(activityId);
            } catch (FinderException e) {
                activityTasks = new ArrayList();
            }

            for (Iterator iterator = activityTasks.iterator(); iterator.hasNext(); ) {
                Task task = (Task) iterator.next();
                task.setActivityId(null);
            }
        }
    }

    private void disableCampaignActivitySalesProcess(Integer activityId) {
        SalesProcessHome salesProcessHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        if (activityId != null) {
            Collection activitySalesProcess;
            try {
                activitySalesProcess = salesProcessHome.findByActivityId(activityId);
            } catch (FinderException e) {
                activitySalesProcess = new ArrayList();
            }

            for (Iterator iterator = activitySalesProcess.iterator(); iterator.hasNext(); ) {
                SalesProcess salesProcess = (SalesProcess) iterator.next();
                salesProcess.setActivityId(null);
            }
        }
    }

    private Campaign findCampaign(Integer campaignId) {
        CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
        if (campaignId != null) {
            try {
                return campaignHome.findByPrimaryKey(campaignId);
            } catch (FinderException e) {
                log.error("Not found campaign " + campaignId, e);
            }
        }
        return null;
    }

    private void logResultMessages(ResultDTO myResultDTO) {
        if (myResultDTO != null && myResultDTO.hasResultMessage()) {
            for (Iterator iterator = myResultDTO.getResultMessages(); iterator.hasNext(); ) {
                ResultMessage message = (ResultMessage) iterator.next();
                log.info("Error key: " + message.getKey());
            }
        }
    }

    private boolean existErrorsInDelete() {
        return resultDTO.hasResultMessage();
    }


    public boolean isStateful() {
        return false;
    }
}
