package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionTypeBean.java 12682 2017-05-22 22:58:15Z miguel ${NAME}.java, v 2.0 14-01-2005 10:41:22 AM Fernando Montaño Exp $
 */


public abstract class ActionTypeBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setActionTypeId(PKGenerator.i.nextKey(SalesConstants.TABLE_ACTIONTYPE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ActionTypeBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getActionTypeId();

    public abstract void setActionTypeId(Integer actionTypeId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getProbability();

    public abstract void setProbability(Integer probability);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract String getActionTypeName();

    public abstract void setActionTypeName(String actionTypeName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);
}
