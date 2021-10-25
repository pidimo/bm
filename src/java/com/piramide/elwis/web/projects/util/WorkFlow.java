package com.piramide.elwis.web.projects.util;

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
public class WorkFlow {
    private Log log = LogFactory.getLog(WorkFlow.class);
    private List<WorkFlowStatus> workFlowStatuses = new ArrayList<WorkFlowStatus>();

    public WorkFlow() {
    }

    public void addWorkFlowStatus(WorkFlowStatus workFlowStatus) {
        this.workFlowStatuses.add(workFlowStatus);
    }

    public List<ActionError> apply(String status,
                                   DefaultForm defaultForm,
                                   HttpServletRequest request
    ) {
        WorkFlowStatus workFlowStatus = getStatus(status);
        if (null == workFlowStatus) {
            log.debug("-> Cannot retrieve WorFlowStatus for " + status);
            return null;
        }

        log.debug("-> Work on Status  : " + workFlowStatus.getActualProjectTimeStatus().getAsString());
        return workFlowStatus.executeActions(defaultForm, request);
    }

    private WorkFlowStatus getStatus(String status) {
        for (WorkFlowStatus workFlowStatus : workFlowStatuses) {
            if (workFlowStatus.isActualProjectTimeStatus(status)) {
                return workFlowStatus;
            }
        }

        return null;
    }
}
