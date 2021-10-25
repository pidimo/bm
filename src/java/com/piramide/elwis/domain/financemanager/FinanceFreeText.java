/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface FinanceFreeText extends EJBLocalObject {
    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getType();

    void setType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    byte[] getValue();

    void setValue(byte[] value);
}
