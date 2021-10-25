package com.piramide.elwis.web.contactmanager.action;

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
 * @version $Id: ContactTaskListAction.java 03-feb-2009 12:54:06
 */
public class ContactTaskListAction extends ContactListAction {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContactTaskListAction...........................");
        //For taskList
        (new TaskListActionUtil()).processFilterForTaskList(request, form, "taskSchedulerList", "scheduler",
                "contactId", "contacts", this);

        ActionForward af = super.execute(mapping, form, request, response);
        return (af);
    }

}
