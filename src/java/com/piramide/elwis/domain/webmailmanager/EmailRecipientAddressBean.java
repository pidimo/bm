/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class EmailRecipientAddressBean implements EntityBean {
    EntityContext entityContext = null;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setEmailRecipientAddressId(PKGenerator.i.nextKey(WebMailConstants.TABLE_RECIPIENTRADDRESS));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {

    }

    public EmailRecipientAddressBean() {
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

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getEmailRecipientAddressId();

    public abstract void setEmailRecipientAddressId(Integer emailRecipientAddressId);

    public abstract Integer getMailRecipientId();

    public abstract void setMailRecipientId(Integer mailRecipientId);
}
