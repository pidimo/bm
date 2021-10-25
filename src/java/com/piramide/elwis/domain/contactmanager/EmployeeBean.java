package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Represents EmployeeBean EntityBean
 *
 * @author Titus
 * @version EmployeeBean.java, v 2.0
 */

public abstract class EmployeeBean implements EntityBean {
    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setVersion(new Integer(1));
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {
    }

    public EmployeeBean() {
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

    public abstract java.math.BigDecimal getCostHour();

    public abstract void setCostHour(java.math.BigDecimal costHour);

    public abstract java.math.BigDecimal getCostPosition();

    public abstract void setCostPosition(java.math.BigDecimal costPosition);

    public abstract Integer getDateEnd();

    public abstract void setDateEnd(Integer dateEnd);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract String getFunction();

    public abstract void setFunction(String function);

    public abstract Integer getHireDate();

    public abstract void setHireDate(Integer hireDate);

    public abstract java.math.BigDecimal getHourlyRate();

    public abstract void setHourlyRate(java.math.BigDecimal hourlyRate);

    public abstract String getSocialSecurityNumber();

    public abstract void setSocialSecurityNumber(String socialSecurityNumber);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Office getOffice();

    public abstract void setOffice(Office office);

    public abstract Integer getCompany();

    public abstract void setCompany(Integer company);

    public abstract Department getDepartment();

    public abstract void setDepartment(Department department);

    public abstract Address getAddress();

    public abstract void setAddress(Address address);

    public abstract Address getHealthFund();

    public abstract void setHealthFund(Address healthFund);

    public abstract Integer getOfficeId();

    public abstract void setOfficeId(Integer officeId);

    public abstract Integer getDepartmentId();

    public abstract void setDepartmentId(Integer departmentId);

    public abstract Integer getHealthFundId();

    public abstract void setHealthFundId(Integer healthFundId);

    public abstract Integer getCostCenterId();

    public abstract void setCostCenterId(Integer costCenterId);

    public abstract String getInitials();

    public abstract void setInitials(String initials);
}
