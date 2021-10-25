package com.piramide.elwis.service.contact;

import com.piramide.elwis.utils.deduplication.ContactMergeWrapper;
import com.piramide.elwis.utils.deduplication.exception.DeleteAddressException;
import com.piramide.elwis.utils.deduplication.exception.MergeAddressException;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface ContactMergeService extends EJBLocalObject {
    boolean mergeDuplicateContact(ContactMergeWrapper contactMergeWrapper) throws MergeAddressException;

    void deleteDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws DeleteAddressException;

    void removeDuplicateGroup(Integer duplicateGroupId) throws MergeAddressException;

    void keepDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws MergeAddressException;

    void keepAllDuplicateAddressProcess(Integer duplicateGroupId) throws MergeAddressException;
}
