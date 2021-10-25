package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public interface AddressRelation extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getRelatedAddressId();

    void setRelatedAddressId(Integer relatedAddressId);

    Integer getCommentId();

    void setCommentId(Integer commentId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRelationId();

    void setRelationId(Integer relationId);

    Integer getRelationTypeId();

    void setRelationTypeId(Integer relationTypeId);

    Integer getVersion();

    void setVersion(Integer version);

    ContactFreeText getContactFreeText();

    void setContactFreeText(ContactFreeText contactFreeText);

    public void setContactFreeText(EJBLocalObject contactFreeText);
}
