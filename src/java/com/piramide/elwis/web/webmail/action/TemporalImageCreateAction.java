package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action to manage create temporal images
 *
 * @author Miky
 * @version $Id: TemporalImageCreateAction.java 2009-05-25 05:38:52 PM $
 */
public class TemporalImageCreateAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing TemporalImageCreateAction........" + request.getParameterMap());

        ActionForward forward = super.execute(mapping, form, request, response);
        log.debug("After of cmd execute...................." + request.getAttribute("dto"));

        if ("Success".equals(forward.getName())) {
            Map dtoValues = (Map) request.getAttribute("dto");
            String imageStoreId = dtoValues.get("imageStoreId").toString();

            //set in request id to download into editor body
            request.setAttribute("createdImageStoreId", imageStoreId);
        }

        return forward;
    }
}