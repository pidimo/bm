/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

public interface ContractType extends EJBLocalObject {
    Integer getContractTypeId();

    void setContractTypeId(Integer contractTypeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getName();

    void setName(String name);

    Boolean getTobeInvoiced();

    void setTobeInvoiced(Boolean tobeInvoiced);

    Integer getVersion();

    void setVersion(Integer version);
}
