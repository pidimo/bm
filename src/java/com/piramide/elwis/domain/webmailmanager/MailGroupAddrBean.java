package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: MailGroupAddrBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}, 14-03-2005 02:10:54 PM alvaro Exp $
 */

public abstract class MailGroupAddrBean implements EntityBean {
    EntityContext entityContext;

    public MailGroupAddrBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setMailGroupAddrId(PKGenerator.i.nextKey(WebMailConstants.TABLE_MAILGROUPADDR));
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

    public abstract Integer getMailGroupAddrId();

    public abstract void setMailGroupAddrId(Integer mailGroupAddrId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Collection getAddressGroups();

    public abstract void setAddressGroups(Collection addressGroups);
}
