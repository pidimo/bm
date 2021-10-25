/**
 * @author: ivan
 * Date: 30-10-2006: 03:55:53 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

public abstract class CampaignTemplateBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTemplateId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGN_TEMPLATE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CampaignTemplateBean() {
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

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getDocumentType();

    public abstract void setDocumentType(Integer documentType);

    public abstract Collection getCampaignText();

    public abstract void setCampaignText(Collection campaignText);
}
