package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccess;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccessHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.schedulermanager.SchedulerAccessDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SchedulerAccessRightCmd extends EJBCommand {
    @Override
    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        if ("getAccessRights".equals(getOp())) {
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            getAccessRights(userId);
        }

        if ("getExternalCalendarUser".equals(getOp())) {
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            getExternalCalendarUser(userId, ctx);
        }
        if ("getExternalCalendarUsers".equals(getOp())) {
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            getExternalCalendarUsers(userId, ctx);
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }

    @SuppressWarnings(value = "unchecked")
    private UserDTO getExternalCalendarUser(Integer userId, SessionContext ctx) {
        UserDTO userDTO = getUser(userId, ctx);
        resultDTO.put("getExternalCalendarUser", userDTO);

        return userDTO;
    }

    @SuppressWarnings(value = "unchecked")
    private List<UserDTO> getExternalCalendarUsers(Integer userId, SessionContext ctx) {
        List<UserDTO> calendarUsers = new ArrayList<UserDTO>();

        for (SchedulerAccess access : getAccesses(userId)) {
            if (SchedulerPermissionUtil.havePermission(access.getPermission(), SchedulerPermissionUtil.PERMISSION_ADD)
                    || SchedulerPermissionUtil.havePermission(access.getPrivatePermission(), SchedulerPermissionUtil.PERMISSION_ADD)) {
                UserDTO userDTO = getUser(access.getOwnerUserId(), ctx);
                if (null != userDTO) {
                    calendarUsers.add(userDTO);
                }
            }
        }

        resultDTO.put("getExternalCalendarUsers", calendarUsers);
        return calendarUsers;
    }

    @SuppressWarnings(value = "unchecked")
    private List<SchedulerAccessDTO> getAccessRights(Integer userId) {
        List<SchedulerAccessDTO> dtoList = new ArrayList<SchedulerAccessDTO>();

        for (SchedulerAccess access : getAccesses(userId)) {
            SchedulerAccessDTO dto = new SchedulerAccessDTO();
            DTOFactory.i.copyToDTO(access, dto);
            dtoList.add(dto);
        }

        resultDTO.put("getAccessRights", dtoList);
        return dtoList;
    }

    @SuppressWarnings(value = "unchecked")
    private List<SchedulerAccess> getAccesses(Integer userId) {
        SchedulerAccessHome schedulerAccessHome =
                (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);

        try {
            return (List<SchedulerAccess>) schedulerAccessHome.findUsersViewCalendar(userId);
        } catch (FinderException e) {
            return new ArrayList<SchedulerAccess>();
        }
    }

    @SuppressWarnings(value = "unchecked")
    private UserDTO getUser(Integer userId, SessionContext ctx) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey(userId);

            LightlyAddressCmd cmd = new LightlyAddressCmd();
            cmd.putParam("addressId", user.getAddressId());
            cmd.executeInStateless(ctx);
            String userName = (String) cmd.getResultDTO().get("addressName");

            UserDTO userDTO = new UserDTO();
            DTOFactory.i.copyToDTO(user, userDTO);
            userDTO.put("userName", userName);

            return userDTO;
        } catch (FinderException e) {
            return null;
        }
    }
}
