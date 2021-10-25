package com.piramide.elwis.web.dashboard.action;

import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.configuration.structure.StaticFilter;
import com.piramide.elwis.web.dashboard.component.web.struts.action.ReadConfigurationAction;
import com.piramide.elwis.web.dashboard.form.ComponentForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:43:16 PM
 */
public class ReadComponentConfigurationAction extends ReadConfigurationAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ActionForward execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)");

        ActionForward forward = super.execute(mapping, form, request, response);

        if (null != dbComponent) {
            ComponentForm f = (ComponentForm) form;
            List<ConfigurableFilter> xmlFilters = xmlComponent.getConfigurableFilters();
            List<StaticFilter> xmlStaticFilters = xmlComponent.getStaticFilters();

            for (ConfigurableFilter xmlFilter : xmlFilters) {
                ConfigurableFilter dbFilter = dbComponent.getConfigurableFilter(xmlFilter.getName());
                if (null != dbFilter) {
                    if (dbFilter.isRangeView()) {
                        f.setDto(dbFilter.getName() + "_0", dbFilter.getInitialValue());
                        f.setDto(dbFilter.getName() + "_1", dbFilter.getFinalValue());
                    } else {
                        f.setDto(dbFilter.getName(), dbFilter.getInitialValue());
                    }
                }
            }

            for (StaticFilter xmlStaticFilter : xmlStaticFilters) {
                StaticFilter dbStaticFilter = dbComponent.getStaticFilter(xmlStaticFilter.getName());
                if (dbStaticFilter != null) {
                    f.setDto(dbStaticFilter.getName(), dbStaticFilter.getValue());
                }
            }
        }
        return forward;
    }

    public Map dataBaseReadParameters(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("dbComponentId", request.getParameter("dbComponentId"));
        return map;
    }
}
