package com.piramide.elwis.service.contact.utils;

import java.util.ArrayList;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2.5
 */
public interface DeduplicationNativeSql {

    public String deleteRecordColumn(Integer profileId);

    public String deleteRecordDuplicate(Integer profileId);

    public String deleteChildImportRecord(Integer profileId);

    public String deleteImportRecord(Integer profileId);

    public ArrayList<String> deleteImportRecordWithoutDuplicateSqlList(Integer profileId);
}
