package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.dto.schedulermanager.AppointmentView;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.SessionContext;
import java.util.*;


/**
 * Jatun S.R.L.
 * Cmd to read all appointments to overview calendar of other users
 *
 * @author Miky
 * @version $Id: LoadOverviewAppointmentsCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LoadOverviewAppointmentsCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    private Map appointmentsByRange;
    private Map appointmentList;
    private int[][] appointments;
    private boolean isYeralyView;

    public boolean isStateful() {
        return false;
    }

    public LoadOverviewAppointmentsCmd(boolean isYeralyView) {
        this.isYeralyView = isYeralyView;
        appointmentsByRange = new HashMap();
        appointmentList = new HashMap();
        if (isYeralyView) {
            appointments = new int[31][12];
        }
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LoadOverviewAppointmentsCmd........" + paramDTO);

        List<String> overviewUserIdList = (List) paramDTO.get("overviewUserIdList");
        Integer currentUserId = new Integer(paramDTO.get("userId").toString());
        Integer appointmentTypeId = (paramDTO.get("appointmentTypeId") != null) ? new Integer(paramDTO.get("appointmentTypeId").toString()) : null;

        for (int i = 0; i < overviewUserIdList.size(); i++) {
            Integer schedulerUserId = new Integer(overviewUserIdList.get(i));

            String viewAppointment = getUserViewAppointment(schedulerUserId, currentUserId, ctx);
            if (viewAppointment != null) {

                UserInfoCmd userInfoCmd = new UserInfoCmd();
                userInfoCmd.putParam("userId", schedulerUserId);
                userInfoCmd.execute(ctx);

                if (!userInfoCmd.getResultDTO().isFailure()) {
                    String schedulerUserName = (String) userInfoCmd.getResultDTO().get("userName");
                    log.debug("Processing user....: " + schedulerUserName);

                    DateTimeZone timeZone;
                    String userTimeZone = (String) userInfoCmd.getResultDTO().get("timeZone");
                    if (userTimeZone != null) {
                        timeZone = DateTimeZone.forID(userTimeZone);
                    } else {
                        timeZone = DateTimeZone.getDefault();
                    }

                    LoadAppointmentsCmd loadAppointmentsCmd = new LoadAppointmentsCmd(isYeralyView);
                    if (isYeralyView) {
                        loadAppointmentsCmd.initialize(true);
                    }

                    //set previous appointment data only if already processed
                    if (i > 0) {
                        loadAppointmentsCmd.setAppointments(appointments);
                    }

                    loadAppointmentsCmd.putParam("startRangeDate", defineNewRangeDate((DateTime) paramDTO.get("startRangeDate"), timeZone));
                    loadAppointmentsCmd.putParam("endRangeDate", defineNewRangeDate((DateTime) paramDTO.get("endRangeDate"), timeZone));
                    loadAppointmentsCmd.putParam("userId", schedulerUserId);
                    loadAppointmentsCmd.putParam("timeZone", timeZone);
                    loadAppointmentsCmd.putParam("viewAppointment", viewAppointment);
                    if (appointmentTypeId != null) {
                        loadAppointmentsCmd.putParam("appointmentTypeId", appointmentTypeId);
                    }

                    loadAppointmentsCmd.executeInStateless(ctx);

                    //get load app data
                    appointments = loadAppointmentsCmd.getAppointments();

                    addInAppointmentViewList(loadAppointmentsCmd.getAppointmentList(), schedulerUserId, schedulerUserName, loadAppointmentsCmd.getAppointmentsByRange());
                }
            }
        }
    }

    /**
     * get user view appointment to process this, return null if no longer permissions
     *
     * @param ownerUserId
     * @param userId
     * @param ctx
     * @return String
     */
    private String getUserViewAppointment(Integer ownerUserId, Integer userId, SessionContext ctx) {
        String viewAppointment = null;

        SchedulerPermissionCmd cmd = new SchedulerPermissionCmd();
        cmd.putParam("op", "getUserPermission");
        cmd.putParam(SchedulerPermissionCmd.OWNER_USERID, ownerUserId);
        cmd.putParam(SchedulerPermissionCmd.USERID, userId);

        cmd.executeInStateless(ctx);

        Byte publicAppPermission = (Byte) cmd.getResultDTO().get("permissionPublic");
        Byte privateAppPermission = (Byte) cmd.getResultDTO().get("permissionPrivate");

        if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)
                && SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
            viewAppointment = SchedulerConstants.PROCESS_ALL_APP;
        } else if (SchedulerPermissionUtil.hasPermissions(publicAppPermission)) {
            viewAppointment = SchedulerConstants.PROCESS_ONLYPUBLIC_APP;
        } else if (SchedulerPermissionUtil.hasPermissions(privateAppPermission)) {
            viewAppointment = SchedulerConstants.PROCESS_ONLYPRIVATE_APP;
        }

        return viewAppointment;
    }

    /**
     * define user name and Add appointment view in list
     *
     * @param appointmentViewMap
     * @param ownerUserName
     * @param appointmentsByRangeMap
     * @param userId
     */
    private void addInAppointmentViewList(Map appointmentViewMap, Integer userId, String ownerUserName, Map<Integer, List<Map<String, Object>>> appointmentsByRangeMap) {

        if (appointmentViewMap != null) {
            for (Iterator iterator = appointmentViewMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                AppointmentView appointmentView = (AppointmentView) appointmentViewMap.get(key);
                appointmentView.setOwnerUserName(ownerUserName);
                appointmentView.setUserId(userId);

                if (appointmentList.containsKey(key)) {
                    //appointments with participants contains the same id, then we define new virtual id
                    //to appoinment view and replace this id inside appoinments range date map
                    appointmentView.setVirtualId(System.currentTimeMillis());

                    for (Iterator<Integer> iterator1 = appointmentsByRangeMap.keySet().iterator(); iterator1.hasNext();) {
                        Integer dateKey = iterator1.next();
                        List<Map<String, Object>> rangeDateList = appointmentsByRangeMap.get(dateKey);
                        boolean isFound = false;
                        Object mapValue = null;
                        for (Iterator<Map<String, Object>> iterator2 = rangeDateList.iterator(); iterator2.hasNext();) {
                            Map<String, Object> map = iterator2.next();
                            if (map.containsKey(key)) {
                                mapValue = map.get(key);
                                iterator2.remove();
                                isFound = true;
                                break;
                            }
                        }
                        if (isFound) {
                            rangeDateList.add(Collections.singletonMap(appointmentView.getId(), mapValue));
                            break;
                        }
                    }

                    appointmentList.put(appointmentView.getId(), appointmentView);
                } else {
                    appointmentList.put(key, appointmentView);
                }
            }

            //add data in map by range Date
            for (Integer dateKey : appointmentsByRangeMap.keySet()) {
                if (appointmentsByRange.containsKey(dateKey)) {
                    List appInDateList = (List) appointmentsByRange.get(dateKey);
                    appInDateList.addAll(appointmentsByRangeMap.get(dateKey));
                } else {
                    appointmentsByRange.put(dateKey, appointmentsByRangeMap.get(dateKey));
                }
            }
        }
    }

    /**
     * Returns a copy of this datetime with a different time zone, preserving the field values
     *
     * @param dateTime
     * @param dateTimeZone
     * @return DateTime
     */
    private DateTime defineNewRangeDate(DateTime dateTime, DateTimeZone dateTimeZone) {
        DateTime newDateTime = dateTime.withZoneRetainFields(dateTimeZone);
        return newDateTime;
    }


    public Map getAppointmentsByRange() {
        return appointmentsByRange;
    }

    public Map getAppointmentList() {
        return appointmentList;
    }

    public int[][] getAppointments() {
        return appointments;
    }
}

