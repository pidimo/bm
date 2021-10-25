package com.piramide.elwis.cmd.campaignmanager.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Map;

/**
 * This class encapsulate all logic to replace variables in Template
 *
 * @author: ivan
 */
public class HtmlTemplateProcessor {
    private Log log = LogFactory.getLog(this.getClass());
    private StringBuilder templateText;
    private String templateName;

    public HtmlTemplateProcessor(StringBuilder templateText) {
        this.templateText = changeElwisVariablesToVelocityVariables(templateText);
        this.templateName = "some/elwis/velocity/template_" + (new Date()).getTime() + ".vm";
        /*if (!VelocityManager.i.isInitialized())
            VelocityManager.i.initializeVelocityManager();*/
    }

    /**
     * This method constructs a <code>StringBuilder<code> object that contains code HTML,
     * which is been from the substitution of variables in determined template
     *
     * @param values <code>Map</code> Object that contain keys and values to be replaced in template.
     * @return <code>StringBuilder</code> Object that contain HTML code.
     */
    public StringBuilder buildHtmlDocument(Map values) {
        StringBuilder result = new StringBuilder();

        if (null != templateText) {
            try {
                result = VelocityManager.i.merge(values, templateText, templateName);
            } catch (Exception e) {
                log.error("Cannot merge template.", e);
            }
        }
        return result;
    }

    /**
     * This method change elwis varibles by velocity variables.
     *
     * @param elwisTemplate elwis html template.
     * @return Velocity template.
     */
    private StringBuilder changeElwisVariablesToVelocityVariables(StringBuilder elwisTemplate) {
        return HtmlTemplateToVelocity.buildVelocityTemplate(elwisTemplate);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        log.debug("removing " + templateName + " from velocity resources.");
        VelocityManager.i.removeTemplateFromRepository(templateName);
    }

}
