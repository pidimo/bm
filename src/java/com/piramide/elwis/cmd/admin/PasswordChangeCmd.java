package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.admin.PasswordChangeDTO;
import com.piramide.elwis.dto.admin.RolePasswordChangeDTO;
import com.piramide.elwis.dto.admin.UserPasswordChangeDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class PasswordChangeCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing PasswordChangeCmd................" + paramDTO);

        if ("create".equals(getOp())) {
            create();
        }
        if ("update".equals(getOp())) {
            update();
        }
        if ("delete".equals(getOp())) {
            delete();
        }
        if ("roleUpdatedByUser".equals(getOp())) {
            Integer userId = new Integer(paramDTO.get("userId").toString());
            List removedRoleIdsList = (List) paramDTO.get("removedRoleIds");
            List currentRoleIdsList = (List) paramDTO.get("assignedRoleIds");
            updateUserPasswordChangeByRole(userId, removedRoleIdsList, currentRoleIdsList);
        }
        if ("deleteFromUser".equals(getOp())) {
            Integer userId = new Integer(paramDTO.get("userId").toString());
            removeUserPasswordChange(userId);
        }
        if ("deleteUserPasswordChange".equals(getOp())) {
            Integer userId = new Integer(paramDTO.get("userId").toString());
            Integer passwordChangeId = new Integer(paramDTO.get("passwordChangeId").toString());
            removeUserPasswordChange(userId, passwordChangeId);
        }

        if ("read".equals(getOp())) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void read() {
        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));

        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(paramDTO);
        PasswordChange passwordChange =(PasswordChange) ExtendedCRUDDirector.i.read(passwordChangeDTO, resultDTO, checkReferences);
    }

    private void create() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(paramDTO);
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        List assignedRoleIds = getAssignedRoleIds();
        if (!isValidRoleIds(assignedRoleIds)) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("PasswordChange.error.roleDeleted");
            return;
        }

        List<Integer> userIds = findUserIdByRoles(assignedRoleIds, companyId);

        passwordChangeDTO.put("totalUser", userIds.size());
        PasswordChange passwordChange = (PasswordChange) ExtendedCRUDDirector.i.create(passwordChangeDTO, resultDTO, false);

        if (passwordChange != null) {
            createRolePasswordChange(assignedRoleIds, passwordChange);
            createUserPasswordChange(userIds, passwordChange);
        }
    }

    private void update() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(paramDTO);
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        List assignedRoleIds = getAssignedRoleIds();
        if (!isValidRoleIds(assignedRoleIds)) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("PasswordChange.error.roleDeleted");
            return;
        }
        List<Integer> userIds = findUserIdByRoles(assignedRoleIds, companyId);

        passwordChangeDTO.put("totalUser", userIds.size());
        PasswordChange passwordChange = (PasswordChange) ExtendedCRUDDirector.i.update(passwordChangeDTO, resultDTO, false, true, true, "Fail");

        if (passwordChange != null && !resultDTO.isFailure()) {
            deleteRolePasswordChange(passwordChange.getPasswordChangeId(), passwordChange.getCompanyId());
            createRolePasswordChange(assignedRoleIds, passwordChange);
            updateUserPasswordChange(userIds, passwordChange);
        }
    }

    private void delete() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(passwordChangeDTO, resultDTO, true, "Fail");
    }

    private boolean createRolePasswordChange(List roleIdsList, PasswordChange passwordChange) {
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        for (int i = 0; i < roleIdsList.size(); i++) {
            Integer roleId = new Integer(roleIdsList.get(i).toString());
            try {
                Role role = roleHome.findByPrimaryKey(roleId);
                createRolePasswordChange(role, passwordChange);
            } catch (FinderException e) {
                log.debug("Not found role:" + roleId, e);
                return false;
            }
        }
        return true;
    }

    private RolePasswordChange createRolePasswordChange(Role role, PasswordChange passwordChange) {
        RolePasswordChangeHome rolePasswordChangeHome = (RolePasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLEPASSWORDCHANGE);

        RolePasswordChangeDTO rolePasswordChangeDTO = new RolePasswordChangeDTO();
        rolePasswordChangeDTO.put("passwordChangeId",passwordChange.getPasswordChangeId() );
        rolePasswordChangeDTO.put("roleId",role.getRoleId() );
        rolePasswordChangeDTO.put("companyId", passwordChange.getCompanyId());

        try {
            return rolePasswordChangeHome.create(rolePasswordChangeDTO);
        } catch (CreateException e) {
            log.debug("Error in create role password change..." + role.getRoleId(), e);
        }
        return null;
    }

    private void deleteRolePasswordChange(Integer passwordChangeId, Integer companyId) {
        RolePasswordChangeHome rolePasswordChangeHome = (RolePasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLEPASSWORDCHANGE);
        try {
            Collection elements = rolePasswordChangeHome.findRoleByPasswordChange(passwordChangeId, companyId);
            for (Object object : elements) {
                RolePasswordChange rolePasswordChange = (RolePasswordChange) object;
                try {
                    rolePasswordChange.remove();
                } catch (RemoveException e) {
                    log.debug("Error in remove rolePasswordChange... roleId=" + rolePasswordChange.getRoleId(), e);
                }
            }
        } catch (FinderException e) {
            log.debug("Error in find rolePasswordChange...", e);
        }
    }

    private boolean createUserPasswordChange(List<Integer> userIds, PasswordChange passwordChange) {
        for (int i = 0; i < userIds.size(); i++) {
            Integer userId = userIds.get(i);
            createUserPasswordChange(userId, passwordChange.getPasswordChangeId(), passwordChange.getCompanyId());
        }
        return true;
    }

    private UserPasswordChange createUserPasswordChange(Integer userId, Integer passwordChangeId, Integer companyId) {
        UserPasswordChangeHome userPasswordChangeHome = (UserPasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERPASSWORDCHANGE);
        UserPasswordChangeDTO dto = new UserPasswordChangeDTO();
        dto.put("passwordChangeId", passwordChangeId);
        dto.put("userId", userId);
        dto.put("companyId", companyId);

        try {
            return userPasswordChangeHome.create(dto);
        } catch (CreateException e) {
            log.debug("Error in create user password change..." + userId, e);
        }
        return null;
    }

    /**
     * Create or remove user relations to this password change
     * @param userIds
     * @param passwordChange
     */
    private void updateUserPasswordChange(List<Integer> userIds, PasswordChange passwordChange) {

        UserPasswordChangeHome userPasswordChangeHome = (UserPasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERPASSWORDCHANGE);
        List<Integer> newUserIds = new ArrayList<Integer>();
        List<Integer> existUserIds = new ArrayList<Integer>();
        List<Integer> removeUserIds = new ArrayList<Integer>();

        Collection userPasswordChanges = null;
        try {
            userPasswordChanges = userPasswordChangeHome.findUserByPasswordChange(passwordChange.getPasswordChangeId(), passwordChange.getCompanyId());
        } catch (FinderException e) {
            userPasswordChanges = new ArrayList();
        }

        //verify old elements
        for (Iterator iterator = userPasswordChanges.iterator(); iterator.hasNext();) {
            UserPasswordChange userPasswordChange = (UserPasswordChange) iterator.next();
            if (userIds.contains(userPasswordChange.getUserId())) {
                existUserIds.add(userPasswordChange.getUserId());
            } else {
                removeUserIds.add(userPasswordChange.getUserId());
            }
        }

        for (Integer userId : userIds) {
            if (!existUserIds.contains(userId)) {
                newUserIds.add(userId);
            }
        }

        createUserPasswordChange(newUserIds, passwordChange);
        removeUserPasswordChange(removeUserIds, passwordChange.getPasswordChangeId());
    }

    private void removeUserPasswordChange(List<Integer> userIds, Integer passwordChangeId) {
        for (Integer userId : userIds) {
            removeUserPasswordChange(userId, passwordChangeId);
        }
    }

    private boolean removeUserPasswordChange(Integer userId, Integer passwordChangeId) {
        UserPasswordChangeHome userPasswordChangeHome = (UserPasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERPASSWORDCHANGE);

        UserPasswordChangePK userPasswordChangePK = new UserPasswordChangePK(passwordChangeId,userId);
        try {
            UserPasswordChange userPasswordChange = userPasswordChangeHome.findByPrimaryKey(userPasswordChangePK);
            userPasswordChange.remove();
        } catch (FinderException e) {
            log.debug("Not found userPasswordChange passwordChangeId=" + passwordChangeId + " userId=" + userId);
            return false;
        } catch (RemoveException e) {
            log.debug("Canot remove userPasswordChange passwordChangeId=" + passwordChangeId + " userId=" + userId);
            return false;
        }
        return true;
    }

    private boolean removeUserPasswordChange(Integer userId) {
        UserPasswordChangeHome userPasswordChangeHome = (UserPasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERPASSWORDCHANGE);
        User user = findUser(userId);
        if (user != null) {
            try {
                Collection userPasswordChanges = userPasswordChangeHome.findPasswordChangeByUser(userId, user.getCompanyId());
                for (Iterator iterator = userPasswordChanges.iterator(); iterator.hasNext();) {
                    UserPasswordChange userPasswordChange = (UserPasswordChange) iterator.next();
                    PasswordChange passwordChange = userPasswordChange.getPasswordChange();
                    userPasswordChange.remove();
                    decrementTotalUser(passwordChange);
                }
            } catch (FinderException e) {
                log.debug("Error in find userPasswordChange by userId:" + userId);
            } catch (RemoveException e) {
                log.debug("Canot remove userPasswordChange...");
                return false;
            }
        }
        return true;
    }

    private void updateUserPasswordChangeByRole(Integer userId, List<Integer> removedRoleIdsList, List<Integer> currentRoleIdsList) {
        User user = findUser(userId);
        if (user != null) {
            Long currentMillis = System.currentTimeMillis();
            if (user.getTimeZone() != null) {
                DateTime dateTime = new DateTime(currentMillis, DateTimeZone.forID(user.getTimeZone()));
                currentMillis = dateTime.getMillis();
            }

            RolePasswordChangeHome rolePasswordChangeHome = (RolePasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLEPASSWORDCHANGE);
            //process user removed roles
            for (Integer roleId : removedRoleIdsList) {
                try {
                    Collection rolePasswordChanges = rolePasswordChangeHome.findPasswordChangeByRole(roleId, user.getCompanyId());
                    for (Iterator iterator = rolePasswordChanges.iterator(); iterator.hasNext();) {
                        RolePasswordChange rolePasswordChange = (RolePasswordChange) iterator.next();
                        PasswordChange passwordChange = rolePasswordChange.getPasswordChange();

                        if (!existUserRolesInPasswordChangeRoles(user, passwordChange)) {
                            //remove if exist registered
                            boolean success = removeUserPasswordChange(userId, passwordChange.getPasswordChangeId());
                            if (success) {
                                decrementTotalUser(passwordChange);
                            }
                        }
                    }
                } catch (FinderException e) {
                    log.debug("Error in find rolePasswordChange...");
                }
            }

            //process user defined roles in valid password changes
            for (Integer roleId : currentRoleIdsList) {
                List<Integer> passwordChangeIdsList = getPasswordChangeIdsByRoleAndTime(roleId, currentMillis);
                for (Integer passwordChangeId : passwordChangeIdsList) {
                    UserPasswordChange userPasswordChange = findUserPasswordChange(userId, passwordChangeId);
                    //create if not exist
                    if (userPasswordChange == null) {
                        userPasswordChange = createUserPasswordChange(userId, passwordChangeId, user.getCompanyId());
                        if (userPasswordChange != null) {
                            incrementTotalUser(userPasswordChange.getPasswordChange());
                        }
                    }
                }
            }
        }
    }

    private List<Integer> getPasswordChangeIdsByRoleAndTime(Integer roleId, Long millis) {
        List<Integer> passwordChangeIdsList = new ArrayList<Integer>();
        RolePasswordChangeHome rolePasswordChangeHome = (RolePasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLEPASSWORDCHANGE);
        try {
            Collection rolePasswordChanges = rolePasswordChangeHome.findPasswordChangeByRoleAndTime(roleId, millis);
            for (Iterator iterator = rolePasswordChanges.iterator(); iterator.hasNext();) {
                RolePasswordChange rolePasswordChange = (RolePasswordChange) iterator.next();
                passwordChangeIdsList.add(rolePasswordChange.getPasswordChangeId());
            }
        } catch (FinderException e) {
            log.debug("Error in find rolePasswordChanges...", e);
        }

        resultDTO.put("passwordChangeIds", passwordChangeIdsList);
        return passwordChangeIdsList;
    }

    private List<Integer> findUserIdByRoles(List roleIdsList, Integer companyId) {
        Set<Integer> userIdSet = new HashSet<Integer>();

        UserRoleHome userRoleHome = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        for (int i = 0; i < roleIdsList.size(); i++) {
            Integer roleId = new Integer(roleIdsList.get(i).toString());
            try {
                Collection userRoles = userRoleHome.findUsersByRole(roleId, companyId);
                for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
                    UserRole userRole = (UserRole) iterator.next();
                    userIdSet.add(userRole.getUserId());
                }
            } catch (FinderException e) {
                log.debug("Error in execute finder user role:" + roleId, e);
            }
        }
        return new ArrayList<Integer>(userIdSet);
    }

    private List getAssignedRoleIds() {
        List assignedRoleIds = null;
        if (paramDTO.containsKey("assignedRoleIds")) {
            assignedRoleIds = (List) paramDTO.get("assignedRoleIds");
        }
        if (assignedRoleIds == null) {
            assignedRoleIds = new ArrayList();
        }
        return assignedRoleIds;
    }

    private boolean isValidRoleIds(List roleIdsList) {
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        for (int i = 0; i < roleIdsList.size(); i++) {
            Integer roleId = new Integer(roleIdsList.get(i).toString());
            try {
                Role role = roleHome.findByPrimaryKey(roleId);
            } catch (FinderException e) {
                log.debug("Not found role:" + roleId, e);
                return false;
            }
        }
        return true;
    }

    private boolean existUserRolesInPasswordChangeRoles(User user, PasswordChange passwordChange) {
        Collection userRoles = user.getUserRole();
        Collection passwordChangeRoles = passwordChange.getRolePasswordChanges();

        for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
            UserRole userRole = (UserRole) iterator.next();
            for (Iterator iterator2 = passwordChangeRoles.iterator(); iterator2.hasNext();) {
                RolePasswordChange rolePasswordChange = (RolePasswordChange) iterator2.next();
                if (userRole.getRoleId().equals(rolePasswordChange.getRoleId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private User findUser(Integer userId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            user = userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            log.debug("Not found user..:" + userId);
        }
        return user;
    }

    private PasswordChange findPasswordChange(Integer passwordChangeId) {
        PasswordChange passwordChange = null;
        PasswordChangeHome passwordChangeHome = (PasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_PASSWORDCHANGE);
        try {
            passwordChange = passwordChangeHome.findByPrimaryKey(passwordChangeId);
        } catch (FinderException e) {
            log.debug("Not found password change..:" + passwordChangeId);
        }
        return passwordChange;
    }

    private UserPasswordChange findUserPasswordChange(Integer userId, Integer passwordChangeId) {
        UserPasswordChange userPasswordChange = null;
        UserPasswordChangeHome userPasswordChangeHome = (UserPasswordChangeHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERPASSWORDCHANGE);
        UserPasswordChangePK userPasswordChangePK = new UserPasswordChangePK(passwordChangeId, userId);
        try {
            userPasswordChange = userPasswordChangeHome.findByPrimaryKey(userPasswordChangePK);
        } catch (FinderException e) {
            log.debug("Not found userPasswordChange passwordChangeId=" + passwordChangeId + " userId=" + userId);
        }
        return userPasswordChange;
    }

    private void incrementTotalUser(PasswordChange passwordChange) {
        passwordChange.setTotalUser(passwordChange.getTotalUser() + 1);
    }

    private void decrementTotalUser(PasswordChange passwordChange) {
        passwordChange.setTotalUser(passwordChange.getTotalUser() - 1);
    }
}
