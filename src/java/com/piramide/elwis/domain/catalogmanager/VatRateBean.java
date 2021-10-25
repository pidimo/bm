/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: VatRateBean.java 2673 2004-10-05 13:18:27Z ivan ${NAME}.java, v 2.0 18-ago-2004 10:31:12 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class VatRateBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVatrateId(PKGenerator.i.nextKey(CatalogConstants.TABLE_VATRATE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {

    }

    public VatRateBean() {
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

    public abstract Integer getValidFrom();

    public abstract void setValidFrom(Integer validFrom);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract java.math.BigDecimal getVatRate();

    public abstract void setVatRate(java.math.BigDecimal vatRate);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getVatrateId();

    public abstract void setVatrateId(Integer vatrateId);
}
