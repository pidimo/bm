package com.piramide.elwis.web.common.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: CancelListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CancelListAction extends ListAction {
    protected static Log log = LogFactory.getLog(CancelListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        log.debug("Cancel Action....");

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        return super.execute(mapping, form, request, response);
    }
}
