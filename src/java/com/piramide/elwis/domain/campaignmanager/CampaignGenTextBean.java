package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenTextBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 30-may-2008 11:15:06 Miky Exp $
 */

public abstract class CampaignGenTextBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCampaignGenTextId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNGENTEXT));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public CampaignGenTextBean() {
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

    public abstract Integer getCampaignGenTextId();

    public abstract void setCampaignGenTextId(Integer campaignGenTextId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getGenerationId();

    public abstract void setGenerationId(Integer generationId);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract CampaignGeneration getCampaignGeneration();

    public abstract void setCampaignGeneration(CampaignGeneration campaignGeneration);

    public abstract CampaignFreeText getGenerationText();

    public abstract void setGenerationText(CampaignFreeText generationText);

    public void setGenerationText(EJBLocalObject generationText) {
        setGenerationText((CampaignFreeText) generationText);
    }

}
