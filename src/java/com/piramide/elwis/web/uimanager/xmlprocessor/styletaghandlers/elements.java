package com.piramide.elwis.web.uimanager.xmlprocessor.styletaghandlers;

import com.piramide.elwis.web.uimanager.xmlprocessor.XmlElementsInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler for the element 'elements' of the XML of css style definitions
 *
 * @author Alvaro
 * @version $Id: elements.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class elements implements XmlElementsInterface {
    /**
     * Handles the start of the element
     */
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, ArrayList data, HashMap env, HashMap sonsList) {
        //For the data
        HashMap hm = new HashMap();
        hm.put("type", "section_open");
        hm.put("name", atts.getValue("name"));
        hm.put("resource", atts.getValue("resource"));
        hm.put("configurable", atts.getValue("configurable"));
        data.add(hm);
        //End For the data
    }

    /**
     * Handles the end of the element
     */
    public void endXmlElement(String uri, String name, String qName, ArrayList data, HashMap env, HashMap sonsList) {
        //For the data
        HashMap hm = new HashMap();
        hm.put("type", "section_close");
        data.add(hm);
        //End For the data
    }

    /**
     * Handles the content of the element
     */
    public void XmlCharacters(String lastName, char ch[], int start, int length, ArrayList data, HashMap env, HashMap sonsList) {
    }
}