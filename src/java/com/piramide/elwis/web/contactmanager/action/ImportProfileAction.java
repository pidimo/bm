package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.contactmanager.ImportProfileCmd;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.*;
import com.piramide.elwis.dto.contactmanager.ImportColumnDTO;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.deduplication.DeduplicationFactory;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import com.piramide.elwis.web.contactmanager.el.DataImportHelper;
import com.piramide.elwis.web.contactmanager.form.DataImportForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportProfileAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ImportProfileAction................." + request.getParameterMap());

        DataImportHelper dataImportHelper = new DataImportHelper();

        DataImportForm dataImportForm = (DataImportForm) form;

        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (!isSaveButtonPressed(request) &&
                !isSaveAndImportButtonPressed(request) &&
                !isImportButtonPressed(request)) {

            updateForm(dataImportForm, request);
            return mapping.getInputForward();
        }

        List<Column> selectedColumns = dataImportForm.getSelectedColumns();
        ResultDTO manageResultDTO = null;
        Integer profileId = null;

        if (isSaveButtonPressed(request) || isSaveAndImportButtonPressed(request)) {
            manageResultDTO = manageProfile(dataImportForm, selectedColumns, request);
            profileId = (Integer) manageResultDTO.get("profileId");
            if (isSaveButtonPressed(request)) {
                dataImportForm.rebuildSelectedColumns(request);
                return findDetailForward(profileId, mapping);
            }
        }

        if (isImportButtonPressed(request) || isSaveAndImportButtonPressed(request)) {
            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
            Integer userId = (Integer) user.getValue(Constants.USERID);
            String locale = (String) user.getValue("locale");

            String datePattern = JSPHelper.getMessage(request, "datePattern");
            String decimalPattern = JSPHelper.getMessage(request, "numberFormat.2DecimalPlaces");
            String cachePath = ConfigurationFactory.getValue("elwis.temp.folder");


            ArrayByteWrapper file = dataImportForm.getFileWrapped();


            String fileName = dataImportForm.getFileName();

            String skipHeaderUI = (String) dataImportForm.getDto("skipFirstRow");
            boolean skipHeader = false;
            if (!GenericValidator.isBlankOrNull(skipHeaderUI) && "true".equals(skipHeaderUI)) {
                skipHeader = true;
            }

            String profileType = (String) dataImportForm.getDto("profileType");

            //read profile info
            selectedColumns = addImportColumnIdInSelectedColumns(selectedColumns, manageResultDTO);

            DataImportConfiguration configuration = new DataImportConfiguration(skipHeader, new Integer(profileType), datePattern, decimalPattern, locale);
            try {

                if (isCheckDuplicateEnable(dataImportForm)) {

                    //before empty temp import records if exist, because existence of duplicates is validate in form
                    DataImportDelegate.i.emptyImportProfileDuplicates(profileId);

                    DataImportDelegate.i.importForDeduplicate(cachePath, selectedColumns, file, fileName, configuration, userId, companyId, profileId);

                    //todo: set deduplicate process
                    Map<Integer, List<Integer>> importRecordDuplicatesMap = DeduplicationFactory.i.importDeduplicateProcess(profileId, request);

                    DataImportDelegate.i.saveImportRecordWithoutDuplicate(importRecordDuplicatesMap, profileId, selectedColumns, configuration, userId, companyId);

                    addImportResultMessage(profileId, request);

                } else {
                    DataImportDelegate.i.importData(cachePath, selectedColumns, file, fileName, configuration, userId, companyId);
                    addImportSuccessMessage(profileId, request);
                }
            } catch (ImportErrorsException e) {
                processImportErrorsException(e.getValidationExceptionList(), dataImportForm, dataImportHelper, request);
                return findDetailForward(profileId, mapping);

            } catch (ValidationException e) {
                List<ValidationException> validationExceptionList = new ArrayList<ValidationException>();
                validationExceptionList.add(e);

                processImportErrorsException(validationExceptionList, dataImportForm, dataImportHelper, request);
                return findDetailForward(profileId, mapping);
            }
        }

        return findDetailForward(profileId, mapping);
    }

    private void processImportErrorsException(List<ValidationException> validationExceptionList, DataImportForm dataImportForm, DataImportHelper dataImportHelper, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (validationExceptionList != null) {
            for (ValidationException e : validationExceptionList) {

                if (e instanceof CSVCharsetException) {
                    errors.add("CSVCharsetException", new ActionError("DataImport.error.csvCharsetError"));
                } else if (e.getValidator() != null) {

                    if (e.getValidator() instanceof RequiredValidator) {
                        String columnLabel = dataImportHelper.getColumnLabel(e.getColumn(), request);
                        errors.add("emailError", new ActionError("DataImport.error.dataRequired",
                                columnLabel,
                                e.getRowIndex(),
                                e.getColumnIndex()));
                    }

                    if (e.getValidator() instanceof EmailValidator) {
                        errors.add("emailError", new ActionError("DataImport.error.invalidEmail", e.getRowIndex(), e.getColumnIndex()));
                    }

                    if (e.getValidator() instanceof IntegerValidator) {
                        errors.add("integerError", new ActionError("DataImport.error.invalidInteger", e.getRowIndex(), e.getColumnIndex()));
                    }

                    if (e.getValidator() instanceof DateValidator) {
                        errors.add("dateError", new ActionError("DataImport.error.invalidDate", e.getRowIndex(), e.getColumnIndex()));
                    }

                    if (e.getValidator() instanceof DecimalValidator) {
                        errors.add("decimalError", new ActionError("DataImport.error.invalidDecimal", e.getRowIndex(), e.getColumnIndex()));
                    }

                    if (e.getValidator() instanceof TextSizeValidator) {
                        Integer limit = ((TextSizeValidator) e.getValidator()).getLimit();
                        errors.add("textSizeError", new ActionError("DataImpoer.error.texOutOfRange", e.getRowIndex(), e.getColumnIndex(), limit));
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        dataImportForm.rebuildSelectedColumns(request);
    }


    private ActionForward findDetailForward(Integer profileId, ActionMapping mapping) {
        ActionForward forward;

        if (profileId != null) {
            ActionForwardParameters updateForwardParameters = new ActionForwardParameters();
            updateForwardParameters.add("profileId", profileId.toString()).
                    add("dto(profileId)", profileId.toString());

            forward = updateForwardParameters.forward(mapping.findForward("Success"));
        } else {
            forward = mapping.findForward("ImportMainSearch");
        }
        return forward;
    }

    private void addImportResultMessage(Integer profileId, HttpServletRequest request) {
        if (profileId != null) {
            if (DataImportDelegate.i.importProfileContainDuplicateRecords(profileId)) {
                ActionErrors errors = new ActionErrors();
                errors.add("containDuplicates", new ActionError("dataImport.error.importWithDuplicates"));
                saveErrors(request, errors);
            } else {
                addImportSuccessMessage(profileId, request);
            }
        }
    }

    private void addImportSuccessMessage(Integer profileId, HttpServletRequest request) {
        if (profileId != null) {
            ActionErrors errors = new ActionErrors();
            errors.add("successMessage", new ActionError("dataImport.msg.importSuccess"));
            saveErrors(request, errors);
        }
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

    private boolean isCheckDuplicateEnable(DataImportForm form) {
        return form.getDto("checkDuplicate") != null;
    }

    /**
     * Execute the <code>ImportProfileCmd</code> command with operation <code>'manageProfile'</code> to
     * create or update the <code>ImportProfile</code> and his selected columns.
     *
     * @param dataImportForm  <code>DataImportForm</code> object than contain the information to create or update
     *                        a <code>ImportProfile</code> entity.
     * @param selectedColumns <code>List</code> of <code>Column</code> that contain the selected columns to be create
     *                        or update <code>ImportColumn</code> entity.
     * @param request         <code>HttpServletRequest</code> used to execute the <code>ImportProfileCmd</code> command.
     */
    private ResultDTO manageProfile(DataImportForm dataImportForm,
                               List<Column> selectedColumns,
                               HttpServletRequest request) {
        ResultDTO resultDTO = null;
        User user = RequestUtils.getUser(request);

        ImportProfileCmd importProfileCmd = new ImportProfileCmd();
        importProfileCmd.setOp("manageProfile");
        importProfileCmd.putParam("selectedColumns", selectedColumns);
        importProfileCmd.putParam("profileId", dataImportForm.getDto("profileId"));
        importProfileCmd.putParam("label", dataImportForm.getDto("label"));
        importProfileCmd.putParam("profileType", dataImportForm.getDto("profileType"));
        importProfileCmd.putParam("skipFirstRow", dataImportForm.getDto("skipFirstRow"));
        importProfileCmd.putParam("checkDuplicate", dataImportForm.getDto("checkDuplicate"));
        importProfileCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        importProfileCmd.putParam("userId", user.getValue(Constants.USERID));
        importProfileCmd.putParam("importStartTime", dataImportForm.getDto("importStartTime"));

        try {
            resultDTO = BusinessDelegate.i.execute(importProfileCmd, request);
            dataImportForm.setDto("profileId", importProfileCmd.getResultDTO().get("profileId"));
        } catch (AppLevelException e) {
            log.error("Cannot execute ImportProfileCmd command on manageProfile method ", e);
        }

        return resultDTO;
    }


    private void updateForm(DataImportForm dataImportForm, HttpServletRequest request) {
        String changeProfile = (String) dataImportForm.getDto("changeProfile");
        if ("true".equals(changeProfile)) {
            readProfile(dataImportForm, request);
        }
    }

    /**
     * Execute the <code>ImportProfileCmd</code> command whith default operation, to read a <code>ImportProfile</code>
     * object after put in the formulary the fields of the selected <code>ImportProfile</code> and the associated
     * columns, also if exists deleted columns, build the error messages.
     * <p/>
     * The method execute the command only when the formulary was defined the <code>profileId</code> field.
     *
     * @param dataImportForm <code>DataImportForm</code> object to put the readed information.
     * @param request        <code>HttpServletRequest</code> used to execute <code>ImportProfileCmd</code>  command.
     */
    private void readProfile(DataImportForm dataImportForm, HttpServletRequest request) {
        String profileId = (String) dataImportForm.getDto("profileId");
        if (GenericValidator.isBlankOrNull(profileId)) {
            return;
        }

        ImportProfileCmd importProfileCmd = new ImportProfileCmd();
        importProfileCmd.putParam("profileId", profileId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(importProfileCmd, request);
            ImportProfileDTO importProfileDTO = (ImportProfileDTO) resultDTO.get("importProfile");

            if (null != importProfileDTO) {
                List<ImportColumnDTO> importColumnDTOs = (List<ImportColumnDTO>) resultDTO.get("importColumns");
                Integer profileType = (Integer) importProfileDTO.get("profileType");
                dataImportForm.rebuildSelectedColumns(importColumnDTOs, profileType.toString(), request);
                dataImportForm.getDtoMap().putAll(importProfileDTO);

                List<ImportColumnDTO> deletedColumnsDTOs = (List<ImportColumnDTO>) resultDTO.get("deletedColumns");
                ActionErrors errors = new ActionErrors();
                for (int i = 0; i < deletedColumnsDTOs.size(); i++) {
                    ImportColumnDTO deletedColumn = deletedColumnsDTOs.get(i);
                    String columnName = (String) deletedColumn.get("columnName");
                    if (null != columnName) {
                        errors.add("DeletedColumn" + i, new ActionError("dataImport.errors.StoredColumnNotFound",
                                columnName, deletedColumn.get("columnValue")));
                    }
                }

                if (!errors.isEmpty()) {
                    saveErrors(request, errors);
                }
            }

        } catch (AppLevelException e) {
            log.error("Cannot execute ImportProfileCmd command ", e);
        }
    }

    private List<Column> addImportColumnIdInSelectedColumns(List<Column> selectedColumns, ResultDTO manageResultDTO) {
        List<ImportColumnDTO> importColumnDTOList = (List<ImportColumnDTO>) manageResultDTO.get("importColumns");

        if (importColumnDTOList != null) {
            for (int i = 0; i < selectedColumns.size(); i++) {
                Column column = selectedColumns.get(i);
                Integer importColumnId = findImportColumnId(column, importColumnDTOList);

                column.setImportColumnId(importColumnId);
            }
        }
        return selectedColumns;
    }

    private Integer findImportColumnId(Column column, List<ImportColumnDTO> importColumnDTOList) {
        Integer importColumnId = null;

        for (int i = 0; i < importColumnDTOList.size(); i++) {
            ImportColumnDTO importColumnDTO = importColumnDTOList.get(i);

            if (column.getGroup().getGroupId().equals(importColumnDTO.get("groupId"))
                    && column.getColumnId().equals(importColumnDTO.get("columnId"))
                    && column.getUiPosition().equals(importColumnDTO.get("uiPosition"))) {
                importColumnId = (Integer) importColumnDTO.get("importColumnId");
                break;
            }
        }

        return importColumnId;
    }
}
