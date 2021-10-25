package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public abstract class SentLogContactBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setSentLogContactId(PKGenerator.i.nextKey(CampaignConstants.TABLE_SENTLOGCONTACT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public SentLogContactBean() {
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCampaignSentLogId();

    public abstract void setCampaignSentLogId(Integer campaignSentLogId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract String getErrorMessage();

    public abstract void setErrorMessage(String errorMessage);

    public abstract Integer getSentLogContactId();

    public abstract void setSentLogContactId(Integer sentLogContactId);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Long getGenerationKey();

    public abstract void setGenerationKey(Long generationKey);

    public abstract Integer ejbSelectCountByFailedCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    public Integer ejbHomeSelectCountByFailedCampaignSentLogId(Integer campaignSentLogId) throws FinderException {
        return ejbSelectCountByFailedCampaignSentLogId(campaignSentLogId);
    }

    public abstract Integer ejbSelectCountByCampaignSentLogIdSuccess(Integer campaignSentLogId) throws FinderException;

    public Integer ejbHomeSelectCountByCampaignSentLogIdSuccess(Integer campaignSentLogId) throws FinderException {
        return ejbSelectCountByCampaignSentLogIdSuccess(campaignSentLogId);
    }

    public abstract Integer ejbSelectCountByCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    public Integer ejbHomeSelectCountByCampaignSentLogId(Integer campaignSentLogId) throws FinderException {
        return ejbSelectCountByCampaignSentLogId(campaignSentLogId);
    }

    public abstract Integer ejbSelectCountByGenerationKeySuccess(Long generationKey) throws FinderException;

    public Integer ejbHomeSelectCountByGenerationKeySuccess(Long generationKey) throws FinderException {
        return ejbSelectCountByGenerationKeySuccess(generationKey);
    }

    public abstract Integer ejbSelectCountByGenerationKeyFailed(Long generationKey) throws FinderException;

    public Integer ejbHomeSelectCountByGenerationKeyFailed(Long generationKey) throws FinderException {
        return ejbSelectCountByGenerationKeyFailed(generationKey);
    }

    public abstract Integer ejbSelectMinSentLogContactIdByStatus(Integer status) throws FinderException;

    public Integer ejbHomeSelectMinSentLogContactIdByStatus(Integer status) throws FinderException {
        return ejbSelectMinSentLogContactIdByStatus(status);
    }

    public abstract Integer ejbSelectCountByCampaignSentLogIdProcessInBackground(Integer campaignSentLogId) throws FinderException;

    public Integer ejbHomeSelectCountByCampaignSentLogIdProcessInBackground(Integer campaignSentLogId) throws FinderException {
        return ejbSelectCountByCampaignSentLogIdProcessInBackground(campaignSentLogId);
    }

}
