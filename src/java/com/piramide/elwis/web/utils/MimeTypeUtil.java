package com.piramide.elwis.web.utils;

import com.piramide.elwis.utils.Constants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class MimeTypeUtil {

    public static void formatResponseToDownloadFile(String fileName, int fileSize, HttpServletResponse response) {
        String contentType = getMimetype(fileName) + "; charset=" + Constants.CHARSET_ENCODING;
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentLength(fileSize);
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
    }

    public static String getMimetype(String fileName) {
        return ExtensionMimeDetectorFactory.i.getMimeType(fileName);
    }

    public static void copy(OutputStream output, InputStream input) throws IOException {
        final int COPY_BUFFER_SIZE = 32 * 1024;

        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int read;

        while (-1 != (read = input.read(buffer))) {
            output.write(buffer, 0, read);
        }
    }
}
