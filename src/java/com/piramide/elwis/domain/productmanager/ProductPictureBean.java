/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductPictureBean.java 2379 2004-08-23 16:10:24Z ivan ${NAME}.java, v 2.0 23-ago-2004 9:32:36 Ivan Exp $
 */
package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProductPictureBean implements EntityBean {
    EntityContext entityContext;

    public ProductPicturePK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ProductPictureBean() {
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

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract String getProductPictureName();

    public abstract void setProductPictureName(String productPictureName);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getSize();

    public abstract void setSize(Integer size);

    public abstract Integer getUploadDate();

    public abstract void setUploadDate(Integer uploadDate);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
