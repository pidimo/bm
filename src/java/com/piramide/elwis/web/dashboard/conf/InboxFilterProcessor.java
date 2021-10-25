package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.execute.FilterPreProcessor;

import java.util.Map;

/**
 * @author: ivan
 * Date: 30-11-2006: 03:45:07 PM
 */
public class InboxFilterProcessor extends FilterPreProcessor {
    public void processConfigurableFilter(Component cloneComponent, Map params) {
        ConfigurableFilter configurableFilter = cloneComponent.getConfigurableFilter("state");

        if (null != configurableFilter) {
            String value = configurableFilter.getInitialValue();

            ConfigurableFilter allConfFilter = new ConfigurableFilter("all", "", "");
            ConfigurableFilter unreadConfFilter = new ConfigurableFilter("unreadFilter", "", "");

            allConfFilter.setInitialValue("false");
            unreadConfFilter.setInitialValue("false");

            if (WebMailConstants.DASHBOARD_MAIL_FILTER_ALL.equals(value)) {
                allConfFilter.setInitialValue("true");
            } else if (WebMailConstants.DASHBOARD_MAIL_FILTER_UNREAD.equals(value)){
                unreadConfFilter.setInitialValue("true");
            }

            cloneComponent.addConfigurableFilter(allConfFilter);
            cloneComponent.addConfigurableFilter(unreadConfFilter);
        }
    }
}
