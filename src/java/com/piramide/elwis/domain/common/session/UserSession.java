package com.piramide.elwis.domain.common.session;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * User session Local interface
 *
 * @author Fernando Montaño
 * @version $Id: UserSession.java,v 1.1 2004/10/06 21:40:36 fernando Exp
 */


public interface UserSession extends EJBLocalObject {
    Integer getUserId();

    void setUserId(Integer userId);

    String getStatusName();

    void setStatusName(String statusName);

    String getModule();

    void setModule(String module);

    Collection getParams();

    void setParams(Collection params);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
