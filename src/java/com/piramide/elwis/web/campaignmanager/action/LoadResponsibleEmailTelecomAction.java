package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.campaignmanager.el.Functions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to load emails of responsibles of send emails and set predetermined email
 *
 * @author Miky
 * @version $Id: LoadResponsibleEmailTelecomAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LoadResponsibleEmailTelecomAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing LoadResponsibleEmailTelecomAction................" + request.getParameterMap());

        List emailTelecomList = Functions.getSenderEmployeeEmails(request.getParameter("activityId"), request.getParameter("senderEmployeeId"), request.getParameter("telecomTypeId"), request);

        for (Iterator iterator = emailTelecomList.iterator(); iterator.hasNext();) {
            Map resultMap = (Map) iterator.next();
            if ("true".equals(resultMap.get("predetermined"))) {
                request.setAttribute("predeterminedValue", resultMap.get("value"));
                break;
            }
        }
        request.setAttribute("employeeEmailList", emailTelecomList);
        return super.execute(mapping, form, request, response);
    }

}
