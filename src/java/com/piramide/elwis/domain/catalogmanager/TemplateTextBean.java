/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: TemplateTextBean.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 09-jun-2004 11:19:11 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class TemplateTextBean implements EntityBean {
    EntityContext entityContext;

    public TemplateTextPK ejbCreate(ComponentDTO dto) throws CreateException {
        setLanguageId(new Integer(dto.get("languageId").toString()));
        setTemplateId(new Integer(dto.get("templateId").toString()));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public TemplateTextBean() {
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

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract FreeText getFreeText();

    public abstract void setFreeText(FreeText freeText);

    public abstract Boolean getByDefault();

    public abstract void setByDefault(Boolean byDefault);
}
