/**
 * Jatun S.R.L
 *
 * @author ivan
 *
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

public interface ActionTypeSequence extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getActionTypeId();

    void setActionTypeId(Integer actionTypeId);

    Integer getNumberId();

    void setNumberId(Integer numberId);
}
