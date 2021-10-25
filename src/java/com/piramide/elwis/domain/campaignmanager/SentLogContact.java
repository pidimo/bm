package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public interface SentLogContact extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCampaignSentLogId();

    void setCampaignSentLogId(Integer campaignSentLogId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    String getErrorMessage();

    void setErrorMessage(String errorMessage);

    Integer getSentLogContactId();

    void setSentLogContactId(Integer sentLogContactId);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getVersion();

    void setVersion(Integer version);

    Long getGenerationKey();

    void setGenerationKey(Long generationKey);
}
