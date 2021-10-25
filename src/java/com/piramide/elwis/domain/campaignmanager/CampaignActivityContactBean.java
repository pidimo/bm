/**
 * @author: ivan
 * Date: 01-11-2006: 01:14:23 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CampaignActivityContactBean implements EntityBean {
    EntityContext entityContext;

    public CampaignActivityContactPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CampaignActivityContactBean() {
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

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getTaskId();

    public abstract void setTaskId(Integer taskId);

    public abstract String getFromEmail();

    public abstract void setFromEmail(String fromEmail);

    public abstract Integer getGenerationId();

    public abstract void setGenerationId(Integer generationId);

    public abstract CampaignGeneration getCampaignGeneration();

    public abstract void setCampaignGeneration(CampaignGeneration campaignGeneration);
}
