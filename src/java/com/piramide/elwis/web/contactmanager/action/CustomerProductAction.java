package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Titus
 * @version CustomerProductAction.java, v 2.0 Aug 26, 2004 6:38:46 PM
 */
/*ContactListAction*/
public class CustomerProductAction extends ContactManagerAction {

    private Log log = LogFactory.getLog(this.getClass());


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        DefaultForm defaultForm = (DefaultForm) form;

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        if (request.getParameter("dto(save)") != null || request.getParameter("dto(contract)") != null) {
            String name = defaultForm.getDto("name").toString();
            log.debug("Save productCustomer");

            ActionErrors errors = form.validate(mapping, request);
            if (!errors.isEmpty()) {
                log.debug(" has errors!!!!   :D");
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }

            ActionForward forward = super.execute(mapping, form, request, response);

            if (request.getParameter("dto(contract)") != null) {
                forward = new ActionForwardParameters()
                        .add("dto(productCustomerId)", defaultForm.getDto("productCustomerId").toString())
                        .add("dto(name)", name)
                        .forward(mapping.findForward("Update"));
            }
            return forward;
        }

        return new ActionForward(mapping.getInput());
    }
}
