package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.salesmanager.action.SalesProcessListAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: Jan 28, 2005
 * Time: 2:51:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPositionUpdateAction extends SalesProcessListAction {


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String processId = request.getParameter("processId").toString();
        String contactId = request.getParameter("dto(contactId)").toString();
        addFilter("processId", processId);
        addFilter("contactId", contactId);

        return super.execute(mapping, form, request, response);
    }
}