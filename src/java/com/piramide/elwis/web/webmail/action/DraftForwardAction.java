package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: ivan
 * Date: 13-12-2006: 11:38:23 AM
 */
public class DraftForwardAction extends WebmailDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm f = (DefaultForm) form;

        super.execute(mapping, form, request, response);

        ActionForward actionForward = new ActionForward();
        actionForward.setName("Success");

        User user = RequestUtils.getUser(request);
        Map systemFolders = com.piramide.elwis.web.webmail.el.Functions.getSystemFolders(
                user.getValue(Constants.USERID).toString(), request);

        Object folderId = f.getDto("folderId");

        if (null != systemFolders && null != systemFolders.get("draftId")) {
            folderId = systemFolders.get("draftId").toString();
            f.setDto("folderId", folderId);
        }

        actionForward.setPath("/Mail/MailTray.do?mailFilter=" + f.getDto("mailFilter") +
                "&folderId=" + folderId +
                "&index=0");

        return actionForward;
    }
}
