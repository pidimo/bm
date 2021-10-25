package com.piramide.elwis.web.dashboard.action;

import com.piramide.elwis.web.dashboard.component.web.struts.action.SaveConfigurationAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:46:55 PM
 */
public class SaveComponentConfigurationAction extends SaveConfigurationAction {


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse httpservletresponse) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");

        } else {
            return super.execute(mapping, form, request, httpservletresponse);
        }
    }

    public Map dataBaseReadParameters(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("dbComponentId", request.getParameter("dbComponentId"));
        return map;
    }
}