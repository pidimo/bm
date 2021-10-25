package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignContactTaskReadCmd;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Jatun S.R.L.
 * forward delete manual referenced validation to activity contact action
 *
 * @author Miky
 * @version $Id: ActivityContactDeleteForwardAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityContactDeleteForwardAction extends CampaignActivityManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityContactDeleteForwardAction............." + request.getParameterMap());

        ActionErrors errors = new ActionErrors();

        //manual referenced validation
        List referencedByTables = new ArrayList();
        Map map = new HashMap();
        //sales process contact validation
        if (Functions.campaignContactHasSalesProcess(request.getParameter("addressId"), request.getParameter("activityId"), request)) {
            errors.add("referenced", new ActionError("customMsg.Referenced", request.getParameter("dto(contactName)")));
            referencedByTables.add(SalesConstants.TABLE_SALESPROCESS);
        }

        //task create for contact and responsible validation
        CampaignContactTaskReadCmd cmd = new CampaignContactTaskReadCmd();
        cmd.putParam("activityId", request.getParameter("activityId"));
        cmd.putParam("campaignId", request.getParameter("campaignId"));
        cmd.putParam("campaignContactId", request.getParameter("dto(campaignContactId)"));

        ResultDTO resultDto = new ResultDTO();
        try {
            resultDto = BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
        }
        if (resultDto.containsKey("withTask")) {
            if (errors.isEmpty()) {
                errors.add("referenced", new ActionError("customMsg.Referenced", request.getParameter("dto(contactName)")));
            }
            referencedByTables.add(SchedulerConstants.TABLE_TASK);
        }

        if (!errors.isEmpty()) {
            map.put("referencedByTables", referencedByTables);
            request.setAttribute("dto", map); //to show referenced tables

            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        return super.execute(mapping, form, request, response);
    }

}
