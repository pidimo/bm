package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.utils.deduplication.ContactMergeField;
import com.piramide.elwis.utils.deduplication.ContactMergeWrapper;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactDeduplicationForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ContactDeduplicationForm......." + getDtoMap());
        log.debug("form request parameter:" + request.getParameterMap());

        ActionErrors errors = super.validate(mapping, request);

        if (isMergeButtonPressed(request)) {
            String mergeSelectedValue = (String) getDto("contactMerge");
            if (GenericValidator.isBlankOrNull(mergeSelectedValue)) {
                errors.add("merge", new ActionError("DedupliContact.deduplicate.error.mergeOptionRequired"));
            } else {
                if (isContactMergeOption(mergeSelectedValue)) {
                    ActionErrors mergeErrors = mergeValidation(mergeSelectedValue, request);
                    if (!mergeErrors.isEmpty()) {
                        errors.add(mergeErrors);
                    }
                }
            }
        }

        return errors;
    }

    private boolean isContactMergeOption(String mergeSelectedValue) {
        return mergeSelectedValue.startsWith(DeduplicationHelper.ContactMergePrefix.MERGE.getConstant());
    }

    private ActionErrors mergeValidation(String mergeSelectedValue, HttpServletRequest request) {
        ActionErrors mergeErrors = new ActionErrors();

        Integer duplicateGroupId = new Integer(getDto("duplicateGroupId").toString());

        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();

        List<Map> rowListMap = deduplicationHelper.buildContactDeduplicationItemValues(duplicateGroupId, request);

        Map addressRowMap1 = new HashMap();
        Map addressRowMap2 = new HashMap();

        String itemUIKey1 = deduplicationHelper.getContactMergeItemUIKey1(mergeSelectedValue);
        String itemUIKey2 = deduplicationHelper.getContactMergeItemUIKey2(mergeSelectedValue);

        for (Iterator<Map> iterator = rowListMap.iterator(); iterator.hasNext();) {
            Map rowMap = iterator.next();
            String uiKey = (String) rowMap.get("uiKey");
            if (uiKey.equals(itemUIKey1)) {
                addressRowMap1 = rowMap;
            }
            if (uiKey.equals(itemUIKey2)) {
                addressRowMap2 = rowMap;
            }
        }

        List<DeduplicationItemWrapper> addressItemWrapperList1 = new ArrayList<DeduplicationItemWrapper>();
        if (!addressRowMap1.isEmpty()) {
            addressItemWrapperList1 = (List<DeduplicationItemWrapper>) addressRowMap1.get("itemList");
        }

        for (DeduplicationItemWrapper addressRecordWrapper : addressItemWrapperList1) {
            if (!GenericValidator.isBlankOrNull(addressRecordWrapper.getValue())) {
                String radioButtonName = addressRecordWrapper.getCheckboxName();
                String itemSelected = (String) getDto(radioButtonName);

                if (GenericValidator.isBlankOrNull(itemSelected)) {
                    mergeErrors.add("columnEmpty", new ActionError("DedupliContact.deduplicate.error.fieldNotSelected", findColumnNameByPosition(addressRecordWrapper.getPosition(), request)));
                }
            }
        }

        if (mergeErrors.isEmpty()) {
            buildContactMergeWrapper(duplicateGroupId, addressRowMap1, addressRowMap2, addressItemWrapperList1, request);
        }

        return mergeErrors;
    }

    private void buildContactMergeWrapper(Integer duplicateGroupId, Map addressRowMap1, Map addressRowMap2, List<DeduplicationItemWrapper> addressItemWrapperList1, HttpServletRequest request) {
        Integer addressId1 = new Integer(addressRowMap1.get("rowElementId").toString());
        Integer addressId2 = new Integer(addressRowMap2.get("rowElementId").toString());
        List<DeduplicationItemWrapper> addressItemWrapperList2 = (List<DeduplicationItemWrapper>) addressRowMap2.get("itemList");

        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        List<Column> columnList = deduplicationHelper.getContactDeduplicationColumns(request);

        List<ContactMergeField> mergeFieldList = new ArrayList<ContactMergeField>();

        for (int i = 0; i < addressItemWrapperList1.size(); i++) {
            DeduplicationItemWrapper addressRecordWrapper1 = addressItemWrapperList1.get(i);

            String radioButtonName = addressRecordWrapper1.getCheckboxName();
            String itemSelected = (String) getDto(radioButtonName);

            ContactMergeField contactMergeField = composeContactMergeField(itemSelected, addressItemWrapperList1, addressItemWrapperList2, columnList);
            if (contactMergeField != null && contactMergeField.isMergeField()) {
                mergeFieldList.add(contactMergeField);
            }
        }

        ContactMergeWrapper contactMergeWrapper = new ContactMergeWrapper();
        contactMergeWrapper.setDuplicateGroupId(duplicateGroupId);
        contactMergeWrapper.setAddressId1(addressId1);
        contactMergeWrapper.setAddressId2(addressId2);
        contactMergeWrapper.setContactMergeFieldList(mergeFieldList);

        //set in request merge wrapper
        request.setAttribute("contactMergeWrapperVar", contactMergeWrapper);
    }

    private ContactMergeField composeContactMergeField(String itemSelected, List<DeduplicationItemWrapper> addressItemWrapperList1, List<DeduplicationItemWrapper> addressItemWrapperList2, List<Column> columnList) {

        boolean mergeField;
        Column column = null;

        DeduplicationItemWrapper itemWrapper = containItemSelected(itemSelected, addressItemWrapperList1);
        if (itemWrapper != null) {
            mergeField = true;
        } else {
            mergeField = false;
            itemWrapper = containItemSelected(itemSelected, addressItemWrapperList2);
        }

        if (itemWrapper != null) {
            column = findColumnByPosition(itemWrapper.getPosition(), columnList);
        }

        ContactMergeField contactMergeField = null;
        if (column != null) {
            contactMergeField = new ContactMergeField(column, mergeField);
        }
        return contactMergeField;
    }

    private DeduplicationItemWrapper containItemSelected(String itemSelected, List<DeduplicationItemWrapper> itemWrapperList) {
        if (itemSelected != null) {
            for (DeduplicationItemWrapper itemWrapper : itemWrapperList) {
                if (itemSelected.equals(itemWrapper.getCheckboxValue())) {
                    return itemWrapper;
                }
            }
        }
        return null;
    }

    private String findColumnNameByPosition(Integer position, HttpServletRequest request) {
        String columnName = "";

        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        List<DeduplicationItemWrapper> columnItemWrapperList = deduplicationHelper.buildHeaderContactColumns(request);

        for (DeduplicationItemWrapper itemWrapper : columnItemWrapperList) {
            if (position.equals(itemWrapper.getPosition())) {
                columnName = itemWrapper.getValue();
                break;
            }
        }
        return columnName;
    }

    private Column findColumnByPosition(Integer position, List<Column> columnList) {
        for (Column column : columnList) {
            if (position.equals(column.getPosition())) {
                return column;
            }
        }
        return null;
    }

    private boolean isMergeButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("mergeButton");
    }
}
