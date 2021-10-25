/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignContact.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 17-jun-2004 16:15:11 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignContact extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getCampaignContactId();

    void setCampaignContactId(Integer campaignContactId);

    Integer getActivityId();

    void setActivityId(Integer activityId);

    Boolean getActive();

    void setActive(Boolean active);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getVersion();

    void setVersion(Integer version);

    CampaignActivity getCampaignActivity();

    void setCampaignActivity(CampaignActivity campaignActivity);
}
