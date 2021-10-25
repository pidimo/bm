package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenText.java 8277 2008-06-12 00:56:40Z miguel ${NAME}.java, v1.0 30-may-2008 11:15:07 Miky Exp $
 */

public interface CampaignGenText extends EJBLocalObject {
    Integer getCampaignGenTextId();

    void setCampaignGenTextId(Integer campaignGenTextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getGenerationId();

    void setGenerationId(Integer generationId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    CampaignGeneration getCampaignGeneration();

    void setCampaignGeneration(CampaignGeneration campaignGeneration);

    CampaignFreeText getGenerationText();

    void setGenerationText(CampaignFreeText generationText);

    public void setGenerationText(EJBLocalObject generationText);

}
