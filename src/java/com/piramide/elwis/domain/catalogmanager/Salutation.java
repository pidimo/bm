package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Salutation Entity Bean
 *
 * @author Ivan
 * @version $Id: Salutation.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Salutation extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getSalutationId();

    void setSalutationId(Integer salutationId);

    Integer getVersion();

    void setVersion(Integer versionId);

    Integer getAddressTextId();

    void setAddressTextId(Integer addressTextId);

    Integer getLetterTextId();

    void setLetterTextId(Integer letterTextId);

    String getSalutationLabel();

    void setSalutationLabel(String salutationLabel);
}
