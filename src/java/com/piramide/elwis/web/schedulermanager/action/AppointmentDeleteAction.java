package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.schedulermanager.AppointmentDeleteCmd;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import com.piramide.elwis.web.schedulermanager.form.AppointmentForm;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L
 *
 * @author yumi
 */

public class AppointmentDeleteAction extends AppointmentGeneralAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            AppointmentForm appointmentForm = (AppointmentForm) form;
            String calendar = request.getParameter("calendar");
            String oldType = (String) appointmentForm.getDto("returnType");

            ActionForward returnSuccess = mapping.findForward("Cancel");
            request.setAttribute("simple", "true");
            if (oldType != null) {
                if (oldType.equals("1")) {
                    returnSuccess = mapping.findForward("ReturnDay");
                } else if (oldType.equals("2")) {
                    returnSuccess = mapping.findForward("ReturnWeek");
                } else if (oldType.equals("3")) {
                    returnSuccess = mapping.findForward("ReturnMonth");
                } else if (oldType.equals("4")) {
                    returnSuccess = mapping.findForward("ReturnYear");
                } else if (oldType.equals("5")) {
                    returnSuccess = mapping.findForward("ReturnSearchList");
                } else if (oldType.equals("6")) {
                    returnSuccess = mapping.findForward("ReturnAdvancedSearchList");
                }

                if (!oldType.equals("5") && !oldType.equals("6")) {
                    return new ActionForwardParameters().add("calendar", calendar)
                            .add("oldType", oldType)
                            .forward(returnSuccess);
                } else {
                    return mapping.findForward("Cancel");
                }
            } else {
                return mapping.findForward("Cancel");
            }
        }

        return super.execute(mapping, form, request, response);
    }

    @Override
    protected boolean accessRightsValidatorCondition(Byte publicAppPermission,
                                                     Byte privateAppPermission,
                                                     DefaultForm defaultForm,
                                                     HttpServletRequest request) {
        boolean isPrivate = null != defaultForm.getDto("isPrivate");
        return Functions.hasDelAppointmentPermission(publicAppPermission, privateAppPermission, !isPrivate);
    }

    @Override
    protected ActionError getAccessRightsValidatorErrorMessage(DefaultForm defaultForm, HttpServletRequest request) {
        return new ActionError("scheduler.permission.error",
                JSPHelper.getMessage(request, "Scheduler.grantAccess.delete"));
    }

    @Override
    protected ActionForward commandExecution(ActionMapping mapping,
                                             DefaultForm defaultForm,
                                             HttpServletRequest request) throws Exception {
        ActionForward returnSuccess = mapping.findForward("Success");

        String returnType = (String) defaultForm.getDto("returnType");
        String calendar = request.getParameter("calendar");
        String oldType = (String) defaultForm.getDto("returnType");

        if (request.getParameter("dto(save)") != null || request.getParameter("dto(onlySave)") != null) {
            if (returnType.equals("1")) {
                returnSuccess = mapping.findForward("ReturnDay");
            } else if (returnType.equals("2")) {
                returnSuccess = mapping.findForward("ReturnWeek");
            } else if (returnType.equals("3")) {
                returnSuccess = mapping.findForward("ReturnMonth");
            } else if (returnType.equals("4")) {
                returnSuccess = mapping.findForward("ReturnYear");
            } else if (returnType.equals("5")) {
                returnSuccess = mapping.findForward("ReturnSearchList");
            } else if (returnType.equals("6")) {
                returnSuccess = mapping.findForward("ReturnAdvancedSearchList");
            }

            AppointmentDeleteCmd deleteCmd = new AppointmentDeleteCmd();
            deleteCmd.putParam(defaultForm.getDtoMap());
            BusinessDelegate.i.execute(deleteCmd, request);
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, deleteCmd.getResultDTO());
            saveErrors(request, errors);
        }

        return new ActionForwardParameters().add("calendar", calendar)
                .add("oldType", oldType)
                .forward(returnSuccess);
    }
}