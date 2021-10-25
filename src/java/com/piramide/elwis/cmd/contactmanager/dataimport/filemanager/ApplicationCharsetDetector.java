package com.piramide.elwis.cmd.contactmanager.dataimport.filemanager;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class ApplicationCharsetDetector {
    private String fileCharset = null;
    private List<String> charsetsToDetect = new ArrayList<String>();
    private List<String> probableCharsets = new ArrayList<String>();

    public ApplicationCharsetDetector(String charset) {
        charsetsToDetect.add(charset.toLowerCase());
    }

    public void addCharset(String charset) {
        charsetsToDetect.add(charset.toLowerCase());
    }

    public String getCharset(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        detect(fileInputStream);
        return getCharset();
    }


    /**
     * Detects the charset associated to <code>InputStream</code> object.
     *
     * @param inputStream <code>InputStream</code> to detect the charset
     * @throws IOException When cannot manage the <code>InputStream</code> object
     */
    private void detect(InputStream inputStream) throws IOException {
        nsDetector nsDetector = new nsDetector();
        nsDetector.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                fileCharset = charset;
            }
        });

        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = inputStream.read(buf, 0, buf.length)) != -1) {

            // Check if the stream is only ascii.
            if (isAscii) {
                isAscii = nsDetector.isAscii(buf, len);
            }

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done) {
                done = nsDetector.DoIt(buf, len, false);
            }
        }

        nsDetector.DataEnd();

        if (isAscii) {
            done = true;
        }

        if (!done) {
            String prob[] = nsDetector.getProbableCharsets();
            for (int i = 0; i < prob.length; i++) {
                probableCharsets.add(prob[i].toLowerCase());
            }
        }
    }


    private String getCharset() {
        if (null != fileCharset) {
            return fileCharset;
        }

        if (probableCharsets.contains("nomatch")) {
            return null;
        }

        for (String charset : charsetsToDetect) {
            if (probableCharsets.contains(charset)) {
                return charset;
            }
        }

        return null;
    }
}
