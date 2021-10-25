package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: May 13, 2005
 * Time: 12:16:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskAdvancedListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        //for to set schedulerUserId in the session
        log.debug("taskAdvancedListAction ..execute ..");
        SearchForm searchForm = (SearchForm) form;
        User user = RequestUtils.getUser(request);
        Integer userId = (Integer) user.getValue("userId");
        Integer schedulerUserId = (Integer) user.getValue("schedulerUserId");
        String paramSchedulerUserId = request.getParameter("schedulerUserId");

        log.debug("UserId:" + userId);
        log.debug("schedulerUserId:" + schedulerUserId);
        log.debug("ParamschedulerUserId:" + paramSchedulerUserId);

        if (schedulerUserId == null) {//For first entry
            schedulerUserId = userId;
            user.setValue("schedulerUserId", userId);
        } else if (paramSchedulerUserId != null) { // for other user calendar
            log.debug("Is other user");
            try {
                schedulerUserId = new Integer(paramSchedulerUserId);
            } catch (NumberFormatException e) {
                schedulerUserId = userId;
            }
            if (userId.equals(schedulerUserId)) {
                user.setValue("schedulerUserId", schedulerUserId);
            }
        }

        //Initialize the fantabulous filter in empty
        log.debug("TaskAdvancedListAction      execute  ....");
        super.initializeFilter();

        if (!"on".equals(request.getParameter("parameter(userType)"))) {
            addFilter("externalUserAlso", SchedulerConstants.TRUE_VALUE);
        }

        addFilter("taskId", request.getParameter("taskId"));

        // simple search - process to show ALL or only NOT CONCLUDED (By default)
        if (SchedulerConstants.TRUE_VALUE.equals(request.getParameter("simple"))) {
            //For taskList
            (new TaskListActionUtil()).processFilterForTaskList(request, form, "taskSingleList", "scheduler",
                    null, "scheduler", this);
        }
        addFilter("taskId", request.getParameter("taskId"));

        return super.execute(mapping, searchForm, request, response);
    }
}