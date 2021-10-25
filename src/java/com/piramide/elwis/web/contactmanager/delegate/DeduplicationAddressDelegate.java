package com.piramide.elwis.web.contactmanager.delegate;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.service.contact.DeduplicationAddressService;
import com.piramide.elwis.service.contact.DeduplicationAddressServiceHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationAddressDelegate {
    private Log log = LogFactory.getLog(this.getClass());

    public static DeduplicationAddressDelegate i = new DeduplicationAddressDelegate();

    private DeduplicationAddressDelegate() {
    }

    private DeduplicationAddressService getService() {
        DeduplicationAddressServiceHome home = (DeduplicationAddressServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEDUPLICATIONADDRESSSERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("Create DeduplicationAddressService Fail ", e);
        }

        return null;
    }

    public List<Integer> getImportRecordDuplicateAddressIds(Integer importRecordId) {
        DeduplicationAddressService service = getService();
        return service.getImportRecordDuplicateAddressIds(importRecordId);
    }

    public List<Column> getImportProfileColumns(Integer profileId) {
        DeduplicationAddressService service = getService();
        return service.getImportProfileColumns(profileId);
    }

    public List<Column> getImportProfileColumnsAddressFixed(Integer profileId) {
        DeduplicationAddressService service = getService();
        return service.getImportProfileColumnsAddressFixed(profileId);
    }

    public List<Column> getImportProfileColumnsContactPersonFixed(Integer profileId) {
        DeduplicationAddressService service = getService();
        return service.getImportProfileColumnsContactPersonFixed(profileId);
    }

    public List<DeduplicationItemWrapper> readImportRecordColumnValues(Integer importRecordId, List<Column> profileColumns) {
        DeduplicationAddressService service = getService();
        return service.readImportRecordColumnValues(importRecordId, profileColumns);
    }

    public List<DeduplicationItemWrapper> readAddressColumnValues(Integer addressId, Integer organizationId, List<Column> profileColumns) {
        DeduplicationAddressService service = getService();
        return service.readAddressColumnValues(addressId, organizationId, profileColumns);
    }

    public Integer initializeContactDeduplication(Integer companyId, Integer userId) {
        DeduplicationAddressService service = getService();
        return service.initializeContactDeduplication(companyId, userId);
    }

    public void finishContactDeduplication(Integer dedupliContactId) {
        DeduplicationAddressService service = getService();
        service.finishContactDeduplication(dedupliContactId);
    }

    public void createProcessedDuplicateAddress(Integer dedupliContactId, Integer companyId, List<List<Integer>> addressIdDupliList) {
        DeduplicationAddressService service = getService();
        service.createProcessedDuplicateAddress(dedupliContactId, companyId, addressIdDupliList);
    }

    public boolean companyHasContactDuplicates(Integer companyId) {
        DeduplicationAddressService service = getService();
        return service.companyHasContactDuplicates(companyId);
    }

    public List<Integer> getContactDuplicateAddressIds(Integer duplicateGroupId) {
        DeduplicationAddressService service = getService();
        return service.getContactDuplicateAddressIds(duplicateGroupId);
    }

    public void emptyContactDuplicates(Integer companyId) {
        DeduplicationAddressService service = getService();
        service.emptyContactDuplicates(companyId);
    }
}
