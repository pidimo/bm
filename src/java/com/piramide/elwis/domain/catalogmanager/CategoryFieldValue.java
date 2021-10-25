package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface CategoryFieldValue extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCategoryValueId();

    void setCategoryValueId(Integer categoryValueId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCustomerId();

    void setCustomerId(Integer customerId);

    Integer getDateValue();

    void setDateValue(Integer dateValue);

    java.math.BigDecimal getDecimalValue();

    void setDecimalValue(java.math.BigDecimal decimalValue);

    Integer getIntegerValue();

    void setIntegerValue(Integer integerValue);

    Integer getProductId();

    void setProductId(Integer productId);

    String getStringValue();

    void setStringValue(String stringValue);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getFieldValueId();

    void setFieldValueId(Integer fieldValueId);

    String getLinkValue();

    void setLinkValue(String linkValue);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getAttachId();

    void setAttachId(Integer attachId);

    String getFilename();

    void setFilename(String filename);

    Integer getProcessId();

    void setProcessId(Integer processId);

    Integer getSalePositionId();

    void setSalePositionId(Integer salePositionId);
}
