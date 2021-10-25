/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;

public interface ProductText extends EJBLocalObject {
    Integer getProductId();

    void setProductId(Integer productId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    ProductFreeText getProductFreeText();

    void setProductFreeText(ProductFreeText productFreeText);
}
