package com.piramide.elwis.utils.deduplication;

import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactMergeWrapper {
    private Integer duplicateGroupId;
    private Integer addressId1;
    private Integer addressId2;
    private List<ContactMergeField> contactMergeFieldList;

    public Integer getDuplicateGroupId() {
        return duplicateGroupId;
    }

    public void setDuplicateGroupId(Integer duplicateGroupId) {
        this.duplicateGroupId = duplicateGroupId;
    }

    public Integer getAddressId1() {
        return addressId1;
    }

    public void setAddressId1(Integer addressId1) {
        this.addressId1 = addressId1;
    }

    public Integer getAddressId2() {
        return addressId2;
    }

    public void setAddressId2(Integer addressId2) {
        this.addressId2 = addressId2;
    }

    public List<ContactMergeField> getContactMergeFieldList() {
        return contactMergeFieldList;
    }

    public void setContactMergeFieldList(List<ContactMergeField> contactMergeFieldList) {
        this.contactMergeFieldList = contactMergeFieldList;
    }
}
