package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class PasswordChangeAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        ActionForward forward;
        if (null != (forward = validatePasswordChangeExistence(request, mapping))) {
            return forward;
        }

        forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        setChangeTimeAsDateHourMinute(defaultForm, dtoValues, request);

        return forward;
    }

    private void setChangeTimeAsDateHourMinute(DefaultForm defaultForm, Map resultDtoValues, HttpServletRequest request) {
        if (resultDtoValues != null) {
            if (resultDtoValues.containsKey("changeTime")) {

                User user = RequestUtils.getUser(request);
                DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
                if (dateTimeZone == null) {
                    dateTimeZone = DateTimeZone.getDefault();
                }
                Long changeTime = Long.valueOf(resultDtoValues.get("changeTime").toString());
                DateTime dateTime = DateUtils.createDate(changeTime, dateTimeZone.getID());

                defaultForm.setDto("changeDate", DateUtils.dateToInteger(dateTime));
                defaultForm.setDto("hourChange", dateTime.getHourOfDay());
                defaultForm.setDto("minuteChange", dateTime.getMinuteOfHour());
            }
        }
    }

    protected ActionForward validatePasswordChangeExistence(HttpServletRequest request, ActionMapping mapping) {
        if (!existsPasswordChange(request.getParameter("dto(passwordChangeId)"))) {
            String description = request.getParameter("dto(description)");
            ActionErrors errors = new ActionErrors();
            errors.add("passwordChangeDelete", new ActionError("customMsg.NotFound", description));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }
        return null;
    }

    private boolean existsPasswordChange(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                AdminConstants.TABLE_PASSWORDCHANGE,
                "passwordchangeid",
                keyValue,
                errors, new ActionError("customMsg.NotFound"));
        return errors.isEmpty();
    }
}
