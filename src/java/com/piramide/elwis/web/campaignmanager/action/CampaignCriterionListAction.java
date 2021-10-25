package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignCriterionListCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionListForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCriterionListAction.java 10205 2012-05-02 20:01:09Z miguel $
 */

public class CampaignCriterionListAction extends CampaignListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("campaignCriteionListAction ... action ...");
        CampaignCriterionListForm listForm = (CampaignCriterionListForm) form;

        ActionForward forward = super.execute(action, listForm, request, response);

        if (!forward.getName().equals("Fail_")) {

            CampaignCriterionListCmd cmd = new CampaignCriterionListCmd();
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

            cmd.putParam("userId", user.getValue("userId"));

            if (request.getParameter("campaignId") != null && !"".equals(request.getParameter("campaignId"))) {
                ;
            }
            cmd.putParam("campaignId", request.getParameter("campaignId"));

            if (request.getParameter("dto(campaignId)") != null && !"".equals(request.getParameter("dto(campaignId)"))) {
                cmd.putParam("campaignId", request.getParameter("dto(campaignId)"));
            }

            BusinessDelegate.i.execute(cmd, request);

            listForm.setAddressType((Integer) cmd.getResultDTO().get("addressType"));
            listForm.setContactType((Integer) cmd.getResultDTO().get("contactType"));
            listForm.setTotalHits((Integer) cmd.getResultDTO().get("totalHits"));
            listForm.setHasEmail((Integer)cmd.getResultDTO().get("hasEmail"));
            listForm.setHasEmailTelecomType((Integer) cmd.getResultDTO().get("hasEmailTelecomType"));


            listForm.setIncludePartner((Boolean) cmd.getResultDTO().get("includePartner"));
            listForm.setIsDouble((Boolean) cmd.getResultDTO().get("isDouble"));
            listForm.setDeletePrevius(new Boolean(true));

            request.setAttribute("criteriaList", new ArrayList(0));
            request.setAttribute("sizeResultQuery", cmd.getResultDTO().get("sizeResultQuery"));
            request.setAttribute("campaignId", cmd.getResultDTO().get("campaignId"));
            request.setAttribute("sizeCurrentCampContact", cmd.getResultDTO().get("sizeCurrentCampContact"));
        }

        return forward;
    }
}