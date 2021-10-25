package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public abstract class WebDocumentBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setWebDocumentId(PKGenerator.i.nextKey(CatalogConstants.TABLE_WEBDOCUMENT));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public WebDocumentBean() {
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

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getWebDocumentId();

    public abstract void setWebDocumentId(Integer webDocumentId);

    public abstract Collection getWebParameters();

    public abstract void setWebParameters(Collection webParameters);
}
