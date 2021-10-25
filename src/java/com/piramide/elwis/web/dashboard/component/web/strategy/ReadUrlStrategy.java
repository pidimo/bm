package com.piramide.elwis.web.dashboard.component.web.strategy;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.core.AbstractStrategy;
import com.piramide.elwis.web.dashboard.component.core.ComponentManager;
import com.piramide.elwis.web.dashboard.component.core.UIWrapper;
import com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction;
import com.piramide.elwis.web.dashboard.component.web.util.TemplateResourceBundle;
import com.piramide.elwis.web.dashboard.component.web.util.TemplateResourceBundleFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author : ivan
 */
public class ReadUrlStrategy extends AbstractStrategy {

    String s = "";

    public ReadUrlStrategy(DrawComponentAction action, int componentId, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Component xmlComponent = Builder.i.findComponentById(componentId);

        Map dataBaseParameters = action.dataBaseReadParameters(request);


        TemplateResourceBundle resources = TemplateResourceBundleFactory.i.
                getTemplateResourceBundle(action.getApplicationResourcesPath(), locale);

        UIWrapper wrapper = new UIWrapper();
        wrapper.setHttpServletRequest(request);
        wrapper.setHttpServletResponse(response);
        wrapper.setTemplateResourcesBundle(resources);

        StringBuilder ui = ComponentManager.i.buildUIComponentHeader(wrapper, new HashMap(), dataBaseParameters, componentId);

        s = ui.toString();
    }

    public String buildComponent() {
        return s;
    }
}
