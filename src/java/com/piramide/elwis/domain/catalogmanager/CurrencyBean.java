package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * This Class represents the Currency Entity Bean
 *
 * @author Ivan
 * @version $Id: CurrencyBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class CurrencyBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCurrencyId(PKGenerator.i.nextKey(CatalogConstants.TABLE_CURRENCY));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate() {
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

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract Boolean getIsBasicCurrency();

    public abstract void setIsBasicCurrency(Boolean isBasicCurrency);

    public abstract java.math.BigDecimal getUnit();

    public abstract void setUnit(java.math.BigDecimal unitId);

    public abstract String getCurrencySymbol();

    public abstract void setCurrencySymbol(String symbol);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract String getCurrencyName();

    public abstract void setCurrencyName(String nameId);

    public abstract Currency ejbSelectIsBasicCurrency(Integer companyId) throws FinderException;

    public Currency ejbHomeGetIsBasicCurrency(Integer companyId) throws FinderException {
        return this.ejbSelectIsBasicCurrency(companyId);
    }

    public abstract String getCurrencyLabel();

    public abstract void setCurrencyLabel(String label);
}
