package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: Action.java 10009 2010-11-15 10:10:53Z ivan ${NAME}.java, v 2.0 24-01-2005 03:40:11 PM Fernando Montaño Exp $
 */


public interface Action extends EJBLocalObject {
    Integer getActionTypeId();

    void setActionTypeId(Integer actionTypeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getFollowUpDate();

    void setFollowUpDate(Integer followUpDate);

    Integer getProcessId();

    void setProcessId(Integer processId);

    java.math.BigDecimal getValue();

    void setValue(java.math.BigDecimal value);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getActionPositions();

    void setActionPositions(Collection actionPositions);

    SalesProcess getSalesProcess();

    void setSalesProcess(SalesProcess salesProcess);

    Boolean getActive();

    void setActive(Boolean active);

    String getNumber();

    void setNumber(String number);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Integer getNetGross();

    void setNetGross(Integer netGross);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Long getUpdateDateTime();

    void setUpdateDateTime(Long updateDateTime);

    Integer getUserId();

    void setUserId(Integer userId);
}
