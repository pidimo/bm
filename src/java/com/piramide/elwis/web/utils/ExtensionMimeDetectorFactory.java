package com.piramide.elwis.web.utils;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.detector.ExtensionMimeDetector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 */
public class ExtensionMimeDetectorFactory {

    private static Log log = LogFactory.getLog(ExtensionMimeDetectorFactory.class);

    private ExtensionMimeDetector detector;

    public final static ExtensionMimeDetectorFactory i = new ExtensionMimeDetectorFactory();

    private ExtensionMimeDetectorFactory() {
        detector = new ExtensionMimeDetector();
    }

    public List<String> getMimeTypes(String fileName) {
        Collection mimeTypes = detector.getMimeTypesFileName(fileName);
        List<String> result = new ArrayList<String>();
        for (Object obj : mimeTypes) {
            MimeType mimeType = (MimeType) obj;
            result.add(mimeType.toString());
        }

        if (result.isEmpty()) {
            result.add("application/octet-stream");
        }


        return result;
    }

    public String getMimeType(String fileName) {
        String mimeType = getMimeTypes(fileName).get(0);
        log.debug("File name: " + fileName + ", mimeType: " + mimeType);
        return mimeType;
    }
}
