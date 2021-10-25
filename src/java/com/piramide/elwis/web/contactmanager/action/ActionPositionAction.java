package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jan 31, 2005
 * Time: 5:47:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPositionAction extends ActionManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward;
        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        }

        DefaultForm positionForm = (DefaultForm) form;

        if (!isSaveAndNewButtonPressed(request) && !isSaveButtonPressed(request)) {
            Functions.updateActionPositionDescription(positionForm, request);
            Functions.updateActionPositionPrice(positionForm, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, form, request, response);
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("dto(save)");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }
}
