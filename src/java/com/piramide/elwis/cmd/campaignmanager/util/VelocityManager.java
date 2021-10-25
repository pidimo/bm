package com.piramide.elwis.cmd.campaignmanager.util;

import com.piramide.elwis.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * The objective of this class is to encapsulate the handling of velocity
 *
 * @author: ivan
 */
public class VelocityManager {
    private Log log = LogFactory.getLog(this.getClass());

    private VelocityEngine engine = new VelocityEngine();
    public static VelocityManager i = new VelocityManager();
    private StringResourceRepository stringRepository;
    private boolean isInitialized = false;

    private VelocityManager() {
    }

    public void initializeVelocityManager() {
        log.debug("Initialize VelocityManager...");
        try {
            init();
            isInitialized = true;
            stringRepository = StringResourceLoader.getRepository();
        } catch (Exception e) {
            log.warn("Cannot initialize VelocityEngine.");
        }
    }

    /**
     * This method initialize velocity engine properties to read
     * <code>String</code> resources and initialize Velocity engine.
     * <p/>
     * <code>
     * resource.loader = string
     * string.resource.loader.description = Velocity StringResource loader
     * string.resource.loader.class = org.apache.velocity.runtime.resource.loader.StringResourceLoader
     * </code>
     *
     * @throws Exception When cannot initialize <code>VelocityEngine</code> object.
     */
    private void init() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "string");
        properties.put("string.resource.loader.description", "Velocity StringResource loader");
        properties.put("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        properties.put(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");

        engine.init(properties);
    }

    /**
     * This method merge <code>Map</code> object that contain the values to replaced in
     * <code>StringBuilder</code> template.
     *
     * @param values       <code>Map</code> that contain values to merge with template.
     * @param template     <code>StringBuilder</code> object that contain template text
     * @param templateName Name with which the template in the velocity resources is registered
     * @return <code>StringBuilder</code> objects that contain result of merge template with values.
     * @throws Exception When <code>VelocityEngine</code> object cannot retrieve template
     *                   or when <code>Template</code> object cannot merge template.
     */
    public StringBuilder merge(Map values, StringBuilder template, String templateName) throws Exception {
        StringBuilder result;

        if (null == stringRepository.getStringResource(templateName)) {
            stringRepository.setEncoding(Constants.CHARSET_ENCODING);
            stringRepository.putStringResource(templateName, template.toString());
        }

        org.apache.velocity.Template velocityTemplate = engine.getTemplate(templateName);
        VelocityContext ctx = new VelocityContext();
        for (Object o : values.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            ctx.put((String) entry.getKey(), entry.getValue());
        }

        StringWriter writer = new StringWriter();

        velocityTemplate.merge(ctx, writer);

        result = new StringBuilder(writer.toString());

        return result;
    }

    public void removeTemplateFromRepository(String templateName) {
        if (null != stringRepository) {
            stringRepository.removeStringResource(templateName);
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}

