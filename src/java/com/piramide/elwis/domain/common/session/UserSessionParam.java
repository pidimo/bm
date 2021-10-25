package com.piramide.elwis.domain.common.session;

import javax.ejb.EJBLocalObject;

/**
 * UserSessionParam local interface
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionParam.java 9821 2009-10-08 22:07:10Z ivan ${NAME}.java, v 2.0 06-10-2004 04:42:29 PM Fernando Montaño Exp $
 */


public interface UserSessionParam extends EJBLocalObject {
    Integer getUserId();

    void setUserId(Integer userId);

    String getStatusName();

    void setStatusName(String statusName);

    String getModule();

    void setModule(String module);

    String getParamName();

    void setParamName(String paramName);

    String getValue();

    void setValue(String value);

    Integer getType();

    void setType(Integer type);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    UserSession getUserSession();

    void setUserSession(UserSession value);
}
