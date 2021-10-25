package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.ActivityUserPercentCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 * form to validate automatic assign of campaign contact in activity user
 *
 * @author Miky
 * @version $Id: AutomaticAssignForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AutomaticAssignForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate AutomaticAssignForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (request.getParameter("assignButton") != null) {
            log.debug("validate before assignation...");

            ActivityUserPercentCmd cmd = new ActivityUserPercentCmd();
            cmd.putParam("activityId", getDto("activityId"));

            ResultDTO resultDto = new ResultDTO();
            try {
                resultDto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
            }

            if (resultDto.containsKey("emptyUser")) {
                errors.add("emptyU", new ActionError("Campaign.activity.user.automaticAssign.emptyActivityUser"));
            }
            if (resultDto.containsKey("emptyContact")) {
                errors.add("emptyC", new ActionError("Campaign.activity.user.automaticAssign.notPosibleAssign"));
            }

            if (errors.isEmpty()) {
                if (getDto("homogeneously") == null) {
                    //assign is by percent
                    if (resultDto.containsKey("exceedPercent")) {
                        errors.add("criteria", new ActionError("Campaign.activity.user.automaticAssign.exceedPercent"));
                    }
                    if (resultDto.containsKey("nullPercent")) {
                        errors.add("criteria", new ActionError("Campaign.activity.user.automaticAssign.nullPercent"));
                    }
                    if (resultDto.containsKey("needPercent")) {
                        errors.add("criteria", new ActionError("Campaign.activity.user.automaticAssign.lowPercent"));
                    }
                }
            }
        }

        return errors;
    }
}

