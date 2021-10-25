package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:31:59 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ArticleRelatedBean implements EntityBean {
    EntityContext entityContext;

    public ArticleRelatedPK ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
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


    public abstract Integer getArticleId();

    public abstract void setArticleId(Integer articleId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getRelatedArticleId();

    public abstract void setRelatedArticleId(Integer relatedArticleId);

    /*public abstract Article getArticle();

    public abstract void setArticle(Article article);*/
}
