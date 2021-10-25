package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.ListAction;
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
public class ProductContractBySalePositionListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        addFilter("salePositionId", request.getParameter("salePositionId"));
        setModuleId(request.getParameter("salePositionId"));
        return super.execute(mapping, form, request, response);
    }
}
