package com.piramide.elwis.domain.catalogmanager;

/**
 * This Class represents the Bank Entity Bean
 *
 * @author Ivan
 * @version $Id: BankBean.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class BankBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setBankId(PKGenerator.i.nextKey(CatalogConstants.TABLE_BANK));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public BankBean() {
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

    public abstract String getBankCode();

    public abstract void setBankCode(String bankCode);

    public abstract Integer getBankId();

    public abstract void setBankId(Integer bankId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getBankInternationalCode();

    public abstract void setBankInternationalCode(String bic);

    public abstract String getBankName();

    public abstract void setBankName(String nameId);

    public abstract String getBankLabel();

    public abstract void setBankLabel(String label);

    public abstract AddressBank getAddress();

    public abstract void setAddress(AddressBank address);

    public String getName1() {
        if (getAddress() != null) {
            return getAddress().getName();
        } else {
            return getBankName();
        }
    }

    public void setName1(String name) {
        if (getAddress() != null) {
            getAddress().setName(name);
            setBankName(null);
        } else {
            setBankName(name);
        }
    }

    public Boolean getIsAddress() {
        if (getAddress() != null) {
            return new Boolean(true);
        } else {
            return new Boolean(false);
        }
    }

    public void setIsAddress(Boolean test) {
    }
}
