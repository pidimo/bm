/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-26 05:54:57 PM $
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class CampaignGenTextImgBean implements EntityBean {

    EntityContext entityContext;

    public CampaignGenTextImgPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }


    public CampaignGenTextImgBean() {
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

    public abstract Integer getImageStoreId();

    public abstract void setImageStoreId(Integer imageStoreId);
}
