package com.piramide.elwis.service.contact;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;

import javax.ejb.EJBLocalObject;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DeduplicationAddressService extends EJBLocalObject {

    List<Integer> getImportRecordDuplicateAddressIds(Integer importRecordId);

    List<Column> getImportProfileColumns(Integer profileId);

    List<Column> getImportProfileColumnsAddressFixed(Integer profileId);

    List<Column> getImportProfileColumnsContactPersonFixed(Integer profileId);

    List<DeduplicationItemWrapper> readImportRecordColumnValues(Integer importRecordId, List<Column> profileColumns);

    List<DeduplicationItemWrapper> readAddressColumnValues(Integer addressId, Integer organizationId, List<Column> profileColumns);

    Integer initializeContactDeduplication(Integer companyId, Integer userId);

    void finishContactDeduplication(Integer dedupliContactId);

    void createProcessedDuplicateAddress(Integer dedupliContactId, Integer companyId, List<List<Integer>> addressIdDupliList);

    boolean companyHasContactDuplicates(Integer companyId);

    List<Integer> getContactDuplicateAddressIds(Integer duplicateGroupId);

    void emptyContactDuplicates(Integer companyId);
}
