package com.piramide.elwis.web.utils;

//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;

public class XmlDOMUtil {
    Log log = LogFactory.getLog(super.getClass());

    public String DOMToString(Document doc) {
        this.log.debug("Converting XML to String........... ");
        StringBuilder stringBuilder = new StringBuilder();
        try {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            OutputFormat outputformat = new OutputFormat();
//            outputformat.setIndent(3);
//            outputformat.setIndenting(true);
//            outputformat.setOmitComments(false);
//            outputformat.setOmitXMLDeclaration(true);
//
//            XMLSerializer serializer = new XMLSerializer();
//            serializer.setOutputFormat(outputformat);
//            serializer.setOutputByteStream(stream);
//            serializer.asDOMSerializer();
//            serializer.serialize(doc.getDocumentElement());

           // stringBuilder = new StringBuilder(stream.toString());
        } catch (Exception e) {
            this.log.error(new StringBuilder().append("Error in converting a DOM tree to a String.... ").append(e.getMessage()).toString());
        }
        return stringBuilder.toString();
    }
}