package com.piramide.elwis.web.dashboard.action;

import com.piramide.elwis.cmd.schedulermanager.LightlyTaskCmd;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class TaskSelectorAction extends AbstractDefaultAction {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionForwardParameters parameters = new ActionForwardParameters();
        parameters.add("taskId", request.getParameter("taskId"))
                .add("dto(title)", request.getParameter("dto(title)"))
                .add("dto(recordUserId)", request.getParameter("dto(recordUserId)"));

        String forwardName = "Scheduler";
        if (hasSalesProcessViewAccessRight(request)) {
            Integer salesProcessId = getSalesProcessId(request);
            if (null != salesProcessId) {
                parameters.add("processId", salesProcessId.toString())
                        .add("tabKey", "Scheduler.Tasks")
                        .add("module", "sales");

                forwardName = "Sales";
            } else {
                parameters.add("module", "scheduler");
            }
        } else {
            parameters.add("module", "scheduler");
        }

        return parameters.forward(mapping.findForward(forwardName));
    }

    private boolean hasSalesProcessViewAccessRight(HttpServletRequest request) {
        return Functions.hasAccessRight(request, "SALESPROCESS", "VIEW");
    }

    private Integer getSalesProcessId(HttpServletRequest request) {
        TaskDTO taskDTO = getTaskDTO(getTaskId(request), request);
        if (null != taskDTO) {
            return (Integer) taskDTO.get("processId");
        }

        return null;
    }

    private TaskDTO getTaskDTO(String taskId, HttpServletRequest request) {
        LightlyTaskCmd cmd = new LightlyTaskCmd();
        cmd.setOp("getTask");
        cmd.putParam("taskId", taskId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            if (null != resultDTO.get("getTask")) {
                return (TaskDTO) resultDTO.get("getTask");
            }
        } catch (AppLevelException e) {
            //
        }

        return null;
    }

    private String getTaskId(HttpServletRequest request) {
        return request.getParameter("taskId");
    }
}
