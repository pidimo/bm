package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author ivan
 *         <p/>
 *         Jatun s.r.l
 */
public class SendForwardAction extends WebmailDefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Map dto = (Map) request.getAttribute("dto");
        String saveSendItem = (String) dto.get("saveSendItem");
        List recipientsList = (List) dto.get("mailListDispatched");
        request.getSession().setAttribute("mailListDispatched", recipientsList);

        return new ActionForwardParameters()
                .add("dto(saveSendItem)", Boolean.valueOf((null != saveSendItem && "true".equals(saveSendItem.trim()))).toString())
                .add("dto(mailId)", dto.get("mailId").toString())
                .forward(mapping.findForward("Success"));
    }
}
