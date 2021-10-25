/**
 * @author Fernando Montaño   16:28:11
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
 * Represents DepartmentBean EntityBean
 *
 * @author Titus
 * @version DepartmentBean.java, v 2.0 Apr 10, 2004 11:24:32 AM
 */
public abstract class DepartmentBean implements EntityBean {
    //<< Titus
    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setDepartmentId(PKGenerator.i.nextKey(ContactConstants.TABLE_DEPARTMENT));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    // Titus >>
    public DepartmentBean() {
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

    public abstract Integer getDepartmentId();

    public abstract void setDepartmentId(Integer departmentId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Department getParent();

    public abstract void setParent(Department parent);

    public abstract Collection getEmployees();

    public abstract void setEmployees(Collection employees);

    public abstract Integer getParentId();

    public abstract void setParentId(Integer parentid);

    public abstract Integer getOrganizationId();

    public abstract void setOrganizationId(Integer organizationId);

    public String getParentName() {
        if (getParent() != null) {
            return getParent().getName();
        }
        return "";
    }

    public void setParentName(String parentName) {

    }

    public abstract Integer getManagerId();

    public abstract void setManagerId(Integer managerId);

    public abstract ContactPerson getContactPerson();

    public abstract void setContactPerson(ContactPerson contactPerson);
}
