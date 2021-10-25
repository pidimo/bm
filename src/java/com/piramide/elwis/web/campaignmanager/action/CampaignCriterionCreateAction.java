package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionForm;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Mauren
 * Date: Nov 16, 2004
 * Time: 2:54:44 PM
 */

public class CampaignCriterionCreateAction extends CampaignManagerAction {
    protected static Log log = LogFactory.getLog(CampaignCriterionCreateAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        CampaignCriterionForm defaultForm = (CampaignCriterionForm) form;

        if (isCancelled(request)) {
            log.debug("Is Cancel");
            request.setAttribute("campaignId", request.getParameter("dto(campaignId)"));
            return (mapping.findForward("Cancel"));
        }

        log.debug(" ... campaignCriterionCreateAction .... execute ...");
        ActionForward forward = mapping.findForward("Success");
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        errors = defaultForm.validate(mapping, request);
        saveErrors(request, errors);

        if (!errors.isEmpty() && "create".equals(request.getParameter("dto(op)"))) {

            String ope = "EMPTY";
            if (!CampaignConstants.EMPTY.equals(defaultForm.getDto("operator")) && defaultForm.getDto("operator") != null) {
                ope = defaultForm.getDto("operator").toString();
            }
            if (defaultForm.getDto("fieldType") == null) {
                request.setAttribute("jsLoad", "onLoad=\"emptyNode();\"");
            } else {
                if (CampaignConstants.TRUEVALUE.equals(defaultForm.getDto("categoryNULL")) && defaultForm.getDto("categoryId") != null) {
                    request.setAttribute("jsLoad", "onLoad=\"emptyNode();\"");
                    return mapping.findForward("redirect");
                } else if (defaultForm.getDto("categoryId") == null) {
                    defaultForm.setDto("categoryId", "-100");
                }
                request.setAttribute("jsLoad", "onLoad=\"show_typeError(" + defaultForm.getDto("categoryId") + "," + defaultForm.getDto("campCriterionValueId") + ",'" + ope + "');\"");
            }
            forward = mapping.findForward("redirect");
        } else if (!errors.isEmpty() && "update".equals(request.getParameter("dto(op)"))) {

            if (!CampaignConstants.EMPTY.equals(defaultForm.getDto("operator"))) {
                request.setAttribute("jsLoad", "onLoad=\"showIU_reload(" + defaultForm.getDto("categoryId") + "," + defaultForm.getDto("campCriterionValueId") + ",'" + defaultForm.getDto("operator") + "');\"");
            }
            forward = mapping.findForward("redirect");
        }
        if (errors.isEmpty()) {
            FantabulousManager manager;
            manager = FantabulousManager.loadFantabulousManager(getServlet().getServletContext(), "/campaign");
            ListStructure listStructure = manager.getList("greatQuery");
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Integer userId = new Integer(user.getValue(Constants.USERID).toString());

            defaultForm.setDto(Constants.USERID, user.getValue(Constants.USERID));
            defaultForm.setDto("greatQuery", addAccessRightSecurity(listStructure, userId));
            defaultForm.setDto("productCategoriesGreatQuery", manager.getList("productCategoriesGreatQuery"));
            defaultForm.setDto("customerCategoriesGreatQuery", manager.getList("customerCategoriesGreatQuery"));
            defaultForm.setDto("addressCategoriesGreatQuery", manager.getList("addressCategoriesGreatQuery"));
            defaultForm.setDto("contactPersonCategoriesGreatQuery", manager.getList("contactPersonCategoriesGreatQuery"));
            defaultForm.setDto("salePositionCategoriesGreatQuery", manager.getList("salePositionCategoriesGreatQuery"));

            forward = super.execute(mapping, defaultForm, request, response);
        }
        if ("update".equals(request.getParameter("dto(op)")) && CampaignConstants.TRUEVALUE.equals(defaultForm.getDto("versionError"))) {
            String catId = null, critValue = null;
            if (defaultForm.getDto("categoryId") == null) {
                catId = "-100";
            } else {
                catId = defaultForm.getDto("categoryId").toString();
            }
            if (defaultForm.getDto("campCriterionValueId") == null) {
                critValue = "-100";
            } else {
                critValue = defaultForm.getDto("campCriterionValueId").toString();
            }
            if ((new Integer(6).equals(defaultForm.getDto("fieldType")) || new Integer(5).equals(defaultForm.getDto("fieldType"))
                    || (!CampaignConstants.CEROVALUE.equals(defaultForm.getDto("fieldname")) && "-100".equals(defaultForm.getDto("categoryId"))))) {

                request.setAttribute("jsLoad", "onLoad=\"showIU_updateValues(" + catId + "," + critValue + ","
                        + defaultForm.getDto("campaignCriterionId") + ",'read','" + defaultForm.getDto("operator") + "')\"");
            }
            forward = mapping.findForward("redirect");
        }
        return forward;
    }

    private ListStructure addAccessRightSecurity(ListStructure sourceListStructure, Integer userId) {
        return AccessRightDataLevelSecurity.i.processAddressAccessRightByList(sourceListStructure, userId);
    }

}