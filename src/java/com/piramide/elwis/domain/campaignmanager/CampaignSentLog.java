package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public interface CampaignSentLog extends EJBLocalObject {
    Integer getCampaignSentLogId();

    void setCampaignSentLogId(Integer campaignSentLogId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGenerationId();

    void setGenerationId(Integer generationId);

    Integer getTotalSent();

    void setTotalSent(Integer totalSent);

    Integer getTotalSuccess();

    void setTotalSuccess(Integer totalSuccess);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getSentLogContacts();

    void setSentLogContacts(Collection sentLogContacts);

    CampaignGeneration getCampaignGeneration();

    void setCampaignGeneration(CampaignGeneration campaignGeneration);

    Long getGenerationKey();

    void setGenerationKey(Long generationKey);
}
