package com.piramide.elwis.utils.deduplication;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.DataImportConfiguration;

import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportMergeWrapper {
    private Integer importRecordId;
    private Integer addressId;
    private List<ImportMergeField> importMergeFieldList;
    private DataImportConfiguration configuration;
    private Integer userId;
    private Integer profileId;
    private boolean isContactPerson;

    public ImportMergeWrapper() {
        isContactPerson = false;
    }

    public Integer getImportRecordId() {
        return importRecordId;
    }

    public void setImportRecordId(Integer importRecordId) {
        this.importRecordId = importRecordId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public List<ImportMergeField> getImportMergeFieldList() {
        return importMergeFieldList;
    }

    public void setImportMergeFieldList(List<ImportMergeField> importMergeFieldList) {
        this.importMergeFieldList = importMergeFieldList;
    }

    public DataImportConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(DataImportConfiguration configuration) {
        this.configuration = configuration;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public boolean isContactPerson() {
        return isContactPerson;
    }

    public void setIsContactPerson(boolean isContactPerson) {
        this.isContactPerson = isContactPerson;
    }
}
