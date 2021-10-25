package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public interface WebDocument extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getName();

    void setName(String name);

    String getUrl();

    void setUrl(String url);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getWebDocumentId();

    void setWebDocumentId(Integer webDocumentId);

    Collection getWebParameters();

    void setWebParameters(Collection webParameters);
}
