package com.piramide.elwis.utils.webmail;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Util to manage webmail cache
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class WebmailCacheManager {
    private final static Log log = LogFactory.getLog(WebmailCacheManager.class);


    public static String PATH_ELWIS_CACHE_DIR = WebmailCacheManager.init();

    private static final String WEBMAIL_CACHE_DIR = "webmail-cache";
    private static final String PARSE_ERROR_DIR = "parseerror";

    private static String init() {
        try {
            String path = new File(ConfigurationFactory.getValue("elwis.temp.folder")).getCanonicalPath() + "/";
            log.debug("Initialize PATH ELWIS CACHE:" + path);
            return path;
        } catch (IOException e) {
            log.debug("Error in elwis cache path..", e);
        }
        return "/";
    }

    public static String pathWebmailCache(boolean slash) {
        return PATH_ELWIS_CACHE_DIR + WEBMAIL_CACHE_DIR + (slash ? "/" : "");
    }

    public static String pathParseError(boolean slash) {
        return pathWebmailCache(true) + PARSE_ERROR_DIR + (slash ? "/" : "");
    }

    public static String pathParseError_CreateIfNotExist(boolean slash) {
        String path = pathParseError(slash);
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }

    public static void saveParseErrorEmlMessage(byte[] parseErrorEml, String uidl) {

        if (parseErrorEml != null) {
            String pathToFile = pathParseError_CreateIfNotExist(true) + composeEmlMessageName(uidl);
            try {
                FileOutputStream stream = new FileOutputStream(pathToFile);
                stream.write(parseErrorEml);
                stream.close();
            } catch (IOException e) {
                log.debug("can't save eml file in wbmail cache...");
            }
        }
    }

    public static String composeEmlMessageName(String uidl) {
        return uidl + ".eml";
    }

    public static int getNumberOfParseErrorEmlFiles() {
        String path = pathParseError_CreateIfNotExist(false);
        File parseErrorDir = new File(path);

        String[] filesList = parseErrorDir.list();
        if (filesList != null) {
            return filesList.length;
        }
        return 0;
    }

    public static boolean existParseErrorEmlFile(String uidl) {
        String pathFile = pathParseError_CreateIfNotExist(true) + composeEmlMessageName(uidl);
        File file = new File(pathFile);
        return file.exists();
    }

}
