package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.MailTrayCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 *         Date: Aug 15, 2006
 *         Time: 12:15:37 PM
 */
public class MoveAllEmailToTrashConfirmation extends WebmailDefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm myform = (DefaultForm) form;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);


        Integer userMailId = new Integer(user.getValue("userId").toString());
        DTO dto = new DTO();
        dto.put("userMailId", userMailId);
        dto.put("companyId", user.getValue("companyId"));
        dto.putAll(myform.getDtoMap());

        MailTrayCmd cmd = new MailTrayCmd();
        cmd.putParam(dto);
        BusinessDelegate.i.execute(cmd, request);

        return new ActionForwardParameters()
                .add("folderId", (String) dto.get("folderId"))
                .add("index", "0")
                .add("returning", "true").forward(mapping.findForward("Success"));

    }
}
