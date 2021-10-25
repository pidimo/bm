package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.webmail.util.AttachFormHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ViewMailBodyAction extends DefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForward forward = super.execute(mapping, form, request, response);

        DefaultForm defaultForm = (DefaultForm) form;

        setUrlIntoBody(defaultForm, request, response);
        return forward;
    }

    protected void setUrlIntoBody(DefaultForm defaultForm, HttpServletRequest request, HttpServletResponse response) {
        List<AttachDTO> attachments = (List<AttachDTO>) defaultForm.getDto("attachments");
        String body = (String) defaultForm.getDto("body");

        AttachFormHelper attachFormHelper = new AttachFormHelper();
        body = attachFormHelper.updateBody(body, attachments, request, response);
        defaultForm.setDto("body", body);
    }
}
