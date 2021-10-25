package com.piramide.elwis.web.uimanager.xmlprocessor.typetaghandlers;

import com.piramide.elwis.web.uimanager.xmlprocessor.XmlElementsInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler for the element attributeType of the XML of attribute type definitions
 *
 * @author Alvaro
 * @version $Id: attributeType.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class attributeType implements XmlElementsInterface {
    /**
     * Handles the start of the element
     */
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, ArrayList data, HashMap env, HashMap sonsList) {
        HashMap hm = new HashMap();
        hm.put("attributeName", atts.getValue("name"));
        hm.put("itemList", new ArrayList());
        data.add(hm);
    }

    /**
     * Handles the end of the element
     */
    public void endXmlElement(String uri, String name, String qName, ArrayList data, HashMap env, HashMap sonsList) {
    }

    /**
     * Handles the content of the element
     */
    public void XmlCharacters(String lastName, char ch[], int start, int length, ArrayList data, HashMap env, HashMap sonsList) {
    }
}