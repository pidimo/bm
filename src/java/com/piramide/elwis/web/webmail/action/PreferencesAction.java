package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public class PreferencesAction extends DefaultAction {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        ActionForward forward = super.execute(mapping, form, request, response);
        if ("Success".equals(forward.getName())) {
            Boolean backgroundDownload = (Boolean) defaultForm.getDto("backgroundDownload");
            RequestUtils.getUser(request).setValue("backgroundDownload", backgroundDownload);
        }
        return forward;
    }
}
