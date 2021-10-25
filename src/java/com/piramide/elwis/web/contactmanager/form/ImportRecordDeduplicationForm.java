package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;
import com.piramide.elwis.dto.contactmanager.ImportProfileDTO;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import com.piramide.elwis.utils.deduplication.ImportMergeField;
import com.piramide.elwis.utils.deduplication.ImportMergeWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
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
public class ImportRecordDeduplicationForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ImportRecordDeduplicationForm......." + getDtoMap());
        log.debug("form request parameter:" + request.getParameterMap());

        ActionErrors errors = super.validate(mapping, request);

        if (isMergeButtonPressed(request)) {
            String mergeSelectedKey = (String) getDto("importMerge");
            if (GenericValidator.isBlankOrNull(mergeSelectedKey)) {
                errors.add("merge", new ActionError("ImportRecord.deduplicate.error.mergeRowRequired"));
            } else {
                if (isImportMergeOption(mergeSelectedKey)) {
                    ActionErrors mergeErrors = mergeValidation(mergeSelectedKey, request);
                    if (!mergeErrors.isEmpty()) {
                        errors.add(mergeErrors);
                    }
                }
            }
        }

        return errors;
    }

    private boolean isImportMergeOption(String mergeSelectedKey) {
        return mergeSelectedKey.startsWith(DeduplicationHelper.ImportMergePrefix.MERGE.getConstant());
    }

    private boolean isDedupliContactPerson() {
        return "true".equals(getDto("isContactPerson"));
    }

    private DeduplicationHelper getDeduplicationHelper() {
        DeduplicationHelper deduplicationHelper;

        if (isDedupliContactPerson()) {
            Integer organizationId = new Integer(getDto("organizationId").toString());
            deduplicationHelper = new DeduplicationHelper(organizationId);
        } else {
            deduplicationHelper = new DeduplicationHelper();
        }

        return deduplicationHelper;
    }

    private ActionErrors mergeValidation(String mergeSelectedKey, HttpServletRequest request) {
        ActionErrors mergeErrors = new ActionErrors();

        Integer profileId = new Integer(getDto("profileId").toString());
        Integer importRecordId = new Integer(getDto("importRecordId").toString());

        DeduplicationHelper deduplicationHelper = getDeduplicationHelper();

        String importRecordRowKey = deduplicationHelper.composeImportRecordUIKey(importRecordId);
        String itemUIKey = deduplicationHelper.getImportMergeItemUIKey(mergeSelectedKey);

        List<Map> rowListMap = deduplicationHelper.buildImportDeduplicationItemValues(importRecordId, profileId, request);

        List<DeduplicationItemWrapper> importRecordItemWrapperList = new ArrayList<DeduplicationItemWrapper>();
        Map addressRowMap = new HashMap();

        for (Iterator<Map> iterator = rowListMap.iterator(); iterator.hasNext();) {
            Map rowMap = iterator.next();
            String uiKey = (String) rowMap.get("uiKey");
            if (uiKey.equals(importRecordRowKey)) {
                importRecordItemWrapperList = (List<DeduplicationItemWrapper>) rowMap.get("itemList");
            }
            if (uiKey.equals(itemUIKey)) {
                addressRowMap = rowMap;
            }
        }

        for (int i = 0; i < importRecordItemWrapperList.size(); i++) {
            DeduplicationItemWrapper importRecordWrapper = importRecordItemWrapperList.get(i);

            if (!GenericValidator.isBlankOrNull(importRecordWrapper.getValue())) {
                String radioButtonName = importRecordWrapper.getCheckboxName();

                String itemSelected = (String) getDto(radioButtonName);

                if (GenericValidator.isBlankOrNull(itemSelected)) {
                    mergeErrors.add("columnEmpty", new ActionError("ImportRecord.deduplicate.error.fieldNotSelected", findColumnNameByPosition(importRecordWrapper.getPosition(), profileId, request)));
                }
            }
        }

        if (mergeErrors.isEmpty()) {
            buildImportMergeWrapper(importRecordId, profileId, addressRowMap, importRecordItemWrapperList, request);
        }

        return mergeErrors;
    }

    private void buildImportMergeWrapper(Integer importRecordId, Integer profileId, Map addressRowMap, List<DeduplicationItemWrapper> importRecordItemWrapperList, HttpServletRequest request) {
        Integer addressId = new Integer(addressRowMap.get("rowElementId").toString());
        List<DeduplicationItemWrapper> addressItemWrapperList = (List<DeduplicationItemWrapper>) addressRowMap.get("itemList");

        List<ImportMergeField> mergeFieldList = new ArrayList<ImportMergeField>();

        for (int i = 0; i < importRecordItemWrapperList.size(); i++) {
            DeduplicationItemWrapper importRecordWrapper = importRecordItemWrapperList.get(i);

            String radioButtonName = importRecordWrapper.getCheckboxName();
            String itemSelected = (String) getDto(radioButtonName);

            if (itemSelected != null) {
                ImportMergeField importMergeField = composeImportMergeField(itemSelected, profileId, importRecordItemWrapperList, addressItemWrapperList);
                if (importMergeField != null && importMergeField.isKeepImportField()) {
                    mergeFieldList.add(importMergeField);
                }
            }
        }

        User user = RequestUtils.getUser(request);
        Integer userId = Integer.valueOf(user.getValue("userId").toString());

        ImportMergeWrapper importMergeWrapper = new ImportMergeWrapper();
        importMergeWrapper.setImportRecordId(importRecordId);
        importMergeWrapper.setAddressId(addressId);
        importMergeWrapper.setImportMergeFieldList(mergeFieldList);
        importMergeWrapper.setConfiguration(getDataImportConfiguration(profileId, request));
        importMergeWrapper.setUserId(userId);
        importMergeWrapper.setProfileId(profileId);
        importMergeWrapper.setIsContactPerson(isDedupliContactPerson());

        //set in request merge wrapper
        request.setAttribute("importMergeWrapperVar", importMergeWrapper);
    }

    private ImportMergeField composeImportMergeField(String itemSelected, Integer profileId, List<DeduplicationItemWrapper> importRecordItemWrapperList, List<DeduplicationItemWrapper> addressItemWrapperList) {

        DeduplicationHelper deduplicationHelper = getDeduplicationHelper();
        List<Column> columnList = deduplicationHelper.getImportProfileColumnsFixed(profileId);

        boolean keepImportField;
        Column column = null;
        DeduplicationItemWrapper itemWrapper = containItemSelected(itemSelected, importRecordItemWrapperList);

        if (itemWrapper != null) {
            keepImportField = true;
        } else {
            //address field is selected
            keepImportField = false;
            itemWrapper = containItemSelected(itemSelected, addressItemWrapperList);
        }

        if (itemWrapper != null) {
            column = findColumnByPosition(itemWrapper.getPosition(), columnList);
        }

        ImportMergeField importMergeField = null;
        if (column != null) {
            importMergeField = new ImportMergeField(column, keepImportField);
        }

        return importMergeField;
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

    private String findColumnNameByPosition(Integer position, Integer profileId, HttpServletRequest request) {
        String columnName = "";

        DeduplicationHelper deduplicationHelper = getDeduplicationHelper();
        List<DeduplicationItemWrapper> columnItemWrapperList = deduplicationHelper.buildHeaderProfileColumns(profileId, request);

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

    public static DataImportConfiguration getDataImportConfiguration(Integer profileId, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        String locale = (String) user.getValue("locale");

        String datePattern = JSPHelper.getMessage(request, "datePattern");
        String decimalPattern = JSPHelper.getMessage(request, "numberFormat.2DecimalPlaces");

        ImportProfileDTO importProfileDTO = DataImportDelegate.i.getImportProfileDTO(profileId);
        Integer profileType = null;
        if (importProfileDTO != null) {
            profileType = new Integer(importProfileDTO.get("profileType").toString());
        }

        DataImportConfiguration configuration = new DataImportConfiguration(false, profileType, datePattern, decimalPattern, locale);
        return configuration;
    }


    private boolean isMergeButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("mergeButton");
    }
}
