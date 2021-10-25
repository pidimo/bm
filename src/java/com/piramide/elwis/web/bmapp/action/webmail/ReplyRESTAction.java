package com.piramide.elwis.web.bmapp.action.webmail;

import com.piramide.elwis.web.webmail.action.ReplyAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ReplyRESTAction extends ReplyAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ReplyRESTAction..." + request.getParameterMap());

        //define default values to read
        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("filterInvalidRecipients", "true");

        return super.execute(mapping, defaultForm, request, response);
    }
}
