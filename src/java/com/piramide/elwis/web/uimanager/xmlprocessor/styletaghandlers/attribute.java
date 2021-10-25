package com.piramide.elwis.web.uimanager.xmlprocessor.styletaghandlers;

import com.piramide.elwis.web.uimanager.xmlprocessor.XmlElementsInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a handler for the element attribute of the XML of css style definitions
 *
 * @author Alvaro
 * @version $Id: attribute.java 12327 2016-01-29 19:45:06Z miguel $
 */
public class attribute implements XmlElementsInterface {
    /**
     * Handles the start of the element
     */
    public void startXmlElement(String uri, String name, String qName, org.xml.sax.Attributes atts, ArrayList data, HashMap env, HashMap sonsList) {
        Object att_url = atts.getValue("url");
        String att_name = atts.getValue("name").toString().toLowerCase();
        Object att_value = atts.getValue("value");
        Object att_arguments = atts.getValue("arguments");
        Object att_obviate = atts.getValue("obviate");
        Object att_overwrite = atts.getValue("overwrite");

        forEnvironment(name, atts, data, env, att_name, att_value, att_url, att_arguments, att_obviate, att_overwrite);
        forData(name, atts, data, env, att_name, att_value, att_url, att_arguments, att_obviate, att_overwrite);
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

    /**
     * Processes for the environment
     */
    private void forEnvironment(String name, org.xml.sax.Attributes atts, ArrayList data, HashMap env,
                                String att_name, Object att_value, Object att_url, Object att_arguments,
                                Object obviate, Object overwrite) {
        Object lastName = env.get("last_ElementName");
        obviate = (obviate == null) ? "false" : obviate.toString();

        if (lastName != null && !lastName.equals("") && obviate.toString().equals("false")) {
            ArrayList myEnv = (ArrayList) env.get(lastName);
            HashMap hm = new HashMap();
            if (att_url != null && att_url.toString().length() > 0) {
                hm.put("url", att_url);
            }

            if (att_arguments != null && att_arguments.toString().length() > 0) {
                hm.put("arguments", att_arguments);
            }

            if (att_value != null && att_value.toString().length() > 0) {
                hm.put("value", att_value);
            }

            if (overwrite != null && overwrite.toString().length() > 0) {
                hm.put("overwrite", overwrite);
            }

            //add type of attribute for inherit (miky)
            hm.put("type", atts.getValue("type"));

            hm.put("name", att_name);
            insert(myEnv, hm);
        } else if (obviate.toString().equals("true")) {
            ArrayList myEnv = (ArrayList) env.get(lastName);
            HashMap toRemove = new HashMap();
            toRemove.put("name", att_name);
            myEnv = remove(myEnv, toRemove);
        }
    }

    /**
     * Processes for the data
     */
    private void forData(String name, org.xml.sax.Attributes atts, ArrayList data, HashMap env,
                         String att_name, Object att_value, Object att_url, Object att_arguments,
                         Object obviate, Object overwrite) {
        ArrayList style_attributtes = (ArrayList) ((HashMap) data.get(data.size() - 1)).get("data");
        HashMap hm = new HashMap();
        hm.put("name", att_name);
        obviate = (obviate == null) ? "false" : obviate.toString();

        if (obviate.toString().equals("false")) {
            if (att_url != null && att_url.toString().length() > 0) {
                hm.put("url", att_url);
            }

            if (att_value != null && att_value.toString().length() > 0) {
                hm.put("value", att_value);
            }

            if (att_arguments != null && att_arguments.toString().length() > 0) {
                hm.put("arguments", att_arguments);
            }

            hm.put("resource", atts.getValue("resource"));
            hm.put("type", atts.getValue("type"));
            Object configurable = atts.getValue("configurable");
            if (configurable != null) {
                hm.put("configurable", configurable.toString());
            } else {
                hm.put("configurable", "false");
            }

            if (overwrite != null && overwrite.toString().length() > 0) {
                hm.put("overwrite", overwrite);
            }

            style_attributtes = insert(style_attributtes, hm);
        } else {//Remove the attribute to obviate
            style_attributtes = remove(style_attributtes, hm);
        }
    }

    /**
     * Inserts a new attribute in the arrayList but if the new attribute already exist's(his name), then
     * rewrites the old attribute
     *
     * @param attributesList
     * @param newElement
     * @return
     */
    private ArrayList insert(ArrayList attributesList, HashMap newElement) {
        boolean wasFound = false;
        for (int i = 0; i < attributesList.size(); i++) {
            HashMap element_i = (HashMap) attributesList.get(i);
            if (element_i.get("name").toString().equals(newElement.get("name").toString())) {
                attributesList.set(i, newElement);
                i = attributesList.size();
                wasFound = true;
            }
        }
        if (!wasFound) {
            attributesList.add(newElement);
        }
        return (attributesList);
    }

    /**
     * Removes the removeElement of the ArrayList
     *
     * @param attributesList
     * @param removeElement
     * @return
     */
    private ArrayList remove(ArrayList attributesList, HashMap removeElement) {
        for (int i = 0; i < attributesList.size(); i++) {
            HashMap element_i = (HashMap) attributesList.get(i);
            if (element_i.get("name").toString().equals(removeElement.get("name").toString())) {
                attributesList.remove(i);
                i = attributesList.size();
            }
        }
        return (attributesList);
    }
}