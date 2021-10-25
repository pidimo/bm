package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.UserSessionLog;
import com.piramide.elwis.domain.admin.UserSessionLogHome;
import com.piramide.elwis.dto.admin.UserSessionLogDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Manages the user session log status, connection, disconnections, validation of already connected users.
 *
 * @author Fernando
 * @version $Id: UserLogCmd.java 12538 2016-04-12 20:54:18Z miguel $
 */

public class UserLogCmd extends EJBCommand {
    private Log log = LogFactory.getLog(UserLogCmd.class);
    private boolean startSession;
    private boolean closeSession;
    private boolean isConnectedUser;
    private boolean checkOtherUserConnected;

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserLogCmd.... " + paramDTO);

        Integer userId = (Integer) paramDTO.get("userId");
        if (startSession) {
            log.debug("starting session log for userId: " + userId);
            Integer companyId = (Integer) paramDTO.get("companyId");
            String ip = paramDTO.getAsString("ip");

            startSession(companyId, userId, ip);

            /*if (!startSession(companyId, userId, ip)) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("Common.alreadyUserLogged", resultDTO.get("bannedIP"));
            }*/
        } else if (closeSession) {
            closeSession(userId);
        } else if (isConnectedUser) {
            isConnectedUser(userId);
        } else if (checkOtherUserConnected) {
            checkForOtherUserConnected(userId);
        }
    }


    public void setStartSession(boolean startSession) {
        this.startSession = startSession;
    }

    public void setCloseSession(boolean closeSession) {
        this.closeSession = closeSession;
    }

    public void setConnectedUser(boolean connectedUser) {
        isConnectedUser = connectedUser;
    }

    public void setCheckOtherUserConnected(boolean checkOtherUserConnected) {
        this.checkOtherUserConnected = checkOtherUserConnected;
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * Checks the user is connected or not, and put in <code>ResultDTO</code> object the result
     * the key:
     * isConnectedUser : contains a <code>Boolean</code> object  than indicate if the user is conected or not.
     *
     * @param userId <code>Integer</code> object that is the user identifier.
     */
    private void isConnectedUser(Integer userId) {
        UserSessionLogHome userSessionLogHome = (UserSessionLogHome)
                EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);

        UserSessionLog userSessionLog = null;
        try {
            userSessionLog = userSessionLogHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            //The user has not assigned any log
        }

        boolean result = false;
        if (null != userSessionLog) {
            Boolean isConnected = userSessionLog.getIsConnected();
            if (null != isConnected) {
                result = isConnected;
            }
        }

        resultDTO.put("isConnectedUser", result);
    }

    /**
     * Start session log, returns true if the session starts correctly, returns false if someone else was using the
     * same account from another IP
     *
     * @param companyId the company id
     * @param userId    the user id
     * @param ip        the current user's IP
     * @return true success, false, someone else was logged in
     */
    private boolean startSession(Integer companyId, Integer userId, String ip) {
        UserSessionLog sessionLog;

        UserSessionLogHome homeLog = (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);

        boolean isStartFromApp = "true".equals(paramDTO.get("isStartFromApp"));
        boolean normalStarted = true;
        try {
            sessionLog = homeLog.findByPrimaryKey(userId);

            /*if (sessionLog.getIsConnected() && sessionLog.getEndConnectionDateTime() == null
                    && !sessionLog.getIp().equals(ip)) {
                resultDTO.put("bannedIP", sessionLog.getIp());
                normalStarted = false;
            }*/

            if (isStartFromApp) {
                sessionLog.setLastActionApp(System.currentTimeMillis());
            } else {
                sessionLog.setConnectionDateTime(System.currentTimeMillis());
                sessionLog.setIp(paramDTO.get("ip").toString());
                sessionLog.setIsConnected(AdminConstants.STATUS_ACTIVE);
                sessionLog.setSessionId(paramDTO.get("sessionId").toString());
                sessionLog.setEndConnectionDateTime(null);
                sessionLog.setLastActionDateTime(System.currentTimeMillis());
            }

        } catch (FinderException e) {
            log.debug("First time log in, create the log");
            UserSessionLogDTO dto = new UserSessionLogDTO();
            dto.put("companyId", companyId);
            dto.put("userId", userId);
            dto.put("connectionDateTime", System.currentTimeMillis());
            dto.put("lastActionDateTime", System.currentTimeMillis());
            dto.put("isConnected", AdminConstants.STATUS_ACTIVE);
            dto.put("sessionId", paramDTO.get("sessionId"));
            dto.put("ip", paramDTO.get("ip"));
            dto.put("lastActionApp", System.currentTimeMillis());
            ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, dto, resultDTO, false, false, false, false);
        }
        return normalStarted;
    }

    /**
     * Looks for a open session in the sessionlog hostory with the specified userId and close the
     * session.
     *
     * @param userId the user id.
     */
    private void closeSession(Integer userId) {
        try {
            UserSessionLogHome home = (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
            com.piramide.elwis.domain.admin.UserSessionLog userLog;
            userLog = home.findByPrimaryKey(userId);
            userLog.setEndConnectionDateTime(System.currentTimeMillis());
            userLog.setIsConnected(AdminConstants.STATUS_INACTIVE);
        } catch (FinderException e) {
            log.debug("The user : " + userId + " never has been logged in previously");
        }
    }

    private void checkForOtherUserConnected(Integer userId) {
        String sessionId = (String) paramDTO.get("sessionId");

        UserSessionLogHome userSessionLogHome = (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);

        UserSessionLog userSessionLog = null;
        try {
            userSessionLog = userSessionLogHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            //The user has not assigned any log
        }

        boolean result = false;
        if (null != userSessionLog) {
            if (userSessionLog.getIsConnected() && userSessionLog.getEndConnectionDateTime() == null
                    && !userSessionLog.getSessionId().equals(sessionId)) {
                result = true;
                resultDTO.put("otherUserSessionId", userSessionLog.getSessionId());
            }
        }

        resultDTO.put("isOtherUserConnected", result);
    }

}