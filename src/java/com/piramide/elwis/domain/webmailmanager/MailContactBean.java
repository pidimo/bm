package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: MailContactBean.java 9695 2009-09-10 21:34:43Z fernando ${MailContactBean}.java ,v 1.1 27-07-2005 10:30:08 AM Alvaro Exp $
 */

public abstract class MailContactBean implements EntityBean {
    EntityContext entityContext;

    public MailContactBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setMailContactId(PKGenerator.i.nextKey(WebMailConstants.TABLE_MAILCONTACT));
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getMailContactId();

    public abstract void setMailContactId(Integer mailContactId);

    public abstract Integer getMailId();

    public abstract void setMailId(Integer mailId);

    public abstract Mail getMail();

    public abstract void setMail(Mail mail);

    public abstract String getEmail();

    public abstract void setEmail(String email);
}
