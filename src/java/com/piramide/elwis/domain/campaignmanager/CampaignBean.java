/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignBean.java 10205 2012-05-02 20:01:09Z miguel ${NAME}.java, v 2.0 17-jun-2004 16:07:48 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public abstract class CampaignBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setCampaignId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGN));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }


    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }


    public abstract Integer getAddressType();

    public abstract void setAddressType(Integer addressType);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract String getCampaignName();

    public abstract void setCampaignName(String campaignName);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactType();

    public abstract void setContactType(Integer contactType);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract Boolean getIncludePartner();

    public abstract void setIncludePartner(Boolean includePartner);

    public abstract Integer getNumberContacts();

    public abstract void setNumberContacts(Integer numberContacts);

    public abstract Integer getRecordDate();

    public abstract void setRecordDate(Integer recordDate);

    public abstract Integer getRemark();

    public abstract void setRemark(Integer remark);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startDate);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getText();

    public abstract void setText(Integer text);

    public abstract Boolean getIsDouble();

    public abstract void setIsDouble(Boolean isDouble);

    public void setStartDateField(Date value) {
        setStartDate(DateUtils.dateToInteger(value));
    }

    public Date getStartDateField() {
        return DateUtils.integerToDate(getStartDate());
    }

    public void setRecordDateField(Date value) {
        setRecordDate(DateUtils.dateToInteger(value));
    }

    public Date getRecordDateField() {
        return DateUtils.integerToDate(getRecordDate());
    }

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getCampaignContact();

    public abstract void setCampaignContact(Collection campaignContact);

    public abstract Collection getCampaignText();

    public abstract void setCampaignText(Collection campaignText);

    public abstract Collection getCampaignCriterion();

    public abstract void setCampaignCriterion(Collection campaignCriterion);

    public abstract CampaignFreeText getCampaignFreeText();

    public abstract void setCampaignFreeText(CampaignFreeText campaignFreeText);

    public abstract java.math.BigDecimal getAwaitedUtility();

    public abstract void setAwaitedUtility(java.math.BigDecimal awaitedUtility);

    public abstract BigDecimal getBudgetCost();

    public abstract void setBudgetCost(BigDecimal budgetcost);

    public abstract Integer getChangeDate();

    public abstract void setChangeDate(Integer changeDate);

    public abstract Integer getCloseDate();

    public abstract void setCloseDate(Integer closeDate);

    public abstract java.math.BigDecimal getRealCost();

    public abstract void setRealCost(java.math.BigDecimal realCost);

    public abstract Integer getTypeId();

    public abstract void setTypeId(Integer typeId);

    public abstract Collection getCampaignActivity();

    public abstract void setCampaignActivity(Collection campaignActivity);

    public abstract Collection getAttach();

    public abstract void setAttach(Collection attach);

    public abstract Collection getCampaignTemplate();

    public abstract void setCampaignTemplate(Collection campaignTemplate);

    public abstract Integer getTotalHits();

    public abstract void setTotalHits(Integer totalHits);

    public abstract Integer getHasEmail();

    public abstract void setHasEmail(Integer hasEmail);

    public abstract Integer getHasEmailTelecomType();

    public abstract void setHasEmailTelecomType(Integer hasEmailTelecomType);
}
