/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignContactBean.java 10484 2014-08-28 22:51:28Z miguel ${NAME}.java, v 2.0 17-jun-2004 16:15:11 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class CampaignContactBean implements EntityBean {

    EntityContext entityContext;

    public CampaignContactPK ejbCreate(ComponentDTO dto) throws CreateException {
        setCampaignContactId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNCONTACT));
        DTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
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

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getCampaignContactId();

    public abstract void setCampaignContactId(Integer campaignContactId);

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract CampaignActivity getCampaignActivity();

    public abstract void setCampaignActivity(CampaignActivity campaignActivity);

    public abstract Integer ejbSelectCountByCampaignIdAndActivityIdNULL(Integer campaignId) throws FinderException;

    public Integer ejbHomeSelectCountByCampaignIdAndActivityIdNULL(Integer campaignId) throws FinderException {
        return ejbSelectCountByCampaignIdAndActivityIdNULL(campaignId);
    }

    public abstract Integer ejbSelectCountEnabledCampaignContactsByActivity(Integer activityId) throws FinderException;

    public Integer ejbHomeSelectCountEnabledCampaignContactsByActivity(Integer activityId) throws FinderException {
        return ejbSelectCountEnabledCampaignContactsByActivity(activityId);
    }

    public abstract Integer ejbSelectCountByActivityIdWithoutResponsible(Integer activityId) throws FinderException;

    public Integer ejbHomeSelectCountByActivityIdWithoutResponsible(Integer activityId) throws FinderException {
        return ejbSelectCountByActivityIdWithoutResponsible(activityId);
    }

}
