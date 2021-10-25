/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductUnitBean.java 2301 2004-08-17 20:40:12Z ivan ${NAME}.java, v 2.0 16-ago-2004 17:10:23 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProductUnitBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setUnitId(PKGenerator.i.nextKey(CatalogConstants.TABLE_PRODUCTUNIT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ProductUnitBean() {
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

    public abstract Integer getUnitId();

    public abstract void setUnitId(Integer unitId);

    public abstract String getUnitName();

    public abstract void setUnitName(String unitName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
