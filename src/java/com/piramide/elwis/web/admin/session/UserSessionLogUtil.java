package com.piramide.elwis.web.admin.session;

import com.piramide.elwis.cmd.admin.UserLogCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides utility methods to update the user session log
 *
 * @author Fernando
 * @version 4.2.2
 */
public class UserSessionLogUtil {
    private static Log log = LogFactory.getLog(UserSessionLogUtil.class);

    /**
     * Looks for a open session in the sessionlog hostory with the specified userId and close the
     * session.
     *
     * @param userId the user id.
     */
    public static void closeSession(Integer userId) {
        try {
            log.debug("Closing session log for userId: " + userId);
            UserLogCmd userLogCmd = new UserLogCmd();
            userLogCmd.setCloseSession(true);
            userLogCmd.putParam("userId", userId);
            BusinessDelegate.i.execute(userLogCmd, null);
        } catch (AppLevelException e) {
            log.error("Unexpected error trying to close the session for the userId: " + userId);
        }
    }

    /**
     * Invoque <code>UserLogCmd</code> command to check if the user associated to <code>userId</code> parameter is
     * connected to the application or not.
     *
     * @param userId <code>Integer</code> object that is the user identifier.
     * @return <code>true</code> if the user is connected <code>false</code> in other case.
     */
    public static boolean isConnectedUser(Integer userId) {
        UserLogCmd userLogCmd = new UserLogCmd();
        userLogCmd.setConnectedUser(true);
        userLogCmd.putParam("userId", userId);
        Boolean isConnectedUser = false;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userLogCmd, null);
            isConnectedUser = (Boolean) resultDTO.get("isConnectedUser");
        } catch (AppLevelException e) {
            log.error("Unexpected error happen when execute UserLogCmd command userId:" + userId, e);
        }

        return isConnectedUser;
    }

    public static Map otherUserConnectedInfo(Integer userId, String sessionId) {
        Map resultMap = new HashMap();

        UserLogCmd userLogCmd = new UserLogCmd();
        userLogCmd.setCheckOtherUserConnected(true);
        userLogCmd.putParam("userId", userId);
        userLogCmd.putParam("sessionId", sessionId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userLogCmd, null);
            resultMap.put("isOtherUserConnected", resultDTO.get("isOtherUserConnected"));
            resultMap.put("otherUserSessionId", resultDTO.get("otherUserSessionId"));

        } catch (AppLevelException e) {
            log.error("Unexpected error happen when execute UserLogCmd command userId:" + userId, e);
        }

        return resultMap;
    }

    public static boolean existOtherUserConnected(Integer userId, String sessionId) {
        Map otherUserInfoMap = otherUserConnectedInfo(userId, sessionId);
        Boolean result = false;
        if (otherUserInfoMap.get("isOtherUserConnected") != null) {
            result = (Boolean) otherUserInfoMap.get("isOtherUserConnected");
        }
        return result;
    }

    public static boolean startSession(User user, String sessionId, boolean isFromMobileApp, HttpServletRequest request) {
        boolean success = true;

        UserLogCmd userLogCmd = new UserLogCmd();
        userLogCmd.setStartSession(true);
        userLogCmd.putParam("userId", user.getValue("userId"));
        userLogCmd.putParam("companyId", user.getValue("companyId"));
        userLogCmd.putParam("ip", request.getRemoteAddr());
        userLogCmd.putParam("sessionId", sessionId);
        userLogCmd.putParam("isStartFromApp", String.valueOf(isFromMobileApp));


        try {
            ResultDTO userLogResultDTO = BusinessDelegate.i.execute(userLogCmd, request);
            if (userLogResultDTO.isFailure()) {
                success = false;
            }
        } catch (AppLevelException e) {
            log.error("Unexpected error happen when execute UserLogCmd command to start session", e);
        }

        return success;
    }
}
