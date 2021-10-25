package com.piramide.elwis.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * XML Util that parse an retrieve nodes from XML DOM Model.
 * This class improve functionality in net.java.dev.strutsejb.util.XMLUtil class according to this application.
 *
 * @author Fernando Montaño
 * @version $Id: XMLUtil.java 9123 2009-04-17 00:32:52Z fernando $
 */

public class XMLUtil {

    public static XMLUtil i = new XMLUtil();
    private DocumentBuilder documentBuilder;

    private XMLUtil() {
        documentBuilder = null;
    }

    public DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        if (documentBuilder == null) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = builderFactory.newDocumentBuilder();
        }
        return documentBuilder;
    }

    /**
     * Parses the XML stream read from Struts RunData, and creates
     * a DOM Document.
     *
     * @param is an InputStream.
     * @return a Document represents the parsed XML tree.
     */
    public Document parseXML(InputStream is) throws SAXException, IOException, ParserConfigurationException {
        return this.getDocumentBuilder().parse(is);

    }

    /**
     * Returns the first element that has the specified tag name.
     *
     * @param el      an Element to find.
     * @param tagName tag name to find.
     * @return an Element found.
     */
    public Element getFirstElement(Element el, String tagName) {
        return (Element) el.getElementsByTagName(tagName).item(0);
    }

    /**
     * Returns the value of the first element that has the specified tag
     * name.
     *
     * @param el      an Element to find.
     * @param tagName tag name to find.
     * @return an Element found.
     */
    public String getFirstElementValue(Element el, String tagName) {
        return XMLUtil
                .i
                .getFirstElement(el, tagName)
                .getFirstChild()
                .getNodeValue();
    }

}
