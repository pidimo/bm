package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 13-oct-2006 18:43:20 Miky Exp $
 */

public abstract class CampaignActivityBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setActivityId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNACTIVITY));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }


    public CampaignActivityBean() {
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

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getCloseDate();

    public abstract void setCloseDate(Integer closeDate);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract BigDecimal getCost();

    public abstract void setCost(BigDecimal cost);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getNumberContact();

    public abstract void setNumberContact(Integer numberContact);

    public abstract Integer getPercent();

    public abstract void setPercent(Integer percent);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startdate);

    public abstract Integer getState();

    public abstract void setState(Integer state);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getCampaignContacts();

    public abstract void setCampaignContacts(Collection campaignContacts);

    public abstract CampaignFreeText getCampaignFreeText();

    public abstract void setCampaignFreeText(CampaignFreeText campaignFreeText);

    public void setCampaignFreeText(EJBLocalObject campaignFreeText) {
        setCampaignFreeText((CampaignFreeText) campaignFreeText);
    }

    public abstract Collection getActivityUsers();

    public abstract void setActivityUsers(Collection activityUsers);
}
