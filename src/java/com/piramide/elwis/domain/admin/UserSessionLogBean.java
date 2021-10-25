package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 23, 2005
 * Time: 2:56:42 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class UserSessionLogBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract String getIp();

    public abstract void setIp(String ip);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract String getSessionId();

    public abstract void setSessionId(String sessionid);

    public abstract Long getConnectionDateTime();

    public abstract void setConnectionDateTime(Long conectiondatetime);

    public abstract Long getEndConnectionDateTime();

    public abstract void setEndConnectionDateTime(Long endconectiondatetime);

    public abstract Long getLastActionDateTime();

    public abstract void setLastActionDateTime(Long lastconectiondatetime);

    public abstract Boolean getIsConnected();

    public abstract void setIsConnected(Boolean isConnected);

    public abstract Collection ejbSelectLoadLoggedUserIds() throws FinderException;

    public Collection ejbHomeSelectLoadLoggedUserIds() throws FinderException {
        return ejbSelectLoadLoggedUserIds();
    }

    public abstract Long getLastActionApp();

    public abstract void setLastActionApp(Long lastActionApp);
}
