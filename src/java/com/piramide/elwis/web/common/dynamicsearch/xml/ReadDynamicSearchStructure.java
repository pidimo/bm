package com.piramide.elwis.web.common.dynamicsearch.xml;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.util.ClassLoaderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalNameException;
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
public class ReadDynamicSearchStructure {
    private Log log = LogFactory.getLog(this.getClass());
    private Element rootElement;

    /**
     * build an jdom Document Object of the xml
     *
     * @param filePath path of the xml file
     * @throws java.io.IOException    throws when cant read xml
     * @throws org.jdom.JDOMException throws when cant parser xml
     */
    public ReadDynamicSearchStructure(String filePath) throws IOException, JDOMException {
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
    public ReadDynamicSearchStructure(URL url) throws IOException, JDOMException {
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
    public ReadDynamicSearchStructure(InputStream inputStream) throws IOException, JDOMException {
        SAXBuilder builder = getSAXBuilderWithDTDValidator();

        Document doc = builder.build(inputStream);
        // Get the root element
        rootElement = doc.getRootElement();
        try {
            inputStream.close();
        } catch (Exception e) {
            log.error("ReadDynamicSearchStructure()", e);
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
                return new InputSource(ClassLoaderUtil.getResourceAsStream(DynamicSearchConstants.DTD_FILE, ReadDynamicSearchStructure.class));
            }
        });
        return saxBuilder;
    }

    public List<Element> getDynamicSearchs() {
        List dynamicSearchList;
        dynamicSearchList = rootElement.getChildren(XmlConstants.ELEMENT_DYNAMICSEARCH);
        return dynamicSearchList;
    }

    public List<Element> getFields(Element dynamicSearchElement) {
        List<Element> resultList = new ArrayList<Element>();
        if (!dynamicSearchElement.getName().equals(XmlConstants.ELEMENT_DYNAMICSEARCH)) {
            throw new IllegalNameException("Not is nodePath element...");
        }

        Element fields = dynamicSearchElement.getChild(XmlConstants.ELEMENT_FIELDS);
        if (fields != null) {
            resultList = fields.getChildren(XmlConstants.ELEMENT_FIELD);
        }
        return resultList;
    }

    public List<Element> getFieldOperators(Element fieldElement) {
        List<Element> resultList = new ArrayList<Element>();
        if (!fieldElement.getName().equals(XmlConstants.ELEMENT_FIELD) && !fieldElement.getName().equals(XmlConstants.ELEMENT_INNERFIELD)) {
            throw new IllegalNameException("Not is nodePath element...");
        }

        Element operatorsElement = fieldElement.getChild(XmlConstants.ELEMENT_OPERATORS);
        if (operatorsElement != null) {
            resultList = operatorsElement.getChildren(XmlConstants.ELEMENT_OPERATOR);
        }
        return resultList;
    }

    public Element getInnerFieldElement(Element fieldElement) {
        if (!fieldElement.getName().equals(XmlConstants.ELEMENT_FIELD)) {
            throw new IllegalNameException("Not is nodePath element...");
        }
        return fieldElement.getChild(XmlConstants.ELEMENT_INNERFIELD);
    }

    public List<Element> getDynamicFields(Element dynamicSearchElement) {
        List<Element> resultList = new ArrayList<Element>();
        if (!dynamicSearchElement.getName().equals(XmlConstants.ELEMENT_DYNAMICSEARCH)) {
            throw new IllegalNameException("Not is nodePath element..." + XmlConstants.ELEMENT_DYNAMICSEARCH);
        }

        Element dynamicFields = dynamicSearchElement.getChild(XmlConstants.ELEMENT_DYNAMICFIELDS);
        if (dynamicFields != null) {
            resultList = dynamicFields.getChildren(XmlConstants.ELEMENT_DYNAMICFIELD);
        }
        return resultList;
    }

}
