package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to initialize appoinments overview of ohter users
 *
 * @author Miky
 * @version $Id: OverviewCalendarAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class OverviewCalendarAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing OverviewCalendarAction... " + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;

        User user = RequestUtils.getUser(request);
        Map userSchedulerMap = new HashMap();
        userSchedulerMap.put("overviewUserList", defaultForm.getDto("overviewUsers") != null ? defaultForm.getDto("overviewUsers") : new ArrayList());
        if (!GenericValidator.isBlankOrNull((String) defaultForm.getDto("appointmentTypeId"))) {
            userSchedulerMap.put("appointmentTypeId", defaultForm.getDto("appointmentTypeId"));
        }

        user.setValue("overviewSchedulerMap", userSchedulerMap);

        return new ActionForwardParameters().add("type", (String) defaultForm.getDto("outputViewType"))
                .forward(mapping.findForward("Success"));
    }

}
