package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.domain.admin.SystemModule;
import com.piramide.elwis.domain.admin.SystemModuleHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.web.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jan 14, 2005
 * Time: 11:22:06 AM
 * To change this template use File | Settings | File Templates.
 */

public class CompanyAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        forward = null;
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        if (!"create".equals(request.getParameter("op"))) {
            forward = super.execute(mapping, form, request, response);
            if (((DefaultForm) form).getDto("creation_") != null) {
                Date date = DateUtils.integerToDate(new Integer(((DefaultForm) form).getDto("creation_").toString()));
                ((DefaultForm) form).setDto("creation_Date", DateUtils.parseDate(date, JSPHelper.getMessage(request, "datePattern")));
            }
        } else {
            forward = new ActionForward(mapping.getParameter());
            ((DefaultForm) form).setDto("active", new Boolean(true));

            //initialize default attach size
            if (((DefaultForm) form).getDto("maxMaxAttachSize") == null) {
                ((DefaultForm) form).setDto("maxMaxAttachSize", new Integer(ConfigurationFactory.getValue("elwis.default.maxMaxAttachSize")));
            }
        }
        SystemModuleHome moduleHome = (SystemModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_SYSTEMMODULE);
        Collection systemModules = moduleHome.findAll();
        List modules = new ArrayList(systemModules.size());
        for (Iterator iterator = systemModules.iterator(); iterator.hasNext();) {
            SystemModule systemModule = (SystemModule) iterator.next();
            Map module = new HashMap(2);
            module.put("nameKey", systemModule.getNameKey());
            module.put("moduleId", systemModule.getModuleId());
            modules.add(module);
        }
        request.setAttribute("moduleList", modules);

        return forward;
    }
}
