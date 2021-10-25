package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.webmail.form.MailAddressSentForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: MailAddressSentAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class MailAddressSentAction extends WebmailDefaultAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        MailAddressSentForm f = (MailAddressSentForm) form;

        log.debug("Executing MailAddressSentAction........");

        // read checked mails
        Object[] selectedMails = f.getSelectedMails();
        List listAddress = new ArrayList();
        for (int i = 0; i < selectedMails.length; i++) {
            Map addressMap = new HashMap(4);
            addressMap.put("address", selectedMails[i].toString());
            addressMap.put("emailIdTemp", String.valueOf(i));
            addressMap.put("typeAddress", WebMailConstants.BLANK_KEY);
            addressMap.put("isUpdate", "false");
            listAddress.add(addressMap);
        }
        request.setAttribute("listAddress", listAddress);

        return super.execute(mapping, f, request, response);
        //return mapping.findForward("Success");
    }
}
