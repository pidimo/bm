package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.deduplication.ContactMergeWrapper;
import com.piramide.elwis.utils.deduplication.exception.DeleteAddressException;
import com.piramide.elwis.utils.deduplication.exception.MergeAddressException;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.contactmanager.delegate.ContactMergeDelegate;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactDeduplicationAction  extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContactDeduplicationAction........" + request.getParameterMap());

        if (isCancelled(request)) {
            log.debug("Is cancel...");
            return mapping.findForward("Cancel");
        }

        ActionForward forward = null;
        DefaultForm defaultForm = (DefaultForm) form;

        Integer duplicateGroupId = new Integer(defaultForm.getDto("duplicateGroupId").toString());
        if (null != (forward = validateDuplicateGroupExistence(duplicateGroupId, request, mapping))) {
            return forward;
        }

        //process
        if (isMergeButtonPressed(request)) {
            String mergeSelectedValue = (String) defaultForm.getDto("contactMerge");
            if (isContactMergeOption(mergeSelectedValue)) {
                forward = contactMergeProcess(duplicateGroupId, mapping, request);
            } else if (isKeepAllOption(mergeSelectedValue)) {
                forward = keepAllProcess(duplicateGroupId, mapping, request);
            } else if (isKeepOption(mergeSelectedValue)) {
                forward = keepProcess(mergeSelectedValue, duplicateGroupId, mapping, request);
            } else if (isDeleteOption(mergeSelectedValue)) {
                forward = deleteProcess(mergeSelectedValue, duplicateGroupId, mapping, request);
            }
        }

        if (forward == null) {
            forward = super.execute(mapping, form, request, response);
        }

        return forward;
    }

    private ActionForward contactMergeProcess(Integer duplicateGroupId, ActionMapping mapping, HttpServletRequest request) {
        ContactMergeWrapper contactMergeWrapper = (ContactMergeWrapper) request.getAttribute("contactMergeWrapperVar");
        try {
            boolean mergeSuccess = ContactMergeDelegate.i.mergeDuplicateContact(contactMergeWrapper);
        } catch (MergeAddressException e) {
            ActionErrors errors = processMergeErrors(e);
            saveErrors(request.getSession(), errors);
            return findFailForward(duplicateGroupId, mapping);
        }
        return findSuccessForward(duplicateGroupId, mapping);
    }

    private ActionForward keepAllProcess(Integer duplicateGroupId, ActionMapping mapping, HttpServletRequest request) {
        try {
            ContactMergeDelegate.i.keepAllDuplicateAddressProcess(duplicateGroupId);
        } catch (MergeAddressException e) {
            ActionErrors errors = new ActionErrors();
            errors.add("keepErr", new ActionError("DedupliContact.deduplicate.keepError"));
            saveErrors(request.getSession(), errors);
            return findFailForward(duplicateGroupId, mapping);
        }
        return findSuccessForward(duplicateGroupId, mapping);
    }

    private ActionForward keepProcess(String mergeSelectedValue, Integer duplicateGroupId, ActionMapping mapping, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        String itemUIKey = deduplicationHelper.getContactKeepItemUIKey(mergeSelectedValue);

        Map addressRowMap = findAddressRowMap(itemUIKey, duplicateGroupId, request);
        if (addressRowMap != null) {
            Integer addressId = new Integer(addressRowMap.get("rowElementId").toString());
            try {
                ContactMergeDelegate.i.keepDuplicateAddressProcess(duplicateGroupId, addressId);
            } catch (MergeAddressException e) {
                ActionErrors errors = new ActionErrors();
                errors.add("keepErr", new ActionError("DedupliContact.deduplicate.keepError"));
                saveErrors(request.getSession(), errors);
                return findFailForward(duplicateGroupId, mapping);
            }
        }
        return findSuccessForward(duplicateGroupId, mapping);
    }

    private ActionForward deleteProcess(String mergeSelectedValue, Integer duplicateGroupId, ActionMapping mapping, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        String itemUIKey = deduplicationHelper.getContactDeleteItemUIKey(mergeSelectedValue);

        Map addressRowMap = findAddressRowMap(itemUIKey, duplicateGroupId, request);
        if (addressRowMap != null) {
            Integer addressId = new Integer(addressRowMap.get("rowElementId").toString());
            try {
                ContactMergeDelegate.i.deleteDuplicateAddressProcess(duplicateGroupId, addressId);

            } catch (DeleteAddressException e) {
                ActionErrors errors = processDeleteErrors(addressId, e, mapping, request);
                saveErrors(request, errors);
                return findFailForward(duplicateGroupId, mapping);
            }
        }
        return findSuccessForward(duplicateGroupId, mapping);
    }

    private Map findAddressRowMap(String itemUIKey, Integer duplicateGroupId, HttpServletRequest request) {
        Map addressRowMap = null;
        if (itemUIKey != null) {
            DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
            List<Map> rowListMap = deduplicationHelper.buildContactDeduplicationItemValues(duplicateGroupId, request);

            for (Iterator<Map> iterator = rowListMap.iterator(); iterator.hasNext();) {
                Map rowMap = iterator.next();
                String uiKey = (String) rowMap.get("uiKey");
                if (itemUIKey.equals(uiKey)) {
                    addressRowMap = rowMap;
                    break;
                }
            }
        }
        return addressRowMap;
    }

    private boolean isMergeButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("mergeButton");
    }

    private boolean isContactMergeOption(String mergeSelectedValue) {
        return mergeSelectedValue.startsWith(DeduplicationHelper.ContactMergePrefix.MERGE.getConstant());
    }

    private boolean isKeepAllOption(String mergeSelectedValue) {
        return mergeSelectedValue.equals(DeduplicationHelper.ContactMergePrefix.KEEP_ALL.getConstant());
    }

    private boolean isKeepOption(String mergeSelectedValue) {
        return mergeSelectedValue.startsWith(DeduplicationHelper.ContactMergePrefix.KEEP.getConstant());
    }

    private boolean isDeleteOption(String mergeSelectedValue) {
        return mergeSelectedValue.startsWith(DeduplicationHelper.ContactMergePrefix.DELETE.getConstant());
    }

    private ActionErrors processMergeErrors(MergeAddressException e) {
        ActionErrors errors = new ActionErrors();
        if (e.isUserMergeError()) {
            errors.add("mergeErr", new ActionError("DedupliContact.deduplicate.mergeError.user"));
        } else {
            errors.add("mergeErr", new ActionError("DedupliContact.deduplicate.mergeError"));
        }
        return errors;
    }

    private ActionErrors processDeleteErrors(Integer addressId, DeleteAddressException e, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        Map addressMap = Functions.getAddressMap(addressId);

        //check for integrity referential
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.put("addressId", addressId);
        addressDTO.put("addressType", ContactConstants.ADDRESSTYPE_PERSON);
        addressDTO.put("name1", addressMap.get("name1"));
        addressDTO.put("name2", addressMap.get("name2"));
        addressDTO.put("name3", addressMap.get("name3"));

        ResultDTO resultDTO = new ResultDTO();
        IntegrityReferentialChecker.i.check(addressDTO, resultDTO);

        if (resultDTO.isFailure()) {
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            request.setAttribute("dto", resultDTO); //to show referenced tables
        } else {
            errors.add("deleteErr", new ActionError("DedupliContact.deduplicate.deleteError"));
        }
        return errors;
    }

    private ActionForward findSuccessForward(Integer duplicateGroupId, ActionMapping mapping) {
        //if exist duplicate contacts, return to same page
        if (duplicateGroupId != null && Functions.existsDuplicateGroup(duplicateGroupId)) {
            return findFailForward(duplicateGroupId, mapping);
        }
        return mapping.findForward("Success");
    }

    private ActionForward findFailForward(Integer duplicateGroupId, ActionMapping mapping) {
        ActionForwardParameters updateForwardParameters = new ActionForwardParameters();
        updateForwardParameters.add("duplicateGroupId", duplicateGroupId.toString()).
                add("dto(duplicateGroupId)", duplicateGroupId.toString());

        ActionForward forward = updateForwardParameters.forward(mapping.findForward("Fail"));
        return forward;
    }

    private ActionForward validateDuplicateGroupExistence(Integer duplicateGroupId, HttpServletRequest request, ActionMapping mapping) {
        if (duplicateGroupId == null || !Functions.existsDuplicateGroup(duplicateGroupId)) {
            ActionErrors errors = new ActionErrors();
            errors.add("duplicateGroup", new ActionError("DedupliContact.duplicate.notFound"));
            saveErrors(request.getSession(), errors);
            //return main list
            return mapping.findForward("Fail");
        }
        return null;
    }
}
