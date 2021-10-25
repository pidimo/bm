package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public abstract class AddressRelationTypeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setRelationTypeId(PKGenerator.i.nextKey(CatalogConstants.TABLE_ADDRESSRELATIONTYPE));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public AddressRelationTypeBean() {
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

    public abstract Integer getRelationTypeId();

    public abstract void setRelationTypeId(Integer relationTypeId);

    public abstract Integer getRelationType();

    public abstract void setRelationType(Integer relationType);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
