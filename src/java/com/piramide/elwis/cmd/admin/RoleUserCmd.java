package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.admin.UserRoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id RoleUserCmd ${time}
 */
public class RoleUserCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO : " + paramDTO);
        String op = this.getOp();
        Integer roleId = Integer.valueOf(paramDTO.get("roleId").toString());

        if ("create".equals(op)) {
            List userKeys = (List) paramDTO.get("userKeys");
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            Collection myUsers = new ArrayList();
            for (int i = 0; i < userKeys.size(); i++) {
                Integer userKey = Integer.valueOf(userKeys.get(i).toString());

                try {
                    User user = userHome.findByPrimaryKey(userKey);
                    myUsers.add(user);
                } catch (FinderException e) {
                }
            }

            if (!myUsers.isEmpty()) {
                RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
                try {
                    Role role = roleHome.findByPrimaryKey(roleId);
                    for (Object element : myUsers) {
                        User user = (User) element;
                        UserRole userRole = createUserRole(role, user);
                        if (userRole != null) {
                            createUserPasswordChange(user.getUserId(), role.getRoleId(), ctx);
                        }
                    }
                } catch (FinderException e) {
                    resultDTO.setForward("Fail");
                }
            }
        }
        if ("".equals(op) || "read".equals(op)) {
            Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
            User user = null;
            try {
                user = userHome.findByPrimaryKey(userId);
                /*if (1 == user.getRoles().size()) {
                    resultDTO.addResultMessage("RoleUser.notRemoveUniqueUser");
                    resultDTO.setForward("Fail");
                    return;
                }*/
            } catch (FinderException e) {
            }

            Role role = null;
            try {
                role = roleHome.findByPrimaryKey(roleId);
            } catch (FinderException e) {
            }

            if (user.getIsDefaultUser().booleanValue() && role.getIsDefault().booleanValue()) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("RoleUser.notEmptyToDefaultUser");
                return;
            }
            if (!useRole(user.getUserId(), role.getRoleId())) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("generalMsg.NotFound");
                return;
            }

            LightlyAddressCmd cmd = new LightlyAddressCmd();
            cmd.putParam("addressId", user.getAddressId());
            cmd.executeInStateless(ctx);

            resultDTO.putAll(cmd.getResultDTO());
            if (cmd.getResultDTO().isFailure() || "Fail".equals(cmd.getResultDTO().getForward())) {
                resultDTO.setForward("Fail");
            }
            resultDTO.put("roleId", roleId);
        }

        if ("delete".equals(op)) {
            delete(roleId, ctx);
        }

        if ("checkSelected".equals(op)) {
            RoleHome roleHome =
                    (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
            List userIds = new ArrayList();
            try {
                Role role = roleHome.findByPrimaryKey(roleId);
                Collection myUsers = readUserByRole(role);
                for (Iterator iterator = myUsers.iterator(); iterator.hasNext();) {
                    UserRole user = (UserRole) iterator.next();
                    userIds.add(user.getUserId().toString());
                }
            } catch (FinderException e) {
            }
            resultDTO.put("usersByRole", userIds);
        }

        if ("readAllUserInformation".equals(op)) {
            List userIds = (List) paramDTO.get("users");
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            List mapStructureList = new ArrayList();
            for (int i = 0; i < userIds.size(); i++) {
                Integer userId = Integer.valueOf(userIds.get(i).toString());

                try {
                    User user = userHome.findByPrimaryKey(userId);

                    LightlyAddressCmd cmd = new LightlyAddressCmd();
                    cmd.putParam("addressId", user.getAddressId());
                    cmd.executeInStateless(ctx);
                    ResultDTO myResultDTO = new ResultDTO();
                    myResultDTO = cmd.getResultDTO();

                    Map userMap = new HashMap();
                    userMap.put("userName", myResultDTO.get("addressName"));
                    userMap.put("userId", user.getUserId());
                    mapStructureList.add(userMap);
                } catch (FinderException e) {
                }
            }
            resultDTO.put("roleId", roleId);
            resultDTO.put("userNameList", mapStructureList);
        }

        if ("deleteAll".equals(op)) {
            RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);

            Role role = null;
            try {
                role = roleHome.findByPrimaryKey(roleId);
            } catch (FinderException e) {
                resultDTO.setForward("Fail");
                return;
            }

            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            Integer userListSize = Integer.valueOf(paramDTO.get("userListSize").toString());

            List cantNotDeletedNames = new ArrayList();

            for (int i = 1; i <= userListSize.intValue(); i++) {
                Integer userKey = Integer.valueOf(paramDTO.get("userId_" + i).toString());

                try {
                    User user = userHome.findByPrimaryKey(userKey);
                    /*if (user.getRoles().size() > 1) {*/


                    if (role.getIsDefault().booleanValue() && user.getIsDefaultUser().booleanValue()) {
                        resultDTO.addResultMessage("RoleUser.notEmptyToDefaultUser");
                    }

                    if (!(role.getIsDefault() && user.getIsDefaultUser())) {
                        boolean success = deleteRoleForUser(user.getUserId(), role.getRoleId());
                        if (success) {
                            deleteUserPasswordChange(user.getUserId(), role.getRoleId(), ctx);
                        }
                    }

                } catch (FinderException e) {
                }
            }


            if (!cantNotDeletedNames.isEmpty()) {
                String notDeletedNames = "";
                for (int i = 0; i < cantNotDeletedNames.size(); i++) {
                    if (i == cantNotDeletedNames.size() - 1) {
                        notDeletedNames += cantNotDeletedNames.get(i);
                    } else {
                        notDeletedNames += cantNotDeletedNames.get(i) + "; ";
                    }
                }
                if (!"".equals(notDeletedNames)) {
                    resultDTO.addResultMessage("RoleUser.notRemoveUniqueUsers", notDeletedNames);
                }

            }


        }
    }


    private void delete(Integer roleId, SessionContext ctx) {
        log.debug("delete('" + roleId + "', javax.ejb.SessionContext)");

        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User user = null;
        try {
            user = userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            resultDTO.setForward("Fail");
            return;
        }

        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        try {
            Role role = roleHome.findByPrimaryKey(roleId);

            boolean success = deleteRoleForUser(user.getUserId(), role.getRoleId());
            if (success) {
                //delete user password change if exist
                deleteUserPasswordChange(user.getUserId(), role.getRoleId(), ctx);
            } else {
                LightlyAddressCmd cmd = new LightlyAddressCmd();
                cmd.putParam("addressId", user.getAddressId());
                cmd.executeInStateless(ctx);

                ResultDTO myResultDTO = cmd.getResultDTO();
                String name = (String) myResultDTO.get("addressName");
                resultDTO.addResultMessage("msg.NotFound", name);
                resultDTO.setForward("Fail");
            }
        } catch (FinderException e) {
            resultDTO.setForward("Fail");
            return;
        }
    }

    private boolean deleteRoleForUser(Integer userId, Integer roleId) {
        UserRoleHome userRoleHome =
                (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        try {
            UserRolePK userRolePK = new UserRolePK();
            userRolePK.roleId = roleId;
            userRolePK.userId = userId;
            UserRole userRole = userRoleHome.findByPrimaryKey(userRolePK);
            try {
                userRole.remove();
                return true;
            } catch (RemoveException e) {
                log.debug("->Delete UserRole for userId=" + userId + " roleId=" + roleId + " FAIL");
            }
        } catch (FinderException e) {
            log.debug("->Read UserRole for userId=" + userId + " roleId=" + roleId + " FAIL");
        }
        return false;
    }

    private UserRole createUserRole(Role role, User user) {
        UserRoleHome home = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        UserRoleDTO dto = new UserRoleDTO();
        dto.put("roleId", role.getRoleId());
        dto.put("userId", user.getUserId());
        dto.put("companyId", role.getCompanyId());
        try {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + user.getUserId() + " OK");
            return home.create(dto);
        } catch (CreateException e) {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + user.getUserId() + " FAIL", e);
        }
        return null;
    }

    private Collection readUserByRole(Role role) {
        Collection userRoles = (Collection) EJBFactory.i.callFinder(new UserRoleDTO(),
                "findUsersByRole",
                new Object[]{role.getRoleId(), role.getCompanyId()});

        if (null == userRoles) {
            userRoles = new ArrayList();
        }

        return userRoles;
    }

    private boolean useRole(Integer userId, Integer roleId) {
        UserRoleHome userRoleHome =
                (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        try {
            UserRolePK userRolePK = new UserRolePK();
            userRolePK.roleId = roleId;
            userRolePK.userId = userId;
            userRoleHome.findByPrimaryKey(userRolePK);
            return true;
        } catch (FinderException e) {
            log.debug("->Read UserRole for userId=" + userId + " roleId=" + roleId + " FAIL");
        }
        return false;
    }

    private void createUserPasswordChange(Integer userId, Integer roleId, SessionContext ctx) {
        PasswordChangeCmdUtil passwordChangeCmdUtil = new PasswordChangeCmdUtil(ctx);
        passwordChangeCmdUtil.userRoleAssigned(userId, roleId);
    }

    private void deleteUserPasswordChange(Integer userId, Integer roleId, SessionContext ctx) {
        PasswordChangeCmdUtil passwordChangeCmdUtil = new PasswordChangeCmdUtil(ctx);
        passwordChangeCmdUtil.userRoleRemoved(userId, roleId);
    }

    public boolean isStateful() {
        return false;
    }

}
