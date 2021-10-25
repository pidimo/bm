/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterion.java 2374 2004-08-22 21:29:19Z mauren ${NAME}.java, v 2.0 17-jun-2004 16:22:21 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignCriterion extends EJBLocalObject {
    Integer getCampaignId();

    void setCampaignId(Integer campaignId);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCampaignCriterionId();

    void setCampaignCriterionId(Integer campaignCriterionId);

    Integer getNumberHits();

    void setNumberHits(Integer numberHits);

    String getOperator();

    void setOperator(String operator);

    Integer getValueId();

    void setValueId(Integer value);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getCampCriterionValueId();

    void setCampCriterionValueId(Integer campcriterionvalueid);

    CampaignFreeText getCampaignFreeText();

    void setCampaignFreeText(CampaignFreeText campaignFreeText);

}
