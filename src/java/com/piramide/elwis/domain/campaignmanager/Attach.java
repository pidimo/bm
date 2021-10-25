/**
 * @author: ivan
 * Date: 25-10-2006: 11:21:53 AM
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface Attach extends EJBLocalObject {
    Integer getAttachId();

    void setAttachId(Integer attachId);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    String getComment();

    void setComment(String comment);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getFilename();

    void setFilename(String filename);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getSize();

    void setSize(Integer size);

    Integer getVersion();

    void setVersion(Integer version);

    CampaignFreeText getCampaignFreeText();

    void setCampaignFreeText(CampaignFreeText campaignFreeText);
}
