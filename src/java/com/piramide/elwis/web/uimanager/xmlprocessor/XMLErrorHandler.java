package com.piramide.elwis.web.uimanager.xmlprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is a error handler for the XMLHandlers that process the XML files
 *
 * @author Alvaro
 * @version $Id: XMLErrorHandler.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class XMLErrorHandler extends DefaultHandler {
    private static Log log = LogFactory.getLog(XMLErrorHandler.class);

    public void error(SAXParseException e) {
        log.debug("ERROR IN parsing the file: ");
        e.printStackTrace();
    }

    public void warning(SAXParseException e) {
        log.debug("A WARNING IN parsing the file: ");
        e.printStackTrace();
    }

    public void fatalError(SAXParseException e) {
        log.debug("FATAL ERROR IN parsing the file(cant continue): ");
        e.printStackTrace();
        System.exit(1);
    }
}