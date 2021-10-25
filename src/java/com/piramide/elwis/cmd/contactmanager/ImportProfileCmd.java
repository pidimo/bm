package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.CompoundGroup;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DinamicColumn;
import com.piramide.elwis.domain.contactmanager.ImportColumn;
import com.piramide.elwis.domain.contactmanager.ImportColumnHome;
import com.piramide.elwis.domain.contactmanager.ImportProfile;
import com.piramide.elwis.domain.contactmanager.ImportProfileHome;
import com.piramide.elwis.dto.contactmanager.ImportColumnDTO;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.service.contact.DataImportService;
import com.piramide.elwis.service.contact.DataImportServiceHome;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public class ImportProfileCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ImportProfileCmd.class);

    /**
     * Build a <code>ImportProfileDTO</code> object from <code>ParamDTO</code> object.
     *
     * @return <code>ImportProdileDTO</code> object
     */
    private ImportProfileDTO getImportProfileDTO() {
        ImportProfileDTO profileDTO = new ImportProfileDTO();

        EJBCommandUtil.i.setValueAsInteger(this, profileDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, profileDTO, "profileId");
        EJBCommandUtil.i.setValueAsInteger(this, profileDTO, "profileType");
        EJBCommandUtil.i.setValueAsInteger(this, profileDTO, "userId");
        EJBCommandUtil.i.setValueAsInteger(this, profileDTO, "version");

        profileDTO.put("label", paramDTO.get("label"));
        Boolean skipFirstRow = EJBCommandUtil.i.getValueAsBoolean(this, "skipFirstRow");
        if (null == skipFirstRow) {
            skipFirstRow = false;
        }

        Boolean checkDuplicate = EJBCommandUtil.i.getValueAsBoolean(this, "checkDuplicate");
        if (null == checkDuplicate) {
            checkDuplicate = false;
        }

        profileDTO.put("skipFirstRow", skipFirstRow);
        profileDTO.put("checkDuplicate", checkDuplicate);
        profileDTO.put("importStartTime", paramDTO.get("importStartTime"));

        return profileDTO;
    }

    /**
     * Build a <code>List</code> of <code>ImportColumnDTO</code> objects from <code>List</code> of <code>Column</code>
     * these are passed to command in <code>paramDTO</code> object under key <code>'selectedColumns'</code>.
     *
     * @return <code>List</code> of <code>ImportColumnDTO</code> objects.
     */
    private List<ImportColumnDTO> getImportColumnDTOs() {
        List<Column> selectedColumns = (List<Column>) paramDTO.get("selectedColumns");

        List<ImportColumnDTO> result = new ArrayList<ImportColumnDTO>();
        for (int i = 0; i < selectedColumns.size(); i++) {
            Column column = selectedColumns.get(i);
            ImportColumnDTO importColumnDTO = new ImportColumnDTO();
            importColumnDTO.put("columnId", column.getColumnId());
            importColumnDTO.put("groupId", column.getGroup().getGroupId());
            importColumnDTO.put("uiPosition", column.getUiPosition());
            importColumnDTO.put("columnValue", column.getPosition() + 1);
            if (column instanceof DinamicColumn) {
                importColumnDTO.put("columnName", ((DinamicColumn) column).getDefaultText());
            }

            result.add(importColumnDTO);
        }

        return result;
    }

    /**
     * Execute the logic associated to the operation defined in the <code>EJBCommand</code> object.
     * The next operations are registred:
     * <p/>
     * <code>Default Operation</code>: If the operation property was not defined in the <code>EJBCommand</code> object
     * it execute by default the read operation.
     * <p/>
     * <code>'manageProfile'</code>: Create and update <code>ImportProfile</code> objects.
     * <p/>
     * <code>'isUniqueLabel'</code>: Verifies if the <code>label</code> is unique by <code>userId</code> and
     * <code>companyId</code> parameters.
     *
     * @param context <code>SessionContext</code> object, than define the context with wich the command
     *                execute its operations.
     */
    @Override
    public void executeInStateless(SessionContext context) {
        boolean isRead = true;

        if ("manageProfile".equals(getOp())) {
            isRead = false;
            ImportProfileDTO importProfileDTO = getImportProfileDTO();
            List<ImportColumnDTO> importColumnDTOs = getImportColumnDTOs();
            manageProfile(importProfileDTO, importColumnDTOs);
        }

        if ("isUniqueLabel".equals(getOp())) {
            isRead = false;
            String label = (String) paramDTO.get("label");
            Integer profileId = EJBCommandUtil.i.getValueAsInteger(this, "profileId");
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

            isUniqueLabel(profileId, label, userId, companyId);
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }

        if (isRead) {
            Integer profileId = EJBCommandUtil.i.getValueAsInteger(this, "profileId");
            read(profileId);
        }
    }

    /**
     * Read the <code>ImportProfile</code> object associated to <code>profileId</code> parameter, delete unused
     * <code>ImportColumn</code> objects, this happen when the associated elements ( telecoms or categories ) were
     * deleted.
     * <p/>
     * After of this put in  <code>resultDTO</code> the next keys:
     * <p/>
     * 1.-  importProfile: contains a <code>ImportProfileDTO</code> object if <code>ImportProfile</code> exists
     * or contains a <code>null</code> value if the <code>ImportProfile</code> was delete.
     * <p/>
     * 2.-  importColumns: contains a <code>List</code> of <code>ImportColumnDTO</code> objects asociated to <code>ImportProfile</code>
     * or contains a <code>null</code> value if the <code>ImportProfile</code> was delete.
     * <p/>
     * 3.- deletedColumns: contains a <code>List</code> of <code>ImportColumnDTO</code> objects, but they were deleted.
     *
     * @param profileId ImportProfile identifier.
     */
    private void read(Integer profileId) {
        ImportProfileDTO importProfileDTO = null;
        List<ImportColumnDTO> importColumnDTOs = null;
        List<ImportColumnDTO> deletedColumnDTOs = null;

        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));

        ImportProfile importProfile = (ImportProfile) ExtendedCRUDDirector.i.read(new ImportProfileDTO(profileId), resultDTO, checkReferences);

        if (importProfile == null) {
            resultDTO.put("importProfile", importProfileDTO);
            resultDTO.put("importColumns", importColumnDTOs);
            resultDTO.put("deletedColumns", deletedColumnDTOs);
            return;
        }

        importProfileDTO = new ImportProfileDTO();
        importColumnDTOs = new ArrayList<ImportColumnDTO>();
        deletedColumnDTOs = new ArrayList<ImportColumnDTO>();

        //read the importProfile configuration
        List<CompoundGroup> configuration =
                getConfiguration(importProfile.getProfileType(), importProfile.getCompanyId());

        List<Column> allColumns = getAllColumns(configuration);
        List<ImportColumn> columnsToDelete = new ArrayList<ImportColumn>();

        DTOFactory.i.copyToDTO(importProfile, importProfileDTO);

        List importColumns = getImportColumns(importProfile.getProfileId(), importProfile.getCompanyId());
        for (int i = 0; i < importColumns.size(); i++) {
            ImportColumn importColumn = (ImportColumn) importColumns.get(i);

            ImportColumnDTO importColumnDTO = new ImportColumnDTO();
            DTOFactory.i.copyToDTO(importColumn, importColumnDTO);

            //if configuration not contain the stored ImportColumn object, mark the object to delete
            if (!dbColumnExistsInConfiguration(importColumn, allColumns)) {
                deletedColumnDTOs.add(importColumnDTO);
                columnsToDelete.add(importColumn);
                continue;
            }

            importColumnDTOs.add(importColumnDTO);
        }

        //delete unused ImportColumn objects
        for (int i = 0; i < columnsToDelete.size(); i++) {
            ImportColumn importColumn = columnsToDelete.get(i);
            try {
                importColumn.remove();
            } catch (RemoveException e) {
                log.error("Could not remove the ImportColumn object, importColumnId="
                        + importColumn.getImportColumnId(), e);
            }
        }

        resultDTO.put("importProfile", importProfileDTO);
        resultDTO.put("importColumns", importColumnDTOs);
        resultDTO.put("deletedColumns", deletedColumnDTOs);
    }

    private void readImportColumns(ImportProfile importProfile) {
        List<ImportColumnDTO> importColumnDTOs = new ArrayList<ImportColumnDTO>();

        if (importProfile != null) {
            List importColumnList = getImportColumns(importProfile.getProfileId(), importProfile.getCompanyId());
            for (int i = 0; i < importColumnList.size(); i++) {
                ImportColumn importColumn = (ImportColumn) importColumnList.get(i);

                ImportColumnDTO importColumnDTO = new ImportColumnDTO();
                DTOFactory.i.copyToDTO(importColumn, importColumnDTO);
                importColumnDTOs.add(importColumnDTO);
            }
        }
        resultDTO.put("importColumns", importColumnDTOs);
    }

    private boolean dbColumnExistsInConfiguration(ImportColumn importColumn, List<Column> allColumns) {
        for (int i = 0; i < allColumns.size(); i++) {
            Column column = allColumns.get(i);

            if (column.getColumnId().equals(importColumn.getColumnId())) {
                return true;
            }
        }

        return false;
    }

    private List<Column> getAllColumns(List<CompoundGroup> configuration) {
        List<Column> allColumns = new ArrayList<Column>();
        for (int i = 0; i < configuration.size(); i++) {
            CompoundGroup group = configuration.get(i);
            allColumns.addAll(group.getAllColumns());
        }

        return allColumns;
    }

    /**
     * Invoque to <code>DataImportService</code> methods according to <code>profileType</code> and
     * <code>companyId</code> to obtain the associated configuration.
     *
     * @param profileType <code>Integer</code> value that is associated to <code>ContactConstants.ImportProfileType</code>
     * @param companyId   <code>Integer</code> value that is the company identifier
     * @return <code>List</code> of <code>CompoundGroup</code> that represents the configuration associated to
     *         <code>profileType</code> parameter.
     */
    private List<CompoundGroup> getConfiguration(Integer profileType, Integer companyId) {
        DataImportServiceHome dataImportServiceHome =
                (DataImportServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DATAIMPORTSERVICE);

        DataImportService service;

        List<CompoundGroup> result = new ArrayList<CompoundGroup>();

        try {
            service = dataImportServiceHome.create();
        } catch (CreateException e) {
            log.error("Can not create DataImportService Object", e);
            return result;
        }

        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(profileType)) {
            result.add(service.getOrganizationGroup(companyId));
        }

        if (ContactConstants.ImportProfileType.PERSON.equal(profileType)) {
            result.add(service.getContactGroup(companyId));
        }

        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(profileType)) {
            result.addAll(service.getOrganizationAndContactPersonGroups(companyId));
        }

        return result;
    }

    /**
     * Manage the profiles according to profileId attribute, if the <code>profileId</code> is not defined (is null)
     * in <code>ImportProfileDTO</code> object, then a new <code>ImportProfile</code> entity is created.
     * <p/>
     * If the profileId attribute is defined in <code>ImportProfileDTO</code> then the associated
     * <code>ImportProfile</code> entity and his <code>ImportColumn</code> objects are updated.
     *
     * @param importProfileDTO <code>ImportProfileDTO</code> object than contain information to create or uptate the
     *                         associated <code>ImportProfile</code> entity.
     * @param columnDTOs       <code>List</code> of <code>ImportColumnDTO</code> every <code>ImportColumnDTO</code> contain
     *                         information to create or update the associated <code>ImportColumn</code> entity.
     */
    private void manageProfile(ImportProfileDTO importProfileDTO, List<ImportColumnDTO> columnDTOs) {
        Integer profileId = (Integer) importProfileDTO.get("profileId");
        ImportProfile importProfile = null;

        if (null == profileId) {
            log.debug("The profileId is " + profileId + " now I create a new Profile ");
            importProfile = createImportProfile(importProfileDTO);
            createImportColumns(importProfile, columnDTOs);
        } else {
            log.debug("The profileId is " + profileId + " now I Update the fields and columns");
            importProfile = updateImportProfile(importProfileDTO);
            updateImportColumns(importProfile, columnDTOs);
        }

        //read and set in result dto
        readImportColumns(importProfile);
    }


    /**
     * Verify if the <code>label</code> parameter is used by other <code>ImportProfile</code> object.
     * <p/>
     * All <code>ImportProfile</code> objects associated to some user must be contain a unique <code>label</code>
     * attribute.
     *
     * @param profileId <code>Integer</code> is the profile identifier, it is used to compare the stored
     *                  <code>ImportProfile</code> identifier and the parameter.
     * @param label     <code>String</code> than contains the label of the <code>ImportProfile</code>.
     * @param userId    <code>Integer</code> is the user identifier.
     * @param companyId <code>Integer</code> is the company identifier.
     * @return true if the <code>label</code> parameter is unique, false in other case, and put in
     *         <code>resultDTO</code> the result under key <code>'isUniqueLabel'</code>.
     */
    private boolean isUniqueLabel(Integer profileId,
                                  String label,
                                  Integer userId,
                                  Integer companyId) {
        ImportProfileHome importProfileHome =
                (ImportProfileHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTPROFILE);

        ImportProfile storedProfile = null;
        try {
            storedProfile = importProfileHome.findByLabel(label, userId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find ImportProfile object for label=" + label +
                    " userId=" + userId +
                    " companyId=" + companyId +
                    " in validateLabel Method");
        }

        //the Label were not used by another profile.
        if (null == storedProfile) {
            resultDTO.put("isUniqueLabel", true);
            return true;
        }

        //The storedProfile is updating
        if (storedProfile.getProfileId().equals(profileId)) {
            resultDTO.put("isUniqueLabel", true);
            return true;
        }

        resultDTO.put("isUniqueLabel", false);
        return false;
    }

    private List getImportColumns(Integer profileId, Integer companyId) {
        ImportColumnHome importColumnHome =
                (ImportColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTCOLUMN);
        try {
            return (List) importColumnHome.findByProfileId(profileId, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private ImportProfile getImportProfile(Integer profileId) {
        ImportProfileHome importProfileHome =
                (ImportProfileHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTPROFILE);

        try {
            return importProfileHome.findByPrimaryKey(profileId);
        } catch (FinderException e) {
            //Cannot find the profile
        }

        return null;
    }

    private ImportProfile updateImportProfile(ImportProfileDTO importProfileDTO) {
        return (ImportProfile) ExtendedCRUDDirector.i.update(importProfileDTO, resultDTO, false, false, false, "Fail");
    }

    private void updateImportColumns(ImportProfile importProfile,
                                     List<ImportColumnDTO> columnDTOs) {
        List oldImportColumns = getImportColumns(importProfile.getProfileId(), importProfile.getCompanyId());
        for (int i = 0; i < oldImportColumns.size(); i++) {
            ImportColumn importColumn = (ImportColumn) oldImportColumns.get(i);
            try {
                importColumn.remove();
            } catch (RemoveException e) {
                log.error("Can not remove the ImportColumn for profileId=" + importProfile.getProfileId(), e);
            }
        }

        createImportColumns(importProfile, columnDTOs);
    }

    private ImportProfile createImportProfile(ImportProfileDTO dto) {
        return (ImportProfile) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    private void createImportColumns(ImportProfile importProfile,
                                     List<ImportColumnDTO> columnDTOs) {
        for (int i = 0; i < columnDTOs.size(); i++) {
            ImportColumnDTO importColumnDTO = columnDTOs.get(i);
            importColumnDTO.put("profileId", importProfile.getProfileId());
            importColumnDTO.put("companyId", importProfile.getCompanyId());
            ExtendedCRUDDirector.i.create(importColumnDTO, resultDTO, false);
        }
    }

    private void delete() {
        ImportProfileDTO importProfileDTO = new ImportProfileDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(importProfileDTO, resultDTO, true, "Fail");
    }

    public boolean isStateful() {
        return false;
    }
}
