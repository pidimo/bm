package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         Date: Sep 4, 2006
 *         Time: 2:26:23 PM
 */
public class RecentForwardAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


        String addressId = request.getParameter("dto(addressId)");
        String addressType = request.getParameter("dto(addressType)");
        String contactId = request.getParameter("contactId");
        String module = request.getParameter("module");
        String index = request.getParameter("index");

        if (ContactConstants.ADDRESSTYPE_PERSON.equals(addressType)) {
            return new ActionForwardParameters().add("dto(addressId)", addressId).
                    add("dto(addressType)", addressType).
                    add("contactId", contactId).
                    add("module", module).
                    add("index", index).forward(mapping.findForward("Person"));
        }
        if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(addressType)) {
            return new ActionForwardParameters().add("dto(addressId)", addressId).
                    add("dto(addressType)", addressType).
                    add("contactId", contactId).
                    add("module", module).
                    add("index", index).forward(mapping.findForward("Organization"));
        }
        return null;
    }
}
