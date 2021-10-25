/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductPicture.java 2379 2004-08-23 16:10:24Z ivan ${NAME}.java, v 2.0 23-ago-2004 9:32:35 Ivan Exp $
 */
package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

public interface ProductPicture extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    String getProductPictureName();

    void setProductPictureName(String productPictureName);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getSize();

    void setSize(Integer size);

    Integer getUploadDate();

    void setUploadDate(Integer uploadDate);

    Integer getVersion();

    void setVersion(Integer version);
}
