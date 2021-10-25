package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: alejandro
 * Date: Jan 12, 2005
 * Time: 7:11:17 PM
 */

public class CampaignCreateAction extends DefaultAction {
    public ActionForward execute(ActionMapping map, ActionForm actionForm, HttpServletRequest req, HttpServletResponse res) throws Exception {

        if (isCancelled(req)) {
            log.debug("Is Cancel");
            return (map.findForward("Cancel"));
        }


        ActionForward forward = super.execute(map, actionForm, req, res);
        if (((DefaultForm) actionForm).getDto("campaignId") != null) {
            return new ActionForwardParameters().add("campaignId", ((DefaultForm) actionForm).getDto("campaignId").toString()).forward(forward);
        }

        return (map.findForward(forward.getName()));
    }

}
