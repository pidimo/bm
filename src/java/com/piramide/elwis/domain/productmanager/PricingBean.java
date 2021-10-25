package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Ernesto
 * @version $Id: PricingBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 23-ago-2004 9:26:07 Ernesto Exp $
 */

public abstract class PricingBean implements EntityBean {

    public PricingPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public PricingBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract Integer getQuantity();

    public abstract void setQuantity(Integer quantity);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
