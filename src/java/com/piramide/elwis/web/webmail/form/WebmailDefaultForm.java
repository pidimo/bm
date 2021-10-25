package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.WebmailReadCmd;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: WebmailDefaultForm.java 9098 2009-04-03 11:20:08Z ivan ${CLASS_NAME}.java,v 1.2 14-02-2005 04:02:54 PM ivan Exp $
 */
public class WebmailDefaultForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("WebmailDefaultForm validate method...");

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        WebmailReadCmd webmailReadCmd = new WebmailReadCmd();
        webmailReadCmd.putParam("userId", user.getValue("userId"));
        webmailReadCmd.putParam("companyId", user.getValue("companyId"));

        ResultDTO resultDTO;
        try {
            resultDTO = BusinessDelegate.i.execute(webmailReadCmd, request);

            if (resultDTO.get(UserMailDTO.KEY_USERMAILID) != null) {
                this.setDto(UserMailDTO.KEY_USERMAILID, resultDTO.get(UserMailDTO.KEY_USERMAILID).toString());
            } else {
                request.setAttribute("hideMailConfigurationTabs", true);
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + WebmailReadCmd.class.getName() + " Fail", e);
        }

        return super.validate(mapping, request);
    }
}
