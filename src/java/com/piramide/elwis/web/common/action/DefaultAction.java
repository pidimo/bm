package com.piramide.elwis.web.common.action;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Catch the cancel button execute
 *
 * @author Fernando Montaño  14:16:24
 * @version 2.0
 */
public class DefaultAction extends AbstractDefaultAction {

    /**
     * Check if cancel button has been pressed, if true return cancel forward, else
     * execute generic Action
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Elwis DefaultAction executing...");
        User user = RequestUtils.getUser(request);
        if (form instanceof DefaultForm) {
            if (!(((DefaultForm) form).getDto("companyId") != null)) {
                ((DefaultForm) form).setDto("companyId", user.getValue("companyId"));
            }
        }
        return super.execute(mapping, form, request, response);
    }
}
