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
import java.util.Collection;

public abstract class EmailAccountErrorBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setEmailAccountErrorId(PKGenerator.i.nextKey(WebMailConstants.TABLE_EMAILACCOUNTERROR));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public EmailAccountErrorBean() {
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

    public abstract Integer getEmailAccountErrorId();

    public abstract void setEmailAccountErrorId(Integer emailAccountErrorId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getErrorType();

    public abstract void setErrorType(Integer errortype);

    public abstract Integer getMailAccountId();

    public abstract void setMailAccountId(Integer mailAccountId);

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Long getTimeError();

    public abstract void setTimeError(Long timeError);

    public abstract Collection getEmailAccountErrorDetails();

    public abstract void setEmailAccountErrorDetails(Collection emailAccountErrorDetails);

    public abstract String getCausedBy();

    public abstract void setCausedBy(String causedBy);
}
