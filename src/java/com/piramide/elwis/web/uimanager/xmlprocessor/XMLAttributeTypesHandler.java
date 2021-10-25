package com.piramide.elwis.web.uimanager.xmlprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler that retrieves the data of the file of attributes of styles represented in XML
 *
 * @author Alvaro
 * @version $Id: XMLAttributeTypesHandler.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class XMLAttributeTypesHandler extends DefaultHandler {
    /**
     * The las name processed
     */
    private String lastName;
    /**
     * The data processed
     */
    private static ArrayList data = new ArrayList();
    /**
     * The XML reader
     */
    static XMLReader parser;
    /**
     * The package name that contains the classes that handles the XML elements
     */
    private final String packageName = "com.piramide.elwis.web.uimanager.xmlprocessor.typetaghandlers";

    private static Log log = LogFactory.getLog(XMLAttributeTypesHandler.class);

    public XMLAttributeTypesHandler() {
        data = new ArrayList();
    }

    /**
     * The Handler that proccess the XML file of attributes
     *
     * @param sourceName
     * @param sourceType
     * @param validating
     * @param is
     */
    public XMLAttributeTypesHandler(String sourceName, int sourceType, boolean validating, InputStream is) {
        data = new ArrayList();
        try {
            SAXParserFactory spfactory = SAXParserFactory.newInstance();
            spfactory.setValidating(validating);
            SAXParser saxParser = spfactory.newSAXParser();

            parser = saxParser.getXMLReader();
            XMLAttributeTypesHandler handler = new XMLAttributeTypesHandler();
            parser.setContentHandler(handler);
            parser.setErrorHandler(new XMLErrorHandler());
            if (sourceType == XMLProcessor.FILEPATHTYPE) {
                parser.parse(new InputSource(sourceName));
            } else if (sourceType == XMLProcessor.URLTYPE) {
                URL url = new URL(sourceName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                InputSource source = new InputSource(in);
                String DTDUri = sourceName.substring(0, sourceName.lastIndexOf("/") + 1);
                source.setSystemId(DTDUri);
                parser.parse(source);
            } else if (sourceType == XMLProcessor.RELATIVEURLTYPE) {
                if (is != null) {
                    InputStreamReader in = new InputStreamReader(is);
                    InputSource source = new InputSource(in);
                    String DTDUri = sourceName.substring(0, sourceName.lastIndexOf("/") + 1);
                    source.setSystemId(DTDUri);
                    parser.parse(source);
                } else {
                    System.out.println("ERROR: the InputStream is invalid " + is);
                }
            }
        }
        catch (Exception ex) {
            parser = null;
            System.out.println("ERROR: Unable to instantiate parser(" + parser + ")\nsourceName:" + sourceName + " sourceType:" + sourceType);
            ex.printStackTrace();
        }
    }

    /**
     * Handle the start of a document
     */
    public void startDocument() throws SAXException {
        log.debug("Starting document process (XMLAttributeTypesHandler)");
    }

    /**
     * Handle the end of a document
     */
    public void endDocument() {
        log.debug("End document process (XMLAttributeTypesHandler)");
    }

    /**
     * Handle the start of an element.
     */
    public void startElement(String uri, String name, String qName, Attributes atts) {
        lastName = qName;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class classToRun = Class.forName(packageName + "." + qName, true, cl);
            XmlElementsInterface etlElement = (XmlElementsInterface) classToRun.newInstance();
            etlElement.startXmlElement(uri, name, qName, atts, data, new HashMap(), new HashMap());
        }
        catch (Exception e) {
            log.debug("ERROR IN startElement, qName: " + qName);
            e.printStackTrace();
        }
    }

    /**
     * Handle the end of an element.
     */
    public void endElement(String uri, String name, String qName) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class classToRun = Class.forName(packageName + "." + qName, true, cl);
            XmlElementsInterface etlElement = (XmlElementsInterface) classToRun.newInstance();
            etlElement.endXmlElement(uri, name, qName, data, new HashMap(), new HashMap());
        }
        catch (Exception e) {
            log.debug("ERROR IN endElement, qName: " + qName);
            e.printStackTrace();
        }
        lastName = "";
    }

    /**
     * Handle character data.
     */
    public void characters(char ch[], int start, int length) {
        try {
            if (!lastName.equals("")) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                Class classToRun = Class.forName(packageName + "." + lastName, true, cl);
                XmlElementsInterface etlElement = (XmlElementsInterface) classToRun.newInstance();
                etlElement.XmlCharacters(lastName, ch, start, length, data, new HashMap(), new HashMap());
            }
        }
        catch (Exception e) {
            log.debug("ERROR IN characters, lastName: " + lastName);
            e.printStackTrace();
        }
    }

    /**
     * Get's the data processed
     */
    public ArrayList getData() {
        return (data);
    }
}