package com.piramide.elwis.web.contactmanager.delegate.dataimport;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.CompoundGroup;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ImportErrorsException;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.service.contact.DataImportService;
import com.piramide.elwis.service.contact.DataImportServiceHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.deduplication.ImportMergeWrapper;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import java.util.List;
import java.util.Map;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class DataImportDelegate {
    private Log log = LogFactory.getLog(this.getClass());

    public static DataImportDelegate i = new DataImportDelegate();

    private DataImportDelegate() {
    }

    private DataImportService getService() {
        DataImportServiceHome home =
                (DataImportServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DATAIMPORTSERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("-> Create DataImportService Fail ", e);
        }

        return null;
    }

    public void importData(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId) throws ImportErrorsException {
        DataImportService service = getService();

        service.importData(cachePath, selectedColumns, file, fileName, configuration, userId, companyId);
    }

    public void importForDeduplicate(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId,
                           Integer profileId) throws ImportErrorsException {
        DataImportService service = getService();

        service.importForDeduplicate(cachePath, selectedColumns, file, fileName, configuration, userId, companyId, profileId);
    }

    public void saveImportRecordWithoutDuplicate(Map<Integer, List<Integer>> importRecordDuplicatesMap,
                                                 Integer profileId,
                                                 List<Column> selectedColumns,
                                                 DataImportConfiguration configuration,
                                                 Integer userId,
                                                 Integer companyId) throws ValidationException {
        DataImportService service = getService();
        service.saveImportRecordWithoutDuplicate(importRecordDuplicatesMap, profileId, selectedColumns, configuration, userId, companyId);
    }

    public boolean mergeImportRecord(ImportMergeWrapper importMergeWrapper) {
        DataImportService service = getService();
        return service.mergeImportRecord(importMergeWrapper);
    }

    public boolean importNevertheless(Integer importRecordId, Integer userId, DataImportConfiguration configuration) throws ValidationException {
        DataImportService service = getService();
        return service.importNevertheless(importRecordId, userId, configuration);
    }

    public void notImportDuplicateProcess(Integer importRecordId) {
        DataImportService service = getService();
        service.notImportDuplicateProcess(importRecordId);
    }

    public boolean importRecordFixedHasDuplicateContactPerson(Integer importRecordId) {
        DataImportService service = getService();
        return service.importRecordFixedHasDuplicateContactPerson(importRecordId);
    }

    public ImportProfileDTO getImportProfileDTO(Integer profileId) {
        DataImportService service = getService();
        return service.getImportProfileDTO(profileId);
    }

    public Integer readImportProfileTotalRecord(Long importStartTime) {
        DataImportService service = getService();
        return service.readImportProfileTotalRecord(importStartTime);
    }

    public boolean importProfileContainDuplicateRecords(Integer profileId) {
        DataImportService service = getService();
        return service.importProfileContainDuplicateRecords(profileId);
    }

    public CompoundGroup getOrganizationGroup(Integer companyId) {
        DataImportService service = getService();
        return service.getOrganizationGroup(companyId);
    }

    public CompoundGroup getContactGroup(Integer companyId) {
        DataImportService service = getService();
        return service.getContactGroup(companyId);
    }

    public List getOrganizationAndContactPersonGroups(Integer companyId) {
        DataImportService service = getService();
        return service.getOrganizationAndContactPersonGroups(companyId);
    }

    public boolean isImportRecordDuplicate(Integer importRecordId) {
        DataImportService service = getService();
        return service.isImportRecordDuplicate(importRecordId);
    }

    public void emptyImportProfileDuplicates(Integer profileId) {
        DataImportService service = getService();
        service.emptyImportProfileDuplicates(profileId);
    }

}
