package com.piramide.elwis.web.schedulermanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: ConfigurationForm.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 04-05-2005 12:01:54 PM ivan Exp $
 */
public class ConfigurationForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate method...");

        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        if (errors.isEmpty()) {
            Integer initialValue = Integer.valueOf(getDto("initialDayOfWork").toString());
            Integer finalValue = Integer.valueOf(getDto("finalDayOfWork").toString());


            if (initialValue.intValue() > finalValue.intValue()) {


                log.debug("initial value " + initialValue);
                log.debug("final value " + finalValue);
                String msg1 = JSPHelper.getMessage(request, "Scheduler.initDayOfWork");
                String msg2 = JSPHelper.getMessage(request, "Scheduler.finalDayOfWork");

                errors.add("dayOfWork", new ActionError("error.greaterThan", msg1, msg2));
            }
        }
        return errors;
    }
}
