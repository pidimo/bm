package com.piramide.elwis.web.webmail;

import com.piramide.elwis.utils.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Util class to invoke a url that response an xml with data, and then return the especific value of the node
 * in XML structure.
 *
 * @author Fernando Montaño
 * @version $Id: URLExecutor.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class URLExecutor {

    public static final URLExecutor i = new URLExecutor();

    private URLExecutor() {
    }

    public URL invoke(String url) {
        URL result = null;
        try {
            result = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Document execute(String httpUrl) {
        URL url = null;
        Document xml = null;
        try {
            url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            xml = XMLUtil.i.parseXML(connection.getInputStream());
            connection.disconnect();
            return xml;
        } catch (MalformedURLException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        }
        return null;
    }

}
