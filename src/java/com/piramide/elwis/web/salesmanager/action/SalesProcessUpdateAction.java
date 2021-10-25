package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalesProcessUpdateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = super.execute(mapping, form, request, response);

        /*if("Success".equals(forward.getName()) && null != request.getParameter("createSaleButton")){
            SalesProcessForm salesProcessForm = (SalesProcessForm) form;
            String processId = (String) salesProcessForm.getDto("processId");
            return new ActionForwardParameters().add("processId", processId)
                    .forward(mapping.findForward("CreateSale"));
        }*/

        return forward;
    }
}
