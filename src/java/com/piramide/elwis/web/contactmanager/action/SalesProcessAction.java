package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version $Id: SalesProcessAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SalesProcessAction extends ContactManagerAction {

    private Log log = LogFactory.getLog(SalesProcessAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm processForm = (DefaultForm) form;

        if (request.getParameter("cancelEvaluation") != null) {
            return new ActionForwardParameters().add("dto(processId)", processForm.getDto("processId").toString())
                    .forward(mapping.findForward("Cancel"));
        }

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName()) && request.getParameter("evaluationButton") != null) {
            return new ActionForwardParameters().add("dto(processId)", processForm.getDto("processId").toString())
                    .forward(mapping.findForward("Evaluation"));
        }

        /*if ("Success".equals(forward.getName()) && null != request.getParameter("createSaleButton")) {
            SalesProcessForm salesProcessForm = (SalesProcessForm) form;
            String processId = (String) salesProcessForm.getDto("processId");
            return new ActionForwardParameters().add("processId", processId)
                    .forward(mapping.findForward("CreateSale"));
        }*/
        return forward;
    }
}