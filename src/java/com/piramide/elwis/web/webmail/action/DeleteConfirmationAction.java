package com.piramide.elwis.web.webmail.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class helps to the view of the delete mails/empty trash confirmation
 *
 * @author Alvaro
 * @version $Id: DeleteConfirmationAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class DeleteConfirmationAction extends WebmailDefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DeleteConfirmationAction.....");
        DefaultForm f = (DefaultForm) form;
        Object[] mailIds = (Object[]) request.getParameterValues("dto(mailId)");
        f.setDto("mailIds", mailIds);
        return (super.execute(mapping, form, request, response));
    }
}
