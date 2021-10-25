package com.piramide.elwis.web.uimanager.xmlprocessor.styletaghandlers;

import com.piramide.elwis.web.uimanager.xmlprocessor.XmlElementsInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler for the element 'element' of the XML of css style definitions
 *
 * @author Alvaro
 * @version $Id: element.java 9714 2009-09-17 21:55:43Z fernando $
 */
public class element implements XmlElementsInterface {
    /**
     * Handles the start of the element
     */
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, ArrayList data, HashMap env, HashMap sonsList) {
        Object dad = atts.getValue("extends");
        forEnvironment(name, atts, data, env, dad);
        forData(name, atts, data, env, dad);
        forSonsList(atts, sonsList);

    }

    /**
     * Handles the end of the element
     */
    public void endXmlElement(String uri, String name, String qName, ArrayList data, HashMap env, HashMap sonsList) {
        env.remove("last_ElementName");
    }

    /**
     * Handles the content of the element
     */
    public void XmlCharacters(String lastName, char ch[], int start, int length, ArrayList data, HashMap env, HashMap sonsList) {
    }

    /**
     * Processes for the environment
     */
    private void forEnvironment(String name, org.xml.sax.Attributes atts, ArrayList data, HashMap env, Object dad) {
        if (dad != null && dad.toString().length() > 0) {
            //A copy of the environment of the base element is required
            ArrayList env_dad = (ArrayList) env.get(dad.toString());
            if (env_dad != null) {
                ArrayList env_dad_copy = new ArrayList(env_dad);
                env.put(atts.getValue("name"), env_dad_copy);
            }
        } else {
            env.put(atts.getValue("name"), new ArrayList());
        }
        env.put("last_ElementName", atts.getValue("name"));
    }

    //Processes for the data
    private void forData(String name, org.xml.sax.Attributes atts, ArrayList data, HashMap env, Object dad) {
        HashMap hm = new HashMap();
        hm.put("type", "element");
        hm.put("name", atts.getValue("name"));
        hm.put("class", atts.getValue("styleClass"));
        hm.put("extends", atts.getValue("extends"));
        Object resource = atts.getValue("resource");
        hm.put("resource", (resource != null ? resource.toString() : ""));

        Object configurable = atts.getValue("configurable");
        if (configurable != null) {
            hm.put("configurable", configurable.toString());
        } else {
            hm.put("configurable", "false");
        }
        if (dad != null && dad.toString().length() > 0) {
            ArrayList env_dad = (ArrayList) env.get(dad.toString());
            if (env_dad != null) {
                hm.put("data", EnvToData(env_dad));
            } else {
                System.out.println("ERROR, the base class is not defined");
            }
        } else {
            hm.put("data", new ArrayList());
        }

        data.add(hm);
    }

    private void forSonsList(org.xml.sax.Attributes atts, HashMap sonsList) {
        Object elementName = atts.getValue("name");
        Object elementDad = atts.getValue("extends");
        if (elementDad != null && elementDad.toString().length() > 0 && elementName != null) {
            ArrayList sons = new ArrayList();
            if (sonsList.get(elementDad.toString()) != null) {
                sons = (ArrayList) sonsList.get(elementDad.toString());
                sons.add(elementName);
            } else {
                sons.add(elementName);
            }
            sonsList.put(elementDad.toString(), sons);
        }
    }

    /**
     * Format an ArrayList of the environment to an ArrayList for the data
     */
    private ArrayList EnvToData(ArrayList array) {
        ArrayList res = new ArrayList();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                HashMap hm_i = (HashMap) array.get(i);
                HashMap newHm = new HashMap(hm_i);
                newHm.put("isInherit", "true");
                res.add(newHm);
            }
        }
        return (res);
    }

}