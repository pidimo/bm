package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import com.piramide.elwis.web.schedulermanager.form.TaskForm;
import com.piramide.elwis.web.schedulermanager.util.TaskFormUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mauren Carrasco
 * @version 4.2.2
 */
public class TaskCreateAction extends CheckEntriesForwardAction {

    @SuppressWarnings(value = "unchecked")
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        TaskForm taskForm = (TaskForm) form;
        taskForm.getDtoMap().putAll(TaskFormUtil.i.getDefaultValuesForCreate(request));
        taskForm.getDtoMap().putAll(TaskFormUtil.i.getDefaultValuesFromSessionUser(request));

        return super.execute(mapping, taskForm, request, response);
    }
}
