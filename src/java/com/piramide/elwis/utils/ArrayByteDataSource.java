package com.piramide.elwis.utils;

import javax.activation.DataSource;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jan 28, 2005
 * Time: 5:27:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayByteDataSource implements DataSource {
    byte[] file;
    String name;

    public ArrayByteDataSource(byte[] file, String name) {
        this.name = name;
        this.file = file;
    }

    public String getContentType() {
        return "application/msword";
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(file);
    }

    public String getName() {
        return name != null ? name : "Attach.doc";
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStream stream = new ByteArrayOutputStream(file.length);
        stream.write(file);
        return stream;
    }
}
