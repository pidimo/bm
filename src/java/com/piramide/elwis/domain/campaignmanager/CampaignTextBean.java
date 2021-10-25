/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignTextBean.java 10515 2015-01-23 22:17:00Z miguel ${NAME}.java, v 2.0 03-ago-2004 20:17:19 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class CampaignTextBean implements EntityBean {


    EntityContext entityContext;

    public CampaignTextPK ejbCreate(ComponentDTO dto) throws CreateException {
        setLanguageId(new Integer(dto.get("languageId").toString()));
        setTemplateId(new Integer(dto.get("templateId").toString()));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public CampaignTextBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Boolean getByDefault();

    public abstract void setByDefault(Boolean byDefault);

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getFreeTextId();

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract CampaignFreeText getCampaignFreeText();

    public abstract void setCampaignFreeText(CampaignFreeText campaignFreeText);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getSize();

    public abstract void setSize(Integer size);
}
