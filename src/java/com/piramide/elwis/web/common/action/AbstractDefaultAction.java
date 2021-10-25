package com.piramide.elwis.web.common.action;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionBusinessDelegate;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Tayes
 * @version $Id: AbstractDefaultAction.java 10068 2011-07-01 15:57:26Z fernando $
 */

public abstract class AbstractDefaultAction extends net.java.dev.strutsejb.web.DefaultAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward checkCancel(ActionMapping mapping, HttpServletRequest request) {
        if (isCancelled(request)) {
            log.debug("Is cancel");
            String toCancel = "Cancel";
            if (request.getParameter("cancel") != null) {
                toCancel = "Success";
            }
            return mapping.findForward(toCancel);
        }
        return null;
    }

    /**
     * Check if cancel button has been pressed, if true return cancel forward, else
     * execute generic Action
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //log.debug("Elwis AbstractDefaultAction executing...");

        //if <CANCEL_BUTTON> is pressed
        ActionForward forward;

        if ((forward = checkCancel(mapping, request)) != null) {
            return forward;
        } else {
            //log.debug("Execute..");
            forward = super.execute(mapping, form, request, response);
            //if <SAVEANDNEW_BUTTON is pressed>
            if (request.getParameter("SaveAndNew") != null) {
                //log.debug("Is SaveAndNew");
                ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
                DefaultForm newForm = (DefaultForm) form;
                //If errors occur in the filling the form
                if (errors != null) {
                    if (!errors.isEmpty()) {
                        request.setAttribute(Globals.ERROR_KEY, errors);
                        return mapping.getInputForward();
                    }
                } else {
                    newForm.getDtoMap().clear();
                    forward = mapping.findForward("SaveAndNew");
                }
            }
            return forward;
        }
    }

    /**
     * save errors in the session
     *
     * @param session
     * @param errors
     */

    protected void saveErrors(HttpSession session, ActionErrors errors) {
        if (errors == null || errors.isEmpty()) {
            session.removeAttribute(Globals.ERROR_KEY);
        } else {
            session.setAttribute(Globals.ERROR_KEY, errors);
        }
    }

    /**
     * This is the responsible method that executes the EJBCommand in the default strutsejb StatelessFacade or
     * in the improved BeanTransactionStatelessFacade which allows the possibility to control the transaction timeout
     *
     * @param req    the request
     * @param ejbCmd the command to execute
     * @return the resultDTO
     * @throws AppLevelException
     */
    @Override
    protected ResultDTO callBusinessDelegate(
            HttpServletRequest req,
            final EJBCommand ejbCmd)
            throws AppLevelException {
        if (ejbCmd instanceof BeanTransactionEJBCommand) {
            return BeanTransactionBusinessDelegate.i.execute((BeanTransactionEJBCommand) ejbCmd);
        } else {
            return BusinessDelegate.i.execute(ejbCmd, req);
        }
    }
}
