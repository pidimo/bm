/**
 * @author: ivan
 * Date: 01-11-2006: 01:14:24 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignActivityContact extends EJBLocalObject {
    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getTaskId();

    void setTaskId(Integer taskId);

    String getFromEmail();

    void setFromEmail(String fromEmail);

    Integer getGenerationId();

    void setGenerationId(Integer generationId);

    CampaignGeneration getCampaignGeneration();

    void setCampaignGeneration(CampaignGeneration campaignGeneration);
}
