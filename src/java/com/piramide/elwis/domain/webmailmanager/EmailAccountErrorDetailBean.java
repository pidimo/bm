package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public abstract class EmailAccountErrorDetailBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setEmailAccountErrorDetailId(PKGenerator.i.nextKey(WebMailConstants.TABLE_EMAILACCOUNTERRORDETAIL));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public EmailAccountErrorDetailBean() {
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

    public abstract Integer getEmailAccountErrorDetailId();

    public abstract void setEmailAccountErrorDetailId(Integer emailAccountErrorDetailId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getEmails();

    public abstract void setEmails(String emails);

    public abstract Integer getEmailAccountErrorId();

    public abstract void setEmailAccountErrorId(Integer emailAccountErrorId);

    public abstract String getSubject();

    public abstract void setSubject(String subject);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getMailIdentifier();

    public abstract void setMailIdentifier(Integer mailIdentifier);
}
