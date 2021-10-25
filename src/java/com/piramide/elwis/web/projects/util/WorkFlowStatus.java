package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class WorkFlowStatus {
    private Log log = LogFactory.getLog(WorkFlowStatus.class);
    private ProjectConstants.ProjectTimeStatus actualProjectTimeStatus;

    private List<WorkFlowAction> workFlowActions = new ArrayList<WorkFlowAction>();

    public WorkFlowStatus(ProjectConstants.ProjectTimeStatus actualProjectTimeStatus) {
        this.actualProjectTimeStatus = actualProjectTimeStatus;
    }

    public void addWorkFlowAction(WorkFlowAction workFlowAction) {
        workFlowActions.add(workFlowAction);
    }

    public List<ActionError> executeActions(DefaultForm defaultForm,
                                            HttpServletRequest request
    ) {
        WorkFlowAction executedAction = getExecutedAction(request);
        if (null == executedAction) {
            log.debug("-> Cannot Retrieve WorkFlowAction !!!");
            return null;
        }
        return executedAction.execute(defaultForm, request);
    }

    private WorkFlowAction getExecutedAction(HttpServletRequest request) {
        for (WorkFlowAction workFlowAction : workFlowActions) {
            if (workFlowAction.hasExecutedAction(request)) {
                return workFlowAction;
            }
        }

        return null;
    }

    public boolean isActualProjectTimeStatus(String status) {
        return actualProjectTimeStatus.getAsString().equals(status);
    }

    public ProjectConstants.ProjectTimeStatus getActualProjectTimeStatus() {
        return actualProjectTimeStatus;
    }
}
