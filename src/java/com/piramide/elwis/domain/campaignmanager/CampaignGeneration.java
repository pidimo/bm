package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGeneration.java 10517 2015-02-26 01:45:04Z miguel ${NAME}.java, v1.0 30-may-2008 11:03:24 Miky Exp $
 */

public interface CampaignGeneration extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getGenerationId();

    void setGenerationId(Integer generationId);

    Long getGenerationTime();

    void setGenerationTime(Long generationTime);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getUserId();

    void setUserId(Integer userId);

    Collection getCampaignActivityContacts();

    void setCampaignActivityContacts(Collection campaignActivityContacts);

    Collection getCampaignGenTexts();

    void setCampaignGenTexts(Collection campaignGenTexts);

    Collection getGenerationAttachs();

    void setGenerationAttachs(Collection generationAttachs);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Campaign getCampaign();

    void setCampaign(Campaign campaign);

    Boolean getCreateComm();

    void setCreateComm(Boolean createComm);

    Integer getDocumentType();

    void setDocumentType(Integer documentType);

    String getEmployeeMail();

    void setEmployeeMail(String employeeMail);

    Integer getSenderEmployeeId();

    void setSenderEmployeeId(Integer senderEmployeeId);

    String getSenderPrefix();

    void setSenderPrefix(String senderPrefix);

    Integer getSenderPrefixType();

    void setSenderPrefixType(Integer senderPrefixType);

    String getSubject();

    void setSubject(String subject);

    Integer getTelecomTypeId();

    void setTelecomTypeId(Integer telecomTypeId);

    CampaignSentLog getCampaignSentLog();

    void setCampaignSentLog(CampaignSentLog campaignSentLog);

    String getRequestLocale();

    void setRequestLocale(String requestLocale);

    String getNotificationMail();

    void setNotificationMail(String notificationMail);
}
