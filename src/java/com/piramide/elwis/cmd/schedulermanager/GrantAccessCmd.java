package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccess;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccessHome;
import com.piramide.elwis.domain.schedulermanager.SchedulerAccessPK;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: GrantAccessCmd.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 05-05-2005 10:49:34 AM ivan Exp $
 */
public class GrantAccessCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing GrantAccessCmd...");

        log.debug("paramDTO... " + paramDTO);

        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        Integer viewUserId = Integer.valueOf(paramDTO.get("viewUserId").toString());


        if ("create".equals(this.getOp()) && !isAlreadyRegistred(userId, viewUserId)) {
            create(userId, viewUserId);
        }

        if ("delete".equals(this.getOp())) {
            delete(userId, viewUserId);
        }

        if ("update".equals(this.getOp())) {
            update(userId, viewUserId);
        }

        if ("".equals(this.getOp())) {
            read(userId, viewUserId);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void create(Integer userId, Integer viewUserId) {
        log.debug("Executing create method...");


        // find userId
        UserDTO userDTO = new UserDTO();
        userDTO.put("userId", viewUserId);
        userDTO.put("employeeName", paramDTO.get("viewUserName"));
        User elwisUser = (User) ExtendedCRUDDirector.i.read(userDTO, resultDTO, true);


        if (!resultDTO.isFailure()) {

            SchedulerAccessHome home = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);
            int permissions = checkSchedulerPermissions();
            Byte result = Byte.valueOf(String.valueOf(permissions));

            int privatePermissions = checkSchedulerPrivatePermissions();
            Byte privatePermissionAsByte = Byte.valueOf(String.valueOf(privatePermissions));
            try {
                SchedulerAccess access = home.create(userId, viewUserId, elwisUser.getCompanyId());
                access.setPermission(result);
                access.setPrivatePermission(privatePermissionAsByte);
            } catch (CreateException e) {
                log.debug("Cannot create entity...");
            }
        }

    }

    private void read(Integer userId, Integer viewUserId) {
        log.debug("Executing read mehotd...");

        //construct SchedulerAccessPK for schedulerAccess
        SchedulerAccessPK pk = new SchedulerAccessPK();
        pk.ownerUserId = userId;
        pk.userId = viewUserId;

        // find userId
        UserDTO userDTO = new UserDTO();
        userDTO.put("userId", viewUserId);
        userDTO.put("employeeName", paramDTO.get("viewUserName"));
        ExtendedCRUDDirector.i.read(userDTO, resultDTO, true);


        if (!resultDTO.isFailure()) {
            log.debug("The userId ..: " + viewUserId + " was found...");

            SchedulerAccessHome home = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);
            try {

                SchedulerAccess access = home.findByPrimaryKey(pk);
                setPermissionsInResultDTO(access.getPermission(), false);
                setPermissionsInResultDTO(access.getPrivatePermission(), true);

            } catch (FinderException e) {
                log.error("Cannot find scheduler access for ownerUserId = " + userId + " and userId = " + userId);
            }
        }
    }

    /**
     * set in resultDTO allowed permission
     *
     * @param permission
     * @param isPrivatePermission
     */
    private void setPermissionsInResultDTO(Byte permission, boolean isPrivatePermission) {
        if (permission != null) {
            List permissionList = SchedulerPermissionUtil.checkPermissionAllowed(permission);
            log.debug("Read all permissions... " + permissionList);

            for (int i = 0; i < permissionList.size(); i++) {
                Map map = (Map) permissionList.get(i);

                if (map.get("blank") == null) {

                    String strPermission = map.get("stringValue").toString();
                    String dtoKey = null;
                    if (SchedulerPermissionUtil.PERMISSION_READ.equals(strPermission)) {
                        dtoKey = (isPrivatePermission ? "privateRead" : "read");
                    } else if (SchedulerPermissionUtil.PERMISSION_ADD.equals(strPermission)) {
                        dtoKey = (isPrivatePermission ? "privateAdd" : "add");
                    } else if (SchedulerPermissionUtil.PERMISSION_EDIT.equals(strPermission)) {
                        dtoKey = (isPrivatePermission ? "privateEdit" : "edit");
                    } else if (SchedulerPermissionUtil.PERMISSION_DELETE.equals(strPermission)) {
                        dtoKey = (isPrivatePermission ? "privateDelete" : "delete");
                    } else if (SchedulerPermissionUtil.PERMISSION_ANONYM.equals(strPermission)) {
                        dtoKey = (isPrivatePermission ? "privateAnonym" : "anonym");
                    }

                    if (dtoKey != null) {
                        if ("true".equals(map.get("isChecked").toString())) {
                            resultDTO.put(dtoKey, "true");
                        } else {
                            resultDTO.put(dtoKey, "false");
                        }
                    }
                }
            }
        }
    }

    /**
     * check the public appointment scheduler permission
     *
     * @return permission as int
     */
    private int checkSchedulerPermissions() {

        int read = (paramDTO.get("read") != null ? SchedulerPermissionUtil.READ : 0);
        int add = (paramDTO.get("add") != null ? SchedulerPermissionUtil.ADD : 0);
        int edit = (paramDTO.get("edit") != null ? SchedulerPermissionUtil.EDIT : 0);
        int delete = (paramDTO.get("delete") != null ? SchedulerPermissionUtil.DELETE : 0);
        int anonym = (paramDTO.get("anonym") != null ? SchedulerPermissionUtil.ANONYM : 0);

        int sum = read + add + edit + delete + anonym;
        return sum;
    }

    /**
     * check the private appointment scheduler permission
     *
     * @return permission as int
     */
    private int checkSchedulerPrivatePermissions() {

        int read = (paramDTO.get("privateRead") != null ? SchedulerPermissionUtil.READ : 0);
        int add = (paramDTO.get("privateAdd") != null ? SchedulerPermissionUtil.ADD : 0);
        int edit = (paramDTO.get("privateEdit") != null ? SchedulerPermissionUtil.EDIT : 0);
        int delete = (paramDTO.get("privateDelete") != null ? SchedulerPermissionUtil.DELETE : 0);
        int anonym = (paramDTO.get("privateAnonym") != null ? SchedulerPermissionUtil.ANONYM : 0);

        int sum = read + add + edit + delete + anonym;
        return sum;
    }

    private void delete(Integer userId, Integer viewUserId) {
        log.debug("Executing delete method...");
        SchedulerAccessPK pk = new SchedulerAccessPK();
        pk.ownerUserId = userId;
        pk.userId = viewUserId;

        SchedulerAccessHome home = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);

        try {
            SchedulerAccess access = home.findByPrimaryKey(pk);
            access.remove();
            resultDTO.setForward("Success");
        } catch (FinderException e) {
            log.error("Cannot find schedulerAccess with ownerUserId = " + userId + " and userId = " + userId + " " + e);
        } catch (RemoveException e) {
            log.error("Cannot remove schedulerAccess with ownerUserId = " + userId + " and userId = " + userId + " " + e);
        }
    }

    private void update(Integer userId, Integer viewUserId) {
        log.debug("Executing update method...");

        int permissions = checkSchedulerPermissions();
        Byte result = Byte.valueOf(String.valueOf(permissions));

        int privatePermissions = checkSchedulerPrivatePermissions();
        Byte privatePermissionAsByte = Byte.valueOf(String.valueOf(privatePermissions));

        SchedulerAccessPK pk = new SchedulerAccessPK();
        pk.ownerUserId = userId;
        pk.userId = viewUserId;

        SchedulerAccessHome home = (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);
        try {
            SchedulerAccess access = home.findByPrimaryKey(pk);

            log.debug("Update scheduler access... ");
            access.setPermission(result);
            access.setPrivatePermission(privatePermissionAsByte);

            resultDTO.setForward("Success");
        } catch (FinderException e) {
            log.error("Cannot find schedulerAccess with ownerUserId = " + userId + " and userId = " + userId + " " + e);
        }
    }

    private boolean isAlreadyRegistred(Integer userId, Integer viewUserId) {
        log.debug("Check if the user already exists...");

        SchedulerAccessPK pk = new SchedulerAccessPK();
        pk.ownerUserId = userId;
        pk.userId = viewUserId;
        boolean exists = false;
        SchedulerAccessHome home =
                (SchedulerAccessHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULERACCESS);

        try {
            home.findByPrimaryKey(pk);

            exists = true;
            log.debug("User cannot register already exists");
            resultDTO.addResultMessage("msg.Duplicated", paramDTO.get("viewUserName"));
            resultDTO.setForward("Exists");

        } catch (FinderException e) {

            exists = false;
        }
        return exists;
    }
}
