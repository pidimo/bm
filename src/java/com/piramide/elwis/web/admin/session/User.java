package com.piramide.elwis.web.admin.session;

import com.piramide.elwis.cmd.webmailmanager.ImageStoreCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the an user logged into application
 * contains user session values for appplication.
 *
 * @author Fernando Montaño
 * @version $Id: User.java 12538 2016-04-12 20:54:18Z miguel $
 */
public class User implements HttpSessionBindingListener {
    private Log log = LogFactory.getLog(this.getClass());

    private Map valueMap = new HashMap();
    private Map securityAccessRights = new HashMap();
    private String loginIP;
    /**
     * This attribute is used to define if this instance in the session must be banned
     */
    private boolean isBanned = false;

    private boolean closeSessionsWhenInvalidate = true;

    public Map getValueMap() {
        return valueMap;
    }

    /**
     * set value to valueMap
     */
    public void setValue(String key, Object val) {
        valueMap.put(key, val);
    }

    /**
     * Gets a value from valueMap.
     */
    public Object getValue(String key) {
        return valueMap.get(key);
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isCloseSessionsWhenInvalidate() {
        return closeSessionsWhenInvalidate;
    }

    public void setCloseSessionsWhenInvalidate(boolean closeSessionsWhenInvalidate) {
        this.closeSessionsWhenInvalidate = closeSessionsWhenInvalidate;
    }

    public String toString() {
        return getValueMap().toString();
    }

    public Map getSecurityAccessRights() {
        return securityAccessRights;
    }

    public void setSecurityAccessRights(Map securityAccessRights) {
        this.securityAccessRights = securityAccessRights;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * When the session is being invalidated (all value bindings are setted as null),
     * this method is called, so close the session for the current user.
     *
     * @param event the session binding event
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (!isBanned && closeSessionsWhenInvalidate) {
            UserSessionLogUtil.closeSession((Integer) getValue("userId"));
        }

        //remove temporal images
        ImageStoreCmd imageStoreCmd = new ImageStoreCmd();
        imageStoreCmd.putParam("op", "deleteSession");
        imageStoreCmd.putParam("sessionId", event.getSession().getId());
        try {
            BusinessDelegate.i.execute(imageStoreCmd, null);
        } catch (AppLevelException e) {
            log.debug("Error in execute ImageStoreCmd...", e);
        }
    }
}
