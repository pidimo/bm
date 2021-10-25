package com.piramide.elwis.web.productmanager.action.report;

import com.piramide.elwis.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: Oct 18, 2005
 * Time: 3:24:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContractReportForwardAction extends org.apache.struts.actions.ForwardAction {

    private Log log = LogFactory.getLog(ProductReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--- ContractReportForwardAction       execute  ....");

        request.setAttribute("all", Constants.TRUE_VALUE);
        return super.execute(mapping, form, request, response);
    }
}
