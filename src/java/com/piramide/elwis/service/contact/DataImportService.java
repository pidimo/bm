/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.service.contact;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.CompoundGroup;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ImportErrorsException;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.deduplication.ImportMergeWrapper;

import javax.ejb.EJBLocalObject;
import java.util.List;
import java.util.Map;

public interface DataImportService extends EJBLocalObject {

    public CompoundGroup getOrganizationGroup(Integer companyId);

    public CompoundGroup getContactGroup(Integer companyId);

    public List getOrganizationAndContactPersonGroups(Integer companyId);

    public void importData(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId) throws ImportErrorsException;

    public void importForDeduplicate(String cachePath,
                           List<Column> selectedColumns,
                           ArrayByteWrapper file,
                           String fileName,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId,
                           Integer profileId) throws ImportErrorsException;

    public void saveImportRecordWithoutDuplicate(Map<Integer, List<Integer>> importRecordDuplicatesMap,
                           Integer profileId,
                           List<Column> selectedColumns,
                           DataImportConfiguration configuration,
                           Integer userId,
                           Integer companyId) throws ValidationException;

    List<CompoundGroup> getColumnStructureConfigurationByProfileType(Integer profileType, Integer companyId);

    public boolean mergeImportRecord(ImportMergeWrapper importMergeWrapper);

    public void notImportDuplicateProcess(Integer importRecordId);

    boolean importNevertheless(Integer importRecordId, Integer userId, DataImportConfiguration configuration) throws ValidationException;

    ImportProfileDTO getImportProfileDTO(Integer profileId);

    public Integer readImportProfileTotalRecord(Long importStartTime);

    boolean importProfileContainDuplicateRecords(Integer profileId);

    boolean importRecordFixedHasDuplicateContactPerson(Integer importRecordId);

    boolean isImportRecordDuplicate(Integer importRecordId);

    void emptyImportProfileDuplicates(Integer profileId);
}
