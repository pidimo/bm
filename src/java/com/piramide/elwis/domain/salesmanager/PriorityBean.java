package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Priority Bean class
 *
 * @author Fernando Montaño
 * @version $Id: PriorityBean.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 14-01-2005 10:13:58 AM Fernando Montaño Exp $
 */


public abstract class PriorityBean implements EntityBean {
    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setPriorityId(PKGenerator.i.nextKey(SalesConstants.TABLE_PRIORITY));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public PriorityBean() {
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

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract String getPriorityName();

    public abstract void setPriorityName(String processPriorityName);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
