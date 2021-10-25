package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * interface to access bean
 *
 * @author ernesto
 * @version EmployeeBean.java, v 2.0 Apr 23, 2004 11:24:32 AM
 */
public interface Employee extends EJBLocalObject {
    java.math.BigDecimal getCostHour();

    void setCostHour(java.math.BigDecimal costHour);

    java.math.BigDecimal getCostPosition();

    void setCostPosition(java.math.BigDecimal costPosition);

    Integer getDateEnd();

    void setDateEnd(Integer dateEnd);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    String getFunction();

    void setFunction(String function);

    Integer getHireDate();

    void setHireDate(Integer hireDate);

    java.math.BigDecimal getHourlyRate();

    void setHourlyRate(java.math.BigDecimal hourlyRate);

    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);

    Integer getVersion();

    void setVersion(Integer version);

    Office getOffice();

    void setOffice(Office office);

    Integer getCompany();

    void setCompany(Integer company);

    Department getDepartment();

    void setDepartment(Department department);

    Address getAddress();

    void setAddress(Address address);

    Address getHealthFund();

    void setHealthFund(Address healthFund);

    Integer getOfficeId();

    void setOfficeId(Integer officeId);

    Integer getDepartmentId();

    void setDepartmentId(Integer departmentId);

    Integer getHealthFundId();

    void setHealthFundId(Integer healthFundId);

    Integer getCostCenterId();

    void setCostCenterId(Integer costCenterId);

    String getInitials();

    void setInitials(String initials);
}
