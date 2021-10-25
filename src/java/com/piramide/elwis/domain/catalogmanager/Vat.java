/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: Vat.java 10072 2011-07-04 20:33:26Z miguel ${NAME}.java, v 2.0 16-ago-2004 16:58:15 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface Vat extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getVatDescription();

    void setVatDescription(String description);

    String getVatLabel();

    void setVatLabel(String label);

    Integer getVatId();

    void setVatId(Integer vatId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getTaxKey();

    void setTaxKey(Integer taxKey);
}
