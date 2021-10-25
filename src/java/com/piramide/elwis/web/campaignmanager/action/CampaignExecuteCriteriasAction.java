package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignAditionalCriteriaCmd;
import com.piramide.elwis.cmd.campaignmanager.CampaignReadCmd;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionBusinessDelegate;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionListForm;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.BusinessDelegate;
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
 * Action Class for interact with CampaignCriterionListForm.
 * send aditional criteria options before of to execute all criterias.
 *
 * @autor yumi
 * Date: Nov 4, 2004
 */

public class CampaignExecuteCriteriasAction extends DefaultAction {

    protected static Log log = LogFactory.getLog(CampaignExecuteCriteriasAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        FantabulousManager manager;
        ListStructure listStructure = null;
        CampaignCriterionListForm criterionForm = (CampaignCriterionListForm) form;

        if (!CampaignConstants.EMPTY.equals(request.getParameter("redirect")) && request.getParameter("redirect") != null) {
            return mapping.findForward("Cancel");
        }

        manager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), "/campaign");

// for aditional criteria option-> with/without contact person
        if (criterionForm.getContactType().equals(new Integer(1))) {
            log.debug("option ONLY WITHOUT CONTACTPERSON     . ** queryAddress ** ..  1 ");
            listStructure = manager.getList("queryContactPerson");
        } else if (criterionForm.getContactType().equals(new Integer(2))) {
            log.debug("option ALSO  WITHOUT CONTACTPERSON     . ** greatQuery ** ..   2");
            listStructure = manager.getList("greatQuery");
        } else {
            log.debug("option ONLY WITH CONTACTPERSON   . ** queryContactPerson ** .. 0");
            listStructure = manager.getList("queryAddress");
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        CampaignAditionalCriteriaCmd cmd = new CampaignAditionalCriteriaCmd();
        CampaignReadCmd readCmd = new CampaignReadCmd();

        cmd.putParam("greatQuery", addAccessRightSecurity(listStructure, userId));
        cmd.putParam("productCategoriesGreatQuery", manager.getList("productCategoriesGreatQuery"));
        cmd.putParam("customerCategoriesGreatQuery", manager.getList("customerCategoriesGreatQuery"));
        cmd.putParam("addressCategoriesGreatQuery", manager.getList("addressCategoriesGreatQuery"));
        cmd.putParam("contactPersonCategoriesGreatQuery", manager.getList("contactPersonCategoriesGreatQuery"));
        cmd.putParam("salePositionCategoriesGreatQuery", manager.getList("salePositionCategoriesGreatQuery"));
        cmd.putParam("campaignId", request.getParameter("dto(campaignId)"));
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam("userId", user.getValue("userId"));
        cmd.putParam("contactType", criterionForm.getContactType());
        cmd.putParam("addressType", criterionForm.getAddressType());
        cmd.putParam("hasEmail", criterionForm.getHasEmail());
        cmd.putParam("hasEmailTelecomType", criterionForm.getHasEmailTelecomType());

        boolean isUpdateHitsOnly = false;
        if (request.getParameter("updateHitsButton") != null) {//update hits button pressed
            isUpdateHitsOnly = true;
            cmd.putParam("isUpdateHitsOnly", isUpdateHitsOnly);
        }


        /*  deleteAll or no before of to generate all criterias*/
        if (criterionForm.getDeletePrevius() != null) {
            cmd.putParam("deleteAll", criterionForm.getDeletePrevius());
        } else {
            cmd.putParam("deleteAll", new Boolean(false));
        }

        if (criterionForm.getIsDouble() != null) {
            cmd.putParam("isDouble", criterionForm.getIsDouble());
        } else {
            cmd.putParam("isDouble", new Boolean(false));
        }

        if (criterionForm.getIncludePartner() != null) {
            cmd.putParam("includePartner", criterionForm.getIncludePartner());
        } else {
            cmd.putParam("includePartner", new Boolean(false));
        }

        log.debug(" ... Read campaign ...");
        readCmd.putParam("campaignId", request.getParameter("dto(campaignId)"));
        BusinessDelegate.i.execute(readCmd, request);
        Integer campContactSize = new Integer(0);
        if (!readCmd.getResultDTO().isFailure()) {
            campContactSize = (Integer) readCmd.getResultDTO().get("campContactSize");
        }


        if (isUpdateHitsOnly) { //just get hits and return the the list of criteria
            BeanTransactionBusinessDelegate.i.execute(cmd);
            request.setAttribute("sizeResultQuery", cmd.getResultDTO().get("sizeResultQuery"));
            return mapping.findForward("Cancel");

        }

/* if contactSize is greater to 0 then send confirmation message before of to delete, else show message */
        if (!(Boolean) cmd.getParamDTO().get("deleteAll") || new Integer(0).equals(campContactSize)) {
            BeanTransactionBusinessDelegate.i.execute(cmd);
        } else if (!CampaignConstants.TRUEVALUE.equals(request.getParameter("question"))) {
            return mapping.findForward("Success");
        }
        if (CampaignConstants.TRUEVALUE.equals(request.getParameter("question"))) {
            BeanTransactionBusinessDelegate.i.execute(cmd);
        }

        return mapping.findForward("campContact");
    }

    private ListStructure addAccessRightSecurity(ListStructure sourceListStructure, Integer userId) {
        return AccessRightDataLevelSecurity.i.processAddressAccessRightByList(sourceListStructure, userId);
    }

}
