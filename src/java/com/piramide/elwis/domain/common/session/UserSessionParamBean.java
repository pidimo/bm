package com.piramide.elwis.domain.common.session;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

/**
 * UserSessionParam Entiy Bean
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionParamBean.java 9821 2009-10-08 22:07:10Z ivan ${NAME}.java, v 2.0 06-10-2004 04:42:23 PM Fernando Montaño Exp $
 */


public abstract class UserSessionParamBean implements EntityBean {
    EntityContext entityContext;

    public UserSessionParamPK ejbCreate(ComponentDTO dto) throws CreateException {
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public UserSessionParamBean() {
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

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract String getStatusName();

    public abstract void setStatusName(String statusName);

    public abstract String getModule();

    public abstract void setModule(String module);

    public abstract String getParamName();

    public abstract void setParamName(String paramName);

    public abstract String getValue();

    public abstract void setValue(String value);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract UserSession getUserSession();

    public abstract void setUserSession(UserSession value);
}
