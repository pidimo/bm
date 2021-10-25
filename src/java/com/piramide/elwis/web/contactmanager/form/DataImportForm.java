package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.ImportProfileCmd;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.CompoundGroup;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DinamicColumn;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.StaticColumn;
import com.piramide.elwis.dto.contactmanager.ImportColumnDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FileValidator;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import com.piramide.elwis.web.contactmanager.el.ColumnWrapper;
import com.piramide.elwis.web.contactmanager.el.DataImportHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * This class contain methods to validate all the fields of formulary, methods to build a structure that contains the
 * options than were selected in user interface, methods to check which button was pressed in user interface
 * and finally methods to put in <code>HttpServletRequest</code> object  required attributes and structures
 * for the jsp page can build the user interface.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public class DataImportForm extends DefaultForm {

    List<Column> selectedColumns = new ArrayList<Column>();

    List<ColumnWrapper> uiSelectedColumns = new ArrayList<ColumnWrapper>();

    ArrayByteWrapper fileWrapped = new ArrayByteWrapper();

    String fileName;

    Integer type;

    /**
     * Execute the validation logic for all fields in the formulary.
     * <p/>
     * The validations are executed only when user press 'save' button or 'saveAndImport' button or 'import' button.
     * <p/>
     * The next validation are executed:
     * <p/>
     * 1.- File validation: This validation is executed only when user press 'saveAndImport' button or 'import' button.
     * <p/>
     * 2.- Label validation: This validation is executed only when user press 'save' button or 'saveAndImport' button.
     * <p/>
     * 3.- Selected columns validation: This validation verifies all columns selected in user interface,
     * if the validation fails, the method returns inmediately the problem.
     * <p/>
     * 4.- Required columns validation: This validation depends of Selected columns validation it checks
     * if the required columns were selected.
     * <p/>
     * 5.- Multiple selection validation: This validation depends of Selected columns validation it checks if the some
     * column was selected many times in user interface and this can not be selected many times.
     *
     * @param mapping <code>ActionMapping</code> object.
     * @param request <code>HttpServletRequest</code> object used in general purposes.
     * @return <code>ActionErrors</code> object, it contains the problems when the validations fails.
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        if (!isSaveButtonPressed(request) &&
                !isSaveAndImportButtonPressed(request) &&
                !isImportButtonPressed(request)) {
            return new ActionErrors();
        }

        List<CompoundGroup> configuration = getConfiguration(request);

        ActionErrors errors = super.validate(mapping, request);

        if (isSaveAndImportButtonPressed(request)
                || isImportButtonPressed(request)) {
            validateFile(errors, request);
        }

        if (isSaveButtonPressed(request) || isSaveAndImportButtonPressed(request)) {
            List<ActionError> labelErrors = validateLabel(request);
            if (!labelErrors.isEmpty()) {
                addErrors(labelErrors, errors);
            }
        }

        uiSelectedColumns = filterSelectedColumns();
        List<ActionError> uiColumnErrors = validateUISelectedColumns(configuration);
        if (!uiColumnErrors.isEmpty()) {
            addErrors(uiColumnErrors, errors);
        } else {
            List<ActionError> requiredColumnsErrors = requiredColumnsValidation(configuration, request);
            if (!requiredColumnsErrors.isEmpty()) {
                addErrors(requiredColumnsErrors, errors);
            }

            List<ActionError> multipleSelectionErrors = validateMultipleSelection(request);
            if (!multipleSelectionErrors.isEmpty()) {
                addErrors(multipleSelectionErrors, errors);
            }
        }

        //validate import record duplicate existence
        if (errors.isEmpty()) {
            validateImportRecordDuplicateExistence(errors);
        }

        if (!errors.isEmpty()) {
            rebuildSelectedColumns(request);
        }

        return errors;
    }

    private void validateImportRecordDuplicateExistence(ActionErrors errors) {
        String op = (String) getDto("op");
        if ("update".equals(op)) {
            Integer profileId = new Integer(getDto("profileId").toString());
            if (DataImportDelegate.i.importProfileContainDuplicateRecords(profileId)) {
                errors.add("containDuplicates", new ActionError("dataImport.error.profileWithDuplicates"));
            }
        }
    }


    /**
     * Fill a <code>List</code> of <code>ColumnWrapper</code> objects that used to build the selected columns
     * in user interface.
     * <p/>
     * For every <code>ImportColumnDTO</code> of <code>importColumns List</code> search the associated
     * <code>Column</code> object.
     * <p/>
     * If can not find the associate <code>Column</code> object the <code>ImportColumnDTO</code> is skipped
     * the otherwise extracts from <code>Column</code> object aditional information (group name and column name)
     * to build a  <code>ColumnWrapper</code> object, finally this object is stored into <code>uiSelectedColumns</code>
     * that is used to build the selected columns in user interface.
     *
     * @param importColumns <code>List</code> of <code>ImportColumnDTO</code> objects, these objects are used to build
     *                      <code>ColumndWrapper</code> objects.
     * @param profileType   <code>String</code> value that is associated to <code>ContactConstants.ImportProfileType</code>
     * @param request       <code>HttpServletRequest</code> object used to recover resources, translations and general purpose.
     */
    public void rebuildSelectedColumns(List<ImportColumnDTO> importColumns,
                                       String profileType,
                                       HttpServletRequest request) {

        this.uiSelectedColumns = new ArrayList<ColumnWrapper>();

        DataImportHelper dataImportHelper = new DataImportHelper();

        List<CompoundGroup> configuration = getConfiguration(profileType, request);

        for (int i = 0; i < importColumns.size(); i++) {
            ImportColumnDTO importColumnDTO = importColumns.get(i);

            Integer columnId = (Integer) importColumnDTO.get("columnId");
            Integer groupId = (Integer) importColumnDTO.get("groupId");
            Integer uiPosition = (Integer) importColumnDTO.get("uiPosition");
            Integer columnValue = (Integer) importColumnDTO.get("columnValue");

            Column column = getColumn(configuration, groupId, columnId);

            //If the column was deleted in the configuration, then skip the associated importColumnDTO
            if (null == column) {
                continue;
            }

            String groupName = JSPHelper.getMessage(request, column.getGroup().getResourceKey());

            String columnName = "";
            if (column instanceof StaticColumn) {
                columnName = JSPHelper.getMessage(request, ((StaticColumn) column).getResourceKey());
            }

            if (column instanceof DinamicColumn) {
                columnName = dataImportHelper.getTranslation((DinamicColumn) column, request);
            }

            this.uiSelectedColumns.add(new ColumnWrapper(columnId.toString(),
                    groupId.toString(),
                    groupName,
                    columnName,
                    columnValue.toString(),
                    uiPosition));
        }

        setDto("columnCounter", uiSelectedColumns.size());
        rebuildSelectedColumns(request);
    }

    public void rebuildSelectedColumns(HttpServletRequest request) {
        Collections.sort(uiSelectedColumns, new Comparator<ColumnWrapper>() {
            public int compare(ColumnWrapper o1, ColumnWrapper o2) {
                if (o1.getUiPosition() < o2.getUiPosition()) {
                    return -1;
                }
                if (o2.getUiPosition() < o1.getUiPosition()) {
                    return 1;
                }
                return 0;
            }
        });

        request.setAttribute("selectedColumnsInUI", uiSelectedColumns);
    }

    private ColumnWrapper buildWrapper(String key, String value) {
        String keyParts[] = key.split("_");
        String groupId = keyParts[1];
        String columnId = keyParts[2];
        String uiPosition = keyParts[3];
        String columnName = (String) getDto("columnNameSelected_" + groupId + "_" + columnId + "_" + uiPosition);
        String groupName = (String) getDto("groupNameSelected_" + groupId + "_" + columnId + "_" + uiPosition);

        return new ColumnWrapper(columnId, groupId, groupName, columnName, value, new Integer(uiPosition));
    }


    /**
     * Validate the <code>label</code> field from the formulary.
     * The next validations are executed.
     * <p/>
     * 1.- Required validation: This validation is executed only when the user press 'save' button or
     * press 'save and import' button, if this validation fails the method returns inmediately a <code>List</code> of
     * <code>ActionError</code> object to report the problem.
     * <p/>
     * 2.- Unique validation: After of first validation, the method execute  <code>ImportProfileCmd</code>
     * command with operation <code>'isUniqueLabel'</code> to check if the <code>label</code> field in the formulary is
     * unique.
     *
     * @param request <code>HttpServletRequest</code> object to recover resources and execute
     *                <code>ImportProfileCmd</code> command.
     * @return <code>List</code> of <code>ActionError</code> objects if the <code>label</code>
     *         is missing or it not is unique or an empty <code>List</code> if the <code>label</code> pass succesfully
     *         the validations.
     */
    private List<ActionError> validateLabel(HttpServletRequest request) {
        String label = (String) getDto("label");

        List<ActionError> errors = new ArrayList<ActionError>();
        if (GenericValidator.isBlankOrNull(label)) {
            errors.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "dataImport.configuration.name")));
            return errors;
        }

        String profileId = (String) getDto("profileId");
        User user = RequestUtils.getUser(request);


        ImportProfileCmd importProfileCmd = new ImportProfileCmd();
        importProfileCmd.setOp("isUniqueLabel");
        importProfileCmd.putParam("profileId", profileId);
        importProfileCmd.putParam("label", label);
        importProfileCmd.putParam("userId", user.getValue(Constants.USERID));
        importProfileCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(importProfileCmd, request);
            Boolean isUniqueLabel = (Boolean) resultDTO.get("isUniqueLabel");
            if (!isUniqueLabel) {
                errors.add(new ActionError("dataImport.duplicateLabel.error", label));
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute ImportProfileCmd in validateLabel method ", e);
        }
        return errors;
    }

    private List<ActionError> validateUISelectedColumns(List<CompoundGroup> configuration) {
        List<ActionError> errors = new ArrayList<ActionError>();
        if (uiSelectedColumns.isEmpty()) {
            errors.add(new ActionError("dataImpor.error.columnNameRequired"));
            return errors;
        }

        Map<Integer, String> uniquePositions = new HashMap<Integer, String>();
        List<Integer> positionDuplicated = new ArrayList<Integer>();

        List<ColumnWrapper> deletedColumns = new ArrayList<ColumnWrapper>();
        for (ColumnWrapper wrapper : uiSelectedColumns) {
            Column column = getColumn(configuration, wrapper);

            if (null == column) {
                errors.add(new ActionError("dataImport.errors.SelectedNotFound",
                        wrapper.getColumnName(),
                        wrapper.getGroupName()));
                deletedColumns.add(wrapper);
                continue;
            }

            if (GenericValidator.isBlankOrNull(wrapper.getValue())) {
                errors.add(new ActionError("errors.required",
                        wrapper.getGroupName() + ": " + wrapper.getColumnName()));
                continue;
            }

            Integer columnPosition;
            try {
                columnPosition = new Integer(wrapper.getValue());
            } catch (NumberFormatException e) {
                errors.add(new ActionError("errors.integer",
                        wrapper.getGroupName() + ": " + wrapper.getColumnName()));
                continue;
            }

            if (null != columnPosition && columnPosition < 0) {
                errors.add(new ActionError("errors.integerPositive",
                        wrapper.getGroupName() + ": " + wrapper.getColumnName()));
                continue;
            }

            if (uniquePositions.containsKey(columnPosition)) {
                if (!positionDuplicated.contains(columnPosition)) {
                    positionDuplicated.add(columnPosition);
                    errors.add(new ActionError("dataImport.errors.PositionUsedByManyColumns", columnPosition));
                }
                continue;
            }

            uniquePositions.put(columnPosition, wrapper.getColumnName());

            Column newColumn = column.getCopy();
            newColumn.setPosition(columnPosition - 1);
            newColumn.setUiPosition(wrapper.getUiPosition());
            this.selectedColumns.add(newColumn);
        }

        if (!deletedColumns.isEmpty()) {
            uiSelectedColumns.removeAll(deletedColumns);
        }

        return errors;
    }

    private List<ColumnWrapper> filterSelectedColumns() {
        List<ColumnWrapper> result = new ArrayList<ColumnWrapper>();

        for (Object object : getDtoMap().entrySet()) {
            Map.Entry mapElement = (Map.Entry) object;
            String key = (String) mapElement.getKey();
            if (key.indexOf("dtoKey_") != -1) {
                String value = (String) mapElement.getValue();
                result.add(buildWrapper(key, value));
            }
        }

        return result;
    }


    private List<ActionError> validateMultipleSelection(HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();

        List<String> columnsCache = new ArrayList<String>();
        List<String> errorCache = new ArrayList<String>();

        DataImportHelper dataImportHelper = new DataImportHelper();

        for (Column column : selectedColumns) {
            if (column.isSelectableManyTimes()) {
                continue;
            }

            String key = column.getGroup().getGroupId() + "_" + column.getColumnId();
            if (!columnsCache.contains(key)) {
                columnsCache.add(key);
                continue;
            }

            if (errorCache.contains(key)) {
                continue;
            }

            errorCache.add(key);
            String columnName = "";
            if (column instanceof StaticColumn) {
                columnName = JSPHelper.getMessage(request, ((StaticColumn) column).getResourceKey());
            }
            if (column instanceof DinamicColumn) {
                columnName = dataImportHelper.getTranslation((DinamicColumn) column, request);
            }

            ActionError error = new ActionError("dataImport.errors.required.multipleSelected",
                    columnName,
                    JSPHelper.getMessage(request, column.getGroup().getResourceKey()));

            errors.add(error);
        }

        return errors;
    }

    private List<ActionError> requiredColumnsValidation(List<CompoundGroup> configuration, HttpServletRequest request) {
        DataImportHelper dataImportHelper = new DataImportHelper();
        List<Column> requiredColumns = new ArrayList<Column>();
        for (CompoundGroup compoundGroup : configuration) {
            requiredColumns.addAll(compoundGroup.getRequiredColumns());
        }

        List<ActionError> errors = new ArrayList<ActionError>();

        for (Column requiredColumn : requiredColumns) {
            boolean requiredColumnIsSelected = false;

            for (Column selectedColumn : selectedColumns) {
                if (selectedColumn.getColumnId().equals(requiredColumn.getColumnId())) {
                    requiredColumnIsSelected = true;
                    break;
                }
            }

            if (!requiredColumnIsSelected) {
                String groupName = JSPHelper.getMessage(request, requiredColumn.getGroup().getResourceKey());
                String colummName = "";
                if (requiredColumn instanceof StaticColumn) {
                    colummName = JSPHelper.getMessage(request, ((StaticColumn) requiredColumn).getResourceKey());
                }

                if (requiredColumn instanceof DinamicColumn) {
                    colummName = dataImportHelper.getTranslation((DinamicColumn) requiredColumn, request);
                }

                errors.add(new ActionError("dataImport.errors.required", colummName, groupName));
            }
        }

        return errors;
    }

    private Column getColumn(List<CompoundGroup> configuration, ColumnWrapper wrapper) {
        return getColumn(configuration, new Integer(wrapper.getGroupId()), new Integer(wrapper.getColumnId()));
    }

    private Column getColumn(List<CompoundGroup> configuration, Integer groupId, Integer columnId) {
        for (CompoundGroup compoundGroup : configuration) {
            Column column = compoundGroup.getColumn(groupId, columnId);
            if (null != column) {
                return column;
            }
        }

        return null;
    }


    private List<CompoundGroup> getConfiguration(HttpServletRequest request) {
        String profileType = (String) getDto("profileType");

        return getConfiguration(profileType, request);
    }


    /**
     * Invoque to <code>DataImportDelegate</code> methods according to <code>profileType</code> and
     * <code>companyId</code> to obtain the associated configuration.
     *
     * @param profileType <code>String</code> value that is associated to <code>ContactConstants.ImportProfileType</code>
     * @param request     <code>HttpServletRequest</code> object used to recover session properties
     * @return <code>List</code> of <code>CompoundGroup</code> that represents the configuration associated to
     *         <code>profileType</code> parameter.
     */
    private List<CompoundGroup> getConfiguration(String profileType, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        List<CompoundGroup> result = new ArrayList<CompoundGroup>();

        if (ContactConstants.ImportProfileType.ORGANIZATION.equal(profileType)) {
            result.add(DataImportDelegate.i.getOrganizationGroup(companyId));
        }

        if (ContactConstants.ImportProfileType.PERSON.equal(profileType)) {
            result.add(DataImportDelegate.i.getContactGroup(companyId));
        }

        if (ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.equal(profileType)) {
            result.addAll(DataImportDelegate.i.getOrganizationAndContactPersonGroups(companyId));
        }

        return result;
    }

    private ActionErrors addErrors(List<ActionError> customErrors, ActionErrors errors) {
        for (int i = 0; i < customErrors.size(); i++) {
            errors.add("customError_" + i, customErrors.get(i));
        }

        return errors;
    }

    private ActionErrors validateFile(ActionErrors errors, HttpServletRequest request) {
        FormFile file = (FormFile) getDto("file");

        if (file.getFileSize() == 0) {
            errors.add("required",
                    new ActionError("errors.required", JSPHelper.getMessage(request, "dataImport.File")));
            return errors;
        }

        //only csv and xls files are allowed
        if (!"csv".equals(FileValidator.i.getFileExtension(file.getFileName())) &&
                !"xls".equals(FileValidator.i.getFileExtension(file.getFileName()))) {
            errors.add("notAllowed", new ActionError("dataImport.error.invalidFile"));
            return errors;
        }

        if ("xls".equals(FileValidator.i.getFileExtension(file.getFileName()))) {
            ActionError error = FileValidator.i.validateExcelFile(file, "dataImport.error.invalidFile");
            if (null != error) {
                errors.add("notAllowed", error);
                return errors;
            }
        }

        User user = (User) request.getSession().getAttribute("user");
        double maxSize = Double.parseDouble(user.getValue("maxAttachSize").toString()); // Mb;
        double fileSize = Math.round(file.getFileSize() / (Math.pow(1024, 2)));

        if (maxSize < fileSize) {
            errors.add("excedded", new ActionError("Common.Error_FilemaxLengthExceeded", String.valueOf(fileSize)));
            return errors;
        }

        try {
            fileWrapped.setFileData(file.getFileData());
            fileName = file.getFileName();
            file.destroy();
        } catch (IOException e) {
            log.debug("-> Read File Fail ", e);
        }
        return errors;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveButton");
    }

    private boolean isSaveAndImportButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("saveAndImportButton");
    }

    private boolean isImportButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("importButton");
    }

    public List<Column> getSelectedColumns() {
        return selectedColumns;
    }

    public ArrayByteWrapper getFileWrapped() {
        return fileWrapped;
    }

    public void setFileWrapped(ArrayByteWrapper fileWrapped) {
        this.fileWrapped = fileWrapped;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
