package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Status Entity bean class
 *
 * @author Fernando Montaño
 * @version $Id: StatusBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:21:23 AM Fernando Montaño Exp $
 */


public abstract class StatusBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setStatusId(PKGenerator.i.nextKey(SalesConstants.TABLE_STATUS));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public StatusBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Boolean getIsFinal();

    public abstract void setIsFinal(Boolean isFinal);

    public abstract Integer getStatusId();

    public abstract void setStatusId(Integer statusId);

    public abstract String getStatusName();

    public abstract void setStatusName(String statusName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
