package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.common.action.AbstractDefaultAction;
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
 * @version SalesProcessListAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class SalesProcessAction extends AbstractDefaultAction {
    private Log log = LogFactory.getLog(SalesProcessAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Sales process action");
        DefaultForm salesProcessForm = (DefaultForm) form;
        String op = (String) salesProcessForm.getDto("op");

        ActionForward forward = super.execute(mapping, salesProcessForm, request, response);

        if (salesProcessForm.getDto("processId") != null && "create".equals(op)) {
            return new ActionForwardParameters().add("processId", salesProcessForm.getDto("processId").toString())
                    .add("addressId", salesProcessForm.getDto("addressId").toString()).forward(forward);
        }

        return forward;
    }
}
