package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: BodyBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}, 02-02-2005 04:30:54 PM alvaro Exp $
 */

public abstract class BodyBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setBodyId(PKGenerator.i.nextKey(WebMailConstants.TABLE_BODY));
        return null;
    }

    public Integer ejbCreate(byte[] bodyContent, Integer type, Integer companyId) throws CreateException {
        this.setBodyId(PKGenerator.i.nextKey(WebMailConstants.TABLE_BODY));
        this.setBodyContent(bodyContent);
        this.setBodyType(type);
        this.setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(byte[] bodyContent, Integer type, Integer companyId) throws CreateException {
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public BodyBean() {
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

    public abstract Integer getBodyId();

    public abstract void setBodyId(Integer bodyId);

    public abstract Integer getBodyType();

    public abstract void setBodyType(Integer bodyType);

    public abstract byte[] getBodyContent();

    public abstract void setBodyContent(byte[] bodyBody);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer bodyCompanyId);
}
