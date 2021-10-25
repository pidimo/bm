package com.piramide.elwis.service.campaign;

import com.piramide.elwis.cmd.campaignmanager.CampaignBackgroundEmailSendCmd;
import com.piramide.elwis.cmd.campaignmanager.CampaignBackgroundLightEmailSendCmd;
import com.piramide.elwis.cmd.campaignmanager.CampaignGenerationCmd;
import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.service.campaign.utils.CampaignBackgroundNotificationMessage;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.campaign.CampaignBackgroundProcessUtil;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignSendBackgroundServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public CampaignSendBackgroundServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public Integer findSentLogContactIdToProcess() {
        Integer sentLogContactId = null;
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        try {
            sentLogContactId = sentLogContactHome.selectMinSentLogContactIdByStatus(CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.getConstant());
        } catch (FinderException e) {
            log.debug("Not found Sent log contact id..", e);
        }

        return sentLogContactId;
    }

    public boolean updateSentLogContactStatusToProcess(Integer sentLogContactId) {
        boolean isSuccess = false;
        SentLogContact sentLogContact = null;
        try {
            sentLogContact = findSentLogContact(sentLogContactId);
        } catch (FinderException e) {
            log.debug("Not found sentLogContact:" + sentLogContactId, e);
        }

        if (sentLogContact != null) {
            UserTransaction transaction = ctx.getUserTransaction();
            try {
                transaction.begin();
                sentLogContact.setStatus(CampaignConstants.SentLogContactStatus.SENDING_IN_BACKGROUND.getConstant());
                transaction.commit();
                isSuccess = true;
            } catch (Exception e) {
                log.error("Error in update sent log contact status to sending..", e);
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    log.debug("Error in rollback", e1);
                }
            }
        }

        return isSuccess;
    }

    public void backgroundProcess(Integer sentLogContactId) {

        if (sentLogContactId != null) {
            log.debug("Init send campaign in backgroun for sentLogContactId...................  " + sentLogContactId);
            CampaignBackgroundProcessUtil.i.addInProgressItem(sentLogContactId);

            try {
                sendCampaign(sentLogContactId);
                verifySentLogContactStatus(sentLogContactId);
            } catch (Exception e) {
                log.error("Error in process SentLogContact in background: " + sentLogContactId, e);
                updateSentLogContactStatusToFail(sentLogContactId);
            }

            //ending the send process
            endingBackgroundProcess(sentLogContactId);

            CampaignBackgroundProcessUtil.i.removeInProgressItem(sentLogContactId);
        }
    }

    private void sendCampaign(Integer sentLogContactId) throws FinderException, SystemException {
        ParamDTO params = readGenerationInfo(sentLogContactId);

        ResultDTO resultDTO = null;
        if (isFromActivity(params)) {
            //execute cmd without transaction
            CampaignBackgroundEmailSendCmd activitySendCmd = new CampaignBackgroundEmailSendCmd();
            activitySendCmd.putParam(params);
            activitySendCmd.executeInStateless(ctx);
            resultDTO = activitySendCmd.getResultDTO();
        } else {
            //execute cmd without transaction
            CampaignBackgroundLightEmailSendCmd sendCmd = new CampaignBackgroundLightEmailSendCmd();
            sendCmd.putParam(params);
            sendCmd.executeInStateless(ctx);
            resultDTO = sendCmd.getResultDTO();
        }

        log.debug("Result DTO......." + resultDTO);
    }

    private void endingBackgroundProcess(Integer sentLogContactId) {
        SentLogContact sentLogContact = null;
        try {
            sentLogContact = findSentLogContact(sentLogContactId);
        } catch (FinderException e) {
            log.error("Not found contact for ending send process..  sentLogContactId:" + sentLogContactId, e);
        }

        if (sentLogContact != null) {
            SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
            Integer remainToProcess = null;
            try {
                remainToProcess = sentLogContactHome.selectCountByCampaignSentLogIdProcessInBackground(sentLogContact.getCampaignSentLogId());
            } catch (FinderException e) {
                log.debug("Error in count remain contact to process..", e);
            }

            if (remainToProcess != null && remainToProcess == 0) {
                log.debug("Generation in background completed!!! send notification...");
                sendCompletedNotificationEmail(sentLogContact);
            }
        }
    }

    private void sendCompletedNotificationEmail(SentLogContact sentLogContact) {

        try {
            CampaignSentLog campaignSentLog = findCampaignSentLog(sentLogContact.getCampaignSentLogId());
            CampaignGeneration campaignGeneration = findCampaignGeneration(campaignSentLog.getGenerationId());
            User user = findUser(campaignGeneration.getUserId());

            DateTimeZone userDateTimeZone = DateTimeZone.getDefault();
            if (user.getTimeZone() != null) {
                userDateTimeZone = DateTimeZone.forID(user.getTimeZone());
            }

            CampaignBackgroundNotificationMessage notificationMessage = new CampaignBackgroundNotificationMessage(user.getFavoriteLanguage());
            notificationMessage.setDateTimeZone(userDateTimeZone);
            notificationMessage.setGenerationTime(campaignGeneration.getGenerationTime());
            notificationMessage.setTotalRecipients(campaignSentLog.getTotalSent());
            notificationMessage.setTotalSuccess(campaignSentLog.getTotalSuccess());
            notificationMessage.setTotalFail(campaignSentLog.getTotalSent() - campaignSentLog.getTotalSuccess());

            String from = ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender");

            Email email = new Email(campaignGeneration.getNotificationMail(), from, notificationMessage.getSubject(),
                    notificationMessage.getMessage(), "text/plain", userDateTimeZone.toTimeZone());

            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);

            //finally delete cache of campaign mail attach
            ElwisCacheManager.deleteCampaignMailAttachFolder(campaignGeneration.getCompanyId(), campaignGeneration.getCampaignId(), campaignGeneration.getUserId(), campaignSentLog.getGenerationKey());
        } catch (Exception e) {
            log.info("Error in send notification email to campaign sent in background... ", e);
        }
    }

    private ParamDTO readGenerationInfo(Integer sentLogContactId) throws FinderException, SystemException {
        ParamDTO paramDTO = new ParamDTO();

        SentLogContact sentLogContact = findSentLogContact(sentLogContactId);
        CampaignSentLog campaignSentLog = findCampaignSentLog(sentLogContact.getCampaignSentLogId());

        ResultDTO resultDTO = null;

        UserTransaction transaction = ctx.getUserTransaction();
        try {
            transaction.begin();
            //transaction is required for execute this cmd
            CampaignGenerationCmd generationCmd = new CampaignGenerationCmd();
            generationCmd.putParam("generationId", campaignSentLog.getGenerationId());
            generationCmd.executeInStateless(ctx);
            resultDTO = generationCmd.getResultDTO();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error in read generation configuration..", e);
        }

        if (resultDTO != null && !resultDTO.isFailure()) {
            paramDTO.put("sentLogContactId", sentLogContactId);
            paramDTO.putAll(resultDTO);

            //process attach if exist
            String ids = "";
            if (resultDTO.containsKey("genAttachmentList")) {
                List<Map> sourceAttachmentList = (List<Map>) resultDTO.get("genAttachmentList");
                for (Map attachmentMap : sourceAttachmentList) {
                    //compose ids
                    if (ids.length() > 0) {
                        ids = ids + "," + attachmentMap.get("attachmentId").toString();
                    } else {
                        ids = attachmentMap.get("attachmentId").toString();
                    }
                }
            }
            paramDTO.put("listIdAttach", ids);

            //read user address id
            Integer userId = new Integer(resultDTO.get("userId").toString());
            paramDTO.put("userAddressId", readUserAddressId(userId));
        }

        return paramDTO;
    }

    private boolean isFromActivity(ParamDTO params) {
        return params.get("activityId") != null && !"".equals(params.get("activityId").toString());
    }


    private void updateSentLogContactStatusToFail(Integer sentLogContactId) {
        SentLogContact sentLogContact = null;
        try {
            sentLogContact = findSentLogContact(sentLogContactId);
        } catch (FinderException e) {
        }

        if (sentLogContact != null) {
            UserTransaction transaction = ctx.getUserTransaction();
            try {
                transaction.begin();

                if (CampaignConstants.SentLogContactStatus.SENDING_IN_BACKGROUND.equal(sentLogContact.getStatus())
                        || CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.equal(sentLogContact.getStatus())) {

                    sentLogContact.setStatus(CampaignConstants.SentLogContactStatus.FAILED_UNKNOWN.getConstant());
                }

                transaction.commit();
            } catch (Exception e) {
                log.error("Error in update sent log contact status to Fail..", e);
                try {
                    transaction.rollback();
                } catch (SystemException e1) {
                }
            }
        }
    }

    private void verifySentLogContactStatus(Integer sentLogContactId) throws Exception {
        SentLogContact sentLogContact = findSentLogContact(sentLogContactId);

        if (CampaignConstants.SentLogContactStatus.SENDING_IN_BACKGROUND.equal(sentLogContact.getStatus())
                || CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.equal(sentLogContact.getStatus())) {
            updateSentLogContactStatusToFail(sentLogContactId);
        }
    }

    private SentLogContact findSentLogContact(Integer sentLogContactId) throws FinderException {
        SentLogContact sentLogContact = null;
        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);
        sentLogContact = sentLogContactHome.findByPrimaryKey(sentLogContactId);
        return sentLogContact;
    }

    private CampaignSentLog findCampaignSentLog(Integer campaignSentLogId) throws FinderException {
        CampaignSentLog campaignSentLog = null;
        CampaignSentLogHome campaignSentLogHome = (CampaignSentLogHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNSENTLOG);
        campaignSentLog = campaignSentLogHome.findByPrimaryKey(campaignSentLogId);
        return campaignSentLog;
    }

    private CampaignGeneration findCampaignGeneration(Integer generationId) throws FinderException {
        CampaignGeneration campaignGeneration = null;
        CampaignGenerationHome generationHome = (CampaignGenerationHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNGENERATION);
        campaignGeneration = generationHome.findByPrimaryKey(generationId);
        return campaignGeneration;
    }

    private User findUser(Integer userId) throws FinderException {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        user = userHome.findByPrimaryKey(userId);
        return user;
    }

    private Integer readUserAddressId(Integer userId) throws FinderException {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User user = userHome.findByPrimaryKey(userId);
        return user.getAddressId();
    }

}
