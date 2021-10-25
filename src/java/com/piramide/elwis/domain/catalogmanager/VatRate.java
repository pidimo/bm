/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: VatRate.java 2673 2004-10-05 13:18:27Z ivan ${NAME}.java, v 2.0 18-ago-2004 10:31:12 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface VatRate extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getValidFrom();

    void setValidFrom(Integer validFrom);

    Integer getVatId();

    void setVatId(Integer vatId);

    java.math.BigDecimal getVatRate();

    void setVatRate(java.math.BigDecimal vatRate);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getVatrateId();

    void setVatrateId(Integer vatrateId);
}
