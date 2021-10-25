package com.piramide.elwis.web.dashboard.component.ui.velocity;

import com.piramide.elwis.web.dashboard.component.util.ResourceReader;

/**
 * @author : ivan
 */
public class CustomTemplateFactory extends TemplateFactory {
    private String className;

    public CustomTemplateFactory(String className) {
        this.className = className;
    }

    protected Template buildTemplate() {
        CustomTemplate customTemplate = (CustomTemplate) ResourceReader.getClassInstance(className);
        return customTemplate;
    }
}
