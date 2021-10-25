package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityUserBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 25-oct-2006 15:54:31 Miky Exp $
 */

public abstract class CampaignActivityUserBean implements EntityBean {
    EntityContext entityContext;

    public CampaignActivityUserPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CampaignActivityUserBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract CampaignActivity getActivity();

    public abstract void setActivity(CampaignActivity activity);

    public abstract Integer getPercent();

    public abstract void setPercent(Integer percent);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
