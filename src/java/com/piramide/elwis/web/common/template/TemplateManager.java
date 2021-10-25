package com.piramide.elwis.web.common.template;

import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author Alejandro Ruiz
 * @version 4.2.1
 */
public class TemplateManager {
    private final static Log log = LogFactory.getLog(TemplateManager.class);
    public static final String ATTRIB_RESOURCES = "i18n";
    private VelocityEngine engine;
    private static final String DEFAULT_LANG;

    static {
        DEFAULT_LANG = "en";
    }


    public TemplateManager() {
        try {
            engine = new VelocityEngine();
            Properties properties = new Properties();
            try {
                properties.load(getResourceAsStream("velocity.properties"));
            }
            catch (java.lang.Exception e) {
                properties.put("resource.loader", "class");
                properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                properties.put(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
            }
            engine.init(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VelocityContext createVelocityContext(Map contextParameters, TemplateResources resources) {
        VelocityContext context = new VelocityContext() {
            public Object put(String key, Object value) {
                if (key == null) {
                    return null;
                } else {
                    return internalPut(key, value);
                }
            }
        };
        if (contextParameters != null) {
            for (Iterator iterator = contextParameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                context.put((String) entry.getKey(), entry.getValue());
            }
        }
        context.put("messages", resources);
        context.put("configurationManager", ConfigurationFactory.getConfigurationManager());//setting the Application Config.
        return context;
    }

    public String mergeTemplate(String templateName, Map contextParameters, TemplateResources resources) throws VelocityException {
        return mergeTemplate(templateName, null, contextParameters, resources);
    }

    /**
     * Merge the template file with the parameters
     *
     * @param templateName
     * @param encode            The encode for the result content, by default is UTF-8
     * @param contextParameters The parameters for merge with the template
     * @return The result content from merge the template
     * @throws VelocityException If the template are null
     */
    public String mergeTemplate(String templateName, String encode, Map contextParameters, TemplateResources resources) throws VelocityException {
        if (templateName == null) {
            throw new VelocityException("No template defined");
        }
        try {
            StringWriter writer = new StringWriter();
            log.debug("Parameters:" + contextParameters);
            VelocityContext context = createVelocityContext(contextParameters, resources);
            if (encode == null) {
                engine.mergeTemplate(templateName, context, writer);
            } else {
                engine.mergeTemplate(templateName, encode, context, writer);
            }
            return writer.toString();
        } catch (Exception e) {
            throw new VelocityException("Fail to merge the template:\n" + e.getMessage());
        }
    }

    /**
     * @param templateContent   The content of template
     * @param contextParameters The parameters
     * @return Return a string with the merge of templateContent with the parameters
     * @throws VelocityException If the content are null
     */
    public String evaluateTemplate(String templateContent, Map contextParameters, TemplateResources resources) throws VelocityException {
        if (templateContent == null) {
            throw new VelocityException("No template content defined");
        }
        try {
            StringWriter writer = new StringWriter();
            VelocityContext context = createVelocityContext(contextParameters, resources);
            engine.evaluate(context, writer, "evaluateTemplate", templateContent);
            return writer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Load a given resource.
     * <p/>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getClassLoader() callingClass.getClassLoader() }
     * </ul>
     *
     * @param resourceName The name of the resource to load
     */
    private static URL getResource(String resourceName) {
        URL url = null;

        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = TemplateManager.class.getClassLoader().getResource(resourceName);
        }
        return url;
    }


    /**
     * This is a convenience method to load a resource as a stream.
     * <p/>
     * The algorithm used to find the resource is given in getResource()
     *
     * @param resourceName The name of the resource to load
     */
    private static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);
        try {
            return url != null ? url.openStream() : null;
        }
        catch (IOException e) {
            return null;
        }
    }

}
