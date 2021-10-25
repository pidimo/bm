package com.piramide.elwis.web.dashboard.component.ui;

import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.core.UIWrapper;
import com.piramide.elwis.web.dashboard.component.ui.velocity.HeaderTemplateFactory;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;
import com.piramide.elwis.web.dashboard.component.ui.velocity.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class UiHeaderProcessor extends UiProcessor {

    public UiHeaderProcessor(UIWrapper wrapper, Map params, Map dataBaseParameters, int componentId) {
        super(dataBaseParameters, params, wrapper, componentId);
    }

    public StringBuilder buildUIComponent() {
        Component xmlComponent = Builder.i.findComponentById(super.getComponentId());

        List<Map> elements = new ArrayList<Map>();
        StringBuilder str;
        HeaderTemplateFactory factory = new HeaderTemplateFactory();
        Template headerTemplate = factory.getTemplate();


        ResourceBundleManager resources = super.getWrapper().getTemplateResourceBundle();
        List<Map> windowActions = buildUrlWindowActions(xmlComponent.getActions());


        Map options = new HashMap();
        options.put("messages", resources);
        options.put("xmlComponentId", super.getComponentId());
        options.put("componentName", xmlComponent.getResourceKey());
        options.put("windowActions", windowActions);
        options.put("isDashboardBootstrapUI", Functions.isBootstrapUIMode(getWrapper().getHttpServletRequest()));
        options.putAll(super.getParams());
        str = headerTemplate.merge(elements, options);

        return str;
    }

    /*private List<Map> buildUrlWindowActions
            (List<Action> actions) {

        String context = super.getWrapper().getHttpServletRequest().getContextPath();

        List<Map> l = new ArrayList<Map>();
        for (Action ac : actions) {
            String action = context + ac.getAction() + "?" +
                    super.getParams().get("DB_COMPONENTID_NAME") +
                    "=" +
                    super.getParams().get("DB_COMPONENTID_VALUE") +
                    "&" +
                    "componentId=" +
                    super.getComponentId() +
                    "&" +
                    super.getParams().get("CONTAINER_ID_NAME") +
                    "=" +
                    super.getParams().get("CONTAINER_ID_VALUE");

            String iconUrl = ac.getIconUrl();

            String resourceKey = ac.getResourcekey();

            Map m = new HashMap();
            m.put("action", super.getWrapper().getHttpServletResponse().encodeURL(action));
            m.put("iconUrl", iconUrl);
            m.put("resourceKey", resourceKey);

            l.add(m);
        }
        return l;

    }*/
}
