package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version SalesProcessListAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class SalesProcessManagerAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        if (null != (forward = validateElementExistence((DefaultForm) form, request, mapping))) {
            return forward;
        }

        if (updateForm((DefaultForm) form, request)) {
            return mapping.getInputForward();
        }

        setCompanyId(form, request);

        forward = super.execute(mapping, form, request, response);
        return processForward(forward, (DefaultForm) form, mapping, request);
    }

    /**
     * Implement this method beacuse SalesProcessManagerAction extends AbstractDefaultAction
     *
     * @param form    ActionForm Object
     * @param request HttpServletRequest Object
     */
    private void setCompanyId(ActionForm form, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        if (form instanceof DefaultForm) {
            if (!(((DefaultForm) form).getDto("companyId") != null)) {
                ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
            }
        }
    }

    protected ActionForward validateElementExistence(DefaultForm defaultForm,
                                                     HttpServletRequest request,
                                                     ActionMapping mapping) {
        ActionErrors errors = Functions.existSalesProcess(request);
        if (null != errors) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    protected boolean updateForm(DefaultForm defaultForm, HttpServletRequest request) {
        return false;
    }

    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {
        return forward;
    }
}
