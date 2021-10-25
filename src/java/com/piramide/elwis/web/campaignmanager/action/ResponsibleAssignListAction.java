package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * contact list to manual assign
 *
 * @author Miky
 * @version $Id: ResponsibleAssignListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ResponsibleAssignListAction extends CampaignActivityListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ResponsibleAssignListAction........" + request.getParameterMap());

        //activity user recurrency validation...
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITYUSER, "activityid", "userid",
                request.getParameter("activityId"), request.getParameter("userId"), errors, new ActionError("Campaign.activity.user.notFound"));
        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        SearchForm listForm = (SearchForm) form;
        log.debug("FFOOOOO:" + listForm.getParameter("ofCustomer"));
        if (listForm.getParameter("ofCustomer") != null && !"".equals(listForm.getParameter("ofCustomer").toString())) {
            addFilter("employeeId", request.getParameter("employeeId"));
        } else {
            addFilter("employeeId", ""); //remove
        }

        if (listForm.getParameter("allContacts") != null && !"".equals(listForm.getParameter("allContacts").toString())) {
            addFilter("onlyNotAssigned", ""); //remove
        } else {
            addFilter("onlyNotAssigned", "true");
        }
        return super.execute(mapping, form, request, response);
    }

}
