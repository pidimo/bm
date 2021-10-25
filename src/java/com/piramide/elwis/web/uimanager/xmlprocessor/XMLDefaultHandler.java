package com.piramide.elwis.web.uimanager.xmlprocessor;

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
 * This class is a handler that retrieves the data of the file of styles represented in XML
 *
 * @author Alvaro
 * @version $Id: XMLDefaultHandler.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class XMLDefaultHandler extends DefaultHandler {
    /**
     * The last name processed
     */
    private String lastName;
    /**
     * The data processed
     */
    private static ArrayList data = new ArrayList();
    /**
     * The environment data
     */
    private static HashMap env = new HashMap();
    /**
     * The XML reader
     */
    static XMLReader parser;
    /**
     * The package that contains the classes that handles the XML elemnts
     */
    private final String packageName = "com.piramide.elwis.web.uimanager.xmlprocessor.styletaghandlers";
    /**
     * The list of fathers and sons
     */
    private static HashMap sonsList = new HashMap();

    public XMLDefaultHandler() {
        data = new ArrayList();
    }

    /**
     * The Handler that proccess the XML file of CSS definitions
     *
     * @param sourceName
     * @param sourceType
     * @param validating
     * @param is
     */
    public XMLDefaultHandler(String sourceName, int sourceType, boolean validating, InputStream is) {
        data = new ArrayList();
        env = new HashMap();
        sonsList = new HashMap();
        try {
            SAXParserFactory spfactory = SAXParserFactory.newInstance();
            spfactory.setValidating(validating);
            SAXParser saxParser = spfactory.newSAXParser();

            parser = saxParser.getXMLReader();
            XMLDefaultHandler handler = new XMLDefaultHandler();
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
        catch (Exception e) {
            parser = null;
            System.out.println("ERROR: Unable to instantiate parser(" + parser + ")\nsourceName:" + sourceName + " sourceType:" + sourceType);
            e.printStackTrace();
        }
    }

    /**
     * Handle the start of a document
     */
    public void startDocument() throws SAXException {
        System.out.println("Starting document process (XMLDefaultHandler)");
    }

    /**
     * Handle the end of a document
     */
    public void endDocument() {
        System.out.println("End document process (XMLDefaultHandler)");
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
            etlElement.startXmlElement(uri, name, qName, atts, data, env, sonsList);
        }
        catch (Exception e) {
            System.out.println("ERROR IN startElement, qName: " + qName);
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
            etlElement.endXmlElement(uri, name, qName, data, env, sonsList);
        }
        catch (Exception e) {
            System.out.println("ERROR IN endElement, qName: " + qName);
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
                etlElement.XmlCharacters(lastName, ch, start, length, data, env, sonsList);
            }
        }
        catch (Exception e) {
            System.out.println("ERROR IN characters, lastName: " + lastName);
            e.printStackTrace();
        }
    }

    /**
     * Gets the data of the XML file
     */
    public ArrayList getData() {
        return (data);
    }

    /**
     * Gets the List of fathers and sons
     */
    public HashMap getSonsList() {
        return (sonsList);
    }
}