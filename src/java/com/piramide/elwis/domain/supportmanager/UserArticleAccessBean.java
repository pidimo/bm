package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public abstract class UserArticleAccessBean implements EntityBean {

    EntityContext entityContext;

    public UserArticleAccessPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public UserArticleAccessBean() {
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

    public abstract Integer getArticleId();

    public abstract void setArticleId(Integer articleId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getUserGroupId();

    public abstract void setUserGroupId(Integer userGroupId);

    public abstract Article getArticle();

    public abstract void setArticle(Article article);
}
