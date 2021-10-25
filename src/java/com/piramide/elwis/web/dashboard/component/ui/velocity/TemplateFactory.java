package com.piramide.elwis.web.dashboard.component.ui.velocity;

/**
 * @author : ivan
 */
public abstract class TemplateFactory {
    public Template getTemplate() {
        Template template;
        template = buildTemplate();
        return template;
    }

    protected abstract Template buildTemplate();
}
