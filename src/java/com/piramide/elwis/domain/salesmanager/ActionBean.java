package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Action Bean class
 *
 * @author Fernando Montaño
 * @version Exp ActionBean.java, v 2.0 24-01-2005 03:40:08 PM Fernando Montaño Exp $
 */


public abstract class ActionBean implements EntityBean {

    public ActionPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ActionBean() {
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

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getFollowUpDate();

    public abstract void setFollowUpDate(Integer followUpDate);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract java.math.BigDecimal getValue();

    public abstract void setValue(java.math.BigDecimal value);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getActionPositions();

    public abstract void setActionPositions(Collection actionPositions);

    public abstract SalesProcess getSalesProcess();

    public abstract void setSalesProcess(SalesProcess salesProcess);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract String getNumber();

    public abstract void setNumber(String number);

    public abstract Integer ejbSelectMaxActionNumber(Integer companyId, Integer processId) throws FinderException;

    public Integer ejbHomeSelectMaxActionNumber(Integer companyId, Integer processId) throws FinderException {
        return ejbSelectMaxActionNumber(companyId, processId);
    }

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract Integer getNetGross();

    public abstract void setNetGross(Integer netGross);

    public abstract Long getCreateDateTime();

    public abstract void setCreateDateTime(Long createDateTime);

    public abstract Long getUpdateDateTime();

    public abstract void setUpdateDateTime(Long updateDateTime);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);
}
