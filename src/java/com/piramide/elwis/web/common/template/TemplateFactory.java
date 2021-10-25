package com.piramide.elwis.web.common.template;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Sep 22, 2005
 * Time: 6:02:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateFactory {
    private static TemplateManager templateManager;

    public static TemplateManager getTemplateManager() {
        if (templateManager == null) {
            synchronized (com.piramide.elwis.web.common.template.TemplateFactory.class) {
                if (templateManager == null) {
                    templateManager = new TemplateManager();
                }
            }
        }
        return templateManager;
    }

    public static void setTemplateManager(TemplateManager templateManager) {
        TemplateFactory.templateManager = templateManager;
    }


}
