package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AppointmentCreateListAction;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class AppointmentListAction extends AppointmentCreateListAction {

    @Override
    protected void initializeSchedulerUserId(HttpServletRequest request) {
        //In the contacts module the schedulerUserId never its updated, because the change take collateral effects in the scheduler module.
        log.debug("The calendar user cannot be updated.");
    }

    @Override
    protected ActionForward accessRightsValidator(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request) {
        //In the contacts module the accessRights validation is performed for every appointment that are showing in the list
        log.debug("The accessRights validation are performed in the AppointmentList.jsp.");
        return null;
    }

    @Override
    protected DateTimeZone getSchedulerUserDateTimeZone(ActionMapping mapping, HttpServletRequest request) {
        //In the contacts module the timeZone loading is performed appointment that are showing in the list;
        log.debug("The TimeZone loading are performed in the AppointmentList.jsp page.");
        return null;
    }

    @Override
    protected Integer getSchedulerUserId(HttpServletRequest request) {
        //In the contacts module the schedulerUserId is null because the list will be show the appointments
        //related to different users. 
        return null;
    }

    @Override
    protected void setAdditionalFilters(ActionForm form, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        DateTimeZone zone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer stateFilter = getStateFilter(user, form, request);

        if (SchedulerConstants.SingleAppointmentFilter.PENDING.getCode().equals(stateFilter)) {
            addFilter("startDateMillis", String.valueOf(getDateTime(zone)));
            addFilter("StartDate", "true");
        }

        addFilter("addressId", request.getParameter("contactId"));
        addFilter("viewerUserId", null);

        setModuleId(request.getParameter("contactId"));
    }


    private Integer getStateFilter(User user, ActionForm form, HttpServletRequest request) {
        Integer userId = (Integer) user.getValue(Constants.USERID);

        SearchForm searchForm = (SearchForm) form;
        if (null != searchForm.getParameter("stateFilter")
                && !"".equals(searchForm.getParameter("stateFilter").toString().trim())) {
            return Integer.valueOf(searchForm.getParameter("stateFilter").toString());
        }

        Map fantabulousModules = (Map) request.getSession().getAttribute("Fantabulous.Modules");
        if (null == fantabulousModules) {
            return SchedulerConstants.SingleAppointmentFilter.PENDING.getCode();
        }

        Object module = fantabulousModules.get("/scheduler");
        if (null == module) {
            return SchedulerConstants.SingleAppointmentFilter.PENDING.getCode();
        }

        if (!request.getParameter("contactId").equals(module.toString())) {
            return SchedulerConstants.SingleAppointmentFilter.PENDING.getCode();
        }

        Map statusParameters = PersistenceManager.persistence().loadStatus(userId.toString(),
                "appointmentAdvancedListByContact",
                "contacts");

        Object stateFilter = statusParameters.get("stateFilter");
        if (null == stateFilter) {
            return SchedulerConstants.SingleAppointmentFilter.PENDING.getCode();
        } else {
            return Integer.valueOf(stateFilter.toString());
        }
    }

    private Long getDateTime(DateTimeZone dateTimeZone) {
        DateTime time = new DateTime(dateTimeZone);
        return new DateTime(time.getYear(),
                time.getMonthOfYear(),
                time.getDayOfMonth(), 0, 0, 1, 0, dateTimeZone).getMillis();
    }
}
