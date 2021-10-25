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
public abstract class WebParameterBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setWebParameterId(PKGenerator.i.nextKey(CatalogConstants.TABLE_WEBPARAMETER));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public WebParameterBean() {
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

    public abstract String getParameterName();

    public abstract void setParameterName(String parameterName);

    public abstract String getVariableName();

    public abstract void setVariableName(String variableName);

    public abstract Integer getVariableType();

    public abstract void setVariableType(Integer variableType);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getWebDocumentId();

    public abstract void setWebDocumentId(Integer webDocumentId);

    public abstract Integer getWebParameterId();

    public abstract void setWebParameterId(Integer webParameterId);

    public abstract WebDocument getWebDocument();

    public abstract void setWebDocument(WebDocument webDocument);

    public abstract Collection ejbSelectWebParameterIdsByWebDocument(Integer wedDocumentId) throws FinderException;

    public Collection ejbHomeSelectWebParameterIdsByWebDocument(Integer wedDocumentId) throws FinderException {
        return ejbSelectWebParameterIdsByWebDocument(wedDocumentId);
    }

}
