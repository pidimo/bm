package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.el.Functions;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class PasswordChangeForm extends DefaultForm {

    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getDefineRoles() {
        List list = (List) getDto("assignedRoleIds");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setDefineRoles(Object[] array) {
        if (array != null) {
            setDto("assignedRoleIds", Arrays.asList(array));
        }
    }

    public Object[] getUndefineRoles() {
        List list = (List) getDto("availableRoleIds");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setUndefineRoles(Object[] array) {
        if (array != null) {
            this.setDto("availableRoleIds", Arrays.asList(array));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);

        String op = (String) this.getDto("op");
        List assignedRoleIdList = (List) this.getDto("assignedRoleIds");
        List availableRoleIdList = (List) this.getDto("availableRoleIds");

        if (assignedRoleIdList == null || assignedRoleIdList.isEmpty()) {
            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "PasswordChange.role.assigned")));
        }

        //validate composed change time, this can not created in past time
        if (errors.isEmpty()) {
            Long changeTimeMillis = composeChangeTime(request);
            if (Functions.isOldRelatedToCurrentTime(changeTimeMillis, request)) {
                errors.add("timeError", new ActionError("PasswordChange.error.changeTime"));
            } else {
                setDto("changeTime", changeTimeMillis);
            }
        }

        if (errors.isEmpty()) {
            if ("update".equals(op)) {
                setDto("updateDateTime", getCurrentTime(request));
            }
        }

        if (!errors.isEmpty()) {
            reWriteAssignedAndAvailableRoles(assignedRoleIdList, availableRoleIdList, request);
        }
        return errors;
    }

    private void reWriteAssignedAndAvailableRoles(List assignedRoleIdList, List availableRoleIdList, HttpServletRequest request) {
        List<Map> assignedList = new ArrayList<Map>();
        List<Map> availableList = new ArrayList<Map>();

        //get all company roles
        Map passwordChangeRoles = Functions.getPasswordChangeRoles(null, request);
        List<Map> companyRoles = (List<Map>) passwordChangeRoles.get("availableRolePassChange");

        for (Map companyRoleMap : companyRoles) {
            if (assignedRoleIdList != null && assignedRoleIdList.contains(companyRoleMap.get("roleId"))) {
                assignedList.add(companyRoleMap);
            }
            if (availableRoleIdList != null && availableRoleIdList.contains(companyRoleMap.get("roleId"))) {
                availableList.add(companyRoleMap);
            }
        }

        request.setAttribute("availableRolesList", availableList);
        request.setAttribute("assignedRolesList", assignedList);
    }

    private Long composeChangeTime(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }

        Integer changeDate = getDtoValueAsInteger("changeDate");
        Integer hourChange = getDtoValueAsInteger("hourChange");
        Integer minuteChange = getDtoValueAsInteger("minuteChange");

        DateTime dateTime = DateUtils.integerToDateTime(changeDate, hourChange, minuteChange, dateTimeZone);

        return dateTime.getMillis();
    }

    private Long getCurrentTime(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        DateTime dateTime = new DateTime(System.currentTimeMillis(), dateTimeZone);
        return dateTime.getMillis();
    }

    private Integer getDtoValueAsInteger(String name) {
        Integer valueInt = null;
        try {
            valueInt = new Integer(getDto(name).toString());
        } catch (NumberFormatException e) {
        }
        return valueInt;
    }
}
