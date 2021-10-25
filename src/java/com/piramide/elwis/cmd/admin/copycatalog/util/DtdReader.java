package com.piramide.elwis.cmd.admin.copycatalog.util;

import com.piramide.elwis.utils.ResourceLoader;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: ivan
 */
public class DtdReader {

    public static SAXBuilder getSAXBuilder(String dtdFileName, Class clazz) {
        InputStream dtdStream = null;

        dtdStream = ResourceLoader.getResourceAsStream(dtdFileName, clazz);
        SAXBuilder saxBuilder = new SAXBuilder(true);
        final InputStream dtdStream1 = dtdStream;

        saxBuilder.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                return new InputSource(dtdStream1);
            }
        });
        return saxBuilder;
    }
}
