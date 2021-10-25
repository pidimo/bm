package com.piramide.elwis.web.dashboard.conf;


import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.execute.FilterPreProcessor;

import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 25, 2006
 *         Time: 3:19:31 PM
 */
public class SupportCaseFilterProcesor extends FilterPreProcessor {
    public void processConfigurableFilter(Component cloneComponent, Map params) {
        ConfigurableFilter newFilter = new ConfigurableFilter("onlyOpenCase");
        newFilter.setInitialValue("true");
        cloneComponent.addConfigurableFilter(newFilter);
    }
}
