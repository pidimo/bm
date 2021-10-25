package com.piramide.elwis.web.uimanager.xmlprocessor.typetaghandlers;

import com.piramide.elwis.web.uimanager.xmlprocessor.XmlElementsInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler for the element valueItem of the XML of attribute type definitions
 *
 * @author Alvaro
 * @version $Id: valueItem.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class valueItem implements XmlElementsInterface {
    /**
     * Handles the start of the element
     */
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, ArrayList data, HashMap env, HashMap sonsList) {
        HashMap hm = (HashMap) data.get(data.size() - 1);
        HashMap newValue = new HashMap();
        newValue.put("itemName", atts.getValue("name"));
        newValue.put("itemValue", atts.getValue("value"));
        newValue.put("itemResource", atts.getValue("resource"));
        newValue.put("itemUrl", atts.getValue("url"));
        ArrayList values = (ArrayList) hm.get("itemList");
        values.add(newValue);
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