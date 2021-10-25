package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;
import java.util.Collection;

/**
 * This Class represents the Template Entity Bean
 *
 * @author Ivan
 * @version $Id: TemplateBean.java 9553 2009-08-14 21:46:04Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class TemplateBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setTemplateId(PKGenerator.i.nextKey(CatalogConstants.TABLE_TEMPLATE));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
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

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getTemplateText();

    public abstract void setTemplateText(Collection templateText);

    public abstract Integer getMediaType();

    public abstract void setMediaType(Integer mediaType);
}
