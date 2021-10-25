package com.piramide.elwis.web.contactmanager.el.deduplication;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.Column;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DinamicColumn;
import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.StaticColumn;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.deduplication.ContactDeduplicationColumnBuilder;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.DeduplicationAddressDelegate;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import com.piramide.elwis.web.contactmanager.el.DataImportHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DeduplicationHelper {
    private Log log = LogFactory.getLog(this.getClass());

    public static enum ImportMergePrefix {
        NOT_IMPORT("notImport"),
        IMPORT_ANYWAY("importAnyway"),
        MERGE("merge");

        private String constant;
        private ImportMergePrefix(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum ContactMergePrefix {
        KEEP_ALL("allKeep"),
        KEEP("keep"),
        DELETE("delete"),
        MERGE("merge");

        private String constant;
        private ContactMergePrefix(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum RowType {
        IMPORT_RECORD("1"),
        ADDRESS("2");

        private String constant;
        private RowType(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static final String MERGE_SEPARATOR = "_";

    private boolean isContactPerson;
    private Integer organizationId;

    public DeduplicationHelper() {
        this.isContactPerson = false;
        this.organizationId = null;
    }

    public DeduplicationHelper(boolean isContactPerson) {
        this.isContactPerson = isContactPerson;
        this.organizationId = null;
    }

    public DeduplicationHelper(Integer organizationId) {
        this.organizationId = organizationId;
        this.isContactPerson = true;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isContactPerson() {
        return isContactPerson;
    }

    public List<Column> getImportProfileColumnsFixed(Integer profileId) {
        List<Column> columnList;
        if (isContactPerson()) {
            columnList = DeduplicationAddressDelegate.i.getImportProfileColumnsContactPersonFixed(profileId);
        } else {
            columnList = DeduplicationAddressDelegate.i.getImportProfileColumnsAddressFixed(profileId);
        }
        return columnList;
    }

    public List<DeduplicationItemWrapper> buildHeaderProfileColumns(Integer profileId, HttpServletRequest request) {
        List<DeduplicationItemWrapper> itemWrapperList = new ArrayList<DeduplicationItemWrapper>();

        List<Column> profileColumns = getImportProfileColumnsFixed(profileId);
        DataImportHelper dataImportHelper = new DataImportHelper();

        for (Column column : profileColumns) {
            String groupName = JSPHelper.getMessage(request, column.getGroup().getResourceKey());

            String columnName = "";
            if (column instanceof StaticColumn) {
                columnName = JSPHelper.getMessage(request, ((StaticColumn) column).getResourceKey());
            }

            if (column instanceof DinamicColumn) {
                columnName = dataImportHelper.getTranslation((DinamicColumn) column, request);
            }

            itemWrapperList.add(new DeduplicationItemWrapper(column.getPosition(), columnName));
        }

        return itemWrapperList;
    }

    public List<DeduplicationItemWrapper> buildHeaderContactColumns(HttpServletRequest request) {
        List<DeduplicationItemWrapper> itemWrapperList = new ArrayList<DeduplicationItemWrapper>();

        List<Column> contactColumns = getContactDeduplicationColumns(request);

        DataImportHelper dataImportHelper = new DataImportHelper();

        for (Column column : contactColumns) {
            String groupName = JSPHelper.getMessage(request, column.getGroup().getResourceKey());

            String columnName = "";
            if (column instanceof StaticColumn) {
                columnName = JSPHelper.getMessage(request, ((StaticColumn) column).getResourceKey());
            }

            if (column instanceof DinamicColumn) {
                columnName = dataImportHelper.getTranslation((DinamicColumn) column, request);
            }

            itemWrapperList.add(new DeduplicationItemWrapper(column.getPosition(), columnName));
        }

        return itemWrapperList;
    }

    public List<Map> buildImportDeduplicationItemValues(Integer importRecordId, Integer profileId, HttpServletRequest request) {
        List<Map> itemMapList = new ArrayList<Map>();

        Map importRecordValuesMap = composeImportRecordValues(importRecordId, profileId, request);
        itemMapList.add(importRecordValuesMap);

        List<Integer> addressIdList = DeduplicationAddressDelegate.i.getImportRecordDuplicateAddressIds(importRecordId);
        for (int i = 0; i < addressIdList.size(); i++) {
            Integer addressId = addressIdList.get(i);
            String title = String.valueOf(i + 1);
            Map addressValuesMap = composeAddressValues(addressId, profileId, title, request);

            itemMapList.add(addressValuesMap);
        }
        return itemMapList;
    }

    private Map composeImportRecordValues(Integer importRecordId, Integer profileId, HttpServletRequest request) {
        List<Column> profileColumns = getImportProfileColumnsFixed(profileId);

        List<DeduplicationItemWrapper> itemWrapperList = DeduplicationAddressDelegate.i.readImportRecordColumnValues(importRecordId, profileColumns);
        //define check box names
        for (DeduplicationItemWrapper itemWrapper : itemWrapperList) {
            itemWrapper.setCheckboxName(composeColumnCheckboxName(itemWrapper.getPosition()));
            itemWrapper.setCheckboxValue(composeImportRecordCheckboxValue(importRecordId, itemWrapper.getPosition()));
        }

        String title = JSPHelper.getMessage(request, "ImportRecord.deduplicate.rowTitle.importRecord");
        String key = composeImportRecordUIKey(importRecordId);

        Map map = composeRowMap(importRecordId, key, title, itemWrapperList, RowType.IMPORT_RECORD.getConstant());
        return map;
    }



    private Map composeAddressValues(Integer addressId, Integer profileId, String title, HttpServletRequest request) {
        List<Column> profileColumns = getImportProfileColumnsFixed(profileId);

        List<DeduplicationItemWrapper> itemWrapperList = DeduplicationAddressDelegate.i.readAddressColumnValues(addressId, organizationId, profileColumns);
        //define check box names
        for (DeduplicationItemWrapper itemWrapper : itemWrapperList) {
            itemWrapper.setCheckboxName(composeColumnCheckboxName(itemWrapper.getPosition()));
            itemWrapper.setCheckboxValue(composeAddressCheckboxValue(addressId, itemWrapper.getPosition()));
        }

        String key = composeAddressUIKey(addressId);

        Map map = composeRowMap(addressId, key, title, itemWrapperList, RowType.ADDRESS.getConstant());
        return map;
    }

    private Map composeRowMap(Integer rowElementId, String uiKey, String title, List<DeduplicationItemWrapper> items, String rowType) {
        Map map = new HashMap();
        map.put("rowElementId", rowElementId);
        map.put("uiKey", uiKey);
        map.put("rowTitle", title);
        map.put("itemList", items);
        map.put("rowType", rowType);
        return map;
    }

    public List<LabelValueBean> getImportMergeValuesList(Integer importRecordId, HttpServletRequest request) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        if (DataImportDelegate.i.isImportRecordDuplicate(importRecordId)) {

            result.add(new LabelValueBean(JSPHelper.getMessage(request, "ImportRecord.deduplicate.notImport"), ImportMergePrefix.NOT_IMPORT.getConstant()));
            result.add(new LabelValueBean(JSPHelper.getMessage(request, "ImportRecord.deduplicate.importNevertheless"), ImportMergePrefix.IMPORT_ANYWAY.getConstant()));

            List<Integer> addressIdList = DeduplicationAddressDelegate.i.getImportRecordDuplicateAddressIds(importRecordId);
            for (int i = 0; i < addressIdList.size(); i++) {
                Integer addressId = addressIdList.get(i);
                String key = mergeImportMergeValue(addressId);
                String label = JSPHelper.getMessage(request, "ImportRecord.deduplicate.mergeIn", String.valueOf(i + 1));
                result.add(new LabelValueBean(label, key));
            }
        }

        return result;
    }

    private String mergeImportMergeValue(Integer addressId) {
        return ImportMergePrefix.MERGE.getConstant() + composeAddressUIKey(addressId);
    }

    public String getImportMergeItemUIKey(String selectValue) {
        String itemUIKey = null;
        if (selectValue != null && selectValue.startsWith(ImportMergePrefix.MERGE.getConstant())) {
            int startIndex = ImportMergePrefix.MERGE.getConstant().length();
            itemUIKey = selectValue.substring(startIndex);
        }
        return itemUIKey;
    }

    public List<Column> getContactDeduplicationColumns(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        ContactDeduplicationColumnBuilder columnBuilder = new ContactDeduplicationColumnBuilder(companyId);
        return columnBuilder.buildColumnsToDeduplicate();
    }

    public List<Map> buildContactDeduplicationItemValues(Integer duplicateGroupId, HttpServletRequest request) {
        List<Map> itemMapList = new ArrayList<Map>();

        if (duplicateGroupId != null) {
            List<Integer> addressIdList = DeduplicationAddressDelegate.i.getContactDuplicateAddressIds(duplicateGroupId);
            for (int i = 0; i < addressIdList.size(); i++) {
                Integer addressId = addressIdList.get(i);
                String title = String.valueOf(i + 1);
                Map addressValuesMap = composeContactAddressValues(addressId, title, request);
                itemMapList.add(addressValuesMap);
            }
        }
        return itemMapList;
    }

    private Map composeContactAddressValues(Integer addressId, String title, HttpServletRequest request) {
        List<Column> contactColumns = getContactDeduplicationColumns(request);

        List<DeduplicationItemWrapper> itemWrapperList = DeduplicationAddressDelegate.i.readAddressColumnValues(addressId, null, contactColumns);
        //define check box names
        for (DeduplicationItemWrapper itemWrapper : itemWrapperList) {
            itemWrapper.setCheckboxName(composeColumnCheckboxName(itemWrapper.getPosition()));
            itemWrapper.setCheckboxValue(composeAddressCheckboxValue(addressId, itemWrapper.getPosition()));
        }

        String key = composeAddressUIKey(addressId);

        Map map = composeRowMap(addressId, key, title, itemWrapperList, RowType.ADDRESS.getConstant());
        return map;
    }

    public List<LabelValueBean> getContactMergeSelectValues(Integer duplicateGroupId, HttpServletRequest request) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        result.add(new LabelValueBean(JSPHelper.getMessage(request, "DedupliContact.merge.keepAll"), ContactMergePrefix.KEEP_ALL.getConstant()));

        if (duplicateGroupId != null) {
            List<Integer> addressIdList = DeduplicationAddressDelegate.i.getContactDuplicateAddressIds(duplicateGroupId);
            for (int i = 0; i < addressIdList.size(); i++) {
                Integer addressId = addressIdList.get(i);
                String itemName = String.valueOf(i + 1);
                String value = keepContactMergeValue(addressId);
                String label = JSPHelper.getMessage(request, "DedupliContact.merge.keep", itemName);
                result.add(new LabelValueBean(label, value));
            }

            for (int i = 0; i < addressIdList.size(); i++) {
                Integer addressId = addressIdList.get(i);
                String itemName = String.valueOf(i + 1);
                String value = deleteContactMergeValue(addressId);
                String label = JSPHelper.getMessage(request, "DedupliContact.merge.delete", itemName);
                result.add(new LabelValueBean(label, value));
            }

            for (int i = 0; i < addressIdList.size(); i++) {
                Integer addressId1 = addressIdList.get(i);
                String itemName1 = String.valueOf(i + 1);
                for (int j = 0; j < addressIdList.size(); j++) {
                    Integer addressId2 = addressIdList.get(j);
                    String itemName2 = String.valueOf(j + 1);

                    if (!addressId1.equals(addressId2)) {
                        String value = mergeContactMergeValue(addressId1, addressId2);
                        Object[] names = {itemName1, itemName2};
                        String label = JSPHelper.getMessage(request, "DedupliContact.merge.mergeInAndDelete", names);
                        result.add(new LabelValueBean(label, value));
                    }
                }
            }
        }
        return result;
    }

    private String keepContactMergeValue(Integer addressId) {
        return ContactMergePrefix.KEEP.getConstant() + composeAddressUIKey(addressId);
    }

    private String deleteContactMergeValue(Integer addressId) {
        return ContactMergePrefix.DELETE.getConstant() + composeAddressUIKey(addressId);
    }

    private String mergeContactMergeValue(Integer addressId1, Integer addressId2) {
        return ContactMergePrefix.MERGE.getConstant() + composeAddressUIKey(addressId1) + MERGE_SEPARATOR + composeAddressUIKey(addressId2);
    }

    public boolean isContactMergeSelectedItem(String selectValue, String uiKey) {
        if (selectValue != null && selectValue.startsWith(ContactMergePrefix.MERGE.getConstant())) {
            String itemUIKey1 = getContactMergeItemUIKey1(selectValue);
            String itemUIKey2 = getContactMergeItemUIKey2(selectValue);

            if (uiKey != null && (uiKey.equals(itemUIKey1) || uiKey.equals(itemUIKey2))) {
                return true;
            }
        }
        return false;
    }

    public String getContactMergeItemUIKey1(String selectValue) {
        String itemUIKey1 = null;
        if (selectValue != null && selectValue.startsWith(ContactMergePrefix.MERGE.getConstant())) {
            int startIndex = ContactMergePrefix.MERGE.getConstant().length();
            int separatorIndex = selectValue.indexOf(MERGE_SEPARATOR);

            itemUIKey1 = selectValue.substring(startIndex, separatorIndex);
        }
        return itemUIKey1;
    }

    public String getContactMergeItemUIKey2(String selectValue) {
        String itemUIKey2 = null;
        if (selectValue != null && selectValue.startsWith(ContactMergePrefix.MERGE.getConstant())) {
            int separatorIndex = selectValue.indexOf(MERGE_SEPARATOR);
            itemUIKey2 = selectValue.substring(separatorIndex + 1);
        }
        return itemUIKey2;
    }

    public String getContactDeleteItemUIKey(String selectValue) {
        String itemUIKey = null;
        if (selectValue != null && selectValue.startsWith(ContactMergePrefix.DELETE.getConstant())) {
            int startIndex = ContactMergePrefix.DELETE.getConstant().length();
            itemUIKey = selectValue.substring(startIndex);
        }
        return itemUIKey;
    }

    public String getContactKeepItemUIKey(String selectValue) {
        String itemUIKey = null;
        if (selectValue != null && selectValue.startsWith(ContactMergePrefix.KEEP.getConstant())) {
            int startIndex = ContactMergePrefix.KEEP.getConstant().length();
            itemUIKey = selectValue.substring(startIndex);
        }
        return itemUIKey;
    }

    private String composeAddressUIKey(Integer addressId) {
        return "A" + addressId;
    }

    public String composeImportRecordUIKey(Integer importRecordId) {
        return "R" + importRecordId;
    }

    public String composeColumnCheckboxName(Integer position) {
        return "C" + position;
    }

    private String composeAddressCheckboxValue(Integer addressId, Integer position) {
        return composeAddressUIKey(addressId) + "_" + position;
    }

    private String composeImportRecordCheckboxValue(Integer importRecordId, Integer position) {
        return composeImportRecordUIKey(importRecordId) + "_" + position;
    }

}
