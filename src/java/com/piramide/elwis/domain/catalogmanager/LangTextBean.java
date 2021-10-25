package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.*;

/**
 * This Class represents the LangText Entity Bean
 *
 * @author yumi
 * @version $Id: LangTextBean.java 8456 2008-09-02 14:19:25Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class LangTextBean implements EntityBean {
    EntityContext entityContext;

    public LangTextPK ejbCreate(ComponentDTO dto) throws CreateException {
        setLangTextId(PKGenerator.i.nextKey(CatalogConstants.TABLE_LANGTEXT));
        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public LangTextBean() {
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

    public abstract Integer getLangTextId();

    public abstract void setLangTextId(Integer langTextId);

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract String getText();

    public abstract void setText(String text);

    public String getLanguageName() throws FinderException {
        LanguageHome languagehome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        Language language = languagehome.findByPrimaryKey(getLanguageId());
        return language.getLanguageName();
    }

    public abstract String getType();

    public abstract void setType(String type);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract Language getLanguage();

    public abstract void setLanguage(Language language);
}
