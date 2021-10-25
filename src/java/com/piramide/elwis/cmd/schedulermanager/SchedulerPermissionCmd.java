package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccess;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccessHome;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccessPK;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Alfacentauro Team
 * this class help to management permissions in scheduler
 *
 * @author miky
 * @version $Id: SchedulerPermissionCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class SchedulerPermissionCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    public static final String RESULT = "result";
    public static final String USERID = "userId";
    public static final String OWNER_USERID = "ownerUserId";

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        //log.debug("Executing SchedulerPermissionCmd..............."+paramDTO);

        if ("userViewCalendarList".equals(getOp())) {
            getListUserViewCalendar();
        } else if ("getUserPermission".equals(getOp())) {
            Integer ownerUserId = Integer.valueOf(paramDTO.get(OWNER_USERID).toString());
            Integer userId = Integer.valueOf(paramDTO.get(USERID).toString());
            getCalendarUserPermission(ownerUserId, userId);
        }
    }

    private void getListUserViewCalendar() {

        ArrayList listUser = new ArrayList();
        Collection usersAccess = null;
        Integer userId = Integer.valueOf(paramDTO.get(USERID).toString());

        SchedulerAccessHome schedulerAccessHome = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);
        try {
            usersAccess = schedulerAccessHome.findUsersViewCalendar(userId);
        } catch (FinderException fe) {
            log.debug("not found user......." + fe);
        }

        if (usersAccess != null) {
            for (Iterator iterator = usersAccess.iterator(); iterator.hasNext();) {
                SchedulerAccess schedulerAcces = (SchedulerAccess) iterator.next();
                Integer userViewId = schedulerAcces.getOwnerUserId();
                Byte permissions = schedulerAcces.getPermission();
                Byte privatePermissions = schedulerAcces.getPrivatePermission();

                if (SchedulerPermissionUtil.hasPermissions(permissions)
                        || SchedulerPermissionUtil.hasPermissions(privatePermissions)) {
                    Map userMap = new HashMap();
                    userMap.put("userId", userViewId);
                    listUser.add(userMap);
                }
            }
        }

        resultDTO.put(RESULT, listUser);
    }

    /**
     * get the user name of this user
     *
     * @param userId
     * @return String, user name
     */
    private String getUserName(Integer userId) {
        String addressName = "";
        Address address = (Address) EJBFactory.i.callFinder(new AddressDTO(), "findByPrimaryKey", new Object[]{userId});

        if (address.getAddressType().equals(ContactConstants.ADDRESSTYPE_PERSON)) {

            addressName = address.getName1() + ""
                    + ((address.getName2() != null && !"".equals(address.getName2())) ? ", " + address.getName2() : ""); // name1 is apellido, name2 is name;
        }

        if (address.getAddressType().equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {

            addressName = address.getName1()
                    + " " + ((address.getName2() != null) ? address.getName2() : "")
                    + " " + ((address.getName3() != null) ? address.getName3() : "");
        }
        return addressName;
    }

    /**
     * get the permission of view calendar of an user
     *
     * @param ownerUserId , is the userID of the owner calendar
     * @param userId      , is the userId of the invited
     */
    private void getCalendarUserPermission(Integer ownerUserId, Integer userId) {
//        log.debug("Executing getCalendarUserPermission()............................ ");

        Byte permissionByte = null;
        Byte privatePermissionByte = null;
        SchedulerAccess schedulerAccess = null;

        SchedulerAccessHome schedulerAccessHome = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);
        SchedulerAccessPK schedulerAccessPk = new SchedulerAccessPK();
        schedulerAccessPk.ownerUserId = ownerUserId;
        schedulerAccessPk.userId = userId;

        try {
            schedulerAccess = schedulerAccessHome.findByPrimaryKey(schedulerAccessPk);
            permissionByte = schedulerAccess.getPermission();
            privatePermissionByte = schedulerAccess.getPrivatePermission();
        } catch (FinderException fe) {
            log.debug("not find SchedulerAccess..............." + fe);
            //if is the same (session user), then this is owner and should have all permissions
            if (ownerUserId != null && ownerUserId.equals(userId)) {
                permissionByte = Byte.valueOf(String.valueOf(allOwnerSchedulerPermissions()));
                privatePermissionByte = Byte.valueOf(String.valueOf(allOwnerSchedulerPermissions()));
            }
        }

        resultDTO.put("permissionPublic", permissionByte);
        resultDTO.put("permissionPrivate", privatePermissionByte);

    }

    /**
     * get permission to owner, generally is all permissions allowed
     *
     * @return int
     */
    private int allOwnerSchedulerPermissions() {
        return SchedulerPermissionUtil.READ + SchedulerPermissionUtil.ADD + SchedulerPermissionUtil.EDIT + SchedulerPermissionUtil.DELETE;
    }

}
