package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.cmd.contactmanager.dataimport.validator.ValidationException;
import com.piramide.elwis.utils.deduplication.ImportMergeWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper;
import com.piramide.elwis.web.contactmanager.form.ImportRecordDeduplicationForm;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportRecordDeduplicationAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ImportRecordDeduplicationAction........" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;

        if (isCancelled(request)) {
            log.debug("Is cancel...");
            return findCancelForward(defaultForm, mapping);
        }

        ActionForward forward = null;

        Integer importRecordId = new Integer(defaultForm.getDto("importRecordId").toString());
        Integer profileId = new Integer(defaultForm.getDto("profileId").toString());

        if (null != (forward = validateProfileExistence(request, mapping))) {
            return forward;
        }

        if (null != (forward = validateImportRecordExistence(importRecordId, defaultForm, request, mapping))) {
            return forward;
        }

        //process
        if (isMergeButtonPressed(request)) {
            String mergeSelectedValue = (String) defaultForm.getDto("importMerge");

            if (isImportMergeOption(mergeSelectedValue)) {
                forward = importRecordMergeProcess(importRecordId, profileId, defaultForm, mapping, request);
            } else if (isImportNeverthelessOption(mergeSelectedValue)) {
                forward = importNeverthelessProcess(importRecordId, profileId, defaultForm, mapping, request);
            } else if (isNotImportOption(mergeSelectedValue)) {
                forward = notImportProcess(importRecordId, profileId, defaultForm, mapping, request);
            }
        }

        if (forward == null) {
            forward = super.execute(mapping, form, request, response);
        }

        return forward;
    }

    private ActionForward importRecordMergeProcess(Integer importRecordId, Integer profileId, DefaultForm defaultForm, ActionMapping mapping, HttpServletRequest request) {
        ImportMergeWrapper importMergeWrapper = (ImportMergeWrapper) request.getAttribute("importMergeWrapperVar");
        DataImportDelegate.i.mergeImportRecord(importMergeWrapper);

        return findSuccessForward(importRecordId, profileId, defaultForm, mapping, request);
    }

    private ActionForward importNeverthelessProcess(Integer importRecordId, Integer profileId, DefaultForm defaultForm, ActionMapping mapping, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = Integer.valueOf(user.getValue("userId").toString());
        DataImportConfiguration configuration = ImportRecordDeduplicationForm.getDataImportConfiguration(profileId, request);

        try {
            DataImportDelegate.i.importNevertheless(importRecordId, userId, configuration);
        } catch (ValidationException e) {
            log.debug("error in import nevertheless..", e);
        }

        return findSuccessForward(importRecordId, profileId, defaultForm, mapping, request);
    }

    private ActionForward notImportProcess(Integer importRecordId, Integer profileId, DefaultForm defaultForm, ActionMapping mapping, HttpServletRequest request) {
        DataImportDelegate.i.notImportDuplicateProcess(importRecordId);
        return findSuccessForward(importRecordId, profileId, defaultForm, mapping, request);
    }

    private boolean isMergeButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("mergeButton");
    }

    private boolean isImportMergeOption(String mergeSelectedValue) {
        return mergeSelectedValue.startsWith(DeduplicationHelper.ImportMergePrefix.MERGE.getConstant());
    }

    private boolean isImportNeverthelessOption(String mergeSelectedValue) {
        return mergeSelectedValue.equals(DeduplicationHelper.ImportMergePrefix.IMPORT_ANYWAY.getConstant());
    }

    private boolean isNotImportOption(String mergeSelectedValue) {
        return mergeSelectedValue.equals(DeduplicationHelper.ImportMergePrefix.NOT_IMPORT.getConstant());
    }

    protected ActionForward findSuccessForward(Integer importRecordId, Integer profileId, DefaultForm defaultForm, ActionMapping mapping, HttpServletRequest request) {

        if (importRecordId != null && Functions.importRecordFixedHasDuplicateContactPerson(importRecordId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("childDuplicates", new ActionError("ImportRecord.deduplicate.msg.duplicateContactPerson"));
            saveErrors(request, errors);

            return findUpdateForward(importRecordId, profileId, mapping);
        }
        return mapping.findForward("Success");
    }

    private ActionForward findUpdateForward(Integer importRecordId, Integer profileId, ActionMapping mapping) {
        ActionForwardParameters updateForwardParameters = new ActionForwardParameters();
        updateForwardParameters.add("importRecordId", importRecordId.toString()).
                add("dto(importRecordId)", importRecordId.toString()).
                add("dto(profileId)", profileId.toString());

        ActionForward forward = updateForwardParameters.forward(mapping.findForward("SuccessUpdate"));
        return forward;
    }

    protected ActionForward findCancelForward(DefaultForm defaultForm, ActionMapping mapping) {
        return mapping.findForward("Cancel");
    }

    private ActionForward validateProfileExistence(HttpServletRequest request, ActionMapping mapping) {
        String profileId = request.getParameter("profileId");
        if (profileId == null || !Functions.existsDataImport(profileId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("ImportProfile", new ActionError("DataImport.NotFound"));
            saveErrors(request.getSession(), errors);
            //return main list
            return mapping.findForward("ImportMainSearch");
        }
        return null;
    }

    protected ActionForward validateImportRecordExistence(Integer importRecordId, DefaultForm defaultForm, HttpServletRequest request, ActionMapping mapping) {
        if (importRecordId == null || !Functions.existsImportRecord(importRecordId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("ImportRecord", new ActionError("ImportRecord.NotFound"));
            saveErrors(request.getSession(), errors);
            //return main list
            return mapping.findForward("Fail");
        }
        return null;
    }
}
