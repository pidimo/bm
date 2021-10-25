package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.action.ListAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: 14-nov-2006
 * Time: 18:18:37
 * To change this template use File | Settings | File Templates.
 */

public class CampaignAdvancedListAction extends ListAction {

    private Log log = LogFactory.getLog(CampaignAdvancedListAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("    ...  CampaignAdvancedListAction  execute    ...    ");
        super.initializeFilter();
        ActionForward forward = mapping.findForward("Success");
        SearchForm defaultForm = (SearchForm) form;
        ActionErrors aErrors = new ActionErrors();

        if ((request.getParameter("parameter(generate)") != null && !CampaignConstants.EMPTY.equals(request.getParameter("parameter(generate)")))) {
            aErrors = defaultForm.validate(mapping, request);
            saveErrors(request, aErrors);
        }

        if (aErrors.isEmpty()) {
            return super.execute(mapping, defaultForm, request, response);
        }
        return forward;
    }
}
