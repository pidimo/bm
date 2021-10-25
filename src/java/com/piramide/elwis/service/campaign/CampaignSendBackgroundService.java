package com.piramide.elwis.service.campaign;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public interface CampaignSendBackgroundService extends EJBLocalObject {

    public void backgroundProcess(Integer sentLogContactId);

    public Integer findSentLogContactIdToProcess();

    public boolean updateSentLogContactStatusToProcess(Integer sentLogContactId);

}