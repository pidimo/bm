/**
 * @author Fernando Montaño   17:27:27
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface Office extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getOfficeId();

    void setOfficeId(Integer officeId);

    String getName();

    void setName(String name);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getEmployees();

    void setEmployees(Collection employees);

    Address getAddress();

    void setAddress(Address address);

    Integer getSupervisorId();

    void setSupervisorId(Integer supervisorId);

    Integer getOrganizationId();

    void setOrganizationId(Integer organizationId);
}
