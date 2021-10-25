package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenAttach.java 8277 2008-06-12 00:56:40Z miguel ${NAME}.java, v1.0 30-may-2008 11:29:38 Miky Exp $
 */

public interface CampaignGenAttach extends EJBLocalObject {
    Integer getAttachmentId();

    void setAttachmentId(Integer attachmentId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGenerationId();

    void setGenerationId(Integer generationId);

    CampaignGeneration getCampaignGeneration();

    void setCampaignGeneration(CampaignGeneration campaignGeneration);
}
