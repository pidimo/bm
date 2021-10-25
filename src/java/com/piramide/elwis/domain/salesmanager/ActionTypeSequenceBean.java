/**
 * Jatun S.R.L
 *
 * @author ivan
 *
 */
package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

public abstract class ActionTypeSequenceBean implements javax.ejb.EntityBean {
    EntityContext entityContext;

    public ActionTypeSequencePK ejbCreate(Integer actionTypeId, Integer numberId, Integer companyId) throws CreateException {
        setActionTypeId(actionTypeId);
        setNumberId(numberId);
        setCompanyId(companyId);
        return null;
    }

    public void ejbPostCreate(Integer actionTypeId, Integer numberId, Integer companyId) throws CreateException {

    }

    public ActionTypeSequencePK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ActionTypeSequenceBean() {
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

    public abstract Integer getActionTypeId();

    public abstract void setActionTypeId(Integer actionTypeId);

    public abstract Integer getNumberId();

    public abstract void setNumberId(Integer numberId);
}
