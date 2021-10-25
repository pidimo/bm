package com.piramide.elwis.cmd.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public class ReportJrxmlCacheManager {

    private static final String USER_DIR = "jrxmlReportUser";
    private static final String REPORT_DIR = "report";

    public static String pathUserFolder(Object companyId, Object userId, boolean slash) {
        return ElwisCacheManager.pathCompanyFolder(companyId, true) + USER_DIR + "/" + userId + (slash ? "/" : "");
    }

    public static String pathReportFolder(Object companyId, Object userId, Object reportId, boolean slash) {
        return pathUserFolder(companyId, userId, true) + REPORT_DIR + "/" + reportId + (slash ? "/" : "");
    }

    public static String pathReportFolder_CreateIfNotExist(Object companyId, Object userId, Object reportId, boolean slash) {
        String path = pathReportFolder(companyId, userId, reportId, slash);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public static boolean deleteReportFolder(Object companyId, Object userId, Object reportId) {
        File dir = new File(pathReportFolder(companyId, userId, reportId, false));
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
            dir.delete();
            return true;
        }
        return false;
    }

    public static String saveReportArtifact(Object companyId, Object userId, Object reportId, byte[] fileData, String fileName) throws IOException {
        String pathToFile = pathReportFolder_CreateIfNotExist(companyId, userId, reportId, true) + fileName;
        FileOutputStream stream = new FileOutputStream(pathToFile);
        stream.write(fileData);
        stream.close();

        return pathToFile;
    }
}
