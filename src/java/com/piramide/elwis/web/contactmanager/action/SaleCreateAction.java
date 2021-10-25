package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
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
public class SaleCreateAction extends ContactManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);
        if ("Success".equals(forward.getName())) {
            ActionForwardParameters parameters = new ActionForwardParameters();
            parameters.add("saleId", ((DefaultForm) form).getDto("saleId").toString()).
                    add("dto(op)", "read");
            return parameters.forward(forward);
        }

        return forward;
    }
}
