package com.piramide.elwis.web.dashboard.component.configuration.reader;


import com.piramide.elwis.web.dashboard.component.util.ResourceReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * @author : ivan
 */
public class ReadComponentDefinition {
    private Log log = LogFactory.getLog(this.getClass());
    private Document document;
    private Element rootElement;


    public ReadComponentDefinition(String filePath) throws IOException, JDOMException {
        log.debug("ReadComponentDefinition('" + filePath + "')");
        File file = new File(filePath);
        SAXBuilder builder = getSAXBuilderWithDTDValidator();

        document = builder.build(file);
        rootElement = document.getRootElement();
    }

    public ReadComponentDefinition(InputStream input) throws IOException, JDOMException {
        log.debug("ReadComponentDefinition(java.io.InputStream)");
        SAXBuilder builder = getSAXBuilderWithDTDValidator();
        document = builder.build(input);
        rootElement = document.getRootElement();
    }

    public Element getElementX(String elementName, Element e) {
        return e.getChild(elementName);
    }

    public List getChildrenOfElementX(String childrenName, Element parentElement) {
        return parentElement.getChildren(childrenName);
    }

    public List getChlidrenOfRoot(String childName) {
        List components =
                rootElement.getChildren(childName);

        return components;
    }

    public Element getRootElement() {
        return rootElement;
    }

    private SAXBuilder getSAXBuilderWithDTDValidator() {
        InputStream dtdStream = null;

        try {
            dtdStream = ResourceReader.getResource("component-def.dtd", this.getClass()).openStream();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read DTD file ", e);
        }

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
