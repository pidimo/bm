package com.piramide.elwis.service.contact.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.5
 */
public class DeduplicationNativeSqlFactory {
    private Log log = LogFactory.getLog(this.getClass());
    private DeduplicationNativeSql nativeSql;

    public static DeduplicationNativeSqlFactory i = new DeduplicationNativeSqlFactory();

    private DeduplicationNativeSqlFactory() {
        //if there other Data base manager, implement this if is necessary
        nativeSql = new GeneralDeduplicationNativeSql();
    }

    public String deleteRecordColumn(Integer profileId) {
        return nativeSql.deleteRecordColumn(profileId);
    }

    public String deleteRecordDuplicate(Integer profileId) {
        return nativeSql.deleteRecordDuplicate(profileId);
    }

    public String deleteChildImportRecord(Integer profileId) {
        return nativeSql.deleteChildImportRecord(profileId);
    }

    public String deleteImportRecord(Integer profileId) {
        return nativeSql.deleteImportRecord(profileId);
    }

    public ArrayList<String> deleteImportRecordWithoutDuplicateSqlList(Integer profileId) {
        return nativeSql.deleteImportRecordWithoutDuplicateSqlList(profileId);
    }

}
