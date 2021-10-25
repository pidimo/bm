package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenAttachBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 30-may-2008 11:29:38 Miky Exp $
 */

public abstract class CampaignGenAttachBean implements EntityBean {
    EntityContext entityContext;

    public CampaignGenAttachPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CampaignGenAttachBean() {
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

    public abstract Integer getAttachmentId();

    public abstract void setAttachmentId(Integer attachmentId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getGenerationId();

    public abstract void setGenerationId(Integer generationId);

    public abstract CampaignGeneration getCampaignGeneration();

    public abstract void setCampaignGeneration(CampaignGeneration campaignGeneration);
}
