package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SaleBySalesProcessAction extends SalesProcessManagerAction {

    @Override
    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionForward forward = super.validateElementExistence(defaultForm, request, mapping);

        //salesprocess was deleted
        if (null != forward) {
            return forward;
        }

        ActionErrors errors = Functions.existsSale(request);
        if (null != errors) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        return null;
    }
}


