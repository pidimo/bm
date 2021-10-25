package com.piramide.elwis.web.common.accessrightdatalevel.xml;

import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.dynamicsearch.util.ClassLoaderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class ReadAccessRightDataLevelStructure {
    private Log log = LogFactory.getLog(this.getClass());
    private Element rootElement;

    /**
     * build an jdom Document Object of the xml
     *
     * @param filePath path of the xml file
     * @throws java.io.IOException    throws when cant read xml
     * @throws org.jdom.JDOMException throws when cant parser xml
     */
    public ReadAccessRightDataLevelStructure(String filePath) throws IOException, JDOMException {
        File file = new File(filePath);
        SAXBuilder builder = getSAXBuilderWithDTDValidator();
        Document doc = builder.build(file);
        // Get the root element
        rootElement = doc.getRootElement();
    }

    /**
     * build an jdom Document Object of the xml
     *
     * @param url Url to xml
     * @throws java.io.IOException    throws when cant read xml
     * @throws org.jdom.JDOMException throws when cant parser xml
     */
    public ReadAccessRightDataLevelStructure(URL url) throws IOException, JDOMException {
        SAXBuilder builder = getSAXBuilderWithDTDValidator();
        Document doc = builder.build(url);
        // Get the root element
        rootElement = doc.getRootElement();
    }

    /**
     * build an jdom Document Object of the xml
     *
     * @param inputStream input stream with xml data
     * @throws java.io.IOException    throws when cant read xml
     * @throws org.jdom.JDOMException throws when cant parser xml
     */
    public ReadAccessRightDataLevelStructure(InputStream inputStream) throws IOException, JDOMException {
        SAXBuilder builder = getSAXBuilderWithDTDValidator();

        Document doc = builder.build(inputStream);
        // Get the root element
        rootElement = doc.getRootElement();
        try {
            inputStream.close();
        } catch (Exception e) {
            log.error("ReadAccessRightDataLevelStructure()", e);
        }
    }

    /**
     * get SAXBuilder with DTD validator
     *
     * @return SAXBuilder
     */
    private SAXBuilder getSAXBuilderWithDTDValidator() {
        SAXBuilder saxBuilder = new SAXBuilder(true);
        saxBuilder.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(String publicId, String systemId) throws IOException {
                return new InputSource(ClassLoaderUtil.getResourceAsStream(AccessRightDataLevelConstants.DTD_FILE, ReadAccessRightDataLevelStructure.class));
            }
        });
        return saxBuilder;
    }

    public List<Element> getDataLevelConfigs() {
        List<Element> resultList = new ArrayList<Element>();
        if (rootElement != null) {
            resultList = rootElement.getChildren(XmlConstants.ELEMENT_DATALEVELCONFIG);
        }
        return resultList;
    }

    public List<Element> getLists(Element dataLevelConfigElement) {
        List<Element> resultList = new ArrayList<Element>();

        Element lists = dataLevelConfigElement.getChild(XmlConstants.ELEMENT_LISTS);
        if (lists != null) {
            resultList = lists.getChildren(XmlConstants.ELEMENT_LIST);
        }
        return resultList;
    }

    public List<Element> getTables(Element dataLevelConfigElement) {
        List<Element> resultList = new ArrayList<Element>();

        Element tables = dataLevelConfigElement.getChild(XmlConstants.ELEMENT_TABLES);
        if (tables != null) {
            resultList = tables.getChildren(XmlConstants.ELEMENT_TABLE);
        }
        return resultList;
    }
}
