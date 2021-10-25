/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductGroupBean.java 2453 2004-08-31 20:22:49Z ivan ${NAME}.java, v 2.0 16-ago-2004 16:54:39 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

public abstract class ProductGroupBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setGroupId(PKGenerator.i.nextKey(CatalogConstants.TABLE_PRODUCTGROUP));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ProductGroupBean() {
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

    public abstract Integer getGroupId();

    public abstract void setGroupId(Integer groupId);

    public abstract String getGroupName();

    public abstract void setGroupName(String groupName);

    public abstract Integer getParentGroupId();

    public abstract void setParentGroupId(Integer parentGroupId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getChildProductGroupList();

    public abstract void setChildProductGroupList(Collection productGroup1);
}
