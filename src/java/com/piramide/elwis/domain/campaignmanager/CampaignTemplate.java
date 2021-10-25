/**
 * @author: ivan
 * Date: 30-10-2006: 03:55:54 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface CampaignTemplate extends EJBLocalObject {
    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getDescription();

    void setDescription(String description);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getDocumentType();

    void setDocumentType(Integer documentType);

    Collection getCampaignText();

    void setCampaignText(Collection campaignText);
}
