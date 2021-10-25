package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.cmd.schedulermanager.LoadHolidaysCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.taglib.CalendarIU;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Jatun S.R.L.
 * General action to manage UI appointment view
 *
 * @author Miky
 * @version $Id: AbstractAppointmentUIAction.java 12551 2016-05-19 23:56:23Z miguel $
 */
public abstract class AbstractAppointmentUIAction extends Action {
    protected static Log log = LogFactory.getLog(AbstractAppointmentUIAction.class);

    public static final int DAILY_VIEW_TYPE = 1;
    public static final int WEEKLY_VIEW_TYPE = 2;
    public static final int MONTHLY_VIEW_TYPE = 3;
    public static final int YEARLY_VIEW_TYPE = 4;
    public static final int LIST_VIEW_TYPE = 5;

    public static final String APPOINTMENT_IN_RANGE = "appointmentInRangeDays";
    public static final String APPOINTMENTS_DATA = "appointmentsData";
    public static final String APPOINTMENTS_YEAR = "appointmentsYear";
    public static final String HOLIDAYS = "holidays";

    private int year;
    private int day;
    private int week_month;

    public static final String USER_TIMEZONE = "timeZone";


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        initializeSchedulerUserId(request);

        ActionForward accessRightsForward = accessRightsValidator(mapping, form, request);
        if (null != accessRightsForward) {
            return accessRightsForward;
        }

        Integer userId = getUserId(request);
        Integer schedulerUserId = getSchedulerUserId(request);

        ResultDTO userInformationDTO = getUserInformation(userId, schedulerUserId, request);

        Integer type = getViewType(request, userInformationDTO);

        DateTimeZone timeZone = getDateTimeZone(userInformationDTO);

        initializeCalendarDate(type, request.getParameter("calendar"), request.getParameter("oldType"), timeZone);

        log.debug("The calendar is working with(YEAR:" + year + ", WEEK_MONTH:" + week_month + ", DAY:" + day + ")");

        boolean isYearlyView = false; // Para saber si se procesara los appointments, no se procesa solo en YEARLY VIEW

        String forward;
        DateTime startRangeDate;
        DateTime endRangeDate;
        switch (type) {
            case WEEKLY_VIEW_TYPE://week
                forward = "weekly";
                CalendarIU calendar = new CalendarIU();
                calendar.setWeek(week_month, year);
                startRangeDate = calendar.getWeek().getStartDate(timeZone);
                endRangeDate = calendar.getWeek().getEndDate(startRangeDate);
                request.setAttribute("week", new Integer(week_month));
                request.setAttribute("year", new Integer(year));
                break;
            case MONTHLY_VIEW_TYPE://month
                forward = "monthly";
                CalendarIU cal = new CalendarIU(week_month, year);
                Iterator weeks = cal.getIteratorWeek();
                CalendarIU.Week week = (CalendarIU.Week) weeks.next();
                startRangeDate = week.getStartDate(timeZone);
                while (weeks.hasNext()) {
                    week = (CalendarIU.Week) weeks.next();
                    while (week.hasNextDay()) {
                        week.nextDay();
                    }
                }
                endRangeDate = week.getEndDate(week.getStartDate(timeZone));
                request.setAttribute("month", new Integer(week_month));
                request.setAttribute("year", new Integer(year));
                break;
            case YEARLY_VIEW_TYPE: //year
                forward = "yearly";
                request.setAttribute("year", new Integer(year));
                startRangeDate = new DateTime(year, 1, 1, 0, 0, 1, 0, timeZone);
                endRangeDate = new DateTime(year, 12, 31, 0, 0, 0, 0, timeZone);
                isYearlyView = true;
                break;
            case DAILY_VIEW_TYPE:
            default:
                forward = "daily";
                if ("true".equals(request.getParameter("ajax"))) {
                    forward = "ajax";
                }

                YearMonthDay ymd;
                try {
                    ymd = new YearMonthDay(year, week_month, day);
                } catch (IllegalArgumentException e) {
                    ymd = new YearMonthDay();
                }
                startRangeDate = ymd.toDateTimeAtMidnight(timeZone);
                endRangeDate = ymd.toDateTimeAtMidnight(timeZone);

                Integer interval;
                interval = (Integer) userInformationDTO.get("dayFragmentation");

                if (interval == null) {
                    interval = 30;
                } else {
                    interval = interval * 15;
                }

                request.setAttribute("minuteIntervalTime", interval);
                request.setAttribute("date", DateUtils.dateToInteger(startRangeDate.toDateTime()));
        }

        endRangeDate = endRangeDate.plus(new Period(23, 59, 0, 0));

        Map holidaysMap = getHolidays(startRangeDate,
                endRangeDate,
                timeZone,
                schedulerUserId,
                type,
                year,
                userInformationDTO,
                request);

        request.setAttribute(HOLIDAYS, holidaysMap);

        log.debug("startRangeDate: " + startRangeDate);
        log.debug("endRangeDate: " + endRangeDate);

        //recovery the appointment data
        Map<String, Object> appointmentDataMap = getAppointmentData(startRangeDate,
                endRangeDate,
                timeZone,
                isYearlyView,
                schedulerUserId,
                request);
        if (isYearlyView) {
            request.setAttribute(APPOINTMENTS_YEAR, appointmentDataMap.get(APPOINTMENTS_YEAR));
        } else {
            request.setAttribute(APPOINTMENT_IN_RANGE, appointmentDataMap.get(APPOINTMENT_IN_RANGE));
            request.setAttribute(APPOINTMENTS_DATA, appointmentDataMap.get(APPOINTMENTS_DATA));
        }

        request.setAttribute(USER_TIMEZONE, timeZone);
        request.setAttribute("view", "calendar");
        request.setAttribute("workStartHour", getInitialDayOfWork(userInformationDTO));
        request.setAttribute("workEndHour", getFinalDayOfWork(userInformationDTO));

        return mapping.findForward(forward);
    }

    /**
     * Implements this method to get appointment data
     * return an Map with key: { AbstractAppointmentUIAction.APPOINTMENTS_DATA,
     * AbstractAppointmentUIAction.APPOINTMENT_IN_RANGE,
     * AbstractAppointmentUIAction.APPOINTMENTS_YEAR }
     *
     * @param startRangeDate
     * @param endRangeDate
     * @param timeZone
     * @param isYearlyView
     * @param schedulerUserId
     * @param request
     * @return
     * @throws Exception
     */
    protected abstract Map<String, Object> getAppointmentData(DateTime startRangeDate,
                                                              DateTime endRangeDate,
                                                              DateTimeZone timeZone,
                                                              boolean isYearlyView,
                                                              Integer schedulerUserId,
                                                              HttpServletRequest request) throws Exception;

    protected ActionForward accessRightsValidator(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request) {
        return null;
    }

    protected void initializeSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        if (null == user.getValue("schedulerUserId")) {
            user.setValue("schedulerUserId", user.getValue(Constants.USERID));
        }
    }

    protected Integer getViewType(HttpServletRequest request, ResultDTO userInformationDTO) {
        Integer viewType = DAILY_VIEW_TYPE;

        String viewTypeParameter = request.getParameter("type");
        try {
            viewType = Integer.parseInt(viewTypeParameter);
        } catch (Exception e) {
            log.debug("The request viewType: '" + viewTypeParameter + "' parameter its invalid.");
            Integer calendarDefaultView = (Integer) userInformationDTO.get("calendarDefaultView");
            if (null != calendarDefaultView) {
                viewType = calendarDefaultView;
            }
        }

        log.debug("Current viewType: " + viewType);
        return viewType;
    }

    protected DateTimeZone getDateTimeZone(ResultDTO userInformationDTO) {
        if (null != userInformationDTO.get("timeZone")) {
            return DateTimeZone.forID((String) userInformationDTO.get("timeZone"));
        }

        return DateTimeZone.getDefault();
    }

    protected Map getHolidays(DateTime startRangeDate,
                              DateTime endRangeDate,
                              DateTimeZone timeZone,
                              Integer schedulerUserId,
                              Integer type,
                              Integer year,
                              ResultDTO userInformationDTO,
                              HttpServletRequest request) {

        User user = RequestUtils.getUser(request);

        LoadHolidaysCmd holidaysCmd = new LoadHolidaysCmd();
        holidaysCmd.putParam("startRangeDate", startRangeDate);
        holidaysCmd.putParam("endRangeDate", endRangeDate);
        holidaysCmd.putParam("timeZone", timeZone);
        holidaysCmd.putParam("userId", schedulerUserId);
        holidaysCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        holidaysCmd.putParam("type", type);
        holidaysCmd.putParam("year", year);
        if (userInformationDTO.containsKey("countryId")) {
            holidaysCmd.putParam("countryId", userInformationDTO.get("countryId"));
            holidaysCmd.putParam("countryCode", userInformationDTO.get("countryCode"));
        }
        try {
            BusinessDelegate.i.execute(holidaysCmd, null);
            return holidaysCmd.getHolidays();
        } catch (AppLevelException e) {
            log.warn("Cannot execute LoadHolidaysCmd, by default return an empty holidays Map object.", e);

            return new HashMap();
        }
    }

    protected Integer getInitialDayOfWork(ResultDTO userInformationDTO) {
        Integer startWork = 8;
        if (null != userInformationDTO.get("initialDayOfWork")) {
            startWork = (Integer) userInformationDTO.get("initialDayOfWork");
        }

        return startWork;
    }

    protected Integer getFinalDayOfWork(ResultDTO userInformationDTO) {
        Integer endWork = 18;
        if (null != userInformationDTO.get("finalDayOfWork")) {
            endWork = (Integer) userInformationDTO.get("finalDayOfWork");
        }

        return endWork;
    }

    protected Integer getSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue("schedulerUserId");
    }

    protected Integer getUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue(Constants.USERID);
    }

    private Integer getInteger(String dateStr) {
        Integer date;
        try {
            date = new Integer(dateStr);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    private void initializeCalendarDate(int type, String value, String oldType, DateTimeZone timezone) {
        Integer date = getInteger(value);
        int[] dateValues;

        if (oldType != null && oldType.length() > 0) {
            dateValues = buildBackDateCalendar(date, type, timezone);
        } else {
            dateValues = buildValidCalendarDate(type, date, timezone);
        }

        day = dateValues[2];
        week_month = dateValues[1];
        year = dateValues[0];
    }

    private int[] buildBackDateCalendar(Integer date, int type, DateTimeZone timezone) {
        if (date == null) {
            date = DateUtils.dateToInteger(new DateTime(timezone));
        }
        int ymd[] = DateUtils.getYearMonthDay(date);
        if (type == WEEKLY_VIEW_TYPE) {
            DateTime dateTime = new DateTime(ymd[0], ymd[1], ymd[2], 0, 0, 0, 0, timezone);
            ymd[0] = dateTime.getWeekyear();
            ymd[1] = dateTime.getWeekOfWeekyear();
        }
        return ymd;
    }

    private int[] buildValidCalendarDate(int type, Integer calendarData, DateTimeZone timeZone) {
        int[] values = new int[3];

        if (calendarData != null) {

            if (DateUtils.isIntegerDateFormat(calendarData)) {
                values = splitValidDate(type, calendarData, timeZone);

            } else {

                int calendar = calendarData.intValue();
                int y, m_w, d;
                y = m_w = d = 1;

                if (DAILY_VIEW_TYPE == type) {
                    y = calendar / 10000;
                    m_w = (calendar / 100) - y * 100;
                    d = calendar % 100;
                } else {
                    if (calendar / 100 > 9999) {
                        calendar = calendar / 100;
                    }
                    y = calendar;

                    if (WEEKLY_VIEW_TYPE == type || MONTHLY_VIEW_TYPE == type) {
                        y = calendar / 100;
                        DateTime weekYear = new DateTime(y, 1, 1, 0, 0, 0, 0);
                        m_w = calendar % 100;
                        if (m_w < 1) {
                            m_w = 1;
                        } else if (WEEKLY_VIEW_TYPE == type) {
                            if (m_w > weekYear.weekOfWeekyear().getMaximumValue()) {
                                m_w = weekYear.weekOfWeekyear().getMaximumValue();
                            }
                        } else if (MONTHLY_VIEW_TYPE == type) {
                            if (m_w > 12) {
                                m_w = 12;
                            }
                        }
                    }
                }

                values[0] = y;
                values[1] = m_w;
                values[2] = d;
            }
        } else {
            values = splitValidDate(type, null, timeZone);
        }
        return values;
    }

    private int[] splitValidDate(int type, Integer date, DateTimeZone timeZone) {
        int[] values = new int[3];
        int y, m_w, d;
        DateTime dateTime;

        if (date != null) {
            dateTime = DateUtils.integerToDateTime(date, timeZone);
        } else {
            dateTime = new DateTime(timeZone);
        }

        y = dateTime.getYear();
        if (WEEKLY_VIEW_TYPE == type) {
            y = dateTime.getWeekyear();
            m_w = dateTime.getWeekOfWeekyear();
        } else {
            m_w = dateTime.getMonthOfYear();
        }
        d = dateTime.getDayOfMonth();

        values[0] = y;
        values[1] = m_w;
        values[2] = d;
        return values;
    }

    private ResultDTO getUserInformation(Integer userId,
                                         Integer schedulerUserId,
                                         HttpServletRequest request) {
        UserInfoCmd userInfoCmd = new UserInfoCmd();

        userInfoCmd.putParam("sameUser", userId.equals(schedulerUserId));
        userInfoCmd.putParam("isAppointment", CampaignConstants.TRUEVALUE);
        userInfoCmd.putParam("otherUserId", userId);
        userInfoCmd.putParam("userId", schedulerUserId);

        try {
            return BusinessDelegate.i.execute(userInfoCmd, request);
        } catch (AppLevelException e) {
            return null;
        }
    }
}
