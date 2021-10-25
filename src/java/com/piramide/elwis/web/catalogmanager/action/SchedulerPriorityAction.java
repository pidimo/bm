package com.piramide.elwis.web.catalogmanager.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 10:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class SchedulerPriorityAction extends com.piramide.elwis.web.common.action.ListAction {
    protected static Log log = LogFactory.getLog(SchedulerPriorityAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        addFilter("type", "SCHEDULER");
        return super.execute(mapping, form, request, response);
    }
}
