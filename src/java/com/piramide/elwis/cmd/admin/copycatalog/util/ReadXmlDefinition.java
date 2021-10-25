package com.piramide.elwis.cmd.admin.copycatalog.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author: ivan
 */
public class ReadXmlDefinition {
    private Log log = LogFactory.getLog(this.getClass());
    private Document document;


    public ReadXmlDefinition(String xmlPath) throws IOException, JDOMException {
        File xmlFile = new File(xmlPath);
        SAXBuilder builder = DtdReader.getSAXBuilder("copycatalog.dtd", this.getClass());
        document = builder.build(xmlFile);
    }

    public ReadXmlDefinition(InputStream xmlStream) throws IOException, JDOMException {
        SAXBuilder builBuilder = DtdReader.getSAXBuilder("copycatalog.dtd", this.getClass());
        document = builBuilder.build(xmlStream);
    }

    public Element getElementX(String elementName, Element e) {
        return e.getChild(elementName);
    }

    public List getChildrenOfElementX(String childrenName, Element parentElement) {
        return parentElement.getChildren(childrenName);
    }

    public List getChlidrenOfRoot(String childName) {
        List components =
                document.getRootElement().getChildren(childName);

        return components;
    }

    public Element getRootElement() {
        return document.getRootElement();
    }


    public static enum Catalogs {
        elementName("catalogs");

        private final String constant;

        Catalogs(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum Module {
        elementName("module"),
        attrPath("path"),
        attrModuleId("moduleId");

        private final String constant;

        Module(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum Catalog {
        elementName("catalog"),
        attrClassName("className");

        private final String constant;

        Catalog(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public static enum DefaultModule {
        elementName("defaultModule");
        private final String constant;

        DefaultModule(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }
}
