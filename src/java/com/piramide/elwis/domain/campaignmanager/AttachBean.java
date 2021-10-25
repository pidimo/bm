/**
 * @author: ivan
 * Date: 25-10-2006: 11:21:50 AM
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class AttachBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        setAttachId(PKGenerator.i.nextKey(CampaignConstants.TABLE_ATTACH));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public AttachBean() {
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

    public abstract Integer getAttachId();

    public abstract void setAttachId(Integer attachId);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract String getComment();

    public abstract void setComment(String comment);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getSize();

    public abstract void setSize(Integer size);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract CampaignFreeText getCampaignFreeText();

    public abstract void setCampaignFreeText(CampaignFreeText campaignFreeText);
}
