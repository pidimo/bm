package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.dashboard.DashboardConfigurationCmd;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.common.session.UserSession;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Favorite;
import com.piramide.elwis.domain.contactmanager.Recent;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.admin.UserRoleDTO;
import com.piramide.elwis.dto.common.session.SessionDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.contactmanager.FavoriteDTO;
import com.piramide.elwis.dto.contactmanager.RecentDTO;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * User Adminstration Comand, bussines Logic encarge to manage user from organizations
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version UserAdminCmd.java, v 2.0 Jun 23, 2004 10:29:46 AM
 */

public class UserAdminCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserCmd");

        log.debug("paramDTO...: " + paramDTO);
        /*HashMapCleaner.clean(paramDTO);*/
        String operation;
        operation = paramDTO.getOp();

        if (CRUDDirector.OP_CREATE.equals(operation)) {
            createUserData();
            return;
        }
        if (CRUDDirector.OP_UPDATE.equals(operation)) {
            updateUserdata(ctx);
            return;
        }
        if (CRUDDirector.OP_DELETE.equals(operation)) {
            deleteUserData(ctx);
            return;
        }

        if (!paramDTO.hasOp() || "true".equals(paramDTO.get("operation"))) {
            try {
                readUserData();
            } catch (FinderException e) {
            }
            return;
        }
    }


    public void readUserData() throws FinderException {
        log.debug("read data...");

        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new UserDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.put("referenced", Boolean.valueOf(true));
            }
        }

        User user = (User) ExtendedCRUDDirector.i.read(new UserDTO(paramDTO), resultDTO, false);
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        if (user != null && !resultDTO.isFailure()) {
            Address address = null;
            Company company = null;
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            address = addressHome.findByPrimaryKey(user.getAddressId());
            CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
            company = companyHome.findByPrimaryKey(companyId);
            readRoles(user, companyId);
            StringBuffer name = new StringBuffer(address.getName1());
            if (address.getName2() != null && address.getName2().length() > 0) {
                name.append(", ").append(address.getName2());
            }
            resultDTO.put("addressId", user.getAddressId());
            resultDTO.put("addressName", name.toString());
            resultDTO.put("employeeName", name.toString());
            resultDTO.put("rowsPerPage", company.getRowsPerPage());
            resultDTO.put("timeout", company.getTimeout());
            resultDTO.put("active", user.getActive());
            resultDTO.put("fail", new Boolean(false));
        } else {
            resultDTO.put("fail", new Boolean(true));
            return;
        }
    }

    private void createUserData() {
        // check employee
        CompanyDTO companyDTO = new CompanyDTO(paramDTO);
        Company company = null;
        company = (Company) EJBFactory.i.findEJB(companyDTO);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        Collection users = new ArrayList();
        User actualUser = null;
        try {//verifica cuantos usuarios activos ya tiene la compania.
            users = userHome.findUserByCompanyAndActiveUsers(company.getCompanyId(), new Boolean(true));

            if (paramDTO.get("addressId") != null && !SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("addressId"))) {
                actualUser = userHome.findByAddressId(new Integer(paramDTO.get("companyId").toString()), new Integer(paramDTO.get("addressId").toString()));
            } else if (paramDTO.get("employeeId") != null && !SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("employeeId"))) {
                actualUser = userHome.findByAddressId(new Integer(paramDTO.get("companyId").toString()), new Integer(paramDTO.get("employeeId").toString()));
            }
        } catch (FinderException e) {
        }//verifica si aun puede crear usuarios

        if (paramDTO.get("employeeId") != null && !SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("employeeId"))) {
            paramDTO.put("addressId", paramDTO.get("employeeId"));
        }

        paramDTO.put("active", new Boolean("on".equals(paramDTO.getAsString("active"))));

        if ((((company.getUsersAllowed() == null || company.getUsersAllowed().intValue() > users.size())) ||
                company.getUsersAllowed().intValue() == users.size() && !((Boolean) paramDTO.get("active")))
                && actualUser == null) {
            //noinspection MismatchedQueryAndUpdateOfCollection
            List assignedRoles = new ArrayList();
            String pass = "";
            //by default attributes for a new user
            paramDTO.put("rowsPerPage", company.getRowsPerPage());
            paramDTO.put("timeout", company.getTimeout());
            paramDTO.put("maxRecentList", new Integer(10));
            if (null != company.getTimeZone() && !"".equals(company.getTimeZone().trim())) {
                paramDTO.put("timeZone", company.getTimeZone());
            }

            log.debug("encript password");
            try {
                pass = EncryptUtil.i.encryt((String) paramDTO.get("passwordConfir"));
            } catch (ServiceUnavailableException e) {
                log.error("Cannot Encript password... " + e);
            }

            User user = (User) CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, new UserDTO(paramDTO), resultDTO);

            RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
            if (paramDTO.get("defineRoles") != null) {

                List rols = (List) paramDTO.get("defineRoles");
                for (Iterator iterator = rols.iterator(); iterator.hasNext(); ) {
                    try {
                        Role rol = (Role) roleHome.findByPrimaryKey(new Integer(iterator.next().toString()));
                        assignedRoles.add(createUserRole(rol, user.getUserId(), company.getCompanyId()));
                    } catch (FinderException e) {
                        log.debug("Cannot find role ..." + e);
                        resultDTO.addResultMessage("User.rolChange");
                        resultDTO.setResultAsFailure();
                        return;
                    }
                }
            }


            user.setIsDefaultUser(new Boolean(false));
            user.setUserPassword(pass);
            //user.setUserRole((Collection) assignedRoles);
        } else {
            if (actualUser != null) {
                if (!SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("employeeName")) && paramDTO.get("employeeName") != null) {
                    resultDTO.addResultMessage("msg.Duplicated", paramDTO.get("employeeName"));
                }
                if (!SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("addressName")) && paramDTO.get("addressName") != null) {
                    resultDTO.addResultMessage("msg.Duplicated", paramDTO.get("addressName"));
                }
                resultDTO.setResultAsFailure();
            } else {
                resultDTO.addResultMessage("User.userAllowedFull");
                resultDTO.setResultAsFailure();
            }
        }
    }


    private List<Integer> deleteUserRoles(Integer userId, Integer companyId) {
        List<Integer> roleIdsList = new ArrayList<Integer>();
        UserRoleHome home = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        try {
            Collection elements = home.findRolesByUser(userId, companyId);
            for (Object object : elements) {
                UserRole userRole = (UserRole) object;
                Integer roleId = userRole.getRoleId();
                roleIdsList.add(roleId);
                try {
                    userRole.remove();
                } catch (RemoveException e) {
                    log.debug("-> Remove UserRole userId=" + userRole.getUserId() +
                            " roleId=" + userRole.getRoleId() + " FAIL");
                }
                log.debug("-> Remove UserRole userId=" + userId +
                        " roleId=" + roleId + " OK");
            }
        } catch (FinderException e) {
            log.debug("-> Read UserRoles for userId=" + userId + " FAIL");
        }
        return roleIdsList;
    }

    private UserRole readUserRole(Integer roleId, Integer userId) {
        UserRoleHome home = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        UserRolePK pk = new UserRolePK();
        pk.roleId = roleId;
        pk.userId = userId;

        try {
            return home.findByPrimaryKey(pk);
        } catch (FinderException e) {
            log.debug("-> Read userRole object FAIL");
        }

        return null;
    }

    private UserRole createUserRole(Role role, Integer userId, Integer companyId) {
        UserRoleHome home = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        UserRoleDTO dto = new UserRoleDTO();
        dto.put("roleId", role.getRoleId());
        dto.put("userId", userId);
        dto.put("companyId", companyId);
        try {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + userId + " OK");
            return home.create(dto);
        } catch (CreateException e) {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + userId + " FAIL", e);
        }
        return null;
    }

    private void updateUserdata(SessionContext ctx) {
        log.debug("update user data...");
        log.debug("Version before: " + paramDTO.get("version"));

        String userName = (paramDTO.get("employeeName") != null ? paramDTO.get("employeeName").toString() : paramDTO.get("addressName").toString());

        String pass = null;
        UserDTO userDTO = new UserDTO(paramDTO);
        Company company = null;
        CompanyHome companyHome = null;
        UserHome userHome = null;
        userDTO.setPrimKey(paramDTO.getAsString("userId"));
        userDTO.put("version", paramDTO.get("version"));
        User user = null;
        Collection users = null;


        if (paramDTO.get("passwordConfir") != null) {
            try {
                pass = EncryptUtil.i.encryt((String) paramDTO.get("passwordConfir"));
                userDTO.put("userPassword", pass);
            } catch (ServiceUnavailableException e) {
            }
            user = (User) ExtendedCRUDDirector.i.update(userDTO, resultDTO, false, true, false, "Fail");

            if (null == user && "Fail".equals(resultDTO.getForward())) {

                resultDTO.addResultMessage("customMsg.NotFound", userName);
                resultDTO.setForward("Fail");
            }
            if ("Success".equals(resultDTO.getForward())) {
                resultDTO.addResultMessage("Common.changesOK");
                log.info(changePasswordAuditLog(user));
            }

            if (user != null || resultDTO.isFailure()) {
                resultDTO.put("employeeName", paramDTO.get("employeeName"));
                resultDTO.put("addressName", paramDTO.get("addressName"));
                return;
            }
        } else {
            try {
                companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
                company = companyHome.findByPrimaryKey(Integer.valueOf(paramDTO.get("companyId").toString()));
            } catch (FinderException e) {
                log.fatal("The company has not been found");
            }
            if ("on".equals(paramDTO.getAsString("active"))) {
                userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
                try {
                    users = userHome.findUserByCompanyAndActiveUsers(new Integer(paramDTO.get("companyId").toString()), new Boolean(true));
                    user = userHome.findByPrimaryKey(new Integer(paramDTO.get("userId").toString()));
                    if (company != null) {
                        if (company.getUsersAllowed() != null) {
                            if (company.getUsersAllowed().intValue() == users.size() && !users.contains(user)) {
                                resultDTO.addResultMessage("User.userAllowedFull");
                                resultDTO.setResultAsFailure();
                                resultDTO.setForward("GoBack");
                                return;
                            }
                        }
                    }
                } catch (FinderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            userDTO.put("userLogin", paramDTO.get("userLogin"));
            userDTO.put("favoriteLanguage", paramDTO.get("favoriteLanguage"));
            userDTO.put("accessIp", paramDTO.get("accessIp"));
            userDTO.put("timeZone", paramDTO.get("timeZone"));
            user = (User) ExtendedCRUDDirector.i.update(userDTO, resultDTO, false, true, false, "Fail");

            if (user != null && !resultDTO.isFailure()) {
                log.info(updateAuditLog(user));

                //only update if this is not an root user
                if (user.getIsDefaultUser() != null && !user.getIsDefaultUser()) {
                    user.setActive(new Boolean("on".equals(paramDTO.getAsString("active")) ? true : false));
                }

                RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
                boolean rolDefault = true;

                List<Integer> oldRoleIdsList = deleteUserRoles(user.getUserId(), user.getCompanyId());
                List<Integer> currentRoleIdsList = new ArrayList<Integer>();
                if (paramDTO.get("defineRoles") != null) {

                    List rols = (List) paramDTO.get("defineRoles");

                    for (Iterator iterator = rols.iterator(); iterator.hasNext(); ) {
                        try {

                            String valor = (String) iterator.next();
                            Role rol = (Role) roleHome.findByPrimaryKey(new Integer(valor));
                            if (rol.getIsDefault().booleanValue()) {
                                rolDefault = false;
                            }
                            UserRole userRole = createUserRole(rol, user.getUserId(), company.getCompanyId());
                            if (userRole != null) {
                                currentRoleIdsList.add(userRole.getRoleId());
                            }
                        } catch (FinderException e) {
                            log.debug("Cannot find role ..." + e);
                            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
                            try {
                                readRoles(user, companyId);
                            } catch (FinderException e1) {
                                log.error("Cannot read roles");
                            }
                            resultDTO.addResultMessage("User.rolChange");
                            resultDTO.setForward("RolesUpdate");
                            return;
                        }
                    }
                }

                //update user password change
                updatePasswordChangeByRoles(user.getUserId(), oldRoleIdsList, currentRoleIdsList, ctx);

                if (user.getIsDefaultUser().booleanValue() && rolDefault) {
                    resultDTO.addResultMessage("User.notEmptyDefaultRol");
                    resultDTO.setForward("GoBack");
                    return;
                }

                //user.setUserRole(list);
                return;
            } else {
                resultDTO.put("employeeName", paramDTO.get("employeeName"));
                resultDTO.put("addressName", paramDTO.get("addressName"));
                if (null != user) {
                    try {
                        readRoles(user, Integer.valueOf((String) paramDTO.get("companyId")));
                    } catch (FinderException e) {
                    }
                } else {
                    resultDTO.addResultMessage("customMsg.NotFound", userName);
                    resultDTO.setForward("Fail");
                }
                return;

            }
        }
    }

    private void deleteUserData(SessionContext ctx) {
        log.info("delete user data...");
        UserDTO dto = new UserDTO(paramDTO);
        IntegrityReferentialChecker.i.check(dto, resultDTO);
        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }
        //delete rescents and favorites
        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        UserSessionLogHome sessionLogHome = (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);

        try {
            Collection recents = (Collection) EJBFactory.i.callFinder(new RecentDTO(), "findByCompanyUser",
                    new Object[]{userId, companyId});
            log.debug("Removing recentes");
            Iterator iterator = recents.iterator();
            while (iterator.hasNext()) {
                Recent recent = (Recent) iterator.next();
                recent.remove();
            }
            log.debug("Removing favorites");
            Collection favorites = (Collection) EJBFactory.i.callFinder(new FavoriteDTO(), "findByCompanyUser",
                    new Object[]{userId, companyId});

            Iterator iterator2 = favorites.iterator();
            while (iterator2.hasNext()) {
                Favorite favorite = (Favorite) iterator2.next();
                favorite.remove();
            }

            //delete dashboardContainer
            deleteDashBoardRelation(userId, companyId, ctx);

            //removing the user session status fron usersession
            Collection userSessions = (Collection) EJBFactory.i.callFinder(new SessionDTO(), "findByUserId",
                    new Object[]{userId});
            for (Iterator iterator4 = userSessions.iterator(); iterator4.hasNext(); ) {
                ((UserSession) iterator4.next()).remove();
            }

            try {// remove userSessionLog ...
                UserSessionLog sessionLog = sessionLogHome.findByPrimaryKey(userId);
                sessionLog.remove();
            } catch (FinderException e) {
            }

            deleteUserRoles(userId, companyId);

            deleteUserPasswordChange(userId, ctx);

        } catch (RemoveException ex) {
            setFailResultDTO();
        }
        log.debug("values before to op " + getOp() + " :" + paramDTO);
        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, new UserDTO(paramDTO), resultDTO);

        if (resultDTO.isFailure()) {
            setFailResultDTO();
        }
        resultDTO.setForward("Success");
        return;
    }

    public boolean isStateful() {
        return false;
    }

    private void setFailResultDTO() {
        resultDTO.setResultAsFailure();
        resultDTO.put("name", paramDTO.get("name"));
        resultDTO.setForward("Fail");
        return;
    }

    private RoleDTO setRoleDTO(Role role) {

        RoleDTO dto = new RoleDTO();
        dto.put("roleId", role.getRoleId());
        dto.put("roleName", role.getRoleName());
        dto.put("isDefault", role.getIsDefault());

        return dto;
    }

    private Collection extractAssignedRolesToAllRoles(Collection allRolesDTOs, Collection assignedRolesDTOs) {
        Collection result = new ArrayList();
        for (Iterator iterator = allRolesDTOs.iterator(); iterator.hasNext(); ) {
            RoleDTO roleDTO = (RoleDTO) iterator.next();

            if (assignedRolesDTOs != null && !assignedRolesDTOs.contains(roleDTO)) {
                result.add(roleDTO);
            }
        }

        return result;
    }

    private void readRoles(User user, Integer companyId) throws FinderException {
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        Collection allRoles = roleHome.findByCompanyKey(companyId);
        Collection assignedRoles = user.getUserRole();

        List assignedRolesList = new ArrayList();
        List availableRolesList = new ArrayList();

        for (Iterator iterator = assignedRoles.iterator(); iterator.hasNext(); ) {
            UserRole o = (UserRole) iterator.next();
            assignedRolesList.add(setRoleDTO(o.getRole()));
        }

        for (Iterator iterator = allRoles.iterator(); iterator.hasNext(); ) {
            Role o = (Role) iterator.next();
            availableRolesList.add(setRoleDTO(o));
        }


        List newAvailableRolesList = (List) extractAssignedRolesToAllRoles(availableRolesList, assignedRolesList);

        resultDTO.put("availableRoles", newAvailableRolesList);
        resultDTO.put("defineRoles", assignedRolesList);
    }

    private void deleteDashBoardRelation(int userId, int companyId, SessionContext ctx) {
        DashboardConfigurationCmd confCmd = new DashboardConfigurationCmd();
        confCmd.setOp("deleteContainer");
        confCmd.putParam("userId", userId);
        confCmd.putParam("companyId", companyId);
        try {
            confCmd.executeInStateless(ctx);
        } catch (AppLevelException e) {
            e.printStackTrace();
        }
    }

    private void updatePasswordChangeByRoles(Integer userId, List<Integer> oldRoleIdsList, List<Integer> currentRoleIdsList, SessionContext ctx) {
        PasswordChangeCmdUtil passwordChangeCmdUtil = new PasswordChangeCmdUtil(ctx);
        passwordChangeCmdUtil.userRoleUpdated(userId, oldRoleIdsList, currentRoleIdsList);
    }

    private void deleteUserPasswordChange(Integer userId, SessionContext ctx) {
        PasswordChangeCmdUtil passwordChangeCmdUtil = new PasswordChangeCmdUtil(ctx);
        passwordChangeCmdUtil.deleteUserPasswordChange(userId);
    }

    private User findUser(Integer userId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        if (userId != null) {
            try {
                user = userHome.findByPrimaryKey(userId);
            } catch (FinderException e) {
                log.debug("Not found user.. " + userId);
            }
        }
        return user;
    }

    private String changePasswordAuditLog(User user) {
        User sessionUser = findUser(new Integer(paramDTO.get("currentUserId").toString()));
        return "[" + sessionUser.getUserLogin() + "/" + sessionUser.getCompany().getLogin() + "] updated password of [" + user.getUserLogin() + "/" + user.getCompany().getLogin() + "] successfully";
    }

    private String updateAuditLog(User user) {
        User sessionUser = findUser(new Integer(paramDTO.get("currentUserId").toString()));
        return "[" + sessionUser.getUserLogin() + "/" + sessionUser.getCompany().getLogin() + "] updated user [" + user.getUserLogin() + "/" + user.getCompany().getLogin() + "] successfully";
    }

}
