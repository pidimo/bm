package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivity.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v1.0 13-oct-2006 18:43:20 Miky Exp $
 */

public interface CampaignActivity extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getCloseDate();

    void setCloseDate(Integer closeDate);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    BigDecimal getCost();

    void setCost(BigDecimal cost);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getNumberContact();

    void setNumberContact(Integer numberContact);

    Integer getPercent();

    void setPercent(Integer percent);

    Integer getStartDate();

    void setStartDate(Integer startdate);

    Integer getState();

    void setState(Integer state);

    String getTitle();

    void setTitle(String title);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getCampaignContacts();

    void setCampaignContacts(Collection campaignContacts);

    CampaignFreeText getCampaignFreeText();

    void setCampaignFreeText(CampaignFreeText campaignFreeText);

    public void setCampaignFreeText(EJBLocalObject campaignFreeText);

    Collection getActivityUsers();

    void setActivityUsers(Collection activityUsers);
}
