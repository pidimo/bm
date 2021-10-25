package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.salesmanager.ActionCalculateValueCmd;
import com.piramide.elwis.cmd.salesmanager.ActionCreateCmd;
import com.piramide.elwis.cmd.salesmanager.ActionUpdateCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.salesmanager.util.ActionFormUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fernando Montaño
 * @version ActionManagerAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class ActionManagerAction extends CommunicationContactAction {
    private Log log = LogFactory.getLog(ActionManagerAction.class);
    private String op;

    @SuppressWarnings(value = "unchecked")
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm actionForm = (DefaultForm) form;
        User user = RequestUtils.getUser(request);
        actionForm.setDto(Constants.USER_ADDRESSID, user.getValue(Constants.USER_ADDRESSID).toString());

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_SALESPROCESS, "processid",
                request.getParameter("dto(processId)"), errors, new ActionError("msg.NotFound",
                        request.getParameter("dto(processName)")));

        if (errors.isEmpty() && request.getParameter("dto(processId)") != null && request.getParameter("dto(contactId)") != null) {
            errors = ForeignkeyValidator.i.validate(SalesConstants.TABLE_ACTION, "processid", "contactid",
                    request.getParameter("dto(processId)"), request.getParameter("dto(contactId)"), errors, new ActionError("msg.NotFound",
                            request.getParameter("dto(note)")));
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        if (request.getParameter("dto(op)") == null
                && request.getParameter("cmd") == null) {
            actionForm.getDtoMap().putAll(ActionFormUtil.i.getDefaultValuesForCreate(request));
            actionForm.getDtoMap().putAll(ActionFormUtil.i.getDefaultValuesFromSessionUser(request));

            return mapping.findForward("Success");
        }

        op = (String) actionForm.getDto("op");

        setDefaultForwardOption((DefaultForm) form);

        ActionForward forward = super.execute(mapping, form, request, response);

        if (request.getParameter("calculate") != null && !"Fail".equals(forward.getName())) {
            ActionCalculateValueCmd calculateCmd = new ActionCalculateValueCmd();
            calculateCmd.putParam(actionForm.getDtoMap());
            try {
                BusinessDelegate.i.execute(calculateCmd, request);
                actionForm.setDto("value", calculateCmd.getResultDTO().get("actionValue"));
                request.setAttribute("actionForm", actionForm);
                log.debug("-> Calculate value OK");
                return mapping.getInputForward();
            } catch (AppLevelException e) {
                log.debug("-> Execute " + ActionCalculateValueCmd.class.getName() + " FAIL", e);
            }
        }

        if ("Success".equals(forward.getName())) {
            if (actionForm.getDto("contactId") != null) {
                return new ActionForwardParameters().add("dto(contactId)", actionForm.getDto("contactId").toString()).
                        forward(forward);
            } else {
                return forward;
            }
        }

        return forward;

    }

    @Override
    protected void hasCreatedCommunications(DefaultForm form,
                                            HttpServletRequest request) {
        Boolean hasCreatedAction = (Boolean) form.getDto("hasCreatedAction");
        if (null == hasCreatedAction) {
            return;
        }

        if (hasCreatedAction) {
            return;
        }

        ActionErrors errors = new ActionErrors();
        errors.add("NoActionsCreatedForSalesProcess",
                new ActionError("Communication.msg.NoActionsCreatedForSalesProcess"));
        saveErrors(request.getSession(), errors);
    }

    protected EJBCommand getGenerateDocumentCmd() {
        log.debug("-> Read form operation=" + op + " OK");
        if ("create".equals(op)) {
            return new ActionCreateCmd();
        } else {
            return new ActionUpdateCmd();
        }
    }

    protected void setDefaultForwardOption(DefaultForm form) {
        form.setDto("forwardOption", "2");
    }
}
