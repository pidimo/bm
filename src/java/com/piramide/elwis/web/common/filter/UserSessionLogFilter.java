package com.piramide.elwis.web.common.filter;

import com.piramide.elwis.domain.admin.UserSessionLog;
import com.piramide.elwis.domain.admin.UserSessionLogHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.UserBannedData;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.ejb.FinderException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


/**
 * Thi filters allows to update the last action time in the user sessions table, updating the last action
 * given a inteval of time between cheking every action.
 *
 * @author Mauren Carrasco
 * @version 4.2.2
 */

public class UserSessionLogFilter implements Filter {

    private Log log = LogFactory.getLog(this.getClass());
    private Map bannedUserMap;
    private static final String TIME_INTERVAL_PARAM = "timeInterval";
    // 1min = 60000ms
    private long timeIntervalInMillis = 300000; //by default five minutes in millis

    public void init(FilterConfig filterConfig) throws ServletException {
        bannedUserMap = new HashMap();
        filterConfig.getServletContext().setAttribute("bannedUser", bannedUserMap);
        long timeInterval = Long.parseLong(filterConfig.getInitParameter(TIME_INTERVAL_PARAM));
        timeIntervalInMillis = timeInterval * (60000);
    }


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws java.io.IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false); //it does not creates a new session.

        boolean hasErrors = false;
        ActionErrors errors = new ActionErrors();
        User user;

        //valid session and valid user
        if (session != null && (user = (User) session.getAttribute(Constants.USER_KEY)) != null) {
            /**
             * first check if the user has been banned because someone has logged in from anther
             *  same account.
             */

            UserBannedData banned = (UserBannedData) bannedUserMap.remove(user.getValue("userId") + "-" + session.getId());

            boolean isUserFromMobile = "true".equals(user.getValue("isMobileUser"));

            //it means the user was thrown out by another user with the same account but another IP.
            if (banned != null && !isUserFromMobile) {
                errors.add("error", new ActionError("Common.user.bannedMessage"));
                user.setBanned(true);
                hasErrors = true;
            } else {
                //it was not banned, so update the last action if time interval allows to do it.

                boolean isBackGroundProcess = false;
                if (!GenericValidator.isBlankOrNull(req.getParameter("isBackgroundProcess"))) {
                    isBackGroundProcess = Boolean.parseBoolean(req.getParameter("isBackgroundProcess"));
                }

                UserSessionLog userLog;
                long lastActionTime = (Long) user.getValue("lastActionTime");
                /**
                 * It the difference between the last action and the current time is greather or equal than the
                 * interval configured, do the updating in the database.
                 */
                if (!isBackGroundProcess && ((System.currentTimeMillis() - lastActionTime) >= timeIntervalInMillis)) {
                    UserSessionLogHome home = (UserSessionLogHome)
                            EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
                    log.debug("Updating the last action time in the session log table");
                    try {
                        userLog = home.findByPrimaryKey((Integer) user.getValue("userId"));

                        if (isUserFromMobile) {
                            userLog.setLastActionApp(System.currentTimeMillis());//update last action of mobile app.
                        } else {

                            userLog.setLastActionDateTime(System.currentTimeMillis());//update last action.

                            /**
                             * If we are defining the last action means the user is connected, so ensure the
                             * status is connected in the session table. The session table can have the status as
                             * disconnected when another session (for the same user) has been started in the same IP and
                             * it was closed by the container (which closes the session).
                             */
                            userLog.setEndConnectionDateTime(null);
                            userLog.setIsConnected(AdminConstants.STATUS_ACTIVE);
                        }


                        //update the session last action to the current time.
                        user.setValue("lastActionTime", System.currentTimeMillis());
                    } catch (FinderException e) {
                        log.debug("The user was not found");
                    }
                }

            }
        }
        if (hasErrors) { //print the errors and return the login page.
            request.setAttribute(Globals.ERROR_KEY, errors);
            request.setAttribute("hasErrors", Constants.TRUE_VALUE);
            session.invalidate();//invalidate the banned user session
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            chain.doFilter(request, response); //continue with the filters chain.
        }
    }

    public void destroy() {
        log = null;
        bannedUserMap = null;
    }

}
