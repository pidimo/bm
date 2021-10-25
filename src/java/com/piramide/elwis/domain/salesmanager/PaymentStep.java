/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

public interface PaymentStep extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContractId();

    void setContractId(Integer contractId);

    java.math.BigDecimal getPayAmount();

    void setPayAmount(java.math.BigDecimal payAmount);

    Integer getPayDate();

    void setPayDate(Integer payDate);

    Integer getPayStepId();

    void setPayStepId(Integer payStepId);

}
