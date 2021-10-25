package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddressGroupBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}, 14-03-2005 02:22:45 PM alvaro Exp $
 */

public abstract class AddressGroupBean implements EntityBean {
    EntityContext entityContext;

    public AddressGroupBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setAddressGroupId(PKGenerator.i.nextKey(WebMailConstants.TABLE_ADDRESSGROUP));
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

    public abstract Integer getAddressGroupId();

    public abstract void setAddressGroupId(Integer addressGroupId);

    public abstract Integer getMailGroupAddrId();

    public abstract void setMailGroupAddrId(Integer mailGroupAddrId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getTelecomId();

    public abstract void setTelecomId(Integer telecomId);

    public abstract Integer getSendToAll();

    public abstract void setSendToAll(Integer sendToAll);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
