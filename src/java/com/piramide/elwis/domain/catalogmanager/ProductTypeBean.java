/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductTypeBean.java 11758 2015-12-09 00:16:33Z miguel ${NAME}.java, v 2.0 16-ago-2004 16:51:27 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProductTypeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTypeId(PKGenerator.i.nextKey(CatalogConstants.TABLE_PRODUCTTYPE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ProductTypeBean() {
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

    public abstract Integer getTypeId();

    public abstract void setTypeId(Integer typeId);

    public abstract String getTypeName();

    public abstract void setTypeName(String typeName);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getProductTypeType();

    public abstract void setProductTypeType(Integer productTypeType);
}
