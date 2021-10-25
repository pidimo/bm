package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miguel Rojas
 * @version 6.5.11
 */
public class SalesProcessCreateAction extends ContactManagerAction {

    /**
     * Process the <code>ActionForward</code> object after of EJB command execution.
     * For credit notes, the method verifies if the credit note does have associated a invoice with invoice positions
     * in this case the <code>ActionForward</code> object will change.
     *
     * @param forward     <code>ActionForward</code> object.
     * @param defaultForm <code>DefaultForm</code> object than contain the information about of recently created invoice.
     * @param mapping     <code>ActionMapping</code> object to select the new <code>ActionForward</code>.
     * @param request     <code>HttpServletRequest</code> object for general purposes.
     * @return <code>ActionForward</code> object for the next page.
     */
    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {

        if ("Success".equals(forward.getName())) {
            ActionForwardParameters parameters = new ActionForwardParameters();
            parameters.add("processId", defaultForm.getDto("processId").toString());
            parameters.add("dto(processId)", defaultForm.getDto("processId").toString());
            parameters.add("dto(processName)", com.piramide.elwis.web.common.el.Functions.encode(defaultForm.getDto("processName").toString()));
            parameters.add("addressId", defaultForm.getDto("addressId").toString());

            return parameters.forward(forward);
        }

        return forward;
    }
}
