package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class WorkFlowAction {
    private Log log = LogFactory.getLog(WorkFlowAction.class);
    private String action;
    private ProjectConstants.ProjectTimeStatus newProjectTimeStatus;
    private WorkFlowValidator validator;
    private WorkFlowDataUpdater dataUpdater;

    public WorkFlowAction(String action,
                          ProjectConstants.ProjectTimeStatus newProjectTimeStatus,
                          WorkFlowValidator validator) {
        this.action = action;
        this.newProjectTimeStatus = newProjectTimeStatus;
        this.validator = validator;
    }

    public boolean hasExecutedAction(HttpServletRequest request) {
        return null != request.getParameter(action);
    }

    public List<ActionError> execute(DefaultForm defaultForm,
                                     HttpServletRequest request
    ) {
        log.debug("-> Action executed : " + action);
        log.debug("-> ValidatorClass  : " + validator.getClass().getName());
        log.debug("-> Form values     : " + defaultForm.getDtoMap());
        List<ActionError> errors = validator.validate(this, defaultForm, request);
        if (null != errors && !errors.isEmpty()) {
            return errors;
        }

        defaultForm.setDto("status", newProjectTimeStatus.getAsString());

        if (null != dataUpdater) {
            dataUpdater.update(defaultForm, request);
        }

        return null;
    }

    public void setDataUpdater(WorkFlowDataUpdater dataUpdater) {
        this.dataUpdater = dataUpdater;
    }

    public String getAction() {
        return action;
    }
}

