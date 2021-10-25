package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public abstract class CampaignSentLogBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCampaignSentLogId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNSENTLOG));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public CampaignSentLogBean() {
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

    public abstract Integer getCampaignSentLogId();

    public abstract void setCampaignSentLogId(Integer campaignSentLogId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getGenerationId();

    public abstract void setGenerationId(Integer generationId);

    public abstract Integer getTotalSent();

    public abstract void setTotalSent(Integer totalSent);

    public abstract Integer getTotalSuccess();

    public abstract void setTotalSuccess(Integer totalSuccess);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getSentLogContacts();

    public abstract void setSentLogContacts(Collection sentLogContacts);

    public abstract CampaignGeneration getCampaignGeneration();

    public abstract void setCampaignGeneration(CampaignGeneration campaignGeneration);

    public abstract Long getGenerationKey();

    public abstract void setGenerationKey(Long generationKey);

    public abstract Integer ejbSelectTotalSentByGenerationKey(Long generationKey) throws FinderException;

    public Integer ejbHomeSelectTotalSentByGenerationKey(Long generationKey) throws FinderException {
        return ejbSelectTotalSentByGenerationKey(generationKey);
    }

}
