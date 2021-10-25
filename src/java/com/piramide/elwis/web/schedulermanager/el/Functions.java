package com.piramide.elwis.web.schedulermanager.el;

import com.piramide.elwis.cmd.admin.UserAdminCmd;
import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.schedulermanager.LightlyAppointmentReadCmd;
import com.piramide.elwis.cmd.schedulermanager.SchedulerPermissionCmd;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.schedulermanager.action.AbstractAppointmentUIAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Functions.java 12674 2017-04-28 22:43:29Z miguel $
 */
public class Functions {
    private static Log log = LogFactory.getLog(com.piramide.elwis.web.schedulermanager.el.Functions.class);

    /**
     * return a list of the user of the that may view your calendar
     *
     * @param servletRequest , is el request with the userId actual
     * @return list of users
     */
    public static List getUserViewCalendar(ServletRequest servletRequest) {
        log.debug("Executing Function getUserViewCalendar in scheduler          .....");
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        LabelValueBean labelValueBean = null;
        ArrayList listUser = new ArrayList();
        ArrayList resultList = new ArrayList();
        UserAdminCmd adminCmd = new UserAdminCmd();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        SchedulerPermissionCmd cmd = new SchedulerPermissionCmd();
        cmd.putParam("op", "userViewCalendarList");
        cmd.putParam(SchedulerPermissionCmd.USERID, userId);

        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
            resultList = (ArrayList) resultDto.get(SchedulerPermissionCmd.RESULT);
        } catch (AppLevelException e) {
            log.error("Error executing", e);
        }

        for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {

            Map userMap = (Map) iterator.next();
            Integer userViewId = Integer.valueOf(userMap.get("userId").toString());
            try {
                adminCmd.putParam("userId", userViewId);
                adminCmd.putParam("companyId", user.getValue("companyId"));
                adminCmd.putParam("operation", SchedulerConstants.TRUE_VALUE);
                ResultDTO resultUserDto = BusinessDelegate.i.execute(adminCmd, request);
                labelValueBean = new LabelValueBean(getUserName((Integer) resultUserDto.get("addressId")), String.valueOf(userViewId));
                listUser.add(labelValueBean);
            } catch (AppLevelException e) {
            }
        }
        //orber by name
        listUser = SortUtils.orderByProperty(listUser, "label");

        //default user
        labelValueBean = new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.user.myCalendar"), String.valueOf(userId));
        listUser.add(0, labelValueBean);

        //shared calendars
        if (resultList.size() > 0) {
            labelValueBean = new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.overviewCalendar.sharedCalendars"), String.valueOf(SchedulerConstants.OVERVIEW_SHAREDCALENDARS));
            listUser.add(1, labelValueBean);
        }

        return listUser;
    }

    public static List<LabelValueBean> getCalendarUsers(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        List<UserDTO> externalCalendarUsers =
                SchedulerAccessRightUtil.i.getExternalCalendarUsers((Integer) user.getValue(Constants.USERID), request);

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (UserDTO dto : externalCalendarUsers) {
            result.add(new LabelValueBean(dto.get("userName").toString(), dto.get("userId").toString()));
        }

        result = SortUtils.orderByProperty((ArrayList) result, "label");

        result.add(0, new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.user.myCalendar"),
                String.valueOf(user.getValue(Constants.USERID))));
        return result;
    }

    /**
     * get the user name of this user
     *
     * @param userId
     * @return String, user name
     */
    private static String getUserName(Integer userId) {
        String addressName = "";
        String addressType = null;
        Object name1 = null;
        Object name2 = null;
        Object name3 = null;
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, null);
            addressType = resultDTO.get("addressType").toString();
            name1 = resultDTO.get("name1");
            name2 = resultDTO.get("name2");
            name3 = resultDTO.get("name3");
        } catch (AppLevelException e) {
            log.error("Error executing", e);
        }
        if (addressType != null) {
            if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {
                addressName = name1 + ""
                        + ((name2 != null && !"".equals(name2.toString())) ? ", " + name2 : ""); // name1 is apellido, name2 is name;
            } else if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {
                addressName = name1
                        + " " + ((name2 != null) ? name2 : "")
                        + " " + ((name3 != null) ? name3 : "");
            }
        }

        return addressName;
    }

    /**
     * get the permission of the user respect of owner of the calendar
     *
     * @param ownerUserId , id of the calendar owner
     * @param userId      , id of user
     * @return Map{'public','private'} Valid Byte if has assigned permission
     *         <li>Byte with value "0" if is same user  </li>
     *         <li><code>null</code> Byte if hasn't permission assigned</li>
     */
    public static Map<String, Byte> getUserPermissions(Integer ownerUserId, Integer userId) {
        Map<String, Byte> permissionMap = new HashMap<String, Byte>();

        if (ownerUserId != null && userId != null) {

            SchedulerPermissionCmd cmd = new SchedulerPermissionCmd();
            cmd.putParam("op", "getUserPermission");
            cmd.putParam(SchedulerPermissionCmd.OWNER_USERID, ownerUserId);
            cmd.putParam(SchedulerPermissionCmd.USERID, userId);

            try {
                ResultDTO resultDto = BusinessDelegate.i.execute(cmd, null);
                permissionMap.put("public", (Byte) resultDto.get("permissionPublic"));
                permissionMap.put("private", (Byte) resultDto.get("permissionPrivate"));

            } catch (AppLevelException e) {
                log.error("Error executing", e);
            }

        } else {
            permissionMap.put("public", Byte.valueOf("0"));
            permissionMap.put("private", Byte.valueOf("0"));
        }
        return permissionMap;
    }


    public static DateTimeZone getSchedulerUserDateTimeZone(Integer schedulerUserId, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        return (DateTimeZone) user.getValue("dateTimeZone");
    }

    public static String getExternalCalendarUser(Integer userId, ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserDTO userDTO = SchedulerAccessRightUtil.i.getExternalCalendarUser(userId, request);
        User sessionUser = RequestUtils.getUser(request);

        if (null != userDTO) {
            String userName = (String) userDTO.get("userName");
            DateTimeZone dateTimeZone = null;
            if (null != userDTO.get("timeZone")) {
                dateTimeZone = DateTimeZone.forID((String) userDTO.get("timeZone"));
            }

            if (null != dateTimeZone && !dateTimeZone.equals(sessionUser.getValue("dateTimeZone"))) {
                userName += " (" + dateTimeZone.getID() + ")";
            }

            return userName;
        }

        return "";
    }

    public static List<LabelValueBean> getSingleAppointmentFilters(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return Arrays.asList(
                new LabelValueBean(JSPHelper.getMessage(request, SchedulerConstants.SingleAppointmentFilter.PENDING.getResourceKey()), SchedulerConstants.SingleAppointmentFilter.PENDING.getCode().toString()),
                new LabelValueBean(JSPHelper.getMessage(request, SchedulerConstants.SingleAppointmentFilter.ALL.getResourceKey()), SchedulerConstants.SingleAppointmentFilter.ALL.getCode().toString())
        );
    }

    public static Integer getDefaultSingleAppointmentFilter() {
        return SchedulerConstants.SingleAppointmentFilter.PENDING.getCode();
    }

    public static Long registerUserToCheckAccessRights(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return SchedulerAccessRightListChecker.i.registerUserToCheckAccessRights(request);
    }

    public static boolean isAddAppointmentPermissionEnabled(ServletRequest servletRequest, Long code) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return SchedulerAccessRightListChecker.i.isAddAppointmentPermissionEnabled(request, code);
    }

    public static Map<String, Byte> getSchedulerUserPermissions(Integer appointmentUserId,
                                                                ServletRequest servletRequest,
                                                                Long code) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return SchedulerAccessRightListChecker.i.getPermissions(appointmentUserId, request, code);
    }

    public static void clearSchedulerUserPermissions(ServletRequest servletRequest, Long code) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        SchedulerAccessRightListChecker.i.clearCache(request, code);
    }

    public static boolean hasPublicPermission(Integer ownerUserId, Integer userId) {
        Map<String, Byte> permissionMap = getUserPermissions(ownerUserId, userId);
        return (permissionMap.get("public") != null && permissionMap.get("public").byteValue() != (byte) 0);
    }

    public static Byte getPublicPermission(Integer ownerUserId, Integer userId) {
        Map<String, Byte> permissionMap = getUserPermissions(ownerUserId, userId);
        return permissionMap.get("public");
    }

    public static boolean hasPrivatePermission(Integer ownerUserId, Integer userId) {
        Map<String, Byte> permissionMap = getUserPermissions(ownerUserId, userId);
        return (permissionMap.get("private") != null && permissionMap.get("private").byteValue() != (byte) 0);
    }

    public static Byte getPrivatePermission(Integer ownerUserId, Integer userId) {
        Map<String, Byte> permissionMap = getUserPermissions(ownerUserId, userId);
        return permissionMap.get("private");
    }

    /**
     * Verify if exist at least one ADD grant access permission
     *
     * @param publicPermission
     * @param privatePermission
     * @return true or false
     */
    public static boolean hasAddAppointmentPermission(Byte publicPermission, Byte privatePermission) {
        return (SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.ADD)
                || SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.ADD));
    }

    public static boolean hasAddAppointmentPermission(ServletRequest servletRequest) {
        log.debug("Executing Function hasAddAppointmentPermission...");
        boolean addPermission = true;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer schedulerUserId = null;
        if (user.getValue("schedulerUserId") != null) {
            schedulerUserId = (Integer) user.getValue("schedulerUserId");
        }

        //only process permission if user view calendar of other user
        if (schedulerUserId != null && !userId.equals(schedulerUserId)) {
            Map<String, Byte> permissionMap = getUserPermissions(schedulerUserId, userId);
            Byte publicAppPermissions = permissionMap.get("public");
            Byte privateAppPermissions = permissionMap.get("private");

            addPermission = hasAddAppointmentPermission(publicAppPermissions, privateAppPermissions);
        }
        log.debug("ADD permission:" + addPermission);
        return addPermission;
    }

    public static boolean hasAddAppointmentPermission(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean addPermission = false;
        if (isPublicAppointment) {
            addPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.ADD);
        } else {
            addPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.ADD);
        }
        return addPermission;
    }

    public static boolean hasEditAppointmentPermission(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean editPermission = false;
        if (isPublicAppointment) {
            editPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.EDIT);
        } else {
            editPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.EDIT);
        }
        return editPermission;
    }

    public static boolean hasUserEditAppointmentPermission(Integer ownerUserId, Integer userId, boolean isPublicAppointment) {
        boolean editPermission = false;

        if (ownerUserId != null && userId != null) {

            if (!ownerUserId.equals(userId)) {
                Map<String, Byte> permissionMap = getUserPermissions(ownerUserId, userId);
                Byte publicAppPermission = permissionMap.get("public");
                Byte privateAppPermission = permissionMap.get("private");

                editPermission = hasEditAppointmentPermission(publicAppPermission, privateAppPermission, isPublicAppointment);
            } else {
                //this should be the same user
                editPermission = true;
            }
        }
        return editPermission;
    }

    public static boolean hasEditAppointmentPermission(Integer ownerUserId, Integer userId, Integer appointmentId) {

        boolean editPermission = false;
        if (ownerUserId != null && !ownerUserId.equals(userId)) {
            //read appointment
            if (appointmentId != null) {
                LightlyAppointmentReadCmd cmd = new LightlyAppointmentReadCmd();
                cmd.putParam("appointmentId", appointmentId);

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
                    if (!resultDTO.isFailure()) {
                        boolean isPrivate = ((Boolean) resultDTO.get("isPrivate")).booleanValue();
                        log.debug("is private:" + isPrivate);

                        editPermission = hasUserEditAppointmentPermission(ownerUserId, userId, !isPrivate);
                    }
                } catch (AppLevelException e) {
                    log.debug("Error in execute cmd..", e);
                }
            }
        } else {
            //this should be the same user
            editPermission = true;
        }
        return editPermission;
    }

    public static boolean hasDelAppointmentPermission(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean delPermission = false;
        if (isPublicAppointment) {
            delPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.DELETE);
        } else {
            delPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.DELETE);
        }
        return delPermission;
    }

    public static boolean hasReadAppointmentPermission(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean readPermission = false;
        if (isPublicAppointment) {
            readPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.READ);
        } else {
            readPermission = SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.READ);
        }
        return readPermission;
    }

    public static boolean hasOnlyAnonymousAppPermission(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean onlyAnonymous = false;
        if (isPublicAppointment) {
            onlyAnonymous = SchedulerPermissionUtil.hasOnlyAnonymousPermission(publicPermission);
        } else {
            onlyAnonymous = SchedulerPermissionUtil.hasOnlyAnonymousPermission(privatePermission);
        }
        return onlyAnonymous;
    }

    public static boolean hasOnlyAnonymousAppPermissionPublicAndPrivate(Byte publicPermission, Byte privatePermission) {
        return (SchedulerPermissionUtil.hasOnlyAnonymousPermission(publicPermission) && SchedulerPermissionUtil.hasOnlyAnonymousPermission(privatePermission));
    }

    public static boolean hasPermissions(Byte publicPermission, Byte privatePermission, boolean isPublicAppointment) {
        boolean hasPermission = false;
        if (isPublicAppointment) {
            hasPermission = SchedulerPermissionUtil.hasPermissions(publicPermission);
        } else {
            hasPermission = SchedulerPermissionUtil.hasPermissions(privatePermission);
        }
        return hasPermission;
    }

    /**
     * when user add appointment, this can be: all, only public, only private appointment
     *
     * @param publicPermission
     * @param privatePermission
     * @param servletRequest
     * @return String
     */
    public static String getProcessPermitedInAppoimtment(Byte publicPermission, Byte privatePermission, String op, ServletRequest servletRequest) {
        String processPermited = SchedulerConstants.PROCESS_ALL_APP;
        int checkPermission;

        if ("create".equals(op)) {
            checkPermission = SchedulerPermissionUtil.ADD;
        } else if ("update".equals(op)) {
            checkPermission = SchedulerPermissionUtil.EDIT;
        } else {
            return processPermited;
        }
        log.debug("CHECK PERMISSION:" + checkPermission);

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue(Constants.USERID);
        Integer schedulerUserId = null;
        if (user.getValue("schedulerUserId") != null) {
            schedulerUserId = (Integer) user.getValue("schedulerUserId");
        }

        //only process permission if user view calendar of other user
        if (schedulerUserId != null && !userId.equals(schedulerUserId)) {
            if (SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, checkPermission)
                    && SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, checkPermission)) {
                processPermited = SchedulerConstants.PROCESS_ALL_APP;
            } else if (SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, checkPermission)) {
                processPermited = SchedulerConstants.PROCESS_ONLYPRIVATE_APP;
            } else if (SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, checkPermission)) {
                processPermited = SchedulerConstants.PROCESS_ONLYPUBLIC_APP;
            }
        }
        log.debug("PROCESS PERMITED:" + processPermited);

        return processPermited;
    }

    public static boolean isEqual(Integer a, String b) {
        log.debug("Integer:" + a + " - String:" + b);

        return b.equals(a.toString());

    }

    public static boolean stringDateIsBefore(Object date, Long limit) {

        try {
            Long dateLong = new Long((String) date);
            return dateLong.longValue() < limit.longValue();
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * get output view type for overview calendar of other user
     *
     * @param servletRequest
     * @return List
     */
    public static List getOverviewCalendarOutputTypes(ServletRequest servletRequest) {
        List list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.overviewCalendar.day"), String.valueOf(AbstractAppointmentUIAction.DAILY_VIEW_TYPE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.overviewCalendar.week"), String.valueOf(AbstractAppointmentUIAction.WEEKLY_VIEW_TYPE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.overviewCalendar.month"), String.valueOf(AbstractAppointmentUIAction.MONTHLY_VIEW_TYPE)));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Scheduler.overviewCalendar.year"), String.valueOf(AbstractAppointmentUIAction.YEARLY_VIEW_TYPE)));

        return list;
    }

    /**
     * get all users that with permission to view calendar
     *
     * @param servletRequest
     * @return list
     */
    public static List getOverviewCalendarUsers(ServletRequest servletRequest) {
        List list = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue(Constants.USERID);

        list = getUserViewCalendar(servletRequest);
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            if (labelValueBean.getValue().equals(SchedulerConstants.OVERVIEW_SHAREDCALENDARS)) {
                //remove of list
                iterator.remove();
            }
        }
        return list;
    }

    /**
     * get elwis user account login
     *
     * @param userId
     * @return String
     */
    public static String getUserLogin(Integer userId) {
        String userLogin = "";
        UserInfoCmd userInfoCmd = new UserInfoCmd();
        userInfoCmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userInfoCmd, null);
            if (!resultDTO.isFailure()) {
                userLogin = (String) resultDTO.get("userLogin");
            }
        } catch (AppLevelException e) {
            log.error("Error executing cmd", e);
        }
        return userLogin;
    }

    public static String getUserTaskStatusLabel(String locale, String status) {
        String res = "";
        if (status != null) {
            if (status.equals(SchedulerConstants.NOTSTARTED)) {
                res = JSPHelper.getMessage(new Locale(locale), "Task.notInit");
            } else if (status.equals(SchedulerConstants.DEFERRED)) {
                res = JSPHelper.getMessage(new Locale(locale), "Task.Deferred");
            } else if (status.equals(SchedulerConstants.INPROGRESS)) {
                res = JSPHelper.getMessage(new Locale(locale), "Task.InProgress");
            } else if (status.equals(SchedulerConstants.CHECK)) {
                res = JSPHelper.getMessage(new Locale(locale), "Task.ToCheck");
            } else if (status.equals(SchedulerConstants.CONCLUDED)) {
                res = JSPHelper.getMessage(new Locale(locale), "Scheduler.Task.Concluded");
            }
        }
        return res;
    }

    public static List<LabelValueBean> getTaskNavigationOptions(ServletRequest servletRequest,
                                                                DefaultForm defaultForm) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        if (com.piramide.elwis.web.common.el.Functions.hasAccessRight(request, "TASK", "CREATE")) {
            result.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.option.newTask"), "1"));
        }

        if (null != defaultForm.getDto("processId") && !"".equals(defaultForm.getDto("processId").toString().trim())) {
            if (com.piramide.elwis.web.common.el.Functions.hasAccessRight(request, "SALESPROCESSACTION", "CREATE")) {
                result.add(new LabelValueBean(JSPHelper.getMessage(request, "Task.option.newAction"), "2"));
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

    public static void initializeSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        user.setValue("schedulerUserId", user.getValue(Constants.USERID));
    }

}
