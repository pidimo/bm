package com.piramide.elwis.web.dashboard.component.ui;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Action;
import com.piramide.elwis.web.dashboard.component.core.UIWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author : ivan
 */
public abstract class UiProcessor {
    private Log log = LogFactory.getLog(this.getClass());
    private Map dataBaseParameters;
    private Map params;
    private UIWrapper wrapper;
    private int componentId;

    public Map getParams() {
        return params;
    }

    public UIWrapper getWrapper() {
        return wrapper;
    }

    public int getComponentId() {
        return componentId;
    }

    public UiProcessor(Map dataBaseParameters, Map params, UIWrapper wrapper, int componentId) {
        this.dataBaseParameters = dataBaseParameters;
        this.params = params;
        this.wrapper = wrapper;
        this.componentId = componentId;
    }

    // todo tambien falta ver la persistencia del componente (como se guardara en la bd) configuracion
    protected List<Map> buildUrlWindowActions
            (List<Action> actions) {
        log.debug("build URL for windowActions " + dataBaseParameters);
        String context = wrapper.getHttpServletRequest().getContextPath();

        String dataBaseParams = "";

        for (Iterator it = dataBaseParameters.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            dataBaseParams += key + "=" + value;
            if (it.hasNext()) {
                dataBaseParams = dataBaseParams + "&";
            }
        }

        List<Map> l = new ArrayList<Map>();
        for (Action ac : actions) {
            String action = context + ac.getAction() + "?" + dataBaseParams;

            String iconUrl = context + ac.getIconUrl();

            String resourceKey = ac.getResourcekey();

            Map m = new HashMap();
            m.put("action", wrapper.getHttpServletResponse().encodeURL(action));
            m.put("iconUrl", iconUrl);
            m.put("resourceKey", resourceKey);

            l.add(m);
        }
        return l;
    }

    public abstract StringBuilder buildUIComponent();
}
