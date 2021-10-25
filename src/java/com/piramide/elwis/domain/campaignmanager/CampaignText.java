/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignText.java 10515 2015-01-23 22:17:00Z miguel ${NAME}.java, v 2.0 03-ago-2004 20:17:19 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignText extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getByDefault();

    void setByDefault(Boolean byDefault);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);


    Integer getVersion();

    void setVersion(Integer version);

    CampaignFreeText getCampaignFreeText();

    void setCampaignFreeText(CampaignFreeText campaignFreeText);

    void setTemplateId(Integer templateId);

    Integer getTemplateId();

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getSize();

    void setSize(Integer size);
}
