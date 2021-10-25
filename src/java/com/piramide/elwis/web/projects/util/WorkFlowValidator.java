package com.piramide.elwis.web.projects.util;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public interface WorkFlowValidator {

    public List<ActionError> validate(WorkFlowAction workFlowAction,
                                      DefaultForm form,
                                      HttpServletRequest request
    );
}
