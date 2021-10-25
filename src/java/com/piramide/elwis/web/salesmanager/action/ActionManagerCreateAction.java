package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.cmd.salesmanager.ActionCreateCmd;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ActionManagerCreateAction extends ActionManagerAction {
    protected EJBCommand getGenerateDocumentCmd() {
        return new ActionCreateCmd();
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
}
