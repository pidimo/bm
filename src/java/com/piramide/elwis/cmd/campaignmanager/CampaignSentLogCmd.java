package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.cmd.contactmanager.utils.AddressUtil;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.dto.campaignmanager.CampaignGenerationDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignSentLogDTO;
import com.piramide.elwis.dto.campaignmanager.SentLogContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignSentLogCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing CampaignSentLogCmd..... " + paramDTO);
        Integer campaignSentLogId = null;

        String op = this.getOp();
        boolean isRead = true;

        if ("create".equals(op)) {
            isRead = false;
            campaignSentLogId = create();
        }
        if ("update".equals(op)) {
            isRead = false;
            update();
        }
        if ("delete".equals(op)) {
            isRead = false;
            delete(sessionContext);
        }

        if ("generationStatus".equals(op)) {
            isRead = false;
            viewGenerationStatus();
        }

        if (isRead) {
            read(sessionContext);
        }

        if (campaignSentLogId != null) {
            resultDTO.put("campaignSentLogId", campaignSentLogId);
        }
    }

    private Integer create() {
        Integer campaignSentLogId = null;

        CampaignSentLogDTO campaignSentLogDTO = new CampaignSentLogDTO(paramDTO);
        CampaignSentLog campaignSentLog = (CampaignSentLog) ExtendedCRUDDirector.i.create(campaignSentLogDTO, resultDTO, false);

        if (campaignSentLog != null) {
            campaignSentLogId = campaignSentLog.getCampaignSentLogId();
        }

        return campaignSentLogId;
    }

    private void read(SessionContext ctx) {
        Integer campaignSentLogId = new Integer(paramDTO.get("campaignSentLogId").toString());

        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences")) {
            checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
        }

        CampaignSentLogDTO campaignSentLogDTO = new CampaignSentLogDTO(campaignSentLogId);
        CampaignSentLog campaignSentLog = (CampaignSentLog) ExtendedCRUDDirector.i.read(campaignSentLogDTO, resultDTO, checkReferences);

        if (campaignSentLog != null) {
            CampaignGeneration generation = campaignSentLog.getCampaignGeneration();
            resultDTO.put("generationTime", generation.getGenerationTime());
            resultDTO.put("userName", AddressUtil.i.getAddressName(generation.getUserId(), ctx));
            resultDTO.put("activityName", readActivityName(generation.getActivityId()));
        }
    }

    private String readActivityName(Integer activityId) {
        String name = "";
        if (activityId != null) {
            CampaignActivityHome activityHome = (CampaignActivityHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_ACTIVITY);
            try {
                CampaignActivity activity = activityHome.findByPrimaryKey(activityId);
                name = activity.getTitle();
            } catch (FinderException e) {
                log.debug("Error in find campaign activity...",e);
            }
        }
        return name;
    }

    private void update() {
        CampaignSentLogDTO campaignSentLogDTO = new CampaignSentLogDTO(paramDTO);
        CampaignSentLog campaignSentLog = (CampaignSentLog) ExtendedCRUDDirector.i.update(campaignSentLogDTO, resultDTO, false, false, false, "Fail");
        if (campaignSentLog != null) {
            if (!resultDTO.isFailure()) {
                updateTotalsSummary(campaignSentLog);
            }
        }
    }

    private void updateSentLogContact(List<Map> contactInfoList) {

        for (Map contactMap : contactInfoList) {
            SentLogContactDTO sentLogContactDTO = new SentLogContactDTO();
            sentLogContactDTO.put("sentLogContactId", contactMap.get("sentLogContactId"));
            sentLogContactDTO.put("errorMessage", contactMap.get("errorMessage"));
            sentLogContactDTO.put("status", contactMap.get("status"));

            SentLogContact sentLogContact = (SentLogContact) ExtendedCRUDDirector.i.update(sentLogContactDTO, resultDTO, false, false, false, "Fail");
        }
    }

    private void delete(SessionContext ctx) {
        Integer campaignSentLogId = new Integer(paramDTO.get("campaignSentLogId").toString());
        Integer generationId = new Integer(paramDTO.get("generationId").toString());

        ExtendedCRUDDirector.i.delete(new CampaignSentLogDTO(campaignSentLogId), resultDTO, false, "Fail");
        if (!resultDTO.isFailure()) {
            if (isGenerationDelete(generationId)) {
                deleteCampaignGeneration(generationId, ctx);
            }
        }
    }

    private boolean isGenerationDelete(Integer generationId) {
        if (generationId != null) {
            CampaignGeneration campaignGeneration = (CampaignGeneration) ExtendedCRUDDirector.i.read(new CampaignGenerationDTO(generationId), new ResultDTO(), false);
            if (campaignGeneration != null) {
                log.debug("related activity contacts:" + campaignGeneration.getCampaignActivityContacts().size());
                return campaignGeneration.getCampaignActivityContacts().isEmpty() && campaignGeneration.getCampaignSentLog() == null;
            }
        }
        return false;
    }

    private ResultDTO deleteCampaignGeneration(Integer generationId, SessionContext ctx) {
        CampaignGenerationDeleteCmd generationDeleteCmd = new CampaignGenerationDeleteCmd();
        generationDeleteCmd.putParam("generationId", generationId);
        generationDeleteCmd.executeInStateless(ctx);
        return generationDeleteCmd.getResultDTO();
    }

    private void updateTotalsSummary(CampaignSentLog campaignSentLog) {
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        Collection sentContactSuccess = null;
        try {
            sentContactSuccess = sentLogContactHome.findByCampaignSentLogIdStatus(campaignSentLog.getCampaignSentLogId(), CampaignConstants.SentLogContactStatus.SUCCESS.getConstant());
        } catch (FinderException e) {
           sentContactSuccess = new ArrayList();
        }

        campaignSentLog.setTotalSent(campaignSentLog.getSentLogContacts().size());
        campaignSentLog.setTotalSuccess(sentContactSuccess.size());
    }

    private void viewGenerationStatus() {
        Long generationKey = new Long(paramDTO.get("generationKey").toString());

        CampaignSentLogHome campaignSentLogHome = (CampaignSentLogHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNSENTLOG);
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);

        Integer totalSent = null;
        Integer totalSuccess = null;
        Integer totalFail = null;

        try {
            totalSent = campaignSentLogHome.selectTotalSentByGenerationKey(generationKey);
        } catch (FinderException e) {
            log.debug("Error in find total sent...", e);
        }

        try {
            totalSuccess = sentLogContactHome.selectCountByGenerationKeySuccess(generationKey);
        } catch (FinderException e) {
            log.debug("Error in count success...", e);
            totalSuccess = 0;
        }

        try {
            totalFail = sentLogContactHome.selectCountByGenerationKeyFailed(generationKey);
        } catch (FinderException e) {
            log.debug("Error in count fail...", e);
            totalFail = 0;
        }

        if (totalSent != null) {
            resultDTO.put("totalSent", totalSent);
            resultDTO.put("totalSuccess", totalSuccess);
            resultDTO.put("totalFail", totalFail);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
