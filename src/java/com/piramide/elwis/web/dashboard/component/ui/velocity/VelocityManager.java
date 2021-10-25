package com.piramide.elwis.web.dashboard.component.ui.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author : ivan
 */
public class VelocityManager {

    public static String processTemplate(String templatePath, String templateName, Map contextMap) {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.put(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");

        VelocityEngine ve = new VelocityEngine();

        try {
            ve.init(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        *  next, get the Template
        */

        org.apache.velocity.Template t = null;
        try {
            t = ve.getTemplate(templatePath + "/" + templateName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         *  create a context and add data
         */

        VelocityContext context = new VelocityContext();
        for (Iterator it = contextMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            context.put((String) entry.getKey(), entry.getValue());
        }
        context.put("name", "World");

        /*
         *  now render the template into a StringWriter
         */

        StringWriter writer = new StringWriter();

        try {
            t.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         *  show the word
         */

        return writer.toString();

    }
}
