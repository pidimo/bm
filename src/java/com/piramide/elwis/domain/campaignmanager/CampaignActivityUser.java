package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityUser.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v1.0 25-oct-2006 15:54:31 Miky Exp $
 */

public interface CampaignActivityUser extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUserId();

    void setUserId(Integer userId);

    CampaignActivity getActivity();

    void setActivity(CampaignActivity activity);

    Integer getPercent();

    void setPercent(Integer percent);

    Integer getVersion();

    void setVersion(Integer version);
}
