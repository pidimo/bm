/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: Campaign.java 10205 2012-05-02 20:01:09Z miguel ${NAME}.java, v 2.0 17-jun-2004 16:07:47 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public interface Campaign extends EJBLocalObject {

    /*String getRemarkValue();

    void setRemarkValue(String remarkValue);*/

    Integer getAddressType();

    void setAddressType(Integer addressType);

    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    String getCampaignName();

    void setCampaignName(String campaignName);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactType();

    void setContactType(Integer contactType);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    Boolean getIncludePartner();

    void setIncludePartner(Boolean includePartner);

    Integer getNumberContacts();

    void setNumberContacts(Integer numberContacts);

    Integer getRecordDate();

    void setRecordDate(Integer recordDate);

    Integer getRemark();

    void setRemark(Integer remark);

    Integer getStartDate();

    void setStartDate(Integer startDate);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getText();

    void setText(Integer text);

    Boolean getIsDouble();

    void setIsDouble(Boolean isDouble);

    public void setStartDateField(Date value);

    public Date getStartDateField();

    public void setRecordDateField(Date value);

    public Date getRecordDateField();

    Integer getVersion();

    void setVersion(Integer version);

    Collection getCampaignContact();

    void setCampaignContact(Collection campaignContact);

    Collection getCampaignText();

    void setCampaignText(Collection campaignText);

    Collection getCampaignCriterion();

    void setCampaignCriterion(Collection campaignCriterion);

    CampaignFreeText getCampaignFreeText();

    void setCampaignFreeText(CampaignFreeText campaignFreeText);

    java.math.BigDecimal getAwaitedUtility();

    void setAwaitedUtility(java.math.BigDecimal awaitedUtility);

    java.math.BigDecimal getBudgetCost();

    void setBudgetCost(BigDecimal budgetcost);

    Integer getChangeDate();

    void setChangeDate(Integer changeDate);

    Integer getCloseDate();

    void setCloseDate(Integer closeDate);

    java.math.BigDecimal getRealCost();

    void setRealCost(java.math.BigDecimal realCost);

    Integer getTypeId();

    void setTypeId(Integer typeId);

    Collection getCampaignActivity();

    void setCampaignActivity(Collection campaignActivity);

    Collection getAttach();

    void setAttach(Collection attach);

    Collection getCampaignTemplate();

    void setCampaignTemplate(Collection campaignTemplate);

    Integer getTotalHits();

    void setTotalHits(Integer totalHits);

    Integer getHasEmail();

    void setHasEmail(Integer hasEmail);

    Integer getHasEmailTelecomType();

    void setHasEmailTelecomType(Integer hasEmailTelecomType);
}
