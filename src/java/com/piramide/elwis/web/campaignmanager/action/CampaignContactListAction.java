package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignCriterionListCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionListForm;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 14-dic-2006
 * Time: 10:50:23
 * To change this template use File | Settings | File Templates.
 */

public class CampaignContactListAction extends CampaignListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping action, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("campaignContactListAction ... execute ...");
        CampaignCriterionListForm listForm = (CampaignCriterionListForm) form;

        ActionForward forward = super.execute(action, listForm, request, response);

        if (!forward.getName().equals("Fail_")) {

            CampaignCriterionListCmd cmd = new CampaignCriterionListCmd();
            FantabulousManager manager;
            manager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), "/campaign");
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            ListStructure listStructure = manager.getList("campaignCriterionList");


            request.setAttribute("criteriaList", listStructure);
        }
        return forward;
    }
}