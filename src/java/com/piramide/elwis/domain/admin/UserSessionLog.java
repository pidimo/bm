package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 23, 2005
 * Time: 2:57:24 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserSessionLog extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUserId();

    void setUserId(Integer userId);

    String getIp();

    void setIp(String ip);

    Boolean getIsConnected();

    void setIsConnected(Boolean IsConnected);

    String getSessionId();

    void setSessionId(String sessionid);

    Long getConnectionDateTime();

    void setConnectionDateTime(Long conectiondatetime);

    Long getEndConnectionDateTime();

    void setEndConnectionDateTime(Long endconectiondatetime);

    Long getLastActionDateTime();

    void setLastActionDateTime(Long lastconectiondatetime);

    Long getLastActionApp();

    void setLastActionApp(Long lastActionApp);
}
