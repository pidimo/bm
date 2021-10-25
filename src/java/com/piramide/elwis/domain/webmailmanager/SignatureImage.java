package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public interface SignatureImage extends EJBLocalObject {
    Integer getSignatureImageId();

    void setSignatureImageId(Integer signatureImageId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImageStoreId();

    void setImageStoreId(Integer imageStoreId);

    Integer getSignatureId();

    void setSignatureId(Integer signatureId);
}
