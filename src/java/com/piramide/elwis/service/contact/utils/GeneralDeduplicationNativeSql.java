package com.piramide.elwis.service.contact.utils;

import com.piramide.elwis.utils.ContactConstants;

import java.util.ArrayList;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.5
 */
public class GeneralDeduplicationNativeSql implements DeduplicationNativeSql {

    private static final String importrecord = ContactConstants.TABLE_IMPORTRECORD;
    private static final String importrecord_importrecordid = importrecord + ".importrecordid";
    private static final String importrecord_profileid = importrecord + ".profileid";

    private static final String recordcolumn = ContactConstants.TABLE_RECORDCOLUMN;
    private static final String recordduplicate = ContactConstants.TABLE_RECORDUPLICATE;

    public String deleteRecordColumn(Integer profileId) {
        return "DELETE FROM " + recordcolumn + " WHERE importrecordid IN (" + innerSelectProfileImportRecords(profileId) + ")";
    }

    public String deleteRecordDuplicate(Integer profileId) {
        return "DELETE FROM " + recordduplicate + " WHERE importrecordid IN (" + innerSelectProfileImportRecords(profileId) + ")";
    }

    private String innerSelectProfileImportRecords(Integer profileId) {
        return "SELECT " + importrecord_importrecordid + " FROM " + importrecord + " WHERE " + importrecord_profileid + " = " + profileId;
    }

    public String deleteChildImportRecord(Integer profileId) {
        return "DELETE FROM " + importrecord + " WHERE parentrecordid IS NOT NULL AND profileid = " + profileId;
    }

    public String deleteImportRecord(Integer profileId) {
        return "DELETE FROM " + importrecord + " WHERE profileid = " + profileId;
    }

    public ArrayList<String> deleteImportRecordWithoutDuplicateSqlList(Integer profileId) {
        ArrayList<String> sqlList = new ArrayList<String>();
        sqlList.add(deleteRecordColumnWithoutDuplicate(profileId));
        sqlList.add(deleteRecordDuplicateWithoutDuplicate(profileId));
        sqlList.add(deleteChildImportRecordWithoutDuplicate(profileId));
        sqlList.add(deleteImportRecordWithoutDuplicate(profileId));

        return sqlList;
    }

    protected String deleteRecordColumnWithoutDuplicate(Integer profileId) {
        return "DELETE FROM " + recordcolumn + " WHERE importrecordid IN (" + innerSelectProfileImportRecordsWithoutDuplicate(profileId) + ")";
    }

    protected String deleteRecordDuplicateWithoutDuplicate(Integer profileId) {
        return "DELETE FROM " + recordduplicate + " WHERE importrecordid IN (" + innerSelectProfileImportRecordsWithoutDuplicate(profileId) + ")";
    }

    private String innerSelectProfileImportRecordsWithoutDuplicate(Integer profileId) {
        return "SELECT r.importrecordid FROM " + importrecord + " as r  WHERE r.profileid = " + profileId + " AND \n" +
                " ((r.isduplicate = 0 AND r.parentrecordid IS NOT NULL AND r.parentrecordid NOT IN \n" +
                " (SELECT parent.importrecordid FROM importrecord as parent WHERE parent.importrecordid = r.parentrecordid AND parent.isduplicate = 1 AND parent.profileid = " + profileId + ")) \n" +
                " OR (r.isduplicate = 0 AND r.parentrecordid IS NULL AND r.importrecordid NOT IN \n" +
                " (SELECT child.parentrecordid FROM " + importrecord + " as child WHERE child.parentrecordid = r.importrecordid AND child.isduplicate = 1 AND child.profileid = " + profileId + " )))\n";
    }

    protected String deleteChildImportRecordWithoutDuplicate(Integer profileId) {
        return "DELETE FROM " + importrecord + " WHERE isduplicate = 0 AND profileid = " + profileId + " AND parentrecordid IS NOT NULL AND importrecordid NOT IN \n" +
                " (SELECT c.importrecordid FROM recordcolumn as c WHERE c.importrecordid = importrecordid)";
    }

    protected String deleteImportRecordWithoutDuplicate(Integer profileId) {
        return "DELETE FROM " + importrecord + " WHERE isduplicate = 0 AND profileid = " + profileId + " AND importrecordid NOT IN \n" +
                " (SELECT c.importrecordid FROM recordcolumn as c WHERE c.importrecordid = importrecordid)";
    }
}
