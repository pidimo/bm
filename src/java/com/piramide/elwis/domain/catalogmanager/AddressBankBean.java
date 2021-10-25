package com.piramide.elwis.domain.catalogmanager;

/**
 * @author Ivan
 * @version $Id: AddressBankBean.java 1843 2004-07-11 18:57:09Z fernando ${NAME}.java, v 2.0 28-abr-2004 11:58:50 Ivan Exp $
 */

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;
import java.util.Date;

public abstract class AddressBankBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setAddressBankId(PKGenerator.i.nextKey(CatalogConstants.TABLE_ADDRESSBANK));
        DTOFactory.i.copyFromDTO(dto, this, false);
        setActive(new Boolean(true));
        setRecordDate(DateUtils.dateToInteger(new Date()));
        setAddressType(ContactConstants.ADDRESSTYPE_ORGANIZATION);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public AddressBankBean() {
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

    public abstract Integer getAddressBankId();

    public abstract void setAddressBankId(Integer addressId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getName();

    public abstract void setName(String name1);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getRecordDate();

    public abstract void setRecordDate(Integer recordDate);

    public abstract String getAddressType();

    public abstract void setAddressType(String addressType);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
