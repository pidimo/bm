package com.piramide.elwis.web.uimanager.xmlprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class implements the methods to access to the functionality of XMLProcessor, and
 * a main method for the generation of the CSS file.
 *
 * @author Alvaro
 * @version $Id: XMLProcessor.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class XMLProcessor {
    public static int FILEPATHTYPE = 0;
    public static int URLTYPE = 1;
    public static int RELATIVEURLTYPE = 2;

    private static Log log = LogFactory.getLog(XMLProcessor.class);

    /**
     * Generates the CSS file
     *
     * @param args args[0]=type of source(-f=file, -u=url); args[1]=source name(file path or url)
     */
    public static void main(String args[]) {

        if (args.length < 2 || (args[0].length() == 2 && (args[0] == null || args[1] == null) ||
                (args[0] != null && (!args[0].toString().equals("-f") && !args[0].toString().equals("-u"))) ||
                (args[1] != null && args[1].toString().length() < 3))) {
            System.out.println("ERROR, invalid parameters passed to the application");
            showHelp();
        } else {
            try {
                int sourceType = FILEPATHTYPE;
                if (args[0].toString().equals("-u")) {
                    sourceType = URLTYPE;
                }
                HashMap XMLProccessResult = getStyleData(args[1], sourceType, true, null);
                ArrayList data = (ArrayList) (XMLProccessResult.get("styleData"));
                XMLCSS_FileGenerator fileGenerator = new XMLCSS_FileGenerator(data);
            }
            catch (Exception ex) {
                log.debug("ERROR, the source: " + args[0] + " has an error\nargs[0]=" + args[0] + " args[1]=" + args[1]);
                log.debug(ex.getStackTrace());
            }
        }
    }

    /**
     * Generates the DataStructure for generate the view of update UIManager
     *
     * @param data Is the data read of the XMLFile with the method getStyleData
     * @return the DataStructure
     */
    public static ArrayList getUIManagerStructure(ArrayList data) {
        ArrayList res = new ArrayList();
        ArrayList elements = new ArrayList();
        HashMap section = new HashMap();
        HashMap element = new HashMap();

        Iterator i = data.iterator();
        while (i.hasNext()) {
            HashMap hm_i = (HashMap) i.next();
            String type = hm_i.get("type").toString();
            if (type.equals("section_open")) {
                section = new HashMap();
                elements = new ArrayList();
                section.put("sectionName", hm_i.get("name"));
                section.put("sectionResource", hm_i.get("resource"));
                section.put("sectionConfigurable", hm_i.get("configurable"));
            }
            if (type.equals("element")) {
                element = new HashMap();
                element.put("elementName", hm_i.get("name"));
                element.put("elementConfigurable", hm_i.get("configurable"));
                element.put("elementResource", hm_i.get("resource"));
                element.put("elementClass", hm_i.get("class"));
                element.put("elementData", hm_i.get("data"));
                elements.add(element);

            }
            if (type.equals("section_close")) {
                section.put("sectionData", elements);
                res.add(section);
            }
        }
        return (res);
    }

    /**
     * Reads the attribute types of the fileName
     *
     * @param sourceName
     * @param sourceType
     * @param validate   if the parser validate the XML File
     * @param is         a InputStream to the attributes XML file
     * @return A data structure with the data of the XML file
     */
    public static ArrayList getAttributeTypes(String sourceName, int sourceType, boolean validate, InputStream is) {
        ArrayList res = new ArrayList();
        try {
            XMLAttributeTypesHandler attTypeHandler = new XMLAttributeTypesHandler(sourceName, sourceType, validate, is);
            res = attTypeHandler.getData();
        }
        catch (Exception ex) {
            log.debug("ERROR, the source: " + sourceName + " has an error");
            log.debug(ex.getStackTrace());
        }
        return (res);
    }

    /**
     * Reads the style structure of the XML file
     *
     * @param sourceName url or path of the XML file
     * @param sourceType type of the source(0=file or 1=url)
     * @param validate   if the parser validate the XML File
     * @param is         an InputStream to the style XML file
     * @return An data structure with the data of the XML file, and the Map of fathers and sons
     */
    public static HashMap getStyleData(String sourceName, int sourceType, boolean validate, InputStream is) {
        HashMap result = new HashMap();
        ArrayList res = new ArrayList();
        try {
            XMLDefaultHandler defaultHandler = new XMLDefaultHandler(sourceName, sourceType, validate, is);
            res = defaultHandler.getData();
            result.put("styleData", res);
            result.put("sonsMap", defaultHandler.getSonsList());
        }
        catch (Exception ex) {
            log.debug("ERROR, the source: " + sourceName + " has an error");
            log.debug(ex.getStackTrace());
        }
        return (result);
    }

    public static void showHelp() {
        StringBuffer message = new StringBuffer("\nXMLPROCESSOR.JAVA, Jatun S.R.L.\n\n");
        message.append("usage: XMLProcessor -modifier sourceName\n")
                .append("where:\n")
                .append("\tmodifier: f to an file source from a file path\n")
                .append("\t          u to an file source from an url\n")
                .append("\tsourceName: the source path or url depending of the modifier\n\n")
                .append("Cochabamba, 19/07/2005");
        System.out.println(message);
    }
}