/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface Account extends EJBLocalObject {
    Integer getAccountId();

    void setAccountId(Integer accountId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getName();

    void setName(String name);

    String getNumber();

    void setNumber(String number);

    Integer getVersion();

    void setVersion(Integer version);
}
