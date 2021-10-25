package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.SentLogContact;
import com.piramide.elwis.domain.campaignmanager.SentLogContactHome;
import com.piramide.elwis.dto.campaignmanager.SentLogContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class SentLogContactCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing SentLogContactCmd..... " + paramDTO);

        String op = this.getOp();
        boolean isRead = true;

        if ("create".equals(op)) {
            isRead = false;
            create();
        }
        if ("update".equals(op)) {
            isRead = false;
            update();
        }
        if ("checkForRetryOp".equals(op)) {
            isRead = false;
            checkForRetryCampaign();
        }

        if ("campaignSentLogSummary".equals(op)) {
            isRead = false;
            campaignSentLogContactSummary();
        }

        if (isRead) {
            read(sessionContext);
        }
    }

    private void create() {

        SentLogContactDTO sentLogContactDTO = new SentLogContactDTO();
        sentLogContactDTO.put("campaignSentLogId", paramDTO.get("campaignSentLogId"));
        sentLogContactDTO.put("generationKey", paramDTO.get("generationKey"));
        sentLogContactDTO.put("companyId", paramDTO.get("companyId"));
        sentLogContactDTO.put("addressId", paramDTO.get("addressId"));
        sentLogContactDTO.put("contactPersonId", paramDTO.get("contactPersonId"));
        sentLogContactDTO.put("userId", paramDTO.get("userId"));
        sentLogContactDTO.put("errorMessage", paramDTO.get("errorMessage"));
        sentLogContactDTO.put("status", paramDTO.get("status"));

        SentLogContact sentLogContact = (SentLogContact) ExtendedCRUDDirector.i.create(sentLogContactDTO, resultDTO, false);
    }

    private void read(SessionContext ctx) {
        Integer sentLogContactId = new Integer(paramDTO.get("sentLogContactId").toString());

        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences")) {
            checkReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
        }

        SentLogContactDTO sentLogContactDTO = new SentLogContactDTO(sentLogContactId);
        SentLogContact sentLogContact = (SentLogContact) ExtendedCRUDDirector.i.read(sentLogContactDTO, resultDTO, checkReferences);
    }

    private void update() {
        SentLogContactDTO sentLogContactDTO = new SentLogContactDTO(paramDTO);
        SentLogContact sentLogContact = (SentLogContact) ExtendedCRUDDirector.i.update(sentLogContactDTO, resultDTO, false, false, false, "Fail");

        if (sentLogContact != null) {
            if (CampaignConstants.SentLogContactStatus.SUCCESS.getConstant() == sentLogContact.getVersion()) {
                sentLogContact.setErrorMessage(null);
            }
        }
    }

    private void checkForRetryCampaign() {
        Boolean isRetry = Boolean.FALSE;
        Integer campaignSentLogId = new Integer(paramDTO.get("campaignSentLogId").toString());

        Integer processInBackgroundRecipients = CampaignGenerateUtil.countInBackgroundProcessCampaignSentLogRecipients(campaignSentLogId);

        if (processInBackgroundRecipients == 0) {
            Integer failedRecipients = CampaignGenerateUtil.countFailedCampaignSentLogRecipients(campaignSentLogId);
            if (failedRecipients > 0) {
                isRetry = Boolean.TRUE;
            }
        }
        resultDTO.put("isRetryCampaign", isRetry);
    }

    private void campaignSentLogContactSummary() {
        Integer campaignSentLogId = new Integer(paramDTO.get("campaignSentLogId").toString());

        SentLogContactHome sentLogContactHome = (SentLogContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_SENTLOGCONTACT);

        Integer totalContacts;
        Integer totalSuccess;
        try {
            totalContacts = sentLogContactHome.selectCountByCampaignSentLogId(campaignSentLogId);
        } catch (FinderException e) {
            totalContacts = 0;
        }

        try {
            totalSuccess = sentLogContactHome.selectCountByCampaignSentLogIdSuccess(campaignSentLogId);
        } catch (FinderException e) {
            totalSuccess = 0;
        }

        resultDTO.put("totalContacts", totalContacts);
        resultDTO.put("totalSuccess", totalSuccess);
    }

    public boolean isStateful() {
        return false;
    }
}
