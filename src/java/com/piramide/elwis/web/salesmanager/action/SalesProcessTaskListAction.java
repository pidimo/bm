package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.schedulermanager.action.TaskListActionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: SalesProcessTaskListAction.java 03-feb-2009 17:45:35
 */
public class SalesProcessTaskListAction extends SalesProcessListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing SalesProcessTaskListAction...........................");
        //For taskList
        (new TaskListActionUtil()).processFilterForTaskList(request, form, "taskSchedulerList", "scheduler",
                "processId", "sales", this);

        ActionForward af = super.execute(mapping, form, request, response);
        return (af);
    }
}
