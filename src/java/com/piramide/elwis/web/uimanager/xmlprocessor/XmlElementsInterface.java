package com.piramide.elwis.web.uimanager.xmlprocessor;

/**
 * This class is an interface for the classes that handle XML elements
 *
 * @author Alvaro
 * @version $Id: XmlElementsInterface.java 9703 2009-09-12 15:46:08Z fernando $
 */

public interface XmlElementsInterface {
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, java.util.ArrayList data, java.util.HashMap env, java.util.HashMap sonsList);

    public void endXmlElement(String uri, String name, String qName, java.util.ArrayList data, java.util.HashMap env, java.util.HashMap sonsList);

    public void XmlCharacters(String lastName, char ch[], int start, int length, java.util.ArrayList data, java.util.HashMap env, java.util.HashMap sonsList);
}