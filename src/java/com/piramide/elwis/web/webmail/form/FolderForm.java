package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FolderForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FolderForm extends WebmailDefaultForm {


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate FolderForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("cancel") == null) {

            errors = validateForm(errors, request);
            if (errors.isEmpty()) {

                String userMailid = super.getDto("userMailId").toString();

                String tableDB = WebMailConstants.TABLE_FOLDER;
                String fieldDB = "name";
                String fieldValueDB = getDto("folderName").toString();
                String fieldIdDB = "";
                String fieldIdValue = "";
                boolean isUpdate = "update".equals(getDto("op"));
                if (isUpdate) {
                    fieldIdDB = "folderid";
                    fieldIdValue = getDto("folderId").toString();
                }

                Map map = new HashMap();
                map.put("usermailid", userMailid);

                if (DataBaseValidator.i.isDuplicate(tableDB, fieldDB, fieldValueDB, fieldIdDB, fieldIdValue, map, isUpdate)) {
                    errors.add("folder", new ActionError("msg.Duplicated", getDto("folderName").toString()));
                }
            }

            if (getDto("ofCompose") != null) {
                request.setAttribute("setInForm", "compose");
            }

        }

        return errors;
    }

    private ActionErrors validateForm(ActionErrors errors, HttpServletRequest request) {

        if (GenericValidator.isBlankOrNull(getDto("folderName").toString())) {
            errors.add("folder", new ActionError("errors.required", JSPHelper.getMessage(request, "Webmail.folder.name")));
        }

        if (GenericValidator.isBlankOrNull(getDto("columnToShow").toString())) {
            errors.add("folder", new ActionError("errors.required", JSPHelper.getMessage(request, "Webmail.folder.columnToShow")));
        }
        return errors;
    }

}
