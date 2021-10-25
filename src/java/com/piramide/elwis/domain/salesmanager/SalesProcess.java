package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Sales Process Local interface.
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcess.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface SalesProcess extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    Integer getEndDate();

    void setEndDate(Integer endDate);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getProcessId();

    void setProcessId(Integer processId);

    String getProcessName();

    void setProcessName(String processName);

    Integer getProbability();

    void setProbability(Integer probability);

    Integer getStatusId();

    void setStatusId(Integer statusId);

    Integer getStartDate();

    void setStartDate(Integer startDate);

    java.math.BigDecimal getValue();

    void setValue(java.math.BigDecimal value);

    Integer getVersion();

    void setVersion(Integer version);

    SalesFreeText getDescriptionText();

    void setDescriptionText(SalesFreeText descriptionText);

    void setDescriptionText(EJBLocalObject descriptionText);

    Collection getActions();

    void setActions(Collection actions);

    Integer getActivityId();

    void setActivityId(Integer activityId);

    Collection getSale();

    void setSale(Collection sale);
}
