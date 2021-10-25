package com.piramide.elwis.web.schedulermanager.el;

import com.piramide.elwis.cmd.schedulermanager.SchedulerAccessRightCmd;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.schedulermanager.SchedulerAccessDTO;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.3.7
 */
public class SchedulerAccessRightUtil {
    private Log log = LogFactory.getLog(this.getClass());

    public static SchedulerAccessRightUtil i = new SchedulerAccessRightUtil();

    private SchedulerAccessRightUtil() {

    }

    public Map<String, Byte> getPermissions(Integer appointmentUserId,
                                            Integer sessionUserId,
                                            List<SchedulerAccessDTO> accesses) {
        Map<String, Byte> result = new HashMap<String, Byte>();

        if (!isTheAppointmentOwner(appointmentUserId, sessionUserId)) {
            for (SchedulerAccessDTO dto : accesses) {
                Integer ownerUserId = (Integer) dto.get("ownerUserId");
                if (appointmentUserId.equals(ownerUserId)) {
                    result.put("public", (Byte) dto.get("permission"));
                    result.put("private", (Byte) dto.get("privatePermission"));
                }
            }
        } else {
            Integer ownerPermission = SchedulerPermissionUtil.READ
                    + SchedulerPermissionUtil.ADD
                    + SchedulerPermissionUtil.EDIT
                    + SchedulerPermissionUtil.DELETE;
            result.put("public", Byte.valueOf(String.valueOf(ownerPermission)));
            result.put("private", Byte.valueOf(String.valueOf(ownerPermission)));
        }

        if (result.isEmpty()) {
            log.debug("Use anonymous permission because the user: "
                    + sessionUserId
                    + " have not enabled permissions by appointment user: " + appointmentUserId);
            result.put("public", Byte.valueOf(String.valueOf(SchedulerPermissionUtil.ANONYM)));
            result.put("private", Byte.valueOf(String.valueOf(SchedulerPermissionUtil.ANONYM)));
        }

        return result;
    }

    @SuppressWarnings(value = "unchecked")
    public List<UserDTO> getExternalCalendarUsers(Integer userId, HttpServletRequest request) {
        SchedulerAccessRightCmd cmd = new SchedulerAccessRightCmd();
        cmd.setOp("getExternalCalendarUsers");
        cmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (List<UserDTO>) resultDTO.get("getExternalCalendarUsers");
        } catch (AppLevelException e) {
            return new ArrayList<UserDTO>();
        }
    }

    public UserDTO getExternalCalendarUser(Integer userId, HttpServletRequest request) {
        SchedulerAccessRightCmd cmd = new SchedulerAccessRightCmd();
        cmd.setOp("getExternalCalendarUser");
        cmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (UserDTO) resultDTO.get("getExternalCalendarUser");
        } catch (AppLevelException e) {
            return null;
        }
    }

    @SuppressWarnings(value = "unchecked")
    public List<SchedulerAccessDTO> getSchedulerAccesses(Integer userId, HttpServletRequest request) {
        SchedulerAccessRightCmd cmd = new SchedulerAccessRightCmd();
        cmd.setOp("getAccessRights");
        cmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (List<SchedulerAccessDTO>) resultDTO.get("getAccessRights");
        } catch (AppLevelException e) {
            return new ArrayList<SchedulerAccessDTO>();
        }
    }

    private boolean isTheAppointmentOwner(Integer appointmentUserId, Integer sessionUserId) {
        return appointmentUserId.equals(sessionUserId);
    }
}
