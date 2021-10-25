/**
 * @author Fernando Montaño   17:27:27
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;
import java.util.Collection;

/**
 * Represents OfficeBean EntityBean
 *
 * @author Titus
 * @version OfficeBean.java, v 2.0 Apr 10, 2004 11:24:32 AM
 */

public abstract class OfficeBean implements EntityBean {
    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setOfficeId(PKGenerator.i.nextKey(ContactConstants.TABLE_OFFICE));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public OfficeBean() {
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

    public abstract Integer getOfficeId();

    public abstract void setOfficeId(Integer officeId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getEmployees();

    public abstract void setEmployees(Collection employees);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);

    public abstract Integer getSupervisorId();

    public abstract void setSupervisorId(Integer supervisorId);

    public abstract Integer getOrganizationId();

    public abstract void setOrganizationId(Integer organizationId);
}
