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
 * @version $Id: MailRecipientBean.java 9139 2009-04-22 22:31:38Z ivan ${NAME}, 14-03-2005 02:33:54 PM alvaro Exp $
 */

public abstract class MailRecipientBean implements EntityBean {
    EntityContext entityContext;

    public MailRecipientBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        Integer key = PKGenerator.i.nextKey(WebMailConstants.TABLE_MAILRECIPIENT);
        this.setMailRecipientId(key);
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

    public abstract Integer getMailRecipientId();

    public abstract void setMailRecipientId(Integer mailRecipientId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract Integer getMailId();

    public abstract void setMailId(Integer mailId);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract String getPersonalName();

    public abstract void setPersonalName(String personalName);

    public abstract java.util.Collection getRecipientAddresses();

    public abstract void setRecipientAddresses(java.util.Collection recipientAddresses);
}
