package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.ImportColumnDTO;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.contactmanager.form.DataImportForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DataImportReadAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DataImportReadAction........" + request.getParameterMap());
        DataImportForm dataImportForm = (DataImportForm) form;

        ActionForward forward = super.execute(mapping, form, request, response);
        log.debug("After of cmd execute...................." + request.getAttribute("dto"));

        if ("Success".equals(forward.getName())) {
            processAfterExecuteCmd(dataImportForm, request);
        }

        return forward;
    }

    private void processAfterExecuteCmd(DataImportForm dataImportForm, HttpServletRequest request) {
        Map resultDTO = (Map) request.getAttribute("dto");

        ImportProfileDTO importProfileDTO = (ImportProfileDTO) resultDTO.get("importProfile");

        if (null != importProfileDTO) {
            List<ImportColumnDTO> importColumnDTOs = (List<ImportColumnDTO>) resultDTO.get("importColumns");
            Integer profileType = (Integer) importProfileDTO.get("profileType");

            dataImportForm.rebuildSelectedColumns(importColumnDTOs, profileType.toString(), request);
            //dataImportForm.getDtoMap().putAll(importProfileDTO);

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
    }
}
