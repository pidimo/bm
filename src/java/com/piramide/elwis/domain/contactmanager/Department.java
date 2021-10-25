/**
 * @author Fernando Montaño   16:28:11
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface Department extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDepartmentId();

    void setDepartmentId(Integer departmentId);

    String getName();

    void setName(String name);

    Integer getVersion();

    void setVersion(Integer version);

    Department getParent();

    void setParent(Department parent);

    Collection getEmployees();

    void setEmployees(Collection employees);

    Integer getParentId();

    void setParentId(Integer parentid);

    Integer getOrganizationId();

    void setOrganizationId(Integer organizationId);

    String getParentName();

    void setParentName(String parentName);

    Integer getManagerId();

    void setManagerId(Integer managerId);

    ContactPerson getContactPerson();

    void setContactPerson(ContactPerson contactPerson);
}
